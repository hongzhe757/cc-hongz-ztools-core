package cc.hongz.ztools.core.annotation.scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link EmptyAnnotationScanner} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("EmptyAnnotationScanner 空扫描器")
class EmptyAnnotationScannerTest {

    /**
     * 空扫描器支持任意可注解元素.
     */
    @Test
    @DisplayName("支持任意元素")
    void support() {
        assertTrue(new EmptyAnnotationScanner().support(EmptyAnnotationScannerTest.class));
    }

    /**
     * 空扫描器不返回任何注解.
     */
    @Test
    @DisplayName("不扫描任何注解")
    void getAnnotations() {
        List<Annotation> annotations = new EmptyAnnotationScanner().getAnnotations(EmptyAnnotationScannerTest.class);
        assertTrue(annotations.isEmpty());
    }
}
