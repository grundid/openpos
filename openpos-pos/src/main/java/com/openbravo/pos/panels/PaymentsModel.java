//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.panels;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.StringUtils;

/**
 * 
 * @author adrianromero
 */
public class PaymentsModel {

	private String m_sHost;
	private int m_iSeq;
	private Date m_dDateStart;
	private Date m_dDateEnd;
	private Integer m_iPayments;
	private Double m_dPaymentsTotal;
	private java.util.List<PaymentsLine> m_lpayments;
	private final static String[] PAYMENTHEADERS = { "Label.Payment", "label.totalcash" };
	private Integer m_iSales;
	private Double m_dSalesBase;
	private Double m_dSalesTaxes;
	private java.util.List<SalesLine> m_lsales;
	private final static String[] SALEHEADERS = { "label.taxcash", "label.totalcash", "label.totalnet" };

	private PaymentsModel() {
	}

	public static PaymentsModel emptyInstance() {
		PaymentsModel p = new PaymentsModel();
		p.m_iPayments = new Integer(0);
		p.m_dPaymentsTotal = new Double(0.0);
		p.m_lpayments = new ArrayList<PaymentsLine>();
		p.m_iSales = null;
		p.m_dSalesBase = null;
		p.m_dSalesTaxes = null;
		p.m_lsales = new ArrayList<SalesLine>();
		return p;
	}

	public static PaymentsModel getInstance(String host, Date startDate, Date endDate) {
		PaymentsModel p = new PaymentsModel();
		p.m_sHost = host;
		p.m_dDateStart = startDate;
		p.m_dDateEnd = endDate;
		p.m_iPayments = new Integer(0);
		p.m_dPaymentsTotal = new Double(0.0);
		p.m_lpayments = new ArrayList<PaymentsLine>();
		p.m_iSales = null;
		p.m_dSalesBase = null;
		p.m_dSalesTaxes = null;
		p.m_lsales = new ArrayList<SalesLine>();
		return p;
	}

	public static PaymentsModel loadInstance(AppView app) throws BasicException {
		PaymentsModel p = new PaymentsModel();
		// Propiedades globales
		p.m_sHost = app.getProperties().getHost();
		p.m_iSeq = app.getActiveCashSequence();
		p.m_dDateStart = app.getActiveCashDateStart();
		p.m_dDateEnd = null;
		loadDataFromDatabaseIntoModel(app.getSession(), app.getActiveCashIndex(), p);
		return p;
	}

	public static PaymentsModel loadInstance(AppView app, Date startDate, Date endDate) throws BasicException {
		PaymentsModel p = new PaymentsModel();
		p.m_sHost = app.getProperties().getHost();
		p.m_iSeq = app.getActiveCashSequence();
		p.m_dDateStart = startDate;
		p.m_dDateEnd = endDate;
		loadDataFromDatabaseIntoModel(app.getSession(), app.getActiveCashIndex(), p, startDate, endDate);
		return p;
	}

	public static void sumModels(PaymentsModel finalPaymentsModel, PaymentsModel monthPaymentsModel) {
		finalPaymentsModel.m_iPayments = sumNullableInteger(finalPaymentsModel.m_iPayments,
				monthPaymentsModel.m_iPayments);
		finalPaymentsModel.m_dPaymentsTotal = sumNullableDouble(finalPaymentsModel.m_dPaymentsTotal,
				monthPaymentsModel.m_dPaymentsTotal);
		finalPaymentsModel.m_iSales = sumNullableInteger(finalPaymentsModel.m_iSales, monthPaymentsModel.m_iSales);
		finalPaymentsModel.m_dSalesBase = sumNullableDouble(finalPaymentsModel.m_dSalesBase,
				monthPaymentsModel.m_dSalesBase);
		finalPaymentsModel.m_dSalesTaxes = sumNullableDouble(finalPaymentsModel.m_dSalesTaxes,
				monthPaymentsModel.m_dSalesTaxes);
		sumPayments(finalPaymentsModel, monthPaymentsModel);
		sumTaxes(finalPaymentsModel, monthPaymentsModel);
	}

