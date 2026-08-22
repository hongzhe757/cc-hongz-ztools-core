package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.annotation.Alias;
import cc.hongz.ztools.core.bean.copier.CopyOptions;
import cc.hongz.ztools.core.bean.copier.ValueProvider;
import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.collection.ListUtil;
import cc.hongz.ztools.core.date.StopWatch;
import cc.hongz.ztools.core.lang.Console;
import cc.hongz.ztools.core.lang.Dict;
import cc.hongz.ztools.core.map.MapBuilder;
import cc.hongz.ztools.core.map.MapUtil;
import cc.hongz.ztools.core.thread.ThreadUtil;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.ObjectUtil;
import cc.hongz.ztools.core.util.StrUtil;
import lombok.*;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Bean 工具")
class BeanUtilTest {

    /**
     * 验证 HashMap 不包含 setXXX 方法，因此不是 Bean.
     */
    @Test
    @DisplayName("HashMap 非 Bean")
    void isBeanTest() {

        // HashMap不包含setXXX方法，不是bean
        final boolean isBean = BeanUtil.isBean(HashMap.class);
        assertFalse(isBean);
    }

    /**
     * 验证通过 ValueProvider 填充 name 和 age 字段.
     */
    @Test
    @DisplayName("ValueProvider 填充")
    void fillBeanTest() {
        final Person person = BeanUtil.fillBean(new Person(), new ValueProvider<String>() {

            @Override
            public Object value(final String key, final Type valueType) {
                switch (key) {
                    case "name":
                        return "张三";
                    case "age":
                        return 18;
                }
                return null;
            }

            @Override
            public boolean containsKey(final String key) {
                // 总是存在key
                return true;
            }

        }, CopyOptions.create());

        assertEquals("张三", person.getName());
        assertEquals(18, person.getAge());
    }

    /**
     * 验证忽略大小写方式填充 Bean，Name、aGe 键能匹配到对应字段.
     */
    @Test
    @DisplayName("忽略大小写填充")
    void fillBeanWithMapIgnoreCaseTest() {
        final Map<String, Object> map = MapBuilder.<String, Object>create()
            .put("Name", "Joe")
            .put("aGe", 12)
            .put("openId", "DFDFSDFWERWER")
            .build();
        final SubPerson person = BeanUtil.fillBeanWithMapIgnoreCase(map, new SubPerson(), false);
        assertEquals("Joe", person.getName());
        assertEquals(12, person.getAge());
        assertEquals("DFDFSDFWERWER", person.getOpenid());
    }

    /**
     * 验证 Bean 转 Map 时忽略 static 静态属性.
     */
    @Test
    @DisplayName("忽略静态属性")
    void toBeanTest() {
        final SubPerson person = new SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");

        final Map<?, ?> map = BeanUtil.toBean(person, Map.class);
        assertEquals("测试A11", map.get("name"));
        assertEquals(14, map.get("age"));
        assertEquals("11213232", map.get("openid"));
        // static属性应被忽略
        assertFalse(map.containsKey("SUBNAME"));
    }

    /**
     * 验证在忽略错误模式下，转换失败的字段被跳过并使用对象默认值.
     */
    @Test
    @DisplayName("忽略转换错误")
    void toBeanIgnoreErrorTest() {
        final HashMap<String, Object> map = MapUtil.newHashMap();
        map.put("name", "Joe");
        // 错误的类型，此处忽略
        map.put("age", "aaaaaa");

        final Person person = BeanUtil.toBeanIgnoreError(map, Person.class);
        assertEquals("Joe", person.getName());
        // 错误的类型，不copy这个字段，使用对象创建的默认值
        assertEquals(0, person.getAge());
    }

    /**
     * 验证忽略大小写方式将 Map 转为 Bean.
     */
    @Test
    @DisplayName("忽略大小写转 Bean")
    void mapToBeanIgnoreCaseTest() {
        final HashMap<String, Object> map = MapUtil.newHashMap();
        map.put("Name", "Joe");
        map.put("aGe", 12);

        final Person person = BeanUtil.toBeanIgnoreCase(map, Person.class, false);
        assertEquals("Joe", person.getName());
        assertEquals(12, person.getAge());
    }

