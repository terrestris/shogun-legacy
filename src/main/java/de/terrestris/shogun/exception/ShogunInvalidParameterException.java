package de.terrestris.shogun.exception;

/**
 * An Exception which can be thrown if parameters of an webservice
 * are missing or invalid.
 *
 * @author terrestris GmbH & Co. KG
 */
public class ShogunInvalidParameterException extends ShogunException {

	/** to make compiler happy **/
	private static final long serialVersionUID = -2512496580612489307L;

	/**
	 * The default error message added to all instances
	 */
	private static final String INVALID_PARAM_TEXT = "Missing or invalid parameters. ";

	/**
	 *
	 */
	public ShogunInvalidParameterException() {
		super(INVALID_PARAM_TEXT);
	}

	/**
	 *
	 * @param message
	 */
	public ShogunInvalidParameterException(String message) {
		super(INVALID_PARAM_TEXT + message);
	}

	/**
	 *
	 * @param cause
	 */
	public ShogunInvalidParameterException(Throwable cause) {
		super(INVALID_PARAM_TEXT, cause);
	}

	/**
	 *
	 * @param message
	 * @param cause
	 */
	public ShogunInvalidParameterException(String message, Throwable cause) {
		super(INVALID_PARAM_TEXT + message, cause);
	}

}
