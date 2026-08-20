package cc.hongz.ztools.core.lang.tree;

import java.io.Serializable;

/**
 * 树配置属性相关.
 *
 * @author liangbaikai
 */
public class TreeNodeConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 默认属性配置对象.
     */
    public static final TreeNodeConfig DEFAULT_CONFIG = new TreeNodeConfig();

    // 属性名配置字段
    private String idKey = "id";
    private String parentIdKey = "parentId";
    private String weightKey = "weight";
    private String nameKey = "name";
    private String childrenKey = "children";
    // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
    private Integer deep;


    /**
     * 获取ID对应的名称.
     *
     * @return ID对应的名称
     */
    public String getIdKey() {
        return this.idKey;
    }

    /**
     * 设置ID对应的名称.
     *
     * @param newIdKey ID对应的名称
     * @return this
     */
    public TreeNodeConfig setIdKey(final String newIdKey) {
        this.idKey = newIdKey;
        return this;
    }

    /**
     * 获取权重对应的名称.
     *
     * @return 权重对应的名称
     */
    public String getWeightKey() {
        return this.weightKey;
    }

    /**
     * 设置权重对应的名称.
     *
     * @param newWeightKey 权重对应的名称
     * @return this
     */
    public TreeNodeConfig setWeightKey(final String newWeightKey) {
        this.weightKey = newWeightKey;
        return this;
    }

    /**
     * 获取节点名对应的名称.
     *
     * @return 节点名对应的名称
     */
    public String getNameKey() {
        return this.nameKey;
    }

    /**
     * 设置节点名对应的名称.
     *
     * @param newNameKey 节点名对应的名称
     * @return this
     */
    public TreeNodeConfig setNameKey(final String newNameKey) {
        this.nameKey = newNameKey;
        return this;
    }

    /**
     * 获取子点对应的名称.
     *
     * @return 子点对应的名称
     */
    public String getChildrenKey() {
        return this.childrenKey;
    }

    /**
     * 设置子点对应的名称.
     *
     * @param newChildrenKey 子点对应的名称
     * @return this
     */
    public TreeNodeConfig setChildrenKey(final String newChildrenKey) {
        this.childrenKey = newChildrenKey;
        return this;
    }

    /**
     * 获取父节点ID对应的名称.
     *
     * @return 父点对应的名称
     */
    public String getParentIdKey() {
        return this.parentIdKey;
    }


    /**
     * 设置父点对应的名称.
     *
     * @param newParentIdKey 父点对应的名称
     * @return this
     */
    public TreeNodeConfig setParentIdKey(final String newParentIdKey) {
        this.parentIdKey = newParentIdKey;
        return this;
    }

    /**
     * 获取递归深度.
     *
     * @return 递归深度
     */
    public Integer getDeep() {
        return this.deep;
    }

    /**
     * 设置递归深度.
     *
     * @param newDeep 递归深度
     * @return this
     */
    public TreeNodeConfig setDeep(final Integer newDeep) {
        this.deep = newDeep;
        return this;
    }
}
