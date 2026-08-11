package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.collection.ListUtil;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * IPV4地址工具类.
 *
 * <p>pr自：https://gitee.com/loolly/hutool/pulls/161</p>
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
     * IPv4正则表达式（内联自 hutool PatternPool.IPV4）.
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\."
            + "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\."
            + "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\."
            + "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)$");

    /**
     * 本地IP地址.
     */
    public static final String LOCAL_IP = "127.0.0.1";

    /**
     * IP段的分割符.
     */
    public static final String IP_SPLIT_MARK = ".";

    /**
     * IP掩码的分割符.
     */
    public static final String IP_MASK_SPLIT_MARK = "/";
    /**
     * IPv4 地址位数.
     */
    private static final int IPV4_BIT_LENGTH = 32;
    /**
     * 每个段的位数.
     */
    private static final int SEGMENT_BIT_LENGTH = 8;
    /**
     * 字节掩码.
     */
    private static final int BYTE_MASK = 0xFF;

    /**
     * 将IP地址转换为二进制文本.
     *
     * @param ipv4 IP地址
     * @return 二进制文本
     */
    public static String toBin(final String ipv4) {
        if (!isIpv4(ipv4)) {
            throw new IllegalArgumentException(
                    "Invalid IPv4 address: " + ipv4);
        }
        final String[] split = StrUtil.splitToArray(ipv4, '.');
        final StringBuilder sb = new StringBuilder();
        for (final String s : split) {
            final String bin = Integer.toBinaryString(Integer.parseInt(s));
            sb.append(StrUtil.fillBefore(bin, '0', SEGMENT_BIT_LENGTH));
        }
        return sb.toString();
    }

    /**
     * 将二进制文本转换为IP地址.
     *
     * @param bin 二进制文本
     * @return IP地址
     */
    public static String binToIpv4(final String bin) {
        if (bin.length() != IPV4_BIT_LENGTH) {
            throw new IllegalArgumentException(
                    "Invalid binary length: " + bin.length());
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            final String part = bin.substring(i * 8, (i + 1) * 8);
            sb.append(Integer.parseInt(part, 2));
            if (i < 3) {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    /**
     * 校验是否为有效的IPv4地址.
     *
     * @param ipv4 IPv4地址
     * @return 是否有效
     */
    public static boolean isIpv4(final String ipv4) {
        if (StrUtil.isBlank(ipv4)) {
            return false;
        }
        return IPV4_PATTERN.matcher(ipv4.trim()).matches();
    }

    /**
     * 将IP地址转换为long.
     *
     * @param ipv4 IP地址
     * @return long值
     */
    public static long ipv4ToLong(final String ipv4) {
        if (!isIpv4(ipv4)) {
            throw new IllegalArgumentException(
                    "Invalid IPv4 address: " + ipv4);
        }
        final String[] split = StrUtil.splitToArray(ipv4, '.');
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << SEGMENT_BIT_LENGTH) | Integer.parseInt(split[i]);
        }
        return result;
    }

    /**
     * 将long转换为IP地址.
     *
     * @param longIP long值
     * @return IP地址
     */
    public static String longToIpv4(final long longIP) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            sb.append((longIP >> (i * SEGMENT_BIT_LENGTH)) & BYTE_MASK);
            if (i > 0) {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    /**
     * 根据掩码位获取子网掩码.
     *
     * @param maskBit 掩码位
     * @return 子网掩码
     */
    public static String getMaskByMaskBit(final int maskBit) {
        return MaskBit.get(maskBit);
    }

    /**
     * 根据子网掩码获取掩码位.
     *
     * @param mask 子网掩码
     * @return 掩码位
     */
    public static Integer getMaskBitByMask(final String mask) {
        return MaskBit.getMaskBit(mask);
    }

    /**
     * 获取指定IP的起始IP和结束IP（CIDR表示法）.
     *
     * @param ipv4 IP地址
     * @param mask 子网掩码
     * @return IP列表 [起始IP, 结束IP]
     */
    public static List<String> getBeginEndIp(final String ipv4,
            final String mask) {
        final long ipLong = ipv4ToLong(ipv4);
        final long maskLong = ipv4ToLong(mask);
        final long begin = ipLong & maskLong;
        final long end = begin | (~maskLong);
        return ListUtil.of(longToIpv4(begin), longToIpv4(end));
    }

    /**
     * 获取指定IP的起始IP和结束IP（掩码位表示法）.
     *
     * @param ipv4    IP地址
     * @param maskBit 掩码位
     * @return IP列表 [起始IP, 结束IP]
     */
    public static List<String> getBeginEndIp(final String ipv4,
            final int maskBit) {
        return getBeginEndIp(ipv4, MaskBit.get(maskBit));
    }

    /**
     * 判断IP是否在CIDR规则内.
     *
     * @param ipv4 IP地址
     * @param cidr CIDR规则
     * @return 是否在范围内
     */
    public static boolean isMaskMatch(final String ipv4, final String cidr) {
        final int idx = cidr.indexOf(IP_MASK_SPLIT_MARK);
        final String ip = cidr.substring(0, idx);
        final int maskBit = Integer.parseInt(
                cidr.substring(idx + 1));
        return isMaskMatch(ipv4, ip, maskBit);
    }

    /**
     * 判断IP是否在掩码范围内.
     *
     * @param ipv4    IP地址
     * @param maskIP  子网IP
     * @param maskBit 掩码位
     * @return 是否在范围内
     */
    public static boolean isMaskMatch(final String ipv4, final String maskIP,
            final int maskBit) {
        final long ipLong = ipv4ToLong(ipv4);
        final long maskIpLong = ipv4ToLong(maskIP);
        final long mask = getMaskLong(maskBit);
        return (ipLong & mask) == (maskIpLong & mask);
    }

    /**
     * 根据掩码位获取掩码的long值.
     *
     * @param maskBit 掩码位
     * @return 掩码的long值
     */
    private static long getMaskLong(final int maskBit) {
        if (maskBit == 0) {
            return 0;
        }
        return (~0L) << (IPV4_BIT_LENGTH - maskBit);
    }

    /**
     * 判断是否为内网IP.
     *
     * @param ipv4 IP地址
     * @return 是否为内网IP
     */
    public static boolean isInnerIP(final String ipv4) {
        return internalIp(ipv4ToLong(ipv4));
    }

    /**
     * 判断long形式的IP是否为内网IP.
     *
     * @param ipLong long形式的IP
     * @return 是否为内网IP
     */
    public static boolean internalIp(final long ipLong) {
        // 10.0.0.0~10.255.255.255 A类
        if (ipLong >= ipv4ToLong("10.0.0.0")
                && ipLong <= ipv4ToLong("10.255.255.255")) {
            return true;
        }
        // 172.16.0.0~172.31.255.255 B类
        if (ipLong >= ipv4ToLong("172.16.0.0")
                && ipLong <= ipv4ToLong("172.31.255.255")) {
            return true;
        }
        // 192.168.0.0~192.168.255.255 C类
        if (ipLong >= ipv4ToLong("192.168.0.0")
                && ipLong <= ipv4ToLong("192.168.255.255")) {
            return true;
        }
        // 127.0.0.0~127.255.255.255 环回
        if (ipLong >= ipv4ToLong("127.0.0.0")
                && ipLong <= ipv4ToLong("127.255.255.255")) {
            return true;
        }
        return false;
    }

    /**
     * 根据IP地址列表获取子网掩码位.
     *
     * @param ipv4s IP地址列表
     * @return 子网掩码位
     */
    public static int getMaskBit(final List<String> ipv4s) {
        Assert.notEmpty(ipv4s, "IP list must not be empty!");
        final long[] ipLongs = new long[ipv4s.size()];
        for (int i = 0; i < ipv4s.size(); i++) {
            ipLongs[i] = ipv4ToLong(ipv4s.get(i));
        }
        long mask = 0;
        for (int i = 0; i < ipLongs.length - 1; i++) {
            mask |= ipLongs[i] ^ ipLongs[i + 1];
        }
        return countBit(mask);
    }

    /**
     * 计算掩码位（从最高位开始连续0的个数）.
     *
     * @param mask 掩码
     * @return 掩码位
     */
    private static int countBit(final long mask) {
        int bit = 0;
        long m = mask;
        while (m != 0) {
            m = m >> 1;
            bit++;
        }
        return IPV4_BIT_LENGTH - bit;
    }

    /**
     * 统计IP地址列表.
     *
     * @param ipv4   IP地址
     * @param maskBit 掩码位
     * @return IP地址列表
     */
    public static List<String> list(final String ipv4, final int maskBit) {
        final long ipLong = ipv4ToLong(ipv4);
        final long mask = getMaskLong(maskBit);
        final long begin = ipLong & mask;
        final long end = begin | (~mask);
        final List<String> ips = new ArrayList<>();
        for (long i = begin; i <= end; i++) {
            ips.add(longToIpv4(i));
        }
        return ips;
    }

    /**
     * 获取IP地址的结束地址.
     *
     * @param ipv4   IP地址
     * @param maskBit 掩码位
     * @return 结束地址
     */
    public static String getEndIp(final String ipv4, final int maskBit) {
        final long ipLong = ipv4ToLong(ipv4);
        final long mask = getMaskLong(maskBit);
        final long begin = ipLong & mask;
        return longToIpv4(begin | (~mask));
    }

    /**
     * 获取IP地址的起始地址.
     *
     * @param ipv4   IP地址
     * @param maskBit 掩码位
     * @return 起始地址
     */
    public static String getBeginIp(final String ipv4, final int maskBit) {
        final long ipLong = ipv4ToLong(ipv4);
        final long mask = getMaskLong(maskBit);
        return longToIpv4(ipLong & mask);
    }
}
