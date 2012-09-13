package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

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
	
}