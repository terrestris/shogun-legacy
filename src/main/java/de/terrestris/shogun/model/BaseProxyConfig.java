package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * BaseProxyConfig Super Class
 * 
 * @author terrestris GmbH & Co. KG
 * 
 * TODO remove for 0.1 release?
 * 
 */
@MappedSuperclass
public class BaseProxyConfig extends BaseModel {
	
	private String mandatoryParameters;
	private String validatorClass;
	
	
	/**
	 * @return the mandatoryParameters
	 */
	@Column(name = "MANDATORYPARAMETERS", nullable = false)
	public String getMandatoryParameters() {
		return mandatoryParameters;
	}
	/**
	 * @param mandatoryParameters the mandatoryParameters to set
	 */
	public void setMandatoryParameters(String mandatoryParameters) {
		this.mandatoryParameters = mandatoryParameters;
	}
	
	/**
	 * @return the validatorClass
	 */
	@Column(name = "VALIDATORCLASS", nullable = false)
	public String getValidatorClass() {
		return validatorClass;
	}
	/**
	 * @param validatorClass the validatorClass to set
	 */
	public void setValidatorClass(String validatorClass) {
		this.validatorClass = validatorClass;
	}
	
	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("mandatoryParameters", mandatoryParameters)
			.append("validatorClass", validatorClass)
			.toString();
	}
}