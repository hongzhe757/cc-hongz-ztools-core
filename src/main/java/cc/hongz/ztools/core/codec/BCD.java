package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.lang.Assert;

/**
 * BCD 码（Binary-Coded Decimal）亦称二进码十进数或二-十进制代码.
 *
 * <p>
 * BCD 码这种编码形式利用了四个位元来储存一个十进制的数码，使二进制和十进制之间的转换得以快捷的进行。
 * see http://cuisuqiang.iteye.com/blog/1429956
 *
 * @author Looly
 * @deprecated 由于对于 ASCII 的编码解码有缺陷，且这种 BCD 实现并不规范，因此会在 6.0.0 中移除
 */
@Deprecated
@SuppressWarnings("checkstyle:MagicNumber")
public final class BCD {

    /**
     * 私有构造，禁止实例化.
     */
    private BCD() {
    }

    /**
     * 字符串转 BCD 码.
     *
     * @param asc ASCII 字符串
     * @return BCD
     */
    public static byte[] strToBcd(final String asc) {
        Assert.notNull(asc, "ASCII must not be null!");
        String target = asc;
        int len = target.length();
        int mod = len % 2;
        if (mod != 0) {
            target = "0" + target;
            len = target.length();
        }
        byte[] abt;
        if (len >= 2) {
            len >>= 1;
        }
        byte[] bbt;
        bbt = new byte[len];
        abt = target.getBytes();
        int j;
        int k;
        for (int p = 0; p < target.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * ASCII 转 BCD.
     *
     * @param ascii ASCII byte 数组
     * @return BCD
     */
    public static byte[] ascToBcd(final byte[] ascii) {
        Assert.notNull(ascii, "Ascii must be not null!");
        return ascToBcd(ascii, ascii.length);
    }

    /**
     * ASCII 转 BCD.
     *
     * @param ascii     ASCII byte 数组
     * @param ascLength 长度
     * @return BCD
     */
    public static byte[] ascToBcd(final byte[] ascii, final int ascLength) {
        Assert.notNull(ascii, "Ascii must be not null!");
        byte[] bcd = new byte[ascLength / 2];
        int j = 0;
        for (int i = 0; i < (ascLength + 1) / 2; i++) {
            bcd[i] = ascToBcd(ascii[j++]);
            bcd[i] = (byte) (((j >= ascLength) ? 0x00 : ascToBcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    /**
     * BCD 转 ASCII 字符串.
     *
     * @param bytes BCD byte 数组
     * @return ASCII 字符串
     */
    public static String bcdToStr(final byte[] bytes) {
        Assert.notNull(bytes, "Bcd bytes must be not null!");
        char[] temp = new char[bytes.length * 2];
        char val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    //----------------------------------------------------------------- Private method start

    /**
     * 转换单个 byte 为 BCD.
     *
     * @param asc ACSII
     * @return BCD
     */
    private static byte ascToBcd(final byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9')) {
            bcd = (byte) (asc - '0');
        } else if ((asc >= 'A') && (asc <= 'F')) {
            bcd = (byte) (asc - 'A' + 10);
        } else if ((asc >= 'a') && (asc <= 'f')) {
            bcd = (byte) (asc - 'a' + 10);
        } else {
            bcd = (byte) (asc - 48);
        }
        return bcd;
    }
    //----------------------------------------------------------------- Private method end
}
