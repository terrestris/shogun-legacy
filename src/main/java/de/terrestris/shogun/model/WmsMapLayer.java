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
	@Column(name = "LAYERS", nullable = false, length = 2048)
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
	
	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 * According to 
	 * http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java
	 * it is recommended only to use getter-methods when using ORM like Hibernate
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(37, 11). // two randomly chosen prime numbers
				appendSuper(super.hashCode()).
				append(getUrl()).
				append(getLayers()).
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
		if (!(obj instanceof WmsMapLayer))
			return false;
		WmsMapLayer other = (WmsMapLayer) obj;

		return new EqualsBuilder().
				appendSuper(super.equals(other)).
				append(getUrl(), other.getUrl()).
				append(getLayers(), other.getLayers()).
				isEquals();
	}

	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("url", url)
			.append("layers", layers)
			.append("transparent", transparent)
			.append("singleTile", singleTile)
			.append("ratio", ratio)
			.append("format", format)
			.toString();
	}
} 