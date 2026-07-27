package cc.hongz.ztools.core.bean.copier;

import java.io.Serializable;

/**
 * 属性拷贝选项（自 Hutool {@code cn.hutool.core.bean.copier.CopyOptions}
 * 迁入，精简版）.
 *
 * @author Looly
 */
public class CopyOptions implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean ignoreNullValue;
    private boolean ignoreError;
    private boolean ignoreCase;

    /**
     * 创建拷贝选项.
     *
     * @return 拷贝选项
     */
    public static CopyOptions create() {
        return new CopyOptions();
    }

    public CopyOptions() {
    }

    /**
     * 构造拷贝选项.
     *
     * @param ignoreNullValue 是否忽略空值
     * @param ignoreError     是否忽略注入错误
     */
    public CopyOptions(final boolean ignoreNullValue,
            final boolean ignoreError) {
        this.ignoreNullValue = ignoreNullValue;
        this.ignoreError = ignoreError;
    }

    /**
     * 设置是否忽略空值.
     *
     * @param ignoreNullValue 是否忽略空值
     * @return CopyOptions
     */
    public CopyOptions setIgnoreNullValue(
            final boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
        return this;
    }

    /**
     * 设置忽略空值.
     *
     * @return CopyOptions
     */
    public CopyOptions ignoreNullValue() {
        return setIgnoreNullValue(true);
    }

    /**
     * 设置是否忽略字段注入错误.
     *
     * @param ignoreError 是否忽略注入错误
     * @return CopyOptions
     */
    public CopyOptions setIgnoreError(
            final boolean ignoreError) {
        this.ignoreError = ignoreError;
        return this;
    }

    /**
     * 设置忽略注入错误.
     *
     * @return CopyOptions
     */
    public CopyOptions ignoreError() {
        return setIgnoreError(true);
    }

    /**
     * 设置是否忽略字段大小写.
     *
     * @param ignoreCase 是否忽略大小写
     * @return CopyOptions
     */
    public CopyOptions setIgnoreCase(
            final boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    /**
     * 设置忽略字段大小写.
     *
     * @return CopyOptions
     */
    public CopyOptions ignoreCase() {
        return setIgnoreCase(true);
    }

    /**
     * 是否忽略空值.
     *
     * @return true/false
     */
    public boolean isIgnoreNullValue() {
        return ignoreNullValue;
    }

    /**
     * 是否忽略注入错误.
     *
     * @return true/false
     */
    public boolean isIgnoreError() {
        return ignoreError;
    }

    /**
     * 是否忽略大小写.
     *
     * @return true/false
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }
}
