package cc.hongz.ztools.core.compiler;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.io.FileUtil;
import cc.hongz.ztools.core.io.IoUtil;
import cc.hongz.ztools.core.io.resource.FileResource;
import cc.hongz.ztools.core.io.resource.Resource;
import cc.hongz.ztools.core.io.resource.StringResource;
import cc.hongz.ztools.core.map.MapUtil;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.ClassLoaderUtil;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.URLUtil;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Java 源码编译器.
 *
 * @author lzpeng
 */
public final class JavaSourceCompiler {

    private final List<Resource> sourceList = new ArrayList<>();
    private final List<File> libraryFileList = new ArrayList<>();
    private final ClassLoader parentClassLoader;

    /**
     * 创建Java源码编译器.
     *
     * @param parent 父类加载器
     * @return Java源码编译器
     */
    public static JavaSourceCompiler create(
            final ClassLoader parent) {
        return new JavaSourceCompiler(parent);
    }

    /**
     * 构造.
     *
     * @param parent 父类加载器，null则使用默认类加载器
     */
    private JavaSourceCompiler(final ClassLoader parent) {
        this.parentClassLoader = ObjectUtil.defaultIfNull(parent,
                ClassLoaderUtil::getClassLoader);
    }

    /**
     * 向编译器中加入待编译的资源.
     *
     * @param resources 待编译的资源
     * @return Java源码编译器
     */
    public JavaSourceCompiler addSource(
            final Resource... resources) {
        if (ArrayUtil.isNotEmpty(resources)) {
            this.sourceList.addAll(
                    Arrays.asList(resources));
        }
        return this;
    }

    /**
     * 向编译器中加入待编译的文件.
     *
     * @param files 待编译的文件
     * @return Java源码编译器
     */
    public JavaSourceCompiler addSource(
            final File... files) {
        if (ArrayUtil.isNotEmpty(files)) {
            for (final File file : files) {
                this.sourceList.add(
                        new FileResource(file));
            }
        }
        return this;
    }

    /**
     * 向编译器中加入待编译的源码Map.
     *
     * @param sourceCodeMap 源码Map key: 类名 value 源码
     * @return Java源码编译器
     */
    public JavaSourceCompiler addSource(
            final Map<String, String> sourceCodeMap) {
        if (MapUtil.isNotEmpty(sourceCodeMap)) {
            sourceCodeMap.forEach(this::addSource);
        }
        return this;
    }

    /**
     * 向编译器中加入待编译的源码.
     *
     * @param className  类名
     * @param sourceCode 源码
     * @return Java文件编译器
     */
    public JavaSourceCompiler addSource(
            final String className, final String sourceCode) {
        if (className != null && sourceCode != null) {
            this.sourceList.add(new StringResource(
                    sourceCode, className));
        }
        return this;
    }

    /**
     * 加入编译Java源码时所需要的jar包.
     *
     * @param files 编译Java源码时所需要的jar包
     * @return Java源码编译器
     */
    public JavaSourceCompiler addLibrary(
            final File... files) {
        if (ArrayUtil.isNotEmpty(files)) {
            this.libraryFileList.addAll(
                    Arrays.asList(files));
        }
        return this;
    }

    /**
     * 编译所有文件并返回类加载器.
     *
     * @return 类加载器
     */
    public ClassLoader compile() {
        return compile(null);
    }

    /**
     * 编译所有文件并返回类加载器.
     *
     * @param options 编译参数
     * @return 类加载器
     */
    public ClassLoader compile(final List<String> options) {
        final List<File> classPath = getClassPath();
        final URL[] urLs = URLUtil.getURLs(
                classPath.toArray(new File[0]));
        final URLClassLoader ucl = URLClassLoader.newInstance(
                urLs, this.parentClassLoader);
        if (sourceList.isEmpty()) {
            return ucl;
        }

        final JavaClassFileManager javaFileManager =
                new JavaClassFileManager(ucl,
                        CompilerUtil.getFileManager());

        List<String> optionList = options;
        if (null == optionList) {
            optionList = new ArrayList<>();
        }
        if (!classPath.isEmpty()) {
            final List<String> cp = CollUtil.map(classPath,
                    File::getAbsolutePath, true);
            final String separator =
                    FileUtil.isWindows() ? ";" : ":";
            optionList.add("-cp");
            optionList.add(CollUtil.join(cp, separator));
        }

        final DiagnosticCollector<? super JavaFileObject>
                diagnosticCollector =
                new DiagnosticCollector<>();
        final List<JavaFileObject> javaFileObjectList =
                getJavaFileObject();
        final CompilationTask task = CompilerUtil.getTask(
                javaFileManager, diagnosticCollector,
                optionList, javaFileObjectList);
        try {
            if (task.call()) {
                return javaFileManager.getClassLoader(
                        StandardLocation.CLASS_OUTPUT);
            }
        } finally {
            IoUtil.close(javaFileManager);
        }
        throw new CompilerException(DiagnosticUtil.getMessages(
                diagnosticCollector));
    }

    /**
     * 获得编译源码时需要的classpath.
     *
     * @return 编译源码时需要的classpath
     */
    private List<File> getClassPath() {
        final List<File> result = new ArrayList<>();
        for (final File file : libraryFileList) {
            final List<File> jars = FileUtil.loopFiles(file,
                    (f) -> JavaFileObjectUtil.isJarOrZipFile(
                            f.getName()));
            result.addAll(jars);
            if (file.isDirectory()) {
                result.add(file);
            }
        }
        return result;
    }

    /**
     * 获得待编译的Java文件对象.
     *
     * @return 待编译的Java文件对象
     */
    private List<JavaFileObject> getJavaFileObject() {
        final List<JavaFileObject> list = new ArrayList<>();
        for (final Resource resource : this.sourceList) {
            if (resource instanceof FileResource) {
                final File file =
                        ((FileResource) resource).getFile();
                FileUtil.walkFiles(file,
                        (f) -> list.addAll(
                                JavaFileObjectUtil
                                    .getJavaFileObjects(f)));
            } else {
                list.add(new JavaSourceFileObject(
                        resource.getName(),
                        resource.getStream()));
            }
        }
        return list;
    }
}
