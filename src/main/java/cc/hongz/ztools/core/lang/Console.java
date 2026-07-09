package cc.hongz.ztools.core.lang;

/**
 * 控制台输出工具类（测试调试用）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，封装标准输出（测试调试）.
 * @author hongzhe.qin
 */
public final class Console {

    /**
     * 私有构造，禁止实例化.
     */
    private Console() {
    }

    /**
     * 向标准输出打印日志.
     *
     * @param values 待输出内容
     */
    public static void log(final Object... values) {
        if (values == null || values.length == 0) {
            System.out.println();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(values[i]);
        }
        System.out.println(sb);
    }
}
