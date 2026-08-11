package cc.hongz.ztools.core.net;

/**
 * SSL或TLS协议.
 *
 * @author looly
 * @since 5.7.8
 */
public interface SSLProtocols {

    /**
     * 支持某些版本的SSL，可能也支持其他版本.
     */
    String SSL = "SSL";
    /**
     * 支持SSL版本2或更高版本，可能也支持其他版本.
     */
    String SSLv2 = "SSLv2";
    /**
     * 支持SSL版本3，可能也支持其他版本.
     */
    String SSLv3 = "SSLv3";

    /**
     * 支持某些版本的TLS，可能也支持其他版本.
     */
    String TLS = "TLS";
    /**
     * 支持 RFC 2246: TLS 版本 1.0，可能也支持其他版本.
     */
    String TLSv1 = "TLSv1";
    /**
     * 支持 RFC 4346: TLS 版本 1.1，可能也支持其他版本.
     */
    String TLSv11 = "TLSv1.1";
    /**
     * 支持 RFC 5246: TLS 版本 1.2，可能也支持其他版本.
     */
    String TLSv12 = "TLSv1.2";
}
