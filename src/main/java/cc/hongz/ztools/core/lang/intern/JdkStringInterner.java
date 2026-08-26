package cc.hongz.ztools.core.lang.intern;

/**
 * JDK中默认的字符串规范化实现.
 *
 * @author looly
 * @since 5.4.3
 */
public class JdkStringInterner implements Interner<String> {

    /**
     * 返回指定对象对应的规范化对象。
     *
     * @param sample 对象
     * @return 规范化对象
     */
    @Override
    public String intern(final String sample) {
        if (null == sample) {
            return null;
        }
        return sample.intern();
    }
}
