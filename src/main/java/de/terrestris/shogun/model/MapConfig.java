package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * MapConfig POJO
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_MAPCONFIG")
public class MapConfig extends BaseModel {
	
	
	private String name; //TODO remove
	
	private String mapId; // id needed to reference map in portalConfig above
	private String title;
	private String projection;
	private String units;
	private Double maxResolution;
	private String maxExtent; // "-20037508, -20037508, 20037508, 20037508"; //TODO data hibernate format
	private String center; // [-10764594.758211, 4523072.3184791], //TODO data hibernate format
	private Integer zoom;
	
	
	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the mapId
	 */
	@Column(name = "MAPID", nullable = false)
	public String getMapId() {
		return mapId;
	}
	/**
	 * @param mapId the mapId to set
	 */
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
	/**
	 * @return the title
	 */
	@Column(name = "TITLE", nullable = false)
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the projection
	 */
	@Column(name = "PROJECTION", nullable = false)
	public String getProjection() {
		return projection;
	}
	/**
	 * @param projection the projection to set
	 */
	public void setProjection(String projection) {
		this.projection = projection;
	}
	
	/**
	 * @return the units
	 */
	@Column(name = "UNITS", nullable = false)
	public String getUnits() {
		return units;
	}
	/**
	 * @param units the units to set
	 */
	public void setUnits(String units) {
		this.units = units;
	}
	
	/**
	 * @return the maxResolution
	 */
	@Column(name = "MAXRESOLUTION", nullable = false)
	public Double getMaxResolution() {
		return maxResolution;
	}
	/**
	 * @param maxResolution the maxResolution to set
	 */
	public void setMaxResolution(Double maxResolution) {
		this.maxResolution = maxResolution;
	}
	
	/**
	 * @return the maxExtent
	 */
	@Column(name = "MAXEXTENT", nullable = false)
	public String getMaxExtent() {
		return maxExtent;
	}
	/**
	 * @param maxExtent the maxExtent to set
	 */
	public void setMaxExtent(String maxExtent) {
		this.maxExtent = maxExtent;
	}
	
	/**
	 * @return the center
	 */
	@Column(name = "CENTER", nullable = false)
	public String getCenter() {
		return center;
	}
	/**
	 * @param center the center to set
	 */
	public void setCenter(String center) { 
		this.center = center;
	}
	
	/**
	 * @return the zoom
	 */
	@Column(name = "ZOOM", nullable = false)
	public Integer getZoom() {
		return zoom;
	}
	/**
	 * @param zoom the zoom to set
	 */
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
}