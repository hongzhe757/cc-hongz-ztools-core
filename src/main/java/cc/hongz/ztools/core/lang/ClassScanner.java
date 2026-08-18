package cc.hongz.ztools.core.lang;

import cc.hongz.ztools.core.collection.EnumerationIter;
import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.util.CharUtil;
import cc.hongz.ztools.core.util.ClassLoaderUtil;
import cc.hongz.ztools.core.util.ClassUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描器.
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.lang.ClassScanner}。
 *
 * @author looly
 * @author hongzhe.qin (migrated &amp; enhanced)
 * @since 4.6.9
 */
public class ClassScanner implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 包名. */
    private final String packageName;
    /** 包名 + "." 后缀. */
    private final String packageNameWithDot;
    /** 包路径（文件分隔符）. */
    private final String packageDirName;
    /** 包路径（/ 分隔）. */
    private final String packagePath;
    /** 过滤器. */
    private final Filter<Class<?>> classFilter;
    /** 编码. */
    private final Charset charset;
    /** 类加载器. */
    private ClassLoader classLoader;
    /** 是否初始化类. */
    private boolean initialize;
    /** 扫描结果集. */
    private final Set<Class<?>> classes = new HashSet<>();
    /** 忽略 loadClass 时的错误. */
    private boolean ignoreLoadError = false;
    /** 加载错误的类名列表. */
    private final Set<String> classesOfLoadError = new HashSet<>();

    // ----------------------------------------------------------------------------------------- Static methods

    /**
     * 扫描指定包路径下所有包含指定注解的类，包括其他加载的 jar 或者类.
     *
     * @param packageName     包路径
     * @param annotationClass 注解类
     * @return 类集合
     */
    public static Set<Class<?>> scanAllPackageByAnnotation(final String packageName, final Class<? extends Annotation> annotationClass) {
        return scanAllPackage(packageName, clazz -> clazz.isAnnotationPresent(annotationClass));
    }

    /**
     * 扫描指定包路径下所有包含指定注解的类.
     *
     * @param packageName     包路径
     * @param annotationClass 注解类
     * @return 类集合
     */
    public static Set<Class<?>> scanPackageByAnnotation(final String packageName, final Class<? extends Annotation> annotationClass) {
        return scanPackage(packageName, clazz -> clazz.isAnnotationPresent(annotationClass));
    }

    /**
     * 扫描指定包路径下所有指定类或接口的子类或实现类.
     *
     * @param packageName 包路径
     * @param superClass  父类或接口
     * @return 类集合
     */
    public static Set<Class<?>> scanAllPackageBySuper(final String packageName, final Class<?> superClass) {
        return scanAllPackage(packageName, clazz -> superClass.isAssignableFrom(clazz) && !superClass.equals(clazz));
    }

    /**
     * 扫描指定包路径下所有指定类或接口的子类或实现类.
     *
     * @param packageName 包路径
     * @param superClass  父类或接口
     * @return 类集合
     */
    public static Set<Class<?>> scanPackageBySuper(final String packageName, final Class<?> superClass) {
        return scanPackage(packageName, clazz -> superClass.isAssignableFrom(clazz) && !superClass.equals(clazz));
    }

    /**
     * 扫描该包路径下所有 class 文件.
     *
     * @return 类集合
     */
    public static Set<Class<?>> scanAllPackage() {
        return scanAllPackage(StrUtil.EMPTY, null);
    }

    /**
     * 扫描 classpath 下所有 class 文件.
     *
     * @return 类集合
     */
    public static Set<Class<?>> scanPackage() {
        return scanPackage(StrUtil.EMPTY, null);
    }

    /**
     * 扫描该包路径下所有 class 文件.
     *
     * @param packageName 包路径
     * @return 类集合
     */
    public static Set<Class<?>> scanPackage(final String packageName) {
        return scanPackage(packageName, null);
    }

    /**
     * 扫描包路径下和所有在 classpath 中加载的类.
     *
     * @param packageName 包路径
     * @param classFilter class 过滤器
     * @return 类集合
     */
    public static Set<Class<?>> scanAllPackage(final String packageName, final Filter<Class<?>> classFilter) {
        return new ClassScanner(packageName, classFilter).scan(true);
    }

    /**
     * 扫描包路径下满足 class 过滤器条件的所有 class 文件.
     *
     * @param packageName 包路径
     * @param classFilter class 过滤器
     * @return 类集合
     */
    public static Set<Class<?>> scanPackage(final String packageName, final Filter<Class<?>> classFilter) {
        return new ClassScanner(packageName, classFilter).scan();
    }

    // ----------------------------------------------------------------------------------------- Constructors

    /**
     * 构造，默认 UTF-8 编码.
     */
    public ClassScanner() {
        this(null);
    }

    /**
     * 构造，默认 UTF-8 编码.
     *
     * @param packageName 包名
     */
    public ClassScanner(final String packageName) {
        this(packageName, null);
    }

    /**
     * 构造，默认 UTF-8 编码.
     *
     * @param packageName 包名
     * @param classFilter 过滤器
     */
    public ClassScanner(final String packageName, final Filter<Class<?>> classFilter) {
        this(packageName, classFilter, StandardCharsets.UTF_8);
    }

    /**
     * 构造.
     *
     * @param packageName 包名
     * @param classFilter 过滤器
     * @param charset     编码
     */
    public ClassScanner(final String packageName, final Filter<Class<?>> classFilter, final Charset charset) {
        final String pn = StrUtil.nullToEmpty(packageName);
        this.packageName = pn;
        this.packageNameWithDot = StrUtil.addSuffixIfNot(pn, StrUtil.DOT);
        this.packageDirName = pn.replace(CharUtil.DOT, File.separatorChar);
        this.packagePath = pn.replace(CharUtil.DOT, CharUtil.SLASH);
        this.classFilter = classFilter;
        this.charset = charset;
    }

    // ----------------------------------------------------------------------------------------- Public methods

    /**
     * 设置是否忽略 loadClass 时的错误.
     *
     * @param ignoreLoadError 忽略 loadClass 时的错误
     * @return this
     */
    public ClassScanner setIgnoreLoadError(final boolean ignoreLoadError) {
        this.ignoreLoadError = ignoreLoadError;
        return this;
    }

    /**
     * 扫描包路径下满足 class 过滤器条件的所有 class 文件.
     *
     * @return 类集合
     */
    public Set<Class<?>> scan() {
        return scan(false);
    }

    /**
     * 扫描包路径下满足 class 过滤器条件的所有 class 文件.
     *
     * @param forceScanJavaClassPaths 是否强制扫描其他位于 classpath 关联 jar 中的类
     * @return 类集合
     */
    public Set<Class<?>> scan(final boolean forceScanJavaClassPaths) {
        this.classes.clear();
        this.classesOfLoadError.clear();

        for (URL url : getResourceUrls(this.packagePath, this.classLoader)) {
            switch (url.getProtocol()) {
                case "file":
                    scanFile(new File(decodeUrl(url.getFile())), null);
                    break;
                case "jar":
                    scanJar(getJarFile(url));
                    break;
                default:
                    break;
            }
        }

        if (forceScanJavaClassPaths || this.classes.isEmpty()) {
            scanJavaClassPaths();
        }

        return Collections.unmodifiableSet(this.classes);
    }

    /**
     * 设置是否在扫描到类时初始化类.
     *
     * @param initialize 是否初始化类
     */
    public void setInitialize(final boolean initialize) {
        this.initialize = initialize;
    }

    /**
     * 设置自定义的类加载器.
     *
     * @param classLoader 类加载器
     */
    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * 忽略加载错误扫描后，可以获得之前扫描时加载错误的类名字集合.
     *
     * @return 加载错误的类名字集合
     */
    public Set<String> getClassesOfLoadError() {
        return Collections.unmodifiableSet(this.classesOfLoadError);
    }

    // ----------------------------------------------------------------------------------------- Private methods

    /**
     * 扫描 Java 指定的 ClassPath 路径.
     */
    private void scanJavaClassPaths() {
        final String[] javaClassPaths = ClassUtil.getJavaClassPaths();
        for (String classPath : javaClassPaths) {
            classPath = decodeUrl(classPath);
            scanFile(new File(classPath), null);
        }
    }

    /**
     * 扫描文件或目录中的类.
     *
     * @param file    文件或目录
     * @param rootDir 包名对应 classpath 绝对路径
     */
    private void scanFile(final File file, final String rootDir) {
        if (file.isFile()) {
            final String fileName = file.getAbsolutePath();
            if (fileName.endsWith(FileUtil.CLASS_EXT)) {
                final String className = fileName
                        .substring(rootDir.length(), fileName.length() - FileUtil.CLASS_EXT.length())
                        .replace(File.separatorChar, CharUtil.DOT);
                addIfAccept(className);
            } else if (fileName.endsWith(FileUtil.JAR_FILE_EXT)) {
                try {
                    scanJar(new JarFile(file));
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }
            }
        } else if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (null != files) {
                for (File subFile : files) {
                    scanFile(subFile, (null == rootDir) ? subPathBeforePackage(file) : rootDir);
                }
            }
        }
    }

    /**
     * 扫描 jar 包.
     *
     * @param jar jar 包
     */
    private void scanJar(final JarFile jar) {
        try {
            String name;
            for (JarEntry entry : new EnumerationIter<>(jar.entries())) {
                name = StrUtil.removePrefix(entry.getName(), StrUtil.SLASH);
                if (StrUtil.isEmpty(packagePath) || name.startsWith(this.packagePath)) {
                    if (name.endsWith(FileUtil.CLASS_EXT) && !entry.isDirectory()) {
                        final String className = name
                                .substring(0, name.length() - FileUtil.CLASS_EXT.length())
                                .replace(CharUtil.SLASH, CharUtil.DOT);
                        addIfAccept(loadClass(className));
                    }
                }
            }
        } finally {
            IoUtil.close(jar);
        }
    }

    /**
     * 加载类.
     *
     * @param className 类名
     * @return 加载的类
     */
    protected Class<?> loadClass(final String className) {
        ClassLoader loader = this.classLoader;
        if (null == loader) {
            loader = ClassLoaderUtil.getClassLoader();
            this.classLoader = loader;
        }

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, this.initialize, loader);
        } catch (NoClassDefFoundError | ClassNotFoundException e) {
            classesOfLoadError.add(className);
        } catch (UnsupportedClassVersionError e) {
            classesOfLoadError.add(className);
        } catch (Throwable e) {
            if (!this.ignoreLoadError) {
                throw ExceptionUtil.wrapRuntime(e);
            } else {
                classesOfLoadError.add(className);
            }
        }
        return clazz;
    }

    /**
     * 通过过滤器，是否满足接受此类的条件.
     *
     * @param className 类名
     */
    private void addIfAccept(final String className) {
        if (StrUtil.isBlank(className)) {
            return;
        }
        int classLen = className.length();
        int packageLen = this.packageName.length();
        if (classLen == packageLen) {
            if (className.equals(this.packageName)) {
                addIfAccept(loadClass(className));
            }
        } else if (classLen > packageLen) {
            if (".".equals(this.packageNameWithDot) || className.startsWith(this.packageNameWithDot)) {
                addIfAccept(loadClass(className));
            }
        }
    }

    /**
     * 通过过滤器，是否满足接受此类的条件.
     *
     * @param clazz 类
     */
    private void addIfAccept(final Class<?> clazz) {
        if (null != clazz) {
            final Filter<Class<?>> filter = this.classFilter;
            if (filter == null || filter.accept(clazz)) {
                this.classes.add(clazz);
            }
        }
    }

    /**
     * 截取文件绝对路径中包名之前的部分.
     *
     * @param file 文件
     * @return 包名之前的部分
     */
    private String subPathBeforePackage(final File file) {
        String filePath = file.getAbsolutePath();
        if (StrUtil.isNotEmpty(this.packageDirName)) {
            filePath = StrUtil.subBefore(filePath, this.packageDirName, true);
        }
        return StrUtil.addSuffixIfNot(filePath, File.separator);
    }

    // ----------------------------------------------------------------------------------------- Utility methods

    /**
     * URL 解码.
     */
    private String decodeUrl(final String url) {
        try {
            return URLDecoder.decode(url, this.charset.name());
        } catch (Exception e) {
            return url;
        }
    }

    /**
     * 从 jar URL 获取 JarFile.
     */
    private static JarFile getJarFile(final URL url) {
        try {
            final JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
            return urlConnection.getJarFile();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获取指定路径下的资源 URL 列表.
     */
    private static List<URL> getResourceUrls(final String resource, final ClassLoader classLoader) {
        final List<URL> result = new ArrayList<>();
        try {
            ClassLoader loader = classLoader;
            if (null == loader) {
                loader = ClassLoaderUtil.getClassLoader();
            }
            final Enumeration<URL> urls = loader.getResources(resource);
            while (urls.hasMoreElements()) {
                result.add(urls.nextElement());
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return result;
    }
}
