/* Copyright (c) 2012-2014, terrestris GmbH & Co. KG
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * (This is the BSD 3-Clause, sometimes called 'BSD New' or 'BSD Simplified',
 * see http://opensource.org/licenses/BSD-3-Clause)
 */
package de.terrestris.shogun.service;

import org.springframework.security.access.prepost.PreAuthorize;
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
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
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
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
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
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
	public WmsMapLayer updateWmsMapLayer(WmsMapLayer WmsMapLayer) {
		return (WmsMapLayer)this.getDatabaseDao().updateEntity(WmsMapLayer.class.getSimpleName(), WmsMapLayer);
	}

	/**
	 * Deletes the given {@link MapLayer} by the given ID.
	 *
	 * @param the_id
	 */
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
	public void deleteMapLayer(int deleteId) {

		// it is only one object - cast to object/bean
		Integer id = new Integer(deleteId);
		this.getDatabaseDao().deleteEntity(WmsMapLayer.class, id);
	}

}
