package com.openbravo.pos.printer.escpos;

import static org.junit.Assert.*;

import org.junit.Test;

import com.openbravo.pos.printer.escpos.EscPosEncoder.CorrectionLevel;

public class EscPosEncoderTest {

	@Test
	public void itShouldEncodeLength() throws Exception {
		byte[] length = EscPosEncoder.encodeLength(7000);
		assertEquals(0x58, length[0]);
		assertEquals(0x1b, length[1]);
		byte[] length2 = EscPosEncoder.encodeLength(255);
		assertEquals((byte)0xff, length2[0]);
		assertEquals(0x00, length2[1]);
		byte[] length3 = EscPosEncoder.encodeLength(256);
		assertEquals(0x00, length3[0]);
		assertEquals(0x01, length3[1]);
	}

	@Test
	public void itShouldEncodeModule() throws Exception {
		assertArrayEquals(new byte[] { (byte)0x1D, (byte)0x28, (byte)0x6B, (byte)0x03, (byte)0x00, (byte)0x31,
				(byte)0x43, (byte)0x04 }, EscPosEncoder.qrCodeModule((byte)4));
	}

	@Test
	public void itShouldEncodeCorrectionLevel() throws Exception {
		assertArrayEquals(new byte[] { (byte)0x1D, (byte)0x28, (byte)0x6B, (byte)0x03, (byte)0x00, (byte)0x31,
				(byte)0x45, (byte)0x33 }, EscPosEncoder.qrCodeCorrection(CorrectionLevel.H));
	}

	@Test
	public void itShouldEncodeStoreCode() throws Exception {
		assertArrayEquals(new byte[] { (byte)0x1D, (byte)0x28, (byte)0x6B, (byte)0x0d, (byte)0x00, (byte)0x31,
				(byte)0x50, (byte)0x30, (byte)0x56, (byte)0x69, (byte)0x76, (byte)0x61, (byte)0x20, (byte)0x43,
				(byte)0x68, (byte)0x69, (byte)0x6c, (byte)0x65 }, EscPosEncoder.qrCodeStore("Viva Chile"));
	}
	
	@Test
	public void itShouldEncodePrint() throws Exception {
		assertArrayEquals(new byte[] { (byte)0x1D, (byte)0x28, (byte)0x6B, (byte)0x03, (byte)0x00, (byte)0x31, (byte)0x51,
						(byte)0x30 }, EscPosEncoder.qrCodePrint());
		
	}
}
