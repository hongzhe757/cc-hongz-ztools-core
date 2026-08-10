package cc.hongz.ztools.core.text.csv;

/**
 * CSV的行处理器，实现此接口用于按照行处理数据.
 *
 * @author looly
 * @since 5.0.4
 */
@FunctionalInterface
public interface CsvRowHandler {

    /**
     * 处理一行数据.
     *
     * @param row CSV行数据
     */
    void handle(final CsvRow row);
}
