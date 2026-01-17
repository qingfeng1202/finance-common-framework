package com.tigercub.commonframework.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * 静态工具类：根据任意字符串 key 获取本地 ReentrantLock
 * 适用于单机环境下的按 ID/账号维度并发控制（如登录、支付等）
 *
 * <p>特性：
 * <ul>
 *   <li>线程安全的锁获取和自动清理</li>
 *   <li>容量保护，防止内存溢出</li>
 *   <li>LRU 风格的访问跟踪</li>
 *   <li>监控和诊断支持</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>{@code
 * String userId = "user123";
 * ReentrantLock lock = StringLocks. get(userId);
 * if (lock.tryLock(5, TimeUnit.SECONDS)) {
 *     try {
 *         // 执行需要互斥的业务逻辑
 *     } finally {
 *         lock.unlock();
 *     }
 * }
 * }</pre>
 */
public class StringLocks {

    private static final Logger LOG = Logger.getLogger(StringLocks.class.getName());
    private static final int DEFAULT_MAX_CAPACITY = 10_000;
    private static final int CLEANUP_THRESHOLD = 9_000; // 90% 时触发清理
    private static final int CLEANUP_BATCH_SIZE = 1_000; // 每次最多清理数量

    /**
     * 增强的锁包装类，支持访问时间跟踪
     */
    private static class TrackedLock {
        final ReentrantLock lock;
        volatile long lastAccessTime;

        TrackedLock() {
            this.lock = new ReentrantLock();
            this.lastAccessTime = System.nanoTime();
        }

        void updateAccessTime() {
            this.lastAccessTime = System.nanoTime();
        }

        boolean isIdle() {
            return !lock.isLocked() && !lock.hasQueuedThreads();
        }
    }

    // 使用内部类实现懒加载 + 线程安全的单例
    private static class Holder {
        static final StringLocks INSTANCE = new StringLocks(DEFAULT_MAX_CAPACITY);
    }

    private final ConcurrentHashMap<String, TrackedLock> lockMap;
    private final int maxCapacity;
    private final int cleanupThreshold;
    private final AtomicInteger cleanupInProgress; // 防止并发清理
    private final ReentrantLock globalCleanupLock;

    private StringLocks(int maxCapacity) {
        this.maxCapacity = maxCapacity > 0 ? maxCapacity : DEFAULT_MAX_CAPACITY;
        this.cleanupThreshold = (int) (this.maxCapacity * 0.9);
        this.lockMap = new ConcurrentHashMap<>(Math.min(maxCapacity / 4, 2048));
        this.cleanupInProgress = new AtomicInteger(0);
        this.globalCleanupLock = new ReentrantLock();
    }

    /**
     * 获取与 key 关联的 ReentrantLock
     * 自动创建或复用，带容量保护和自动清理
     *
     * @param key 锁标识（不能为空或空字符串）
     * @return 对应的 ReentrantLock
     * @throws IllegalArgumentException 如果 key 为 null 或空字符串
     * @throws IllegalStateException    如果系统负载过高且无法清理出空间
     */
    public static ReentrantLock get(String key) {
        validateKey(key);

        StringLocks instance = Holder.INSTANCE;

        // 主动触发清理（非阻塞）
        if (instance.lockMap.size() >= instance.cleanupThreshold) {
            instance.asyncCleanup();
        }

        // 获取或创建锁
        TrackedLock tracked = instance.lockMap.compute(key, (k, existing) -> {
            if (existing != null) {
                existing.updateAccessTime();
                return existing;
            }

            // 严格容量检查
            if (instance.lockMap.size() >= instance.maxCapacity) {
                throw new IllegalStateException(
                        String.format("StringLocks capacity exceeded (%d).  Key: %s.  Consider increasing capacity or check for leaks.",
                                instance.maxCapacity, key)
                );
            }

            return new TrackedLock();
        });

        return tracked.lock;
    }

