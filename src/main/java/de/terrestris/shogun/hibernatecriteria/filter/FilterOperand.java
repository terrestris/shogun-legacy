package de.terrestris.shogun.hibernatecriteria.filter;

/**
 *
 */
public class FilterOperand {

	/**
	 *
	 */
	private Object operand;

	/**
	 *
	 * @param operand
	 */
	public FilterOperand(Object operand) {
		this.operand = operand;
	}

	/**
	 *
	 * @return
	 */
	public Object getOperand() {
		return operand;
	}

	/**
	 *
	 * @param operand
	 */
	public void setOperand(Object operand) {
		this.operand = operand;
	}

}
