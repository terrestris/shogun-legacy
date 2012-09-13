/**
 * 
 */
package de.terrestris.shogun.service;

import org.springframework.beans.factory.annotation.Autowired;

import de.terrestris.shogun.dao.DatabaseDAO;

/**
 * The service level of SHOGun
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
public abstract class AbstractShogunService {
	
	/**
	 * Reference to an database DAO object
	 */
	private DatabaseDAO databaseDAO;
	

	/**
	 * @return the databaseDAO
	 */
	public DatabaseDAO getDatabaseDAO() {
		return databaseDAO;
	}

	/**
	 * Auto generation of an DatabaseDAO instance via dependency injection.
	 * 
	 * @param databaseDAO the databaseDAO to set
	 */
	@Autowired
	public void setDatabaseDAO(DatabaseDAO databaseDAO) {
		this.databaseDAO = databaseDAO;
	}

}
