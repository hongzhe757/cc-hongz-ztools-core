package cc.hongz.ztools.core.io.resource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link BytesResource}、{@link StringResource}、{@link CharSequenceResource} 单元测试.
 */
@DisplayName("Resource 类型单元测试")
class ResourceTypesTest {

    @DisplayName("BytesResource 读取内容")
    @Test
    void bytesResourceReadsContent() throws IOException {
        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        BytesResource resource = new BytesResource(data);

        try (InputStream is = resource.getStream()) {
            assertArrayEquals(data, is.readAllBytes());
        }
        assertArrayEquals(data, resource.readBytes());
    }

    @DisplayName("BytesResource 获取名称")
    @Test
    void bytesResourceGetName() {
        byte[] data = {1, 2, 3};
        BytesResource resource = new BytesResource(data, "test-bytes");
        assertEquals("test-bytes", resource.getName());
    }

    @DisplayName("StringResource 读取内容")
    @Test
    void stringResourceReadsContent() throws IOException {
        String text = "你好世界";
        StringResource resource = new StringResource(text);

        assertNotNull(resource.getStream());
        try (InputStream is = resource.getStream()) {
            String result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertEquals(text, result);
        }
    }

    @DisplayName("StringResource 带编码")
    @Test
    void stringResourceWithCharset() throws IOException {
        String text = "hello charset";
        StringResource resource = new StringResource(text, "name", StandardCharsets.UTF_8);

        try (InputStream is = resource.getStream()) {
            String result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertEquals(text, result);
        }
    }

    @DisplayName("CharSequenceResource 读取内容")
    @Test
    void charSequenceResourceReadsContent() throws IOException {
        CharSequence cs = "test content";
        CharSequenceResource resource = new CharSequenceResource(cs);

        assertNotNull(resource.getStream());
        assertEquals(cs.toString(), resource.readStr(StandardCharsets.UTF_8));
    }

    @DisplayName("CharSequenceResource 获取名称")
    @Test
    void charSequenceResourceGetName() {
        CharSequenceResource resource = new CharSequenceResource("data", "my-name");
        assertEquals("my-name", resource.getName());
    }
}
