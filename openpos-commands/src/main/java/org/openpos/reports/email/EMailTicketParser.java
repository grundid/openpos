package org.openpos.reports.email;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.openbravo.data.loader.LocalRes;
import com.openbravo.pos.printer.DeviceDisplayBase;
import com.openbravo.pos.printer.DevicePrinter;
import com.openbravo.pos.printer.DeviceTicket;
import com.openbravo.pos.printer.TicketPrinterException;

public class EMailTicketParser extends DefaultHandler {

	private static SAXParser m_sp = null;
	private StringBuffer text;
	private String bctype;
	private String bcposition;
	private int m_iTextAlign;
	private int m_iTextLength;
	private int m_iTextStyle;
	private StringBuffer m_sVisorLine;
	private int m_iVisorAnimation;
	private String m_sVisorLine1;
	private String m_sVisorLine2;
	private double m_dValue1;
	private double m_dValue2;
	private int attribute3;
	private int m_iOutputType;
	private static final int OUTPUT_NONE = 0;
	private static final int OUTPUT_DISPLAY = 1;
	private static final int OUTPUT_TICKET = 2;
	private static final int OUTPUT_FISCAL = 3;
	private DevicePrinter outputPrinter;

	public EMailTicketParser(DevicePrinter devicePrinter) {
		outputPrinter = devicePrinter;
	}

	public void printTicket(String sIn) throws TicketPrinterException {
		printTicket(new StringReader(sIn));
	}

