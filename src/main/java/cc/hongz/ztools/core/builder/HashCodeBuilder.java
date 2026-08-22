package cc.hongz.ztools.core.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.util.ArrayUtil;

/**
 * <p>
 * 辅助实现 {@link Object#hashCode()} 方法.
 * </p>
 *
 * <p>
 * 此类可为任何类构建良好的 <code>hashCode</code> 方法，遵循
 * Joshua Bloch 的 <a href="http://www.oracle.com/technetwork/java/effectivejava-136174.html">Effective Java</a> 一书中的规则，编写
 * 良好的 <code>hashCode</code> 方法实际上相当困难，此类旨在简化这个过程.
 * </p>
 *
 * <p>
 * 采用的方法如下：追加数据字段时，当前总值乘以乘数，
 * 然后加上该数据类型对应的值，
 * 例如，当前 hashCode 为 17，乘数为 37，
 * 追加整数 45 将生成 hashcode 674，即 17 * 37 + 45.
 * </p>
 *
 * <p>
 * 对象的所有相关字段都应包含在 <code>hashCode</code> 方法中，派生字段可以
 * 排除。通常，<code>equals</code> 方法中使用的任何字段都必须在 <code>hashCode</code>
 * 方法中使用.
 * </p>
 *
 * <p>
 * 使用此类编写如下代码：
 * </p>
 *
 * <pre>
 * public class Person {
 *   String name;
 *   int age;
 *   boolean smoker;
 *   ...
 *
 *   public int hashCode() {
 *     // you pick a hard-coded, randomly chosen, non-zero, odd number
 *     // ideally different for each class
 *     return new HashCodeBuilder(17, 37).
 *       append(name).
 *       append(age).
 *       append(smoker).
 *       toHashCode();
 *   }
 * }
 * </pre>
 *
 * <p>
 * 如果需要，可以使用 {@link #appendSuper} 添加父类的 <code>hashCode()</code>.
 * </p>
 *
 * <p>
 * 另外，还有一个使用反射确定测试字段的方法，因为这些字段
 * 通常是 private 的，<code>reflectionHashCode</code> 方法使用 <code>AccessibleObject.setAccessible</code>
 * 改变字段的可见性，在 security manager 下会失败，除非正确设置权限，
 * 它也比显式测试慢.
 * </p>
 *
 * <p>
 * 此方法的典型调用方式如下：
 * </p>
 *
 * <pre>
 * public int hashCode() {
 *   return HashCodeBuilder.reflectionHashCode(this);
 * }
 * </pre>
 *
 * TODO 待整理
 * 来自于Apache-Commons-Lang3
 * @author looly，Apache-Commons
 * @since 4.2.2
 */
public class HashCodeBuilder implements Builder<Integer> {
    private static final long serialVersionUID = 1L;

    /**
     * 反射哈希码构建时使用的默认初始值.
     */
    private static final int DEFAULT_INITIAL_VALUE = 17;

    /**
     * 反射哈希码构建时使用的默认乘数.
     */
    private static final int DEFAULT_MULTIPLIER_VALUE = 37;

    /**
     * long 类型的高位位移量.
     */
    private static final int LONG_HALF_SHIFT = 32;

    /**
     * <p>
     * 反射方法使用的对象注册表，用于检测循环对象引用并避免无限循环.
     * </p>
     *
     * @since 2.3
     */
    private static final ThreadLocal<Set<IDKey>> REGISTRY = new ThreadLocal<>();

    /*
     * 注意：不能在 HashSet 中存储实际对象，因为那会使用正在计算的 hashCode()
     * （正在计算的 hashCode）
     *
     * 因此生成从原始对象到新对象的一对一映射.
     *
     * HashSet 使用 equals() 确定两个具有相同 hashcode 的元素是否真的
     * 相等，因此还需要确保替换对象只有在原始对象相同的情况下才相等.
     * （原始对象相同）
     *
     * 原始实现（2.4 及之前）使用 System.identityHashCode()
     * 方法，但这不能保证生成唯一的 id（例如 LANG-459）
     *
     * 现在使用 IDKey 辅助类（改编自 org.apache.axis.utils.IDKey）
     * 来消除重复 id 的歧义.
     */

