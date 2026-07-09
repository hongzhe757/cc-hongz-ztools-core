package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link StrBuilder} 单元测试.
 * <p>
 * 覆盖链式 append 与 toString 输出.
 *
 * @author hongzhe.qin
 */
class StrBuilderTest {

    /**
     * 验证 append 多种类型后 toString 拼接正确.
     */
    @Test
    void appendAndToString() {
        final StrBuilder builder = new StrBuilder();
        builder.append("a").append(1);
        assertEquals("a1", builder.toString());
    }
}
