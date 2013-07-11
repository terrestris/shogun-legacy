package de.terrestris.shogun.jsonrequest;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Class represents a complex request to the backend with filter, sort criteria
 * etc...
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
@JsonAutoDetect
public class Request {

	/**
	 * The model class-name which is requested
	 */
	private String object_type;
	
	/**
	 * Filter conditions
	 */
	private Filter filter;
	
	/**
	 * 
	 */
	private Set<String> fields;
	
	/**
	 * Additional Filter conditions that are added as global conjunction. 
	 * 
	 * This is only a intermediate solution.
	 */
	private Filter globalAndFilter;

	/**
	 * Sort criteria
	 */
	private Sort sortObject;
	
	/**
	 * 
	 */
	private Paging pagingObject;

	public Request() {
	}

	public void finalize() throws Throwable {
	}

	

	public String getObject_type() {
		return object_type;
	}

	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}

	/**
	 * 
	 * @return filter
	 */
	 public Filter getFilter() {
		 return filter;
	 }

	/**
	 * 
	 * @param filter
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	
	/**
	 * @return the fields
	 */
	public Set<String> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Set<String> fields) {
		this.fields = fields;
	}

	/**
	 * @return the globalAndFilter
	 */
	public Filter getGlobalAndFilter() {
		return globalAndFilter;
	}

	/**
	 * @param globalAndFilter the globalAndFilter to set
	 */
	public void setGlobalAndFilter(Filter globalAndFilter) {
		this.globalAndFilter = globalAndFilter;
	}

	/**
	 * @return the sortObject
	 */
	public Sort getSortObject() {
		return sortObject;
	}

	/**
	 * @param sortObject the sortObject to set
	 */
	public void setSortObject(Sort sortObject) {
		this.sortObject = sortObject;
	}

	/**
	 * @return the pagingObject
	 */
	public Paging getPagingObject() {
		return pagingObject;
	}

	/**
	 * @param pagingObject the pagingObject to set
	 */
	public void setPagingObject(Paging pagingObject) {
		this.pagingObject = pagingObject;
	}
	
	

}