/**
 *
 */
package de.terrestris.shogun.jsonmodel;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.terrestris.shogun.model.Group;

/**
 * The POJO for a list of Group items.
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
public class GroupList {

	List<Group> groups;

	/**
	 * @return the groups
	 */
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

}