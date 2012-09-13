/**
 * 
 */
package de.terrestris.shogun.exception;

/**
 *
 */
public class ShogunDatabaseAccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8838422708926053728L;

	/**
	 * 
	 */
	public ShogunDatabaseAccessException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ShogunDatabaseAccessException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public ShogunDatabaseAccessException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 * @param throwable
	 */
	public ShogunDatabaseAccessException(String msg, Throwable throwable) {
		super(msg, throwable);
		// TODO Auto-generated constructor stub
	}

}
