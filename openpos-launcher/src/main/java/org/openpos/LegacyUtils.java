package org.openpos;

import com.openbravo.pos.util.AltEncrypter;

public class LegacyUtils {

	public static String decryptPassword(String user, String password) {
		if (password.startsWith("crypt:")) {
			AltEncrypter cypher = new AltEncrypter("cypherkey" + user);
			password = cypher.decrypt(password.substring(6));
		}
		return password;
	}
}
