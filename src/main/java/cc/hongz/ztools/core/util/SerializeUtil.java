package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.io.FastByteArrayOutputStream;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.ValidateObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 序列化工具类.
 *
 * <p>
 * 注意！此工具类依赖于 JDK 的序列化机制，某些版本的 JDK 中可能存在远程注入漏洞.
 *
 * <p>
 * 设计模式：工具类（Utility Class），私有构造禁止实例化，静态方法聚合序列化能力.
 *
 * @author looly
 */
public class SerializeUtil {

    /**
     * 私有构造，禁止实例化.
     */
    private SerializeUtil() {
    }

    /**
     * 序列化后拷贝流的方式克隆.
     * 对象必须实现 {@link Serializable} 接口.
     *
     * @param <T> 对象类型
     * @param obj 被克隆对象
     * @return 克隆后的对象，非 Serializable 返回 null
     * @throws UtilException IO 异常和 ClassNotFoundException 封装
     */
    public static <T> T clone(final T obj) {
        if (!(obj instanceof Serializable)) {
            return null;
        }
        return deserialize(serialize(obj));
    }

    /**
     * 序列化.
     * 对象必须实现 {@link Serializable} 接口.
     *
     * @param <T> 对象类型
     * @param obj 要被序列化的对象
     * @return 序列化后的字节码，非 Serializable 返回 null
     */
    public static <T> byte[] serialize(final T obj) {
        if (!(obj instanceof Serializable)) {
            return null;
        }
        final FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(byteOut)) {
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return byteOut.toByteArray();
    }

    /**
     * 反序列化.
     * 对象必须实现 {@link Serializable} 接口.
     *
     * <p>
     * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
     * </p>
     *
     * @param <T>           对象类型
     * @param bytes         反序列化的字节码
     * @param acceptClasses 白名单的类
     * @return 反序列化后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(final byte[] bytes, final Class<?>... acceptClasses) {
        try {
            return (T) new ValidateObjectInputStream(
                    new ByteArrayInputStream(bytes), acceptClasses).readObject();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new UtilException(e);
        }
    }
}
