package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
}