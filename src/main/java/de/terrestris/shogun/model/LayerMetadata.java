package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
	
	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 * According to 
	 * http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java
	 * it is recommended only to use getter-methods when using ORM like Hibernate
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(19, 17). // two randomly chosen prime numbers
				appendSuper(super.hashCode()).
				append(getKey()).
				append(getValue()).
				toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * According to 
	 * http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java
	 * it is recommended only to use getter-methods when using ORM like Hibernate
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LayerMetadata))
			return false;
		LayerMetadata other = (LayerMetadata) obj;

		return new EqualsBuilder().
				appendSuper(super.equals(other)).
				append(getKey(), other.getKey()).
				append(getValue(), other.getValue()).
				isEquals();
	}
}