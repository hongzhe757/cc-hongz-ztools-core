package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link FileMagicNumber} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("FileMagicNumber 文件类型魔数")
class FileMagicNumberTest {

    @Test
    @DisplayName("识别 PNG 魔数")
    void detectPng() {
        final byte[] png = {(byte) 0x89, 0x50, 0x4E, 0x47};
        assertEquals(FileMagicNumber.PNG, FileMagicNumber.getMagicNumber(png));
    }

    @Test
    @DisplayName("识别 ZIP 魔数")
    void detectZip() {
        final byte[] zip = {0x50, 0x4B, 0x03, 0x04};
        assertEquals(FileMagicNumber.ZIP, FileMagicNumber.getMagicNumber(zip));
    }
}
