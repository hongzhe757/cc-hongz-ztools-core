package cc.hongz.ztools.core.exceptions;

/**
 * {@link java.lang.reflect.InvocationTargetException} 的运行时包装.
 *
 * @author looly
 * @since 5.8.1
 */
public class InvocationTargetRuntimeException extends UtilException {

	/**
	 * 根据原因异常构造.
	 *
	 * @param e 原因异常
	 */
	public InvocationTargetRuntimeException(final Throwable e) {
		super(e);
	}

	/**
	 * 构造.
	 *
	 * @param message 消息
	 */
	public InvocationTargetRuntimeException(final String message) {
		super(message);
	}

	/**
	 * 按模板构造消息.
	 *
	 * @param messageTemplate 消息模板
	 * @param params          模板参数
	 */
	public InvocationTargetRuntimeException(final String messageTemplate, final Object... params) {
		super(messageTemplate, params);
	}

	/**
	 * 构造.
	 *
	 * @param message   消息
	 * @param throwable 原因
	 */
	public InvocationTargetRuntimeException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * 构造（可控制抑制与栈轨迹）.
	 *
	 * @param message            消息
	 * @param throwable          原因
	 * @param enableSuppression  是否启用抑制
	 * @param writableStackTrace 是否可写栈轨迹
	 */
	public InvocationTargetRuntimeException(final String message, final Throwable throwable,
											final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	/**
	 * 按模板构造消息并附带原因.
	 *
	 * @param throwable       原因
	 * @param messageTemplate 消息模板
	 * @param params          模板参数
	 */
	public InvocationTargetRuntimeException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(throwable, messageTemplate, params);
	}
}
