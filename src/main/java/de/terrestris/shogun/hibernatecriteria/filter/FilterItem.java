package de.terrestris.shogun.hibernatecriteria.filter;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * This class represents a filter item
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
public class FilterItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4913019526806074963L;

	/**
	 * 
	 */
	private String fieldName;

	/**
	 * 
	 */
	private Operator operator;

	/**
	 * 
	 */
	private List<FilterOperand> operands = new Vector<FilterOperand>();
	
	
	/**
	 * 
	 */
	public enum Operator {
		
		Equals,
		Smaller, 
		Greater, 
		SmallerEq, 
		GreaterEq, 
		Like,
		ILike, 
		Between, 
		Any, 
		NotNull, 
		In, 
		NotEquals, 
		NotLike, 
		IsNull,
		DWithin
	}


	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}


	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}


	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}


	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}


	/**
	 * @return the operands
	 */
	public List<FilterOperand> getOperands() {
		return operands;
	}


	/**
	 * @param operands the operands to set
	 */
	public void setOperands(List<FilterOperand> operands) {
		this.operands = operands;
	}
	
}
