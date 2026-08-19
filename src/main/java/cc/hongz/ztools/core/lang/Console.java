package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.StrUtil;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * 控制台输出工具类（测试调试用）.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，封装标准输出（测试调试）.
 * @author hongzhe.qin
 */
public final class Console {

    private static final String TEMPLATE_VAR = "{}";

    /**
     * 私有构造，禁止实例化.
     */
    private Console() {
    }

    // --------------------------------------------------------------------------------- Log

    /**
     * 向标准输出打印日志.
     *
     * @param values 待输出内容
     */
    public static void log(final Object... values) {
        if (values == null || values.length == 0) {
            out.println();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(values[i]);
        }
        out.println(sb);
    }

    // --------------------------------------------------------------------------------- Error

    /**
     * 同 System.err.println()方法，打印控制台日志。
     */
    public static void error() {
        err.println();
    }

    /**
     * 同 System.err.println()方法，打印控制台日志。
     *
     * @param obj 要打印的对象
     */
    public static void error(final Object obj) {
        if (obj instanceof Throwable) {
            Throwable e = (Throwable) obj;
            error(e, e.getMessage());
        } else {
            error(TEMPLATE_VAR, obj);
        }
    }

    /**
     * 同 System.err.println()方法，打印控制台日志。
     * 如果传入打印对象为{@link Throwable}对象，那么同时打印堆栈
     *
     * @param obj1      第一个要打印的对象
     * @param otherObjs 其它要打印的对象
     */
    public static void error(final Object obj1, final Object... otherObjs) {
        if (ArrayUtil.isEmpty(otherObjs)) {
            error(obj1);
        } else {
            error(buildTemplateSplitBySpace(otherObjs.length + 1), ArrayUtil.insert(otherObjs, 0, obj1));
        }
    }

    /**
     * 同 System.err.println()方法，打印控制台日志。
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    public static void error(final String template, final Object... values) {
        if (ArrayUtil.isEmpty(values) || StrUtil.contains(template, TEMPLATE_VAR)) {
            errorInternal(template, values);
        } else {
            errorInternal(buildTemplateSplitBySpace(values.length + 1), ArrayUtil.insert(values, 0, template));
        }
    }

    /**
     * 同 System.err.println()方法，打印控制台日志。
     *
     * @param t        异常对象
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    public static void error(final Throwable t, final String template, final Object... values) {
        err.println(StrUtil.format(template, values));
        if (null != t) {
            //noinspection CallToPrintStackTrace
            t.printStackTrace(err);
            err.flush();
        }
    }

    /**
     * 同 System.err.println()方法，打印控制台日志。
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    private static void errorInternal(final String template, final Object... values) {
        error(null, template, values);
    }

    /**
     * 构建空格分隔的模板，类似于"{} {} {} {}"。
     *
     * @param count 变量数量
     * @return 模板
     */
    private static String buildTemplateSplitBySpace(final int count) {
        return StrUtil.repeatAndJoin(TEMPLATE_VAR, count, StrUtil.SPACE);
    }
}
