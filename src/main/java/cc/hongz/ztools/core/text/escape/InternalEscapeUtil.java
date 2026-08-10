package cc.hongz.ztools.core.text.escape;

/**
 * 内部Escape工具类.
 *
 * @author looly
 * @since 5.7.14
 */
final class InternalEscapeUtil {

    /**
     * 私有构造器.
     */
    private InternalEscapeUtil() {
    }

    /**
     * 将数组中的0和1位置的值互换，即键值转换.
     *
     * @param array 被转换的数组
     * @return 转换后的数组
     */
    public static String[][] invert(final String[][] array) {
        final String[][] newarray = new String[array.length][2];
        for (int i = 0; i < array.length; i++) {
            newarray[i][0] = array[i][1];
            newarray[i][1] = array[i][0];
        }
        return newarray;
    }
}