	private static void sumTaxes(PaymentsModel finalPaymentsModel, PaymentsModel monthPaymentsModel) {
		for (SalesLine salesLine : finalPaymentsModel.m_lsales) {
			for (Iterator<SalesLine> it = monthPaymentsModel.m_lsales.iterator(); it.hasNext();) {
				SalesLine monthPaymentsLine = it.next();
				if (salesLine.m_SalesTaxName.equals(monthPaymentsLine.m_SalesTaxName)) {
					salesLine.m_SalesTaxes = sumNullableDouble(salesLine.m_SalesTaxes, monthPaymentsLine.m_SalesTaxes);
					it.remove();
				}
			}
		}
		for (SalesLine salesLine : monthPaymentsModel.m_lsales)
			finalPaymentsModel.m_lsales.add(salesLine);
	}

	private static void sumPayments(PaymentsModel finalPaymentsModel, PaymentsModel monthPaymentsModel) {
		for (PaymentsLine paymentsLine : finalPaymentsModel.m_lpayments) {
			for (Iterator<PaymentsLine> it = monthPaymentsModel.m_lpayments.iterator(); it.hasNext();) {
				PaymentsLine monthPaymentsLine = it.next();
				if (paymentsLine.m_PaymentType.equals(monthPaymentsLine.m_PaymentType)) {
					paymentsLine.m_PaymentValue = sumNullableDouble(paymentsLine.m_PaymentValue,
							monthPaymentsLine.m_PaymentValue);
					it.remove();
				}
			}
		}
		for (PaymentsLine paymentsLine : monthPaymentsModel.m_lpayments)
			finalPaymentsModel.m_lpayments.add(paymentsLine);
	}

	private static Integer sumNullableInteger(Integer i1, Integer i2) {
		if (i1 == null)
			return i2;
		if (i2 == null)
			return i1;
		return Integer.valueOf(i1.intValue() + i2.intValue());
	}

	private static Double sumNullableDouble(Double d1, Double d2) {
		if (d1 == null)
			return d2;
		if (d2 == null)
			return d1;
		return Double.valueOf(d1.doubleValue() + d2.doubleValue());
	}

