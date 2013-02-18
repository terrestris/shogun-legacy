package de.terrestris.shogun.exception;

/**
 * The database Access Exception class.
 *
 * @author terrestris GmbH & Co. KG
 */
public class ShogunDatabaseAccessException extends ShogunException {

	/**
	 *
	 */
	private static final long serialVersionUID = 8838422708926053728L;

	/**
	 *
	 */
	public ShogunDatabaseAccessException() {}

	/**
	 * @param msg
	 */
	public ShogunDatabaseAccessException(String msg) {
		super(msg);
	}

	/**
	 * @param throwable
	 */
	public ShogunDatabaseAccessException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * @param msg
	 * @param throwable
	 */
	public ShogunDatabaseAccessException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
