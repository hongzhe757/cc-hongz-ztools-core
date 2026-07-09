package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * 时间工具类.
 *
 * @author hongzhe.qin
 */
public final class DateUtil {

    private DateUtil() {
    }



    /**
     * yyyy-MM.
     */
    private static final String YYYYMM = "yyyy-MM";

    /**
     * yyyy-MM-dd.
     */
    private static final String YYYYMMDD = "yyyy-MM-dd";

    /**
     * yyyyMMddHHmmss.
     */
    private static final String YYYYMMDDHHMMSS_UNSIGNED = "yyyyMMddHHmmss";

    /**
     * yyyy-MM-dd HH:mm:ss.
     */
    private static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式匹配.
     */
    private static final String[] PARSE_PATTERNS = {
            YYYYMMDD, YYYYMMDDHHMMSS, "yyyy-MM-dd HH:mm", YYYYMM,
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};



    /**
     * 按多种模式解析日期字符串.
     *
     * @param str            日期字符串
     * @param parsePatterns  模式列表
     * @return 日期
     * @throws ParseException 解析失败
     */
    public static Date parseDate(final String str, final String... parsePatterns)
            throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
        ParseException last = null;
        for (final String pattern : parsePatterns) {
            try {
                return new SimpleDateFormat(pattern).parse(str);
            } catch (ParseException e) {
                last = e;
            }
        }
        throw last != null ? last : new ParseException("Unable to parse: " + str, -1);
    }

    /**
     * 获取当前Date型日期.
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd.
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYYMMDD);
    }

    /**
     * 获取时间.
     *
     * @return String
     */
    public static final String getTime() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    /**
     * 获取现在时间.
     *
     * @return String
     */
    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS_UNSIGNED);
    }

    /**
     * 获取现在时间.
     *
     * @param format 转换格式
     * @return String
     */
    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    /**
     * 获取现在时间.
     *
     * @param date 日期
     * @return String
     */
    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYYMMDD, date);
    }

    /**
     * Date类型时间转为字符串.
     *
     * @param format 字符串格式
     * @param date   时间
     * @return 时间
     */
    public static final String parseDateToStr(final String format,
                                              final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 格式化时间.
     *
     * @param mss
     * @return 格式化完成后
     */
    public static String formatLongTime(final long mss) {

        String dateTimes = null;
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / 60;
        long seconds = mss % 60;

        dateTimes = String.format("%05d:", hours)
                + String.format("%02d:", minutes)
                + String.format("%02d", seconds);
        return dateTimes;
    }

    /**
     * 时间.
     *
     * @param format 字符串
     * @param ts     时间字符串
     * @return Date 时间
     */
    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new UtilException(e.getMessage());
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08.
     *
     * @return 时间
     */
    public static final String datePath() {
        Date now = new Date();
        return parseDateToStr("yyyy/MM/dd", now);
    }

    /**
     * 日期路径 即年/月/日 如20180808.
     *
     * @return 时间
     */
    public static final String dateTime() {
        Date now = new Date();
        return parseDateToStr("yyyyMMdd", now);
    }

    /**
     * 日期型字符串转化为日期 格式.
     *
     * @param strs 时间
     * @return 时间
     */
    public static Date parseDate(final Object strs) {
        if (strs == null) {
            return null;
        }
        try {
            return parseDate(strs.toString(), PARSE_PATTERNS);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间.
     *
     * @return 服务器启动时间.
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差.
     *
     * @param endDate 结束时间
     * @param nowDate 现在时间
     * @return 时间差
     */
    public static String getDatePoor(final Date endDate, final Date nowDate) {
        long nd = Long.valueOf(1000) * Long.valueOf(24)
                * Long.valueOf(60) * Long.valueOf(60);
        long nh = Long.valueOf(1000) * Long.valueOf(60)
                * Long.valueOf(60);
        long nm = Long.valueOf(1000) * Long.valueOf(60);
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        return day + "天" + hour + "小时" + min + "分钟";
    }
}
