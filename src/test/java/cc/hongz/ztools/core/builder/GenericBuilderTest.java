package cc.hongz.ztools.core.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link GenericBuilder} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("GenericBuilder 通用构建器")
class GenericBuilderTest {

    @Test
    @DisplayName("build with fluent setters")
    void build_withFluentSetters() {
        Box box = GenericBuilder
                .of(Box::new)
                .with(Box::setId, 1024L)
                .with(Box::setTitle, "Hello World!")
                .with(Box::setLength, 9)
                .with(Box::setWidth, 8)
                .with(Box::setHeight, 7)
                .build();

        assertEquals(1024L, box.getId().longValue());
        assertEquals("Hello World!", box.getTitle());
        assertEquals(9, box.getLength().intValue());
        assertEquals(8, box.getWidth().intValue());
        assertEquals(7, box.getHeight().intValue());
    }

    @Test
    @DisplayName("build modifies existing object")
    void build_modifiesExistingObject() {
        Box box = GenericBuilder
                .of(Box::new)
                .with(Box::setId, 1024L)
                .with(Box::setTitle, "Hello World!")
                .build();

        Box boxModified = GenericBuilder
                .of(() -> box)
                .with(Box::setTitle, "Hello Friend!")
                .with(Box::setLength, 3)
                .with(Box::setWidth, 4)
                .with(Box::setHeight, 5)
                .build();

        assertEquals(1024L, boxModified.getId().longValue());
        assertEquals("Hello Friend!", box.getTitle());
        assertEquals(3, boxModified.getLength().intValue());
        assertEquals(4, boxModified.getWidth().intValue());
        assertEquals(5, boxModified.getHeight().intValue());
    }

    @Test
    @DisplayName("build with parameterized constructor")
    void build_withParameterizedConstructor() {
        Box box = GenericBuilder
                .of(Box::new, 2048L, "Hello Partner!", 222, 333, 444)
                .with(Box::alis)
                .build();

        assertEquals(2048L, box.getId().longValue());
        assertEquals("Hello Partner!", box.getTitle());
        assertEquals(222, box.getLength().intValue());
        assertEquals(333, box.getWidth().intValue());
        assertEquals(444, box.getHeight().intValue());
        assertEquals("TomXin:\"Hello Partner!\"", box.getTitleAlias());
    }

    @Test
    @DisplayName("build map")
    void build_map() {
        HashMap<String, String> colorMap = GenericBuilder
                .of(HashMap<String, String>::new)
                .with(Map::put, "red", "#FF0000")
                .with(Map::put, "yellow", "#FFFF00")
                .with(Map::put, "blue", "#0000FF")
                .build();

        assertEquals("#FF0000", colorMap.get("red"));
        assertEquals("#FFFF00", colorMap.get("yellow"));
        assertEquals("#0000FF", colorMap.get("blue"));
    }

    static class Box {
        private Long id;
        private String title;
        private Integer length;
        private Integer width;
        private Integer height;
        private String titleAlias;

        Box() {
        }

        Box(Long id, String title, Integer length, Integer width, Integer height) {
            this.id = id;
            this.title = title;
            this.length = length;
            this.width = width;
            this.height = height;
        }

        Long getId() {
            return id;
        }

        void setId(Long id) {
            this.id = id;
        }

        String getTitle() {
            return title;
        }

        void setTitle(String title) {
            this.title = title;
        }

        Integer getLength() {
            return length;
        }

        void setLength(Integer length) {
            this.length = length;
        }

        Integer getWidth() {
            return width;
        }

        void setWidth(Integer width) {
            this.width = width;
        }

        Integer getHeight() {
            return height;
        }

        void setHeight(Integer height) {
            this.height = height;
        }

        String getTitleAlias() {
            return titleAlias;
        }

        void alis() {
            if (null != this.title && !this.title.isEmpty()) {
                this.titleAlias = "TomXin:\"" + title + "\"";
            }
        }
    }
}
