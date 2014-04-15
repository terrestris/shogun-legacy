
package de.terrestris.shogun.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A base class defining the web controllers.
 */
public abstract class AbstractWebController {
	
	/**
	 * Generates modelMap representing the JSON returning a set of
	 * data records.
	 * 
	 * @param data A list of {@link Object} to be returned
	 * @return the map instance representing the JSON
	 */
	protected Map<String, Object> getModelMapSuccess(List<? extends Object> data) {
		Map<String, Object> returnMap = new HashMap<String, Object>(3);
		returnMap.put("total", data.size());
		returnMap.put("data", data);
		returnMap.put("success", true);
		
		return returnMap;
	}
	
	/**
	 * Generates modelMap representing the JSON returning a set of
	 * data records.
	 *
	 * @param data A set of {@link Object} to be returned
	 * @return the map instance representing the JSON
	 */
	protected Map<String, Object> getModelMapSuccess(Set<? extends Object> data) {
		Map<String, Object> returnMap = new HashMap<String, Object>(3);
		returnMap.put("total", data.size());
		returnMap.put("data", data);
		returnMap.put("success", true);

		return returnMap;
	}

	/**
	 * Generates modelMap representing the JSON returning one
	 * data record.
	 * 
	 * @param dataset the {@link Object} instance to be returned
	 * @return the map instance representing the JSON
	 */
	protected Map<String, Object> getModelMapSuccess(Object dataset) {
		Map<String, Object> returnMap = new HashMap<String, Object>(3);
		returnMap.put("total", 1);
		returnMap.put("data", dataset);
		returnMap.put("success", true);
		
		return returnMap;
	}
	
	/**
	 * Generates modelMap to representing the JSON returning an error.
	 * 
	 * @param msg message the error message to be returned
	 * @return the map instance representing the JSON
	 */
	protected Map<String, Object> getModelMapError(String msg) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);

		return modelMap;
	}
	
}
