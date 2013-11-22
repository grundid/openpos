package com.openbravo.pos.printer;

import java.util.HashMap;
import java.util.Map;

import com.openbravo.pos.printer.escpos.PrinterWriterFactory;
import com.openbravo.pos.printer.escpos.PrinterWritter;
import com.openbravo.pos.printer.escpos.PrinterWritterFile;
import com.openbravo.pos.printer.escpos.PrinterWritterRXTX;

public class PrinterWritterPool implements PrinterWriterFactory {

    protected Map<String, PrinterWritter> m_apool = new HashMap<String, PrinterWritter>();

    public PrinterWritter getPrinterWriter(String con, String port) throws TicketPrinterException {

        String skey = con + "-->" + port;
        PrinterWritter pw = (PrinterWritter) m_apool.get(skey);
        if (pw == null) {
            if ("serial".equals(con) || "rxtx".equals(con)) {
                pw = new PrinterWritterRXTX(port);
                m_apool.put(skey, pw);
            } else if ("file".equals(con)) {
                pw = new PrinterWritterFile(port);
                m_apool.put(skey, pw);
            } else {
                throw new TicketPrinterException();
            }
        }
        return pw;
    }
}