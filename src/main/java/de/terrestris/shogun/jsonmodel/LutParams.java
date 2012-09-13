
package de.terrestris.shogun.jsonmodel;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * 
 * A class representing the JSON of a request for LookUp tables (LUT)
 * 
 * @author terrestris GmbH & Co. KG
 * 
 * TODO still needed?
 *
 */
@JsonAutoDetect
public class LutParams {
	
	private String lut_name;
	private String language;
	
	
	/**
	 * @return the lut_name
	 */
	public String getLut_name() {
		return lut_name;
	}
	/**
	 * @param lut_name the lut_name to set
	 */
	public void setLut_name(String lut_name) {
		this.lut_name = lut_name;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
}