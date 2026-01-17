package com.tigercub.commonframework.util;



import com.tigercub.commonframework.model.constant.DateFormatConstant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * <p>
 * 日期时间工具类
 * </p>
 *
 * @author qingfeng
 * @since 2025/12/2
 */
public class DateTimeUtils {

    /**
     * 获取当前日期（格式：yyyy-MM-dd）
     *
     * @return 当前日期字符串
     */
    public static String getCurrentDate() {
        return formatDate(LocalDate.now(), DateFormatConstant.YYYY_MM_DD);
    }

    /**
     * 获取当前日期时间
     *
     * @param pattern 日期格式
     * @return 格式化后的日期时间字符串
     */
    public static String formatDate(String pattern) {
        return formatDateTime(LocalDateTime.now(), pattern);
    }

    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return 格式化后的日期字符串
     */
    public static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return 格式化后的日期字符串
     */
    public static String formatDate(Date date, String pattern) {
        LocalDate localDate = DateTimeUtils.toLocalDateTime(date).toLocalDate();
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期时间
     *
     * @param dateTime 日期时间
     * @param pattern  日期格式
     * @return 格式化后的日期时间字符串
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前时间戳(秒)
     *
     * @return 当前时间戳（秒）
     */
    public static Long getCurrentTimestampInSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前时间戳(毫秒)
     *
     * @return 当前时间戳（毫秒）
     */
    public static Long getCurrentTimestampInMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 在指定日期上增加指定月数
     *
     * @param date   日期
     * @param months 月数
     * @return 增加后的日期
     */
    public static Date addMonths(Date date, int months) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        LocalDateTime newDateTime = localDateTime.plusMonths(months);
        return toDate(newDateTime);
    }

