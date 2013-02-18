package de.terrestris.shogun.exception;

/**
 * The Exception class for handling parsing errors.
 *
 * @author terrestris GmbH & Co. KG
 */
public class ShogunParseException extends ShogunException {

	/**
	 *
	 */
	private static final long serialVersionUID = 8506603569783487676L;

	/**
	 *
	 */
	public ShogunParseException() {
		super("Unspecified parsing error");
	}

	/**
	 *
	 * @param msg
	 */
	public ShogunParseException(String msg) {
		super(msg);
	}

	/**
	 *
	 * @param e
	 */
	public ShogunParseException(Throwable e) {
		super("Unspecified parsing error", e);
	}

	/**
	 *
	 * @param msg
	 * @param e
	 */
	public ShogunParseException(String msg, Throwable e) {
		super(msg, e);
	}

}
