package cc.hongz.ztools.core.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Boolean 类型相关工具类.
 *
 * <p>提供 Boolean 值的判断、取反、类型转换、字符串映射以及逻辑运算等功能。
 * 支持中英文等多种字符串形式的布尔值识别。
 *
 * <p>设计模式：工具类（Utility Class），私有构造禁止实例化。
 *
 * @author looly
 * @author hongzhe.qin (enhanced)
 * @since 4.1.16
 */
public final class BooleanUtil {

    /**
     * 表示为 "真" 的字符串集合.
     * <p>包含英文（true, yes, y, t, ok, correct, success, on, 1）、
     * 中文（是, 对, 真, 對, 正确, 开, 开启）以及符号（√, ☑）。
     */
    private static final Set<String> TRUE_SET = new HashSet<>(Arrays.asList(
            "true", "yes", "y", "t", "ok", "correct", "success", "on", "1",
            "是", "对", "真", "對", "正确", "开", "开启", "√", "☑"
    ));

    /**
     * 表示为 "假" 的字符串集合.
     * <p>包含英文（false, no, n, f, wrong, fail, off, 0）、
     * 中文（否, 错, 假, 錯, 错误, 关, 关闭）以及符号（×, ☒）。
     */
    private static final Set<String> FALSE_SET = new HashSet<>(Arrays.asList(
            "false", "no", "n", "f", "wrong", "fail", "off", "0",
            "否", "错", "假", "錯", "错误", "关", "关闭", "×", "☒"
    ));

    /**
     * 私有构造，禁止实例化.
     */
    private BooleanUtil() {
    }

    // ---------------------------------------------------------------------- negate

