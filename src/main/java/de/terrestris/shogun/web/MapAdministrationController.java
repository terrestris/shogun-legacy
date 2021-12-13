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
package de.terrestris.shogun.web;

import de.terrestris.shogun.jsonmodel.DeleteIdList;
import de.terrestris.shogun.jsonmodel.MapConfigList;
import de.terrestris.shogun.jsonmodel.WmsMapLayerList;
import de.terrestris.shogun.model.Group;
import de.terrestris.shogun.model.MapConfig;
import de.terrestris.shogun.model.Module;
import de.terrestris.shogun.model.WmsMapLayer;
import de.terrestris.shogun.service.MapAdministrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * The web controller handling the map-administration interfaces.
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@Controller
public class MapAdministrationController extends AbstractWebController {

	/**
	 * the logger
	 */
	private static final Logger LOGGER = LogManager.getLogger(MapAdministrationController.class);

	/**
	 * the reference to the needed service instance
	 */
	private MapAdministrationService shogunService;


	/**
	 * Web-interface creating MapConfig objects in the database
	 *
	 * @param users A list of {@link Group} objects to be updated,
	 *   which is delivered as JSON and automatically deserialized
	 * @param response A HttpServletResponse object in order to return the response
	 *
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse
	 */
	@RequestMapping(value = "/map/createMapConfig.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> createMapConfig(@RequestBody MapConfigList mapConfigList)
			throws Exception {

		return getModelMapError("Method not implemnted yet.");

	}

	/**
	 * Web-interface updating MapConfig objects in the database
	 *
	 * @param users A list of {@link Group} objects to be updated,
	 *   which is delivered as JSON and automatically deserialized
	 * @param response A HttpServletResponse object in order to return the response
	 *
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse
	 */
	@RequestMapping(value = "/map/updateMapConfig.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> updateMapConfig(@RequestBody MapConfigList mapConfigList)
			throws Exception {
		try {

			MapConfig returnedMapConfig = this.getShogunService().updateMapConfigs(mapConfigList.getMapConfigs().get(0));
			// Wrap to classical return object containing total, data, success
			Map<String, Object> returnMap = this.getModelMapSuccess(returnedMapConfig);

			return returnMap;

		} catch (Exception e) {
			LOGGER.error("Error in updateMapConfig", e);
			return getModelMapError("Error trying to update Group: " + e.getMessage());
		}
	}

	/**
	 * Web-interface deleting MapConfig objects in the database
	 *
	 * @param deleteIds
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/map/deleteMapConfigs.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> deleteMapConfigs(@RequestBody DeleteIdList deleteIds)
			throws Exception {

		return getModelMapError("Method not implemnted yet.");
	}


	/**
	 * Web-interface creating a new Module instance in the database
	 *
	 * @param modules A list of new {@link Module} objects,
	 *   which is delivered as JSON and automatically deserialized
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/map/createWmsMapLayer.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> insertWmsMapLayer(@RequestBody WmsMapLayerList wmsMapLayerList) throws Exception {

		try {
			WmsMapLayer returnedWmsMapLayer = this.getShogunService().createWmsMapLayer(wmsMapLayerList.getWmsMapLayers().get(0));
			// Wrap to classical return object containing total, data, success
			Map<String, Object> returnMap = this.getModelMapSuccess(returnedWmsMapLayer);

			return returnMap;
		} catch (Exception e) {
			LOGGER.error("Error in createWmsMapLayer", e);
			return getModelMapError("Error trying to create WmsMapLayer: " + e.getMessage());
		}
	}

	/**
	 * Web-interface updating a WmsMapLayer objects in the database
	 *
	 * @param users A list of {@link WmsMapLayer} objects to be updated,
	 *   which is delivered as JSON and automatically deserialized
	 * @param response A HttpServletResponse object in order to return the response
	 *
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse
	 */
	@RequestMapping(value = "/map/updateWmsMapLayer.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> updateWmsMapLayer(@RequestBody WmsMapLayerList wmsMapLayerList)
			throws Exception {
		try {

			WmsMapLayer returnedWmsMapLayer = this.getShogunService().updateWmsMapLayer(wmsMapLayerList.getWmsMapLayers().get(0));
			// Wrap to classical return object containing total, data, success
			Map<String, Object> returnMap = this.getModelMapSuccess(returnedWmsMapLayer);

			return returnMap;

		} catch (Exception e) {
			LOGGER.error("Error in updateWmsMapLayer", e);
			return getModelMapError("Error trying to update WmsMapLayer: " + e.getMessage());
		}
	}

	/**
	 * Web-interface deleting a WmsMapLayer objects in the database
	 *
	 * @param the_id an ID of the User to be deleted
	 * @param response A HttpServletResponse object in order to return the response
	 *
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse
	 */
	@RequestMapping(value = "/map/deleteWmsMapLayer.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> deleteWmsMapLayer(@RequestBody DeleteIdList deleteIds)
			throws Exception {

		try {

			this.getShogunService().deleteMapLayer(deleteIds.getDeleteIds().get(0));

			Map<String, Object> modelMap = new HashMap<String, Object>(3);
			modelMap.put("success", true);

			return modelMap;

		} catch (Exception e) {
			LOGGER.error("Error in deleteWmsMapLayer", e);
			return getModelMapError("Error trying to delete WmsMapLayer: " + e.getMessage());
		}
	}

	/**
	 * @return the shogunService
	 */
	public MapAdministrationService getShogunService() {
		return shogunService;
	}

	/**
	 * @param shogunService the shogunService to set
	 */
	public void setShogunService(MapAdministrationService shogunService) {
		this.shogunService = shogunService;
	}

}
