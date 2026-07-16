package cc.hongz.ztools.core.lang;

/**
 * 责任链接口.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.Chain}.
 *
 * @param <E> 元素类型
 * @param <T> 目标类类型，用于返回this对象
 * @author looly
 */
public interface Chain<E, T> extends Iterable<E> {
    /**
     * 加入责任链.
     *
     * @param element 责任链新的环节元素
     * @return this
     */
    T addChain(E element);
}
