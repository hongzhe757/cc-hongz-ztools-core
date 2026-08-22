package cc.hongz.ztools.core.codec;

import cc.hongz.ztools.core.lang.Assert;

/**
 * 凯撒密码实现.
 *
 * <p>
 * 算法来自：https://github.com/zhaorenjie110/SymmetricEncryptionAndDecryption
 *
 * @author looly
 */
@SuppressWarnings("checkstyle:MagicNumber")
public final class Caesar {

    /**
     * 26 个字母表.
     */
    public static final String TABLE = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";

    /**
     * 私有构造，禁止实例化.
     */
    private Caesar() {
    }

    /**
     * 传入明文，加密得到密文.
     *
     * @param message 加密的消息
     * @param offset  偏移量
     * @return 加密后的内容
     */
    public static String encode(final String message, final int offset) {
        Assert.notNull(message, "message must be not null!");
        final int len = message.length();
        final char[] plain = message.toCharArray();
        char c;
        for (int i = 0; i < len; i++) {
            c = message.charAt(i);
            if (false == Character.isLetter(c)) {
                continue;
            }
            plain[i] = encodeChar(c, offset);
        }
        return new String(plain);
    }

    /**
     * 传入密文解密到明文.
     *
     * @param cipherText 密文
     * @param offset     偏移量
     * @return 解密后的内容
     */
    public static String decode(final String cipherText, final int offset) {
        Assert.notNull(cipherText, "cipherText must be not null!");
        final int len = cipherText.length();
        final char[] plain = cipherText.toCharArray();
        char c;
        for (int i = 0; i < len; i++) {
            c = cipherText.charAt(i);
            if (false == Character.isLetter(c)) {
                continue;
            }
            plain[i] = decodeChar(c, offset);
        }
        return new String(plain);
    }

    // ----------------------------------------------------------------------------------------- Private method start

    /**
     * 加密轮盘.
     *
     * @param c      被加密字符
     * @param offset 偏移量
     * @return 加密后的字符
     */
    private static char encodeChar(final char c, final int offset) {
        int position = (TABLE.indexOf(c) + offset) % 52;
        return TABLE.charAt(position);
    }

    /**
     * 解密轮盘.
     *
     * @param c      字符
     * @param offset 偏移量
     * @return 解密后的字符
     */
    private static char decodeChar(final char c, final int offset) {
        int position = (TABLE.indexOf(c) - offset) % 52;
        if (position < 0) {
            position += 52;
        }
        return TABLE.charAt(position);
    }
    // ----------------------------------------------------------------------------------------- Private method end
}
