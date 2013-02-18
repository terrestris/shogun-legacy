/**
 * 
 */
package de.terrestris.shogun.service;

import org.springframework.beans.factory.annotation.Autowired;

import de.terrestris.shogun.dao.DatabaseDao;

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
	private DatabaseDao databaseDAO;
	

	/**
	 * @return the databaseDAO
	 */
	public DatabaseDao getDatabaseDAO() {
		return databaseDAO;
	}

	/**
	 * Auto generation of an DatabaseDao instance via dependency injection.
	 * 
	 * @param databaseDAO the databaseDAO to set
	 */
	@Autowired
	public void setDatabaseDAO(DatabaseDao databaseDAO) {
		this.databaseDAO = databaseDAO;
	}

}