	@SuppressWarnings("unchecked")
	public static void loadDataFromDatabaseIntoModel(Session session, String activeCashIndex, PaymentsModel p)
			throws BasicException {
		// Pagos
		Object[] valtickets = (Object[])new StaticSentence(session, "SELECT COUNT(*), SUM(PAYMENTS.TOTAL) "
				+ "FROM PAYMENTS, RECEIPTS " + "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ?",
				SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] { Datas.INT, Datas.DOUBLE }))
				.find(activeCashIndex);
		if (valtickets == null) {
			p.m_iPayments = new Integer(0);
			p.m_dPaymentsTotal = new Double(0.0);
		}
		else {
			p.m_iPayments = (Integer)valtickets[0];
			p.m_dPaymentsTotal = (Double)valtickets[1];
		}
		List<PaymentsLine> l = new StaticSentence(session, "SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL) "
				+ "FROM PAYMENTS, RECEIPTS " + "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ? "
				+ "GROUP BY PAYMENTS.PAYMENT", SerializerWriteString.INSTANCE, new SerializerReadClass(
				PaymentsModel.PaymentsLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
				.list(activeCashIndex);
		if (l == null) {
			p.m_lpayments = new ArrayList<PaymentsLine>();
		}
		else {
			p.m_lpayments = l;
		}
		// Sales
		Object[] recsales = (Object[])new StaticSentence(session,
				"SELECT COUNT(DISTINCT RECEIPTS.ID), SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) "
						+ "FROM RECEIPTS, TICKETLINES WHERE RECEIPTS.ID = TICKETLINES.TICKET AND RECEIPTS.MONEY = ?",
				SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] { Datas.INT, Datas.DOUBLE }))
				.find(activeCashIndex);
		if (recsales == null) {
			p.m_iSales = null;
			p.m_dSalesBase = null;
		}
		else {
			p.m_iSales = (Integer)recsales[0];
			p.m_dSalesBase = (Double)recsales[1];
		}
		// Taxes
		Object[] rectaxes = (Object[])new StaticSentence(session, "SELECT SUM(TAXLINES.AMOUNT) "
				+ "FROM RECEIPTS, TAXLINES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ?",
				SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] { Datas.DOUBLE }))
				.find(activeCashIndex);
		if (rectaxes == null) {
			p.m_dSalesTaxes = null;
		}
		else {
			p.m_dSalesTaxes = (Double)rectaxes[0];
		}
		List<SalesLine> asales = new StaticSentence(
				session,
				"SELECT TAXCATEGORIES.NAME, SUM(TAXLINES.AMOUNT), TAXES.RATE "
						+ "FROM RECEIPTS, TAXLINES, TAXES, TAXCATEGORIES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID "
						+ "AND RECEIPTS.MONEY = ?" + " GROUP BY TAXCATEGORIES.NAME", SerializerWriteString.INSTANCE,
				new SerializerReadClass(PaymentsModel.SalesLine.class)).list(activeCashIndex);
		if (asales == null) {
			p.m_lsales = new ArrayList<SalesLine>();
		}
		else {
			p.m_lsales = asales;
		}
	}

	@SuppressWarnings("unchecked")
	public static void loadDataFromDatabaseIntoModel(Session session, String activeCashIndex, PaymentsModel p,
			Date startDate, Date endDate) throws BasicException {
		SerializerWrite<Object[]> serializerWrite = new SerializerWriteBasic(new Datas[] { Datas.STRING,
				Datas.TIMESTAMP, Datas.TIMESTAMP });
		Object[] params = { activeCashIndex, startDate, endDate };
		String whereStatement = "RECEIPTS.MONEY = ? AND DATENEW >= ? AND DATENEW < ?";
		Object[] valtickets = (Object[])new StaticSentence(session, "SELECT COUNT(*), SUM(PAYMENTS.TOTAL) "
				+ "FROM PAYMENTS, RECEIPTS " + "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND " + whereStatement,
				serializerWrite, new SerializerReadBasic(new Datas[] { Datas.INT, Datas.DOUBLE })).find(params);
		if (valtickets == null) {
			p.m_iPayments = new Integer(0);
			p.m_dPaymentsTotal = new Double(0.0);
		}
		else {
			p.m_iPayments = (Integer)valtickets[0];
			p.m_dPaymentsTotal = (Double)valtickets[1];
		}
		List<PaymentsLine> l = new StaticSentence(session, "SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL) "
				+ "FROM PAYMENTS, RECEIPTS " + "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND " + whereStatement
				+ " GROUP BY PAYMENTS.PAYMENT", serializerWrite, new SerializerReadClass(
				PaymentsModel.PaymentsLine.class)) //new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.DOUBLE}))
				.list(params);
		if (l == null) {
			p.m_lpayments = new ArrayList<PaymentsLine>();
		}
		else {
			p.m_lpayments = l;
		}
		// Sales
		Object[] recsales = (Object[])new StaticSentence(session,
				"SELECT COUNT(DISTINCT RECEIPTS.ID), SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) "
						+ "FROM RECEIPTS, TICKETLINES WHERE RECEIPTS.ID = TICKETLINES.TICKET AND " + whereStatement,
				serializerWrite, new SerializerReadBasic(new Datas[] { Datas.INT, Datas.DOUBLE })).find(params);
		if (recsales == null) {
			p.m_iSales = null;
			p.m_dSalesBase = null;
		}
		else {
			p.m_iSales = (Integer)recsales[0];
			p.m_dSalesBase = (Double)recsales[1];
		}
		// Taxes
		Object[] rectaxes = (Object[])new StaticSentence(session, "SELECT SUM(TAXLINES.AMOUNT) "
				+ "FROM RECEIPTS, TAXLINES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND " + whereStatement,
				serializerWrite, new SerializerReadBasic(new Datas[] { Datas.DOUBLE })).find(params);
		if (rectaxes == null) {
			p.m_dSalesTaxes = null;
		}
		else {
			p.m_dSalesTaxes = (Double)rectaxes[0];
		}
		List<SalesLine> asales = new StaticSentence(
				session,
				"SELECT TAXCATEGORIES.NAME, SUM(TAXLINES.AMOUNT), TAXES.RATE "
						+ "FROM RECEIPTS, TAXLINES, TAXES, TAXCATEGORIES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID "
						+ "AND " + whereStatement + " GROUP BY TAXCATEGORIES.NAME", serializerWrite,
				new SerializerReadClass(PaymentsModel.SalesLine.class)).list(params);
		if (asales == null) {
			p.m_lsales = new ArrayList<SalesLine>();
		}
		else {
			p.m_lsales = asales;
		}
	}

	public int getPayments() {
		return m_iPayments.intValue();
	}

	public double getTotal() {
		return m_dPaymentsTotal.doubleValue();
	}

	public String getHost() {
		return m_sHost;
	}

	public int getSequence() {
		return m_iSeq;
	}

	public Date getDateStart() {
		return m_dDateStart;
	}

	public void setDateEnd(Date dValue) {
		m_dDateEnd = dValue;
	}

	public Date getDateEnd() {
		return m_dDateEnd;
	}

	public String printHost() {
		return StringUtils.encodeXML(m_sHost);
	}

	public String printSequence() {
		return Formats.INT.formatValue(m_iSeq);
	}

	public String printDateStart() {
		return Formats.TIMESTAMP.formatValue(m_dDateStart);
	}

	public String printDateEnd() {
		return Formats.TIMESTAMP.formatValue(m_dDateEnd);
	}

	public String printPayments() {
		return Formats.INT.formatValue(m_iPayments);
	}

	public String printPaymentsTotal() {
		return Formats.CURRENCY.formatValue(m_dPaymentsTotal);
	}

	public List<PaymentsLine> getPaymentLines() {
		return m_lpayments;
	}

	public int getSales() {
		return m_iSales == null ? 0 : m_iSales.intValue();
	}

	public String printSales() {
		return Formats.INT.formatValue(m_iSales);
	}

	public String printSalesBase() {
		return Formats.CURRENCY.formatValue(m_dSalesBase);
	}

	public String printSalesTaxes() {
		return Formats.CURRENCY.formatValue(m_dSalesTaxes);
	}

	public String printSalesTotal() {
		return Formats.CURRENCY.formatValue((m_dSalesBase == null || m_dSalesTaxes == null) ? null : m_dSalesBase
				+ m_dSalesTaxes);
	}

	public List<SalesLine> getSaleLines() {
		return m_lsales;
	}

	public AbstractTableModel getPaymentsModel() {
		return new AbstractTableModel() {

			@Override
			public String getColumnName(int column) {
				return AppLocal.getIntString(PAYMENTHEADERS[column]);
			}

			@Override
			public int getRowCount() {
				return m_lpayments.size();
			}

			@Override
			public int getColumnCount() {
				return PAYMENTHEADERS.length;
			}

			@Override
			public Object getValueAt(int row, int column) {
				PaymentsLine l = m_lpayments.get(row);
				switch (column) {
					case 0:
						return l.getType();
					case 1:
						return l.getValue();
					default:
						return null;
				}
			}
		};
	}

	public static class SalesLine implements SerializableRead {

		private String m_SalesTaxName;
		private Double m_SalesTaxes;
		private Double taxRate;

		@Override
		public void readValues(DataRead dr) throws BasicException {
			m_SalesTaxName = dr.getString(1);
			m_SalesTaxes = dr.getDouble(2);
			taxRate = dr.getDouble(3);
		}

		public String printTaxName() {
			return m_SalesTaxName;
		}

		public String printTaxes() {
			return Formats.CURRENCY.formatValue(m_SalesTaxes);
		}

		public String printNetValue() {
			return Formats.CURRENCY.formatValue(getNetValue());
		}

		public Double getNetValue() {
			return (m_SalesTaxes / (taxRate * 100)) * 100;
		}

		public String getTaxName() {
			return m_SalesTaxName;
		}

		public Double getTaxes() {
			return m_SalesTaxes;
		}
	}

	public AbstractTableModel getSalesModel() {
		return new AbstractTableModel() {

			@Override
			public String getColumnName(int column) {
				return AppLocal.getIntString(SALEHEADERS[column]);
			}

			@Override
			public int getRowCount() {
				return m_lsales.size();
			}

			@Override
			public int getColumnCount() {
				return SALEHEADERS.length;
			}

			@Override
			public Object getValueAt(int row, int column) {
				SalesLine l = m_lsales.get(row);
				switch (column) {
					case 0:
						return l.getTaxName();
					case 1:
						return l.getTaxes();
					case 2:
						return l.getNetValue();
					default:
						return null;
				}
			}
		};
	}

	public static class PaymentsLine implements SerializableRead {

		private String m_PaymentType;
		private Double m_PaymentValue;

		@Override
		public void readValues(DataRead dr) throws BasicException {
			m_PaymentType = dr.getString(1);
			m_PaymentValue = dr.getDouble(2);
		}

		public String printType() {
			return AppLocal.getIntString("transpayment." + m_PaymentType);
		}

		public String getType() {
			return m_PaymentType;
		}

		public String printValue() {
			return Formats.CURRENCY.formatValue(m_PaymentValue);
		}

		public Double getValue() {
			return m_PaymentValue;
		}
	}
}