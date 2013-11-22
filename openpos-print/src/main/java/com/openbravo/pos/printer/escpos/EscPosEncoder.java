package com.openbravo.pos.printer.escpos;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class EscPosEncoder {

	private static final byte[] QRCODE_PREFIX = new byte[] { ESCPOS.GS, 0x28, 0x6b };

	public static enum CorrectionLevel {
		L(48), M(49), Q(50), H(51);

		private byte level;

		private CorrectionLevel(int level) {
			this.level = (byte)level;
		}

		public byte getLevel() {
			return level;
		}
	}

	public static byte[] encodeLength(int length) {
		byte highByte = (byte)(length >> 8);
		byte lowByte = (byte)(length & 0xff);
		return new byte[] { lowByte, highByte };
	}

	public static byte[] qrCodeModule(byte module) {
		return encode(QRCODE_PREFIX, new byte[] { 0x31, 0x43 }, new byte[] { module });
	}

	private static byte[] encode(byte[] prefix, byte[] command, byte[] data) {
		byte[] length = encodeLength(command.length + data.length);
		ByteBuffer bb = ByteBuffer.allocate(2 + prefix.length + command.length + data.length);
		bb.put(prefix);
		bb.put(length);
		bb.put(command);
		bb.put(data);
		return bb.array();
	}

	public static byte[] qrCodeCorrection(CorrectionLevel cl) {
		return encode(QRCODE_PREFIX, new byte[] { 0x31, 0x45 }, new byte[] { cl.getLevel() });
	}

	public static byte[] qrCodeStore(String data) {
		try {
			return encode(QRCODE_PREFIX, new byte[] { 0x31, 0x50, 0x30 }, data.getBytes("utf8"));
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] qrCodePrint() {
		return encode(QRCODE_PREFIX, new byte[] { 0x31, 0x51 }, new byte[] { 0x30 });
	}
}
