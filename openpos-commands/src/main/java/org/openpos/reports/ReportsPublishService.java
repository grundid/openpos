package org.openpos.reports;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openpos.reports.email.EMailTicketParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.printer.TicketPrinterException;

@Service
public class ReportsPublishService implements IReportsPublishService {

	@Autowired
	private MailSender mailSender;
	@Autowired
	private AppConfig appConfig;

	@Override
	public void sendDayEndReport(final String evaluatedReport) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String message = convertToMessage(evaluatedReport);
				File file = new File("C:\\");
				long freeSpace = file.getFreeSpace();
				DecimalFormat df = new DecimalFormat("#,##0.00");
				String freeSpaceMessage = "\n\nFreier Speicherplatz: " + df.format((double)freeSpace / 1024) + " KB";
				message += freeSpaceMessage;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				sendMessage("Tagesabschluss " + sdf.format(new Date()), message);
			}
		}).start();
	}

	@Override
	public void sendMonthlyCashReport(int month, int year, String evaluatedReport) {
		String message = convertToMessage(evaluatedReport);
		sendMessage("Monatsabschluss " + formatMonthYear(month, year), message);
	}

	@Override
	public void sendMonthlyTimeReport(int month, int year, List<String> evaluatedReports) {
		StringBuilder message = new StringBuilder();
		for (String string : evaluatedReports) {
			message.append(convertToMessage(string));
			message.append('\n');
		}
		sendMessage("Zeiterfassung " + formatMonthYear(month, year), message.toString());
	}

	private String formatMonthYear(int month, int year) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month);
		c.set(Calendar.YEAR, year);
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
		return sdf.format(c.getTime());
	}

	private void sendMessage(String subject, String message) {
		if (appConfig.getProperty("mail.sendreports").equalsIgnoreCase("true")) {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(appConfig.getProperty("mail.to").split(","));
			mailMessage.setFrom(appConfig.getProperty("mail.from"));
			mailMessage.setSubject(subject);
			mailMessage.setText(message.toString());
			mailSender.send(mailMessage);
		}
	}

	private String convertToMessage(String evaluatedReport) {
		try {
			PlainTextPrinter printer = new PlainTextPrinter();
			EMailTicketParser ticketParser = new EMailTicketParser(printer);
			ticketParser.printTicket(evaluatedReport);
			return printer.getPlainText();
		}
		catch (TicketPrinterException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
