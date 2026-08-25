package cc.hongz.ztools.core.exceptions;

import cc.hongz.ztools.core.util.StrUtil;

/**
 * 组件或上下文未初始化异常.
 *
 * @author xiaoleilu
 */
public class NotInitedException extends RuntimeException {

	private static final long serialVersionUID = 8247610319171014183L;

	/**
	 * 构造.
	 *
	 * @param e 原因
	 */
	public NotInitedException(final Throwable e) {
		super(e);
	}

	/**
	 * 构造.
	 *
	 * @param message 消息
	 */
	public NotInitedException(final String message) {
		super(message);
	}

	/**
	 * 按模板构造消息.
	 *
	 * @param messageTemplate 消息模板
	 * @param params          模板参数
	 */
	public NotInitedException(final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	/**
	 * 构造.
	 *
	 * @param message   消息
	 * @param throwable 原因
	 */
	public NotInitedException(final String message, final Throwable throwable) {
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
	public NotInitedException(final String message, final Throwable throwable,
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
	public NotInitedException(final Throwable throwable, final String messageTemplate, final Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
