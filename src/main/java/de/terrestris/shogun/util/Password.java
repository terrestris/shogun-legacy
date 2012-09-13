/**
 * 
 */
package de.terrestris.shogun.util;

import java.util.Random;

/**
 * Helper class for password related issues
 */
public class Password {
	
	/**
	 * the charset of which the PW consists of
	 */
	private static final String CHARSET = "!0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	
	/**
	 * Helper method to create a random password
	 * 
	 * @param length the length of the password
	 * @return the new password
	 */
	public static String getRandomPassword(int length) {
		
		String charset = CHARSET;

		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}

}
