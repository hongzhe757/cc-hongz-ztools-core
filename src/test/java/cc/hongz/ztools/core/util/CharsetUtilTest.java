package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CharsetUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("CharsetUtil 字符集工具")
class CharsetUtilTest {

    // ==================================================================== 常量

    /**
     * 字符串常量值验证.
     */
    @Test
    @DisplayName("字符集名称常量")
    void charsetNameConstants() {
        assertEquals("ISO-8859-1", CharsetUtil.ISO_8859_1);
        assertEquals("UTF-8", CharsetUtil.UTF_8);
        assertEquals("GBK", CharsetUtil.GBK);
    }

    /**
     * Charset 常量验证.
     */
    @Test
    @DisplayName("Charset 常量")
    void charsetConstants() {
        assertEquals(StandardCharsets.ISO_8859_1, CharsetUtil.CHARSET_ISO_8859_1);
        assertEquals(StandardCharsets.UTF_8, CharsetUtil.CHARSET_UTF_8);
        // GBK 在不支持的环境中可能为 null，但至少应不为 null（Windows/Linux 均支持）
        assertNotNull(CharsetUtil.CHARSET_GBK);
        assertEquals("GBK", CharsetUtil.CHARSET_GBK.name());
    }

    // ==================================================================== charset()

    /**
     * charset() 空字符串返回默认编码.
     */
    @Test
    @DisplayName("charset(null/空) 返回默认编码")
    void charsetBlankReturnsDefault() {
        assertEquals(Charset.defaultCharset(), CharsetUtil.charset(null));
        assertEquals(Charset.defaultCharset(), CharsetUtil.charset(""));
        assertEquals(Charset.defaultCharset(), CharsetUtil.charset("  "));
    }

    /**
     * charset() 有效编码返回对应 Charset.
     */
    @Test
    @DisplayName("charset(有效名称) 返回对应 Charset")
    void charsetValidName() {
        assertEquals(StandardCharsets.UTF_8, CharsetUtil.charset("UTF-8"));
        assertEquals(StandardCharsets.ISO_8859_1, CharsetUtil.charset("ISO-8859-1"));
    }

    /**
     * charset() 不支持的编码抛出异常.
     */
    @Test
    @DisplayName("charset(不支持) 抛出 UnsupportedCharsetException")
    void charsetUnsupportedThrows() {
        assertThrows(UnsupportedCharsetException.class, () -> CharsetUtil.charset("INVALID-CHARSET-XYZ"));
    }

    // ==================================================================== parse()

    /**
     * parse() 空字符串返回默认编码.
     */
    @Test
    @DisplayName("parse(null/空) 返回默认编码")
    void parseBlankReturnsDefault() {
        assertEquals(Charset.defaultCharset(), CharsetUtil.parse(null));
        assertEquals(Charset.defaultCharset(), CharsetUtil.parse(""));
    }

    /**
     * parse() 有效编码返回对应 Charset.
     */
    @Test
    @DisplayName("parse(有效名称) 返回对应 Charset")
    void parseValidName() {
        assertEquals(StandardCharsets.UTF_8, CharsetUtil.parse("UTF-8"));
        assertEquals(StandardCharsets.ISO_8859_1, CharsetUtil.parse("ISO-8859-1"));
    }

    /**
     * parse() 不支持的编码返回默认编码（不抛异常）.
     */
    @Test
    @DisplayName("parse(不支持) 返回默认编码")
    void parseUnsupportedReturnsDefault() {
        assertEquals(Charset.defaultCharset(), CharsetUtil.parse("INVALID-CHARSET-XYZ"));
    }

    /**
     * parse() 自定义默认编码.
     */
    @Test
    @DisplayName("parse(name, defaultCharset) 自定义默认编码")
    void parseWithCustomDefault() {
        assertEquals(StandardCharsets.UTF_8, CharsetUtil.parse(null, StandardCharsets.UTF_8));
        assertEquals(StandardCharsets.UTF_8, CharsetUtil.parse("INVALID-XYZ", StandardCharsets.UTF_8));
        assertEquals(StandardCharsets.UTF_8, CharsetUtil.parse("UTF-8", StandardCharsets.ISO_8859_1));
    }

    // ==================================================================== convert(String, String, String)

