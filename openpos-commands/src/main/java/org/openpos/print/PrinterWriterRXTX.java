package org.openpos.print;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.ParallelPort;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.printer.escpos.PrinterWritter;

public class PrinterWriterRXTX extends PrinterWritter {

	private boolean initialized = false;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream(100 * 1024);

	private CommPortIdentifier m_PortIdPrinter;
	private CommPort m_CommPortPrinter;

	private String m_sPortPrinter;
	private OutputStream m_out;

	/** Creates a new instance of PrinterWritterComm */
	public PrinterWriterRXTX(String sPortPrinter) throws TicketPrinterException {
		m_sPortPrinter = sPortPrinter;
		m_out = null;
	}

	protected void daemonWrite(byte[] data) {
		try {
			if (m_out == null) {
				initPort();
			}
			m_out.write(data);
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}

	private void initPort() throws NoSuchPortException, PortInUseException, IOException,
			UnsupportedCommOperationException {
		m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPortPrinter); // Tomamos el puerto                   
		m_CommPortPrinter = m_PortIdPrinter.open("PORTID", 2000); // Abrimos el puerto       

		if (m_PortIdPrinter.getPortType() == CommPortIdentifier.PORT_SERIAL) {
			((SerialPort)m_CommPortPrinter).setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE); // Configuramos el puerto
		}
		else if (m_PortIdPrinter.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
			((ParallelPort)m_CommPortPrinter).setMode(1);
		}
		m_out = m_CommPortPrinter.getOutputStream(); // Tomamos el chorro de escritura   

	}

	protected void daemonFlush() {
		try {
			if (m_out != null) {
				m_out.flush();
			}
		}
		catch (IOException e) {
			System.err.println(e);
		}
	}

	protected void daemonClose() {
		try {
			if (m_out != null) {
				daemonFlush();
				m_out.close();
				m_CommPortPrinter.close();
			}
		}
		catch (IOException e) {
			System.err.println(e);
		}
		finally {
			m_out = null;
			m_CommPortPrinter = null;
			m_PortIdPrinter = null;
		}
	}

	private void localWrite(byte[] data) {
		try {
			baos.write(data);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void init(byte[] data) {
		if (!initialized) {
			localWrite(data);
			initialized = true;
		}
	}

	@Override
	public void write(byte[] data) {
		localWrite(data);
	}

	@Override
	public void write(String sValue) {
		localWrite(sValue.getBytes());
	}

	@Override
	public void flush() {
		daemonWrite(baos.toByteArray());
		daemonFlush();
		baos.reset();
		close();
	}

	@Override
	public void close() {
		daemonClose();
	}

	@Override
	protected void internalWrite(byte[] data) {

	}

	@Override
	protected void internalFlush() {

	}

	@Override
	protected void internalClose() {
	}
}