    /**
     * 验证通过字段映射将 Map 的别名键映射到 Bean 字段.
     */
    @Test
    @DisplayName("字段映射转 Bean")
    void mapToBeanTest() {
        final HashMap<String, Object> map = MapUtil.newHashMap();
        map.put("a_name", "Joe");
        map.put("b_age", 12);

        // 别名，用于对应bean的字段名
        final HashMap<String, String> mapping = MapUtil.newHashMap();
        mapping.put("a_name", "name");
        mapping.put("b_age", "age");

        final Person person = BeanUtil.toBean(map, Person.class, CopyOptions.create().setFieldMapping(mapping));
        assertEquals("Joe", person.getName());
        assertEquals(12, person.getAge());
    }

    /**
     * 验证 public 类型的字段可直接注入赋值.
     */
    @Test
    @DisplayName("公有字段注入")
    void mapToBeanTest2() {
        final HashMap<String, Object> map = MapUtil.newHashMap();
        map.put("name", "Joe");
        map.put("age", 12);

        // 非空构造也可以实例化成功
        final Person2 person = BeanUtil.toBean(map, Person2.class, CopyOptions.create());
        assertEquals("Joe", person.name);
        assertEquals(12, person.age);
    }

    /**
     * 验证在不忽略错误情况下，转换失败时抛出异常.
     */
    @Test
    @DisplayName("非法值抛异常")
    void mapToBeanWinErrorTest() {
        assertThrows(NumberFormatException.class, () -> {
            final Map<String, String> map = new HashMap<>();
            map.put("age", "哈哈");
            BeanUtil.toBean(map, Person.class);
        });
    }

    /**
     * 验证 Bean 转 Map 时忽略 static 静态属性.
     */
    @Test
    @DisplayName("Bean 转 Map")
    void beanToMapTest() {
        final SubPerson person = new SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");

        final Map<String, Object> map = BeanUtil.beanToMap(person);

        assertEquals("测试A11", map.get("name"));
        assertEquals(14, map.get("age"));
        assertEquals("11213232", map.get("openid"));
        // static属性应被忽略
        assertFalse(map.containsKey("SUBNAME"));
    }

    /**
     * 验证 Bean 转 Map 时传入 null 属性列表仍可正常转换并忽略静态属性.
     */
    @Test
    @DisplayName("null 属性转 Map")
    void beanToMapNullPropertiesTest() {
        final SubPerson person = new SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");

        final Map<String, Object> map = BeanUtil.beanToMap(person, (String[]) null);

        assertEquals("测试A11", map.get("name"));
        assertEquals(14, map.get("age"));
        assertEquals("11213232", map.get("openid"));
        // static属性应被忽略
        assertFalse(map.containsKey("SUBNAME"));
    }

    /**
     * 验证 Bean 转 Map 时可生成下划线分隔的驼峰键.
     */
    @Test
    @DisplayName("驼峰键转 Map")
    void beanToMapTest2() {
        final SubPerson person = new SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");

        final Map<String, Object> map = BeanUtil.beanToMap(person, true, true);
        assertEquals("sub名字", map.get("sub_name"));
    }

    /**
     * 验证 Bean 转 Map 时通过字段值编辑器改写值.
     */
    @Test
    @DisplayName("值编辑器转 Map")
    void beanToMapWithValueEditTest() {
        final SubPerson person = new SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");

        final Map<String, Object> map = BeanUtil.beanToMap(person, new LinkedHashMap<>(),
            CopyOptions.create().setFieldValueEditor((key, value) -> key + "_" + value));
        assertEquals("subName_sub名字", map.get("subName"));
    }

    /**
     * 验证 Bean 转 Map 时使用别名字段作为键.
     */
    @Test
    @DisplayName("别名转 Map")
    void beanToMapWithAliasTest() {
        final SubPersonWithAlias person = new SubPersonWithAlias();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");
        person.setSlow(true);
        person.setBooleana(true);
        person.setBooleanb(true);

        final Map<String, Object> map = BeanUtil.beanToMap(person);
        assertEquals("sub名字", map.get("aliasSubName"));
    }

    /**
     * 验证 Map 转 Bean 时根据别名字段赋值.
     */
    @Test
    @DisplayName("别名转 Bean")
    void mapToBeanWithAliasTest() {
        final Map<String, Object> map = MapUtil.newHashMap();
        map.put("aliasSubName", "sub名字");
        map.put("slow", true);
        map.put("is_booleana", "1");
        map.put("is_booleanb", true);

        final SubPersonWithAlias subPersonWithAlias = BeanUtil.toBean(map, SubPersonWithAlias.class);
        assertEquals("sub名字", subPersonWithAlias.getSubName());

        //https://gitee.com/chinabugotech/hutool/issues/I6H0XF
        assertFalse(subPersonWithAlias.isBooleana());
        assertNull(subPersonWithAlias.getBooleanb());
    }

