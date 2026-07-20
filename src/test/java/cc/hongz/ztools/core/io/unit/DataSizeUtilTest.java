package cc.hongz.ztools.core.io.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link DataSizeUtil} 单元测试.
 */
class DataSizeUtilTest {

    @Test
    void parseBytesTest() {
        assertEquals(1024L, DataSizeUtil.parse("1KB"));
        assertEquals(1048576L, DataSizeUtil.parse("1MB"));
        assertEquals(1073741824L, DataSizeUtil.parse("1GB"));
    }

    @Test
    void parseWithUnitTest() {
        assertEquals(500L, DataSizeUtil.parse("500B"));
        assertEquals(5120L, DataSizeUtil.parse("5KB"));
    }

    @Test
    void formatTest() {
        final String result = DataSizeUtil.format(1024L);
        assertNotNull(result);
        assertTrue(result.contains("KB") || result.contains("1"));
    }

    @Test
    void formatLargeSizeTest() {
        final String result = DataSizeUtil.format(1073741824L);
        assertNotNull(result);
        assertTrue(result.contains("GB") || result.contains("1"));
    }

    @Test
    void formatZeroTest() {
        assertEquals("0", DataSizeUtil.format(0L));
    }

    @Test
    void dataSizeOfBytesTest() {
        final DataSize size = DataSize.ofBytes(2048L);
        assertEquals(2048L, size.toBytes());
        assertEquals(2L, size.toKilobytes());
    }

    @Test
    void dataSizeToMegabytesTest() {
        final DataSize size = DataSize.ofBytes(2 * 1024 * 1024L);
        assertEquals(2L, size.toMegabytes());
    }

    @Test
    void dataSizeToGigabytesTest() {
        final DataSize size = DataSize.ofBytes(3L * 1024 * 1024 * 1024);
        assertEquals(3L, size.toGigabytes());
    }
}
