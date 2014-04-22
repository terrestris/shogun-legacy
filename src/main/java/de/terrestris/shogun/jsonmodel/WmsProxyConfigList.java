/**
 *
 */
package de.terrestris.shogun.jsonmodel;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.terrestris.shogun.model.WmsProxyConfig;

/**
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
public class WmsProxyConfigList {

    List<WmsProxyConfig> wmsProxyConfigs;

	/**
	 * @return the wmsProxyConfigs
	 */
	public List<WmsProxyConfig> getWmsProxyConfigs() {
		return wmsProxyConfigs;
	}

	/**
	 * @param wmsProxyConfigs the wmsProxyConfigs to set
	 */
	public void setWmsProxyConfigs(List<WmsProxyConfig> wmsProxyConfigs) {
		this.wmsProxyConfigs = wmsProxyConfigs;
	}

}