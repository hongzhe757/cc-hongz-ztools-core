package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link Console} 单元测试.
 * <p>
 * 通过重定向标准输出验证 log 行为.
 *
 * @author hongzhe.qin
 */
class ConsoleTest {

    /**
     * 验证 {@link Console#log(Object...)} 多个参数以空格分隔并换行输出.
     */
    @Test
    void logMultipleValues() {
        final PrintStream original = System.out;
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            Console.log("a", 1);
            assertEquals("a 1" + System.lineSeparator(), buffer.toString(StandardCharsets.UTF_8));
        } finally {
            System.setOut(original);
        }
    }

    /**
     * 验证 {@link Console#log(Object...)} 无参数时仅输出换行.
     */
    @Test
    void logEmpty() {
        final PrintStream original = System.out;
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            Console.log();
            assertEquals(System.lineSeparator(), buffer.toString(StandardCharsets.UTF_8));
        } finally {
            System.setOut(original);
        }
    }
}
