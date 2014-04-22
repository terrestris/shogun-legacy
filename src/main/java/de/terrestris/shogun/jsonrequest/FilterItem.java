package de.terrestris.shogun.jsonrequest;

/**
 * Class represents a filter criteria as POJO Given by the client
 *
 * @author terrestris GmbH & Co. KG
 *
 */
public class FilterItem {

	/**
	 * Field which should be filtered, e.g. 'id'
	 */
	private String fieldName;

	/**
	 * Operator of the filter criteria, e.g. 'Smaller' oder 'ILike'
	 */
	private String operator;

	/**
	 * Operands of the filter criteria, e.g. 12.0 Is an Array, so also [12.0.
	 * 3.0] is allowed
	 */
	private String[] operands;

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the operands
	 */
	public String[] getOperands() {
		return operands;
	}

	/**
	 * @param operands
	 *            the operands to set
	 */
	public void setOperands(String[] operands) {
		this.operands = operands;
	}

}