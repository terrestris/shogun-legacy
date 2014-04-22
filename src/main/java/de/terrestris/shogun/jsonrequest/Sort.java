package de.terrestris.shogun.jsonrequest;

import java.util.List;

/**
 * Class represents a set of sort items as POJO
 * Given by the client
 *
 * @author terrestris GmbH & Co. KG
 *
 */
public class Sort {

	List<SortItem> sortItems = null;

	/**
	 * @return the sortItems
	 */
	public List<SortItem> getSortItems() {
		return sortItems;
	}

	/**
	 * @param sortItems the sortItems to set
	 */
	public void setSortItems(List<SortItem> sortItems) {
		this.sortItems = sortItems;
	}

}
