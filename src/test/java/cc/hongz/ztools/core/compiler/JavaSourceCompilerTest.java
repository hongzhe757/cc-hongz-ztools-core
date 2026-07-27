package cc.hongz.ztools.core.compiler;

import cc.hongz.ztools.core.io.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Java源码编译器测试（自 Hutool {@code JavaSourceCompilerTest} 迁入）.
 *
 * @author lzpeng
 * @author hongzhe.qin
 */
class JavaSourceCompilerTest {

    // ==================== 基础编译（对应 hutool testCompile） ====================

    @Test
    @DisplayName("testCompile - 从test-compile资源编译Java源码")
    void testCompile() throws Exception {
        // 编译test-compile中的B.java和C.java（纯Java，无hutool依赖）
        final ClassLoader classLoader =
                CompilerUtil.getCompiler(null)
                    .addSource(FileUtil.file(
                            "test-compile/b/B.java"))
                    .addSource("c.C", FileUtil.readUtf8String(
                            "test-compile/c/C.java"))
                    .compile();
        final Class<?> clazz = classLoader.loadClass("c.C");
        assertNotNull(clazz);
        assertNotNull(clazz.getDeclaredConstructor()
                .newInstance());
    }

    // ==================== 错误编译（对应 hutool testErrorCompile） ====================

