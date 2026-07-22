package cc.hongz.ztools.core.convert;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 类型转换器（简化版，仅保留map包所需的最小功能）
 *
 * @author hongz
 */
public class Convert {

	/**
	 * 转换值为指定类型
	 *
	 * @param <T>   目标类型
	 * @param type  目标类型
	 * @param value 值
	 * @return 转换后的值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convert(Type type, Object value) {
		if (null == value) {
			return null;
		}
		if (null == type) {
			return (T) value;
		}
		// 目标类型是Class
		if (type instanceof Class) {
			Class<?> clazz = (Class<?>) type;
			if (clazz.isInstance(value)) {
				return (T) value;
			}
			// String
			if (clazz == String.class) {
				return (T) value.toString();
			}
			// 数字类型
			if (value instanceof Number) {
				Number num = (Number) value;
				if (clazz == Integer.class || clazz == int.class) {
					return (T) Integer.valueOf(num.intValue());
				}
				if (clazz == Long.class || clazz == long.class) {
					return (T) Long.valueOf(num.longValue());
				}
				if (clazz == Double.class || clazz == double.class) {
					return (T) Double.valueOf(num.doubleValue());
				}
				if (clazz == Float.class || clazz == float.class) {
					return (T) Float.valueOf(num.floatValue());
				}
				if (clazz == Short.class || clazz == short.class) {
					return (T) Short.valueOf(num.shortValue());
				}
				if (clazz == Byte.class || clazz == byte.class) {
					return (T) Byte.valueOf(num.byteValue());
				}
				if (clazz == Boolean.class || clazz == boolean.class) {
					return (T) Boolean.valueOf(num.intValue() != 0);
				}
				if (clazz == BigDecimal.class) {
					return (T) new BigDecimal(num.toString());
				}
				if (clazz == BigInteger.class) {
					return (T) BigInteger.valueOf(num.longValue());
				}
			}
			// String -> 其他类型
			if (value instanceof String) {
				String str = (String) value;
				if (clazz == Integer.class || clazz == int.class) {
					return (T) Integer.valueOf(str);
				}
				if (clazz == Long.class || clazz == long.class) {
					return (T) Long.valueOf(str);
				}
				if (clazz == Double.class || clazz == double.class) {
					return (T) Double.valueOf(str);
				}
				if (clazz == Float.class || clazz == float.class) {
					return (T) Float.valueOf(str);
				}
				if (clazz == Boolean.class || clazz == boolean.class) {
					return (T) Boolean.valueOf(str);
				}
				if (clazz == BigDecimal.class) {
					return (T) new BigDecimal(str);
				}
				if (clazz == BigInteger.class) {
					return (T) new BigInteger(str);
				}
			}
		}
		// 默认不做转换
		return (T) value;
	}

	/**
	 * 转换值为指定类型
	 *
	 * @param <T>   目标类型
	 * @param clazz 目标类型
	 * @param value 值
	 * @return 转换后的值
	 */
	public static <T> T convert(Class<T> clazz, Object value) {
		return convert((Type) clazz, value);
	}

	// --------------- 便捷转换方法 ---------------

	public static String toStr(Object value, String defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof String) {
			return (String) value;
		}
		return value.toString();
	}

	public static Integer toInt(Object value, Integer defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		try {
			return Integer.valueOf(value.toString());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static Short toShort(Object value, Short defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof Short) {
			return (Short) value;
		}
		if (value instanceof Number) {
			return ((Number) value).shortValue();
		}
		try {
			return Short.valueOf(value.toString());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static Boolean toBool(Object value, Boolean defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue() != 0;
		}
		String str = value.toString();
		if ("true".equalsIgnoreCase(str) || "yes".equalsIgnoreCase(str) || "1".equals(str)) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(str) || "no".equalsIgnoreCase(str) || "0".equals(str)) {
			return Boolean.FALSE;
		}
		return defaultValue;
	}

	public static Long toLong(Object value, Long defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof Long) {
			return (Long) value;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		try {
			return Long.valueOf(value.toString());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static Character toChar(Object value, Character defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof Character) {
			return (Character) value;
		}
		String str = value.toString();
		if (str.length() > 0) {
			return str.charAt(0);
		}
		return defaultValue;
	}

	public static Float toFloat(Object value, Float defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof Float) {
			return (Float) value;
		}
		if (value instanceof Number) {
			return ((Number) value).floatValue();
		}
		try {
			return Float.valueOf(value.toString());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static Double toDouble(Object value, Double defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof Double) {
			return (Double) value;
		}
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		try {
			return Double.valueOf(value.toString());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static Byte toByte(Object value, Byte defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof Byte) {
			return (Byte) value;
		}
		if (value instanceof Number) {
			return ((Number) value).byteValue();
		}
		try {
			return Byte.valueOf(value.toString());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		if (value instanceof Number) {
			return new BigDecimal(value.toString());
		}
		try {
			return new BigDecimal(value.toString());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static BigInteger toBigInteger(Object value, BigInteger defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof BigInteger) {
			return (BigInteger) value;
		}
		if (value instanceof Number) {
			return BigInteger.valueOf(((Number) value).longValue());
		}
		try {
			return new BigInteger(value.toString());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof Enum) {
			return (E) value;
		}
		try {
			return Enum.valueOf(clazz, value.toString());
		} catch (IllegalArgumentException e) {
			return defaultValue;
		}
	}

	public static Date toDate(Object value, Date defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof Date) {
			return (Date) value;
		}
		if (value instanceof java.util.Calendar) {
			return ((java.util.Calendar) value).getTime();
		}
		if (value instanceof Number) {
			return new Date(((Number) value).longValue());
		}
		return defaultValue;
	}
}