    /**
     * 验证 Bean 转 Map 时保留 LocalDateTime 与 LocalDate 类型不变.
     */
    @Test
    @DisplayName("保留时间类型")
    void beanToMapWithLocalDateTimeTest() {
        final LocalDateTime now = LocalDateTime.now();

        final SubPerson person = new SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");
        person.setDate(now);
        person.setDate2(now.toLocalDate());

        final Map<String, Object> map = BeanUtil.beanToMap(person, false, true);
        assertEquals(now, map.get("date"));
        assertEquals(now.toLocalDate(), map.get("date2"));
    }

    /**
     * 验证可读取 Bean 中的属性值.
     */
    @Test
    @DisplayName("读取属性")
    void getPropertyTest() {
        final SubPerson person = new SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");

        final Object name = BeanUtil.getProperty(person, "name");
        assertEquals("测试A11", name);
        final Object subName = BeanUtil.getProperty(person, "subName");
        assertEquals("sub名字", subName);
    }

    /**
     * 验证对 null 对象读取属性时返回 null.
     */
    @Test
    @SuppressWarnings("ConstantConditions")
    @DisplayName("null 取属性")
    void getNullPropertyTest() {
        final Object property = BeanUtil.getProperty(null, "name");
        assertNull(property);
    }

    /**
     * 验证可获取 Bean 的全部属性描述符.
     */
    @Test
    @DisplayName("获取属性描述")
    void getPropertyDescriptorsTest() {
        final HashSet<Object> set = CollUtil.newHashSet();
        final PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(SubPerson.class);
        for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            set.add(propertyDescriptor.getName());
        }
        assertTrue(set.contains("age"));
        assertTrue(set.contains("id"));
        assertTrue(set.contains("name"));
        assertTrue(set.contains("openid"));
        assertTrue(set.contains("slow"));
        assertTrue(set.contains("subName"));
    }

    /**
     * 验证拷贝属性到新的目标实例.
     */
    @Test
    @DisplayName("拷贝到新实例")
    void copyPropertiesTest() {
        final SubPerson person = new SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");

        final SubPerson person1 = BeanUtil.copyProperties(person, SubPerson.class);
        assertEquals(14, person1.getAge());
        assertEquals("11213232", person1.getOpenid());
        assertEquals("测试A11", person1.getName());
        assertEquals("sub名字", person1.getSubName());
    }

    /**
     * 验证多线程环境下拷贝属性的稳定性.
     */
    @Test
    @Disabled
    @DisplayName("多线程拷贝")
    void multiThreadTest() {
        final Student student = new Student();
        student.setName("张三");
        student.setAge(123);
        student.setNo(3158L);

        final Student student2 = new Student();
        student.setName("李四");
        student.setAge(125);
        student.setNo(8848L);

        final List<Student> studentList = ListUtil.of(student, student2);

        for (int i = 0; i < 5000; i++) {
            new Thread(() -> {
                final List<Student> list = ObjectUtil.clone(studentList);
                final List<Student> listReps = list.stream().map(s1 -> {
                    final Student s2 = new Student();
                    BeanUtil.copyProperties(s1, s2);
                    return s2;
                }).collect(Collectors.toList());

                System.out.println(listReps);
            }).start();
        }

        ThreadUtil.waitForDie();
    }

    /**
     * 验证 isXXX 与非 isXXX 形式的 boolean 字段均能正确拷贝.
     */
    @Test
    @DisplayName("布尔字段拷贝")
    void copyPropertiesHasBooleanTest() {
        final SubPerson p1 = new SubPerson();
        p1.setSlow(true);

        // 测试boolean参数值isXXX形式
        final SubPerson p2 = new SubPerson();
        BeanUtil.copyProperties(p1, p2);
        assertTrue(p2.getSlow());

        // 测试boolean参数值非isXXX形式
        final SubPerson2 p3 = new SubPerson2();
        BeanUtil.copyProperties(p1, p3);
        assertTrue(p3.getSlow());
    }

    /**
     * 验证忽略 null 值时保留目标已有属性，否则被 null 覆盖.
     */
    @Test
    @DisplayName("忽略 null 拷贝")
    void copyPropertiesIgnoreNullTest() {
        final SubPerson p1 = new SubPerson();
        p1.setSlow(true);
        p1.setName(null);

        final SubPerson2 p2 = new SubPerson2();
        p2.setName("oldName");

        // null值不覆盖目标属性
        BeanUtil.copyProperties(p1, p2, CopyOptions.create().ignoreNullValue());
        assertEquals("oldName", p2.getName());

        // null覆盖目标属性
        BeanUtil.copyProperties(p1, p2);
        assertNull(p2.getName());
    }

    /**
     * 验证将 Bean 拷贝为 Map.
     */
    @Test
    @DisplayName("Bean 拷贝为 Map")
    void copyPropertiesBeanToMapTest() {
        // 测试BeanToMap
        final SubPerson p1 = new SubPerson();
        p1.setSlow(true);
        p1.setName("测试");
        p1.setSubName("sub测试");

        final Map<String, Object> map = MapUtil.newHashMap();
        BeanUtil.copyProperties(p1, map);
        assertTrue((Boolean) map.get("slow"));
        assertEquals("测试", map.get("name"));
        assertEquals("sub测试", map.get("subName"));
    }

    /**
     * 验证将 Map 拷贝为 Map.
     */
    @Test
    @DisplayName("Map 拷贝为 Map")
    void copyPropertiesMapToMapTest() {
        // 测试MapToMap
        final Map<String, Object> p1 = new HashMap<>();
        p1.put("isSlow", true);
        p1.put("name", "测试");
        p1.put("subName", "sub测试");

        final Map<String, Object> map = MapUtil.newHashMap();
        BeanUtil.copyProperties(p1, map);
        assertTrue((Boolean) map.get("isSlow"));
        assertEquals("测试", map.get("name"));
        assertEquals("sub测试", map.get("subName"));
    }

    /**
     * 验证 Map 拷贝为 Map 时忽略 null 值，null 键不写入目标.
     */
    @Test
    @DisplayName("忽略 null Map 拷贝")
    void copyPropertiesMapToMapIgnoreNullTest() {
        // 测试MapToMap
        final Map<String, Object> p1 = new HashMap<>();
        p1.put("isSlow", true);
        p1.put("name", "测试");
        p1.put("subName", null);

        final Map<String, Object> map = MapUtil.newHashMap();
        BeanUtil.copyProperties(p1, map, CopyOptions.create().setIgnoreNullValue(true));
        assertTrue((Boolean) map.get("isSlow"));
        assertEquals("测试", map.get("name"));
        assertFalse(map.containsKey("subName"));
    }

    /**
     * 验证去除字符串字段首尾空格并返回新对象.
     */
    @Test
    @DisplayName("去除字段空格")
    void trimBeanStrFieldsTest() {
        final Person person = new Person();
        person.setAge(1);
        person.setName("  张三 ");
        person.setOpenid(null);
        final Person person2 = BeanUtil.trimStrFields(person);

        // 是否改变原对象
        assertEquals("张三", person.getName());
        assertEquals("张三", person2.getName());
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Getter
    @Setter
    public static class SubPerson extends Person {

        public static final String SUBNAME = "TEST";

        private UUID id;
        private String subName;
        private Boolean slow;
        private LocalDateTime date;
        private LocalDate date2;
    }

    @Getter
    @Setter
    public static class SubPerson2 extends Person {
        private String subName;
        // boolean参数值非isXXX形式
        private Boolean slow;
    }

    @Getter
    @Setter
    @ToString
    public static class SubPersonWithAlias extends Person {
        // boolean参数值非isXXX形式
        @Alias("aliasSubName")
        private String subName;
        private Boolean slow;
        private boolean booleana;
        private Boolean booleanb;
    }

    @Getter
    @Setter
    public static class SubPersonWithOverlayTransientField extends PersonWithTransientField {
        // 覆盖父类中 transient 属性
        private String name;
    }

    @Getter
    @Setter
    public static class Person {
        private String name;
        private int age;
        private String openid;
    }

    @Getter
    @Setter
    public static class PersonWithTransientField {
        private transient String name;
        private int age;
        private String openid;
    }

    public static class Person2 {

        public Person2(final String name, final int age, final String openid) {
            this.name = name;
            this.age = age;
            this.openid = openid;
        }

        public String name;
        public int age;
        public String openid;
    }

    /**
     * 验证子类字段可覆盖父类的 transient 属性并正确拷贝.
     */
    @Test
    @DisplayName("覆盖 transient 字段")
    void beanToBeanOverlayFieldTest() {
        final SubPersonWithOverlayTransientField source = new SubPersonWithOverlayTransientField();
        source.setName("zhangsan");
        source.setAge(20);
        source.setOpenid("1");
        final SubPersonWithOverlayTransientField dest = new SubPersonWithOverlayTransientField();
        BeanUtil.copyProperties(source, dest);

        assertEquals(source.getName(), dest.getName());
        assertEquals(source.getAge(), dest.getAge());
        assertEquals(source.getOpenid(), dest.getOpenid());
    }

    /**
     * 验证对象无 getter 方法时转换不报错.
     */
    @Test
    @DisplayName("无 getter 转 Bean")
    void beanToBeanTest() {
        // 修复对象无getter方法导致报错的问题
        final Page page1 = new Page();
        BeanUtil.toBean(page1, Page.class);
    }

    public static class Page {
        private boolean optimizeCountSql = true;

        public boolean optimizeCountSql() {
            return optimizeCountSql;
        }

        public Page setOptimizeCountSql(final boolean optimizeCountSql) {
            this.optimizeCountSql = optimizeCountSql;
            return this;
        }
    }

    /**
     * 验证拷贝属性时别名字段有效.
     */
    @Test
    @DisplayName("别名字段拷贝")
    void copyBeanToBeanTest() {
        // 测试在copyProperties方法中alias是否有效
        final Food info = new Food();
        info.setBookID("0");
        info.setCode("123");
        final HllFoodEntity entity = new HllFoodEntity();
        BeanUtil.copyProperties(info, entity);
        assertEquals(info.getBookID(), entity.getBookId());
        assertEquals(info.getCode(), entity.getCode2());
    }

    /**
     * 验证拷贝时按属性名忽略指定属性.
     */
    @Test
    @DisplayName("忽略属性拷贝")
    void copyBeanTest() {
        final Food info = new Food();
        info.setBookID("0");
        info.setCode("123");
        final Food newFood = BeanUtil.copyProperties(info, Food.class, "code");
        assertEquals(info.getBookID(), newFood.getBookID());
        assertNull(newFood.getCode());
    }

    /**
     * 验证源对象为 null 时拷贝返回 null.
     */
    @Test
    @DisplayName("null 源拷贝")
    void copyNullTest() {
        assertNull(BeanUtil.copyProperties(null, Food.class));
    }

    /**
     * 验证通过属性过滤器过滤空字符串字段.
     */
    @Test
    @DisplayName("属性过滤拷贝")
    void copyBeanPropertiesFilterTest() {
        final Food info = new Food();
        info.setBookID("0");
        info.setCode("");
        final Food newFood = new Food();
        final CopyOptions copyOptions = CopyOptions.create().setPropertiesFilter((f, v) -> !(v instanceof CharSequence) || StrUtil.isNotBlank(v.toString()));
        BeanUtil.copyProperties(info, newFood, copyOptions);
        assertEquals(info.getBookID(), newFood.getBookID());
        assertNull(newFood.getCode());
    }

    /**
     * 验证通过 getter 方法引用忽略指定属性.
     */
    @Test
    @DisplayName("getter 忽略属性")
    void copyBeanPropertiesFunctionFilterTest() {
        //https://gitee.com/chinabugotech/hutool/pulls/590
        final Person o = new Person();
        o.setName("asd");
        o.setAge(123);
        o.setOpenid("asd");

        @SuppressWarnings("unchecked") final CopyOptions copyOptions = CopyOptions.create().setIgnoreProperties(Person::getAge, Person::getOpenid);
        final Person n = new Person();
        BeanUtil.copyProperties(o, n, copyOptions);

        // 是否忽略拷贝属性
        assertNotEquals(o.getAge(), n.getAge());
        assertNotEquals(o.getOpenid(), n.getOpenid());
    }

    @Data
    public static class Food {
        @Alias("bookId")
        private String bookID;
        private String code;
    }

    @Data
    public static class HllFoodEntity implements Serializable {
        private static final long serialVersionUID = 1L;

        private String bookId;
        @Alias("code")
        private String code2;
    }

    /**
     * 验证通过嵌套路径赋值时可自动创建 Map 与 List.
     */
    @Test
    @DisplayName("嵌套路径赋值")
    void setPropertiesTest() {
        final Map<String, Object> resultMap = MapUtil.newHashMap();
        BeanUtil.setProperty(resultMap, "codeList[0].name", "张三");
        assertEquals("{codeList=[{name=张三}]}", resultMap.toString());
    }

    /**
     * 验证泛型类型 id 字段可正确拷贝.
     */
    @Test
    @DisplayName("泛型 ID 拷贝")
    void beanCopyTest() {
        final Station station = new Station();
        station.setId(123456L);

        final Station station2 = new Station();

        BeanUtil.copyProperties(station, station2);
        assertEquals(new Long(123456L), station2.getId());
    }

    enum Version {
        dev,
        prod
    }

    @Data
    public static class Vto {
        EnumSet<Version> versions;
    }


    /**
     * 验证 EnumSet 类型字段可正确拷贝.
     */
    @Test
    @DisplayName("EnumSet 拷贝")
    void beanWithEnumSetTest() {
        final Vto v1 = new Vto();
        v1.setVersions(EnumSet.allOf(Version.class));
        final Vto v2 = BeanUtil.copyProperties(v1, Vto.class);
        assertNotNull(v2);
        assertNotNull(v2.getVersions());
    }

    /**
     * 验证通过 CollUtil 可创建 EnumSet 集合.
     */
    @Test
    @DisplayName("创建 EnumSet")
    void enumSetTest() {
        final Collection<Version> objects = CollUtil.create(EnumSet.class, Version.class);
        assertNotNull(objects);
        assertTrue(EnumSet.class.isAssignableFrom(objects.getClass()));
    }

    static class Station extends Tree<Long> {
    }

    static class Tree<T> extends Entity<T> {
    }

    @Data
    public static class Entity<T> {
        private T id;
    }

    /**
     * 验证将列表元素拷贝转换为目标类型列表.
     */
    @Test
    @DisplayName("列表元素转换")
    void copyListTest() {
        final Student student = new Student();
        student.setName("张三");
        student.setAge(123);
        student.setNo(3158L);

        final Student student2 = new Student();
        student.setName("李四");
        student.setAge(125);
        student.setNo(8848L);

        final List<Student> studentList = ListUtil.of(student, student2);
        final List<Person> people = BeanUtil.copyToList(studentList, Person.class);

        assertEquals(studentList.size(), people.size());
        for (int i = 0; i < studentList.size(); i++) {
            assertEquals(studentList.get(i).getName(), people.get(i).getName());
            assertEquals(studentList.get(i).getAge(), people.get(i).getAge());
        }

    }

    /**
     * 验证大列表元素拷贝转换的正确性与性能.
     */
    @Test
    @DisplayName("大列表拷贝")
    void copyLargeListTest(){
        final SubPerson person = new SubPerson();
        person.setName("测试A11");
        person.setAge(14);
        person.setOpenid("11213232");

        person.setId(UUID.randomUUID());
        person.setSubName("sub名字");
        person.setSlow(true);
        person.setDate(LocalDateTime.now());
        person.setDate2(LocalDate.now());

        final List<SubPerson> list = new ArrayList<>();
        CollUtil.padRight(list, 1000, person);

        // 预先构建一次缓存，防止干扰
        BeanUtil.copyProperties(person, new SubPerson2());
        // org.springframework.beans.BeanUtils.copyProperties(new SubPerson(), new SubPerson2());

        Console.log("copy bean size: {}\n", list.size());
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start("BeanUtil#copyToList");
        List<SubPerson2> copyList = BeanUtil.copyToList(list, SubPerson2.class);
        // list.forEach(item -> org.springframework.beans.BeanUtils.copyProperties(item, new SubPerson2()));
        stopWatch.stop();
        Console.log(stopWatch.prettyPrint());
        assertEquals(copyList.size(),list.size());
    }


    /**
     * 验证 Bean 转 Map 时通过键编辑器过滤键，无效键不写入.
     */
    @Test
    @DisplayName("键编辑器转 Map")
    void toMapTest() {
        // 测试转map的时候返回key
        final PrivilegeIClassification a = new PrivilegeIClassification();
        a.setId("1");
        a.setName("2");
        a.setCode("3");
        a.setCreateTime(new Date());
        a.setSortOrder(9L);

        final Map<String, Object> f = BeanUtil.beanToMap(
            a,
            new LinkedHashMap<>(),
            false,
            key -> Arrays.asList("id", "name", "code", "sortOrder").contains(key) ? key : null);
        assertFalse(f.containsKey(null));
    }

    @Data
    public static class PrivilegeIClassification implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id;
        private String name;
        private String code;
        private Long rowStatus;
        private Long sortOrder;
        private Date createTime;
    }

    /**
     * 验证通过路径读取数组字段中所有元素的属性值.
     */
    @Test
    @DisplayName("数组字段取值")
    void getFieldValue() {
        final TestPojo testPojo = new TestPojo();
        testPojo.setName("名字");

        final TestPojo2 testPojo2 = new TestPojo2();
        testPojo2.setAge(2);
        final TestPojo2 testPojo3 = new TestPojo2();
        testPojo3.setAge(3);


        testPojo.setTestPojo2List(new TestPojo2[]{testPojo2, testPojo3});

        final BeanPath beanPath = BeanPath.create("testPojo2List.age");
        final Object o = beanPath.get(testPojo);

        assertEquals(Integer.valueOf(2), ArrayUtil.get(o, 0));
        assertEquals(Integer.valueOf(3), ArrayUtil.get(o, 1));
    }

    @Data
    public static class TestPojo {
        private String name;
        private TestPojo2[] testPojo2List;
    }

    @Data
    public static class TestPojo2 {
        private int age;
    }

    @Data
    public static class Student implements Serializable {
        private static final long serialVersionUID = 1L;

        String name;
        int age;
        Long no;
    }

    /**
     * 验证拷贝时通过字段名编辑器将下划线字段转为驼峰字段.
     *
     * @author dazer
     */
    @Test
    @DisplayName("字段名编辑器拷贝")
    void beanToBeanCopyOptionsTest() {
        final ChildVo1 childVo1 = new ChildVo1();
        childVo1.setChild_address("中国北京五道口");
        childVo1.setChild_name("张三");
        childVo1.setChild_father_name("张无忌");
        childVo1.setChild_mother_name("赵敏敏");

        final CopyOptions copyOptions = CopyOptions.create().
            //setIgnoreNullValue(true).
            //setIgnoreCase(false).
                setFieldNameEditor(StrUtil::toCamelCase);

        final ChildVo2 childVo2 = new ChildVo2();
        BeanUtil.copyProperties(childVo1, childVo2, copyOptions);

        assertEquals(childVo1.getChild_address(), childVo2.getChildAddress());
        assertEquals(childVo1.getChild_name(), childVo2.getChildName());
        assertEquals(childVo1.getChild_father_name(), childVo2.getChildFatherName());
        assertEquals(childVo1.getChild_mother_name(), childVo2.getChildMotherName());
    }

    @Data
    public static class ChildVo1 {
        String child_name;
        String child_address;
        String child_mother_name;
        String child_father_name;
    }

    @Data
    public static class ChildVo2 {
        String childName;
        String childAddress;
        String childMotherName;
        String childFatherName;
    }

    /**
     * 验证泛型集合转换失败时忽略错误并返回 null.
     */
    @Test
    @Disabled("泛型集合转换边界用例：List<String> -> List<Integer> 需报错忽略，ztools 移植暂未覆盖")
    @DisplayName("泛型集合转换")
    void issueI41WKPTest() {
        final Test1 t1 = new Test1().setStrList(ListUtil.toList("list"));
        final Test2 t2_hu = new Test2();
        BeanUtil.copyProperties(t1, t2_hu, CopyOptions.create().setIgnoreError(true));
        assertNull(t2_hu.getStrList());
    }

    @Data
    @Accessors(chain = true)
    public static class Test1 {
        private List<String> strList;
    }

    @Data
    @Accessors(chain = true)
    public static class Test2 {
        private List<Integer> strList;
    }

    /**
     * 验证空字符串不会写入 LocalDateTime 字段，保持为 null.
     */
    @Test
    @DisplayName("空字符串转 Bean")
    void issuesI53O9JTest() {
        final Map<String, String> map = new HashMap<>();
        map.put("statusIdUpdateTime", "");

        final WkCrmCustomer customer = new WkCrmCustomer();
        BeanUtil.copyProperties(map, customer);

        assertNull(customer.getStatusIdUpdateTime());
    }

    @Data
    public static class WkCrmCustomer {
        private LocalDateTime statusIdUpdateTime;
    }

    /**
     * 验证通过 ValueProvider 结合字段映射将值转为 Bean.
     */
    @Test
    @DisplayName("ValueProvider 转 Bean")
    void valueProviderToBeanTest() {
        // https://gitee.com/chinabugotech/hutool/issues/I5B4R7
        final CopyOptions copyOptions = CopyOptions.create();
        final Map<String, String> filedMap = new HashMap<>();
        filedMap.put("name", "sourceId");
        copyOptions.setFieldMapping(filedMap);
        final TestPojo pojo = BeanUtil.toBean(TestPojo.class, new ValueProvider<String>() {
            final HashMap<String, Object> map = new HashMap<>();

            {
                map.put("sourceId", "123");
            }

            @Override
            public Object value(final String key, final Type valueType) {
                return map.get(key);
            }

            @Override
            public boolean containsKey(final String key) {
                return map.containsKey(key);
            }
        }, copyOptions);
        assertEquals("123", pojo.getName());
    }

    @Data
    @EqualsAndHashCode
    private static class TestUserEntity {
        private String username;
        private String name;
        private Integer age;
        private Integer sex;
        private String mobile;
        private Date createTime;
    }

    @Data
    @EqualsAndHashCode
    private static class TestUserDTO {
        private String name;
        private Integer age;
        private Integer sex;
        private String mobile;
    }

    /**
     * 验证比较两个对象公共字段值是否相等，并支持忽略指定字段.
     */
    @Test
    @DisplayName("公共字段比较")
    void isCommonFieldsEqualTest() {
        final TestUserEntity userEntity = new TestUserEntity();
        final TestUserDTO userDTO = new TestUserDTO();

        userDTO.setAge(20);
        userDTO.setName("takaki");
        userDTO.setSex(1);
        userDTO.setMobile("17812312023");

        BeanUtil.copyProperties(userDTO, userEntity);

        assertTrue(BeanUtil.isCommonFieldsEqual(userDTO, userEntity));

        userEntity.setAge(13);
        assertFalse(BeanUtil.isCommonFieldsEqual(userDTO, userEntity));
        assertTrue(BeanUtil.isCommonFieldsEqual(userDTO, userEntity, "age"));

        assertTrue(BeanUtil.isCommonFieldsEqual(null, null));
        assertFalse(BeanUtil.isCommonFieldsEqual(null, userEntity));
        assertFalse(BeanUtil.isCommonFieldsEqual(userEntity, null));

        userEntity.setSex(0);
        assertTrue(BeanUtil.isCommonFieldsEqual(userDTO, userEntity, "age", "sex"));
    }

    /**
     * 验证 Object 类无 getter 方法，返回 false.
     */
    @Test
    @DisplayName("Object 无 getter")
    void hasGetterTest() {
        // https://gitee.com/chinabugotech/hutool/issues/I6M7Z7
        final boolean b = BeanUtil.hasGetter(Object.class);
        assertFalse(b);
    }

    /**
     * 验证 Dict 不是 Bean.
     */
    @Test
    @DisplayName("Dict 非 Bean")
    void issueI9VTZGTest() {
        final boolean bean = BeanUtil.isBean(Dict.class);
        assertFalse(bean);
    }

    // ============ Test for GitHub issue #4245 ============

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order4245 {
        private Long orderId;
        private String orderNo;
        private List<OrderItem4245> orderItemList;
    }

    @Getter
    @Setter(AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor
    public static class OrderItem4245 {
        private Long itemId;
        private String productName;
        private Integer quantity;
    }

    @Data
    public static class OrderDto4245 {
        private Long orderId;
        private String orderNo;
        private List<OrderItemDto4245> orderItemList;
    }

    @Data
    public static class OrderItemDto4245 {
        private Long itemId;
        private String productName;
        private Integer quantity;
    }

    /**
     * 验证源列表元素具有 protected setter 时仍可拷贝，不抛出 ConvertException.
     */
    @Test
    @Disabled("泛型集合深拷贝边界用例：List<OrderItem> -> List<OrderItemDto> 需元素级转换，ztools 移植暂未覆盖")
    @DisplayName("protected setter 列表拷贝")
    void issue4245Test() {
        final Order4245 order = new Order4245(
                1L,
                "01",
                new ArrayList<>()
        );
        order.getOrderItemList().add(
                new OrderItem4245(1L, "aa", 1)
        );

        // This should not throw ConvertException
        final OrderDto4245 dto = BeanUtil.copyProperties(order, OrderDto4245.class);

        assertNotNull(dto);
        assertEquals(order.getOrderId(), dto.getOrderId());
        assertEquals(order.getOrderNo(), dto.getOrderNo());
        assertNotNull(dto.getOrderItemList());
        assertEquals(1, dto.getOrderItemList().size());
        assertEquals(Long.valueOf(1L), dto.getOrderItemList().get(0).getItemId());
        assertEquals("aa", dto.getOrderItemList().get(0).getProductName());
        assertEquals(Integer.valueOf(1), dto.getOrderItemList().get(0).getQuantity());
    }

    /**
     * 验证具有 protected setter 的 Bean 可正常转为 Map.
     */
    @Test
    @DisplayName("protected setter 转 Map")
    void issue4245MapConvertTest() {
        final OrderItem4245 item = new OrderItem4245(1L, "aa", 1);
        // Should not throw UnsupportedOperationException
        final Map<String, Object> map = BeanUtil.beanToMap(item);
        assertNotNull(map);
        assertEquals(1L, map.get("itemId"));
        assertEquals("aa", map.get("productName"));
        assertEquals(1, map.get("quantity"));
    }
}
