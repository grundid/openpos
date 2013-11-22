package org.openpos.print;

import com.openbravo.pos.printer.PrinterWritterPool;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.printer.escpos.PrinterWritter;

public class MyPrinterWriterFactory extends PrinterWritterPool {

	private PrinterWriterRXTX printerWriterRXTX;

	@Override
	public PrinterWritter getPrinterWriter(String con, String port) throws TicketPrinterException {

		if ("serial".equals(con) || "rxtx".equals(con)) {
			if (printerWriterRXTX == null)
				printerWriterRXTX = new PrinterWriterRXTX(port);
			return printerWriterRXTX;
		}
		else if ("flushlessserial".equals(con)) {
			if (printerWriterRXTX == null)
				printerWriterRXTX = new FlushLessPrinterWriterRXTX(port);
			return printerWriterRXTX;
		}
		else
			return super.getPrinterWriter(con, port);
	}

}
