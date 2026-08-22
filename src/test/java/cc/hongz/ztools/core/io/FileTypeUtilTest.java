package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link FileTypeUtil} 单元测试.
 */
@DisplayName("FileTypeUtil 文件类型判断")
class FileTypeUtilTest {

    @Test
    @DisplayName("按文件名获取扩展名")
    void getFileTypeFromName() {
        assertEquals("txt", FileTypeUtil.getFileType("demo.TXT"));
    }

    @Test
    @DisplayName("魔数识别 png")
    void detectPngByMagic() {
        final byte[] pngHeader = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
        assertEquals("png", FileTypeUtil.getTypeByMagic(pngHeader));
        assertEquals("png", FileTypeUtil.getType(pngHeader));
    }

    @Test
    @DisplayName("魔数识别 jpg")
    void detectJpegByMagic() {
        final byte[] jpegHeader = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10};
        assertEquals("jpg", FileTypeUtil.getTypeByMagic(jpegHeader));
    }

    @Test
    @DisplayName("魔数识别 zip")
    void detectZipByMagic() {
        final byte[] zipHeader = new byte[]{0x50, 0x4B, 0x03, 0x04};
        assertEquals("zip", FileTypeUtil.getType(zipHeader));
    }

    @Test
    @DisplayName("getFileExtendName 回退 jpg")
    void getFileExtendNameFallbackJpg() {
        assertEquals("jpg", FileTypeUtil.getFileExtendName(new byte[]{0x00}));
    }

    @Test
    @DisplayName("空字节返回 null")
    void getTypeByMagicNullForEmpty() {
        assertNull(FileTypeUtil.getTypeByMagic(new byte[0]));
    }

    @Test
    @DisplayName("扩展名回退识别")
    void getTypeWithFileNameFallback() {
        assertEquals("txt", FileTypeUtil.getType(new byte[]{0x68, 0x69}, "readme.txt"));
    }
}
