package cc.hongz.ztools.core.io.checksum.crc16;

/**
 * CRC16_X25：多项式x16+x12+x5+1（0x1021），初始值0xffff，低位在前，高位在后，结果与0xFFFF异或
 * 0x8408是0x1021按位颠倒后的结果。
 *
 * @author looly
 * @since 5.3.10
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class CRC16X25 extends CRC16Checksum {
    private static final long serialVersionUID = 1L;

    /**
     * CRC16 多项式常量。
     */
    private static final int WC_POLY = 0x8408;

    /**
     * 重置校验和。
     */
    @Override
    public void reset() {
        this.wCRCin = 0xffff;
    }

    /**
     * 更新校验和。
     */
    @Override
    public void update(final byte[] b, final int off, final int len) {
        super.update(b, off, len);
        wCRCin ^= 0xffff;
    }

    /**
     * 更新校验和。
     */
    @Override
    public void update(final int b) {
        wCRCin ^= (b & 0x00ff);
        for (int j = 0; j < 8; j++) {
            if ((wCRCin & 0x0001) != 0) {
                wCRCin >>= 1;
                wCRCin ^= WC_POLY;
            } else {
                wCRCin >>= 1;
            }
        }
    }
}
