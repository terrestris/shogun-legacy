/**
 * 
 */
package de.terrestris.shogun.jsonmodel;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * The POJO for a list of entities.
 * 
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
public class DeleteIdList {
	
	List<Integer> deleteIds;

	/**
	 * @return the deleteIds
	 */
	public List<Integer> getDeleteIds() {
		return deleteIds;
	}

	/**
	 * @param deleteIds the deleteIds to set
	 */
	public void setDeleteIds(List<Integer> deleteIds) {
		this.deleteIds = deleteIds;
	}

}