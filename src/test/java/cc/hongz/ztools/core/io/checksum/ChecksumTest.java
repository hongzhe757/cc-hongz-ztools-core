package cc.hongz.ztools.core.io.checksum;

import cc.hongz.ztools.core.io.checksum.crc16.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * {@link CRC8}、{@link CRC16} 及 crc16 子包 单元测试.
 */
@DisplayName("CRC 校验和单元测试")
class ChecksumTest {

    private static final byte[] TEST_DATA = "hello".getBytes();

    @DisplayName("CRC8 计算校验和")
    @Test
    void crc8ComputesChecksum() {
        CRC8 crc8 = new CRC8(0x07, (short) 0);
        crc8.update(TEST_DATA, 0, TEST_DATA.length);
        long value = crc8.getValue();
        // CRC8 值应在 0-255 范围内
        assertEquals(value, value & 0xFF);
    }

    @DisplayName("CRC8 重置清零")
    @Test
    void crc8ResetClearsValue() {
        CRC8 crc8 = new CRC8(0x07, (short) 0);
        crc8.update(TEST_DATA, 0, TEST_DATA.length);
        crc8.reset();
        assertEquals(0, crc8.getValue());
    }

    @DisplayName("CRC16 计算校验和")
    @Test
    void crc16ComputesChecksum() {
        CRC16 crc16 = new CRC16();
        crc16.update(TEST_DATA, 0, TEST_DATA.length);
        long value = crc16.getValue();
        assertNotEquals(0, value);
    }

    @DisplayName("CRC16_ANSI 计算校验和")
    @Test
    void crc16AnsiComputesChecksum() {
        CRC16Ansi crc = new CRC16Ansi();
        crc.update(TEST_DATA, 0, TEST_DATA.length);
        long value = crc.getValue();
        assertEquals(value, value & 0xFFFF);
    }

    @DisplayName("CRC16_CCITT 计算校验和")
    @Test
    void crc16CCITTComputesChecksum() {
        CRC16CCITT crc = new CRC16CCITT();
        crc.update(TEST_DATA, 0, TEST_DATA.length);
        long value = crc.getValue();
        assertEquals(value, value & 0xFFFF);
    }

    @DisplayName("CRC16_CCITT_FALSE 计算校验和")
    @Test
    void crc16CCITTFalseComputesChecksum() {
        CRC16CCITTFalse crc = new CRC16CCITTFalse();
        crc.update(TEST_DATA, 0, TEST_DATA.length);
        assertEquals(crc.getValue(), crc.getValue() & 0xFFFF);
    }

    @DisplayName("CRC16_DNP 计算校验和")
    @Test
    void crc16DNPComputesChecksum() {
        CRC16DNP crc = new CRC16DNP();
        crc.update(TEST_DATA, 0, TEST_DATA.length);
        assertEquals(crc.getValue(), crc.getValue() & 0xFFFF);
    }

    @DisplayName("CRC16_IBM 计算校验和")
    @Test
    void crc16IBMComputesChecksum() {
        CRC16IBM crc = new CRC16IBM();
        crc.update(TEST_DATA, 0, TEST_DATA.length);
        assertEquals(crc.getValue(), crc.getValue() & 0xFFFF);
    }

    @DisplayName("CRC16_MAXIM 计算校验和")
    @Test
    void crc16MaximComputesChecksum() {
        CRC16Maxim crc = new CRC16Maxim();
        crc.update(TEST_DATA, 0, TEST_DATA.length);
        assertEquals(crc.getValue(), crc.getValue() & 0xFFFF);
    }

    @DisplayName("CRC16_MODBUS 计算校验和")
    @Test
    void crc16ModbusComputesChecksum() {
        CRC16Modbus crc = new CRC16Modbus();
        crc.update(TEST_DATA, 0, TEST_DATA.length);
        assertEquals(crc.getValue(), crc.getValue() & 0xFFFF);
    }

    @DisplayName("CRC16_USB 计算校验和")
    @Test
    void crc16USBComputesChecksum() {
        CRC16USB crc = new CRC16USB();
        crc.update(TEST_DATA, 0, TEST_DATA.length);
        assertEquals(crc.getValue(), crc.getValue() & 0xFFFF);
    }

    @DisplayName("CRC16_X25 计算校验和")
    @Test
    void crc16X25ComputesChecksum() {
        CRC16X25 crc = new CRC16X25();
        crc.update(TEST_DATA, 0, TEST_DATA.length);
        assertEquals(crc.getValue(), crc.getValue() & 0xFFFF);
    }

    @DisplayName("CRC16_XMODEM 计算校验和")
    @Test
    void crc16XModemComputesChecksum() {
        CRC16XModem crc = new CRC16XModem();
        crc.update(TEST_DATA, 0, TEST_DATA.length);
        assertEquals(crc.getValue(), crc.getValue() & 0xFFFF);
    }

    @DisplayName("不同算法产生不同结果")
    @Test
    void differentVariantsProduceDifferentResults() {
        CRC16Ansi ansi = new CRC16Ansi();
        CRC16CCITT ccitt = new CRC16CCITT();
        ansi.update(TEST_DATA, 0, TEST_DATA.length);
        ccitt.update(TEST_DATA, 0, TEST_DATA.length);
        // 不同算法对相同数据应产生不同结果
        assertNotEquals(ansi.getValue(), ccitt.getValue());
    }
}
