package de.terrestris.shogun.hibernatecriteria.filter;

import java.util.List;
import java.util.Vector;

/**
 * Class defining the filter for a query
 * Has one or more filter items (criteria)
 *
 * @author terrestris GmbH & Co. KG
 *
 */
public class Filter {

	/**
	 *
	 */
	public enum LogicalOperator {
		AND,
		OR
	}

	/**
	 *
	 */
	private List<FilterItem> filterItems = new Vector<FilterItem>();

	/**
	 *
	 */
	private LogicalOperator logicalOperator = LogicalOperator.AND;

	/**
	 *
	 * @param filterItem
	 */
	public void addFilterItem(FilterItem filterItem) {
		this.filterItems.add(filterItem);
	}

	/**
	 * @return the logicalOperator
	 */
	public LogicalOperator getLogicalOperator() {
		return logicalOperator;
	}

	/**
	 * @param logicalOperator the logicalOperator to set
	 */
	public void setLogicalOperator(LogicalOperator logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public FilterItem getFilterItem(int index) {
		return filterItems.get(index);
	}

	/**
	 *
	 * @return
	 */
	public int getFilterItemCount() {
		return filterItems.size();
	}

}