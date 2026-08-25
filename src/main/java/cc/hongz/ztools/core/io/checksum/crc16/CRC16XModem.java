package cc.hongz.ztools.core.io.checksum.crc16;

/**
 * CRC-CCITT (XModem)
 * CRC16_XMODEM：多项式x16+x12+x5+1（0x1021），初始值0x0000，低位在后，高位在前，结果与0x0000异或。
 *
 * @author looly
 * @since 5.3.10
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class CRC16XModem extends CRC16Checksum {
    private static final long serialVersionUID = 1L;

    /**
     * CRC16 多项式常量。
     */
    private static final int WC_POLY = 0x1021;

    @Override
    public void update(final byte[] b, final int off, final int len) {
        super.update(b, off, len);
        wCRCin &= 0xffff;
    }

    @Override
    public void update(final int b) {
        for (int i = 0; i < 8; i++) {
            boolean bit = ((b >> (7 - i) & 1) == 1);
            boolean c15 = ((wCRCin >> 15 & 1) == 1);
            wCRCin <<= 1;
            if (c15 ^ bit) {
                wCRCin ^= WC_POLY;
            }
        }
    }
}
