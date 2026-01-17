package com.tigercub.commonframework.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Argon2 密码工具类（基于 argon2-jvm）
 * <p>
 * 使用建议：
 * - 存储 hash 字符串（encoded）到用户表，例如 "$argon2id$v=19$m=65536,t=4,p=2$..."
 * - 校验时使用 verify(hashed, passwordChars)
 * - 若 needsRehash 返回 true，则在用户下次成功登录时重新生成 hash
 * </p>
 *
 * @author qingfeng
 * @since 2025/10/10
 */
public class PasswordUtil {

    // 推荐的安全参数（可根据服务端内存/并发压力进行调整）
    private static final int ITERATIONS = 4;
    /**
     * memory in KB (65536 KB = 64 MB)
     */
    private static final int MEMORY_KB = 65536;
    private static final int PARALLELISM = 2;

    // 正则用于解析 encoded string 中的参数 m=...,t=...,p=...
    private static final Pattern PARAM_PATTERN = Pattern.compile("m=(\\d+),t=(\\d+),p=(\\d+)");

    private PasswordUtil() { /* utility */ }

    /**
     * 使用当前配置生成 Argon2id 哈希（encoded string）
     * 注意：调用后应调用 clearPassword(password) 清除明文数组
     *
     * @param password 明文密码（char 数组）
     * @return Argon2 encoded hash 字符串，包含盐与参数，直接存储即可
     */
    public static String hash(char[] password) {
        if (password == null || password.length == 0) {
            throw new IllegalArgumentException("password must not be null or empty");
        }

        // Argon2 实现会自动生成随机 salt 并把 salt 和参数封装到 encoded 字符串中
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        try {
            return argon2.hash(ITERATIONS, MEMORY_KB, PARALLELISM, password);
        } finally {
            argon2.wipeArray(password); // 清理明文密码
        }
    }

    /**
     * 验证明文密码是否与存储的 encoded hash 匹配
     * 验证完成后可调用 clearPassword(password) 清除明文
     *
     * @param encodedHash 存储的 Argon2 encoded hash（数据库存储）
     * @param password    明文密码（char 数组）
     * @return true if matches
     */
    public static boolean verify(String encodedHash, char[] password) {
        if (encodedHash == null || encodedHash.isEmpty()) {
            throw new IllegalArgumentException("encodedHash must not be null or empty");
        }
        if (password == null) {
            return false;
        }

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        try {
            return argon2.verify(encodedHash, password);
        } finally {
            argon2.wipeArray(password);
        }
    }

    /**
     * 判断已存储的 hash 是否需要重新哈希（例如参数升级后使用）
     * 解析 encodedHash 中的 m,t,p 并与当前配置比较，若低于当前安全策略则返回 true。
     *
     * @param encodedHash 存储的 Argon2 encoded hash
     * @return true if rehash is recommended
     */
    public static boolean needsRehash(String encodedHash) {
        if (encodedHash == null || encodedHash.isEmpty()) {
            throw new IllegalArgumentException("encodedHash must not be null or empty");
        }

        Matcher m = PARAM_PATTERN.matcher(encodedHash);
        if (!m.find()) {
            // 无法解析参数时，建议重新哈希
            return true;
        }

        try {
            int mem = Integer.parseInt(m.group(1)); // m
            int iter = Integer.parseInt(m.group(2)); // t
            int par = Integer.parseInt(m.group(3)); // p

            // 若任一参数低于当前配置，则推荐 rehash
            return mem < MEMORY_KB || iter < ITERATIONS || par < PARALLELISM;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * 清除密码数组（覆盖填充），以降低明文密码在堆上残留风险
     *
     * @param password password char array
     */
    public static void clearPassword(char[] password) {
        if (password == null) return;
        Arrays.fill(password, '\0');
    }

    /**
     * 简单演示（仅作示例，生产环境请写单元测试）
     */
    public static void main(String[] args) {
        char[] pwd = "123456".toCharArray();
        try {
            String hashed = PasswordUtil.hash(pwd);
            System.out.println("hashed = " + hashed);

            // 验证
            char[] tryPwd = "123456".toCharArray();
            boolean ok = PasswordUtil.verify(hashed, tryPwd);
            System.out.println("verify ok = " + ok);
            clearPassword(tryPwd);

            // 是否需要 rehash（比如你升级了参数）
            System.out.println("needsRehash = " + PasswordUtil.needsRehash(hashed));
        } finally {
            // 最后清空明文
            PasswordUtil.clearPassword(pwd);
        }
    }

}
