package cc.hongz.ztools.core.convert.impl;

import java.nio.charset.Charset;

import cc.hongz.ztools.core.convert.AbstractConverter;
import cc.hongz.ztools.core.util.CharsetUtil;

/**
 * 编码对象转换器
 * @author Looly
 *
 */
public class CharsetConverter extends AbstractConverter<Charset>{
	private static final long serialVersionUID = 1L;

	@Override
	protected Charset convertInternal(Object value) {
		return CharsetUtil.charset(convertToStr(value));
	}

}