    /**
     * <p>
     * 返回当前线程中反射方法正在遍历的对象注册表.
     * </p>
     *
     * @return 正在遍历的对象注册表 Set
     * @since 2.3
     */
    private static Set<IDKey> getRegistry() {
        return REGISTRY.get();
    }

    /**
     * <p>
     * 如果注册表包含给定对象则返回 <code>true</code>，用于反射方法避免
     * 无限循环.
     * </p>
     *
     * @param value
     *            要在注册表中查找的对象.
     * @return 如果注册表包含给定对象则返回 <code>true</code>
     * @since 2.3
     */
    private static boolean isRegistered(final Object value) {
        final Set<IDKey> registry = getRegistry();
        return registry != null && registry.contains(new IDKey(value));
    }

    /**
     * <p>
     * 追加给定 <code>Class</code> 对象所定义的字段和值.
     * </p>
     *
     * @param object
     *            要追加详细信息的对象
     * @param clazz
     *            要追加详细信息的类
     * @param builder
     *            要追加到的构建器
     * @param useTransients
     *            是否使用 transient 字段
     * @param excludeFields
     *            计算哈希码时要排除的 String 字段名集合
     */
    private static void reflectionAppend(final Object object, final Class<?> clazz, final HashCodeBuilder builder,
                final boolean useTransients,
            final String[] excludeFields) {
        if (isRegistered(object)) {
            return;
        }
        try {
            register(object);
            final Field[] fields = clazz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            for (final Field field : fields) {
                if (false == ArrayUtil.contains(excludeFields, field.getName())
                    && (field.getName().indexOf('$') == -1)
                    && (useTransients || !Modifier.isTransient(field.getModifiers()))
                    && (!Modifier.isStatic(field.getModifiers()))) {
                    try {
                        final Object fieldValue = field.get(object);
                        builder.append(fieldValue);
                    } catch (final IllegalAccessException e) {
                        // 此情况不会发生，否则会得到 Security 异常
                        // 抛出运行时异常以防不可能的情况发生
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        } finally {
            unregister(object);
        }
    }

    /**
     * <p>
     * 使用反射从 {@code object} 的字段构建有效的哈希码.
     * </p>
     *
     * <p>
     * 使用 <code>AccessibleObject.setAccessible</code> 访问私有字段，这意味着
     * 如果权限设置不正确，在 security manager 下会抛出安全异常，
     * 它也比显式测试慢.
     * </p>
     *
     * <p>
     * transient 成员不会被使用，因为它们可能是派生字段，不属于对象的值，
     * <code>Object</code>.
     * </p>
     *
     * <p>
     * 静态字段不会被测试，父类字段会被包含.
     * </p>
     *
     * <p>
     * 必须传入两个随机选择的非零奇数，理想情况下每个类应不同，
     * 然而这不是至关重要的，质数是首选，尤其是乘数.
     * </p>
     *
     * @param initialNonZeroOddNumber
     *            用作初始值的非零奇数，这将是返回的
     *            如果没有字段被包含到哈希码中则返回此值
     * @param multiplierNonZeroOddNumber
     *            用作乘数的非零奇数
     * @param object
     *            要为其创建 <code>hashCode</code> 的 Object
     * @return int 哈希码
     * @throws IllegalArgumentException
     *             如果 Object 为 <code>null</code>
     * @throws IllegalArgumentException
     *             如果数字是零或偶数
     */
    public static int reflectionHashCode(final int initialNonZeroOddNumber, final int multiplierNonZeroOddNumber,
                final Object object) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, null);
    }

    /**
     * <p>
     * 使用反射从 {@code object} 的字段构建有效的哈希码.
     * </p>
     *
     * <p>
     * 使用 <code>AccessibleObject.setAccessible</code> 访问私有字段，这意味着
     * 如果权限设置不正确，在 security manager 下会抛出安全异常，
     * 它也比显式测试慢.
     * </p>
     *
     * <p>
     * 如果 testTransients 参数为 <code>true</code>, transient 成员会被测试，否则它们
     * 被忽略，因为它们可能是派生字段，不属于 <code>Object</code> 的值.
     * </p>
     *
     * <p>
     * 静态字段不会被测试，父类字段会被包含.
     * </p>
     *
     * <p>
     * 必须传入两个随机选择的非零奇数，理想情况下每个类应不同，
     * 然而这不是至关重要的，质数是首选，尤其是乘数.
     * </p>
     *
     * @param initialNonZeroOddNumber
     *            用作初始值的非零奇数，这将是返回的
     *            如果没有字段被包含到哈希码中则返回此值
     * @param multiplierNonZeroOddNumber
     *            用作乘数的非零奇数
     * @param object
     *            要为其创建 <code>hashCode</code> 的 Object
     * @param testTransients
     *            是否包含 transient 字段
     * @return int 哈希码
     * @throws IllegalArgumentException
     *             如果 Object 为 <code>null</code>
     * @throws IllegalArgumentException
     *             如果数字是零或偶数
     */
    public static int reflectionHashCode(final int initialNonZeroOddNumber, final int multiplierNonZeroOddNumber,
                final Object object,
            final boolean testTransients) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, null);
    }

    /**
     * <p>
     * 使用反射从 {@code object} 的字段构建有效的哈希码.
     * </p>
     *
     * <p>
     * 使用 <code>AccessibleObject.setAccessible</code> 访问私有字段，这意味着
     * 如果权限设置不正确，在 security manager 下会抛出安全异常，
     * 它也比显式测试慢.
     * </p>
     *
     * <p>
     * 如果 testTransients 参数为 <code>true</code>, transient 成员会被测试，否则它们
     * 被忽略，因为它们可能是派生字段，不属于 <code>Object</code> 的值.
     * </p>
     *
     * <p>
     * 静态字段不会被包含，父类字段会包含到指定的
     * 父类（含），null 父类按 java.lang.Object 处理.
     * </p>
     *
     * <p>
     * 必须传入两个随机选择的非零奇数，理想情况下每个类应不同，
     * 然而这不是至关重要的，质数是首选，尤其是乘数.
     * </p>
     *
     * @param <T>
     *            所涉及对象的类型
     * @param initialNonZeroOddNumber
     *            用作初始值的非零奇数，这将是返回的
     *            如果没有字段被包含到哈希码中则返回此值
     * @param multiplierNonZeroOddNumber
     *            用作乘数的非零奇数
     * @param object
     *            要为其创建 <code>hashCode</code> 的 Object
     * @param testTransients
     *            是否包含 transient 字段
     * @param reflectUpToClass
     *            要反射到的父类（含），可为 <code>null</code>
     * @param excludeFields
     *            计算哈希码时要排除的字段名数组
     * @return int 哈希码
     * @throws IllegalArgumentException
     *             如果 Object 为 <code>null</code>
     * @throws IllegalArgumentException
     *             如果数字是零或偶数
     * @since 2.0
     */
    public static <T> int reflectionHashCode(final int initialNonZeroOddNumber, final int multiplierNonZeroOddNumber,
                final T object,
            final boolean testTransients, final Class<? super T> reflectUpToClass, final String... excludeFields) {

        if (object == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        }
        final HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        Class<?> clazz = object.getClass();
        reflectionAppend(object, clazz, builder, testTransients, excludeFields);
        while (clazz.getSuperclass() != null && clazz != reflectUpToClass) {
            clazz = clazz.getSuperclass();
            reflectionAppend(object, clazz, builder, testTransients, excludeFields);
        }
        return builder.toHashCode();
    }

    /**
     * <p>
     * 使用反射从 {@code object} 的字段构建有效的哈希码.
     * </p>
     *
     * <p>
     * 此构造器使用两个硬编码的选择作为构建哈希码所需的常量.
     * </p>
     *
     * <p>
     * 使用 <code>AccessibleObject.setAccessible</code> 访问私有字段，这意味着
     * 如果权限设置不正确，在 security manager 下会抛出安全异常，
     * 它也比显式测试慢.
     * </p>
     *
     * <P>
     * 如果 testTransients 参数为 <code>true</code>, transient 成员会被测试，否则它们
     * 被忽略，因为它们可能是派生字段，不属于 <code>Object</code> 的值.
     * </p>
     *
     * <p>
     * 静态字段不会被测试，父类字段会被包含，如果没有找到要包含的字段，
     * 在哈希码中，此方法的结果将是常量.
     * </p>
     *
     * @param object
     *            要为其创建 <code>hashCode</code> 的 Object
     * @param testTransients
     *            是否包含 transient 字段
     * @return int 哈希码
     * @throws IllegalArgumentException
     *             如果对象为 <code>null</code>
     */
    public static int reflectionHashCode(final Object object, final boolean testTransients) {
        return reflectionHashCode(DEFAULT_INITIAL_VALUE, DEFAULT_MULTIPLIER_VALUE, object,
                testTransients, null);
    }

    /**
     * <p>
     * 使用反射从 {@code object} 的字段构建有效的哈希码.
     * </p>
     *
     * <p>
     * 此构造器使用两个硬编码的选择作为构建哈希码所需的常量.
     * </p>
     *
     * <p>
     * 使用 <code>AccessibleObject.setAccessible</code> 访问私有字段，这意味着
     * 如果权限设置不正确，在 security manager 下会抛出安全异常，
     * 它也比显式测试慢.
     * </p>
     *
     * <p>
     * transient 成员不会被使用，因为它们可能是派生字段，不属于对象的值，
     * <code>Object</code>.
     * </p>
     *
     * <p>
     * 静态字段不会被测试，父类字段会被包含，如果没有找到要包含的字段，
     * 在哈希码中，此方法的结果将是常量.
     * </p>
     *
     * @param object
     *            要为其创建 <code>hashCode</code> 的 Object
     * @param excludeFields
     *            计算哈希码时要排除的 String 字段名集合
     * @return int 哈希码
     * @throws IllegalArgumentException
     *             如果对象为 <code>null</code>
     */
    public static int reflectionHashCode(final Object object, final Collection<String> excludeFields) {
        return reflectionHashCode(object, ArrayUtil.toArray(excludeFields, String.class));
    }

    // -------------------------------------------------------------------------

    /**
     * <p>
     * 使用反射从 {@code object} 的字段构建有效的哈希码.
     * </p>
     *
     * <p>
     * 此构造器使用两个硬编码的选择作为构建哈希码所需的常量.
     * </p>
     *
     * <p>
     * 使用 <code>AccessibleObject.setAccessible</code> 访问私有字段，这意味着
     * 如果权限设置不正确，在 security manager 下会抛出安全异常，
     * 它也比显式测试慢.
     * </p>
     *
     * <p>
     * transient 成员不会被使用，因为它们可能是派生字段，不属于对象的值，
     * <code>Object</code>.
     * </p>
     *
     * <p>
     * 静态字段不会被测试，父类字段会被包含，如果没有找到要包含的字段，
     * 在哈希码中，此方法的结果将是常量.
     * </p>
     *
     * @param object
     *            要为其创建 <code>hashCode</code> 的 Object
     * @param excludeFields
     *            计算哈希码时要排除的字段名数组
     * @return int 哈希码
     * @throws IllegalArgumentException
     *             如果对象为 <code>null</code>
     */
    public static int reflectionHashCode(final Object object, final String... excludeFields) {
        return reflectionHashCode(DEFAULT_INITIAL_VALUE, DEFAULT_MULTIPLIER_VALUE, object, false,
                null, excludeFields);
    }

    /**
     * <p>
     * 注册给定对象，用于反射方法避免 无限循环.
     * </p>
     *
     * @param value
     *            要注册的对象.
     */
    static void register(final Object value) {
        synchronized (HashCodeBuilder.class) {
            if (getRegistry() == null) {
                REGISTRY.set(new HashSet<IDKey>());
            }
        }
        getRegistry().add(new IDKey(value));
    }

    /**
     * <p>
     * 注销给定对象.
     * </p>
     *
     * <p>
     * 用于反射方法避免 无限循环.
     *
     * @param value
     *            要注销的对象.
     * @since 2.3
     */
    static void unregister(final Object value) {
        Set<IDKey> registry = getRegistry();
        if (registry != null) {
            registry.remove(new IDKey(value));
            synchronized (HashCodeBuilder.class) {
                //read again
                registry = getRegistry();
                if (registry != null && registry.isEmpty()) {
                    REGISTRY.remove();
                }
            }
        }
    }

    /**
     * 构建 hashCode 时使用的常量.
     */
    private final int iConstant;

    /**
     * hashCode 的运行总数.
     */
    private int iTotal;

    /**
     * <p>
     * 使用两个硬编码的选择作为构建 <code>hashCode</code> 所需的常量.
     * </p>
     */
    public HashCodeBuilder() {
        iConstant = DEFAULT_MULTIPLIER_VALUE;
        iTotal = DEFAULT_INITIAL_VALUE;
    }

    /**
     * <p>
     * 必须传入两个随机选择的奇数，理想情况下每个类应不同，
     * 然而这不是至关重要的.
     * </p>
     *
     * <p>
     * 质数是首选，尤其是乘数.
     * </p>
     *
     * @param initialOddNumber
     *            用作初始值的奇数
     * @param multiplierOddNumber
     *            用作乘数的奇数
     * @throws IllegalArgumentException
     *             如果数字是偶数
     */
    public HashCodeBuilder(final int initialOddNumber, final int multiplierOddNumber) {
        Assert.isTrue(initialOddNumber % 2 != 0, "HashCodeBuilder requires an odd initial value");
        Assert.isTrue(multiplierOddNumber % 2 != 0, "HashCodeBuilder requires an odd multiplier");
        iConstant = multiplierOddNumber;
        iTotal = initialOddNumber;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>boolean</code>.
     * </p>
     * <p>
     * 为 true 时在 <code>hashCode</code> 中添加 <code>1</code>，为 false 时添加 <code>0</code>.
     * </p>
     * <p>
     * 这与标准 <code>java.lang.Boolean.hashCode</code> 处理相反，后者
     * 对表示 <code>true</code> 的 <code>java.lang.Boolean</code> 实例计算 <code>1231</code>
     * 对表示 <code>false</code> 的实例计算 <code>1237</code>
     * 
     * </p>
     * <p>
     * 这符合 <i>Effective Java</i> 设计.
     * </p>
     *
     * @param value
     *            要添加到哈希码的 boolean<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final boolean value) {
        iTotal = iTotal * iConstant + (value ? 0 : 1);
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>boolean</code> array.
     * </p>
     *
     * @param array
     *            要添加到哈希码的数组<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final boolean[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final boolean element : array) {
                append(element);
            }
        }
        return this;
    }

    // -------------------------------------------------------------------------

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>byte</code>.
     * </p>
     *
     * @param value
     *            要添加到哈希码的 byte<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final byte value) {
        iTotal = iTotal * iConstant + value;
        return this;
    }

    // -------------------------------------------------------------------------

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>byte</code> array.
     * </p>
     *
     * @param array
     *            要添加到哈希码的数组<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final byte[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final byte element : array) {
                append(element);
            }
        }
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>char</code>.
     * </p>
     *
     * @param value
     *            要添加到哈希码的 char<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final char value) {
        iTotal = iTotal * iConstant + value;
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>char</code> array.
     * </p>
     *
     * @param array
     *            要添加到哈希码的数组<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final char[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final char element : array) {
                append(element);
            }
        }
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>double</code>.
     * </p>
     *
     * @param value
     *            要添加到哈希码的 double<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final double value) {
        return append(Double.doubleToLongBits(value));
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>double</code> array.
     * </p>
     *
     * @param array
     *            要添加到哈希码的数组<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final double[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final double element : array) {
                append(element);
            }
        }
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>float</code>.
     * </p>
     *
     * @param value
     *            要添加到哈希码的 float<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final float value) {
        iTotal = iTotal * iConstant + Float.floatToIntBits(value);
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>float</code> array.
     * </p>
     *
     * @param array
     *            要添加到哈希码的数组<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final float[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final float element : array) {
                append(element);
            }
        }
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for an <code>int</code>.
     * </p>
     *
     * @param value
     *            要添加到哈希码的 int<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final int value) {
        iTotal = iTotal * iConstant + value;
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for an <code>int</code> array.
     * </p>
     *
     * @param array
     *            要添加到哈希码的数组<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final int[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final int element : array) {
                append(element);
            }
        }
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>long</code>.
     * </p>
     *
     * @param value
     *            要添加到哈希码的 long<code>hashCode</code>
     * @return this
     */
    // 注意：此方法使用 >> 而不是 >>>，如 Effective Java 和
    //       Long.hashCode 那样，理想情况下应该切换到 >>>，
    //       但有向后兼容问题，所以
    //       暂时只能等待，参考 LANG-342.
    public HashCodeBuilder append(final long value) {
        iTotal = iTotal * iConstant + ((int) (value ^ (value >> LONG_HALF_SHIFT)));
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>long</code> array.
     * </p>
     *
     * @param array
     *            要添加到哈希码的数组<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final long[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final long element : array) {
                append(element);
            }
        }
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for an <code>Object</code>.
     * </p>
     *
     * @param object
     *            要添加到 <code>hashCode</code> 的 Object
     * @return this
     */
    public HashCodeBuilder append(final Object object) {
        if (object == null) {
            iTotal = iTotal * iConstant;

        } else {
            if (object.getClass().isArray()) {
                // 'Switch' on type of array, to dispatch to the correct handler
                // 此处理多维数组
                if (object instanceof long[]) {
                    append((long[]) object);
                } else if (object instanceof int[]) {
                    append((int[]) object);
                } else if (object instanceof short[]) {
                    append((short[]) object);
                } else if (object instanceof char[]) {
                    append((char[]) object);
                } else if (object instanceof byte[]) {
                    append((byte[]) object);
                } else if (object instanceof double[]) {
                    append((double[]) object);
                } else if (object instanceof float[]) {
                    append((float[]) object);
                } else if (object instanceof boolean[]) {
                    append((boolean[]) object);
                } else {
                    // Not an array of primitives
                    append((Object[]) object);
                }
            } else {
                iTotal = iTotal * iConstant + object.hashCode();
            }
        }
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for an <code>Object</code> array.
     * </p>
     *
     * @param array
     *            要添加到哈希码的数组<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final Object[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final Object element : array) {
                append(element);
            }
        }
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>short</code>.
     * </p>
     *
     * @param value
     *            要添加到哈希码的 short<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final short value) {
        iTotal = iTotal * iConstant + value;
        return this;
    }

    /**
     * <p>
     * 追加 <code>hashCode</code> for a <code>short</code> array.
     * </p>
     *
     * @param array
     *            要添加到哈希码的数组<code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(final short[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final short element : array) {
                append(element);
            }
        }
        return this;
    }

    /**
     * <p>
     * 将 super.hashCode() 的结果添加到本构建器.
     * </p>
     *
     * @param superHashCode
     *            调用 <code>super.hashCode()</code>
     * @return this HashCodeBuilder，用于链式调用
     * @since 2.0
     */
    public HashCodeBuilder appendSuper(final int superHashCode) {
        iTotal = iTotal * iConstant + superHashCode;
        return this;
    }

    /**
     * <p>
     * 返回计算出的 <code>hashCode</code>.
     * </p>
     *
     * @return 基于已追加字段的 <code>hashCode</code>
     */
    public int toHashCode() {
        return iTotal;
    }

    /**
     * 返回计算出的 <code>hashCode</code>.
     *
     * @return 基于已追加字段的 <code>hashCode</code>
     *
     * @since 3.0
     */
    @Override
    public Integer build() {
        return toHashCode();
    }

    /**
     * <p>
     * 返回 toHashCode() 计算出的 <code>hashCode</code>，因为
     * 误调用 toHashCode() 可能引入 bug，而且 hashCode 本身的值并不重要
     * （HashCodeBuilder 本身）.
     *
     * @return 基于已追加字段的 <code>hashCode</code>
     * @since 2.5
     */
    @SuppressWarnings("checkstyle:EqualsHashCode")
    @Override
    public int hashCode() {
        return toHashCode();
    }

}
