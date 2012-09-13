package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * WmsProxyConfig POJO
 * 
 * @author terrestris GmbH & Co. KG
 * 
 * TODO remove this for 0.1 release?
 * 
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_WMSPROXYCONFIG")
public class WmsProxyConfig extends OwsProxyConfig {
	
	private String layers;
	private String srs;
	private String styles;
	private String bbox;
	private String width;
	private String height;
	private String format;
	private String exceptions;
	private String transparent;
	
	
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
	 * @return the srs
	 */
	@Column(name = "SRS", nullable = false)
	public String getSrs() {
		return srs;
	}
	/**
	 * @param srs the srs to set
	 */
	public void setSrs(String srs) {
		this.srs = srs;
	}
	
	/**
	 * @return the styles
	 */
	@Column(name = "STYLES", nullable = false)
	public String getStyles() {
		return styles;
	}
	/**
	 * @param styles the styles to set
	 */
	public void setStyles(String styles) {
		this.styles = styles;
	}
	
	/**
	 * @return the bbox
	 */
	@Column(name = "BBOX", nullable = false)
	public String getBbox() {
		return bbox;
	}
	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(String bbox) {
		this.bbox = bbox;
	}
	
	/**
	 * @return the width
	 */
	@Column(name = "WIDTH", nullable = false)
	public String getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	
	/**
	 * @return the height
	 */
	@Column(name = "HEIGHT", nullable = false)
	public String getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
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
	
	/**
	 * @return the exceptions
	 */
	@Column(name = "EXCEPTIONS", nullable = false)
	public String getExceptions() {
		return exceptions;
	}
	/**
	 * @param exceptions the exceptions to set
	 */
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}
	
	/**
	 * @return the transparent
	 */
	@Column(name = "TRANSPARENT", nullable = false)
	public String getTransparent() {
		return transparent;
	}
	/**
	 * @param transparent the transparent to set
	 */
	public void setTransparent(String transparent) {
		this.transparent = transparent;
	}
	
	
}