/**
 * 
 */
package de.terrestris.shogun.jsonmodel;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.terrestris.shogun.model.WfsProxyConfig;

/**
 * 
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
public class WfsProxyConfigList {
	
	List<WfsProxyConfig> wfsProxyConfigs;

	/**
	 * @return the wfsProxyConfigs
	 */
	public List<WfsProxyConfig> getWfsProxyConfigs() {
		return wfsProxyConfigs;
	}

	/**
	 * @param wfsProxyConfigs the wfsProxyConfigs to set
	 */
	public void setWfsProxyConfigs(List<WfsProxyConfig> wfsProxyConfigs) {
		this.wfsProxyConfigs = wfsProxyConfigs;
	}
}