	public void printTicket(Reader in) throws TicketPrinterException {
		try {
			if (m_sp == null) {
				SAXParserFactory spf = SAXParserFactory.newInstance();
				m_sp = spf.newSAXParser();
			}
			m_sp.parse(new InputSource(in), this);
		}
		catch (ParserConfigurationException ePC) {
			throw new TicketPrinterException(LocalRes.getIntString("exception.parserconfig"), ePC);
		}
		catch (SAXException eSAX) {
			throw new TicketPrinterException(LocalRes.getIntString("exception.xmlfile"), eSAX);
		}
		catch (IOException eIO) {
			throw new TicketPrinterException(LocalRes.getIntString("exception.iofile"), eIO);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		// inicalizo las variables pertinentes
		text = null;
		bctype = null;
		bcposition = null;
		m_sVisorLine = null;
		m_iVisorAnimation = DeviceDisplayBase.ANIMATION_NULL;
		m_sVisorLine1 = null;
		m_sVisorLine2 = null;
		m_iOutputType = OUTPUT_NONE;
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (m_iOutputType) {
			case OUTPUT_NONE:
				if ("play".equals(qName)) {
					text = new StringBuffer();
				}
				else if ("ticket".equals(qName)) {
					m_iOutputType = OUTPUT_TICKET;
				}
				else if ("display".equals(qName)) {
					m_iOutputType = OUTPUT_DISPLAY;
					String animation = attributes.getValue("animation");
					if ("scroll".equals(animation)) {
						m_iVisorAnimation = DeviceDisplayBase.ANIMATION_SCROLL;
					}
					else if ("flyer".equals(animation)) {
						m_iVisorAnimation = DeviceDisplayBase.ANIMATION_FLYER;
					}
					else if ("blink".equals(animation)) {
						m_iVisorAnimation = DeviceDisplayBase.ANIMATION_BLINK;
					}
					else if ("curtain".equals(animation)) {
						m_iVisorAnimation = DeviceDisplayBase.ANIMATION_CURTAIN;
					}
					else { // "none"
						m_iVisorAnimation = DeviceDisplayBase.ANIMATION_NULL;
					}
					m_sVisorLine1 = null;
					m_sVisorLine2 = null;
					outputPrinter = null;
				}
				break;
			case OUTPUT_TICKET:
				if ("image".equals(qName)) {
					text = new StringBuffer();
				}
				else if ("barcode".equals(qName)) {
					text = new StringBuffer();
					bctype = attributes.getValue("type");
					bcposition = attributes.getValue("position");
				}
				else if ("line".equals(qName)) {
					outputPrinter.beginLine(parseInt(attributes.getValue("size"), DevicePrinter.SIZE_0));
				}
				else if ("text".equals(qName)) {
					text = new StringBuffer();
					m_iTextStyle = ("true".equals(attributes.getValue("bold")) ? DevicePrinter.STYLE_BOLD
							: DevicePrinter.STYLE_PLAIN)
							| ("true".equals(attributes.getValue("underline")) ? DevicePrinter.STYLE_UNDERLINE
									: DevicePrinter.STYLE_PLAIN);
					String sAlign = attributes.getValue("align");
					if ("right".equals(sAlign)) {
						m_iTextAlign = DevicePrinter.ALIGN_RIGHT;
					}
					else if ("center".equals(sAlign)) {
						m_iTextAlign = DevicePrinter.ALIGN_CENTER;
					}
					else {
						m_iTextAlign = DevicePrinter.ALIGN_LEFT;
					}
					m_iTextLength = parseInt(attributes.getValue("length"), 0);
				}
				break;
			case OUTPUT_DISPLAY:
				if ("line".equals(qName)) { // line 1 or 2 of the display
					m_sVisorLine = new StringBuffer();
				}
				else if ("line1".equals(qName)) { // linea 1 del visor
					m_sVisorLine = new StringBuffer();
				}
				else if ("line2".equals(qName)) { // linea 2 del visor
					m_sVisorLine = new StringBuffer();
				}
				else if ("text".equals(qName)) {
					text = new StringBuffer();
					String sAlign = attributes.getValue("align");
					if ("right".equals(sAlign)) {
						m_iTextAlign = DevicePrinter.ALIGN_RIGHT;
					}
					else if ("center".equals(sAlign)) {
						m_iTextAlign = DevicePrinter.ALIGN_CENTER;
					}
					else {
						m_iTextAlign = DevicePrinter.ALIGN_LEFT;
					}
					m_iTextLength = parseInt(attributes.getValue("length"));
				}
				break;
			case OUTPUT_FISCAL:
				if ("line".equals(qName)) {
					text = new StringBuffer();
					m_dValue1 = parseDouble(attributes.getValue("price"));
					m_dValue2 = parseDouble(attributes.getValue("units"), 1.0);
					attribute3 = parseInt(attributes.getValue("tax"));
				}
				else if ("message".equals(qName)) {
					text = new StringBuffer();
				}
				else if ("total".equals(qName)) {
					text = new StringBuffer();
					m_dValue1 = parseDouble(attributes.getValue("paid"));
				}
				break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (m_iOutputType) {
			case OUTPUT_NONE:
				if ("play".equals(qName)) {
					try {
						AudioClip oAudio = Applet
								.newAudioClip(getClass().getClassLoader().getResource(text.toString()));
						oAudio.play();
					}
					catch (Exception fnfe) {
						//throw new ResourceNotFoundException( fnfe.getMessage() );
					}
					text = null;
				}
				break;
			case OUTPUT_TICKET:
				if ("image".equals(qName)) {
					text = null;
				}
				else if ("barcode".equals(qName)) {
					outputPrinter.printBarCode(bctype, bcposition, text.toString());
					text = null;
				}
				else if ("text".equals(qName)) {
					if (m_iTextLength > 0) {
						switch (m_iTextAlign) {
							case DevicePrinter.ALIGN_RIGHT:
								outputPrinter.printText(m_iTextStyle,
										DeviceTicket.alignRight(text.toString(), m_iTextLength));
								break;
							case DevicePrinter.ALIGN_CENTER:
								outputPrinter.printText(m_iTextStyle,
										DeviceTicket.alignCenter(text.toString(), m_iTextLength));
								break;
							default: // DevicePrinter.ALIGN_LEFT
								outputPrinter.printText(m_iTextStyle,
										DeviceTicket.alignLeft(text.toString(), m_iTextLength));
								break;
						}
					}
					else {
						outputPrinter.printText(m_iTextStyle, text.toString());
					}
					text = null;
				}
				else if ("line".equals(qName)) {
					outputPrinter.endLine();
				}
				else if ("ticket".equals(qName)) {
					outputPrinter.endReceipt();
					m_iOutputType = OUTPUT_NONE;
					outputPrinter = null;
				}
				break;
			case OUTPUT_DISPLAY:
				if ("line".equals(qName)) { // line 1 or 2 of the display
					if (m_sVisorLine1 == null) {
						m_sVisorLine1 = m_sVisorLine.toString();
					}
					else {
						m_sVisorLine2 = m_sVisorLine.toString();
					}
					m_sVisorLine = null;
				}
				else if ("line1".equals(qName)) { // linea 1 del visor
					m_sVisorLine1 = m_sVisorLine.toString();
					m_sVisorLine = null;
				}
				else if ("line2".equals(qName)) { // linea 2 del visor
					m_sVisorLine2 = m_sVisorLine.toString();
					m_sVisorLine = null;
				}
				else if ("text".equals(qName)) {
					if (m_iTextLength > 0) {
						switch (m_iTextAlign) {
							case DevicePrinter.ALIGN_RIGHT:
								m_sVisorLine.append(DeviceTicket.alignRight(text.toString(), m_iTextLength));
								break;
							case DevicePrinter.ALIGN_CENTER:
								m_sVisorLine.append(DeviceTicket.alignCenter(text.toString(), m_iTextLength));
								break;
							default: // DevicePrinter.ALIGN_LEFT
								m_sVisorLine.append(DeviceTicket.alignLeft(text.toString(), m_iTextLength));
								break;
						}
					}
					else {
						m_sVisorLine.append(text);
					}
					text = null;
				}
				else if ("display".equals(qName)) {
					m_iVisorAnimation = DeviceDisplayBase.ANIMATION_NULL;
					m_sVisorLine1 = null;
					m_sVisorLine2 = null;
					m_iOutputType = OUTPUT_NONE;
					outputPrinter = null;
				}
				break;
			case OUTPUT_FISCAL:
				break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (text != null) {
			text.append(ch, start, length);
		}
	}

	private int parseInt(String sValue, int iDefault) {
		try {
			return Integer.parseInt(sValue);
		}
		catch (NumberFormatException eNF) {
			return iDefault;
		}
	}

	private int parseInt(String sValue) {
		return parseInt(sValue, 0);
	}

	private double parseDouble(String sValue, double ddefault) {
		try {
			return Double.parseDouble(sValue);
		}
		catch (NumberFormatException eNF) {
			return ddefault;
		}
	}

	private double parseDouble(String sValue) {
		return parseDouble(sValue, 0.0);
	}
}
