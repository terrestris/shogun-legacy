package de.terrestris.shogun.jsonrequest;

/**
 * POJO for a paging object in order to 
 * deserialze the corresponding JSON object
 * 
 * @author terrestris GmbH & Co. KG
 *
 */
public class Paging {
	
	/**
	 * 
	 */
	String start;
	
	/**
	 * 
	 */
	String limit;

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}
	
	

}
