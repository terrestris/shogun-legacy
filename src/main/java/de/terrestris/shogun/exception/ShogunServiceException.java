/**
 * 
 */
package de.terrestris.shogun.exception;

/**
 *
 */
public class ShogunServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8838422708926053728L;

	/**
	 * 
	 */
	public ShogunServiceException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ShogunServiceException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public ShogunServiceException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 * @param throwable
	 */
	public ShogunServiceException(String msg, Throwable throwable) {
		super(msg, throwable);
		// TODO Auto-generated constructor stub
	}

}
