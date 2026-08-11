package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.collection.ListUtil;
import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IPV4地址工具类.
 *
 * @author ZhuKun
 * @since 5.4.1
 */
public final class Ipv4Util {

    /**
     * 私有构造器.
     */
    private Ipv4Util() {
    }

    /**
     * IPv4正则表达式（内联自 PatternPool.IPV4）.
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\."
            + "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\."
            + "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\."
            + "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)$");

    /**
     * 本地IP.
     */
    public static final String LOCAL_IP = "127.0.0.1";

    /**
     * IP段的分割符.
     */
    public static final String IP_SPLIT_MARK = "-";

    /**
     * IP与掩码的分割符.
     */
    public static final String IP_MASK_SPLIT_MARK = StrUtil.SLASH;

    /**
     * 最大掩码位.
     */
    public static final int IP_MASK_MAX = 32;

    /**
     * 格式化IP段.
     *
     * @param ip   IP地址
     * @param mask 掩码
     * @return 返回xxx.xxx.xxx.xxx/mask的格式
     */
    public static String formatIpBlock(final String ip, final String mask) {
        return ip + IP_MASK_SPLIT_MARK + getMaskBitByMask(mask);
    }

    /**
     * 智能转换IP地址集合.
     *
     * @param ipRange IP段，支持X.X.X.X-X.X.X.X或X.X.X.X/X
     * @param isAll   true:全量地址，false:可用地址
     * @return IP集
     */
    public static List<String> list(final String ipRange, final boolean isAll) {
        if (ipRange.contains(IP_SPLIT_MARK)) {
            final String[] range = StrUtil.splitToArray(ipRange, IP_SPLIT_MARK);
            return list(range[0], range[1]);
        } else if (ipRange.contains(IP_MASK_SPLIT_MARK)) {
            final String[] param = StrUtil.splitToArray(ipRange, IP_MASK_SPLIT_MARK);
            return list(param[0], Integer.parseInt(param[1]), isAll);
        } else {
            return ListUtil.toList(ipRange);
        }
    }

    /**
     * 根据IP地址、子网掩码获取IP地址区间.
     *
     * @param ip      IP地址
     * @param maskBit 掩码位，例如24、32
     * @param isAll   true:全量地址，false:可用地址
     * @return 区间地址
     */
    public static List<String> list(final String ip, final int maskBit,
            final boolean isAll) {
        if (maskBit == IP_MASK_MAX) {
            final List<String> list = new ArrayList<>();
            if (isAll) {
                list.add(ip);
            }
            return list;
        }

        String startIp = getBeginIpStr(ip, maskBit);
        String endIp = getEndIpStr(ip, maskBit);
        if (isAll) {
            return list(startIp, endIp);
        }

        int lastDotIndex = startIp.lastIndexOf(CharUtil.DOT) + 1;
        startIp = StrUtil.subPre(startIp, lastDotIndex)
                + (Integer.parseInt(Objects.requireNonNull(
                        StrUtil.subSuf(startIp, lastDotIndex))) + 1);
        lastDotIndex = endIp.lastIndexOf(CharUtil.DOT) + 1;
        endIp = StrUtil.subPre(endIp, lastDotIndex)
                + (Integer.parseInt(Objects.requireNonNull(
                        StrUtil.subSuf(endIp, lastDotIndex))) - 1);
        return list(startIp, endIp);
    }

