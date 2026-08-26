package cc.hongz.ztools.core.lang.generator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link Generator} 及各实现类单元测试
 */
public @DisplayName("Generator 生成器单元测试")
class GeneratorTest {

    @DisplayName("Generator 接口")
    @Test
    public void generatorInterfaceTest() {
        Generator<Integer> gen = new Generator<Integer>() {
            private int count = 0;
            @Override
            public Integer next() {
                return ++count;
            }
        };
        assertEquals(1, gen.next());
        assertEquals(2, gen.next());
    }

    @DisplayName("ObjectGenerator")
    @Test
    public void objectGeneratorTest() {
        ObjectGenerator<StringBuilder> gen = new ObjectGenerator<>(StringBuilder.class);
        StringBuilder sb = gen.next();
        assertNotNull(sb);
        assertEquals(0, sb.length());
    }

    @DisplayName("ObjectIdGenerator")
    @Test
    public void objectIdGeneratorTest() {
        ObjectIdGenerator gen = new ObjectIdGenerator();
        String id1 = gen.next();
        String id2 = gen.next();
        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
        // ObjectId 长度为 24
        assertEquals(24, id1.length());
    }

    @DisplayName("SnowflakeGenerator")
    @Test
    public void snowflakeGeneratorTest() {
        SnowflakeGenerator gen = new SnowflakeGenerator();
        Long id1 = gen.next();
        Long id2 = gen.next();
        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
        assertTrue(id1 > 0);
    }

    @DisplayName("SnowflakeGenerator 自定义 ID")
    @Test
    public void snowflakeGeneratorWithCustomIdTest() {
        SnowflakeGenerator gen = new SnowflakeGenerator(1, 1);
        Long id = gen.next();
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @DisplayName("UUIDGenerator")
    @Test
    public void uuidGeneratorTest() {
        UUIDGenerator gen = new UUIDGenerator();
        String uuid1 = gen.next();
        String uuid2 = gen.next();
        assertNotNull(uuid1);
        assertNotNull(uuid2);
        assertNotEquals(uuid1, uuid2);
        // UUID 格式: 8-4-4-4-12 = 36 字符
        assertEquals(36, uuid1.length());
        assertTrue(uuid1.contains("-"));
    }
}
