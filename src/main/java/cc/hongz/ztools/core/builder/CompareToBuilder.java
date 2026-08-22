package cc.hongz.ztools.core.builder;

import cc.hongz.ztools.core.util.ArrayUtil;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Comparator;

/**
 * 用于构建 {@link java.lang.Comparable#compareTo(Object)} 方法的辅助工具
 *
 * <p>
 * 在Bean对象中，所有相关字段都参与比对，继承的字段不参与。使用方法如下：
 *
 * <pre>
 * public class MyClass {
 *   String field1;
 *   int field2;
 *   boolean field3;
 *
 *   ...
 *
 *   public int compareTo(Object o) {
 *     MyClass myClass = (MyClass) o;
 *     return new CompareToBuilder()
 *       .appendSuper(super.compareTo(o)
 *       .append(this.field1, myClass.field1)
 *       .append(this.field2, myClass.field2)
 *       .append(this.field3, myClass.field3)
 *       .toComparison();
 *   }
 * }
 * </pre>
 *
 * 字段值按照顺序比较，如果某个字段返回非0结果，比较终止，使用{@code toComparison()}返回结果，后续比较忽略。
 *
 * <p>
 * 也可以使用{@link #reflectionCompare(Object, Object) reflectionCompare} 方法通过反射比较字段，使用方法如下：
 *
 * <pre>
 * public int compareTo(Object o) {
 *   return CompareToBuilder.reflectionCompare(this, o);
 * }
 * </pre>
 *
 *TODO 待整理
 * 来自于Apache-Commons-Lang3
 * @author looly，Apache-Commons
 * @since 4.2.2
 */
public class CompareToBuilder implements Builder<Integer> {
    private static final long serialVersionUID = 1L;

    /** 当前比较状态. */
    private int comparison;

    /**
     * 构造，构造后调用append方法增加比较项，然后调用{@link #toComparison()}获取结果.
     */
    public CompareToBuilder() {
        comparison = 0;
    }

    //-----------------------------------------------------------------------
    /**
     * 通过反射比较两个Bean对象，对象字段可以为private。比较规则如下：
     *
     * <ul>
     * <li>static字段不比较</li>
     * <li>Transient字段不参与比较</li>
     * <li>父类字段参与比较</li>
     * </ul>
     *
     *<p>
     *如果被比较的两个对象都为<code>null</code>，被认为相同。
     *
     * @param lhs  第一个对象
     * @param rhs  第二个对象
     * @return 负整数、零或正整数，表示 <code>lhs</code>
     *  表示 <code>lhs</code> 小于、等于或大于 <code>rhs</code>
     * @throws NullPointerException  if either (but not both) parameters are
     *  <code>null</code>
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  与 <code>lhs</code> 赋值不兼容
     */
    public static int reflectionCompare(final Object lhs, final Object rhs) {
        return reflectionCompare(lhs, rhs, false, null);
    }

    /**
     * <p>通过反射比较两个 Object.
     *
     * <p>字段可以为 private，因此使用 <code>AccessibleObject.setAccessible</code>
     * 绕过正常的访问控制检查，在 security manager 下会失败，除非正确设置权限.
     * 安全权限未正确设置时会失败.
     *
     * <ul>
     * <li>static 字段不比较</li>
     * <li>If <code>compareTransients</code> is <code>true</code>,
     *     比较 transient 成员，否则忽略它们，因为它们
     *     可能是派生字段.</li>
     * <li>父类字段会参与比较</li>
     * </ul>
     *
     * <p>If both <code>lhs</code> and <code>rhs</code> are <code>null</code>,
     * 它们被认为相等.
     *
     * @param lhs 左侧对象
     * @param rhs 右侧对象
     * @param compareTransients 是否比较 transient 字段
     * @return 负整数、零或正整数，表示 <code>lhs</code>
     *  表示 <code>lhs</code> 小于、等于或大于 <code>rhs</code>
     * @throws NullPointerException  if either <code>lhs</code> or <code>rhs</code>
     *  （但不是两者）为 <code>null</code>
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  与 <code>lhs</code> 赋值不兼容
     */
    public static int reflectionCompare(final Object lhs, final Object rhs, final boolean compareTransients) {
        return reflectionCompare(lhs, rhs, compareTransients, null);
    }