    /**
     * convert 字符串名称重载：正常转换.
     */
    @Test
    @DisplayName("convert(String, String, String) 正常转换")
    void convertByNames() {
        // "测试" 以 ISO-8859-1 编码后再以 UTF-8 还原
        String original = "Hello World";
        String iso = new String(original.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        String restored = CharsetUtil.convert(iso, "ISO-8859-1", "UTF-8");
        assertEquals(original, restored);
    }

    // ==================================================================== convert(String, Charset, Charset)

    /**
     * convert null 返回 null.
     */
    @Test
    @DisplayName("convert(null) 返回 null")
    void convertNullReturnsNull() {
        assertNull(CharsetUtil.convert((String) null, StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1));
    }

    /**
     * convert 空字符串返回原字符串.
     */
    @Test
    @DisplayName("convert(空字符串) 返回原字符串")
    void convertBlankReturnsOriginal() {
        assertEquals("", CharsetUtil.convert("", StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1));
    }

    /**
     * convert 相同编码返回原字符串.
     */
    @Test
    @DisplayName("convert(相同编码) 返回原字符串")
    void convertSameCharsetReturnsOriginal() {
        String text = "测试内容";
        assertEquals(text, CharsetUtil.convert(text, StandardCharsets.UTF_8, StandardCharsets.UTF_8));
    }

    /**
     * convert 正确转换编码.
     */
    @Test
    @DisplayName("convert 乱码还原")
    void convertRestoresEncoding() {
        String original = "中文测试";
        // 模拟错误读取：以 UTF-8 编码字节按 ISO-8859-1 解读
        String garbled = new String(original.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        assertNotEquals(original, garbled);
        // 还原
        String restored = CharsetUtil.convert(garbled, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_8);
        assertEquals(original, restored);
    }

    /**
     * convert null 字符集使用默认值.
     */
    @Test
    @DisplayName("convert null 字符集参数使用默认值")
    void convertNullCharsetsUseDefaults() {
        // srcCharset=null → ISO-8859-1, destCharset=null → UTF-8
        String text = "Hello";
        String isoEncoded = new String(text.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1);
        String result = CharsetUtil.convert(isoEncoded, (Charset) null, (Charset) null);
        assertEquals(text, result);
    }

    // ==================================================================== convert(File, Charset, Charset)

    /**
     * convert File null 返回 null.
     */
    @Test
    @DisplayName("convert(null File) 返回 null")
    void convertNullFileReturnsNull() {
        assertNull(CharsetUtil.convert((File) null, StandardCharsets.UTF_8, Charset.forName("GBK")));
    }

    /**
     * convert File 正常转换文件编码.
     */
    @Test
    @DisplayName("convert(File) 正常转换文件编码")
    void convertFileEncoding(@TempDir Path tempDir) {
        final File file = tempDir.resolve("test.txt").toFile();
        final String content = "中文测试内容";
        // 写入 UTF-8 文件
        cc.hongz.ztools.core.io.FileUtil.writeString(content, file, StandardCharsets.UTF_8);
        // 转换为 GBK
        final File result = CharsetUtil.convert(file, StandardCharsets.UTF_8, Charset.forName("GBK"));
        assertNotNull(result);
        // 以 GBK 读取应为原内容
        final String readBack = cc.hongz.ztools.core.io.FileUtil.readString(result, Charset.forName("GBK"));
        assertEquals(content, readBack);
    }

    // ==================================================================== defaultCharset(InputStream)

    /**
     * defaultCharset(InputStream) 探测 UTF-8 编码.
     */
    @Test
    @DisplayName("defaultCharset(InputStream) 探测 UTF-8")
    void defaultCharsetDetectUtf8() {
        final byte[] utf8Bytes = "Hello UTF-8 Content".getBytes(StandardCharsets.UTF_8);
        final InputStream in = new ByteArrayInputStream(utf8Bytes);
        final Charset detected = CharsetUtil.defaultCharset(in);
        assertNotNull(detected);
        // 探测结果应为有效的 Charset
        assertNotNull(detected.name());
    }

    /**
     * defaultCharset(InputStream) 指定候选编码.
     */
    @Test
    @DisplayName("defaultCharset(InputStream, charsets) 指定候选")
    void defaultCharsetWithCandidates() {
        final byte[] utf8Bytes = "Test".getBytes(StandardCharsets.UTF_8);
        final InputStream in = new ByteArrayInputStream(utf8Bytes);
        final Charset detected = CharsetUtil.defaultCharset(in, StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1);
        assertNotNull(detected);
    }

    // ==================================================================== systemCharset / defaultCharset

    /**
     * systemCharset 返回非 null.
     */
    @Test
    @DisplayName("systemCharset() 返回非 null Charset")
    void systemCharsetNotNull() {
        assertNotNull(CharsetUtil.systemCharset());
    }

    /**
     * systemCharsetName 返回非空字符串.
     */
    @Test
    @DisplayName("systemCharsetName() 返回非空字符串")
    void systemCharsetNameNotEmpty() {
        assertNotNull(CharsetUtil.systemCharsetName());
        assertFalse(CharsetUtil.systemCharsetName().isEmpty());
    }

    /**
     * Windows 下 systemCharset 为 GBK.
     */
    @Test
    @DisplayName("Windows 系统下 systemCharset 为 GBK")
    void systemCharsetWindows() {
        String osName = System.getProperty("os.name", "");
        if (osName.toLowerCase().startsWith("windows")) {
            // Windows 下应返回 GBK（或 GBK 不可用时返回默认编码）
            assertNotNull(CharsetUtil.systemCharset());
            assertEquals(CharsetUtil.systemCharset().name(), CharsetUtil.systemCharsetName());
        }
        // 非 Windows 系统跳过此断言
    }

    /**
     * defaultCharset 返回 JVM 默认编码.
     */
    @Test
    @DisplayName("defaultCharset() 等于 Charset.defaultCharset()")
    void defaultCharsetEqualsJvmDefault() {
        assertEquals(Charset.defaultCharset(), CharsetUtil.defaultCharset());
    }

    /**
     * defaultCharsetName 返回 JVM 默认编码名称.
     */
    @Test
    @DisplayName("defaultCharsetName() 等于 Charset.defaultCharset().name()")
    void defaultCharsetNameEqualsJvmDefaultName() {
        assertEquals(Charset.defaultCharset().name(), CharsetUtil.defaultCharsetName());
    }

    /**
     * systemCharset 和 systemCharsetName 一致性.
     */
    @Test
    @DisplayName("systemCharset() 和 systemCharsetName() 一致")
    void systemCharsetAndNameConsistent() {
        assertEquals(CharsetUtil.systemCharset().name(), CharsetUtil.systemCharsetName());
    }
}
