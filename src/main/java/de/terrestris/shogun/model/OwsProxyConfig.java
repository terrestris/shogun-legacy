package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * OwsProxyConfig Super Class
 * 
 * @author terrestris GmbH & Co. KG
 * 
 * TODO remove this for 0.1 release
 * 
 */
@MappedSuperclass
public class OwsProxyConfig extends BaseProxyConfig {
	
	private String version;
	private String request;
	private String service;
	
	
	/**
	 * @return the version
	 */
	@Column(name = "VERSION", nullable = false)
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * @return the request
	 */
	@Column(name = "REQUEST", nullable = false)
	public String getRequest() {
		return request;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(String request) {
		this.request = request;
	}
	
	/**
	 * @return the service
	 */
	@Column(name = "SERVICE", nullable = false)
	public String getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}
	
	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("version", version)
			.append("request", request)
			.append("service", service)
			.toString();
	}
}