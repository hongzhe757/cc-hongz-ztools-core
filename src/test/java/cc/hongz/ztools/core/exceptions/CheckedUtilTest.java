package cc.hongz.ztools.core.exceptions;

import cc.hongz.ztools.core.lang.func.Func1;
import cc.hongz.ztools.core.lang.func.VoidFunc0;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CheckedUtil 单元测试.
 */
@DisplayName("CheckedUtil 受检异常转换")
public class CheckedUtilTest {

    @Test
    @DisplayName("睡眠测试")
    public void sleepTest() {
        VoidFunc0 func = () -> Thread.sleep(1000L);
        func.callWithRuntimeException();
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    @DisplayName("Supplier 测试")
    public void supplierTest() {
        File noFile = new File("./no-file");
        try {
            // 本行代码原本需要抛出受检查异常，现在只抛出运行时异常
            CheckedUtil.uncheck(() -> new FileInputStream(noFile)).call();
        } catch (Exception re) {
            assertTrue(re instanceof RuntimeException);
        }
    }

    @Test
    @DisplayName("Function 测试")
    public void functionTest() {
        Func1<String, String> afunc = (funcParam) -> {
            if (funcParam.length() > 5) {
                throw new Exception("这是受检查异常需要显示处理");
            }
            return funcParam.toUpperCase();
        };

        try {
            // 本行代码原本需要抛出受检查异常，现在只抛出运行时异常
            CheckedUtil.uncheck(afunc).call("hello world");
        } catch (Exception re) {
            assertTrue(re instanceof RuntimeException);
        }
    }
}
