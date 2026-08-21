package cc.hongz.ztools.core.lang.ansi;

/**
 * ANSI可转义节点接口，实现为ANSI颜色等.
 *
 * <p>来自Spring Boot</p>
 *
 * @author Phillip Webb
 */
public interface AnsiElement {

    /**
     * 获取ANSI转义编码.
     *
     * @return ANSI转义编码
     */
    @Override
    String toString();

    /**
     * 获取ANSI代码，默认返回-1.
     * @return ANSI代码
     * @since 5.8.7
     */
    default int getCode() {
        return -1;
    }
}
