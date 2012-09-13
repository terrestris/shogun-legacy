/**
 * 
 */
package de.terrestris.shogun.jsonmodel;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.terrestris.shogun.model.MapConfig;

/**
 * 
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
public class MapConfigList {
	
	List<MapConfig> mapConfigs;

	/**
	 * @return the mapConfigs
	 */
	public List<MapConfig> getMapConfigs() {
		return mapConfigs;
	}

	/**
	 * @param mapConfigs the mapConfigs to set
	 */
	public void setMapConfigs(List<MapConfig> mapConfigs) {
		this.mapConfigs = mapConfigs;
	}

}