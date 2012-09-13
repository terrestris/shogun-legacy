package de.terrestris.shogun.jsonrequest;

import java.util.List;

/**
 * Class represents a filter as POJO Given by the client
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
public class Filter {

	/**
	 * List of filter criteria
	 */
	private List<FilterItem> filterItems = null;

	/**
	 * The logical operator to connect the filter items (AND/OR)
	 */
	private String logicalOperator = "AND";
	
	
	/**
	 * @param filterItem the filterItem to set
	 */
	public void setFilterItems(List<FilterItem> filterItems) {
		this.filterItems = filterItems;
	}
	
	/**
	 * @return the filterItems
	 */
	public List<FilterItem> getFilterItems() {
		return filterItems;
	}

	/**
	 * @param logicalOperator the logicalOperator to set
	 */
	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	/**
	 * @return the logicalOperator
	 */
	public String getLogicalOperator() {
		return logicalOperator;
	}
}