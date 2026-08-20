package cc.hongz.ztools.core.lang.tree;

import cc.hongz.ztools.core.collection.CollUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 树工具类单元测试.
 */
@DisplayName("TreeUtil 树工具")
class TreeUtilTest {

    // 模拟数据
    static List<TreeNode<String>> nodeList = CollUtil.newArrayList();

    static {
        nodeList.add(new TreeNode<>("1", "0", "系统管理", 5));
        nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));
        nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));

        nodeList.add(new TreeNode<>("2", "0", "店铺管理", 1));
        nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
        nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));
    }

    @Test
    @DisplayName("构建树（默认配置）")
    public void buildWithDefaultConfig() {
        List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");
        assertNotNull(treeList);
        assertEquals(2, treeList.size());
        for (Tree<String> tree : treeList) {
            assertEquals("0", tree.getParentId());
        }
    }

    @Test
    @DisplayName("构建单根节点树（默认配置）")
    public void buildSingleWithDefaultConfig() {
        Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
        assertNotNull(tree);
        assertEquals("0", tree.getId());
        assertNotNull(tree.getChildren());
        assertEquals(2, tree.getChildren().size());
    }

    @Test
    @DisplayName("构建树（自定义配置）")
    public void buildWithCustomConfig() {
        TreeNodeConfig config = new TreeNodeConfig();
        config.setWeightKey("order");
        config.setIdKey("rid");
        config.setDeep(2);

        List<Tree<String>> treeNodes = TreeUtil.build(nodeList, "0", config,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getWeight());
                    tree.setName(treeNode.getName());
                    tree.putExtra("extraField", 666);
                });

        assertEquals(2, treeNodes.size());
    }

    @Test
    @DisplayName("构建单根节点树（TreeNode 列表）")
    public void buildWithTreeNodeList() {
        List<TreeNode<Integer>> intNodes = new ArrayList<>();
        intNodes.add(new TreeNode<>(1, 0, "root", 0));
        intNodes.add(new TreeNode<>(2, 1, "child1", 0));
        intNodes.add(new TreeNode<>(3, 1, "child2", 0));

        Tree<Integer> tree = TreeUtil.buildSingle(intNodes);
        assertNotNull(tree);
        // buildSingle 会生成 id=0 的空根节点，其唯一子节点是 "root"
        assertEquals(1, tree.getChildren().size());
        // "root" 节点下有两个子节点
        assertEquals(2, tree.getChildren().get(0).getChildren().size());
    }

    @Test
    @DisplayName("构建树（Integer 列表）")
    public void buildWithIntegerList() {
        List<TreeNode<Integer>> intNodes = new ArrayList<>();
        intNodes.add(new TreeNode<>(1, 0, "root", 0));
        intNodes.add(new TreeNode<>(2, 1, "child1", 0));
        intNodes.add(new TreeNode<>(3, 1, "child2", 0));

        List<Tree<Integer>> treeList = TreeUtil.build(intNodes);
        assertNotNull(treeList);
        assertEquals(1, treeList.size());
    }

    @Test
    @DisplayName("遍历树")
    public void walkTest() {
        List<String> ids = new ArrayList<>();
        final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
        tree.walk((tr) -> ids.add(tr.getId()));

        assertEquals(7, ids.size());
    }

    @Test
    @DisplayName("克隆树")
    public void cloneTreeTest() {
        final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
        final Tree<String> cloneTree = tree.cloneTree();

        List<String> ids = new ArrayList<>();
        cloneTree.walk((tr) -> ids.add(tr.getId()));

        assertEquals(7, ids.size());
    }

    @Test
    @DisplayName("过滤树")
    public void filterTest() {
        final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
        tree.filter((t) -> {
            final CharSequence name = t.getName();
            return null != name && name.toString().contains("店铺");
        });

        List<String> ids = new ArrayList<>();
        tree.walk((tr) -> ids.add(tr.getId()));
        assertEquals(4, ids.size());
    }

    @Test
    @DisplayName("过滤生成新树")
    public void filterNewTest() {
        final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");

        Tree<String> newTree = tree.filterNew((t) -> {
            final CharSequence name = t.getName();
            return null != name && name.toString().contains("店铺");
        });

        List<String> ids = new ArrayList<>();
        newTree.walk((tr) -> ids.add(tr.getId()));
        assertEquals(4, ids.size());

        // 原树不受影响
        List<String> ids2 = new ArrayList<>();
        tree.walk((tr) -> ids2.add(tr.getId()));
        assertEquals(7, ids2.size());
    }

    @Test
    @DisplayName("获取节点")
    public void getNodeTest() {
        final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
        Tree<String> node = tree.getNode("111");
        assertNotNull(node);
        assertEquals("111", node.getId());
        assertEquals("用户添加", node.getName().toString());
    }

    @Test
    @DisplayName("获取不存在的节点")
    public void getNodeNotFoundTest() {
        final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
        Tree<String> node = tree.getNode("999");
        assertNull(node);
    }

    @Test
    @DisplayName("获取父节点名称列表")
    public void getParentsNameTest() {
        final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
        Tree<String> node = tree.getNode("111");
        assertNotNull(node);

        // 设置parent关系
        Tree<String> parentNode = tree.getNode("11");
        assertNotNull(parentNode);
        node.setParent(parentNode);

        List<CharSequence> names = TreeUtil.getParentsName(node, true);
        assertFalse(names.isEmpty());
    }

    @Test
    @DisplayName("获取父节点名称（空节点）")
    public void getParentsNameWithNullNode() {
        List<CharSequence> names = TreeUtil.getParentsName(null, true);
        assertNotNull(names);
        assertTrue(names.isEmpty());
    }

    @Test
    @DisplayName("获取父节点 ID 列表")
    public void getParentsIdTest() {
        final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
        Tree<String> node = tree.getNode("111");
        assertNotNull(node);

        Tree<String> parentNode = tree.getNode("11");
        assertNotNull(parentNode);
        node.setParent(parentNode);

        List<String> ids = TreeUtil.getParentsId(node, true);
        assertFalse(ids.isEmpty());
    }

    @Test
    @DisplayName("创建空节点")
    public void createEmptyNodeTest() {
        Tree<String> node = TreeUtil.createEmptyNode("test");
        assertNotNull(node);
        assertEquals("test", node.getId());
        assertNull(node.getChildren());
    }

    @Test
    @DisplayName("判断是否有子节点")
    public void hasChildTest() {
        final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
        assertTrue(tree.hasChild());

        // 叶子节点
        Tree<String> leafNode = tree.getNode("111");
        assertNotNull(leafNode);
        assertFalse(leafNode.hasChild());
    }

    @Test
    @DisplayName("树配置")
    public void treeConfigTest() {
        TreeNodeConfig config = new TreeNodeConfig();
        config.setIdKey("myId")
                .setParentIdKey("myParentId")
                .setNameKey("myName")
                .setWeightKey("myWeight")
                .setChildrenKey("myChildren")
                .setDeep(3);

        assertEquals("myId", config.getIdKey());
        assertEquals("myParentId", config.getParentIdKey());
        assertEquals("myName", config.getNameKey());
        assertEquals("myWeight", config.getWeightKey());
        assertEquals("myChildren", config.getChildrenKey());
        assertEquals(3, config.getDeep());
    }

    @Test
    @DisplayName("TreeNode 相等判断")
    public void treeNodeEqualsTest() {
        TreeNode<String> node1 = new TreeNode<>("1", "0", "test", 0);
        TreeNode<String> node2 = new TreeNode<>("1", "0", "test2", 1);
        TreeNode<String> node3 = new TreeNode<>("2", "0", "test", 0);

        assertEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    @DisplayName("TreeBuilder 追加节点")
    public void treeBuilderAppendTest() {
        TreeBuilder<String> builder = TreeBuilder.of("0");
        builder.append(nodeList, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getName());
        });

        Tree<String> result = builder.build();
        assertNotNull(result);
        assertNotNull(result.getChildren());
    }

    @Test
    @DisplayName("TreeBuilder 构建列表")
    public void treeBuilderBuildListTest() {
        TreeBuilder<String> builder = TreeBuilder.of("0");
        builder.append(nodeList, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getName());
        });

        List<Tree<String>> list = builder.buildList();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("TreeBuilder 重置")
    public void treeBuilderResetTest() {
        TreeBuilder<String> builder = TreeBuilder.of("0");
        builder.append(nodeList, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getName());
        });
        builder.build();

        // reset后可以重新构建
        builder.reset();
        builder.append(nodeList, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getName());
        });
        Tree<String> result = builder.build();
        assertNotNull(result);
    }

    @Test
    @DisplayName("TreeBuilder 重复构建检查")
    public void treeBuilderCheckBuiltTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            final TreeBuilder<Integer> of = TreeBuilder.of(0);
            of.build();
            of.append(new ArrayList<>());
        });
    }

    @Test
    @DisplayName("TreeBuilder 设置属性")
    public void treeBuilderSetPropertiesTest() {
        TreeBuilder<String> builder = TreeBuilder.of("root");
        builder.setId("newId")
                .setParentId("parentId")
                .setName("testName")
                .setWeight(10)
                .putExtra("key1", "value1");

        Tree<String> tree = builder.build();
        assertEquals("newId", tree.getId());
        assertEquals("parentId", tree.getParentId());
        assertEquals("testName", tree.getName());
    }

    @Test
    @DisplayName("添加子节点")
    public void treeAddChildrenTest() {
        Tree<String> parent = new Tree<>();
        parent.setId("parent");

        Tree<String> child1 = new Tree<>();
        child1.setId("child1");

        Tree<String> child2 = new Tree<>();
        child2.setId("child2");

        parent.addChildren(child1, child2);

        assertNotNull(parent.getChildren());
        assertEquals(2, parent.getChildren().size());
        assertEquals(parent, child1.getParent());
        assertEquals(parent, child2.getParent());
    }

    @Test
    @DisplayName("设置子节点为 null")
    public void treeSetChildrenNullTest() {
        Tree<String> tree = new Tree<>();
        tree.setId("test");
        tree.setChildren(new ArrayList<>());
        assertNotNull(tree.getChildren());

        tree.setChildren(null);
        assertNull(tree.getChildren());
    }

    @Test
    @DisplayName("Tree toString")
    public void treeToStringTest() {
        final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
        String str = tree.toString();
        assertNotNull(str);
        assertFalse(str.isEmpty());
    }

    @Test
    @DisplayName("树深度裁剪")
    public void treeDeepCutTest() {
        TreeNodeConfig config = new TreeNodeConfig();
        config.setDeep(1);

        Tree<String> tree = TreeUtil.buildSingle(nodeList, "0", config,
                (treeNode, t) -> {
                    t.setId(treeNode.getId());
                    t.setParentId(treeNode.getParentId());
                    t.setWeight(treeNode.getWeight());
                    t.setName(treeNode.getName());
                });

        assertNotNull(tree);
        // deep=1 表示只保留到第1层子节点
        List<Tree<String>> children = tree.getChildren();
        assertNotNull(children);
        for (Tree<String> child : children) {
            // 第1层子节点不应再有子节点
            assertNull(child.getChildren());
        }
    }

    @Test
    @DisplayName("函数式构建树")
    public void functionalBuildTest() {
        // 函数式构建树（不依赖Tree类）
        List<TestNode> nodes = new ArrayList<>();
        nodes.add(new TestNode(1, 0, "root"));
        nodes.add(new TestNode(2, 1, "child1"));
        nodes.add(new TestNode(3, 1, "child2"));
        nodes.add(new TestNode(4, 2, "grandchild1"));

        List<TestNode> result = TreeUtil.build(nodes, 0,
                TestNode::getId,
                TestNode::getParentId,
                (node, children) -> node.setChildren(children));

        assertNotNull(result);
        assertEquals(1, result.size());
        TestNode root = result.get(0);
        assertEquals(2, root.getChildren().size());
    }

    @Test
    @DisplayName("默认节点解析器")
    public void defaultNodeParserTest() {
        TreeNode<String> treeNode = new TreeNode<>("1", "0", "test", 5);
        treeNode.setExtra(java.util.Collections.singletonMap("extraKey", "extraValue"));

        Tree<String> tree = new Tree<>();
        cc.hongz.ztools.core.lang.tree.parser.DefaultNodeParser<String> parser =
                new cc.hongz.ztools.core.lang.tree.parser.DefaultNodeParser<>();
        parser.parse(treeNode, tree);

        assertEquals("1", tree.getId());
        assertEquals("0", tree.getParentId());
        assertEquals("test", tree.getName().toString());
    }

    /**
     * 测试用节点类（函数式构建树测试用）.
     */
    static class TestNode {
        private Integer id;
        private Integer parentId;
        private String name;
        private List<TestNode> children;

        public TestNode(Integer id, Integer parentId, String name) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }

        public Integer getId() { return id; }
        public Integer getParentId() { return parentId; }
        public String getName() { return name; }
        public List<TestNode> getChildren() { return children; }
        public void setChildren(List<TestNode> children) { this.children = children; }
    }
}
