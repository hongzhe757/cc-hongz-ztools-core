package cc.hongz.ztools.core.lang.tree.parser;

import cc.hongz.ztools.core.lang.tree.Tree;

/**
 * 树节点解析器 可以参考{@link DefaultNodeParser}.
 *
 * @param <T> 转换的实体 为数据源里的对象类型
 * @author liangbaikai
 */
@FunctionalInterface
public interface NodeParser<T, E> {
    /**
     * 解析实体为树节点.
     *
     * @param object   源数据实体.
     * @param treeNode 树节点实体.
     */
    void parse(T object, Tree<E> treeNode);
}
