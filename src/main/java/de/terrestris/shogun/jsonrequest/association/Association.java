package de.terrestris.shogun.jsonrequest.association;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Class represents an association of entities, for example
 * add some layers to an User
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
@JsonAutoDetect
public class Association {

	
	private String leftEntity;
	private Integer leftEntityId;
	private String rightEntity;
	private List<Integer> associations;
	
	/**
	 * @return the leftEntity
	 */
	public String getLeftEntity() {
		return leftEntity;
	}
	/**
	 * @param leftEntity the leftEntity to set
	 */
	public void setLeftEntity(String leftEntity) {
		this.leftEntity = leftEntity;
	}
	/**
	 * @return the leftEntityId
	 */
	public Integer getLeftEntityId() {
		return leftEntityId;
	}
	/**
	 * @param leftEntityId the leftEntityId to set
	 */
	public void setLeftEntityId(Integer leftEntityId) {
		this.leftEntityId = leftEntityId;
	}
	/**
	 * @return the rightEntity
	 */
	public String getRightEntity() {
		return rightEntity;
	}
	/**
	 * @param rightEntity the rightEntity to set
	 */
	public void setRightEntity(String rightEntity) {
		this.rightEntity = rightEntity;
	}
	/**
	 * @return the associations
	 */
	public List<Integer> getAssociations() {
		return associations;
	}
	/**
	 * @param associations the associations to set
	 */
	public void setAssociations(List<Integer> associations) {
		this.associations = associations;
	}
	

}