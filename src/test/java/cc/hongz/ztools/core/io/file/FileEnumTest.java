package cc.hongz.ztools.core.io.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * file 子包枚举类测试
 * 覆盖：FileMode、LineSeparator
 */
@DisplayName("FileMode 与 LineSeparator 枚举单元测试")
class FileEnumTest {

    // ============================== FileMode ==============================

    /**
     * 测试 FileMode 枚举值
     */
    @DisplayName("FileMode values")
    @Test
    void fileModeValuesTest() {
        FileMode[] values = FileMode.values();
        assertEquals(4, values.length);
        assertNotNull(FileMode.r);
        assertNotNull(FileMode.rw);
        assertNotNull(FileMode.rws);
        assertNotNull(FileMode.rwd);
    }

    /**
     * 测试 FileMode valueOf
     */
    @DisplayName("FileMode valueOf")
    @Test
    void fileModeValueOfTest() {
        assertEquals(FileMode.r, FileMode.valueOf("r"));
        assertEquals(FileMode.rw, FileMode.valueOf("rw"));
        assertEquals(FileMode.rws, FileMode.valueOf("rws"));
        assertEquals(FileMode.rwd, FileMode.valueOf("rwd"));
    }

    // ============================== LineSeparator ==============================

    /**
     * 测试 LineSeparator 枚举值和换行符
     */
    @DisplayName("LineSeparator values")
    @Test
    void lineSeparatorValuesTest() {
        assertEquals("\r", LineSeparator.MAC.getValue());
        assertEquals("\n", LineSeparator.LINUX.getValue());
        assertEquals("\r\n", LineSeparator.WINDOWS.getValue());
    }

    /**
     * 测试 LineSeparator valueOf
     */
    @DisplayName("LineSeparator valueOf")
    @Test
    void lineSeparatorValueOfTest() {
        assertEquals(LineSeparator.MAC, LineSeparator.valueOf("MAC"));
        assertEquals(LineSeparator.LINUX, LineSeparator.valueOf("LINUX"));
        assertEquals(LineSeparator.WINDOWS, LineSeparator.valueOf("WINDOWS"));
    }

    /**
     * 测试 LineSeparator 枚举数量
     */
    @DisplayName("LineSeparator 数量")
    @Test
    void lineSeparatorCountTest() {
        assertEquals(3, LineSeparator.values().length);
    }
}
