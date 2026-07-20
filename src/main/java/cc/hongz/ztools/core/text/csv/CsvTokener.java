package cc.hongz.ztools.core.text.csv;

import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.io.IoUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * CSV解析器，用于解析CSV文件
 *
 * @author looly
 * @since 5.8.0
 */
public class CsvTokener implements Closeable {

	private final Reader raw;
	private long index;
	private int prev;
	private boolean usePrev;

	public CsvTokener(final Reader reader) {
		this.raw = IoUtil.toBuffered(reader);
	}

	public int next() {
		if(this.usePrev){
			this.usePrev = false;
		}else{
			try {
				this.prev = this.raw.read();
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}
		this.index++;
		return this.prev;
	}

	public void back() throws IllegalStateException {
		if (this.usePrev || this.index <= 0) {
			throw new IllegalStateException("Stepping back two steps is not supported");
		}
		this.index --;
		this.usePrev = true;
	}

	public long getIndex() { return this.index; }

	@Override
	public void close() throws IOException {
		IoUtil.close(this.raw);
	}
}
