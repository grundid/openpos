package com.openbravo.pos.forms;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Properties;

import com.openbravo.basic.BasicException;

public interface IDataLogicSystem {

	String findVersion() throws BasicException;

	List listPeopleVisible() throws BasicException;

	AppUser findPeopleByCard(String card) throws BasicException;

	String findRolePermissions(String sRole);

	byte[] getResourceAsBinary(String sName);

	String getResourceAsText(String sName);

	String getResourceAsXML(String sName);

	BufferedImage getResourceAsImage(String sName);

	Properties getResourceAsProperties(String sName);

	int getSequenceCash(String host) throws BasicException;

	Object[] findActiveCash(String sActiveCashIndex) throws BasicException;

	String findLocationName(String iLocation) throws BasicException;

}