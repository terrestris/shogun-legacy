package de.terrestris.shogun.util;

import java.util.Random;

/**
 * Helper class for password related issues
 *
 * @author terrestris GmbH & Co. KG
 */
public class Password {

	/**
	 * A set of characters to build passwords
	 */
	private static final String PW_CHARSET = "_!0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * Helper method to create a random password
	 * 
	 * @param length the length of the password
	 * @return the new password
	 */
	public static String getRandomPassword(int length) {

		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(PW_CHARSET.length());
			sb.append(PW_CHARSET.charAt(pos));
		}
		return sb.toString();
	}
}
