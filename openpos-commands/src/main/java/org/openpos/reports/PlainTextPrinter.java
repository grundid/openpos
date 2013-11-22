package org.openpos.reports;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.openbravo.pos.printer.DevicePrinter;

public class PlainTextPrinter implements DevicePrinter {

	private static final char NEW_LINE = '\n';
	private StringBuilder out = new StringBuilder();

	public String getPlainText() {
		return out.toString();
	}

	@Override
	public String getPrinterName() {
		return "PlainText";
	}

	@Override
	public String getPrinterDescription() {
		return null;
	}

	@Override
	public JComponent getPrinterComponent() {
		return null;
	}

	@Override
	public void reset() {
	}

	@Override
	public void beginReceipt() {
	}

	@Override
	public void printImage(BufferedImage image) {
	}

	@Override
	public void printBarCode(String type, String position, String code) {
		if (!DevicePrinter.POSITION_NONE.equals(position)) {
			out.append(code);
			out.append(NEW_LINE);
		}
	}

	@Override
	public void beginLine(int iTextSize) {
	}

	@Override
	public void printText(int iStyle, String sText) {
		out.append(sText);
	}

	@Override
	public void endLine() {
		out.append(NEW_LINE);
	}

	@Override
	public void endReceipt() {
		out.append(NEW_LINE);
		out.append(NEW_LINE);
		out.append(NEW_LINE);
		out.append(NEW_LINE);
		out.append(NEW_LINE);
	}

	@Override
	public void openDrawer() {
	}
}