    /**
     * <p>通过反射比较两个 Object.
     *
     * <p>字段可以为 private，因此使用 <code>AccessibleObject.setAccessible</code>
     * 绕过正常的访问控制检查，在 security manager 下会失败，除非正确设置权限.
     * 安全权限未正确设置时会失败.
     *
     * <ul>
     * <li>static 字段不比较</li>
     * <li>If <code>compareTransients</code> is <code>true</code>,
     *     比较 transient 成员，否则忽略它们，因为它们
     *     可能是派生字段.</li>
     * <li>父类字段会参与比较</li>
     * </ul>
     *
     * <p>If both <code>lhs</code> and <code>rhs</code> are <code>null</code>,
     * 它们被认为相等.
     *
     * @param lhs 左侧对象
     * @param rhs 右侧对象
     * @param excludeFields 要排除的 String 字段集合
     * @return 负整数、零或正整数，表示 <code>lhs</code>
     *  表示 <code>lhs</code> 小于、等于或大于 <code>rhs</code>
     * @throws NullPointerException  if either <code>lhs</code> or <code>rhs</code>
     *  （但不是两者）为 <code>null</code>
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  与 <code>lhs</code> 赋值不兼容
     * @since 2.2
     */
    public static int reflectionCompare(final Object lhs, final Object rhs, final Collection<String> excludeFields) {
        return reflectionCompare(lhs, rhs, ArrayUtil.toArray(excludeFields, String.class));
    }

    /**
     * <p>通过反射比较两个 Object.
     *
     * <p>字段可以为 private，因此使用 <code>AccessibleObject.setAccessible</code>
     * 绕过正常的访问控制检查，在 security manager 下会失败，除非正确设置权限.
     * 安全权限未正确设置时会失败.
     *
     * <ul>
     * <li>static 字段不比较</li>
     * <li>If <code>compareTransients</code> is <code>true</code>,
     *     比较 transient 成员，否则忽略它们，因为它们
     *     可能是派生字段.</li>
     * <li>父类字段会参与比较</li>
     * </ul>
     *
     * <p>If both <code>lhs</code> and <code>rhs</code> are <code>null</code>,
     * 它们被认为相等.
     *
     * @param lhs 左侧对象
     * @param rhs 右侧对象
     * @param excludeFields 要排除的字段数组
     * @return 负整数、零或正整数，表示 <code>lhs</code>
     *  表示 <code>lhs</code> 小于、等于或大于 <code>rhs</code>
     * @throws NullPointerException  if either <code>lhs</code> or <code>rhs</code>
     *  （但不是两者）为 <code>null</code>
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  与 <code>lhs</code> 赋值不兼容
     * @since 2.2
     */
    public static int reflectionCompare(final Object lhs, final Object rhs, final String... excludeFields) {
        return reflectionCompare(lhs, rhs, false, null, excludeFields);
    }

