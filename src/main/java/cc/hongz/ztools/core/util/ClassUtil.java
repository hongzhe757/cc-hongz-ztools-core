package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.ClassScanner;
import cc.hongz.ztools.core.lang.Filter;
import cc.hongz.ztools.core.bean.NullWrapperBean;
import cc.hongz.ztools.core.lang.Singleton;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类工具类.
 * @author xiaoleilu
 * @author hongzhe.qin (enhanced)
 * @since 3.0.9
 */
public final class ClassUtil {

    /**
     * 包装类型 → 原始类型映射，例如 Integer.class → int.class.
     */
    private static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP = new ConcurrentHashMap<>(8);

    /**
     * 原始类型 → 包装类型映射，例如 int.class → Integer.class.
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP = new ConcurrentHashMap<>(8);

    static {
        WRAPPER_PRIMITIVE_MAP.put(Boolean.class, boolean.class);
        WRAPPER_PRIMITIVE_MAP.put(Byte.class, byte.class);
        WRAPPER_PRIMITIVE_MAP.put(Character.class, char.class);
        WRAPPER_PRIMITIVE_MAP.put(Double.class, double.class);
        WRAPPER_PRIMITIVE_MAP.put(Float.class, float.class);
        WRAPPER_PRIMITIVE_MAP.put(Integer.class, int.class);
        WRAPPER_PRIMITIVE_MAP.put(Long.class, long.class);
        WRAPPER_PRIMITIVE_MAP.put(Short.class, short.class);

        for (Map.Entry<Class<?>, Class<?>> entry : WRAPPER_PRIMITIVE_MAP.entrySet()) {
            PRIMITIVE_WRAPPER_MAP.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * 私有构造，禁止实例化.
     */
    private ClassUtil() {
    }

    // --------------------------------------- Static method start

    /**
     * {@code null} 安全的获取对象类型.
     *
     * @param <T> 对象类型
     * @param obj 对象，如果为 {@code null} 返回 {@code null}
     * @return 对象类型，提供对象如果为 {@code null} 返回 {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(final T obj) {
        return ((null == obj) ? null : (Class<T>) obj.getClass());
    }

    /**
     * 获得外围类.
     * <p>返回定义此类或匿名类所在的类，如果类本身是在包中定义的，返回 {@code null}.
     *
     * @param clazz 类
     * @return 外围类
     * @since 4.5.7
     */
    public static Class<?> getEnclosingClass(final Class<?> clazz) {
        return null == clazz ? null : clazz.getEnclosingClass();
    }

    /**
     * 是否为顶层类，即定义在包中的类，而非定义在类中的内部类.
     *
     * @param clazz 类
     * @return 是否为顶层类
     * @since 4.5.7
     */
    public static boolean isTopLevelClass(final Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return null == getEnclosingClass(clazz);
    }

    /**
     * 获取类名.
     *
     * @param obj      获取类名对象
     * @param isSimple 是否简单类名，如果为 true，返回不带包名的类名
     * @return 类名
     * @since 3.0.7
     */
    public static String getClassName(final Object obj, final boolean isSimple) {
        if (null == obj) {
            return null;
        }
        final Class<?> clazz = obj.getClass();
        return getClassName(clazz, isSimple);
    }

    /**
     * 获取类名.
     * <p>类名并不包含 ".class" 这个扩展名.
     * <pre>
     * isSimple 为 false: "cc.hongz.ztools.core.util.ClassUtil"
     * isSimple 为 true: "ClassUtil"
     * </pre>
     *
     * @param clazz    类
     * @param isSimple 是否简单类名，如果为 true，返回不带包名的类名
     * @return 类名
     * @since 3.0.7
     */
    public static String getClassName(final Class<?> clazz, final boolean isSimple) {
        if (null == clazz) {
            return null;
        }
        return isSimple ? clazz.getSimpleName() : clazz.getName();
    }

