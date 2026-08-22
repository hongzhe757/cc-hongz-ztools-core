package cc.hongz.ztools.core.builder;

import cc.hongz.ztools.core.lang.Pair;
import cc.hongz.ztools.core.util.ArrayUtil;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>{@link Object#equals(Object)} 方法的构建器.
 *
 * <p>两个对象equals必须保证hashCode值相等，hashCode值相等不能保证一定equals.
 *
 * <p>使用方法如下：</p>
 * <pre>
 * public boolean equals(Object obj) {
 *   if (obj == null) { return false; }
 *   if (obj == this) { return true; }
 *   if (obj.getClass() != getClass()) {
 *     return false;
 *   }
 *   MyClass rhs = (MyClass) obj;
 *   return new EqualsBuilder()
 *                 .appendSuper(super.equals(obj))
 *                 .append(field1, rhs.field1)
 *                 .append(field2, rhs.field2)
 *                 .append(field3, rhs.field3)
 *                 .isEquals();
 *  }
 * </pre>
 *
 * <p> 我们也可以通过反射判断所有字段是否equals：</p>
 * <pre>
 * public boolean equals(Object obj) {
 *   return EqualsBuilder.reflectionEquals(this, obj);
 * }
 * </pre>
 * <p>
 * 来自Apache Commons Lang改造.
 */
public class EqualsBuilder implements Builder<Boolean> {
    private static final long serialVersionUID = 1L;

    /**
     * 反射方法使用的对象注册表，用于检测循环对象引用并避免无限循环.
     */
    private static final ThreadLocal<Set<Pair<IDKey, IDKey>>> REGISTRY = new ThreadLocal<>();

    /**
     * 返回当前线程中反射方法正在遍历的对象对注册表.
     *
     * @return Set 正在遍历的对象注册表
     * @since 3.0
     */
    static Set<Pair<IDKey, IDKey>> getRegistry() {
        return REGISTRY.get();
    }

    /**
     * 将值对转换为注册对.
     *
     * @param lhs {@code this} 对象
     * @param rhs 另一个对象
     * @return 注册对
     */
    static Pair<IDKey, IDKey> getRegisterPair(final Object lhs, final Object rhs) {
        final IDKey left = new IDKey(lhs);
        final IDKey right = new IDKey(rhs);
        return new Pair<>(left, right);
    }

    /**
     * 如果注册表包含给定的对象对则返回 {@code true}，用于反射方法避免无限循环.
     * 对象可能被交换，因此需要检查对象对是否以给定顺序或交换顺序注册.
     *
     * @param lhs 在注册表中查找的 {@code this} 对象
     * @param rhs 在注册表中查找的另一个对象
     * @return 如果注册表包含给定对象则返回 {@code true}
     * @since 3.0
     */
    static boolean isRegistered(final Object lhs, final Object rhs) {
        final Set<Pair<IDKey, IDKey>> registry = getRegistry();
        final Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
        final Pair<IDKey, IDKey> swappedPair = new Pair<>(pair.getKey(), pair.getValue());

        return registry != null
                && (registry.contains(pair) || registry.contains(swappedPair));
    }

    /**
     * 注册给定的对象对，用于反射方法避免无限循环.
     *
     * @param lhs 要注册的 {@code this} 对象
     * @param rhs 要注册的另一个对象
     */
    static void register(final Object lhs, final Object rhs) {
        synchronized (EqualsBuilder.class) {
            if (getRegistry() == null) {
                REGISTRY.set(new HashSet<>());
            }
        }

        final Set<Pair<IDKey, IDKey>> registry = getRegistry();
        final Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
        registry.add(pair);
    }

    /**
     * 注销给定的对象对，用于反射方法避免无限循环.
     *
     * @param lhs 要注销的 {@code this} 对象
     * @param rhs 要注销的另一个对象
     * @since 3.0
     */
    static void unregister(final Object lhs, final Object rhs) {
        Set<Pair<IDKey, IDKey>> registry = getRegistry();
        if (registry != null) {
            final Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
            registry.remove(pair);
            synchronized (EqualsBuilder.class) {
                // 重新读取
                registry = getRegistry();
                if (registry != null && registry.isEmpty()) {
                    REGISTRY.remove();
                }
            }
        }
    }

    /**
     * 是否equals，此值随着构建会变更，默认true.
     */
    private boolean isEquals = true;

    /**
     * 构造，初始状态值为true.
     */
    public EqualsBuilder() {
        // 暂不处理
    }

    //-------------------------------------------------------------------------

    /**
     * <p>反射检查两个对象是否equals，此方法检查对象及其父对象的属性（包括私有属性）是否equals.
     *
     * @param lhs           此对象
     * @param rhs           另一个对象
     * @param excludeFields 排除的字段集合，如果有不参与计算equals的字段加入此集合即可
     * @return 两个对象是否equals，是返回{@code true}
     */
    public static boolean reflectionEquals(final Object lhs, final Object rhs, final Collection<String> excludeFields) {
        return reflectionEquals(lhs, rhs, ArrayUtil.toArray(excludeFields, String.class));
    }

    /**
     * <p>反射检查两个对象是否equals，此方法检查对象及其父对象的属性（包括私有属性）是否equals.
     *
     * @param lhs           此对象
     * @param rhs           另一个对象
     * @param excludeFields 排除的字段集合，如果有不参与计算equals的字段加入此集合即可
     * @return 两个对象是否equals，是返回{@code true}
     */
    public static boolean reflectionEquals(final Object lhs, final Object rhs, final String... excludeFields) {
        return reflectionEquals(lhs, rhs, false, null, excludeFields);
    }

    /**
     * <p>此方法使用反射判断两个 {@code Object} 是否相等.
     *
     * <p>使用 {@code AccessibleObject.setAccessible} 访问私有字段，如果运行在 security manager 下且权限设置不正确会抛出安全异常，
     * 效率也不如显式测试，非基本类型字段使用 {@code equals()} 比较.
     *
     * <p>如果 testTransients 参数为 {@code true}，transient 成员会被测试，否则被忽略，
     * 因为它们可能是派生字段，不属于 {@code Object} 的值.
     *
     * <p>静态字段不会被测试，父类字段会被包含.
     *
     * @param lhs            {@code this} 对象
     * @param rhs            另一个对象
     * @param testTransients 是否包含 transient 字段
     * @return 两个对象是否 equals 则返回 {@code true}
     */
    public static boolean reflectionEquals(final Object lhs, final Object rhs, final boolean testTransients) {
        return reflectionEquals(lhs, rhs, testTransients, null);
    }

    /**
     * <p>此方法使用反射判断两个 {@code Object} 是否相等.
     *
     * <p>使用 {@code AccessibleObject.setAccessible} 访问私有字段，如果运行在 security manager 下且权限设置不正确会抛出安全异常，
     * 效率也不如显式测试，非基本类型字段使用 {@code equals()} 比较.
     *
     * <p>如果 testTransients 参数为 {@code true}，transient 成员会被测试，否则被忽略，
     * 因为它们可能是派生字段，不属于 {@code Object} 的值.
     *
     * <p>静态字段不会被包含，父类字段会追加到指定的父类（含），null 父类按 java.lang.Object 处理.
     *
     * @param lhs              {@code this} 对象
     * @param rhs              另一个对象
     * @param testTransients   是否包含 transient 字段
     * @param reflectUpToClass 反射到哪个父类（含），可为 {@code null}
     * @param excludeFields    从测试中排除的字段名数组
     * @return 两个对象是否 equals 则返回 {@code true}
     * @since 2.0
     */
    public static boolean reflectionEquals(final Object lhs, final Object rhs, final boolean testTransients,
                                           final Class<?> reflectUpToClass, final String... excludeFields) {
        if (lhs == rhs) {
            return true;
        }
        if (lhs == null || rhs == null) {
            return false;
        }
        // 查找叶子类，因为 transient 可能存在于叶子类或叶子与根之间的类中
        // 如果不测试 transient 或子类没有 ivars，则子类可以 equals 父类
        final Class<?> lhsClass = lhs.getClass();
        final Class<?> rhsClass = rhs.getClass();
        Class<?> testClass;
        if (lhsClass.isInstance(rhs)) {
            testClass = lhsClass;
            if (!rhsClass.isInstance(lhs)) {
                // rhsClass 是 lhsClass 的子类
                testClass = rhsClass;
            }
        } else if (rhsClass.isInstance(lhs)) {
            testClass = rhsClass;
            if (!lhsClass.isInstance(rhs)) {
                // lhsClass 是 rhsClass 的子类
                testClass = lhsClass;
            }
        } else {
            // 两个类不相关
            return false;
        }
        final EqualsBuilder equalsBuilder = new EqualsBuilder();
        try {
            if (testClass.isArray()) {
                equalsBuilder.append(lhs, rhs);
            } else {
                reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
                    testClass = testClass.getSuperclass();
                    reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                }
            }
        } catch (final IllegalArgumentException e) {
            // 此情况下尝试测试子类 vs 父类，子类有 ivars 或 ivars 是 transient 且正在测试 transient
            // 如果子类有 ivars 正在测试，会得到异常，此时对象不相等
            return false;
        }
        return equalsBuilder.isEquals();
    }

    /**
     * <p>追加给定类的对象所定义的字段和值.
     *
     * @param lhs           左侧对象
     * @param rhs           右侧对象
     * @param clazz         要追加详细信息的类
     * @param builder       要追加到的构建器
     * @param useTransients 是否测试 transient 字段
     * @param excludeFields 从测试中排除的字段名数组
     */
    private static void reflectionAppend(
            final Object lhs,
            final Object rhs,
            final Class<?> clazz,
            final EqualsBuilder builder,
            final boolean useTransients,
            final String[] excludeFields) {

        if (isRegistered(lhs, rhs)) {
            return;
        }

        try {
            register(lhs, rhs);
            final Field[] fields = clazz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            for (int i = 0; i < fields.length && builder.isEquals; i++) {
                final Field f = fields[i];
                if (false == ArrayUtil.contains(excludeFields, f.getName())
                        && (f.getName().indexOf('$') == -1)
                        && (useTransients || !Modifier.isTransient(f.getModifiers()))
                        && (!Modifier.isStatic(f.getModifiers()))) {
                    try {
                        builder.append(f.get(lhs), f.get(rhs));
                    } catch (final IllegalAccessException e) {
                        // 此情况不会发生，否则会得到 Security 异常
                        // 抛出运行时异常以防不可能的情况发生
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        } finally {
            unregister(lhs, rhs);
        }
    }

    //-------------------------------------------------------------------------

    /**
     * <p>将 {@code super.equals()} 的结果添加到本构建器.
     *
     * @param superEquals 调用 {@code super.equals()} 的结果
     * @return EqualsBuilder - 用于链式调用
     * @since 2.0
     */
    public EqualsBuilder appendSuper(final boolean superEquals) {
        if (isEquals == false) {
            return this;
        }
        isEquals = superEquals;
        return this;
    }

    //-------------------------------------------------------------------------

    /**
     * <p>使用 {@code equals} 方法测试两个 {@code Object} 是否相等.
     *
     * @param lhs 左侧对象
     * @param rhs 右侧对象
     * @return EqualsBuilder - 用于链式调用
     */
    public EqualsBuilder append(final Object lhs, final Object rhs) {
        if (isEquals == false) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null || rhs == null) {
            return setEquals(false);
        }
        if (ArrayUtil.isArray(lhs)) {
            // 判断数组的equals
            return setEquals(ArrayUtil.equals(lhs, rhs));
        }

        // 简单情况，非数组，直接测试元素
        return setEquals(lhs.equals(rhs));
    }

    /**
     * <p>测试两个 {@code long} 是否相等.
     *
     * @param lhs 左侧 {@code long}
     * @param rhs 右侧 {@code long}
     * @return EqualsBuilder - 用于链式调用
     */
    public EqualsBuilder append(final long lhs, final long rhs) {
        if (isEquals == false) {
            return this;
        }
        isEquals = (lhs == rhs);
        return this;
    }

    /**
     * <p>测试两个 {@code int} 是否相等.
     *
     * @param lhs 左侧 {@code int}
     * @param rhs 右侧 {@code int}
     * @return EqualsBuilder - 用于链式调用
     */
    public EqualsBuilder append(final int lhs, final int rhs) {
        if (isEquals == false) {
            return this;
        }
        isEquals = (lhs == rhs);
        return this;
    }

    /**
     * <p>测试两个 {@code short} 是否相等.
     *
     * @param lhs 左侧 {@code short}
     * @param rhs 右侧 {@code short}
     * @return EqualsBuilder - 用于链式调用
     */
    public EqualsBuilder append(final short lhs, final short rhs) {
        if (isEquals == false) {
            return this;
        }
        isEquals = (lhs == rhs);
        return this;
    }

    /**
     * <p>测试两个 {@code char} 是否相等.
     *
     * @param lhs 左侧 {@code char}
     * @param rhs 右侧 {@code char}
     * @return EqualsBuilder - 用于链式调用
     */
    public EqualsBuilder append(final char lhs, final char rhs) {
        if (isEquals == false) {
            return this;
        }
        isEquals = (lhs == rhs);
        return this;
    }

    /**
     * <p>测试两个 {@code byte} 是否相等.
     *
     * @param lhs 左侧 {@code byte}
     * @param rhs 右侧 {@code byte}
     * @return EqualsBuilder - 用于链式调用
     */
    public EqualsBuilder append(final byte lhs, final byte rhs) {
        if (isEquals == false) {
            return this;
        }
        isEquals = (lhs == rhs);
        return this;
    }

    /**
     * <p>测试两个 {@code double} 是否相等，通过测试 {@code doubleToLong} 返回的位模式是否相等.
     *
     * <p>此方法处理 NaN、Infinity 和 {@code -0.0}.
     *
     * <p>与 {@code HashCodeBuilder} 生成的 hash code 兼容.
     *
     * @param lhs 左侧 {@code double}
     * @param rhs 右侧 {@code double}
     * @return EqualsBuilder - 用于链式调用
     */
    public EqualsBuilder append(final double lhs, final double rhs) {
        if (isEquals == false) {
            return this;
        }
        return append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
    }

    /**
     * <p>测试两个 {@code float} 是否相等，通过测试 {@code floatToIntBits} 返回的位模式是否相等.
     *
     * <p>此方法处理 NaN、Infinity 和 {@code -0.0}.
     *
     * <p>与 {@code HashCodeBuilder} 生成的 hash code 兼容.
     *
     * @param lhs 左侧 {@code float}
     * @param rhs 右侧 {@code float}
     * @return EqualsBuilder - 用于链式调用
     */
    public EqualsBuilder append(final float lhs, final float rhs) {
        if (isEquals == false) {
            return this;
        }
        return append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
    }

    /**
     * <p>测试两个 {@code boolean} 是否相等.
     *
     * @param lhs 左侧 {@code boolean}
     * @param rhs 右侧 {@code boolean}
     * @return EqualsBuilder - 用于链式调用
     */
    public EqualsBuilder append(final boolean lhs, final boolean rhs) {
        if (isEquals == false) {
            return this;
        }
        isEquals = (lhs == rhs);
        return this;
    }

    /**
     * <p>如果已检查的字段全部相等则返回 {@code true}.
     *
     * @return boolean
     */
    public boolean isEquals() {
        return this.isEquals;
    }

    /**
     * <p>如果已检查的字段全部相等则返回 {@code true}.
     *
     * @return 所有已检查字段都相等返回 {@code true}，否则 {@code false}
     * @since 3.0
     */
    @Override
    public Boolean build() {
        return isEquals();
    }

    /**
     * 设置 {@code isEquals} 值.
     *
     * @param newIsEquals 要设置的值
     * @return this
     */
    protected EqualsBuilder setEquals(final boolean newIsEquals) {
        this.isEquals = newIsEquals;
        return this;
    }

    /**
     * 重置 EqualsBuilder，以便再次使用同一个对象.
     *
     * @since 2.5
     */
    public void reset() {
        this.isEquals = true;
    }
}