    /**
     * 验证 key 的有效性
     */
    private static void validateKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty");
        }
        // 可选：限制 key 长度防止内存滥用
        if (key.length() > 256) {
            throw new IllegalArgumentException("Key length must not exceed 256 characters");
        }
    }

    /**
     * 非阻塞的异步清理
     */
    private void asyncCleanup() {
        // 使用 CAS 确保只有一个线程执行清理
        if (!cleanupInProgress.compareAndSet(0, 1)) {
            return; // 已有清理在进行
        }

        try {
            // 使用 tryLock 避免阻塞业务线程
            if (globalCleanupLock.tryLock(10, TimeUnit.MILLISECONDS)) {
                try {
                    performCleanup();
                } finally {
                    globalCleanupLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.warning("Cleanup interrupted: " + e.getMessage());
        } finally {
            cleanupInProgress.set(0);
        }
    }

    /**
     * 执行实际的清理逻辑
     * 优先移除最久未使用的空闲锁
     */
    private void performCleanup() {
        long startTime = System.currentTimeMillis();
        AtomicInteger removed = new AtomicInteger(0);

        // 收集可移除的条目（空闲 + 按时间排序）
        lockMap.entrySet().stream()
                .filter(entry -> entry.getValue().isIdle())
                .sorted((e1, e2) -> Long.compare(e1.getValue().lastAccessTime, e2.getValue().lastAccessTime))
                .limit(CLEANUP_BATCH_SIZE)
                .forEach(entry -> {
                    // 双重检查避免移除正在使用的锁
                    if (entry.getValue().isIdle() && lockMap.remove(entry.getKey(), entry.getValue())) {
                        removed.incrementAndGet();
                    }
                });

        long duration = System.currentTimeMillis() - startTime;
        if (removed.get() > 0) {
            LOG.info(String.format("Cleaned up %d idle locks in %dms.  Current size: %d",
                    removed.get(), duration, lockMap.size()));
        }
    }

    // ===== 监控和诊断 API =====

    /**
     * 获取当前锁池中的锁数量
     */
    public static int currentSize() {
        return Holder.INSTANCE.lockMap.size();
    }

    /**
     * 获取最大容量
     */
    public static int maxCapacity() {
        return Holder.INSTANCE.maxCapacity;
    }

    /**
     * 获取使用率（0. 0 - 1.0）
     */
    public static double utilizationRate() {
        StringLocks instance = Holder.INSTANCE;
        return (double) instance.lockMap.size() / instance.maxCapacity;
    }

    /**
     * 手动触发清理（同步）
     *
     * @return 清理的锁数量
     */
    public static int triggerCleanup() {
        StringLocks instance = Holder.INSTANCE;
        instance.globalCleanupLock.lock();
        try {
            int sizeBefore = instance.lockMap.size();
            instance.performCleanup();
            return sizeBefore - instance.lockMap.size();
        } finally {
            instance.globalCleanupLock.unlock();
        }
    }

    /**
     * 检查指定 key 的锁是否存在
     */
    public static boolean contains(String key) {
        if (key == null || key.isEmpty()) {
            return false;
        }
        return Holder.INSTANCE.lockMap.containsKey(key);
    }

    /**
     * 获取锁的状态信息（用于调试）
     */
    public static String getLockInfo(String key) {
        validateKey(key);
        TrackedLock tracked = Holder.INSTANCE.lockMap.get(key);
        if (tracked == null) {
            return "Lock not found for key: " + key;
        }

        ReentrantLock lock = tracked.lock;
        long idleTime = System.nanoTime() - tracked.lastAccessTime;
        return String.format(
                "Key: %s | Locked: %s | QueueLength: %d | IdleTime: %dms",
                key, lock.isLocked(), lock.getQueueLength(), TimeUnit.NANOSECONDS.toMillis(idleTime)
        );
    }

    /**
     * 清空所有锁（仅用于测试环境）
     * 生产环境慎用！
     */
    public static void clearAllForTest() {
        StringLocks instance = Holder.INSTANCE;
        instance.globalCleanupLock.lock();
        try {
            instance.lockMap.clear();
            LOG.warning("All locks cleared (test mode)");
        } finally {
            instance.globalCleanupLock.unlock();
        }
    }

    // 禁止实例化
    private StringLocks() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

}
