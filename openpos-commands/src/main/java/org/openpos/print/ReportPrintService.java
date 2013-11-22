package org.openpos.print;

import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;

import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;

public class ReportPrintService {

	private TicketParser ticketParser;
	private JComponent component;
	private String scriptResource;

	public ReportPrintService(TicketParser ticketParser, JComponent component, String scriptResource) {
		this.ticketParser = ticketParser;
		this.component = component;
		this.scriptResource = scriptResource;
	}

	public void print(Map<String, Object> environment) {
		try {
			ScriptEngine scriptEngine = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
			for (Entry<String, Object> entry : environment.entrySet()) {
				scriptEngine.put(entry.getKey(), entry.getValue());
			}
			String evaluatedReport = scriptEngine.eval(scriptResource).toString();
			ticketParser.printTicket(evaluatedReport);
		}
		catch (ScriptException e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"),
					e);
			msg.show(component);
		}
		catch (TicketPrinterException e) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"),
					e);
			msg.show(component);
		}
	}
}
