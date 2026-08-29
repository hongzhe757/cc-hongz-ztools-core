package cc.hongz.ztools.core.net.url;

import cc.hongz.ztools.core.codec.PercentCodec;
import cc.hongz.ztools.core.map.MapUtil;
import cc.hongz.ztools.core.map.TableMap;
import cc.hongz.ztools.core.net.FormUrlencoded;
import cc.hongz.ztools.core.net.RFC3986;
import cc.hongz.ztools.core.net.URLDecoder;
import cc.hongz.ztools.core.util.StrUtil;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * URL中查询字符串部分的封装.
 *
 * @author looly
 * @since 5.3.1
 */
public class UrlQuery {

    /**
     * 查询键值对映射.
     */
    private final TableMap<CharSequence, CharSequence> query;
    /**
     * 是否为x-www-form-urlencoded模式，此模式下空格会编码为'+'.
     */
    private final boolean isFormUrlEncoded;
    /**
     * 是否严格模式，严格模式下，query的name和value中均不允许有分隔符.
     */
    private boolean isStrict;

    /**
     * 构建UrlQuery.
     *
     * @param queryMap 初始化的查询键值对
     * @return UrlQuery
     */
    public static UrlQuery of(
            final Map<? extends CharSequence, ?> queryMap) {
        return new UrlQuery(queryMap);
    }

    /**
     * 构建UrlQuery.
     *
     * @param queryMap         初始化的查询键值对
     * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式
     * @return UrlQuery
     */
    public static UrlQuery of(
            final Map<? extends CharSequence, ?> queryMap,
            final boolean isFormUrlEncoded) {
        return new UrlQuery(queryMap, isFormUrlEncoded);
    }

    /**
     * 构建UrlQuery.
     *
     * @param queryStr 初始化的查询字符串
     * @param charset  decode用的编码，null表示不做decode
     * @return UrlQuery
     */
    public static UrlQuery of(final String queryStr, final Charset charset) {
        return of(queryStr, charset, true);
    }

    /**
     * 构建UrlQuery.
     *
     * @param queryStr       初始化的查询字符串
     * @param charset        decode用的编码，null表示不做decode
     * @param autoRemovePath 是否自动去除path部分
     * @return UrlQuery
     * @since 5.5.8
     */
    public static UrlQuery of(final String queryStr, final Charset charset,
            final boolean autoRemovePath) {
        return of(queryStr, charset, autoRemovePath, false);
    }

    /**
     * 构建UrlQuery.
     *
     * @param queryStr         初始化的查询字符串
     * @param charset          decode用的编码，null表示不做decode
     * @param autoRemovePath   是否自动去除path部分
     * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式
     * @return UrlQuery
     * @since 5.7.16
     */
    public static UrlQuery of(final String queryStr, final Charset charset,
            final boolean autoRemovePath, final boolean isFormUrlEncoded) {
        return new UrlQuery(isFormUrlEncoded).parse(queryStr, charset,
                autoRemovePath);
    }

    /**
     * 构造.
     */
    public UrlQuery() {
        this(null);
    }

    /**
     * 构造.
     *
     * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式
     * @since 5.7.16
     */
    public UrlQuery(final boolean isFormUrlEncoded) {
        this(null, isFormUrlEncoded);
    }

    /**
     * 构造.
     *
     * @param queryMap 初始化的查询键值对
     */
    public UrlQuery(final Map<? extends CharSequence, ?> queryMap) {
        this(queryMap, false);
    }

    /**
     * 构造.
     *
     * @param queryMap         初始化的查询键值对
     * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式
     * @since 5.7.16
     */
    public UrlQuery(final Map<? extends CharSequence, ?> queryMap,
            final boolean isFormUrlEncoded) {
        if (MapUtil.isNotEmpty(queryMap)) {
            query = new TableMap<>(queryMap.size());
            addAll(queryMap);
        } else {
            query = new TableMap<>(MapUtil.DEFAULT_INITIAL_CAPACITY);
        }
        this.isFormUrlEncoded = isFormUrlEncoded;
    }

    /**
     * 设置是否严格模式.
     *
     * @param strict 是否严格模式
     * @return this
     * @since 5.8.20
     */
    public UrlQuery setStrict(final boolean strict) {
        isStrict = strict;
        return this;
    }

