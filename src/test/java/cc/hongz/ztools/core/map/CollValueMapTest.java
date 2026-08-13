package cc.hongz.ztools.core.map;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.collection.ListUtil;
import cc.hongz.ztools.core.map.multi.ListValueMap;
import cc.hongz.ztools.core.map.multi.SetValueMap;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class CollValueMapTest {

    @Test
    public void testListValueMapRemove() {
        final ListValueMap<String, String> entries = new ListValueMap<>();
        entries.putValue("one","11");
        entries.putValue("one","22");
        entries.putValue("one","33");
        entries.putValue("one","22");

        entries.putValue("two","44");
        entries.putValue("two","55");

        entries.putValue("three","11");

        entries.removeValue("one","22");

        assertEquals(ListUtil.of("11","33","22"), entries.get("one"));

        entries.removeValues("two",ListUtil.of("44","55"));
        assertEquals(ListUtil.empty(),entries.get("two"));
    }

    @Test
    public void testSetValueMapRemove() {
        final SetValueMap<String, String> entries = new SetValueMap<>();
        entries.putValue("one","11");
        entries.putValue("one","22");
        entries.putValue("one","33");
        entries.putValue("one","22");

        entries.putValue("two","44");
        entries.putValue("two","55");

        entries.putValue("three","11");

        entries.removeValue("one","22");
        assertEquals(CollUtil.newHashSet("11","33"), entries.get("one"));

        entries.removeValues("two",ListUtil.of("44","55"));
        assertEquals(CollUtil.empty(HashSet.class),entries.get("two"));
    }

}
