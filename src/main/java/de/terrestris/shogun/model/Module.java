package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Module POJO
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_MODULE")
public class Module extends BaseModel {
	
	
	private String module_name;
	private String module_fullname;
	
	
	/**
	 * @return the module_name
	 */
	@Column(name="MODULE_NAME", length = 200)
	public String getModule_name() {
		return module_name;
	}

	/**
	 * @param module_name the module_name to set
	 */
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	
	/**
	 * @return the module_fullname
	 */
	@Column(name="MODULE_FULLNAME")
	public String getModule_fullname() {
		return module_fullname;
	}

	/**
	 * @param module_fullname the module_fullname to set
	 */
	public void setModule_fullname(String module_fullname) {
		this.module_fullname = module_fullname;
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
		return new HashCodeBuilder(43, 23). // two randomly chosen prime numbers
				appendSuper(super.hashCode()).
				append(getModule_name()).
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
		if (!(obj instanceof Module))
			return false;
		Module other = (Module) obj;

		return new EqualsBuilder().
				appendSuper(super.equals(other)).
				append(getModule_name(), other.getModule_name()).
				isEquals();
	}
	
	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("module_name", module_name)
			.append("module_fullname", module_fullname)
			.toString();
	}
}