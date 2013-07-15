package de.terrestris.shogun.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import de.terrestris.shogun.serializer.LeanBaseModelSerializer;

/**
 * Wms POJO
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_WMS")
public class Wms extends BaseModel {
	
	private String supportedVersion;
	private String baseUrl;
	private Set<WmsLayer> wmsLayers;
	
	/**
	 * The default Constructor
	 */
	public Wms() {

	}
	
	/**
	 * An alternative constructor for backwards compatibility: Field `wmsLayers`
	 * is of type Set<WmsLayer>, no longer List<WmsLayer>.
	 *
	 * @param supportedVersion
	 * @param baseUrl
	 * @param wmsLayers
	 */
	public Wms(String supportedVersion, String baseUrl, List<WmsLayer> wmsLayers) {
		this.supportedVersion = supportedVersion;
		this.baseUrl = baseUrl;
		this.wmsLayers = new HashSet<WmsLayer>(wmsLayers);
		
		this.setCreated_at(new Date());
		this.setUpdated_at(new Date());
	}

	/**
	 * An alternative constructor which will all fields to the given values.
	 *
	 * @param supportedVersion
	 * @param baseUrl
	 * @param wmsLayers
	 */
	public Wms(String supportedVersion, String baseUrl, Set<WmsLayer> wmsLayers) {
		this.supportedVersion = supportedVersion;
		this.baseUrl = baseUrl;
		this.wmsLayers = wmsLayers;
		
		this.setCreated_at(new Date());
		this.setUpdated_at(new Date());
	}
	
	/**
	 * @return the supportedVersion
	 */
	@Column(name = "SUPPORTEDVERSION", nullable = false)
	public String getSupportedVersion() {
		return supportedVersion;
	}
	
	/**
	 * @param supportedVersion the supportedVersion to set
	 */
	public void setSupportedVersion(String supportedVersion) {
		this.supportedVersion = supportedVersion;
	}
	
	
	/**
	 * @return the baseUrl
	 */
	@Column(name = "BASEURL", nullable = false)
	public String getBaseUrl() {
		return baseUrl;
	}
	
	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	
	/**
	 * @return the wmsLayers
	 */
	@OneToMany(fetch = FetchType.LAZY, targetEntity=WmsLayer.class)
	@Fetch(FetchMode.SUBSELECT)
	@JsonSerialize(using=LeanBaseModelSerializer.class)
	public Set<WmsLayer> getWmsLayers() {
		return wmsLayers;
	}

	/**
	 * @param wmsLayers the wmsLayers to set
	 */
	public void setWmsLayers(Set<WmsLayer> wmsLayers) {
		this.wmsLayers = wmsLayers;
	}
	
}