    /**
     * 对 {@link Boolean} 包装类型取反.
     *
     * <pre>
     *   BooleanUtil.negate(Boolean.TRUE)  = Boolean.FALSE
     *   BooleanUtil.negate(Boolean.FALSE) = Boolean.TRUE
     *   BooleanUtil.negate(null)          = null
     * </pre>
     *
     * @param bool Boolean 包装值，可以为 {@code null}
     * @return 相反的 Boolean 值；入参为 {@code null} 时返回 {@code null}
     */
    public static Boolean negate(final Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * 对 {@code boolean} 基本类型取反.
     *
     * <pre>
     *   BooleanUtil.negate(true)  = false
     *   BooleanUtil.negate(false) = true
     * </pre>
     *
     * @param bool boolean 基本类型值
     * @return 相反的 boolean 值
     */
    public static boolean negate(final boolean bool) {
        return !bool;
    }

    // ---------------------------------------------------------------------- isTrue / isFalse

    /**
     * 检查 {@link Boolean} 包装值是否为 {@code true}.
     *
     * <p>安全处理 {@code null}，不会抛出 {@link NullPointerException}。
     *
     * <pre>
     *   BooleanUtil.isTrue(Boolean.TRUE)  = true
     *   BooleanUtil.isTrue(Boolean.FALSE) = false
     *   BooleanUtil.isTrue(null)          = false
     * </pre>
     *
     * @param bool 被检查的 Boolean 包装值，可以为 {@code null}
     * @return 当值为 {@code true} 且非 {@code null} 时返回 {@code true}
     */
    public static boolean isTrue(final Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }

    /**
     * 检查 {@link Boolean} 包装值是否为 {@code false}.
     *
     * <p>安全处理 {@code null}，不会抛出 {@link NullPointerException}。
     *
     * <pre>
     *   BooleanUtil.isFalse(Boolean.TRUE)  = false
     *   BooleanUtil.isFalse(Boolean.FALSE) = true
     *   BooleanUtil.isFalse(null)          = false
     * </pre>
     *
     * @param bool 被检查的 Boolean 包装值，可以为 {@code null}
     * @return 当值为 {@code false} 且非 {@code null} 时返回 {@code true}
     */
    public static boolean isFalse(final Boolean bool) {
        return Boolean.FALSE.equals(bool);
    }

    // ---------------------------------------------------------------------- toBoolean

    /**
     * 将字符串转换为 {@code boolean} 基本类型.
     *
     * <p>识别 {@link #TRUE_SET} 中的字符串为 {@code true}（忽略大小写、去除首尾空白），
     * 其余情况（包括 {@code null}、空白字符串）均返回 {@code false}。
     *
     * <pre>
     *   BooleanUtil.toBoolean("true")  = true
     *   BooleanUtil.toBoolean("yes")   = true
     *   BooleanUtil.toBoolean("是")    = true
     *   BooleanUtil.toBoolean("false") = false
     *   BooleanUtil.toBoolean(null)    = false
     *   BooleanUtil.toBoolean("")      = false
     * </pre>
     *
     * @param valueStr 字符串，可以为 {@code null}
     * @return 字符串匹配真值集合时返回 {@code true}，否则返回 {@code false}
     */
    public static boolean toBoolean(final String valueStr) {
        if (StrUtil.isNotBlank(valueStr)) {
            return TRUE_SET.contains(valueStr.trim().toLowerCase());
        }
        return false;
    }

    /**
     * 将字符串转换为 {@link Boolean} 包装类型（三态）.
     *
     * <p>如果字符串匹配 {@link #TRUE_SET}，返回 {@code Boolean.TRUE}；
     * 如果匹配 {@link #FALSE_SET}，返回 {@code Boolean.FALSE}；
     * 其余情况（包括 {@code null}、空白字符串）返回 {@code null}。
     *
     * <pre>
     *   BooleanUtil.toBooleanObject("true")  = Boolean.TRUE
     *   BooleanUtil.toBooleanObject("false") = Boolean.FALSE
     *   BooleanUtil.toBooleanObject("是")    = Boolean.TRUE
     *   BooleanUtil.toBooleanObject("否")    = Boolean.FALSE
     *   BooleanUtil.toBooleanObject("abc")   = null
     *   BooleanUtil.toBooleanObject(null)    = null
     * </pre>
     *
     * @param valueStr 字符串，可以为 {@code null}
     * @return {@link Boolean#TRUE}、{@link Boolean#FALSE} 或 {@code null}
     * @since 5.8.1
     */
    public static Boolean toBooleanObject(final String valueStr) {
        if (StrUtil.isNotBlank(valueStr)) {
            final String normalized = valueStr.trim().toLowerCase();
            if (TRUE_SET.contains(normalized)) {
                return Boolean.TRUE;
            } else if (FALSE_SET.contains(normalized)) {
                return Boolean.FALSE;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------------- boolean 转其他基本类型

    /**
     * boolean 值转为 {@code int}.
     *
     * <p>{@code true → 1}，{@code false → 0}。
     *
     * @param value boolean 值
     * @return 对应的 int 值（1 或 0）
     */
    public static int toInt(final boolean value) {
        return value ? 1 : 0;
    }

    /**
     * boolean 值转为 {@link Integer} 包装类型.
     *
     * @param value boolean 值
     * @return 对应的 Integer 值（1 或 0）
     */
    public static Integer toInteger(final boolean value) {
        return toInt(value);
    }

    /**
     * boolean 值转为 {@code char}.
     *
     * <p>{@code true → '\u0001'}，{@code false → '\u0000'}。
     *
     * @param value boolean 值
     * @return 对应的 char 值
     */
    public static char toChar(final boolean value) {
        return (char) toInt(value);
    }

    /**
     * boolean 值转为 {@link Character} 包装类型.
     *
     * @param value boolean 值
     * @return 对应的 Character 值
     */
    public static Character toCharacter(final boolean value) {
        return toChar(value);
    }

    /**
     * boolean 值转为 {@code byte}.
     *
     * <p>{@code true → 1}，{@code false → 0}。
     *
     * @param value boolean 值
     * @return 对应的 byte 值
     */
    public static byte toByte(final boolean value) {
        return (byte) toInt(value);
    }

    /**
     * boolean 值转为 {@link Byte} 包装类型.
     *
     * @param value boolean 值
     * @return 对应的 Byte 值
     */
    public static Byte toByteObj(final boolean value) {
        return toByte(value);
    }

    /**
     * boolean 值转为 {@code long}.
     *
     * <p>{@code true → 1L}，{@code false → 0L}。
     *
     * @param value boolean 值
     * @return 对应的 long 值
     */
    public static long toLong(final boolean value) {
        return toInt(value);
    }

    /**
     * boolean 值转为 {@link Long} 包装类型.
     *
     * @param value boolean 值
     * @return 对应的 Long 值
     */
    public static Long toLongObj(final boolean value) {
        return toLong(value);
    }

    /**
     * boolean 值转为 {@code short}.
     *
     * <p>{@code true → 1}，{@code false → 0}。
     *
     * @param value boolean 值
     * @return 对应的 short 值
     */
    public static short toShort(final boolean value) {
        return (short) toInt(value);
    }

    /**
     * boolean 值转为 {@link Short} 包装类型.
     *
     * @param value boolean 值
     * @return 对应的 Short 值
     */
    public static Short toShortObj(final boolean value) {
        return toShort(value);
    }

    /**
     * boolean 值转为 {@code float}.
     *
     * <p>{@code true → 1.0f}，{@code false → 0.0f}。
     *
     * @param value boolean 值
     * @return 对应的 float 值
     */
    public static float toFloat(final boolean value) {
        return (float) toInt(value);
    }

    /**
     * boolean 值转为 {@link Float} 包装类型.
     *
     * @param value boolean 值
     * @return 对应的 Float 值
     */
    public static Float toFloatObj(final boolean value) {
        return toFloat(value);
    }

    /**
     * boolean 值转为 {@code double}.
     *
     * <p>{@code true → 1.0}，{@code false → 0.0}。
     *
     * @param value boolean 值
     * @return 对应的 double 值
     */
    public static double toDouble(final boolean value) {
        return toInt(value);
    }

    /**
     * boolean 值转为 {@link Double} 包装类型.
     *
     * @param value boolean 值
     * @return 对应的 Double 值
     */
    public static Double toDoubleObj(final boolean value) {
        return toDouble(value);
    }

    // ---------------------------------------------------------------------- toString

    /**
     * 将 boolean 转换为字符串 {@code "true"} 或 {@code "false"}.
     *
     * <pre>
     *   BooleanUtil.toStringTrueFalse(true)  = "true"
     *   BooleanUtil.toStringTrueFalse(false) = "false"
     * </pre>
     *
     * @param bool boolean 值
     * @return {@code "true"} 或 {@code "false"}
     */
    public static String toStringTrueFalse(final boolean bool) {
        return toString(bool, "true", "false");
    }

    /**
     * 将 boolean 转换为字符串 {@code "on"} 或 {@code "off"}.
     *
     * <p>常用于开关状态的展示。
     *
     * <pre>
     *   BooleanUtil.toStringOnOff(true)  = "on"
     *   BooleanUtil.toStringOnOff(false) = "off"
     * </pre>
     *
     * @param bool boolean 值
     * @return {@code "on"} 或 {@code "off"}
     */
    public static String toStringOnOff(final boolean bool) {
        return toString(bool, "on", "off");
    }

    /**
     * 将 boolean 转换为字符串 {@code "yes"} 或 {@code "no"}.
     *
     * <p>常用于是否确认场景的展示。
     *
     * <pre>
     *   BooleanUtil.toStringYesNo(true)  = "yes"
     *   BooleanUtil.toStringYesNo(false) = "no"
     * </pre>
     *
     * @param bool boolean 值
     * @return {@code "yes"} 或 {@code "no"}
     */
    public static String toStringYesNo(final boolean bool) {
        return toString(bool, "yes", "no");
    }

    /**
     * 将 boolean 转换为自定义字符串（基本类型版本）.
     *
     * <pre>
     *   BooleanUtil.toString(true, "true", "false")  = "true"
     *   BooleanUtil.toString(false, "true", "false") = "false"
     * </pre>
     *
     * @param bool        boolean 值
     * @param trueString  值为 {@code true} 时返回的字符串，可以为 {@code null}
     * @param falseString 值为 {@code false} 时返回的字符串，可以为 {@code null}
     * @return 对应的字符串
     */
    public static String toString(final boolean bool, final String trueString, final String falseString) {
        return bool ? trueString : falseString;
    }

    /**
     * 将 {@link Boolean} 包装类型转换为自定义字符串（三态版本）.
     *
     * <p>当值为 {@code null} 时返回指定的 nullString，适用于需要区分三态的场景。
     *
     * <pre>
     *   BooleanUtil.toString(true, "true", "false", null)  = "true"
     *   BooleanUtil.toString(false, "true", "false", null) = "false"
     *   BooleanUtil.toString(null, "true", "false", null)  = null
     * </pre>
     *
     * @param bool        Boolean 包装值，可以为 {@code null}
     * @param trueString  值为 {@code true} 时返回的字符串，可以为 {@code null}
     * @param falseString 值为 {@code false} 时返回的字符串，可以为 {@code null}
     * @param nullString  值为 {@code null} 时返回的字符串，可以为 {@code null}
     * @return 对应的字符串
     */
    public static String toString(final Boolean bool, final String trueString,
                                  final String falseString, final String nullString) {
        if (bool == null) {
            return nullString;
        }
        return bool ? trueString : falseString;
    }

    // ---------------------------------------------------------------------- and

    /**
     * 对 {@code boolean} 数组执行逻辑与（AND）运算.
     *
     * <p>所有元素均为 {@code true} 时返回 {@code true}，任一元素为 {@code false} 则返回 {@code false}。
     *
     * <pre>
     *   BooleanUtil.and(true, true)         = true
     *   BooleanUtil.and(false, false)       = false
     *   BooleanUtil.and(true, false)        = false
     *   BooleanUtil.and(true, true, true)   = true
     *   BooleanUtil.and(true, true, false)  = false
     * </pre>
     *
     * @param array {@code boolean} 数组，不能为空
     * @return 逻辑与运算结果
     * @throws IllegalArgumentException 数组为空时抛出
     */
    public static boolean and(final boolean... array) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException("The Array must not be empty !");
        }
        for (final boolean element : array) {
            if (!element) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对 {@link Boolean} 包装数组执行逻辑与（AND）运算.
     *
     * <p>{@code null} 元素视为 {@code false}（通过 {@link #isTrue(Boolean)} 安全判断）。
     *
     * <pre>
     *   BooleanUtil.andOfWrap(Boolean.TRUE, Boolean.TRUE)                 = Boolean.TRUE
     *   BooleanUtil.andOfWrap(Boolean.FALSE, Boolean.FALSE)               = Boolean.FALSE
     *   BooleanUtil.andOfWrap(Boolean.TRUE, Boolean.FALSE)                = Boolean.FALSE
     *   BooleanUtil.andOfWrap(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)   = Boolean.TRUE
     *   BooleanUtil.andOfWrap(Boolean.TRUE, null)                         = Boolean.FALSE
     * </pre>
     *
     * @param array {@link Boolean} 包装数组，不能为空
     * @return 逻辑与运算结果
     * @throws IllegalArgumentException 数组为空时抛出
     */
    public static Boolean andOfWrap(final Boolean... array) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException("The Array must not be empty !");
        }
        for (final Boolean b : array) {
            if (!isTrue(b)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    // ---------------------------------------------------------------------- or

    /**
     * 对 {@code boolean} 数组执行逻辑或（OR）运算.
     *
     * <p>任一元素为 {@code true} 即返回 {@code true}，所有元素均为 {@code false} 时返回 {@code false}。
     *
     * <pre>
     *   BooleanUtil.or(true, true)          = true
     *   BooleanUtil.or(false, false)        = false
     *   BooleanUtil.or(true, false)         = true
     *   BooleanUtil.or(true, true, false)   = true
     *   BooleanUtil.or(false, false, false) = false
     * </pre>
     *
     * @param array {@code boolean} 数组，不能为空
     * @return 逻辑或运算结果
     * @throws IllegalArgumentException 数组为空时抛出
     */
    public static boolean or(final boolean... array) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException("The Array must not be empty !");
        }
        for (final boolean element : array) {
            if (element) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对 {@link Boolean} 包装数组执行逻辑或（OR）运算.
     *
     * <p>{@code null} 元素视为 {@code false}（通过 {@link #isTrue(Boolean)} 安全判断）。
     *
     * <pre>
     *   BooleanUtil.orOfWrap(Boolean.TRUE, Boolean.TRUE)                  = Boolean.TRUE
     *   BooleanUtil.orOfWrap(Boolean.FALSE, Boolean.FALSE)                = Boolean.FALSE
     *   BooleanUtil.orOfWrap(Boolean.TRUE, Boolean.FALSE)                 = Boolean.TRUE
     *   BooleanUtil.orOfWrap(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE)  = Boolean.TRUE
     *   BooleanUtil.orOfWrap(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE) = Boolean.FALSE
     *   BooleanUtil.orOfWrap(null, Boolean.FALSE)                         = Boolean.FALSE
     * </pre>
     *
     * @param array {@link Boolean} 包装数组，不能为空
     * @return 逻辑或运算结果
     * @throws IllegalArgumentException 数组为空时抛出
     */
    public static Boolean orOfWrap(final Boolean... array) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException("The Array must not be empty !");
        }
        for (final Boolean b : array) {
            if (isTrue(b)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    // ---------------------------------------------------------------------- xor

    /**
     * 对 {@code boolean} 数组执行逻辑异或（XOR）运算.
     *
     * <p>语义：当数组中 {@code true} 的数量为奇数时返回 {@code true}，
     * 数量为偶数时返回 {@code false}。
     *
     * <p><b>注意：</b>该方法并不是判断"是否恰好只有一个 true"，
     * 不适用于互斥条件校验（例如：多个条件只能有一个成立的场景）。
     * 如需判断"是否恰好只有一个 true"，请使用 {@link #exactlyOneTrue(boolean...)}。
     *
     * <pre>
     *   BooleanUtil.xor(true, true)            = false
     *   BooleanUtil.xor(false, false)          = false
     *   BooleanUtil.xor(true, false)           = true
     *   BooleanUtil.xor(true, true, true)      = true
     *   BooleanUtil.xor(true, true, false)     = false
     *   BooleanUtil.xor(false, false, false)   = false
     * </pre>
     *
     * @param array {@code boolean} 数组，不能为空
     * @return 异或运算结果
     * @throws IllegalArgumentException 数组为空时抛出
     * @see #exactlyOneTrue(boolean...)
     */
    public static boolean xor(final boolean... array) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException("The Array must not be empty");
        }
        boolean result = false;
        for (final boolean element : array) {
            result ^= element;
        }
        return result;
    }

    /**
     * 判断 {@code boolean} 数组中是否<b>恰好只有一个</b>元素为 {@code true}.
     *
     * <p>常用于互斥条件校验场景，例如：
     * <ul>
     *     <li>多个角色只能同时存在一个</li>
     *     <li>多个条件只能命中一个分支</li>
     * </ul>
     *
     * <p>与 {@link #xor(boolean...)} 不同，本方法在出现多个 {@code true} 时会直接返回 {@code false}。
     *
     * <pre>
     *   BooleanUtil.exactlyOneTrue(true, false, false)  = true
     *   BooleanUtil.exactlyOneTrue(true, true, false)   = false
     *   BooleanUtil.exactlyOneTrue(false, false, false) = false
     * </pre>
     *
     * @param array {@code boolean} 数组，不能为空
     * @return 当且仅当数组中恰好只有一个 {@code true} 时返回 {@code true}
     * @throws IllegalArgumentException 数组为空时抛出
     */
    public static boolean exactlyOneTrue(final boolean... array) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException("The Array must not be empty");
        }
        int count = 0;
        for (final boolean b : array) {
            if (b && ++count > 1) {
                return false;
            }
        }
        return count == 1;
    }

    /**
     * 对 {@link Boolean} 包装数组执行逻辑异或（XOR）运算.
     *
     * <p>内部将包装类型手动拆箱为基本类型后委托给 {@link #xor(boolean...)}。
     * {@code null} 元素拆箱为 {@code false}。
     *
     * <pre>
     *   BooleanUtil.xorOfWrap(Boolean.TRUE, Boolean.TRUE)   = Boolean.FALSE
     *   BooleanUtil.xorOfWrap(Boolean.FALSE, Boolean.FALSE) = Boolean.FALSE
     *   BooleanUtil.xorOfWrap(Boolean.TRUE, Boolean.FALSE)  = Boolean.TRUE
     * </pre>
     *
     * @param array {@link Boolean} 包装数组，不能为空
     * @return 异或运算结果
     * @throws IllegalArgumentException 数组为空时抛出
     */
    public static Boolean xorOfWrap(final Boolean... array) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException("The Array must not be empty !");
        }
        // 手动拆箱，null 视为 false
        final boolean[] primitive = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            primitive[i] = isTrue(array[i]);
        }
        return xor(primitive);
    }

    // ---------------------------------------------------------------------- isBoolean

    /**
     * 判断给定的 {@link Class} 是否为 {@code boolean} 或 {@link Boolean} 类型.
     *
     * <pre>
     *   BooleanUtil.isBoolean(boolean.class) = true
     *   BooleanUtil.isBoolean(Boolean.class) = true
     *   BooleanUtil.isBoolean(String.class)  = false
     *   BooleanUtil.isBoolean(null)          = false
     * </pre>
     *
     * @param clazz 类，可以为 {@code null}
     * @return 是否为 boolean 或 Boolean 类型
     * @since 4.5.2
     */
    public static boolean isBoolean(final Class<?> clazz) {
        return (clazz == Boolean.class || clazz == boolean.class);
    }
}
