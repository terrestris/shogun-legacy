package de.terrestris.shogun.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * WfsProxyConfig POJO
 * 
 * @author terrestris GmbH & Co. KG
 * 
 * TODO remove this for 0.1 release?
 * 
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_WFSPROXYCONFIG")
public class WfsProxyConfig extends OwsProxyConfig {
	
	private String typename;
	private String srs;
	private String bbox;
	private String outputformat;
	private String exceptions;
	private String maxfeatures;
	
	
	/**
	 * @return the typename
	 */
	@Column(name = "TYPENAME", nullable = false)
	public String getTypename() {
		return typename;
	}
	/**
	 * @param typename the typename to set
	 */
	public void setTypename(String typename) {
		this.typename = typename;
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
	 * @return the outputformat
	 */
	@Column(name = "OUTPUTFORMAT", nullable = false)
	public String getOutputformat() {
		return outputformat;
	}
	/**
	 * @param outputformat the outputformat to set
	 */
	public void setOutputformat(String outputformat) {
		this.outputformat = outputformat;
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
	 * @return the maxfeatures
	 */
	@Column(name = "MAXFEATURES", nullable = false)
	public String getMaxfeatures() {
		return maxfeatures;
	}
	/**
	 * @param maxfeatures the maxfeatures to set
	 */
	public void setMaxfeatures(String maxfeatures) {
		this.maxfeatures = maxfeatures;
	}
	
	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("typename", typename)
			.append("srs", srs)
			.append("bbox", bbox)
			.append("outputformat", outputformat)
			.append("exceptions", exceptions)
			.append("maxfeatures", maxfeatures)
			.toString();
	}
}