    /**
     * <p>通过反射比较两个 Object.
     *
     * <p>字段可以为 private，因此使用 <code>AccessibleObject.setAccessible</code>
     * 绕过正常的访问控制检查，在 security manager 下会失败，除非正确设置权限.
     * 安全权限未正确设置时会失败.
     *
     * <ul>
     * <li>static 字段不比较</li>
     * <li>If the <code>compareTransients</code> is <code>true</code>,
     *     比较 transient 成员，否则忽略它们，因为它们
     *     可能是派生字段.</li>
     * <li>Compares superclass fields up to and including <code>reflectUpToClass</code>.
     *     如果 <code>reflectUpToClass</code> 为 <code>null</code> 则比较所有父类字段.</li>
     * </ul>
     *
     * <p>If both <code>lhs</code> and <code>rhs</code> are <code>null</code>,
     * 它们被认为相等.
     *
     * @param lhs 左侧对象
     * @param rhs 右侧对象
     * @param compareTransients 是否比较 transient 字段
     * @param reflectUpToClass 最后一个比较字段的父类
     * @param excludeFields 要排除的字段
     * @return 负整数、零或正整数，表示 <code>lhs</code>
     *  表示 <code>lhs</code> 小于、等于或大于 <code>rhs</code>
     * @throws NullPointerException  if either <code>lhs</code> or <code>rhs</code>
     *  （但不是两者）为 <code>null</code>
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  与 <code>lhs</code> 赋值不兼容
     * @since 2.2 (2.0 as <code>reflectionCompare(Object, Object, boolean, Class)</code>)
     */
    public static int reflectionCompare(
        final Object lhs,
        final Object rhs,
        final boolean compareTransients,
        final Class<?> reflectUpToClass,
        final String... excludeFields) {

        if (lhs == rhs) {
            return 0;
        }
        if (lhs == null || rhs == null) {
            throw new NullPointerException();
        }
        Class<?> lhsClazz = lhs.getClass();
        if (!lhsClazz.isInstance(rhs)) {
            throw new ClassCastException();
        }
        final CompareToBuilder compareToBuilder = new CompareToBuilder();
        reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
        while (lhsClazz.getSuperclass() != null && lhsClazz != reflectUpToClass) {
            lhsClazz = lhsClazz.getSuperclass();
            reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
        }
        return compareToBuilder.toComparison();
    }

    /**
     * <p>向 builder 追加 <code>lhs</code> 与 <code>rhs</code> 的比较
     * 使用 <code>clazz</code> 中定义的字段.
     *
     * @param lhs 左侧对象
     * @param rhs 右侧对象
     * @param clazz 定义要比较字段的 <code>Class</code>
     * @param builder 要追加到的 <code>CompareToBuilder</code>
     * @param useTransients 是否比较 transient 字段
     * @param excludeFields 要排除的字段
     */
    private static void reflectionAppend(
        final Object lhs,
        final Object rhs,
        final Class<?> clazz,
        final CompareToBuilder builder,
        final boolean useTransients,
        final String[] excludeFields) {

        final Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (int i = 0; i < fields.length && builder.comparison == 0; i++) {
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
    }

    //-----------------------------------------------------------------------
    /**
     * <p>向 builder 追加父类的 <code>compareTo(Object)</code>
     * 结果.
     *
     * @param superCompareTo 调用 <code>super.compareTo(Object)</code> 的结果
     * @return this - 用于链式追加调用
     * @since 2.0
     */
    public CompareToBuilder appendSuper(final int superCompareTo) {
        if (comparison != 0) {
            return this;
        }
        comparison = superCompareTo;
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>向 builder 追加比较
     * 两个 Object.
     *
     * <ol>
     * <li>检查 <code>lhs == rhs</code></li>
     * <li>检查 <code>lhs</code> 或 <code>rhs</code> 是否为 <code>null</code>，
     *     <code>null</code> 对象小于非 <code>null</code> 对象</li>
     * <li>检查对象内容</li>
     * </ol>
     *
     * <p><code>lhs</code> 必须是数组或实现 {@link Comparable}.
     *
     * @param lhs 左侧对象
     * @param rhs 右侧对象
     * @return this - 用于链式追加调用
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  与 <code>lhs</code> 赋值不兼容
     */
    public CompareToBuilder append(final Object lhs, final Object rhs) {
        return append(lhs, rhs, null);
    }

    /**
     * <p>向 builder 追加比较
     * 两个 Object.
     *
     * <ol>
     * <li>检查 <code>lhs == rhs</code></li>
     * <li>检查 <code>lhs</code> 或 <code>rhs</code> 是否为 <code>null</code>，
     *     <code>null</code> 对象小于非 <code>null</code> 对象</li>
     * <li>检查对象内容</li>
     * </ol>
     *
     * <p>如果 <code>lhs</code> 是数组，使用数组比较方法.
     * 否则使用 <code>comparator</code> 比较对象.
     * 如果 <code>comparator</code> 为 <code>null</code>，<code>lhs</code> 必须
     * 改为实现 {@link Comparable}.
     *
     * @param lhs 左侧对象
     * @param rhs 右侧对象
     * @param comparator  <code>Comparator</code> 用于比较对象的 <code>Comparator</code>，
     *  <code>null</code> 表示将 lhs 视为 <code>Comparable</code>
     * @return this - 用于链式追加调用
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  与 <code>lhs</code> 赋值不兼容
     * @since 2.0
     */
    public CompareToBuilder append(final Object lhs, final Object rhs, final Comparator<?> comparator) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.getClass().isArray()) {
            // 按数组类型分发到正确的处理器
            // 处理多维数组
            // 如果 rhs 不是正确的数组类型则抛出 ClassCastException
            if (lhs instanceof long[]) {
                append((long[]) lhs, (long[]) rhs);
            } else if (lhs instanceof int[]) {
                append((int[]) lhs, (int[]) rhs);
            } else if (lhs instanceof short[]) {
                append((short[]) lhs, (short[]) rhs);
            } else if (lhs instanceof char[]) {
                append((char[]) lhs, (char[]) rhs);
            } else if (lhs instanceof byte[]) {
                append((byte[]) lhs, (byte[]) rhs);
            } else if (lhs instanceof double[]) {
                append((double[]) lhs, (double[]) rhs);
            } else if (lhs instanceof float[]) {
                append((float[]) lhs, (float[]) rhs);
            } else if (lhs instanceof boolean[]) {
                append((boolean[]) lhs, (boolean[]) rhs);
            } else {
                // 不是基本类型数组
                // 如果 rhs 不是数组则抛出 ClassCastException
                append((Object[]) lhs, (Object[]) rhs, comparator);
            }
        } else {
            // 简单情况，非数组，直接测试元素
            if (comparator == null) {
                @SuppressWarnings("unchecked") // assume this can be done; if not throw CCE as per Javadoc
                final Comparable<Object> comparable = (Comparable<Object>) lhs;
                comparison = comparable.compareTo(rhs);
            } else {
                @SuppressWarnings("unchecked") // assume this can be done; if not throw CCE as per Javadoc
                final Comparator<Object> comparator2 = (Comparator<Object>) comparator;
                comparison = comparator2.compare(lhs, rhs);
            }
        }
        return this;
    }

