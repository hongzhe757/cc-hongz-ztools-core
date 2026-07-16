package cc.hongz.ztools.core.util;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JAXBUtil 单元测试.
 */
class JAXBUtilTest {

    /**
     * 测试用 Bean.
     */
    @XmlRootElement(name = "student")
    public static class Student {
        private String name;
        private int age;

        public Student() {
        }

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @XmlElement
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @XmlElement
        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Student student = (Student) o;
            return age == student.age && Objects.equals(name, student.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

    @Test
    void beanToXmlDefaultTest() {
        Student student = new Student("张三", 20);
        String xml = JAXBUtil.beanToXml(student);
        assertNotNull(xml);
        assertTrue(xml.contains("张三"));
        assertTrue(xml.contains("20"));
        assertTrue(xml.contains("student"));
    }

    @Test
    void beanToXMLWithCharsetAndFormatTest() {
        Student student = new Student("李四", 25);
        // 格式化输出
        String xmlFormatted = JAXBUtil.beanToXml(student, StandardCharsets.UTF_8, true);
        assertNotNull(xmlFormatted);
        assertTrue(xmlFormatted.contains("李四"));

        // 不格式化
        xmlFormatted = JAXBUtil.beanToXml(student, StandardCharsets.UTF_8, false);
        assertNotNull(xmlFormatted);
        assertTrue(xmlFormatted.contains("李四"));
    }

    @Test
    void xmlToBeanFromStringTest() {
        Student original = new Student("王五", 30);
        String xml = JAXBUtil.beanToXml(original);
        Student result = JAXBUtil.xmlToBean(xml, Student.class);
        assertNotNull(result);
        assertEquals(original, result);
    }

    @Test
    void xmlToBeanFromReaderTest() {
        Student original = new Student("赵六", 22);
        String xml = JAXBUtil.beanToXml(original);
        Student result = JAXBUtil.xmlToBean(new StringReader(xml), Student.class);
        assertNotNull(result);
        assertEquals(original, result);
    }

    @Test
    void xmlToBeanFromFileTest() throws Exception {
        Student original = new Student("孙七", 28);
        String xml = JAXBUtil.beanToXml(original);

        // 写入临时文件
        File tempFile = File.createTempFile("jaxb_test_", ".xml");
        tempFile.deleteOnExit();
        cc.hongz.ztools.core.io.FileUtil.writeString(xml, tempFile, StandardCharsets.UTF_8);

        Student result = JAXBUtil.xmlToBean(tempFile, StandardCharsets.UTF_8, Student.class);
        assertNotNull(result);
        assertEquals(original, result);
    }

    @Test
    void roundTripTest() {
        // 测试 Bean → XML → Bean 往返一致性
        Student original = new Student("测试用户", 99);
        String xml = JAXBUtil.beanToXml(original);
        Student restored = JAXBUtil.xmlToBean(xml, Student.class);
        assertEquals(original.getName(), restored.getName());
        assertEquals(original.getAge(), restored.getAge());
    }

    @Test
    void beanToXmlInvalidBeanTest() {
        // 没有 @XmlRootElement 注解的对象应抛出异常
        assertThrows(Exception.class, () -> JAXBUtil.beanToXml("not a jaxb bean"));
    }
}
