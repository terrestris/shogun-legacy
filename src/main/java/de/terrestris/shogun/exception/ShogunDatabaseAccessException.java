package de.terrestris.shogun.exception;

/**
 * Common exception class for database access errors
 */
public class ShogunDatabaseAccessException extends Exception {

	/**
	 * the serial version UID
	 */
	private static final long serialVersionUID = 8838422708926053728L;

	/**
	 * the standard constructor
	 */
	public ShogunDatabaseAccessException() {
		super();
	}

	/**
	 * Constructor handling a error message
	 * @param msg the error message describing this exception
	 */
	public ShogunDatabaseAccessException(String msg) {
		super(msg);
	}

	/**
	 * Constructor handling a throwable, e.g. a nested exception
	 * @param throwable
	 */
	public ShogunDatabaseAccessException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor handling an error message and a throwable
	 * 
	 * @param msg
	 * @param throwable
	 */
	public ShogunDatabaseAccessException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
