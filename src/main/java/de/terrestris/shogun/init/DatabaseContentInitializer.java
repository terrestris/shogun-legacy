package de.terrestris.shogun.init;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import de.terrestris.shogun.dao.DatabaseDao;
import de.terrestris.shogun.exception.ShogunDatabaseAccessException;
import de.terrestris.shogun.model.BaseModelInterface;
import de.terrestris.shogun.model.Group;
import de.terrestris.shogun.model.MapConfig;
import de.terrestris.shogun.model.MapLayer;
import de.terrestris.shogun.model.Module;
import de.terrestris.shogun.model.Role;
import de.terrestris.shogun.model.User;
import de.terrestris.shogun.model.WmsMapLayer;
import de.terrestris.shogun.model.WmsProxyConfig;

/**
 * The class handling the initial import of all data needed for a running setup.
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
public class DatabaseContentInitializer {

	/**
	 * the logger instance
	 */
	private static Logger LOGGER = Logger
			.getLogger(DatabaseContentInitializer.class);
	
	/** 
	 * flag symbolizing if the database will be modified by this class 
	 */
	private Boolean databaseInitializationEnabled;

	/**
	 * a list of module objects for this applications
	 */
	private List<Module> availableModules;

	/**
	 * The name of the SUPERADMIN
	 */
	private String superAdminName;

	/**
	 * the password for the SUPERADMIN
	 */
	private String superAdminPw;

	/**
	 * determines if an anonymous user should be created?
	 */
	private Boolean autoCreateAnonymousUser = true;

	/**
	 * a default user group
	 */
	private Group defaultGroup;

	/**
	 * a list of available user roles
	 */
	private List<String> availableRoles;

	/**
	 * a default map configuration
	 */
	private MapConfig mapConfig;

	/**
	 * modules to be assigned to anonymous user
	 */
	private List<String> modulesForAnonymous;

	/**
	 * a default WMS map layer
	 */
	private WmsMapLayer wmsMapLayer;

	/**
	 * the DB dao to access database via Hibernate
	 */
	private DatabaseDao dbDao;

	/**
	 * The persistant representation of the standard WMS
	 */
	private WmsMapLayer persistantStdWmsLayer;

	/**
	 * The persistant representation of the standard map configuration
	 */
	private MapConfig persistantStdMapConfig;

	/**
	 * The method called on init.
	 * 
	 * Delegated the tasks to fill the database due to config
	 */
	public void initializeDatabaseContent() {

		if (this.databaseInitializationEnabled == true) {
			
			LOGGER.info("Initializing database content on servlet init.");

			try {
				this.persistantStdWmsLayer = this.createStandardWmsMapLayer();
				this.createAvailableRoles();
				this.createAvailableModules();
				this.persistantStdMapConfig = this.createAvailableMapConfig();
				this.createDefaultGroup();
				this.createSuperAdmin();
				this.createAnonymousUser(this.persistantStdWmsLayer, this.persistantStdMapConfig, null);
			} catch (Exception e) {
				LOGGER.error("Caught exception '" + e.getClass().getSimpleName()
						+ "':", e);
			}
		}
	}

	/**
	 * Creates the available standard {@link WmsMapLayer} entry in the database
	 * 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ShogunDatabaseAccessException
	 */
	private WmsMapLayer createStandardWmsMapLayer()
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, ShogunDatabaseAccessException {
		WmsMapLayer desiredWmsMapLayer = this.getWmsMapLayer();

		HashMap<String, String> fieldsAndValues = new HashMap<String, String>();
		fieldsAndValues.put("url", desiredWmsMapLayer.getUrl());
		fieldsAndValues.put("layers", desiredWmsMapLayer.getLayers());

		WmsMapLayer existingMapLayer = (WmsMapLayer) this.dbDao
				.getEntityByStringFields(WmsMapLayer.class, fieldsAndValues);

		// check if we have an existing WMS map layer
		// create or apply/update a one
		existingMapLayer = (WmsMapLayer) this.createOrApplyObjects(
				existingMapLayer, desiredWmsMapLayer);

		return existingMapLayer;
	}

	/**
	 * Creates the available standard {@link MapConfig} entry in the database
	 * 
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws ShogunDatabaseAccessException
	 */
	private MapConfig createAvailableMapConfig() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ShogunDatabaseAccessException {
		MapConfig desiredMapConfig = this.getMapConfig();
		MapConfig existingMapConfig = (MapConfig) this.dbDao
				.getEntityByStringField(MapConfig.class, "mapId",
						desiredMapConfig.getMapId());

		// check if we have an existing map config
		// create or apply/update a module
		existingMapConfig = (MapConfig) this.createOrApplyObjects(
				existingMapConfig, desiredMapConfig);

		return existingMapConfig;
	}

	/**
	 * Creates the available {@link Module} entries in the database
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws ShogunDatabaseAccessException 
	 */
	private void createAvailableModules() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, 
			ShogunDatabaseAccessException {
		LOGGER.info("Creating available modules");
		
		List<Module> availableModules = this.getAvailableModules();

		for (Module desiredModule : availableModules) {
			String moduleName = desiredModule.getModule_name();
			Module existingModule = (Module) this.dbDao.getEntityByStringField(
					Module.class, "module_name", moduleName);

			// check if we have an existing module
			// create or apply/update a module
			existingModule = (Module) this.createOrApplyObjects(existingModule,
					desiredModule);

		}
	}

	/**
	 * Creates the available {@link Role} entries in the database
	 * @throws ShogunDatabaseAccessException 
	 */
	private void createAvailableRoles() throws ShogunDatabaseAccessException {
		LOGGER.info("Creating available roles");
		
		for (Iterator<String> iterator = this.getAvailableRoles().iterator(); iterator
				.hasNext();) {
			String rolename = (String) iterator.next();

			if (this.dbDao.getEntityByStringField(Role.class, "name", rolename) == null) {
				LOGGER.info("  - Role '" + rolename + "' needs to be created.");
				
				Role newRole = new Role();
				newRole.setName(rolename);
				newRole.setApp_user("auto-create-on-init");

				this.dbDao.createEntity("Role", newRole);
			} else {
				LOGGER.info("  - Role '" + rolename
						+ "' already exists. Skipping.");
			}
		}
	}

	/**
	 * Creates a default group to ensure there is always one
	 * 
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws ShogunDatabaseAccessException 
	 * 
	 */
	private void createDefaultGroup() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, 
			ShogunDatabaseAccessException {

		LOGGER.info("Creating default group");

		Group desiredGroup = this.defaultGroup;
		Group existinGroup = (Group) this.dbDao.getEntityByStringField(
				Group.class, "group_nr", this.defaultGroup.getGroup_nr());

		existinGroup = (Group) this.createOrApplyObjects(existinGroup,
				desiredGroup);
	}

	/**
	 * Creates an SuperAdmin {@link User} with declared properties
	 * @throws ShogunDatabaseAccessException 
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void createSuperAdmin() throws ShogunDatabaseAccessException {
		LOGGER.info("Creating superadmin user");
		
		List<Object> allUsers = this.dbDao.getAllEntities(User.class);

		// determine if we hav already a superadmin and save for later
		User currentSuperAdmin = null;
		for (Iterator<Object> iterator = allUsers.iterator(); iterator
				.hasNext();) {
			User user = (User) iterator.next();

			if (user.hasSuperAdminRole() == true) {
				LOGGER.info("  - We already have a superuser. We possibly need to update.");
				currentSuperAdmin = user;
				break;
			}
		}

		// instanciate a new super admin, because we could not find any
		if (currentSuperAdmin == null) {
			LOGGER.info("  - We could not find a superuser. Create one.");
			currentSuperAdmin = this.dbDao.createUser(new User(),
					User.ROLENAME_SUPERADMIN, false);
		}

		currentSuperAdmin.setUser_name(this.superAdminName);

		PasswordEncoder pwencoder = new Md5PasswordEncoder();
		String hashed = pwencoder.encodePassword(this.superAdminPw, null);
		currentSuperAdmin.setUser_password(hashed);
		currentSuperAdmin.setApp_user("auto-create-on-init");

		// give the superadmin all available modules:
		List<Module> allModules = (List<Module>) (List<?>) this.dbDao
				.getAllEntities(Module.class);

		Set<Module> allModuleSet = new HashSet<Module>(allModules);

		currentSuperAdmin.setModules(allModuleSet);
		LOGGER.info("  - Assigning all " + allModules.size()
				+ " modules to superadmin.");

		this.dbDao.createOrUpdateEntity("User", currentSuperAdmin);
	}

	/**
	 * Creates an anonymous {@link User} with declared properties, such as
	 * {@link MapLayer} and {@link MapConfig}
	 * @throws ShogunDatabaseAccessException 
	 * 
	 * @throws Exception
	 */
	private void createAnonymousUser(WmsMapLayer wmsMapLayer,
			MapConfig mapConfig, WmsProxyConfig wmsProxyConfig) throws ShogunDatabaseAccessException {
		if (this.getAutoCreateAnonymousUser()) {
			LOGGER.info("Creating anonymous user");

			List<Object> allUsers = this.dbDao.getAllEntities(User.class);

			// determine if we already have an anonymous and save for later
			User anon = null;
			for (Iterator<Object> iterator = allUsers.iterator(); iterator
					.hasNext();) {
				User user = (User) iterator.next();
				if (user.hasAnonymousRole() == true) {
					anon = user;
					break;
				}
			}
			if (anon == null) {
				LOGGER.info("  - We could not find an anonymous user. Create one.");
				anon = new User();
				anon.setUser_name("anonymousUser");
				anon.setApp_user("auto-create-on-init");
				anon = this.dbDao.createUser(anon, User.ROLENAME_ANONYMOUS,
						false);
			}

			LOGGER.info("  - Assigning the desired modules to anonymous");
			List<String> anonModules = this.getModulesForAnonymous();
			Set<Module> modulesToAssign = new HashSet<Module>();

			for (String anonModuleName : anonModules) {
				Module m = (Module) this.dbDao.getEntityByStringField(
						Module.class, "module_name", anonModuleName);
				modulesToAssign.add(m);
				LOGGER.info("	- assigning module '" + anonModuleName + "'");
			}
			anon.setModules(modulesToAssign);

			// TODO check for old properties first, before overwriting these

			// Set the auto created mapconf
			// HashSet<MapConfig> mapConfigSet = new HashSet<MapConfig>();
			// mapConfigSet.add(mapConfig);
			// anon.setMapConfigs(mapConfigSet);
			anon.setMapConfig(mapConfig);

			// set the auto created WmsMapLayer
			Set<MapLayer> wmsMapLayerSet = new HashSet<MapLayer>();
			wmsMapLayerSet.add(wmsMapLayer);

			anon.setMapLayers(wmsMapLayerSet);

			if (wmsProxyConfig != null) {
				// set the auto created WmsProxyConfig
				anon.setWmsProxyConfig(wmsProxyConfig);
			}

			this.dbDao.updateUser(anon);
		} else {
			LOGGER.info("Skipping the creation of an anonymous user.");
		}
	}

	/**
	 * Method checks if there is an existing target. If yes the source object is
	 * applied to the target. If there is no existing target, we create a new
	 * one.
	 * 
	 * @param target
	 * @param source
	 * @return
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private BaseModelInterface createOrApplyObjects(BaseModelInterface target,
			BaseModelInterface source) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		if (target != null && !target.getClass().equals(source.getClass())) {
			throw new IllegalArgumentException("Cannot update object of class "
					+ target.getClass().getSimpleName()
					+ " with object of class "
					+ source.getClass().getSimpleName());
		}

		String className = source.getClass().getSimpleName();
		if (target == null) {
			// tag the record, that it has been done automatically
			source.setApp_user("auto-create-on-init");
			target = (BaseModelInterface) this.dbDao.createEntity(className,
					source);
		} else {
			// we need to store the id of the originally existing object
			// because otherwise BeanUtils.copyProperties would set it to 0
			// and a new object would be created by this.dbDao.updateEntity
			int oldid = target.getId();
			Date createdAt = target.getCreated_at();
			PropertyUtils.copyProperties(target, source);
			target.setId(oldid);
			target.setCreated_at(createdAt);
			target.setUpdated_at(new Date());
			target.setApp_user("auto-create-on-init");
			target = (BaseModelInterface) this.dbDao.updateEntity(className,
					target);
		}

		return target;
	}
	
	/**
	 * @param shogunDatabaseInitializationEnabled 
	 * 			the shogunDatabaseInitializationEnabled to set
	 */
	@Autowired
	@Qualifier("databaseInitializationEnabled")
	public void setDatabaseInitializationEnabled(
			Boolean databaseInitializationEnabled) {
		this.databaseInitializationEnabled = databaseInitializationEnabled;
	}

	/**
	 * @return the autoCreateAnonymousUser
	 */
	public Boolean getAutoCreateAnonymousUser() {
		return autoCreateAnonymousUser;
	}

	/**
	 * @param autoCreateAnonymousUser
	 *			the autoCreateAnonymousUser to set
	 */
	public void setAutoCreateAnonymousUser(Boolean autoCreateAnonymousUser) {
		this.autoCreateAnonymousUser = autoCreateAnonymousUser;
	}

	/**
	 * @return the defaultGroup
	 */
	public Group getDefaultGroup() {
		return defaultGroup;
	}

	/**
	 * @param defaultGroup
	 *			the defaultGroup to set
	 */
	public void setDefaultGroup(Group defaultGroup) {
		this.defaultGroup = defaultGroup;
	}

	/**
	 * @return the superAdminName
	 */
	public String getSuperAdminName() {
		return superAdminName;
	}

	/**
	 * @param superAdminName
	 *			the superAdminName to set
	 */
	public void setSuperAdminName(String superAdminName) {
		this.superAdminName = superAdminName;
	}

	/**
	 * @return the superAdminPw
	 */
	public String getSuperAdminPw() {
		return superAdminPw;
	}

	/**
	 * @param superAdminPw
	 *			the superAdminPw to set
	 */
	public void setSuperAdminPw(String superAdminPw) {
		this.superAdminPw = superAdminPw;
	}

	/**
	 * @return the availableRoles
	 */
	public List<String> getAvailableRoles() {
		return availableRoles;
	}

	/**
	 * @param availableRoles
	 *			the availableRoles to set
	 */
	public void setAvailableRoles(List<String> availableRoles) {
		this.availableRoles = availableRoles;
	}

	/**
	 * @return the modulesForAnonymous
	 */
	public List<String> getModulesForAnonymous() {
		return modulesForAnonymous;
	}

	/**
	 * @param modulesForAnonymous
	 *			the modulesForAnonymous to set
	 */
	public void setModulesForAnonymous(List<String> modulesForAnonymous) {
		this.modulesForAnonymous = modulesForAnonymous;
	}

	/**
	 * @return the wmsMapLayer
	 */
	public WmsMapLayer getWmsMapLayer() {
		return wmsMapLayer;
	}

	/**
	 * @param wmsMapLayer
	 *			the wmsMapLayer to set
	 */
	public void setWmsMapLayer(WmsMapLayer wmsMapLayer) {
		this.wmsMapLayer = wmsMapLayer;
	}

	/**
	 * @return the mapConfig
	 */
	public MapConfig getMapConfig() {
		return mapConfig;
	}

	/**
	 * @param mapConfig
	 *			the mapConfig to set
	 */
	public void setMapConfig(MapConfig mapConfig) {
		this.mapConfig = mapConfig;
	}

	/**
	 * @return the availableModules
	 */
	public List<Module> getAvailableModules() {
		return availableModules;
	}

	/**
	 * @param test
	 *			the availableModules to set
	 */
	public void setAvailableModules(List<Module> availableModules) {
		this.availableModules = availableModules;
	}

	/**
	 * @return the dbDao
	 */
	public DatabaseDao getDbDao() {
		return dbDao;
	}

	/**
	 * Auto generation of an TsDAO instance via dependency injection.
	 * 
	 * @param dao
	 */
	@Autowired
	public void setDbDao(DatabaseDao dao) {
		this.dbDao = dao;
	}

	/**
	 * @return the persistantStdWmsLayer
	 */
	public WmsMapLayer getPersistantStdWmsLayer() {
		return persistantStdWmsLayer;
	}

	/**
	 * @return the persistantStdMapConfig
	 */
	public MapConfig getPersistantStdMapConfig() {
		return persistantStdMapConfig;
	}

}