    /**
     * 增加键值对.
     *
     * @param key   键
     * @param value 值，集合和数组转换为逗号分隔形式
     * @return this
     */
    public UrlQuery add(final CharSequence key, final Object value) {
        this.query.put(key, toStr(value));
        return this;
    }

    /**
     * 批量增加键值对.
     *
     * @param queryMap query中的键值对
     * @return this
     */
    public UrlQuery addAll(
            final Map<? extends CharSequence, ?> queryMap) {
        if (MapUtil.isNotEmpty(queryMap)) {
            queryMap.forEach(this::add);
        }
        return this;
    }

    /**
     * 移除键及对应所有的值.
     *
     * @param key 键
     * @return this
     * @since 5.8.30
     */
    public UrlQuery remove(final CharSequence key) {
        this.query.remove(key);
        return this;
    }

    /**
     * 解析URL中的查询字符串.
     *
     * @param queryStr 查询字符串，类似于key1=v1&amp;key2=&amp;key3=v3
     * @param charset  decode编码，null表示不做decode
     * @return this
     */
    public UrlQuery parse(final String queryStr, final Charset charset) {
        return parse(queryStr, charset, true);
    }

    /**
     * 解析URL中的查询字符串.
     *
     * @param queryStr       查询字符串
     * @param charset        decode编码，null表示不做decode
     * @param autoRemovePath 是否自动去除path部分
     * @return this
     * @since 5.5.8
     */
    public UrlQuery parse(final String queryStr, final Charset charset,
            final boolean autoRemovePath) {
        if (StrUtil.isBlank(queryStr)) {
            return this;
        }

        String effectiveQueryStr = queryStr;
        if (autoRemovePath) {
            int pathEndPos = effectiveQueryStr.indexOf('?');
            if (pathEndPos > -1) {
                effectiveQueryStr = StrUtil.subSuf(effectiveQueryStr, pathEndPos + 1);
                if (StrUtil.isBlank(effectiveQueryStr)) {
                    return this;
                }
            } else if (StrUtil.startWith(effectiveQueryStr, "http://")
                    || StrUtil.startWith(effectiveQueryStr, "https://")) {
                return this;
            }
        }

        return doParse(effectiveQueryStr, charset);
    }

    /**
     * 获得查询的Map.
     *
     * @return 查询的Map，只读
     */
    public Map<CharSequence, CharSequence> getQueryMap() {
        return MapUtil.unmodifiable(this.query);
    }

    /**
     * 获取查询值.
     *
     * @param key 键
     * @return 值
     */
    public CharSequence get(final CharSequence key) {
        if (MapUtil.isEmpty(this.query)) {
            return null;
        }
        return this.query.get(key);
    }

    /**
     * 构建URL查询字符串.
     *
     * @param charset encode编码，null表示不做encode编码
     * @return URL查询字符串
     */
    public String build(final Charset charset) {
        return build(charset, true);
    }

    /**
     * 构建URL查询字符串.
     *
     * @param charset       encode编码，null表示不做encode编码
     * @param encodePercent 是否编码`%`
     * @return URL查询字符串
     */
    public String build(final Charset charset, final boolean encodePercent) {
        if (isFormUrlEncoded) {
            return build(FormUrlencoded.ALL, FormUrlencoded.ALL, charset,
                    encodePercent);
        }

        if (isStrict) {
            return build(RFC3986.QUERY_PARAM_NAME_STRICT,
                    RFC3986.QUERY_PARAM_VALUE_STRICT, charset, encodePercent);
        }
        return build(RFC3986.QUERY_PARAM_NAME, RFC3986.QUERY_PARAM_VALUE,
                charset, encodePercent);
    }

    /**
     * 构建URL查询字符串.
     *
     * @param keyCoder   键值对中键的编码器
     * @param valueCoder 键值对中值的编码器
     * @param charset    encode编码，null表示不做encode编码
     * @return URL查询字符串
     * @since 5.7.16
     */
    public String build(final PercentCodec keyCoder,
            final PercentCodec valueCoder, final Charset charset) {
        return build(keyCoder, valueCoder, charset, true);
    }

