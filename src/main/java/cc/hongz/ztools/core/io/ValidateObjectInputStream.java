package cc.hongz.ztools.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashSet;
import java.util.Set;

/**
 * 带有类验证的对象流，用于避免反序列化漏洞.
 *
 * <p>
 * 通过黑白名单机制过滤反序列化类，防止反序列化远程代码执行漏洞.
 * 详细见：https://xz.aliyun.com/t/41/
 *
 * <p>
 * 设计模式：装饰器（Decorator），装饰 {@link ObjectInputStream}，增加类验证能力.
 *
 * @author looly
 */
public class ValidateObjectInputStream extends ObjectInputStream {

    /**
     * 白名单类集合.
     */
    private Set<String> whiteClassSet;

    /**
     * 黑名单类集合.
     */
    private Set<String> blackClassSet;

    /**
     * 构造.
     *
     * @param inputStream   输入流
     * @param acceptClasses 白名单的类
     * @throws IOException IO 异常
     */
    public ValidateObjectInputStream(final InputStream inputStream, final Class<?>... acceptClasses) throws IOException {
        super(inputStream);
        accept(acceptClasses);
    }

    /**
     * 禁止反序列化的类，用于反序列化验证.
     *
     * @param refuseClasses 禁止反序列化的类
     */
    public void refuse(final Class<?>... refuseClasses) {
        if (null == this.blackClassSet) {
            this.blackClassSet = new HashSet<>();
        }
        for (final Class<?> refuseClass : refuseClasses) {
            this.blackClassSet.add(refuseClass.getName());
        }
    }

    /**
     * 接受反序列化的类，用于反序列化验证.
     *
     * @param acceptClasses 接受反序列化的类
     */
    public void accept(final Class<?>... acceptClasses) {
        if (null == this.whiteClassSet) {
            this.whiteClassSet = new HashSet<>();
        }
        for (final Class<?> acceptClass : acceptClasses) {
            this.whiteClassSet.add(acceptClass.getName());
        }
    }

    /**
     * 只允许反序列化白名单中的类.
     */
    @Override
    protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        validateClassName(desc.getName());
        return super.resolveClass(desc);
    }

    /**
     * 验证反序列化的类是否合法.
     *
     * @param className 类名
     * @throws InvalidClassException 非法类
     */
    private void validateClassName(final String className) throws InvalidClassException {
        // 黑名单检查
        if (this.blackClassSet != null && !this.blackClassSet.isEmpty()) {
            if (this.blackClassSet.contains(className)) {
                throw new InvalidClassException("Unauthorized deserialization attempt by black list", className);
            }
        }

        // 白名单为空时放行，否则必须在白名单中
        if (this.whiteClassSet == null || this.whiteClassSet.isEmpty() || this.whiteClassSet.contains(className)) {
            return;
        }

        throw new InvalidClassException("Unauthorized deserialization attempt", className);
    }
}
