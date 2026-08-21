package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link Console} 单元测试.
 * <p>
 * 通过重定向标准输出/错误流验证输出行为.
 *
 * @author hongzhe.qin
 */
@DisplayName("Console 控制台输出")
class ConsoleTest {

    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream outBuffer;
    private ByteArrayOutputStream errBuffer;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        originalErr = System.err;
        outBuffer = new ByteArrayOutputStream();
        errBuffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outBuffer, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(errBuffer, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // ---- log ----

    /**
     * 验证 {@link Console#log(Object...)} 多个参数以空格分隔并换行输出.
     */
    @Test
    void logMultipleValues() {
        Console.log("a", 1);
        assertEquals("a 1" + System.lineSeparator(), outBuffer.toString(StandardCharsets.UTF_8));
    }

    /**
     * 验证 {@link Console#log(Object...)} 无参数时仅输出换行.
     */
    @Test
    void logEmpty() {
        Console.log();
        assertEquals(System.lineSeparator(), outBuffer.toString(StandardCharsets.UTF_8));
    }

    // ---- error ----

    @Test
    void errorNoArg() {
        Console.error();
        assertEquals(System.lineSeparator(), errBuffer.toString(StandardCharsets.UTF_8));
    }

    @Test
    void errorWithObject() {
        Console.error("hello");
        assertTrue(errBuffer.toString(StandardCharsets.UTF_8).contains("hello"));
    }

    @Test
    void errorWithThrowable() {
        Console.error(new RuntimeException("boom"));
        final String out = errBuffer.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("boom"));
        // 异常信息中应包含堆栈帧
        assertTrue(out.contains("RuntimeException"));
    }

    @Test
    void errorWithTemplate() {
        Console.error("value={}", 42);
        assertTrue(errBuffer.toString(StandardCharsets.UTF_8).contains("value=42"));
    }

    @Test
    void errorWithThrowableAndTemplate() {
        Console.error(new RuntimeException("root"), "msg={}", "test");
        final String out = errBuffer.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("msg=test"));
        assertTrue(out.contains("RuntimeException"));
    }

    @Test
    void errorMultipleObjects() {
        Console.error("a", "b", "c");
        assertTrue(errBuffer.toString(StandardCharsets.UTF_8).contains("a"));
    }
}