    /**
     * 得到IP地址区间.
     *
     * @param ipFrom 开始IP
     * @param ipTo   结束IP
     * @return 区间地址
     */
    public static List<String> list(final String ipFrom, final String ipTo) {
        final int count = countByIpRange(ipFrom, ipTo);
        final int[] from = parseIntArray(
                StrUtil.splitToArray(ipFrom, CharUtil.DOT));
        final int[] to = parseIntArray(
                StrUtil.splitToArray(ipTo, CharUtil.DOT));

        final List<String> ips = new ArrayList<>(count);
        boolean aIsStart = true;
        boolean bIsStart = true;
        boolean cIsStart = true;
        boolean aIsEnd;
        boolean bIsEnd;
        boolean cIsEnd;
        final int aEnd = to[0];
        int bEnd;
        int cEnd;
        int dEnd;
        final int maxOctet = 255;
        for (int a = from[0]; a <= aEnd; a++) {
            aIsEnd = (a == aEnd);
            bEnd = aIsEnd ? to[1] : maxOctet;
            for (int b = (aIsStart ? from[1] : 0); b <= bEnd; b++) {
                bIsEnd = aIsEnd && (b == bEnd);
                cEnd = bIsEnd ? to[2] : maxOctet;
                for (int c = (bIsStart ? from[2] : 0); c <= cEnd; c++) {
                    cIsEnd = bIsEnd && (c == cEnd);
                    dEnd = cIsEnd ? to[3] : maxOctet;
                    for (int d = (cIsStart ? from[3] : 0); d <= dEnd; d++) {
                        ips.add(a + "." + b + "." + c + "." + d);
                    }
                    cIsStart = false;
                }
                bIsStart = false;
            }
            aIsStart = false;
        }
        return ips;
    }

    /**
     * 根据long值获取ip v4地址：xx.xx.xx.xx.
     *
     * @param longIP IP的long表示形式
     * @return IP V4 地址
     */
    public static String longToIpv4(final long longIP) {
        final int octetMask = 0xFF;
        final int bit24 = 24;
        final int bit16 = 16;
        final int bit8 = 8;
        final StringBuilder sb = StrUtil.builder();
        sb.append(longIP >> bit24 & octetMask);
        sb.append(CharUtil.DOT);
        sb.append(longIP >> bit16 & octetMask);
        sb.append(CharUtil.DOT);
        sb.append(longIP >> bit8 & octetMask);
        sb.append(CharUtil.DOT);
        sb.append(longIP & octetMask);
        return sb.toString();
    }

    /**
     * 根据ip地址(xxx.xxx.xxx.xxx)计算出long型的数据，方法别名：inet_aton.
     *
     * @param strIP IP V4 地址
     * @return long值
     */
    public static long ipv4ToLong(final String strIP) {
        final Matcher matcher = IPV4_PATTERN.matcher(strIP);
        if (matcher.matches()) {
            return matchAddress(matcher);
        }
        throw new IllegalArgumentException("Invalid IPv4 address!");
    }