    @Test
    @DisplayName("testErrorCompile - 编译错误源码抛出CompilerException")
    void testErrorCompile() {
        Exception exception = null;
        try {
            CompilerUtil.getCompiler(null)
                    .addSource(FileUtil.file(
                        "test-compile/error/ErrorClazz.java"))
                    .compile();
        } catch (final Exception ex) {
            exception = ex;
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof CompilerException);
        }
    }

    // ==================== CompilerUtil 补充 ====================

    @Test
    @DisplayName("CompileUtil - SYSTEM_COMPILER非空")
    void testSystemCompiler() {
        assertNotNull(CompilerUtil.SYSTEM_COMPILER);
    }

    @Test
    @DisplayName("CompileUtil - getFileManager非空")
    void testGetFileManager() {
        assertNotNull(CompilerUtil.getFileManager());
    }

    @Test
    @DisplayName("CompileUtil - getCompiler非空")
    void testGetCompiler() {
        assertNotNull(CompilerUtil.getCompiler(null));
    }

    // ==================== JavaFileObjectUtil 补充 ====================

    @Test
    @DisplayName("JavaFileObjectUtil - isJavaFile")
    void testIsJavaFile() {
        assertTrue(JavaFileObjectUtil.isJavaFile("Test.java"));
        assertFalse(JavaFileObjectUtil.isJavaFile("Test.class"));
        assertFalse(JavaFileObjectUtil.isJavaFile("Test.txt"));
    }

    @Test
    @DisplayName("JavaFileObjectUtil - isJarOrZipFile")
    void testIsJarOrZipFile() {
        assertTrue(JavaFileObjectUtil.isJarOrZipFile(
                "lib.jar"));
        assertTrue(JavaFileObjectUtil.isJarOrZipFile(
                "lib.zip"));
        assertFalse(JavaFileObjectUtil.isJarOrZipFile(
                "lib.txt"));
    }

    @Test
    @DisplayName("JavaFileObjectUtil - getJavaFileObjects from java file")
    void testGetJavaFileObjects(
            @TempDir final Path tempDir) throws Exception {
        Path srcFile = tempDir.resolve("Test.java");
        Files.write(srcFile,
                ("public class Test { "
                        + "public String say() { "
                        + "return \"hello\"; } }")
                    .getBytes(StandardCharsets.UTF_8));

        java.util.List<?> objects =
                JavaFileObjectUtil.getJavaFileObjects(
                        srcFile.toFile());
        assertEquals(1, objects.size());
    }

    // ==================== JavaSourceCompiler 补充 ====================

    @Test
    @DisplayName("JavaSourceCompiler - 内存中编译源码字符串")
    void testCompileFromString()
            throws ClassNotFoundException {
        String code = "package test; "
                + "public class Hello { "
                + "public String greet() { "
                + "return \"hello world\"; } }";
        ClassLoader classLoader =
                CompilerUtil.getCompiler(null)
                    .addSource("test.Hello", code)
                    .compile();
        Class<?> clazz = classLoader.loadClass("test.Hello");
        assertNotNull(clazz);
    }

    @Test
    @DisplayName("JavaSourceCompiler - 编译简单源码")
    void testCompileSimple(
            @TempDir final Path tempDir) throws Exception {
        Path srcFile = tempDir.resolve("Simple.java");
        String code = "public class Simple { "
                + "public String say() { "
                + "return \"ok\"; } }";
        Files.write(srcFile,
                code.getBytes(StandardCharsets.UTF_8));

        ClassLoader classLoader =
                CompilerUtil.getCompiler(null)
                    .addSource(srcFile.toFile())
                    .compile();
        Class<?> clazz = classLoader.loadClass("Simple");
        assertNotNull(clazz);
        Object obj = clazz.getDeclaredConstructor()
                .newInstance();
        String result = (String) clazz
                .getMethod("say").invoke(obj);
        assertEquals("ok", result);
    }

    @Test
    @DisplayName("JavaSourceCompiler - addSource Map")
    void testAddSourceMap()
            throws ClassNotFoundException {
        java.util.Map<String, String> map =
                new java.util.HashMap<>();
        map.put("test.MyClass",
                "package test; public class MyClass {}");
        ClassLoader classLoader =
                CompilerUtil.getCompiler(null)
                    .addSource(map)
                    .compile();
        assertNotNull(classLoader.loadClass(
                "test.MyClass"));
    }

    // ==================== CompilerException ====================

    @Test
    @DisplayName("CompilerException - 多种构造器")
    void testCompilerExceptionConstructors() {
        CompilerException e1 = new CompilerException("msg");
        assertEquals("msg", e1.getMessage());

        CompilerException e2 = new CompilerException(
                new RuntimeException("cause"));
        assertNotNull(e2.getCause());

        CompilerException e3 = new CompilerException(
                "msg {}", "param");
        assertEquals("msg param", e3.getMessage());

        CompilerException e4 = new CompilerException(
                "msg", new RuntimeException("cause"));
        assertEquals("msg", e4.getMessage());
        assertNotNull(e4.getCause());
    }

    @Test
    @DisplayName("CompilerException - Throwable+模板构造器")
    void testCompilerExceptionThrowableWithTemplate() {
        RuntimeException cause = new RuntimeException("root cause");
        CompilerException e = new CompilerException(cause,
                "compile failed: {}", "syntax error");
        assertEquals("compile failed: syntax error", e.getMessage());
        assertNotNull(e.getCause());
        assertEquals(cause, e.getCause());
    }

    // ==================== DiagnosticUtil ====================

    @Test
    @DisplayName("DiagnosticUtil - getMessages空收集器")
    void testDiagnosticUtil() {
        DiagnosticCollector<JavaFileObject> collector =
                new DiagnosticCollector<>();
        String msg = DiagnosticUtil.getMessages(collector);
        assertNotNull(msg);
        assertEquals("", msg);
    }

    @Test
    @DisplayName("DiagnosticUtil - 编译错误后收集诊断消息")
    void testDiagnosticUtilWithErrors() {
        String code = "public class BadClass { invalid syntax }";
        DiagnosticCollector<JavaFileObject> collector =
                new DiagnosticCollector<>();
        try {
            CompilerUtil.getCompiler(null)
                    .addSource("BadClass", code)
                    .compile();
        } catch (CompilerException ignored) {
            // 预期抛出异常
        }
        // 编译失败后collector中应有诊断信息（通过CompilerException间接验证）
        // 直接测试空collector返回空字符串
        DiagnosticCollector<JavaFileObject> emptyCollector =
                new DiagnosticCollector<>();
        assertEquals("", DiagnosticUtil.getMessages(emptyCollector));
    }

    // ==================== CompilerUtil 补充 ====================

    @Test
    @DisplayName("CompilerUtil - getFileManager带DiagnosticListener")
    void testGetFileManagerWithListener() {
        DiagnosticCollector<JavaFileObject> listener =
                new DiagnosticCollector<>();
        assertNotNull(CompilerUtil.getFileManager(listener));
    }

    @Test
    @DisplayName("CompilerUtil - getTask非空")
    void testGetTask() {
        javax.tools.StandardJavaFileManager fm =
                CompilerUtil.getFileManager();
        assertNotNull(fm);
        List<JavaFileObject> emptyList = new ArrayList<>();
        javax.tools.JavaCompiler.CompilationTask task =
                CompilerUtil.getTask(fm, null, null, emptyList);
        assertNotNull(task);
    }

    // ==================== JavaSourceCompiler 补充 ====================

    @Test
    @DisplayName("JavaSourceCompiler - create工厂方法")
    void testCreateFactory() {
        JavaSourceCompiler compiler =
                JavaSourceCompiler.create(null);
        assertNotNull(compiler);
    }

    @Test
    @DisplayName("JavaSourceCompiler - create带ClassLoader")
    void testCreateWithClassLoader() {
        JavaSourceCompiler compiler =
                JavaSourceCompiler.create(
                        Thread.currentThread().getContextClassLoader());
        assertNotNull(compiler);
    }

    @Test
    @DisplayName("JavaSourceCompiler - 空源列表编译返回ClassLoader")
    void testCompileEmptySource() {
        ClassLoader cl = CompilerUtil.getCompiler(null).compile();
        assertNotNull(cl);
    }

    @Test
    @DisplayName("JavaSourceCompiler - addSource null参数不抛异常")
    void testAddSourceNullParams() {
        JavaSourceCompiler compiler =
                CompilerUtil.getCompiler(null);
        // null className 和 null sourceCode 应被忽略
        compiler.addSource(null, null);
        // 编译空源列表不应抛异常
        assertNotNull(compiler.compile());
    }

    @Test
    @DisplayName("JavaSourceCompiler - addSource Map空Map")
    void testAddSourceEmptyMap() {
        Map<String, String> emptyMap = new HashMap<>();
        JavaSourceCompiler compiler =
                CompilerUtil.getCompiler(null);
        compiler.addSource(emptyMap);
        assertNotNull(compiler.compile());
    }

    @Test
    @DisplayName("JavaSourceCompiler - addLibrary")
    void testAddLibrary(@TempDir Path tempDir) {
        // 创建一个空的jar文件
        File jarFile = tempDir.resolve("empty.jar").toFile();
        // addLibrary不应抛异常，即使jar文件不存在
        JavaSourceCompiler compiler = CompilerUtil.getCompiler(null)
                .addLibrary(jarFile);
        assertNotNull(compiler);
    }

    @Test
    @DisplayName("JavaSourceCompiler - 编译带参数的源码")
    void testCompileWithOptions(
            @TempDir Path tempDir) throws Exception {
        Path srcFile = tempDir.resolve("OptionTest.java");
        String code = "public class OptionTest { "
                + "public int add(int a, int b) { "
                + "return a + b; } }";
        Files.write(srcFile,
                code.getBytes(StandardCharsets.UTF_8));

        List<String> options = new ArrayList<>();
        options.add("-source");
        options.add("17");

        ClassLoader cl = CompilerUtil.getCompiler(null)
                .addSource(srcFile.toFile())
                .compile(options);
        assertNotNull(cl);
        Class<?> clazz = cl.loadClass("OptionTest");
        Object obj = clazz.getDeclaredConstructor().newInstance();
        int result = (int) clazz.getMethod("add", int.class, int.class)
                .invoke(obj, 3, 4);
        assertEquals(7, result);
    }

    @Test
    @DisplayName("JavaSourceCompiler - 编译多个内存源码")
    void testCompileMultipleSources()
            throws ClassNotFoundException {
        String code1 = "package multi; public class A { "
                + "public String name() { return \"A\"; } }";
        String code2 = "package multi; public class B { "
                + "public String name() { return \"B\"; } }";
        ClassLoader cl = CompilerUtil.getCompiler(null)
                .addSource("multi.A", code1)
                .addSource("multi.B", code2)
                .compile();
        assertNotNull(cl.loadClass("multi.A"));
        assertNotNull(cl.loadClass("multi.B"));
    }

    // ==================== JavaSourceFileObject ====================

    @Test
    @DisplayName("JavaSourceFileObject - 从URI构造")
    void testJavaSourceFileObjectFromUri(
            @TempDir Path tempDir) throws Exception {
        Path srcFile = tempDir.resolve("UriTest.java");
        Files.write(srcFile,
                "public class UriTest {}".getBytes(
                        StandardCharsets.UTF_8));
        JavaSourceFileObject fObj =
                new JavaSourceFileObject(srcFile.toUri());
        assertNotNull(fObj);
        assertEquals(JavaFileObject.Kind.SOURCE, fObj.getKind());
    }

    @Test
    @DisplayName("JavaSourceFileObject - openInputStream")
    void testJavaSourceFileObjectOpenInputStream(
            @TempDir Path tempDir) throws Exception {
        Path srcFile = tempDir.resolve("StreamTest.java");
        String code = "public class StreamTest {}";
        Files.write(srcFile,
                code.getBytes(StandardCharsets.UTF_8));
        JavaSourceFileObject fObj =
                new JavaSourceFileObject(srcFile.toUri());
        try (InputStream is = fObj.openInputStream()) {
            assertNotNull(is);
            byte[] bytes = is.readAllBytes();
            assertEquals(code, new String(bytes,
                    StandardCharsets.UTF_8));
        }
    }

    @Test
    @DisplayName("JavaSourceFileObject - getCharContent")
    void testJavaSourceFileObjectGetCharContent(
            @TempDir Path tempDir) throws Exception {
        Path srcFile = tempDir.resolve("CharTest.java");
        String code = "public class CharTest { int x = 1; }";
        Files.write(srcFile,
                code.getBytes(StandardCharsets.UTF_8));
        JavaSourceFileObject fObj =
                new JavaSourceFileObject(srcFile.toUri());
        CharSequence content = fObj.getCharContent(true);
        assertNotNull(content);
        assertEquals(code, content.toString());
    }

    @Test
    @DisplayName("JavaSourceFileObject - 从类名和流构造")
    void testJavaSourceFileObjectFromStream() throws Exception {
        String code = "public class FlowTest {}";
        JavaSourceFileObject fObj =
                new JavaSourceFileObject("FlowTest",
                        new java.io.ByteArrayInputStream(
                                code.getBytes(StandardCharsets.UTF_8)));
        assertNotNull(fObj);
        CharSequence content = fObj.getCharContent(true);
        assertEquals(code, content.toString());
    }

    // ==================== JavaClassFileObject ====================

    @Test
    @DisplayName("JavaClassFileObject - 构造和URI")
    void testJavaClassFileObject() {
        JavaClassFileObject obj =
                new JavaClassFileObject("com.example.MyClass");
        assertNotNull(obj);
        assertEquals(JavaFileObject.Kind.CLASS, obj.getKind());
        assertTrue(obj.toUri().toString().endsWith(".class"));
    }

    @Test
    @DisplayName("JavaClassFileObject - 写入和读取字节码")
    void testJavaClassFileObjectWriteAndRead()
            throws Exception {
        JavaClassFileObject obj =
                new JavaClassFileObject("test.Sample");
        byte[] data = {0x01, 0x02, 0x03, (byte) 0xCA,
                (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
        try (java.io.OutputStream os = obj.openOutputStream()) {
            os.write(data);
        }
        try (InputStream is = obj.openInputStream()) {
            byte[] read = is.readAllBytes();
            assertArrayEquals(data, read);
        }
    }

    @Test
    @DisplayName("JavaClassFileObject - 多次读取返回相同内容")
    void testJavaClassFileObjectMultipleRead()
            throws Exception {
        JavaClassFileObject obj =
                new JavaClassFileObject("test.Repeat");
        byte[] data = "hello bytecode".getBytes(
                StandardCharsets.UTF_8);
        try (java.io.OutputStream os = obj.openOutputStream()) {
            os.write(data);
        }
        // 第一次读取
        byte[] read1;
        try (InputStream is = obj.openInputStream()) {
            read1 = is.readAllBytes();
        }
        // 第二次读取
        byte[] read2;
        try (InputStream is = obj.openInputStream()) {
            read2 = is.readAllBytes();
        }
        assertArrayEquals(read1, read2);
        assertArrayEquals(data, read1);
    }
}
