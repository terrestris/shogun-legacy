package de.terrestris.shogun.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.terrestris.shogun.jsonmodel.DeleteIdList;
import de.terrestris.shogun.jsonmodel.MapConfigList;
import de.terrestris.shogun.jsonmodel.WmsMapLayerList;
import de.terrestris.shogun.model.Group;
import de.terrestris.shogun.model.MapConfig;
import de.terrestris.shogun.model.Module;
import de.terrestris.shogun.model.WmsMapLayer;
import de.terrestris.shogun.service.MapAdministrationService;

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
	private static final Logger LOGGER = Logger.getLogger(MapAdministrationController.class);

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
