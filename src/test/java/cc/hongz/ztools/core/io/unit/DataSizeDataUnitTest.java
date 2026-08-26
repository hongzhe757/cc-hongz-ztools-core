package cc.hongz.ztools.core.io.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit 子包测试
 * 覆盖：DataSize、DataUnit
 */
@DisplayName("DataSize 与 DataUnit 单元测试")
class DataSizeDataUnitTest {

    // ============================== DataUnit ==============================

    /**
     * 测试 DataUnit 后缀
     */
    @DisplayName("DataUnit 后缀")
    @Test
    void dataUnitSuffixTest() {
        assertEquals("B", DataUnit.BYTES.getSuffix());
        assertEquals("KB", DataUnit.KILOBYTES.getSuffix());
        assertEquals("MB", DataUnit.MEGABYTES.getSuffix());
        assertEquals("GB", DataUnit.GIGABYTES.getSuffix());
        assertEquals("TB", DataUnit.TERABYTES.getSuffix());
    }

    /**
     * 测试 DataUnit.fromSuffix 正常解析
     */
    @DisplayName("DataUnit.fromSuffix 正常解析")
    @Test
    void dataUnitFromSuffixTest() {
        assertEquals(DataUnit.BYTES, DataUnit.fromSuffix("B"));
        assertEquals(DataUnit.KILOBYTES, DataUnit.fromSuffix("KB"));
        assertEquals(DataUnit.MEGABYTES, DataUnit.fromSuffix("MB"));
        assertEquals(DataUnit.GIGABYTES, DataUnit.fromSuffix("GB"));
        assertEquals(DataUnit.TERABYTES, DataUnit.fromSuffix("TB"));
    }

    /**
     * 测试 DataUnit.fromSuffix 大小写不敏感
     */
    @DisplayName("DataUnit.fromSuffix 大小写不敏感")
    @Test
    void dataUnitFromSuffixCaseInsensitiveTest() {
        assertEquals(DataUnit.KILOBYTES, DataUnit.fromSuffix("kb"));
        assertEquals(DataUnit.MEGABYTES, DataUnit.fromSuffix("mb"));
        assertEquals(DataUnit.KILOBYTES, DataUnit.fromSuffix("K"));
    }

    /**
     * 测试 DataUnit.fromSuffix KiB/MiB 兼容
     */
    @DisplayName("DataUnit.fromSuffix KiB/MiB 兼容")
    @Test
    void dataUnitFromSuffixIBFormatTest() {
        assertEquals(DataUnit.KILOBYTES, DataUnit.fromSuffix("KiB"));
        assertEquals(DataUnit.MEGABYTES, DataUnit.fromSuffix("MiB"));
        assertEquals(DataUnit.GIGABYTES, DataUnit.fromSuffix("GiB"));
    }

    /**
     * 测试 DataUnit.fromSuffix 未知后缀抛异常
     */
    @DisplayName("DataUnit.fromSuffix 未知后缀抛异常")
    @Test
    void dataUnitFromSuffixUnknownTest() {
        assertThrows(IllegalArgumentException.class, () -> DataUnit.fromSuffix("XYZ"));
    }

    // ============================== DataSize ==============================

    /**
     * 测试 DataSize.ofBytes
     */
    @DisplayName("DataSize.ofBytes")
    @Test
    void dataSizeOfBytesTest() {
        DataSize ds = DataSize.ofBytes(1024);
        assertEquals(1024, ds.toBytes());
    }

    /**
     * 测试 DataSize.ofKilobytes
     */
    @DisplayName("DataSize.ofKilobytes")
    @Test
    void dataSizeOfKilobytesTest() {
        DataSize ds = DataSize.ofKilobytes(1);
        assertEquals(1024, ds.toBytes());
    }

    /**
     * 测试 DataSize.ofMegabytes
     */
    @DisplayName("DataSize.ofMegabytes")
    @Test
    void dataSizeOfMegabytesTest() {
        DataSize ds = DataSize.ofMegabytes(1);
        assertEquals(1048576, ds.toBytes());
    }

    /**
     * 测试 DataSize.ofGigabytes
     */
    @DisplayName("DataSize.ofGigabytes")
    @Test
    void dataSizeOfGigabytesTest() {
        DataSize ds = DataSize.ofGigabytes(1);
        assertEquals(1073741824L, ds.toBytes());
    }

    /**
     * 测试 DataSize.ofTerabytes
     */
    @DisplayName("DataSize.ofTerabytes")
    @Test
    void dataSizeOfTerabytesTest() {
        DataSize ds = DataSize.ofTerabytes(1);
        assertEquals(1099511627776L, ds.toBytes());
    }

    /**
     * 测试 DataSize.of 带 DataUnit
     */
    @DisplayName("DataSize.of 带 DataUnit")
    @Test
    void dataSizeOfWithDataUnitTest() {
        DataSize ds = DataSize.of(5, DataUnit.MEGABYTES);
        assertEquals(5 * 1048576L, ds.toBytes());
    }

    /**
     * 测试 DataSize.of 带 BigDecimal
     */
    @DisplayName("DataSize.of 带 BigDecimal")
    @Test
    void dataSizeOfBigDecimalTest() {
        DataSize ds = DataSize.of(new BigDecimal("2.5"), DataUnit.KILOBYTES);
        assertEquals(2560, ds.toBytes());
    }

    /**
     * 测试 DataSize.parse 基本解析
     */
    @DisplayName("DataSize.parse 基本解析")
    @Test
    void dataSizeParseTest() {
        assertEquals(12288, DataSize.parse("12KB").toBytes());
        assertEquals(5242880, DataSize.parse("5MB").toBytes());
        assertEquals(20, DataSize.parse("20").toBytes());
    }

    /**
     * 测试 DataSize.parse 带默认单位
     */
    @DisplayName("DataSize.parse 带默认单位")
    @Test
    void dataSizeParseWithDefaultUnitTest() {
        DataSize ds = DataSize.parse("10", DataUnit.KILOBYTES);
        assertEquals(10240, ds.toBytes());
    }

    /**
     * 测试 DataSize.parse 无效输入抛异常
     */
    @DisplayName("DataSize.parse 无效输入抛异常")
    @Test
    void dataSizeParseInvalidTest() {
        assertThrows(IllegalArgumentException.class, () -> DataSize.parse("abc"));
    }

    /**
     * 测试 DataSize 负值
     */
    @DisplayName("DataSize 负值")
    @Test
    void dataSizeNegativeTest() {
        DataSize ds = DataSize.ofBytes(-100);
        assertEquals(-100, ds.toBytes());
    }

    /**
     * 测试 DataSize compareTo
     */
    @DisplayName("DataSize compareTo")
    @Test
    void dataSizeCompareToTest() {
        DataSize ds1 = DataSize.ofBytes(100);
        DataSize ds2 = DataSize.ofBytes(200);
        assertTrue(ds1.compareTo(ds2) < 0);
        assertTrue(ds2.compareTo(ds1) > 0);
        assertEquals(0, ds1.compareTo(DataSize.ofBytes(100)));
    }

    /**
     * 测试 DataSize equals
     */
    @DisplayName("DataSize equals")
    @Test
    void dataSizeEqualsTest() {
        DataSize ds1 = DataSize.ofKilobytes(1);
        DataSize ds2 = DataSize.ofBytes(1024);
        assertEquals(ds1, ds2);
    }

    /**
     * 测试 DataSize toString
     */
    @DisplayName("DataSize toString")
    @Test
    void dataSizeToStringTest() {
        DataSize ds = DataSize.ofBytes(1024);
        assertNotNull(ds.toString());
    }
}