    /**
     * 在指定日期上增加指定天数
     *
     * @param date 日期
     * @param days 天数
     * @return 增加后的日期
     */
    public static Date addDays(Date date, int days) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        LocalDateTime newDateTime = localDateTime.plusDays(days);
        return toDate(newDateTime);
    }

    /**
     * 在指定日期上增加指定小时数
     *
     * @param date  日期
     * @param hours 小时数
     * @return 增加后的日期
     */
    public static Date addHours(Date date, int hours) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        LocalDateTime newDateTime = localDateTime.plusHours(hours);
        return toDate(newDateTime);
    }

    /**
     * 在指定日期上增加指定分钟数
     *
     * @param date    日期
     * @param minutes 分钟数
     * @return 增加后的日期
     */
    public static Date addMinutes(Date date, int minutes) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        LocalDateTime newDateTime = localDateTime.plusMinutes(minutes);
        return toDate(newDateTime);
    }

    /**
     * 在指定日期上增加指定秒数
     *
     * @param date    日期
     * @param seconds 秒数
     * @return 增加后的日期
     */
    public static Date addSeconds(Date date, int seconds) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        LocalDateTime newDateTime = localDateTime.plusSeconds(seconds);
        return toDate(newDateTime);
    }

    /**
     * 获取当前年的年初时间字符串
     *
     * @return 年末时间
     */
    public static String getYearStartTimeString() {
        return getYearStartTimeString(0, DateFormatConstant.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定偏移年份的年初时间字符串
     *
     * @param yearOffset 年份偏移量
     * @param pattern    日期格式
     * @return 年初时间字符串
     */
    public static String getYearStartTimeString(int yearOffset, String pattern) {
        LocalDateTime localDateTime = getYearStartDateTime(yearOffset);
        return formatDateTime(localDateTime, pattern);
    }

    /**
     * 获取指定偏移年份的年初时间
     *
     * @param yearOffset 年份偏移量
     * @return 年初时间
     */
    public static Date getYearStartTime(int yearOffset) {
        return toDate(getYearStartDateTime(yearOffset));
    }

    /**
     * 获取指定偏移年份的年初LocalDateTime
     *
     * @param yearOffset 年份偏移量
     * @return 年初LocalDateTime
     */
    private static LocalDateTime getYearStartDateTime(int yearOffset) {
        return LocalDateTime.now()
                .plusYears(yearOffset)
                .with(TemporalAdjusters.firstDayOfYear())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取当前年的年末时间字符串
     *
     * @return 年末时间
     */
    public static String getYearEndTimeString() {
        return getYearEndTimeString(0, DateFormatConstant.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定偏移年份的年初时间字符串
     *
     * @param yearOffset 年份偏移量
     * @param pattern    日期格式
     * @return 年初时间字符串
     */
    public static String getYearEndTimeString(int yearOffset, String pattern) {
        LocalDateTime localDateTime = getYearEndDateTime(yearOffset);
        return formatDateTime(localDateTime, pattern);
    }

    /**
     * 获取指定偏移年份的年末时间
     *
     * @param yearOffset 年份偏移量
     * @return 年末时间
     */
    public static Date getYearEndTime(int yearOffset) {
        LocalDateTime localDateTime = getYearEndDateTime(yearOffset);
        return toDate(localDateTime);
    }

    /**
     * 获取指定偏移年份的年初LocalDateTime
     *
     * @param yearOffset 年份偏移量
     * @return 年初LocalDateTime
     */
    private static LocalDateTime getYearEndDateTime(int yearOffset) {
        return LocalDateTime.now()
                .plusYears(yearOffset)
                .with(TemporalAdjusters.lastDayOfYear())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

    /**
     * 获取指定偏移月份的月初时间
     *
     * @param monthOffset 月份偏移量
     * @return 月初时间
     */
    public static Date getMonthStartTime(int monthOffset) {
        LocalDateTime localDateTime = LocalDateTime.now()
                .plusMonths(monthOffset)
                .with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        return toDate(localDateTime);
    }

    /**
     * 获取指定偏移月份的月末时间
     *
     * @param monthOffset 月份偏移量
     * @return 月末时间
     */
    public static Date getMonthEndTime(int monthOffset) {
        LocalDateTime localDateTime = LocalDateTime.now()
                .plusMonths(monthOffset)
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);

        return toDate(localDateTime);
    }

    /**
     * 获取今天的开始时间
     *
     * @return 今天的开始时间
     */
    public static Date getTodayStartTime() {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return toDate(localDateTime);
    }

    /**
     * 获取今天的结束时间
     *
     * @return 今天的结束时间
     */
    public static Date getTodayEndTime() {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return toDate(localDateTime);
    }

    /**
     * 计算两个日期之间的天数差
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 天数差
     */
    public static long getDaysBetween(Date startDate, Date endDate) {
        LocalDate start = toLocalDateTime(startDate).toLocalDate();
        LocalDate end = toLocalDateTime(endDate).toLocalDate();
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 计算两个日期之间的月数差
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 月数差
     */
    public static long getMonthsBetween(Date startDate, Date endDate) {
        LocalDate start = toLocalDateTime(startDate).toLocalDate();
        LocalDate end = toLocalDateTime(endDate).toLocalDate();
        return ChronoUnit.MONTHS.between(start, end);
    }

    /**
     * 计算两个日期之间的年数差
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 年数差
     */
    public static long getYearsBetween(Date startDate, Date endDate) {
        LocalDate start = toLocalDateTime(startDate).toLocalDate();
        LocalDate end = toLocalDateTime(endDate).toLocalDate();
        return ChronoUnit.YEARS.between(start, end);
    }

    /**
     * 解析日期字符串为Date对象
     *
     * @param dateString 日期字符串
     * @param pattern    日期格式
     * @return Date对象
     */
    public static Date parseDate(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        // 判断日期格式是否只包含日期部分（不包含时间部分）
        if (!pattern.contains("H") && !pattern.contains("h") &&
                !pattern.contains("m") && !pattern.contains("s") &&
                !pattern.contains("S") && !pattern.contains("a")) {
            // 只包含日期部分，使用LocalDate解析
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return toDate(localDate.atStartOfDay());
        } else {
            // 包含时间部分，使用LocalDateTime解析
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            return toDate(dateTime);
        }
    }

    /**
     * 判断日期是否在指定范围内
     *
     * @param date      需要判断的日期
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 是否在范围内
     */
    public static boolean isDateInRange(Date date, Date startDate, Date endDate) {
        return !date.before(startDate) && !date.after(endDate);
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 是否是同一天
     */
    public static boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = toLocalDateTime(date1).toLocalDate();
        LocalDate localDate2 = toLocalDateTime(date2).toLocalDate();
        return localDate1.isEqual(localDate2);
    }

    /**
     * 将Date转换为LocalDateTime
     *
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将LocalDateTime转换为Date
     *
     * @param localDateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
