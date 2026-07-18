package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.io.FastByteArrayOutputStream;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.io.ValidateObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * 序列化工具类.
 *
 * <p>
 * 注意！此工具类依赖于JDK的序列化机制，某些版本的JDK中可能存在远程注入漏洞。
 * </p>
 *
 * @author looly
 * @since 5.6.3
 */
public class SerializeUtil {

    /**
     * 序列化后拷贝流的方式克隆.
     *
     * <p>对象必须实现Serializable接口</p>
     *
     * @param <T> 对象类型
     * @param obj 被克隆对象
     * @return 克隆后的对象
     * @throws UtilException IO异常和ClassNotFoundException封装
     */
    public static <T> T clone(T obj) {
        if (false == (obj instanceof Serializable)) {
            return null;
        }
        return deserialize(serialize(obj));
    }

    /**
     * 序列化.
     *
     * <p>对象必须实现Serializable接口</p>
     *
     * @param <T> 对象类型
     * @param obj 要被序列化的对象
     * @return 序列化后的字节码
     */
    public static <T> byte[] serialize(T obj) {
        if (false == (obj instanceof Serializable)) {
            return null;
        }
        final FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
        IoUtil.writeObjects(byteOut, false, (Serializable) obj);
        return byteOut.toByteArray();
    }

    /**
     * 反序列化.
     *
     * <p>对象必须实现Serializable接口</p>
     *
     * <p>
     * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
     * </p>
     *
     * @param <T>           对象类型
     * @param bytes         反序列化的字节码
     * @param acceptClasses 白名单的类，为空时不限制
     * @return 反序列化后的对象
     */
    public static <T> T deserialize(byte[] bytes, Class<?>... acceptClasses) {
        try {
            final ValidateObjectInputStream validateStream = new ValidateObjectInputStream(
                    new ByteArrayInputStream(bytes));
            // 指定白名单类时，将类及其继承链全部加入白名单
            if (acceptClasses != null && acceptClasses.length > 0) {
                for (Class<?> clazz : acceptClasses) {
                    addClassHierarchy(validateStream, clazz);
                }
            }
            return IoUtil.readObj(validateStream, null);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 将类及其所有父类、接口加入白名单.
     *
     * @param stream ValidateObjectInputStream
     * @param clazz  要加入的类
     */
    private static void addClassHierarchy(ValidateObjectInputStream stream, Class<?> clazz) {
        if (clazz == null || clazz == Object.class) {
            return;
        }
        stream.accept(clazz);
        // 递归加入父类
        addClassHierarchy(stream, clazz.getSuperclass());
        // 递归加入接口
        for (Class<?> iface : clazz.getInterfaces()) {
            addClassHierarchy(stream, iface);
        }
    }
}
