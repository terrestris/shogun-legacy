/**
 * 
 */
package de.terrestris.shogun.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.terrestris.shogun.model.MapConfig;
import de.terrestris.shogun.model.MapLayer;
import de.terrestris.shogun.model.User;
import de.terrestris.shogun.model.WfsProxyConfig;
import de.terrestris.shogun.model.WmsMapLayer;
import de.terrestris.shogun.model.WmsProxyConfig;

/**
 * A service class of SHOGun offering Map related business logic.
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
@Service
public class MapAdministrationService extends AbstractShogunService {
	
	/**
	 * Update a {@link MapConfig} in DB
	 * 
	 * @param mapConfigs
	 * @return
	 */
	@Transactional
	public MapConfig updateMapConfigs(MapConfig mapConfig) {
		
		return (MapConfig)getDatabaseDAO().updateEntity(MapConfig.class.getSimpleName(),mapConfig);
	}
	
	/**
	 * Update a {@link WfsProxyConfig} in DB
	 * 
	 * @param wfsProxyConfigs
	 * @return
	 */
	@Transactional
	public WfsProxyConfig updateWfsProxyConfigs(WfsProxyConfig wfsProxyConfig) {
		return (WfsProxyConfig)getDatabaseDAO().updateEntity(WfsProxyConfig.class.getSimpleName(), wfsProxyConfig);
	}
	
	/**
	 * Update a {@link WmsProxyConfig} in DB
	 * 
	 * @param wmsProxyConfig
	 * @return
	 */
	@Transactional
	public WmsProxyConfig updateWmsProxyConfigs(WmsProxyConfig wmsProxyConfig) {
		return (WmsProxyConfig)getDatabaseDAO().updateEntity(WmsProxyConfig.class.getSimpleName(), wmsProxyConfig);
	}
	
	/**
	 * Creates a new {@link MapLayer} instance
	 * 
	 * @param wmsMapLayer
	 * @return
	 */
	@Transactional
	public WmsMapLayer createWmsMapLayer(WmsMapLayer wmsMapLayer) {
		
		// create the instance itself
		WmsMapLayer newWmsMapLayer = (WmsMapLayer)getDatabaseDAO().createEntity(WmsMapLayer.class.getSimpleName(), wmsMapLayer);
		
		// add the newly created wmsmaplayer to he current logged in user
		User user = getDatabaseDAO().getUserObjectFromSession();
		Set<MapLayer> currentMapLayersSet = user.getMapLayers();
		currentMapLayersSet.add(newWmsMapLayer);
		
		getDatabaseDAO().updateUser(user);
		
		return newWmsMapLayer;
	}
	
	/**
	 * Updates the given {@link MapLayer}.
	 * 
	 * @param wmsMapLayer
	 * @return
	 */
	@Transactional
	public WmsMapLayer updateWmsMapLayer(WmsMapLayer WmsMapLayer) {
		return (WmsMapLayer)getDatabaseDAO().updateEntity(WmsMapLayer.class.getSimpleName(), WmsMapLayer);
	}
	
	/**
	 * Deletes the given {@link MapLayer} by the given ID.
	 * 
	 * @param the_id
	 */
	@Transactional
	public void deleteMapLayer(int deleteId) {
		
		// it is only one object - cast to object/bean
		Integer id = new Integer(deleteId);
		getDatabaseDAO().deleteEntity(WmsMapLayer.class, id);
	}

}
