package de.terrestris.shogun.jsonrequest;


/**
 * Class represents a sort criteria as POJO
 * Given by the client
 *
 * @author terrestris GmbH & Co. KG
 *
 */
public class SortItem {

	/**
	 * Field which should be used as sort criteria
	 */
	private String sort;

	/**
	 * Sort direction ASC or DESC
	 */
	private String dir;


	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

}