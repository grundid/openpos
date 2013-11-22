package com.openbravo.pos.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashcypherNew {

	public static boolean authenticate(String sPassword, String sHashPassword) {
		if (sHashPassword == null || sHashPassword.equals("") || sHashPassword.startsWith("empty:")) {
			return sPassword == null || sPassword.equals("");
		}
		else if (sHashPassword.startsWith("sha1:")) {
			return sHashPassword.equals(hashString(sPassword));
		}
		else if (sHashPassword.startsWith("plain:")) {
			return sHashPassword.equals("plain:" + sPassword);
		}
		else {
			return sHashPassword.equals(sPassword);
		}
	}

	public static String hashString(String sPassword) {

		if (sPassword == null || sPassword.equals("")) {
			return "empty:";
		}
		else {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(sPassword.getBytes("UTF-8"));
				byte[] res = md.digest();
				return "sha1:" + StringUtils.byte2hex(res);
			}
			catch (NoSuchAlgorithmException e) {
				return "plain:" + sPassword;
			}
			catch (UnsupportedEncodingException e) {
				return "plain:" + sPassword;
			}
		}
	}

}
