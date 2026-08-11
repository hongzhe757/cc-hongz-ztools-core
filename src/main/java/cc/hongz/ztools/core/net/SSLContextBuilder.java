package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.builder.Builder;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.StrUtil;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * {@link SSLContext}构建器，可以自定义：<br>
 * <ul>
 *     <li>协议（protocol），默认TLS</li>
 *     <li>{@link KeyManager}，默认空</li>
 *     <li>{@link TrustManager}，默认{@link DefaultTrustManager}，即信任全部</li>
 *     <li>{@link SecureRandom}</li>
 * </ul>
 * <p>
 * 构建后可获得{@link SSLContext}，通过调用
 * {@link SSLContext#getSocketFactory()}获取{@link javax.net.ssl.SSLSocketFactory}.
 *
 * @author looly
 * @since 5.5.2
 */
public class SSLContextBuilder implements SSLProtocols, Builder<SSLContext> {
    private static final long serialVersionUID = 1L;

    /**
     * SSL/TLS 协议，默认TLS.
     */
    private String protocol = TLS;
    /**
     * JSSE 密钥管理器.
     */
    private KeyManager[] keyManagers;
    /**
     * 信任管理器，默认信任全部.
     */
    private TrustManager[] trustManagers = {DefaultTrustManager.INSTANCE};
    /**
     * 安全随机数.
     */
    private SecureRandom secureRandom = new SecureRandom();

    /**
     * 创建 SSLContextBuilder.
     *
     * @return SSLContextBuilder
     */
    public static SSLContextBuilder create() {
        return new SSLContextBuilder();
    }

    /**
     * 设置协议，例如TLS等.
     *
     * @param aProtocol 协议
     * @return 自身
     */
    public SSLContextBuilder setProtocol(final String aProtocol) {
        if (StrUtil.isNotBlank(aProtocol)) {
            this.protocol = aProtocol;
        }
        return this;
    }

    /**
     * 设置信任信息.
     *
     * @param newTrustManagers TrustManager列表
     * @return 自身
     */
    public SSLContextBuilder setTrustManagers(
            final TrustManager... newTrustManagers) {
        if (ArrayUtil.isNotEmpty(newTrustManagers)) {
            this.trustManagers = newTrustManagers;
        }
        return this;
    }

    /**
     * 设置 JSSE 密钥管理器.
     *
     * @param newKeyManagers JSSE 密钥管理器
     * @return 自身
     */
    public SSLContextBuilder setKeyManagers(
            final KeyManager... newKeyManagers) {
        if (ArrayUtil.isNotEmpty(newKeyManagers)) {
            this.keyManagers = newKeyManagers;
        }
        return this;
    }

    /**
     * 设置 SecureRandom.
     *
     * @param newSecureRandom SecureRandom
     * @return 自己
     */
    public SSLContextBuilder setSecureRandom(
            final SecureRandom newSecureRandom) {
        if (null != newSecureRandom) {
            this.secureRandom = newSecureRandom;
        }
        return this;
    }

    /**
     * 构建{@link SSLContext}.
     *
     * @return {@link SSLContext}
     */
    @Override
    public SSLContext build() {
        return buildQuietly();
    }

    /**
     * 构建{@link SSLContext}需要处理异常.
     *
     * @return {@link SSLContext}
     * @throws NoSuchAlgorithmException 无此算法异常
     * @throws KeyManagementException   密钥管理异常
     * @since 5.7.22
     */
    public SSLContext buildChecked() throws NoSuchAlgorithmException,
            KeyManagementException {
        final SSLContext sslContext = SSLContext.getInstance(protocol);
        sslContext.init(this.keyManagers, this.trustManagers,
                this.secureRandom);
        return sslContext;
    }

    /**
     * 构建{@link SSLContext}.
     *
     * @return {@link SSLContext}
     * @throws IORuntimeException 包装 GeneralSecurityException异常
     */
    public SSLContext buildQuietly() throws IORuntimeException {
        try {
            return buildChecked();
        } catch (final GeneralSecurityException e) {
            throw new IORuntimeException(e);
        }
    }
}
