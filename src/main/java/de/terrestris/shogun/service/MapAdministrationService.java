/**
 *
 */
package de.terrestris.shogun.service;

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

		return (MapConfig)this.getDatabaseDao().updateEntity(MapConfig.class.getSimpleName(),mapConfig);
	}

	/**
	 * Update a {@link WfsProxyConfig} in DB
	 *
	 * @param wfsProxyConfigs
	 * @return
	 */
	@Transactional
	public WfsProxyConfig updateWfsProxyConfigs(WfsProxyConfig wfsProxyConfig) {
		return (WfsProxyConfig)this.getDatabaseDao().updateEntity(WfsProxyConfig.class.getSimpleName(), wfsProxyConfig);
	}

	/**
	 * Update a {@link WmsProxyConfig} in DB
	 *
	 * @param wmsProxyConfig
	 * @return
	 */
	@Transactional
	public WmsProxyConfig updateWmsProxyConfigs(WmsProxyConfig wmsProxyConfig) {
		return (WmsProxyConfig)this.getDatabaseDao().updateEntity(WmsProxyConfig.class.getSimpleName(), wmsProxyConfig);
	}

	/**
	 * Creates a new {@link MapLayer} instance
	 *
	 * @param wmsMapLayer
	 * @return
	 */
	@Transactional
	public WmsMapLayer createWmsMapLayer(WmsMapLayer wmsMapLayer) {

		// always set the owner to the currently logged in user.
		// add the newly created wmsmaplayer to he current logged in user
		User user = this.getDatabaseDao().getUserObjectFromSession();
		wmsMapLayer.setOwner(user);

		// create the instance itself
		WmsMapLayer newWmsMapLayer = (WmsMapLayer)this.getDatabaseDao().createEntity(WmsMapLayer.class.getSimpleName(), wmsMapLayer);

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
		return (WmsMapLayer)this.getDatabaseDao().updateEntity(WmsMapLayer.class.getSimpleName(), WmsMapLayer);
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
		this.getDatabaseDao().deleteEntity(WmsMapLayer.class, id);
	}

}