    /**
     * 获取完整类名的短格式.
     * <p>例如：cc.hongz.ztools.core.util.StrUtil → c.h.z.c.u.StrUtil
     *
     * @param className 类名
     * @return 短格式类名
     * @since 4.1.9
     */
    public static String getShortClassName(final String className) {
        final List<String> packages = StrUtil.split(className, CharUtil.DOT);
        if (null == packages || packages.size() < 2) {
            return className;
        }

        final int size = packages.size();
        final StringBuilder result = new StringBuilder();
        result.append(packages.get(0).charAt(0));
        for (int i = 1; i < size - 1; i++) {
            result.append(CharUtil.DOT).append(packages.get(i).charAt(0));
        }
        result.append(CharUtil.DOT).append(packages.get(size - 1));
        return result.toString();
    }

    /**
     * 获得对象数组的类数组.
     *
     * @param objects 对象数组，如果数组中存在 {@code null} 元素，则此元素被认为是 Object 类型
     * @return 类数组
     */
    public static Class<?>[] getClasses(final Object... objects) {
        Class<?>[] classes = new Class<?>[objects.length];
        Object obj;
        for (int i = 0; i < objects.length; i++) {
            obj = objects[i];
            if (obj instanceof NullWrapperBean) {
                // 自定义 null 值的参数类型
                classes[i] = ((NullWrapperBean<?>) obj).getWrappedClass();
            } else if (null == obj) {
                classes[i] = Object.class;
            } else {
                classes[i] = obj.getClass();
            }
        }
        return classes;
    }

    /**
     * 指定类是否与给定的类名相同.
     *
     * @param clazz      类
     * @param className  类名，可以是全类名（包含包名），也可以是简单类名（不包含包名）
     * @param ignoreCase 是否忽略大小写
     * @return 指定类是否与给定的类名相同
     * @since 3.0.7
     */
    public static boolean equals(final Class<?> clazz, final String className, final boolean ignoreCase) {
        if (null == clazz || StrUtil.isBlank(className)) {
            return false;
        }
        if (ignoreCase) {
            return className.equalsIgnoreCase(clazz.getName()) || className.equalsIgnoreCase(clazz.getSimpleName());
        } else {
            return className.equals(clazz.getName()) || className.equals(clazz.getSimpleName());
        }
    }

    // ----------------------------------------------------------------------------------------- Scan classes

    /**
     * 扫描指定包路径下所有包含指定注解的类.
     *
     * @param packageName     包路径
     * @param annotationClass 注解类
     * @return 类集合
     * @see ClassScanner#scanPackageByAnnotation(String, Class)
     */
    public static Set<Class<?>> scanPackageByAnnotation(final String packageName,
            final Class<? extends Annotation> annotationClass) {
        return ClassScanner.scanPackageByAnnotation(packageName, annotationClass);
    }

    /**
     * 扫描指定包路径下所有指定类或接口的子类或实现类.
     *
     * @param packageName 包路径
     * @param superClass  父类或接口
     * @return 类集合
     * @see ClassScanner#scanPackageBySuper(String, Class)
     */
    public static Set<Class<?>> scanPackageBySuper(final String packageName, final Class<?> superClass) {
        return ClassScanner.scanPackageBySuper(packageName, superClass);
    }

    /**
     * 扫描该包路径下所有 class 文件.
     *
     * @return 类集合
     * @see ClassScanner#scanPackage()
     */
    public static Set<Class<?>> scanPackage() {
        return ClassScanner.scanPackage();
    }

    /**
     * 扫描该包路径下所有 class 文件.
     *
     * @param packageName 包路径
     * @return 类集合
     * @see ClassScanner#scanPackage(String)
     */
    public static Set<Class<?>> scanPackage(final String packageName) {
        return ClassScanner.scanPackage(packageName);
    }

    /**
     * 扫描包路径下满足 class 过滤器条件的所有 class 文件.
     *
     * @param packageName 包路径
     * @param classFilter class 过滤器
     * @return 类集合
     */
    public static Set<Class<?>> scanPackage(final String packageName, final Filter<Class<?>> classFilter) {
        return ClassScanner.scanPackage(packageName, classFilter);
    }

    // ----------------------------------------------------------------------------------------- Method

    /**
     * 获得指定类中的 Public 方法名，去重重载的方法.
     *
     * @param clazz 类
     * @return 方法名 Set
     */
    public static Set<String> getPublicMethodNames(final Class<?> clazz) {
        return ReflectUtil.getPublicMethodNames(clazz);
    }

