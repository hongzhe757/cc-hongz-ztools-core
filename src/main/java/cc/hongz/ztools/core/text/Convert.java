package cc.hongz.ztools.core.text;

import cc.hongz.ztools.core.util.CharsetUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Set;

/**
 * 类型转换器.
 *
 * @author hongzhe.qin
 */
public final class Convert {

    private Convert() {

    }

    /**
     * 将值转换为目标类型（阶段 1 Segment 依赖，仅支持常见数值类型）.
     *
     * @param type  目标类型
     * @param value 值
     * @param <T>   目标泛型
     * @return 转换结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(final Type type, final Object value) {
        if (null == value) {
            return null;
        }
        if (!(type instanceof Class)) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        final Class<?> clazz = (Class<?>) type;
        if (clazz == Integer.class || clazz == int.class) {
            return (T) Integer.valueOf(((Number) value).intValue());
        }
        if (clazz == Long.class || clazz == long.class) {
            return (T) Long.valueOf(((Number) value).longValue());
        }
        if (clazz == Short.class || clazz == short.class) {
            return (T) Short.valueOf(((Number) value).shortValue());
        }
        if (clazz == Byte.class || clazz == byte.class) {
            return (T) Byte.valueOf(((Number) value).byteValue());
        }
        if (clazz == Double.class || clazz == double.class) {
            return (T) Double.valueOf(((Number) value).doubleValue());
        }
        if (clazz == Float.class || clazz == float.class) {
            return (T) Float.valueOf(((Number) value).floatValue());
        }
        if (clazz == BigDecimal.class) {
            return (T) toBigDecimal(value);
        }
        if (clazz == BigInteger.class) {
            return (T) toBigInteger(value);
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    /**
     * 转换为字符串.<br>
     * 如果给定的值为null，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static String toStr(final Object value,
                               final String defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    /**
     * 转换为字符串.<br>
     * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static String toStr(final Object value) {
        return toStr(value, null);
    }

    /**
     * 转换为字符.<br>
     * 如果给定的值为null，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Character toChar(final Object value,
                                   final Character defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof Character) {
            return (Character) value;
        }

        final String valueStr = toStr(value, null);
        return StrUtil.isEmpty(valueStr)
                ? defaultValue : valueStr.charAt(0);
    }

    /**
     * 转换为字符.<br>
     * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Character toChar(final Object value) {
        return toChar(value, null);
    }

    /**
     * 转换为byte.<br>
     * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Byte toByte(final Object value, final Byte defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Byte) {
            return (Byte) value;
        }
        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }
        final String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为byte.<br>
     * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Byte toByte(final Object value) {
        return toByte(value, null);
    }

    /**
     * 转换为Short.<br>
     * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Short toShort(final Object value,
                                final Short defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Short) {
            return (Short) value;
        }
        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }
        final String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Short.parseShort(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为Short.<br>
     * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Short toShort(final Object value) {
        return toShort(value, null);
    }

    /**
     * 转换为Number.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Number toNumber(final Object value,
                                  final Number defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return (Number) value;
        }
        final String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return NumberFormat.getInstance().parse(valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为Number.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Number toNumber(final Object value) {
        return toNumber(value, null);
    }

    /**
     * 转换为int.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Integer toInt(final Object value,
                                final Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        final String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为int.<br>
     * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Integer toInt(final Object value) {
        return toInt(value, null);
    }

    /**
     * 转换为Integer数组.<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(final String str) {
        return toIntArray(",", str);
    }

    /**
     * 转换为Long数组.<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Long[] toLongArray(final String str) {
        return toLongArray(",", str);
    }

    /**
     * 转换为Integer数组.<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(final String split,
                                       final String str) {
        if (StrUtil.isEmpty(str)) {
            return new Integer[]{};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Integer v = toInt(arr[i], 0);
            ints[i] = v;
        }
        return ints;
    }

    /**
     * 转换为Long数组.<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Long[] toLongArray(final String split,
                                     final String str) {
        if (StrUtil.isEmpty(str)) {
            return new Long[]{};
        }
        String[] arr = str.split(split);
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Long v = toLong(arr[i], null);
            longs[i] = v;
        }
        return longs;
    }

    /**
     * 转换为String数组.<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static String[] toStrArray(final String str) {
        return toStrArray(",", str);
    }

    /**
     * 转换为String数组.<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static String[] toStrArray(final String split,
                                      final String str) {
        return str.split(split);
    }

    /**
     * 转换为long.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Long toLong(final Object value,
                              final Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        final String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return new BigDecimal(valueStr.trim()).longValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为long.<br>
     * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Long toLong(final Object value) {
        return toLong(value, null);
    }

    /**
     * 转换为double.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Double toDouble(final Object value,
                                  final Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        final String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return new BigDecimal(valueStr.trim()).doubleValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为double.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Double toDouble(final Object value) {
        return toDouble(value, null);
    }

    /**
     * 转换为Float.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Float toFloat(final Object value,
                                final Float defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        final String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为Float.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Float toFloat(final Object value) {
        return toFloat(value, null);
    }

    /**
     * 转换为boolean.<br>
     * String支持的值为：true、false、yes、ok、no，1,0 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Boolean toBool(final Object value,
                                 final Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        valueStr = valueStr.trim().toLowerCase();
        switch (valueStr) {
            case "true":
                return true;
            case "false":
                return false;
            case "yes":
                return true;
            case "ok":
                return true;
            case "no":
                return false;
            case "1":
                return true;
            case "0":
                return false;
            default:
                return defaultValue;
        }
    }

    /**
     * 转换为boolean.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static Boolean toBool(final Object value) {
        return toBool(value, null);
    }

    /**
     * 转换为Enum对象.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     *
     * @param <E>          泛型对象
     * @param clazz        Enum的Class
     * @param value        值
     * @param defaultValue 默认值
     * @return Enum
     */
    public static <E extends Enum<E>> E toEnum(final Class<E> clazz,
                                               final Object value,
                                               final E defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (clazz.isAssignableFrom(value.getClass())) {
            @SuppressWarnings("unchecked")
            E myE = (E) value;
            return myE;
        }
        final String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Enum.valueOf(clazz, valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为Enum对象.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
     *
     * @param <E>   泛型对象
     * @param clazz Enum的Class
     * @param value 值
     * @return Enum
     */
    public static <E extends Enum<E>> E toEnum(final Class<E> clazz,
                                               final Object value) {
        return toEnum(clazz, value, null);
    }

    /**
     * 转换为BigInteger.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static BigInteger toBigInteger(final Object value,
                                          final BigInteger defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        if (value instanceof Long) {
            return BigInteger.valueOf((Long) value);
        }
        final String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return new BigInteger(valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为BigInteger.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static BigInteger toBigInteger(final Object value) {
        return toBigInteger(value, null);
    }

    /**
     * 转换为BigDecimal.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static BigDecimal toBigDecimal(final Object value,
                                          final BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Long) {
            return new BigDecimal((Long) value);
        }
        if (value instanceof Double) {
            return BigDecimal.valueOf((Double) value);
        }
        if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        }
        final String valueStr = toStr(value, null);
        if (StrUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return new BigDecimal(valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为BigDecimal.<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    public static BigDecimal toBigDecimal(final Object value) {
        return toBigDecimal(value, null);
    }

    /**
     * 将对象转为字符串.<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String utf8Str(final Object obj) {
        return str(obj, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 将对象转为字符串.<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj         对象
     * @param charsetName 字符集
     * @return 字符串
     */
    public static String str(final Object obj, final String charsetName) {
        return str(obj, Charset.forName(charsetName));
    }

    /**
     * 将对象转为字符串.<br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj     对象
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(final Object obj, final Charset charset) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[] || obj instanceof Byte[]) {
            if (obj instanceof byte[]) {
                return str((byte[]) obj, charset);
            } else {
                Byte[] bytes = (Byte[]) obj;
                int length = bytes.length;
                byte[] dest = new byte[length];
                for (int i = 0; i < length; i++) {
                    Arrays.copyOf(new byte[]{dest[i]}, bytes[i]);
                }
                return str(dest, charset);
            }
        } else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer) obj, charset);
        }
        return obj.toString();
    }

    /**
     * 将byte数组转为字符串.
     *
     * @param bytes   byte数组
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(final byte[] bytes,
                             final String charset) {
        return str(bytes, StrUtil.isEmpty(charset)
                ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 解码字节码.
     *
     * @param data    字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(final byte[] data,
                             final Charset charset) {
        if (data == null) {
            return null;
        }

        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * 将编码的byteBuffer数据转换为字符串.
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(final ByteBuffer data,
                             final String charset) {
        if (data == null) {
            return null;
        }

        return str(data, Charset.forName(charset));
    }

    /**
     * 将编码的byteBuffer数据转换为字符串.
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(final ByteBuffer data,
                             final Charset charset) {
        Charset charsetTemp = charset;
        if (null == charsetTemp) {
            charsetTemp = Charset.defaultCharset();
        }
        return charsetTemp.decode(data).toString();
    }

    /**
     * 半角转全角.
     *
     * @param input String.
     * @return 全角字符串.
     */
    public static String toSBC(final String input) {
        return toSBC(input, null);
    }

    /**
     * 半角转全角.
     *
     * @param input         String
     * @param notConvertSet 不替换的字符集合
     * @return 全角字符串.
     */
    public static String toSBC(final String input,
                               final Set<Character> notConvertSet) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (null != notConvertSet && notConvertSet.contains(c[i])) {
                continue;
            }

            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * 全角转半角.
     *
     * @param input String.
     * @return 半角字符串
     */
    public static String toDBC(final String input) {
        return toDBC(input, null);
    }

    /**
     * 替换全角为半角.
     *
     * @param text          文本
     * @param notConvertSet 不替换的字符集合
     * @return 替换后的字符
     */
    public static String toDBC(final String text,
                               final Set<Character> notConvertSet) {
        char[] c = text.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (null != notConvertSet && notConvertSet.contains(c[i])) {
                continue;
            }

            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 数字金额大写转换 先写个完整的然后将如零拾替换成零.
     *
     * @param n 数字
     * @return 中文大写数字
     */
    public static String digitUppercase(final double n) {
        double nTemp = n;
        String[] fraction = {"角", "分"};
        String[] digit = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String[][] unit = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};

        String head = nTemp < 0 ? "负" : "";

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < fraction.length; i++) {
            s.append(s).append((digit[(int) (Math.floor(n * 10
                    * Math.pow(10, i)) % 10)]
                    + fraction[i]).replaceAll("(零.)+", ""));
        }
        if (s.length() < 1) {
            s = new StringBuilder("整");
        }
        int integerPart = (int) Math.floor(n);

        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            StringBuilder p = new StringBuilder();
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p.append(digit[integerPart
                        % 10] + unit[1][j] + p);
                integerPart = integerPart / 10;
            }
            s = new StringBuilder(p.toString().replaceAll("(零).*?零$", "")
                    .replaceAll("^$", "零") + unit[0][i] + s);
        }
        return head + s.toString().replaceAll("(零).*?零元", "元")
                .replaceFirst("(零.)+", "")
                .replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
}
