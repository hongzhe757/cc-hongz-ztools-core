package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.JarClassLoader;

import java.io.File;
import java.lang.reflect.Array;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ClassLoader} 工具类.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.util.ClassLoaderUtil}。
 *
 * @author Looly
 * @author hongzhe.qin (migrated &amp; enhanced)
 * @since 3.0.9
 */
public final class ClassLoaderUtil {

    /**
     * 数组类的结尾符: "[]".
     */
    private static final String ARRAY_SUFFIX = "[]";

    /**
     * 内部数组类名前缀: "[".
     */
    private static final String INTERNAL_ARRAY_PREFIX = "[";

    /**
     * 内部非原始类型类名前缀: "[L".
     */
    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";

    /**
     * 包名分界符: '.'.
     */
    private static final char PACKAGE_SEPARATOR = '.';

    /**
     * 内部类分界符: '$'.
     */
    private static final char INNER_CLASS_SEPARATOR = '$';

    /**
     * 原始类型名和其 class 对应表，例如：int → int.class.
     */
    private static final Map<String, Class<?>> PRIMITIVE_TYPE_NAME_MAP = new ConcurrentHashMap<>(32);

    static {
        final List<Class<?>> primitiveTypes = new ArrayList<>(32);
        // 加入原始类型及其包装类
        primitiveTypes.add(boolean.class);
        primitiveTypes.add(Boolean.class);
        primitiveTypes.add(byte.class);
        primitiveTypes.add(Byte.class);
        primitiveTypes.add(char.class);
        primitiveTypes.add(Character.class);
        primitiveTypes.add(double.class);
        primitiveTypes.add(Double.class);
        primitiveTypes.add(float.class);
        primitiveTypes.add(Float.class);
        primitiveTypes.add(int.class);
        primitiveTypes.add(Integer.class);
        primitiveTypes.add(long.class);
        primitiveTypes.add(Long.class);
        primitiveTypes.add(short.class);
        primitiveTypes.add(Short.class);
        // 加入原始类型数组类型
        primitiveTypes.add(boolean[].class);
        primitiveTypes.add(byte[].class);
        primitiveTypes.add(char[].class);
        primitiveTypes.add(double[].class);
        primitiveTypes.add(float[].class);
        primitiveTypes.add(int[].class);
        primitiveTypes.add(long[].class);
        primitiveTypes.add(short[].class);
        primitiveTypes.add(void.class);
        for (final Class<?> primitiveType : primitiveTypes) {
            PRIMITIVE_TYPE_NAME_MAP.put(primitiveType.getName(), primitiveType);
        }
    }