    /**
     * 获得本类及其父类所有 Public 方法.
     *
     * @param clazz 查找方法的类
     * @return 过滤后的方法列表
     */
    public static Method[] getPublicMethods(final Class<?> clazz) {
        return ReflectUtil.getPublicMethods(clazz);
    }

    /**
     * 获得指定类过滤后的 Public 方法列表.
     *
     * @param clazz  查找方法的类
     * @param filter 过滤器
     * @return 过滤后的方法列表
     */
    public static List<Method> getPublicMethods(final Class<?> clazz, final Filter<Method> filter) {
        return ReflectUtil.getPublicMethods(clazz, filter);
    }

    /**
     * 获得指定类过滤后的 Public 方法列表.
     *
     * @param clazz          查找方法的类
     * @param excludeMethods 不包括的方法
     * @return 过滤后的方法列表
     */
    public static List<Method> getPublicMethods(final Class<?> clazz, final Method... excludeMethods) {
        return ReflectUtil.getPublicMethods(clazz, excludeMethods);
    }

    /**
     * 获得指定类过滤后的 Public 方法列表.
     *
     * @param clazz              查找方法的类
     * @param excludeMethodNames 不包括的方法名列表
     * @return 过滤后的方法列表
     */
    public static List<Method> getPublicMethods(final Class<?> clazz, final String... excludeMethodNames) {
        return ReflectUtil.getPublicMethods(clazz, excludeMethodNames);
    }

    /**
     * 查找指定 Public 方法.
     *
     * @param clazz      类
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 方法
     * @throws SecurityException 无权访问抛出异常
     */
    public static Method getPublicMethod(final Class<?> clazz, final String methodName,
            final Class<?>... paramTypes) throws SecurityException {
        return ReflectUtil.getPublicMethod(clazz, methodName, paramTypes);
    }

    /**
     * 获得指定类中的方法名，去重重载的方法.
     *
     * @param clazz 类
     * @return 方法名 Set
     */
    public static Set<String> getDeclaredMethodNames(final Class<?> clazz) {
        return ReflectUtil.getMethodNames(clazz);
    }

    /**
     * 获得声明的所有方法，包括本类及其父类和接口的所有方法和 Object 类的方法.
     *
     * @param clazz 类
     * @return 方法数组
     */
    public static Method[] getDeclaredMethods(final Class<?> clazz) {
        return ReflectUtil.getMethods(clazz);
    }

    /**
     * 查找指定对象中的所有方法（包括非 public 方法）.
     *
     * @param obj        被查找的对象
     * @param methodName 方法名
     * @param args       参数
     * @return 方法
     * @throws SecurityException 无访问权限抛出异常
     */
    public static Method getDeclaredMethodOfObj(final Object obj, final String methodName,
            final Object... args) throws SecurityException {
        return getDeclaredMethod(obj.getClass(), methodName, getClasses(args));
    }

    /**
     * 查找指定类中的所有方法（包括非 public 方法）.
     *
     * @param clazz          被查找的类
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @return 方法
     * @throws SecurityException 无访问权限抛出异常
     */
    public static Method getDeclaredMethod(final Class<?> clazz, final String methodName,
            final Class<?>... parameterTypes) throws SecurityException {
        return ReflectUtil.getMethod(clazz, methodName, parameterTypes);
    }

    // ----------------------------------------------------------------------------------------- Field

