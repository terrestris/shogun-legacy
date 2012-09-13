package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * WmsMapLayer POJO. 
 * 
 * This is the POJO for a visual layer in the application. Instances of this 
 * class can be used in the client to derive valid OpenLayers.Layer-objects.
 *  
 * @author terrestris GmbH & Co. KG
 * 
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_WMSMAPLAYER")
public class WmsMapLayer extends MapLayer {

	private String url;
	private String layers;
	private Boolean transparent = true;
	private Boolean singleTile = false;
	private Integer ratio = null;
	private String format;
	
	
	/**
	 * @return the url
	 */
	@Column(name = "URL", nullable = false)
	public String getUrl() {
		return url;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	/**
	 * @return the layers
	 */
	@Column(name = "LAYERS", nullable = false)
	public String getLayers() {
		return layers;
	}
	
	/**
	 * @param layers the layers to set
	 */
	public void setLayers(String layers) {
		this.layers = layers;
	}
	
	/**
	 * @return the transparent
	 */
	@Column(name = "TRANSPARENT", nullable = false)
	public Boolean getTransparent() {
		return transparent;
	}
	
	/**
	 * @param transparent the transparent to set
	 */
	public void setTransparent(Boolean transparent) {
		this.transparent = transparent;
	}
	
	/**
	 * @return the singleTile value
	 */
	@Column(name = "SINGLETILE", nullable = false)
	public Boolean getSingleTile() {
		return singleTile;
	}
	
	/**
	 * @param singleTile the value of singleTile to set
	 */
	public void setSingleTile(Boolean singleTile) {
		this.singleTile = singleTile;
	}
	
	
	/**
	 * @return the ratio
	 */
	@Column(name = "RATIO")
	public Integer getRatio() {
		return ratio;
	}

	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}

	/**
	 * @return the format
	 */
	@Column(name = "FORMAT", nullable = false)
	public String getFormat() {
		return format;
	}
	
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	

} 