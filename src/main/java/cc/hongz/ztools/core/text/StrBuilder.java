package cc.hongz.ztools.core.text;

/**
 * 字符串构建器（captcha 最小子集）.
 *
 * <p>
 * 设计模式：建造者（Builder），链式 append 后 toString 产出字符串.
 * @author hongzhe.qin
 */
public class StrBuilder {

    /**
     * 内部 {@link StringBuilder}.
     */
    private final StringBuilder builder;

    /**
     * 构造空构建器.
     */
    public StrBuilder() {
        this.builder = new StringBuilder();
    }

    /**
     * 追加内容.
     *
     * @param obj 追加对象
     * @return 当前构建器
     */
    public StrBuilder append(final Object obj) {
        builder.append(obj);
        return this;
    }

    /**
     * 转为字符串.
     *
     * @return 构建结果
     */
    @Override
    public String toString() {
        return builder.toString();
    }
}
