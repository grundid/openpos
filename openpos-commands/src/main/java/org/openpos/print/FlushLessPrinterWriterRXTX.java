package org.openpos.print;

import com.openbravo.pos.printer.TicketPrinterException;

public class FlushLessPrinterWriterRXTX extends PrinterWriterRXTX {

	public FlushLessPrinterWriterRXTX(String sPortPrinter) throws TicketPrinterException {
		super(sPortPrinter);
	}

	@Override
	protected void daemonFlush() {
	}

}
