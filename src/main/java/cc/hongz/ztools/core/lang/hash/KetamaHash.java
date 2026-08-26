package cc.hongz.ztools.core.lang.hash;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.util.StrUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Ketama算法，用于在一致性Hash中快速定位服务器位置.
 *
 * @author looly
 * @since 5.7.20
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class KetamaHash implements Hash64<String>, Hash32<String> {

    /**
     * 计算 64 位 Hash 值。
     *
     * @param key 键
     * @return 64 位 Hash 值
     */
    @Override
    public long hash64(final String key) {
        byte[] bKey = md5(key);
        return ((long) (bKey[3] & 0xFF) << 24)
                | ((long) (bKey[2] & 0xFF) << 16)
                | ((long) (bKey[1] & 0xFF) << 8)
                | (bKey[0] & 0xFF);
    }

    /**
     * 计算 32 位 Hash 值。
     *
     * @param key 键
     * @return 32 位 Hash 值
     */
    @Override
    public int hash32(final String key) {
        return (int) (hash64(key) & 0xffffffffL);
    }

    /**
     * 计算 Hash 值。
     *
     * @param key 键
     * @return Hash 值
     */
    @Override
    public Number hash(final String key) {
        return hash64(key);
    }

    /**
     * 计算MD5值，使用UTF-8编码.
     *
     * @param key 被计算的键
     * @return MD5值
     */
    private static byte[] md5(final String key) {
        final MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new UtilException("MD5 algorithm not support!", e);
        }
        return md5.digest(StrUtil.utf8Bytes(key));
    }
}
