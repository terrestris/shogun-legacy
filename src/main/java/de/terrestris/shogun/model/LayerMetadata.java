package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.terrestris.shogun.model.BaseModel;

/**
 * LayerMetadata POJO
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_LAYERMETADATA")
@Embeddable
public class LayerMetadata extends BaseModel {
	
	/** the key of this metadata record  **/
	private String key;
	
	/** the value of this metadata record  **/
	private String value;
	
	
	/**
	 * @return the key
	 */
	@Column(name="KEY")
	public String getKey() {
		return key;
	}
	
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	
	/**
	 * @return the value
	 */
	@Column(name="VALUE")
	public String getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}