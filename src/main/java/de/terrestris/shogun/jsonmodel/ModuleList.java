/**
 *
 */
package de.terrestris.shogun.jsonmodel;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.terrestris.shogun.model.Module;

/**
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
public class ModuleList {

	List<Module> modules;

	/**
	 * @return the modules
	 */
	public List<Module> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
}