package de.terrestris.shogun.exception;

/**
 * A generic SHOGun exception class
 * At the moment no extended functionality to {@link Exception}
 *
 * @author terrestris GmbH & Co. KG
 */
public class ShogunException extends Exception {

	/** to make compiler happy **/
	private static final long serialVersionUID = 1243539651674146607L;

	/**
	 *
	 */
	public ShogunException() {}

	/**
	 * @param message
	 */
	public ShogunException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ShogunException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ShogunException(String message, Throwable cause) {
		super(message, cause);
	}
}
