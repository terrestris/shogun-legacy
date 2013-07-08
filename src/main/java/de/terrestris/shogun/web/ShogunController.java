package de.terrestris.shogun.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.terrestris.shogun.jsonmodel.ModuleList;
import de.terrestris.shogun.jsonrequest.Request;
import de.terrestris.shogun.jsonrequest.association.Association;
import de.terrestris.shogun.model.Module;
import de.terrestris.shogun.service.ShogunService;

/**
 * The main web controller of SHOGun
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
@Controller
public class ShogunController extends AbstractWebController {
	
	/**
	 * the logger
	 */
	private static final Logger LOGGER = Logger.getLogger(ShogunController.class);
	
	/**
	 * the reference to the needed service instance
	 */
	private ShogunService shogunService;
	
	
	/**
	 * General web interface for receiving a set of entities
	 * enables paging filtering and sorting
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/data/get.action")
	public @ResponseBody
	Map<String, ? extends Object> getEntities(@RequestBody Request request) throws Exception {
		
		try {
			
			Map<String, Object> returnMap = this.getShogunService().getEntities(request);
			
			return returnMap;

		} catch (Exception e) {
			return this.getModelMapError("Error retrieving data from database: " + e.getMessage());
		}
	}
	
	
	/**
	 * Web-interface returns the context object for the shogun web application
	 * including the user information of the user logged in user and some 
	 * general application settings. 
	 * 
	 * @return HashMap representing the application context JSON 
	 * @throws Exception
	 */
	@RequestMapping(value = "/config/getAppContext.action")
	public @ResponseBody
	Map<String, ? extends Object> getAppContext() throws Exception {
		
		try {
			
			Map<String, Object> appContextMap = this.getShogunService().getAppContextBySession();
			
			// Wrap to classical return object containing total, data, success
			Map<String, Object> returnMap = this.getModelMapSuccess(appContextMap);
			
			return returnMap;

		} catch (Exception e) {
			LOGGER.error("Exception in getAppContext", e);
			return getModelMapError("Error retrieving application context: " + e.getMessage());
		}
	}
	
	/**
	 * Returns all Modules of a given user.
	 * 
	 * TODO still needed, or should get.action be used instead?
	 * 
	 * @param username the name of the user who's modules should be returned
	 * @return Map representation of the JSON having of the user's modules
	 */
	@RequestMapping(value = "/config/getModulesByUsername.action")
	public @ResponseBody
	Map<String, ? extends Object> getModulesByUsername(@RequestBody String username) {
		
		try {

			Set<Module> modules = null;
			
			if (username != null && username.isEmpty() == false) {
				
				modules = this.getShogunService().getModulesByUser(username);
				// Wrap to classical return object containing total, data, success
				Map<String, Object> modelMap = this.getModelMapSuccess(modules);
				
				return modelMap;
			}
			else {
				throw new Exception("No valid user name provided!");
			}

		} catch (Exception e) {
			LOGGER.error("Exception in getModulesByUsername", e);
			return getModelMapError("Error retrieving modules by user name from database: " + e.getMessage());
		}
	}
	
	/**
	 * Web-interface for retrieving all Modules in the 
	 * database.
	 * 
	 * TODO still needed, or should get.action be used instead?
	 * 
	 * @return Map representing JSON of all modules 
	 */
	@RequestMapping(value = "/config/getAllModules.action")
	public @ResponseBody
	Map<String, ? extends Object> getAllModules() {

		try {
			List<Module> modules = null;

			// return all, but only ROLE_SUPERADMIN allowed
			modules = this.getShogunService().getAllModules();
			// Wrap to classical return object containing total, data, success
			Map<String, Object> modelMap = this.getModelMapSuccess(modules);

			return modelMap;

		} catch (Exception e) {
			LOGGER.error("Exception in getAllModules", e);
			return getModelMapError("Error retrieving all Modules from database: " + e.getMessage());
		}
	}
	
	/**
	 * Updates the associations of two entities classes by a specified
	 * JSON request
	 * 
	 * TODO add a request example here and README
	 * 
	 * @param association
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/data/updateAssociations.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody Map<String, ? extends Object> updateAssociations(@RequestBody Association association, HttpServletResponse response)
			throws Exception {
		try {
			
			Integer numUpdated = this.getShogunService().updateAssociation( association );
			
			HashMap<String, Integer> data = new HashMap<String, Integer>();
			data.put("numUpdated", numUpdated);
			
			Map<String, Object> returnMap = this.getModelMapSuccess(data);

			return returnMap;
			
		} catch (Exception e) {
			LOGGER.error("Exception in updateAssociations", e);
			return getModelMapError("Error trying to update Group: " + e.getMessage());
		}
	}
	
	/**
	 * Web-interface creating a new Module instance in the database
	 * 
	 * TODO replace hard coded path interception
	 * 
	 * @param modules A list of new {@link Module} objects, which is delivered as JSON and automatically deserialized
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/module/createModule.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> insertModule(@RequestBody ModuleList modules, HttpServletRequest request)
			throws Exception {
	
		try {
			
			// try to get the path of the JS files dynamically
			String fullContextPath = request.getSession().getServletContext().getRealPath(request.getSession().getServletContext().getContextPath());
			String[] aContextPath = fullContextPath.split("/");
			String contextPath = "";
			
			List<String> contextPathList = Arrays.asList(aContextPath);
			
			// decide between real tomcat and virtual one of the IDE (eclipse)
			if(contextPathList.contains(".metadata") && contextPathList.contains("wtpwebapps")) {
				contextPath = "/" + aContextPath[1] + "/" + aContextPath[2] + "/" + aContextPath[3] + "/" + aContextPath[9] + "/";
			}
			else {
				contextPath = fullContextPath;
			}
			
			if (!modules.getModules().isEmpty()) {
				this.getShogunService().insertModule(modules.getModules().get(0), contextPath);
				
				// Wrap to classical return object containing total, data, success
				Map<String, Object> returnMap = this.getModelMapSuccess(modules.getModules());
				
				return returnMap;
			}
			else {
				return getModelMapError("Error trying to create module: Module object from client is empty!");
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception in insertModule", e);
			return getModelMapError("Error trying to create module: " + e.getMessage());
		}
	}
	
	/**
	 * Web-interface deleting a Module objects in the database
	 * 
	 * @param the_id an ID of the User to be deleted
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse 
	 */
	@RequestMapping(value = "/module/deleteModule.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> deleteModule(@RequestBody String module_name)
			throws Exception {
	
		try {
	
			this.getShogunService().deleteModule(module_name);
	
			Map<String, Object> modelMap = new HashMap<String, Object>(3);
			modelMap.put("success", true);
	
			return modelMap;
	
		} catch (Exception e) {
			LOGGER.error("Exception in deleteModule", e);
			return getModelMapError("Error trying to delete module: " + e.getMessage());
		}
	}


	/**
	 * @return the shogunService
	 */
	public ShogunService getShogunService() {
		return shogunService;
	}


	/**
	 * @param shogunService the shogunService to set
	 */
	@Autowired
	public void setShogunService(ShogunService shogunService) {
		this.shogunService = shogunService;
	}

}