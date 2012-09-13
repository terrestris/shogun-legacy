/**
 * 
 */
package de.terrestris.shogun.jsonmodel;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.terrestris.shogun.model.User;

/**
 * The POJO for a list of User items.
 * 
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
public class UserList {
	
	List<User> users;

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}
}