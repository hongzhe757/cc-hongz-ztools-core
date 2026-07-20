package cc.hongz.ztools.core.text.csv;

/**
 * CSV的行处理器，实现此接口用于按照行处理数据
 *
 * @author Looly
 * @since 5.0.4
 */
@FunctionalInterface
public interface CsvRowHandler {
	void handle(CsvRow row);
}