    /**
     * 根据ip地址(xxx.xxx.xxx.xxx)计算出long型的数据, 如果格式不正确返回 defaultValue.
     *
     * @param strIP       IP V4 地址
     * @param defaultValue 默认值
     * @return long值
     */
    public static long ipv4ToLong(final String strIP, final long defaultValue) {
        return IPV4_PATTERN.matcher(strIP).matches()
                ? ipv4ToLong(strIP) : defaultValue;
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP（字符串型），方法别名：inet_ntoa.
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的字符串表示
     */
    public static String getBeginIpStr(final String ip, final int maskBit) {
        return longToIpv4(getBeginIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP（Long型）.
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的长整型表示
     */
    public static Long getBeginIpLong(final String ip, final int maskBit) {
        return ipv4ToLong(ip) & ipv4ToLong(getMaskByMaskBit(maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP（字符串型）.
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的字符串表示
     */
    public static String getEndIpStr(final String ip, final int maskBit) {
        return longToIpv4(getEndIpLong(ip, maskBit));
    }

    /**
     * 根据子网掩码转换为掩码位.
     *
     * @param mask 掩码的点分十进制表示，例如 255.255.255.0
     * @return 掩码位，例如 24
     * @throws IllegalArgumentException 子网掩码非法
     */
    public static int getMaskBitByMask(final String mask) {
        final Integer maskBit = MaskBit.getMaskBit(mask);
        if (maskBit == null) {
            throw new IllegalArgumentException("Invalid netmask " + mask);
        }
        return maskBit;
    }

    /**
     * 计算子网大小.
     *
     * @param maskBit 掩码位
     * @param isAll   true:全量地址，false:可用地址
     * @return 地址总数
     */
    public static int countByMaskBit(final int maskBit, final boolean isAll) {
        if ((false == isAll) && (maskBit <= 0 || maskBit >= IP_MASK_MAX)) {
            return 0;
        }

        final int count = (int) Math.pow(2, IP_MASK_MAX - maskBit);
        return isAll ? count : count - 2;
    }

    /**
     * 根据掩码位获取掩码.
     *
     * @param maskBit 掩码位
     * @return 掩码
     */
    public static String getMaskByMaskBit(final int maskBit) {
        return MaskBit.get(maskBit);
    }

    /**
     * 根据开始IP与结束IP计算掩码.
     *
     * @param fromIp 开始IP
     * @param toIp   结束IP
     * @return 掩码x.x.x.x
     */
    public static String getMaskByIpRange(final String fromIp,
            final String toIp) {
        final long from = ipv4ToLong(fromIp);
        final long to = ipv4ToLong(toIp);
        final long xor = from ^ to;
        int maskBit = 0;
        long temp = xor;
        while (temp != 0) {
            temp = temp >> 1;
            maskBit++;
        }
        return getMaskByMaskBit(IP_MASK_MAX - maskBit);
    }

    /**
     * 计算IP区间有多少个IP.
     *
     * @param fromIp 开始IP
     * @param toIp   结束IP
     * @return IP数量
     */
    public static int countByIpRange(final String fromIp, final String toIp) {
        final long from = ipv4ToLong(fromIp);
        final long to = ipv4ToLong(toIp);
        return (int) (to - from + 1);
    }

    /**
     * 检测给定IP是否有效.
     *
     * @param mask 掩码
     * @return 是否有效
     */
    public static boolean isMaskValid(final String mask) {
        try {
            getMaskBitByMask(mask);
            return true;
        } catch (final IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 检测掩码位是否有效.
     *
     * @param maskBit 掩码位
     * @return 是否有效
     */
    public static boolean isMaskBitValid(final int maskBit) {
        return maskBit > 0 && maskBit <= IP_MASK_MAX;
    }

    /**
     * 判定是否为内网IPv4.
     *
     * @param ipAddress IP地址
     * @return 是否为内网IP
     */
    public static boolean isInnerIP(final String ipAddress) {
        return isInner(ipv4ToLong(ipAddress),
                ipv4ToLong("10.0.0.0"), ipv4ToLong("10.255.255.255"))
                || isInner(ipv4ToLong(ipAddress),
                ipv4ToLong("172.16.0.0"), ipv4ToLong("172.31.255.255"))
                || isInner(ipv4ToLong(ipAddress),
                ipv4ToLong("192.168.0.0"), ipv4ToLong("192.168.255.255"))
                || isInner(ipv4ToLong(ipAddress),
                ipv4ToLong("127.0.0.0"), ipv4ToLong("127.255.255.255"));
    }

    /**
     * 判定给定IP是否匹配通配符模式.
     *
     * @param wildcard  通配符，如 192.168.*.1
     * @param ipAddress IP地址
     * @return 是否匹配
     */
    public static boolean matches(final String wildcard,
            final String ipAddress) {
        final String regex = StrUtil.replace(wildcard, ".",
                "\\.").replace("*", "\\d{1,3}");
        return Pattern.matches(regex, ipAddress);
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP（Long型）.
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的长整型表示
     */
    public static Long getEndIpLong(final String ip, final int maskBit) {
        return getBeginIpLong(ip, maskBit)
                + countByMaskBit(maskBit, true) - 1;
    }

    /**
     * 将字符串数组转为int数组.
     *
     * @param strs 字符串数组
     * @return int数组
     */
    private static int[] parseIntArray(final String[] strs) {
        final int[] result = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            result[i] = Integer.parseInt(strs[i]);
        }
        return result;
    }

    /**
     * 根据正则匹配结果计算long值.
     *
     * @param matcher 正则匹配器
     * @return long值
     */
    private static long matchAddress(final Matcher matcher) {
        long addr = 0;
        final int octetBit = 8;
        for (int i = 1; i <= 4; i++) {
            addr = (addr << octetBit) | Integer.parseInt(matcher.group(i));
        }
        return addr;
    }

    /**
     * 判断long形式的IP是否在区间内.
     *
     * @param userIp 用户IP
     * @param begin  起始IP
     * @param end    结束IP
     * @return 是否在区间内
     */
    private static boolean isInner(final long userIp, final long begin,
            final long end) {
        return userIp >= begin && userIp <= end;
    }
}