    //-------------------------------------------------------------------------
    /**
     * 向 builder 追加比较
     * 两个 long 值.
     *
     * @param lhs 左侧值
     * @param rhs 右侧值
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final long lhs, final long rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = (Long.compare(lhs, rhs));
        return this;
    }

    /**
     * 向 builder 追加比较
     * 两个 int 值.
     *
     * @param lhs 左侧值
     * @param rhs 右侧值
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final int lhs, final int rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = (Integer.compare(lhs, rhs));
        return this;
    }

    /**
     * 向 builder 追加比较
     * 两个 short 值.
     *
     * @param lhs 左侧值
     * @param rhs 右侧值
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final short lhs, final short rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = (Short.compare(lhs, rhs));
        return this;
    }

    /**
     * 向 builder 追加比较
     * 两个 char 值.
     *
     * @param lhs 左侧值
     * @param rhs 右侧值
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final char lhs, final char rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = (Character.compare(lhs, rhs));
        return this;
    }

    /**
     * 向 builder 追加比较
     * 两个 byte 值.
     *
     * @param lhs 左侧值
     * @param rhs 右侧值
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final byte lhs, final byte rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = (Byte.compare(lhs, rhs));
        return this;
    }

    /**
     * <p>向 builder 追加比较
     * 两个 double 值.
     *
     * <p>This handles NaNs, Infinities, and <code>-0.0</code>.
     *
     * <p>It is compatible with the hash code generated by
     * <code>HashCodeBuilder</code>.
     *
     * @param lhs 左侧值
     * @param rhs 右侧值
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final double lhs, final double rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Double.compare(lhs, rhs);
        return this;
    }

    /**
     * <p>向 builder 追加比较
     * 两个 float 值.
     *
     * <p>This handles NaNs, Infinities, and <code>-0.0</code>.
     *
     * <p>It is compatible with the hash code generated by
     * <code>HashCodeBuilder</code>.
     *
     * @param lhs 左侧值
     * @param rhs 右侧值
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final float lhs, final float rhs) {
        if (comparison != 0) {
            return this;
        }
        comparison = Float.compare(lhs, rhs);
        return this;
    }

    /**
     * 向 builder 追加比较
     * 两个 boolean 值.
     *
     * @param lhs 左侧值
     * @param rhs 右侧值
     * @return this - 用于链式追加调用
      */
    public CompareToBuilder append(final boolean lhs, final boolean rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == false) {
            comparison = -1;
        } else {
            comparison = +1;
        }
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>向 builder 追加深度比较
     * 两个 Object 数组.
     *
     * <ol>
     *  <li>使用 <code>==</code> 检查数组是否相同</li>
     *  <li>检查 null，null 小于非 null</li>
     *  <li>检查数组长度，短数组小于长数组</li>
     *  <li>使用 {@link #append(Object, Object, Comparator)} 逐元素检查数组内容</li>
     * </ol>
     *
     * <p>此方法也会被调用用于多维、
     * 不规则和多类型数组的顶层.
     *
     * @param lhs 左侧数组
     * @param rhs 右侧数组
     * @return this - 用于链式追加调用
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  与 <code>lhs</code> 赋值不兼容
     */
    public CompareToBuilder append(final Object[] lhs, final Object[] rhs) {
        return append(lhs, rhs, null);
    }

    /**
     * <p>向 builder 追加深度比较
     * 两个 Object 数组.
     *
     * <ol>
     *  <li>使用 <code>==</code> 检查数组是否相同</li>
     *  <li>检查 null，null 小于非 null</li>
     *  <li>检查数组长度，短数组小于长数组</li>
     *  <li>使用 {@link #append(Object, Object, Comparator)} 逐元素检查数组内容</li>
     * </ol>
     *
     * <p>此方法也会被调用用于多维、
     * 不规则和多类型数组的顶层.
     *
     * @param lhs 左侧数组
     * @param rhs 右侧数组
     * @param comparator  <code>Comparator</code> to use to compare the array elements,
     *  <code>null</code> 表示将 <code>lhs</code> 元素视为 <code>Comparable</code>.
     * @return this - 用于链式追加调用
     * @throws ClassCastException  if <code>rhs</code> is not assignment-compatible
     *  与 <code>lhs</code> 赋值不兼容
     * @since 2.0
     */
    public CompareToBuilder append(final Object[] lhs, final Object[] rhs, final Comparator<?> comparator) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i], comparator);
        }
        return this;
    }

    /**
     * <p>向 builder 追加深度比较
     * 两个 long 数组.
     *
     * <ol>
     *  <li>使用 <code>==</code> 检查数组是否相同</li>
     *  <li>检查 null，null 小于非 null</li>
     *  <li>检查数组长度，短数组小于长数组</li>
     *  <li>使用 {@link #append(long, long)} 逐元素检查数组内容</li>
     * </ol>
     *
     * @param lhs 左侧数组
     * @param rhs 右侧数组
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final long[] lhs, final long[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>向 builder 追加深度比较
     * 两个 int 数组.
     *
     * <ol>
     *  <li>使用 <code>==</code> 检查数组是否相同</li>
     *  <li>检查 null，null 小于非 null</li>
     *  <li>检查数组长度，短数组小于长数组</li>
     *  <li>使用 {@link #append(int, int)} 逐元素检查数组内容</li>
     * </ol>
     *
     * @param lhs 左侧数组
     * @param rhs 右侧数组
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final int[] lhs, final int[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>向 builder 追加深度比较
     * 两个 short 数组.
     *
     * <ol>
     *  <li>使用 <code>==</code> 检查数组是否相同</li>
     *  <li>检查 null，null 小于非 null</li>
     *  <li>检查数组长度，短数组小于长数组</li>
     *  <li>使用 {@link #append(short, short)} 逐元素检查数组内容</li>
     * </ol>
     *
     * @param lhs 左侧数组
     * @param rhs 右侧数组
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final short[] lhs, final short[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>向 builder 追加深度比较
     * 两个 char 数组.
     *
     * <ol>
     *  <li>使用 <code>==</code> 检查数组是否相同</li>
     *  <li>检查 null，null 小于非 null</li>
     *  <li>检查数组长度，短数组小于长数组</li>
     *  <li>使用 {@link #append(char, char)} 逐元素检查数组内容</li>
     * </ol>
     *
     * @param lhs 左侧数组
     * @param rhs 右侧数组
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final char[] lhs, final char[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>向 builder 追加深度比较
     * 两个 byte 数组.
     *
     * <ol>
     *  <li>使用 <code>==</code> 检查数组是否相同</li>
     *  <li>检查 null，null 小于非 null</li>
     *  <li>检查数组长度，短数组小于长数组</li>
     *  <li>使用 {@link #append(byte, byte)} 逐元素检查数组内容</li>
     * </ol>
     *
     * @param lhs 左侧数组
     * @param rhs 右侧数组
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final byte[] lhs, final byte[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>向 builder 追加深度比较
     * 两个 double 数组.
     *
     * <ol>
     *  <li>使用 <code>==</code> 检查数组是否相同</li>
     *  <li>检查 null，null 小于非 null</li>
     *  <li>检查数组长度，短数组小于长数组</li>
     *  <li>使用 {@link #append(double, double)} 逐元素检查数组内容</li>
     * </ol>
     *
     * @param lhs 左侧数组
     * @param rhs 右侧数组
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final double[] lhs, final double[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>向 builder 追加深度比较
     * 两个 float 数组.
     *
     * <ol>
     *  <li>使用 <code>==</code> 检查数组是否相同</li>
     *  <li>检查 null，null 小于非 null</li>
     *  <li>检查数组长度，短数组小于长数组</li>
     *  <li>使用 {@link #append(float, float)} 逐元素检查数组内容</li>
     * </ol>
     *
     * @param lhs 左侧数组
     * @param rhs 右侧数组
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final float[] lhs, final float[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    /**
     * <p>向 builder 追加深度比较
     * 两个 boolean 数组.
     *
     * <ol>
     *  <li>使用 <code>==</code> 检查数组是否相同</li>
     *  <li>检查 null，null 小于非 null</li>
     *  <li>检查数组长度，短数组小于长数组</li>
     *  <li>使用 {@link #append(boolean, boolean)} 逐元素检查数组内容</li>
     * </ol>
     *
     * @param lhs 左侧数组
     * @param rhs 右侧数组
     * @return this - 用于链式追加调用
     */
    public CompareToBuilder append(final boolean[] lhs, final boolean[] rhs) {
        if (comparison != 0) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }
        if (lhs == null) {
            comparison = -1;
            return this;
        }
        if (rhs == null) {
            comparison = +1;
            return this;
        }
        if (lhs.length != rhs.length) {
            comparison = (lhs.length < rhs.length) ? -1 : +1;
            return this;
        }
        for (int i = 0; i < lhs.length && comparison == 0; i++) {
            append(lhs[i], rhs[i]);
        }
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * 返回负整数、正整数或零，表示
     * builder 判断左侧
     * 小于、大于或等于右侧
     * （比较结果）.
     *
     * @return 最终比较结果
     * @see #build()
     */
    public int toComparison() {
        return comparison;
    }

    /**
     * 返回负 Integer、正 Integer 或零，表示
     * builder 判断左侧
     * 小于、大于或等于右侧
     * （比较结果）.
     *
     * @return 最终比较结果 as an Integer
     * @see #toComparison()
     * @since 3.0
     */
    @Override
    public Integer build() {
        return toComparison();
    }
}