    /**
     * 构建URL查询字符串.
     *
     * @param keyCoder      键值对中键的编码器
     * @param valueCoder    键值对中值的编码器
     * @param charset       encode编码，null表示不做encode编码
     * @param encodePercent 是否编码`%`
     * @return URL查询字符串
     * @since 5.8.0
     */
    public String build(final PercentCodec keyCoder,
            final PercentCodec valueCoder, final Charset charset,
            final boolean encodePercent) {
        if (MapUtil.isEmpty(this.query)) {
            return StrUtil.EMPTY;
        }

        final char[] safeChars = encodePercent ? null : new char[]{'%'};
        final StringBuilder sb = new StringBuilder();
        CharSequence name;
        CharSequence value;
        for (final Map.Entry<CharSequence, CharSequence> entry :
                this.query.entrySet()) {
            name = entry.getKey();
            if (null != name) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(keyCoder.encode(name, charset, safeChars));
                value = entry.getValue();
                if (null != value) {
                    sb.append("=").append(
                            valueCoder.encode(value, charset, safeChars));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 生成查询字符串.<br>
     * 此方法不对任何特殊字符编码，仅用于输出显示.
     *
     * @return 查询字符串
     */
    @Override
    public String toString() {
        return build(null);
    }

    /**
     * 解析URL中的查询字符串.
     * 规则见：https://url.spec.whatwg.org/#urlencoded-parsing
     *
     * @param queryStr 查询字符串
     * @param charset  decode编码，null表示不做decode
     * @return this
     * @since 5.5.8
     */
    private UrlQuery doParse(final String queryStr, final Charset charset) {
        final int len = queryStr.length();
        String name = null;
        int pos = 0;
        int i;
        char c;
        for (i = 0; i < len; i++) {
            c = queryStr.charAt(i);
            switch (c) {
                case '=':
                    if (null == name) {
                        name = queryStr.substring(pos, i);
                        pos = i + 1;
                    }
                    break;
                case '&':
                    addParam(name, queryStr.substring(pos, i), charset);
                    name = null;
                    final int ampEnd = i + 5;
                    if (i + 4 < len
                            && "amp;".equals(queryStr.substring(i + 1,
                                    ampEnd))) {
                        i += 4;
                    }
                    pos = i + 1;
                    break;
                default:
                    break;
            }
        }

        addParam(name, queryStr.substring(pos, i), charset);

        return this;
    }

    /**
     * 对象转换为字符串，用于URL的Query中.
     *
     * @param value 值
     * @return 字符串
     */
    private static String toStr(final Object value) {
        String result;
        if (value instanceof Iterable) {
            final StringBuilder sb = new StringBuilder();
            final Iterable<?> iterable = (Iterable<?>) value;
            boolean first = true;
            for (final Object item : iterable) {
                if (item == null) {
                    continue;
                }
                if (!first) {
                    sb.append(",");
                } else {
                    first = false;
                }
                sb.append(item);
            }
            result = sb.toString();
        } else if (value instanceof Iterator) {
            final StringBuilder sb = new StringBuilder();
            final Iterator<?> iterator = (Iterator<?>) value;
            boolean first = true;
            while (iterator.hasNext()) {
                final Object item = iterator.next();
                if (item == null) {
                    continue;
                }
                if (!first) {
                    sb.append(",");
                } else {
                    first = false;
                }
                sb.append(item);
            }
            result = sb.toString();
        } else {
            result = value == null ? null : value.toString();
        }
        return result;
    }

    /**
     * 将键值对加入到Map中.
     *
     * @param key     key，为null则value作为key
     * @param value   value，为null且key不为null时传入""
     * @param charset 编码
     */
    private void addParam(final String key, final String value,
            final Charset charset) {
        if (null != key) {
            final String actualKey = URLDecoder.decode(key, charset,
                    isFormUrlEncoded);
            this.query.put(actualKey,
                    StrUtil.nullToEmpty(URLDecoder.decode(value, charset,
                            isFormUrlEncoded)));
        } else if (null != value) {
            this.query.put(URLDecoder.decode(value, charset,
                    isFormUrlEncoded), null);
        }
    }
}
