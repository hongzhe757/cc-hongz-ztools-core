package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.codec.PercentCodec;

/**
 * <a href="https://www.ietf.org/rfc/rfc3986.html">RFC3986</a> 编码实现. 定义见：
 * <a href="https://www.ietf.org/rfc/rfc3986.html#appendix-A">RFC3986 Appendix A</a>
 *
 * <pre>
 * 编码层级关系：
 *   UNRESERVED  = ALPHA / DIGIT / "-" / "." / "_" / "~"
 *   SUB_DELIMS  = "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
 *   PCHAR       = UNRESERVED + SUB_DELIMS + ":" + "@"
 *   SEGMENT     = PCHAR                                  （不含 "/"）
 *   PATH        = SEGMENT + "/"                          （= PCHAR + "/"）
 *   QUERY       = PATH + "?"                              （= PCHAR + "/" + "?"）
 *   FRAGMENT    = QUERY                                  （同 QUERY）
 * </pre>
 *
 * @author looly
 * @since 5.7.16
 */
public final class RFC3986 {

    /**
     * gen-delims = ":" / "/" / "?" / "#" / "[" / "]" / "@".
     */
    public static final PercentCodec GEN_DELIMS = PercentCodec.of(":/?#[]@");

    /**
     * sub-delims = "!" / "$" / "{@code &}" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "=".
     */
    public static final PercentCodec SUB_DELIMS = PercentCodec.of("!$&'()*+,;=");

    /**
     * 保留字符（reserved） = gen-delims / sub-delims. 参考RFC3986 Section 2.2.
     */
    public static final PercentCodec RESERVED = GEN_DELIMS.orNew(SUB_DELIMS);

    /**
     * 非保留字符（unreserved） = ALPHA / DIGIT / "-" / "." / "_" / "~". 参考RFC3986 Section 2.3.
     */
    public static final PercentCodec UNRESERVED = PercentCodec.of(unreservedChars());

    /**
     * pchar = unreserved / pct-encoded / sub-delims / ":" / "@".
     */
    public static final PercentCodec PCHAR = UNRESERVED.orNew(SUB_DELIMS)
            .or(PercentCodec.of(":@"));

    /**
     * segment = pchar. 参考RFC3986 Section 3.3.
     */
    public static final PercentCodec SEGMENT = PCHAR;

    /**
     * segment-nz-nc = SEGMENT（不包含冒号 ":"）.
     */
    public static final PercentCodec SEGMENT_NZ_NC =
            PercentCodec.of(SEGMENT).removeSafe(':');

    /**
     * path = segment / "/".
     */
    public static final PercentCodec PATH =
            SEGMENT.orNew(PercentCodec.of("/"));

    /**
     * query = pchar / "/" / "?".
     */
    public static final PercentCodec QUERY =
            PCHAR.orNew(PercentCodec.of("/?"));

    /**
     * fragment = pchar / "/" / "?".
     */
    public static final PercentCodec FRAGMENT = QUERY;

    /**
     * query中的value.<br>
     * value不能包含"{@code &}"，可以包含 "=".
     */
    public static final PercentCodec QUERY_PARAM_VALUE =
            PercentCodec.of(QUERY).removeSafe('&');

    /**
     * query中的value编码器，严格模式，value中不能包含任何分隔符.
     */
    public static final PercentCodec QUERY_PARAM_VALUE_STRICT = UNRESERVED;

    /**
     * query中的key.<br>
     * key不能包含"{@code &}" 和 "=".
     */
    public static final PercentCodec QUERY_PARAM_NAME =
            PercentCodec.of(QUERY_PARAM_VALUE).removeSafe('=');

    /**
     * query中的key编码器，严格模式，key中不能包含任何分隔符.
     */
    public static final PercentCodec QUERY_PARAM_NAME_STRICT = UNRESERVED;

    private RFC3986() {
    }

    /**
     * unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~".
     *
     * @return unreserved字符
     */
    private static StringBuilder unreservedChars() {
        StringBuilder sb = new StringBuilder();

        // ALPHA
        for (char c = 'A'; c <= 'Z'; c++) {
            sb.append(c);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            sb.append(c);
        }

        // DIGIT
        for (char c = '0'; c <= '9'; c++) {
            sb.append(c);
        }

        // "-" / "." / "_" / "~"
        sb.append("_.-~");

        return sb;
    }
}
