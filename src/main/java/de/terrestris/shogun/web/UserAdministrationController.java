package de.terrestris.shogun.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.terrestris.shogun.jsonmodel.GroupList;
import de.terrestris.shogun.jsonmodel.UserList;
import de.terrestris.shogun.model.Group;
import de.terrestris.shogun.model.User;
import de.terrestris.shogun.service.UserAdministrationService;

/**
 * The web controller handling the user-administration interfaces. 
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
@Controller
public class UserAdministrationController extends AbstractWebController {
	
	/**
	 * the logger
	 */
	private static final Logger LOGGER = Logger.getLogger(UserAdministrationController.class);
	
	/**
	 * the reference to the needed service instance
	 */
	private UserAdministrationService shogunService;
	
	
	// ---------------------------------------------------------------------------------
	// USER ENTITIES
	// ---------------------------------------------------------------------------------
	
	
	/**
	 * Web-interface creating a new User instance in the database
	 * 
	 * @param users A list of new User objects, which is delivered as JSON and automatically deserialized
	 * @param response A HttpServletResponse object in order to return the response
	 * 
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse 
	 */
	@RequestMapping(value = "/user/createUser.action", method=RequestMethod.POST, headers="Accept=application/json, plain/text")
	public @ResponseBody Map<String, ? extends Object> createUser(@RequestBody UserList users, HttpServletResponse response) {
	    	
    	try {
    		
	    	List<User> returnUsers = this.getShogunService().createUsers(users.getUsers());
	    	// Wrap to classical return object containing total, data, success
	    	Map<String, Object> returnMap = this.getModelMapSuccess(returnUsers);

			return returnMap;
    	}
    	catch (Exception e) {
    		LOGGER.error("Error in createUser", e);
    		return getModelMapError("Error trying to create user: " + e.getMessage());
		}
	    	 
	}
	
	/**
	 * Web-interface updating User objects in the database
	 * 
	 * @param users A list of User objects to be updated, which is delivered as JSON and automatically deserialized
	 * @param response A HttpServletResponse object in order to return the response
	 * 
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse 
	 */
	@RequestMapping(value = "/user/updateUser.action", method=RequestMethod.POST, headers="Accept=application/json, plain/text")
	public @ResponseBody
	Map<String, ? extends Object> updateUser(@RequestBody UserList users, HttpServletResponse response) throws Exception {
		
		try {
			
			List<User> returnedUsers = this.getShogunService().updateUser(users.getUsers());
			// Wrap to classical return object containing total, data, success
			Map<String, Object> returnMap = this.getModelMapSuccess(returnedUsers);
			
			return returnMap;
			
		} catch (Exception e) {
			LOGGER.error("Error in updateUser", e);
			return getModelMapError("Error trying to update user: " + e.getMessage());
		}
	}

	/**
	 * Gets the ID of the currently logged in user.
	 *
	 * @return the userId of the currently logged in user
	 */
	@RequestMapping(value = "/user/getLoggedInUserId.action", method=RequestMethod.GET)
	public @ResponseBody
	Map<String, ? extends Object> getLoggedInUserId() {
		Integer userId = this.shogunService.getDatabaseDao().getUserIdFromSession();
		return this.getModelMapSuccess(userId);
	}

	/**
	 * Web-interface deleting a User objects in the database
	 * 
	 * @param the_id an ID of the User to be deleted
	 * @param response A HttpServletResponse object in order to return the response
	 * 
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse 
	 */
	@RequestMapping(value = "/user/deleteUser.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> deleteUser(@RequestBody int the_id, HttpServletResponse response)
			throws Exception {
	
		try {
	
			this.getShogunService().deleteUser(the_id);
	
			Map<String, Object> modelMap = new HashMap<String, Object>(3);
			modelMap.put("success", true);
	
			return modelMap;
	
		} catch (Exception e) {
			LOGGER.error("Error in deleteUser", e);
			return getModelMapError("Error trying to delete user: " + e.getMessage());
		}
	}
	
	/**
	 * Web-interface for creating a new user password and 
	 * send the new password to the user email 
	 * 
	 * @param user_id the user ID of the User who should get a new password 
	 * @param response response A HttpServletResponse object in order to return the response
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse
	 * @throws Exception
	 */
	@RequestMapping(value = "/user/updateUserPw.action")
	public @ResponseBody
	Map<String, ? extends Object> updateUserPassword(@RequestBody String user_id, HttpServletResponse response) throws Exception {
		
		try {

			this.getShogunService().updateUserPassword(user_id);
			
			Map<String, Object> returnMap = new HashMap<String, Object>(2);
			returnMap.put("message", "Mail with new password sent successfully");
			returnMap.put("success", true);
			
			return returnMap;

		} catch (Exception e) {
			LOGGER.error("Error in updateUserPassword", e);
			return getModelMapError("Error updating User password: " + e.getMessage());
		}
	}
	
	
	// ---------------------------------------------------------------------------------
	// GROUP ENTITIES
	// ---------------------------------------------------------------------------------
	
	
	/**
	 * Web-interface creating a new Group instance in the database
	 * 
	 * @param users A list of new {@link Group} objects, 
	 *   which is delivered as JSON and automatically deserialized
	 * @param response A HttpServletResponse object in order to return the response
	 * 
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse 
	 */
	@RequestMapping(value = "/user/createGroup.action", method=RequestMethod.POST, headers="Accept=application/json, plain/text")
	public @ResponseBody Map<String, ? extends Object> createGroup(@RequestBody GroupList groups, HttpServletResponse response) {
	    	
    	try {
    	
	    	List<Group> returnGroups = this.getShogunService().createGroups(groups.getGroups());
	    	// Wrap to classical return object containing total, data, success
	    	Map<String, Object> returnMap = this.getModelMapSuccess(returnGroups);

			return returnMap;
    	}
    	catch (Exception e) {
    		LOGGER.error("Error in createGroup", e);
    		return getModelMapError("Error trying to create Group: " + e.getMessage());
		}
	    	 
	}
	
	
	/**
	 * Web-interface updating Group objects in the database
	 * 
	 * @param users A list of {@link Group} objects to be updated, 
	 *   which is delivered as JSON and automatically deserialized
	 * @param response A HttpServletResponse object in order to return the response
	 * 
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse 
	 */
	@RequestMapping(value = "/user/updateGroup.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> updateGroup(@RequestBody GroupList groups, HttpServletResponse response)
			throws Exception {
		
		try {
			
			List<Group> returnedgroups = this.getShogunService().updateGroup(groups.getGroups());
			// Wrap to classical return object containing total, data, success
			Map<String, Object> returnMap = this.getModelMapSuccess(returnedgroups);
			
			return returnMap;
			
		} catch (Exception e) {
			LOGGER.error("Error in updateGroup", e);
			return getModelMapError("Error trying to update Group: " + e.getMessage());
		}
	}
	
	
	/**
	 * Web-interface deleting a Group objects in the database
	 * 
	 * @param the_id an ID of the {@link Group} to be deleted
	 * @param response A HttpServletResponse object in order to return the response
	 * 
	 * @return A Map object representing the JSON structure of the returned HttpServletResponse 
	 */
	@RequestMapping(value = "/user/deleteGroup.action", method=RequestMethod.POST, headers="Accept=application/json,plain/text")
	public @ResponseBody
	Map<String, ? extends Object> deleteGroup(@RequestBody int the_id, HttpServletResponse response)
			throws Exception {
	
		try {
	
			this.getShogunService().deleteGroup(the_id);
	
			Map<String, Object> modelMap = new HashMap<String, Object>(3);
			modelMap.put("success", true);
	
			return modelMap;
	
		} catch (Exception e) {
			LOGGER.error("Error in deleteGroup", e);
			return getModelMapError("Error trying to delete group: " + e.getMessage());
		}
	}
	
	/**
	 * @return the shogunService
	 */
	public UserAdministrationService getShogunService() {
		return shogunService;
	}

	/**
	 * @param shogunService the shogunService to set
	 */
	@Autowired
	public void setShogunService(UserAdministrationService shogunService) {
		this.shogunService = shogunService;
	}
}
