package com.openbravo.pos.printer.escpos;

import com.openbravo.pos.printer.TicketPrinterException;

public interface PrinterWriterFactory {

	public PrinterWritter getPrinterWriter(String con, String port) throws TicketPrinterException;
}
