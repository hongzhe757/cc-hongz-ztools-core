package cc.hongz.ztools.core.lang.ansi;

import cc.hongz.ztools.core.lang.Assert;

/**
 * ANSI 8-bit前景或背景色（即8位编码，共256种颜色（2^8） ）.<br>
 * <ul>
 *     <li>0-7：                        标准颜色（同ESC [ 30–37 m）</li>
 *     <li>8-15：                       高强度颜色（同ESC [ 90–97 m）</li>
 *     <li>16-231（6 × 6 × 6 共 216色）： 16 + 36 × r + 6 × g + b (0 ≤ r, g, b ≤ 5)</li>
 *     <li>232-255：                    从黑到白的24阶灰度色</li>
 * </ul>.
 *
 * <p>来自Spring Boot</p>
 *
 * @author Toshiaki Maki, Phillip Webb
 * @see #foreground(int)
 * @see #background(int)
 * @since 5.8.0
 */
@SuppressWarnings("checkstyle:MagicNumber")
public final class Ansi8BitColor implements AnsiElement {

    /**
     * 前景色前缀。
     */
    private static final String PREFIX_FORE = "38;5;";

    /**
     * 背景色前缀。
     */
    private static final String PREFIX_BACK = "48;5;";

    /**
     * 前景色ANSI颜色实例.
     *
     * @param code 颜色代码(0-255)
     * @return 前景色ANSI颜色实例
     */
    public static Ansi8BitColor foreground(final int code) {
        return new Ansi8BitColor(PREFIX_FORE, code);
    }

    /**
     * 背景色ANSI颜色实例.
     *
     * @param code 颜色代码(0-255)
     * @return 背景色ANSI颜色实例
     */
    public static Ansi8BitColor background(final int code) {
        return new Ansi8BitColor(PREFIX_BACK, code);
    }

    /**
     * 前景或背景前缀。
     */
    private final String prefix;

    /**
     * 颜色代码(0-255)。
     */
    private final int code;

    /**
     * 构造.
     *
     * @param prefix 前缀
     * @param code   颜色代码(0-255)
     * @throws IllegalArgumentException 颜色代码不在0~255范围内
     */
    private Ansi8BitColor(final String prefix, final int code) {
        Assert.isTrue(code >= 0 && code <= 255, "Code must be between 0 and 255");
        this.prefix = prefix;
        this.code = code;
    }

    /**
     * 获取颜色代码(0-255).
     *
     * @return 颜色代码(0 - 255)
     */
    @Override
    public int getCode() {
        return this.code;
    }

    /**
     * 比较两个 8bit 颜色是否相等。
     *
     * @param obj 待比较对象
     * @return 相等返回 true，否则 false
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Ansi8BitColor other = (Ansi8BitColor) obj;
        return this.prefix.equals(other.prefix) && this.code == other.code;
    }

    /**
     * 计算哈希值。
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        return this.prefix.hashCode() * 31 + this.code;
    }

    /**
     * 转换为 ANSI 编码字符串。
     *
     * @return ANSI 编码字符串
     */
    @Override
    public String toString() {
        return this.prefix + this.code;
    }
}