    /**
     * 查找指定类中的所有字段（包括非 public 字段），字段不存在则返回 {@code null}.
     *
     * @param clazz     被查找字段的类
     * @param fieldName 字段名
     * @return 字段
     * @throws SecurityException 安全异常
     */
    public static Field getDeclaredField(final Class<?> clazz, final String fieldName) throws SecurityException {
        if (null == clazz || StrUtil.isBlank(fieldName)) {
            return null;
        }
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // ignore
        }
        return null;
    }

    /**
     * 查找指定类中的所有字段（包括非 public 字段）.
     *
     * @param clazz 被查找字段的类
     * @return 字段
     * @throws SecurityException 安全异常
     */
    public static Field[] getDeclaredFields(final Class<?> clazz) throws SecurityException {
        if (null == clazz) {
            return null;
        }
        return clazz.getDeclaredFields();
    }

    // ----------------------------------------------------------------------------------------- Classpath

    /**
     * 获得 ClassPath，不解码路径中的特殊字符（例如空格和中文）.
     *
     * @return ClassPath 集合
     */
    public static Set<String> getClassPathResources() {
        return getClassPathResources(false);
    }

    /**
     * 获得 ClassPath.
     *
     * @param isDecode 是否解码路径中的特殊字符（例如空格和中文）
     * @return ClassPath 集合
     * @since 4.0.11
     */
    public static Set<String> getClassPathResources(final boolean isDecode) {
        return getClassPaths(StrUtil.EMPTY, isDecode);
    }

    /**
     * 获得 ClassPath，不解码路径中的特殊字符（例如空格和中文）.
     *
     * @param packageName 包名称
     * @return ClassPath 路径字符串集合
     */
    public static Set<String> getClassPaths(final String packageName) {
        return getClassPaths(packageName, false);
    }

    /**
     * 获得 ClassPath.
     *
     * @param packageName 包名称
     * @param isDecode    是否解码路径中的特殊字符（例如空格和中文）
     * @return ClassPath 路径字符串集合
     * @since 4.0.11
     */
    public static Set<String> getClassPaths(final String packageName, final boolean isDecode) {
        String packagePath = packageName.replace(StrUtil.DOT, StrUtil.SLASH);
        Enumeration<URL> resources;
        try {
            resources = getClassLoader().getResources(packagePath);
        } catch (IOException e) {
            throw new UtilException(e, "Loading classPath [{}] error!", packagePath);
        }
        final Set<String> paths = new HashSet<>();
        String path;
        while (resources.hasMoreElements()) {
            path = resources.nextElement().getPath();
            if (isDecode) {
                try {
                    path = URLDecoder.decode(path, Charset.defaultCharset().name());
                } catch (Exception ignored) {
                    // 解码失败则使用原始路径
                }
            }
            paths.add(path);
        }
        return paths;
    }

    /**
     * 获得 ClassPath，将编码后的中文路径解码为原字符.
     *
     * @return ClassPath
     */
    public static String getClassPath() {
        return getClassPath(false);
    }

    /**
     * 获得 ClassPath.
     *
     * @param isEncoded 是否编码路径中的中文
     * @return ClassPath
     * @since 3.2.1
     */
    public static String getClassPath(final boolean isEncoded) {
        final URL classPathURL = getClassPathURL();
        String path;
        if (isEncoded) {
            path = classPathURL.getPath();
        } else {
            try {
                path = URLDecoder.decode(classPathURL.getPath(), Charset.defaultCharset().name());
            } catch (Exception e) {
                path = classPathURL.getPath();
            }
        }
        return path;
    }

    /**
     * 获得 ClassPath URL.
     *
     * @return ClassPath URL
     */
    public static URL getClassPathURL() {
        return getResourceURL(StrUtil.EMPTY);
    }

    /**
     * 获得资源的 URL.
     * <p>路径用 / 分隔.
     *
     * @param resource 资源（相对 Classpath 的路径）
     * @return 资源 URL
     */
    public static URL getResourceURL(final String resource) {
        return getClassLoader().getResource(resource);
    }

    /**
     * 获取指定路径下的资源列表.
     * <p>路径格式必须为目录格式，用 / 分隔.
     *
     * @param resource 资源路径
     * @return 资源列表
     */
    public static List<URL> getResources(final String resource) {
        final List<URL> result = new ArrayList<>();
        try {
            final Enumeration<URL> urls = getClassLoader().getResources(resource);
            while (urls.hasMoreElements()) {
                result.add(urls.nextElement());
            }
        } catch (IOException e) {
            throw new UtilException(e);
        }
        return result;
    }

    /**
     * 获得资源相对路径对应的 URL.
     *
     * @param resource  资源相对路径
     * @param baseClass 基准 Class，获得的相对路径相对于此 Class 所在路径，
     *                  如果为 {@code null} 则相对 ClassPath
     * @return {@link URL}
     */
    public static URL getResourceUrl(final String resource, final Class<?> baseClass) {
        if (baseClass == null) {
            return getResourceURL(resource);
        }
        return baseClass.getResource(resource);
    }

    /**
     * 获得 Java ClassPath 路径，不包括 jre.
     *
     * @return Java ClassPath 路径数组
     */
    public static String[] getJavaClassPaths() {
        return System.getProperty("java.class.path").split(System.getProperty("path.separator"));
    }

    /**
     * 获取当前线程的 {@link ClassLoader}.
     *
     * @return 当前线程的 class loader
     * @see ClassLoaderUtil#getClassLoader()
     */
    public static ClassLoader getContextClassLoader() {
        return ClassLoaderUtil.getContextClassLoader();
    }

    /**
     * 获取 {@link ClassLoader}.
     *
     * @return 类加载器
     * @see ClassLoaderUtil#getClassLoader()
     */
    public static ClassLoader getClassLoader() {
        return ClassLoaderUtil.getClassLoader();
    }

    /**
     * 比较判断 types1 和 types2 两组类，如果 types1 中所有的类都与 types2 对应位置的类相同，
     * 或者是其父类或接口，则返回 {@code true}.
     *
     * @param types1 类组 1
     * @param types2 类组 2
     * @return 是否相同、父类或接口
     */
    public static boolean isAllAssignableFrom(final Class<?>[] types1, final Class<?>[] types2) {
        if (ArrayUtil.isEmpty(types1) && ArrayUtil.isEmpty(types2)) {
            return true;
        }
        if (null == types1 || null == types2) {
            return false;
        }
        if (types1.length != types2.length) {
            return false;
        }

        Class<?> type1;
        Class<?> type2;
        for (int i = 0; i < types1.length; i++) {
            type1 = types1[i];
            type2 = types2[i];
            if (isBasicType(type1) && isBasicType(type2)) {
                // 原始类型和包装类型存在不一致情况
                if (unWrap(type1) != unWrap(type2)) {
                    return false;
                }
            } else if (!type1.isAssignableFrom(type2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 加载类.
     *
     * @param <T>           对象类型
     * @param className     类名
     * @param isInitialized 是否初始化
     * @return 类
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(final String className, final boolean isInitialized) {
        return (Class<T>) ClassLoaderUtil.loadClass(className, isInitialized);
    }

    /**
     * 加载类并初始化.
     *
     * @param <T>       对象类型
     * @param className 类名
     * @return 类
     */
    public static <T> Class<T> loadClass(final String className) {
        return loadClass(className, true);
    }

    // ---------------------------------------------------------------------------------------------------- Invoke start

    /**
     * 执行方法.
     * <p>可执行 Private 方法，也可执行 static 方法。
     * 执行非 static 方法时，必须满足对象有默认构造方法。
     * 非单例模式，如果是非静态方法，每次创建一个新对象。
     *
     * @param <T>                     对象类型
     * @param classNameWithMethodName 类名和方法名表达式，类名与方法名用 {@code .} 或 {@code #} 连接
     * @param args                    参数
     * @return 返回结果
     */
    public static <T> T invoke(final String classNameWithMethodName, final Object[] args) {
        return invoke(classNameWithMethodName, false, args);
    }

    /**
     * 执行方法.
     *
     * @param <T>                     对象类型
     * @param classNameWithMethodName 类名和方法名表达式
     * @param isSingleton             是否为单例对象
     * @param args                    参数
     * @return 返回结果
     */
    public static <T> T invoke(final String classNameWithMethodName, final boolean isSingleton, final Object... args) {
        if (StrUtil.isBlank(classNameWithMethodName)) {
            throw new UtilException("Blank classNameDotMethodName!");
        }

        int splitIndex = classNameWithMethodName.lastIndexOf('#');
        if (splitIndex <= 0) {
            splitIndex = classNameWithMethodName.lastIndexOf('.');
        }
        if (splitIndex <= 0) {
            throw new UtilException("Invalid classNameWithMethodName [{}]!", classNameWithMethodName);
        }

        final String className = classNameWithMethodName.substring(0, splitIndex);
        final String methodName = classNameWithMethodName.substring(splitIndex + 1);

        return invoke(className, methodName, isSingleton, args);
    }

    /**
     * 执行方法.
     *
     * @param <T>        对象类型
     * @param className  类名
     * @param methodName 方法名
     * @param args       参数
     * @return 返回结果
     */
    public static <T> T invoke(final String className, final String methodName, final Object[] args) {
        return invoke(className, methodName, false, args);
    }

    /**
     * 执行方法.
     *
     * @param <T>         对象类型
     * @param className   类名
     * @param methodName  方法名
     * @param isSingleton 是否为单例对象
     * @param args        参数
     * @return 返回结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(final String className, final String methodName,
            final boolean isSingleton, final Object... args) {
        final Class<Object> clazz = loadClass(className);
        try {
            final Method method = getDeclaredMethod(clazz, methodName, getClasses(args));
            if (null == method) {
                throw new NoSuchMethodException(StrUtil.format("No such method: [{}]", methodName));
            }
            if (isStatic(method)) {
                return ReflectUtil.invoke(null, method, args);
            } else {
                return ReflectUtil.invoke(
                        isSingleton ? Singleton.get(clazz) : ReflectUtil.newInstance(clazz), method, args);
            }
        } catch (Exception e) {
            throw new UtilException(e);
        }
    }

    // ---------------------------------------------------------------------------------------------------- Invoke end

    // ----------------------------------------------------------------------------------------- Type check

    /**
     * 是否为包装类型.
     *
     * @param clazz 类
     * @return 是否为包装类型
     */
    public static boolean isPrimitiveWrapper(final Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return WRAPPER_PRIMITIVE_MAP.containsKey(clazz);
    }

    /**
     * 是否为基本类型（包括包装类和原始类）.
     *
     * @param clazz 类
     * @return 是否为基本类型
     */
    public static boolean isBasicType(final Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    /**
     * 是否简单值类型或简单值类型的数组.
     * <p>包括：原始类型、String、other CharSequence、a Number、a Date、a URI、a URL、a Locale
     * 或 a Class 及其数组.
     *
     * @param clazz 属性类
     * @return 是否简单值类型或简单值类型的数组
     */
    public static boolean isSimpleTypeOrArray(final Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
    }

    /**
     * 是否为简单值类型.
     * <p>包括：原始类型、String、other CharSequence、Number、Date、URI、URL、Locale、Class.
     *
     * @param clazz 类
     * @return 是否为简单值类型
     */
    public static boolean isSimpleValueType(final Class<?> clazz) {
        return isBasicType(clazz)
                || clazz.isEnum()
                || CharSequence.class.isAssignableFrom(clazz)
                || Number.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)
                || clazz.equals(URI.class)
                || clazz.equals(URL.class)
                || clazz.equals(Locale.class)
                || clazz.equals(Class.class)
                // jdk8 date object
                || TemporalAccessor.class.isAssignableFrom(clazz);
    }

    /**
     * 检查目标类是否可以从原类转化.
     * <p>转化包括：
     * <pre>
     * 1、原类是对象，目标类型是原类型实现的接口
     * 2、目标类型是原类型的父类
     * 3、两者是原始类型或者包装类型（相互转换）
     * </pre>
     *
     * @param targetType 目标类型
     * @param sourceType 原类型
     * @return 是否可转化
     */
    public static boolean isAssignable(final Class<?> targetType, final Class<?> sourceType) {
        if (null == targetType || null == sourceType) {
            return false;
        }

        // 对象类型
        if (targetType.isAssignableFrom(sourceType)) {
            return true;
        }

        // 基本类型
        if (targetType.isPrimitive()) {
            // 原始类型
            Class<?> resolvedPrimitive = WRAPPER_PRIMITIVE_MAP.get(sourceType);
            return targetType.equals(resolvedPrimitive);
        } else {
            // 包装类型
            Class<?> resolvedWrapper = PRIMITIVE_WRAPPER_MAP.get(sourceType);
            return resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper);
        }
    }

    // ----------------------------------------------------------------------------------------- Modifier check

    /**
     * 指定类是否为 Public.
     *
     * @param clazz 类
     * @return 是否为 public
     */
    public static boolean isPublic(final Class<?> clazz) {
        if (null == clazz) {
            throw new NullPointerException("Class to provided is null.");
        }
        return Modifier.isPublic(clazz.getModifiers());
    }

    /**
     * 指定方法是否为 Public.
     *
     * @param method 方法
     * @return 是否为 public
     */
    public static boolean isPublic(final Method method) {
        Assert.notNull(method, "Method to provided is null.");
        return Modifier.isPublic(method.getModifiers());
    }

    /**
     * 指定类是否为非 public.
     *
     * @param clazz 类
     * @return 是否为非 public
     */
    public static boolean isNotPublic(final Class<?> clazz) {
        return !isPublic(clazz);
    }

    /**
     * 指定方法是否为非 public.
     *
     * @param method 方法
     * @return 是否为非 public
     */
    public static boolean isNotPublic(final Method method) {
        return !isPublic(method);
    }

    /**
     * 是否为静态方法.
     *
     * @param method 方法
     * @return 是否为静态方法
     */
    public static boolean isStatic(final Method method) {
        Assert.notNull(method, "Method to provided is null.");
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 设置方法为可访问.
     *
     * @param method 方法
     * @return 方法
     */
    public static Method setAccessible(final Method method) {
        if (null != method && !method.isAccessible()) {
            method.setAccessible(true);
        }
        return method;
    }

    /**
     * 是否为抽象类.
     *
     * @param clazz 类
     * @return 是否为抽象类
     */
    public static boolean isAbstract(final Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    /**
     * 是否为标准的类.
     * <p>这个类必须：
     * <pre>
     * 1、非接口
     * 2、非抽象类
     * 3、非 Enum 枚举
     * 4、非数组
     * 5、非注解
     * 6、非原始类型（int, long 等）
     * 7、非 String
     * </pre>
     *
     * @param clazz 类
     * @return 是否为标准类
     */
    public static boolean isNormalClass(final Class<?> clazz) {
        return null != clazz
                && !clazz.isInterface()
                && !isAbstract(clazz)
                && !clazz.isEnum()
                && !clazz.isArray()
                && !clazz.isAnnotation()
                && !clazz.isSynthetic()
                && !clazz.isPrimitive()
                // issue#3965 String 有 isEmpty 方法，但是不能被当作 bean
                && clazz != String.class;
    }

    /**
     * 判断类是否为枚举类型.
     *
     * @param clazz 类
     * @return 是否为枚举类型
     * @since 3.2.0
     */
    public static boolean isEnum(final Class<?> clazz) {
        return null != clazz && clazz.isEnum();
    }

    /**
     * 获得给定类的第一个泛型参数.
     *
     * @param clazz 被检查的类
     * @return {@link Class}
     */
    public static Class<?> getTypeArgument(final Class<?> clazz) {
        return getTypeArgument(clazz, 0);
    }

    /**
     * 获得给定类的泛型参数.
     *
     * @param clazz 被检查的类
     * @param index 泛型类型的索引号
     * @return {@link Class}
     */
    public static Class<?> getTypeArgument(final Class<?> clazz, final int index) {
        final Type argumentType = TypeUtil.getTypeArgument(clazz, index);
        return TypeUtil.getClass(argumentType);
    }

    // ----------------------------------------------------------------------------------------- Package

    /**
     * 获得给定类所在包的名称.
     * <p>例如：cc.hongz.ztools.core.util.ClassUtil → cc.hongz.ztools.core.util
     *
     * @param clazz 类
     * @return 包名
     */
    public static String getPackage(final Class<?> clazz) {
        if (clazz == null) {
            return StrUtil.EMPTY;
        }
        final String className = clazz.getName();
        int packageEndIndex = className.lastIndexOf(StrUtil.DOT);
        if (packageEndIndex == -1) {
            return StrUtil.EMPTY;
        }
        return className.substring(0, packageEndIndex);
    }

    /**
     * 获得给定类所在包的路径.
     * <p>例如：cc.hongz.ztools.core.util.ClassUtil → cc/hongz/ztools/core/util
     *
     * @param clazz 类
     * @return 包路径
     */
    public static String getPackagePath(final Class<?> clazz) {
        return getPackage(clazz).replace(StrUtil.C_DOT, StrUtil.C_SLASH);
    }

    // ----------------------------------------------------------------------------------------- Default value

    /**
     * 获取指定类型的默认值.
     * <pre>
     * 1、如果为原始类型，返回 0
     * 2、非原始类型返回 {@code null}
     * </pre>
     *
     * @param clazz 类
     * @return 默认值
     * @since 3.0.8
     */
    public static Object getDefaultValue(final Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return getPrimitiveDefaultValue(clazz);
        }
        return null;
    }

    /**
     * 获取指定原始类型的默认值.
     * <pre>
     * 1、如果为原始类型，返回 0
     * 2、非原始类型返回 {@code null}
     * </pre>
     *
     * @param clazz 类
     * @return 默认值
     * @since 5.8.0
     */
    public static Object getPrimitiveDefaultValue(final Class<?> clazz) {
        if (long.class == clazz) {
            return 0L;
        } else if (int.class == clazz) {
            return 0;
        } else if (short.class == clazz) {
            return (short) 0;
        } else if (char.class == clazz) {
            return (char) 0;
        } else if (byte.class == clazz) {
            return (byte) 0;
        } else if (double.class == clazz) {
            return 0D;
        } else if (float.class == clazz) {
            return 0f;
        } else if (boolean.class == clazz) {
            return false;
        }
        return null;
    }

    /**
     * 获得默认值列表.
     *
     * @param classes 值类型
     * @return 默认值列表
     * @since 3.0.9
     */
    public static Object[] getDefaultValues(final Class<?>... classes) {
        final Object[] values = new Object[classes.length];
        for (int i = 0; i < classes.length; i++) {
            values[i] = getDefaultValue(classes[i]);
        }
        return values;
    }

    // ----------------------------------------------------------------------------------------- Location

    /**
     * 是否为 JDK 中定义的类或接口.
     * <pre>
     * 1、以 java.、javax. 开头的包名
     * 2、ClassLoader 为 null
     * </pre>
     *
     * @param clazz 被检查的类
     * @return 是否为 JDK 中定义的类或接口
     * @since 4.6.5
     */
    public static boolean isJdkClass(final Class<?> clazz) {
        final Package objectPackage = clazz.getPackage();
        if (null == objectPackage) {
            return false;
        }
        final String objectPackageName = objectPackage.getName();
        return objectPackageName.startsWith("java.")
                || objectPackageName.startsWith("javax.")
                || clazz.getClassLoader() == null;
    }

    /**
     * 获取 class 类路径 URL，不管是否在 jar 包中都会返回文件夹的路径.
     * <p>class 在 jar 包中返回 jar 的路径，class 不在 jar 中返回文件夹目录.
     * <p>jdk 中的类不能使用此方法.
     *
     * @param clazz 类
     * @return URL
     * @since 5.2.4
     */
    public static URL getLocation(final Class<?> clazz) {
        if (null == clazz) {
            return null;
        }
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }

    /**
     * 获取 class 类路径，不管是否在 jar 包中都会返回文件夹的路径.
     * <p>class 在 jar 包中返回 jar 的路径，class 不在 jar 中返回文件夹目录.
     * <p>jdk 中的类不能使用此方法.
     *
     * @param clazz 类
     * @return class 路径
     * @since 5.2.4
     */
    public static String getLocationPath(final Class<?> clazz) {
        final URL location = getLocation(clazz);
        if (null == location) {
            return null;
        }
        return location.getPath();
    }

    /**
     * 是否为抽象类或接口.
     *
     * @param clazz 类
     * @return 是否为抽象类或接口
     * @since 5.8.2
     */
    public static boolean isAbstractOrInterface(final Class<?> clazz) {
        return isAbstract(clazz) || isInterface(clazz);
    }

    /**
     * 是否为接口.
     *
     * @param clazz 类
     * @return 是否为接口
     * @since 5.8.2
     */
    public static boolean isInterface(final Class<?> clazz) {
        return clazz != null && clazz.isInterface();
    }

    // --------------------------------------- Private method start

    /**
     * 包装类转为原始类，非包装类返回原类.
     *
     * @param clazz 包装类
     * @return 原始类
     */
    private static Class<?> unWrap(final Class<?> clazz) {
        if (null == clazz || clazz.isPrimitive()) {
            return clazz;
        }
        Class<?> result = WRAPPER_PRIMITIVE_MAP.get(clazz);
        return (null == result) ? clazz : result;
    }

    // --------------------------------------- Private method end
}
