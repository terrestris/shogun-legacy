package de.terrestris.shogun.exception;

/**
 * The class handling errors at the service level.
 *
 * @author terrestris GmbH & Co. KG
 */
public class ShogunServiceException extends ShogunException {

	/**
	 *
	 */
	private static final long serialVersionUID = 8838422708926053728L;

	/**
	 *
	 */
	public ShogunServiceException() {
		super();
	}

	/**
	 *
	 * @param msg
	 */
	public ShogunServiceException(String msg) {
		super(msg);
	}

	/**
	 * @param throwable
	 */
	public ShogunServiceException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * @param msg
	 * @param throwable
	 */
	public ShogunServiceException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
