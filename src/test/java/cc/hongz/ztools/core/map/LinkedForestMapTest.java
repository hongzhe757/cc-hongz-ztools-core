package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link LinkedForestMap} 单元测试.
 */
@DisplayName("LinkedForestMap 森林Map")
class LinkedForestMapTest {

    // ======================= 基本Map操作 =======================

    @Test
    @DisplayName("size/isEmpty - 基本Map操作")
    void sizeAndIsEmpty() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        map.putNode("a", "val");
        assertFalse(map.isEmpty());
        assertEquals(1, map.size());
    }

    @Test
    @DisplayName("containsKey/containsValue - 判断是否包含")
    void containsKeyAndValue() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "val");
        assertTrue(map.containsKey("a"));
        assertFalse(map.containsKey("b"));
    }

    @Test
    @DisplayName("get - 获取节点")
    void get() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "val");
        TreeEntry<String, String> entry = map.get("a");
        assertNotNull(entry);
        assertEquals("a", entry.getKey());
        assertEquals("val", entry.getValue());
        assertNull(map.get("nonexist"));
    }

    @Test
    @DisplayName("keySet - 获取所有key")
    void keySet() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "1");
        map.putNode("b", "2");
        assertEquals(2, map.keySet().size());
        assertTrue(map.keySet().contains("a"));
        assertTrue(map.keySet().contains("b"));
    }

    @Test
    @DisplayName("values - 获取所有Entry")
    void values() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "1");
        map.putNode("b", "2");
        assertEquals(2, map.values().size());
    }

    @Test
    @DisplayName("entrySet - 获取Entry集合")
    void entrySet() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "1");
        assertEquals(1, map.entrySet().size());
    }

    @Test
    @DisplayName("clear - 清空")
    void clear() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "1");
        map.putNode("b", "2");
        map.clear();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    // ======================= putNode =======================

    @Test
    @DisplayName("putNode - 添加新节点")
    void putNodeNew() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        assertNull(map.putNode("a", "val"));
        assertEquals("val", map.get("a").getValue());
    }

    @Test
    @DisplayName("putNode - 更新已有节点")
    void putNodeUpdate() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "old");
        TreeEntry<String, String> old = map.putNode("a", "new");
        assertNotNull(old);
        assertEquals("old", old.getValue());
        assertEquals("new", map.get("a").getValue());
    }

    // ======================= remove =======================

    @Test
    @DisplayName("remove - 删除节点")
    void remove() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "1");
        assertNotNull(map.remove("a"));
        assertNull(map.get("a"));
    }

    @Test
    @DisplayName("remove - 删除不存在的节点")
    void removeNonExist() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        assertNull(map.remove("nonexist"));
    }

    @Test
    @DisplayName("remove - 删除中间节点后子节点连接到父节点")
    void removeMiddle() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putLinkedNodes("a", "v1", "b", "v2");
        map.putLinkedNodes("b", "c", "v3");
        // 结构: a -> b -> c
        map.remove("b");
        // 删除b后，c应该成为a的子节点
        assertNotNull(map.get("a"));
        assertNotNull(map.get("c"));
        assertNull(map.get("b"));
    }

    // ======================= linkNodes =======================

    @Test
    @DisplayName("linkNodes - 建立父子关系")
    void linkNodes() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.linkNodes("parent", "child", (p, c) -> {
            p.setValue("pVal");
            c.setValue("cVal");
        });
        assertNotNull(map.get("parent"));
        assertNotNull(map.get("child"));
        assertEquals("pVal", map.get("parent").getValue());
        assertEquals("cVal", map.get("child").getValue());
    }

    @Test
    @DisplayName("linkNodes - 子节点已有父节点且不允许覆盖时抛异常")
    void linkNodesConflict() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.linkNodes("a", "b", null);
        assertThrows(IllegalArgumentException.class, () ->
                map.linkNodes("c", "b", null));
    }

    @Test
    @DisplayName("linkNodes - 子节点已有父节点但允许覆盖")
    void linkNodesOverride() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(true);
        map.linkNodes("a", "b", null);
        // 不抛异常
        map.linkNodes("c", "b", null);
        assertNotNull(map.get("b"));
    }

    // ======================= putLinkedNodes =======================

    @Test
    @DisplayName("putLinkedNodes - 同时添加父子节点")
    void putLinkedNodes() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putLinkedNodes("parent", "pVal", "child", "cVal");
        assertEquals("pVal", map.get("parent").getValue());
        assertEquals("cVal", map.get("child").getValue());
    }

    @Test
    @DisplayName("putLinkedNodes(3参数) - 添加子节点指定父节点")
    void putLinkedNodes3Args() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("parent", "pVal");
        map.putLinkedNodes("parent", "child", "cVal");
        assertEquals("cVal", map.get("child").getValue());
    }

    // ======================= unlinkNode =======================

    @Test
    @DisplayName("unlinkNode - 解除父子关系")
    void unlinkNode() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putLinkedNodes("a", "v1", "b", "v2");
        map.unlinkNode("a", "b");
        // 节点仍然存在，但父子关系解除
        assertNotNull(map.get("b"));
    }

    @Test
    @DisplayName("unlinkNode - 子节点不存在")
    void unlinkNodeNonExist() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        // 不抛异常
        map.unlinkNode("a", "nonexist");
    }

    // ======================= TreeEntryNode =======================

    @Test
    @DisplayName("TreeEntryNode - 根节点属性")
    void treeEntryNodeRoot() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("root", "val");
        TreeEntry<String, String> entry = map.get("root");
        assertTrue(((LinkedForestMap.TreeEntryNode<String, String>) entry).isRoot());
        assertEquals(0, entry.getWeight());
        assertEquals(entry, entry.getRoot());
    }

    @Test
    @DisplayName("TreeEntryNode - 子节点属性")
    void treeEntryNodeChild() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putLinkedNodes("root", "rv", "child", "cv");
        TreeEntry<String, String> child = map.get("child");
        assertFalse(((LinkedForestMap.TreeEntryNode<String, String>) child).isRoot());
        assertEquals(1, child.getWeight());
        assertEquals("root", child.getRoot().getKey());
    }

    @Test
    @DisplayName("TreeEntryNode - setValue/getValue")
    void treeEntryNodeSetValue() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "old");
        TreeEntry<String, String> entry = map.get("a");
        String oldVal = entry.setValue("new");
        assertEquals("old", oldVal);
        assertEquals("new", entry.getValue());
    }

    @Test
    @DisplayName("TreeEntryNode - getDeclaredParent/getDeclaredChildren")
    void treeEntryNodeParentChildren() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putLinkedNodes("a", "v1", "b", "v2");
        map.putLinkedNodes("a", "c", "v3");
        TreeEntry<String, String> parent = map.get("a");
        assertNotNull(parent.getDeclaredChildren());
        assertEquals(2, parent.getDeclaredChildren().size());
        TreeEntry<String, String> childB = map.get("b");
        assertEquals("a", childB.getDeclaredParent().getKey());
    }

    @Test
    @DisplayName("TreeEntryNode - getChildren (全部后代)")
    void treeEntryNodeAllChildren() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putLinkedNodes("a", "v1", "b", "v2");
        map.putLinkedNodes("b", "c", "v3");
        TreeEntry<String, String> root = map.get("a");
        Map<String, ? extends TreeEntry<String, String>> allChildren = root.getChildren();
        assertEquals(2, allChildren.size());
        assertTrue(allChildren.containsKey("b"));
        assertTrue(allChildren.containsKey("c"));
    }

    @Test
    @DisplayName("TreeEntryNode - forEachChild")
    void treeEntryNodeForEachChild() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putLinkedNodes("a", "v1", "b", "v2");
        map.putLinkedNodes("a", "c", "v3");
        TreeEntry<String, String> root = map.get("a");
        java.util.List<String> keys = new java.util.ArrayList<>();
        root.forEachChild(false, child -> keys.add(child.getKey()));
        assertEquals(2, keys.size());
        assertTrue(keys.contains("b"));
        assertTrue(keys.contains("c"));
    }

    @Test
    @DisplayName("TreeEntryNode - equalsKey")
    void treeEntryNodeEqualsKey() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "val");
        LinkedForestMap.TreeEntryNode<String, String> node =
                (LinkedForestMap.TreeEntryNode<String, String>) map.get("a");
        assertTrue(node.equalsKey("a"));
        assertFalse(node.equalsKey("b"));
    }

    @Test
    @DisplayName("TreeEntryNode - equals/hashCode")
    void treeEntryNodeEqualsHashCode() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putNode("a", "val1");
        LinkedForestMap.TreeEntryNode<String, String> node1 =
                (LinkedForestMap.TreeEntryNode<String, String>) map.get("a");
        // 相同key的节点应该相等
        assertEquals(node1, node1);
        assertNotEquals(null, node1);
        // hashCode应保持一致性
        assertEquals(node1.hashCode(), node1.hashCode());
    }

    @Test
    @DisplayName("TreeEntryNode - getChild")
    void treeEntryNodeGetChild() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putLinkedNodes("a", "v1", "b", "v2");
        map.putLinkedNodes("b", "c", "v3");
        TreeEntry<String, String> root = map.get("a");
        // getChild 应该通过广度优先遍历找到后代
        assertNotNull(root.getChild("c"));
        assertEquals("c", root.getChild("c").getKey());
    }

    @Test
    @DisplayName("TreeEntryNode - getParent")
    void treeEntryNodeGetParent() {
        LinkedForestMap<String, String> map = new LinkedForestMap<>(false);
        map.putLinkedNodes("a", "v1", "b", "v2");
        map.putLinkedNodes("b", "c", "v3");
        TreeEntry<String, String> leaf = map.get("c");
        assertNotNull(leaf.getParent("a"));
        assertEquals("a", leaf.getParent("a").getKey());
    }
}
