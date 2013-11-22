package org.openpos.reports.closemonth;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.panels.PaymentsModel;
import com.openbravo.pos.panels.PaymentsModel.SalesLine;

public class PaymentsModelLoader {

	private Logger log = Logger.getLogger(PaymentsModelLoader.class.getName());

	private int month;
	private int year;
	private Calendar startDate;
	private Calendar endDate;
	private Session session;
	private PaymentsModel paymentsModel;

	public PaymentsModelLoader(Session session, int month, int year) throws BasicException {
		this.session = session;
		this.month = month;
		this.year = year;
		initDates();
		paymentsModel = PaymentsModel.getInstance("", startDate.getTime(), endDate.getTime());
		loadMonthlyCloseCash();
		filterZeroValueTaxes();
	}

	private void filterZeroValueTaxes() {
		for (Iterator<SalesLine> it = paymentsModel.getSaleLines().iterator(); it.hasNext();) {
			if (it.next().getTaxes().doubleValue() == 0)
				it.remove();
		}
	}

	public PaymentsModel getPaymentsModel() throws BasicException {
		return paymentsModel;
	}

	private void loadMonthlyCloseCash() throws BasicException {
		long time = System.currentTimeMillis();
		for (Object[] activeCashIndex : getActiveCashIndexes()) {
			PaymentsModel monthPaymentsModel = PaymentsModel.emptyInstance();
			PaymentsModel.loadDataFromDatabaseIntoModel(session, (String)activeCashIndex[0], monthPaymentsModel);
			PaymentsModel.sumModels(paymentsModel, monthPaymentsModel);
		}
		log.info("Time needed: " + (System.currentTimeMillis() - time));

	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getActiveCashIndexes() throws BasicException {
		return new StaticSentence(session, "SELECT MONEY FROM CLOSEDCASH WHERE DATEEND >= ? AND DATEEND < ?",
				new SerializerWriteBasic(new Datas[] { Datas.TIMESTAMP, Datas.TIMESTAMP }), new SerializerReadBasic(
						new Datas[] { Datas.STRING })).list(startDate.getTime(), endDate.getTime());
	}

	private void initDates() {
		startDate = Calendar.getInstance();
		startDate.set(year, month, 1, 0, 0, 0);
		endDate = (Calendar)startDate.clone();
		endDate.add(Calendar.MONTH, 1);
	}
}
