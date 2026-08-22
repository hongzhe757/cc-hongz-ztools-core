package cc.hongz.ztools.core.annotation.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * 默认不扫描任何元素的扫描器.
 *
 * @author huangchengxing
 */
public class EmptyAnnotationScanner implements AnnotationScanner {

    @Override
    public boolean support(final AnnotatedElement annotatedEle) {
        return true;
    }

    @Override
    public List<Annotation> getAnnotations(final AnnotatedElement annotatedEle) {
        return Collections.emptyList();
    }

    @Override
    public void scan(final BiConsumer<Integer, Annotation> consumer,  final AnnotatedElement annotatedEle,  final Predicate<Annotation> filter) {
        // do nothing
    }
}