    /**
     * 私有构造，禁止实例化.
     */
    private ClassLoaderUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * 获取当前线程的 {@link ClassLoader}.
     *
     * @return 当前线程的 class loader
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        } else {
            // 绕开权限检查
            return AccessController.doPrivileged(
                    (PrivilegedAction<ClassLoader>) () -> Thread.currentThread().getContextClassLoader());
        }
    }

    /**
     * 获取系统 {@link ClassLoader}.
     *
     * @return 系统 {@link ClassLoader}
     * @see ClassLoader#getSystemClassLoader()
     * @since 5.7.0
     */
    public static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        } else {
            // 绕开权限检查
            return AccessController.doPrivileged(
                    (PrivilegedAction<ClassLoader>) ClassLoader::getSystemClassLoader);
        }
    }

    /**
     * 获取 {@link ClassLoader}.
     * <p>获取顺序如下：
     * <pre>
     * 1、获取当前线程的 ContextClassLoader
     * 2、获取当前类对应的 ClassLoader
     * 3、获取系统 ClassLoader（{@link ClassLoader#getSystemClassLoader()}）
     * </pre>
     *
     * @return 类加载器
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoaderUtil.class.getClassLoader();
            if (null == classLoader) {
                classLoader = getSystemClassLoader();
            }
        }
        return classLoader;
    }

    // ----------------------------------------------------------------------------------- loadClass

    /**
     * 加载类，使用默认 ClassLoader 并初始化类（调用 static 模块内容和初始化 static 属性）.
     * <p>
     * 扩展 {@link Class#forName(String, boolean, ClassLoader)} 方法，支持以下几类类名的加载：
     * <pre>
     * 1、原始类型，例如：int
     * 2、数组类型，例如：int[]、Long[]、String[]
     * 3、内部类，例如：java.lang.Thread.State 会被转为 java.lang.Thread$State 加载
     * </pre>
     *
     * @param name 类名
     * @return 类名对应的类
     * @throws UtilException 包装 {@link ClassNotFoundException}
     */
    public static Class<?> loadClass(final String name) throws UtilException {
        return loadClass(name, true);
    }

    /**
     * 加载类，使用默认 ClassLoader.
     * <p>
     * 扩展 {@link Class#forName(String, boolean, ClassLoader)} 方法，支持以下几类类名的加载：
     * <pre>
     * 1、原始类型，例如：int
     * 2、数组类型，例如：int[]、Long[]、String[]
     * 3、内部类，例如：java.lang.Thread.State 会被转为 java.lang.Thread$State 加载
     * </pre>
     *
     * @param name          类名
     * @param isInitialized 是否初始化类
     * @return 类名对应的类
     * @throws UtilException 包装 {@link ClassNotFoundException}
     */
    public static Class<?> loadClass(final String name, final boolean isInitialized) throws UtilException {
        return loadClass(name, null, isInitialized);
    }

    /**
     * 加载类.
     * <p>
     * 此方法支持缓存，第一次被加载的类之后会读取缓存中的类。
     * 加载失败的原因可能是此类不存在或其关联引用类不存在。
     * <p>
     * 扩展 {@link Class#forName(String, boolean, ClassLoader)} 方法，支持以下几类类名的加载：
     * <pre>
     * 1、原始类型，例如：int
     * 2、数组类型，例如：int[]、Long[]、String[]
     * 3、内部类，例如：java.lang.Thread.State 会被转为 java.lang.Thread$State 加载
     * </pre>
     *
     * @param name          类名
     * @param classLoader   {@link ClassLoader}，{@code null} 则使用 {@link #getClassLoader()} 获取
     * @param isInitialized 是否初始化类
     * @return 类名对应的类
     * @throws UtilException 包装 {@link ClassNotFoundException}
     */
    public static Class<?> loadClass(final String name, final ClassLoader classLoader, final boolean isInitialized)
            throws UtilException {
        Assert.notNull(name, "Name must not be null");

        // 自动将包名中的 "/" 替换为 "."
        String resolvedName = name.replace('/', '.');
        ClassLoader resolvedLoader = classLoader;
        if (null == resolvedLoader) {
            resolvedLoader = getClassLoader();
        }

        // 加载原始类型和缓存中的类
        Class<?> clazz = loadPrimitiveClass(resolvedName);
        if (clazz == null) {
            clazz = doLoadClass(resolvedName, resolvedLoader, isInitialized);
        }
        return clazz;
    }

    /**
     * 加载原始类型的类.
     * <p>包括原始类型、原始类型数组和 void。
     *
     * @param name 原始类型名，比如 int
     * @return 原始类型类，未找到返回 null
     */
    public static Class<?> loadPrimitiveClass(final String name) {
        Class<?> result = null;
        if (StrUtil.isNotBlank(name)) {
            result = PRIMITIVE_TYPE_NAME_MAP.get(name.trim());
        }
        return result;
    }

    /**
     * 创建新的 {@link JarClassLoader}，并使用此 Classloader 加载目录下的 class 文件和 jar 文件.
     *
     * @param jarOrDir jar文件或者包含jar和class文件的目录
     * @return {@link JarClassLoader}
     * @since 4.4.2
     */
    public static JarClassLoader getJarClassLoader(final File jarOrDir) {
        return JarClassLoader.load(jarOrDir);
    }

    /**
     * 加载外部类.
     *
     * @param jarOrDir jar文件或者包含jar和class文件的目录
     * @param name     类名
     * @return 类
     * @since 4.4.2
     */
    public static Class<?> loadClass(final File jarOrDir, final String name) {
        try {
            return getJarClassLoader(jarOrDir).loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new UtilException(e);
        }
    }

    // ----------------------------------------------------------------------------------- isPresent

    /**
     * 指定类是否被提供，使用默认 ClassLoader.
     * <p>
     * 通过调用 {@link #loadClass(String, ClassLoader, boolean)} 方法尝试加载指定类名的类，
     * 如果加载失败返回 false。
     *
     * @param className 类名
     * @return 是否被提供
     */
    public static boolean isPresent(final String className) {
        return isPresent(className, null);
    }

    /**
     * 指定类是否被提供.
     * <p>
     * 通过调用 {@link #loadClass(String, ClassLoader, boolean)} 方法尝试加载指定类名的类，
     * 如果加载失败返回 false。
     *
     * @param className   类名
     * @param classLoader {@link ClassLoader}
     * @return 是否被提供
     */
    public static boolean isPresent(final String className, final ClassLoader classLoader) {
        try {
            loadClass(className, classLoader, false);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    // --------------------------------------- Private method start

    /**
     * 加载非原始类，无缓存.
     *
     * @param name          类名
     * @param classLoader   {@link ClassLoader}
     * @param isInitialized 是否初始化
     * @return 类
     */
    private static Class<?> doLoadClass(final String name, final ClassLoader classLoader, final boolean isInitialized) {
        final ClassLoader resolvedLoader = null == classLoader ? getClassLoader() : classLoader;
        Class<?> clazz;
        if (name.endsWith(ARRAY_SUFFIX)) {
            // 对象数组 "java.lang.String[]" 风格
            final String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            final Class<?> elementClass = loadClass(elementClassName, resolvedLoader, isInitialized);
            clazz = Array.newInstance(elementClass, 0).getClass();
        } else if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {
            // "[Ljava.lang.String;" 风格
            final String elementName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
            final Class<?> elementClass = loadClass(elementName, resolvedLoader, isInitialized);
            clazz = Array.newInstance(elementClass, 0).getClass();
        } else if (name.startsWith(INTERNAL_ARRAY_PREFIX)) {
            // "[[I" 或 "[[Ljava.lang.String;" 风格
            final String elementName = name.substring(INTERNAL_ARRAY_PREFIX.length());
            final Class<?> elementClass = loadClass(elementName, resolvedLoader, isInitialized);
            clazz = Array.newInstance(elementClass, 0).getClass();
        } else {
            // 加载普通类
            try {
                clazz = Class.forName(name, isInitialized, resolvedLoader);
            } catch (ClassNotFoundException ex) {
                // 尝试获取内部类，例如 java.lang.Thread.State → java.lang.Thread$State
                clazz = tryLoadInnerClass(name, resolvedLoader, isInitialized);
                if (null == clazz) {
                    throw new UtilException(ex);
                }
            }
        }
        return clazz;
    }

    /**
     * 尝试转换并加载内部类，例如 java.lang.Thread.State → java.lang.Thread$State.
     *
     * @param name          类名
     * @param classLoader   {@link ClassLoader}
     * @param isInitialized 是否初始化类
     * @return 类名对应的类，失败返回 null
     * @since 4.1.20
     */
    private static Class<?> tryLoadInnerClass(final String name, final ClassLoader classLoader,
                                              final boolean isInitialized) {
        final int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
        if (lastDotIndex > 0) {
            final String innerClassName = name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR
                    + name.substring(lastDotIndex + 1);
            try {
                return Class.forName(innerClassName, isInitialized, classLoader);
            } catch (ClassNotFoundException ex2) {
                // 尝试获取内部类失败，忽略
            }
        }
        return null;
    }

    // --------------------------------------- Private method end
}
