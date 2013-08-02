package de.terrestris.shogun.init;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private static Logger LOGGER = Logger.getLogger(DatabaseContentInitializer.class);

	protected static final String APP_USER_AUTO_CREATED = "auto-create-on-init";

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
	private Group defaultAnonymousGroup;

	/**
	 * a default user group
	 */
	private Group defaultSuperAdminGroup;

	/**
	 * a default map configuration
	 */
	private List<MapConfig> mapConfigs;

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
	 */
	protected Group persistantDefaultAnonymousGroup;

	/**
	 */
	protected Group persistantDefaultSuperAdminGroup;

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
				this.createAvailableModules();
				this.persistantStdMapConfig = this.createAvailableMapConfig();
				this.createAvailableRoles();
				this.persistantDefaultAnonymousGroup = this.createDefaultAnonymousGroup();
				this.persistantDefaultSuperAdminGroup = this.createDefaultSuperAdminGroup();
				this.createSuperAdmin();
				this.createAnonymousUser(this.persistantStdWmsLayer, this.persistantStdMapConfig, null);
			} catch (Exception e) {
				LOGGER.error("Caught exception '" + e.getClass().getSimpleName()
						+ "':", e);
			}
		}
	}

	/**
	 *
	 */
	private void createAvailableRoles() {
		LOGGER.info("Creating available roles");
		List<String> rolenames = Arrays.asList(
			Group.ROLENAME_ANONYMOUS,
			Group.ROLENAME_USER,
			Group.ROLENAME_ADMIN,
			Group.ROLENAME_SUPERADMIN
		);
		List<Role> roles = new ArrayList<Role>();
		for (String rolename : rolenames) {
			Role role = new Role();
			role.setName(rolename);
			role.setApp_user(APP_USER_AUTO_CREATED);
			roles.add(role);
		}
		this.dbDao.createEntities(roles);
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

		List<MapConfig> allMapConfigs = this.getMapConfigs();
		for (Iterator<MapConfig> iterator = allMapConfigs.iterator(); iterator.hasNext();) {
			MapConfig desiredMapConfig = (MapConfig) iterator.next();
			// check for existing config
			MapConfig existingMapConfig = (MapConfig) this.dbDao.getEntityByStringField(MapConfig.class, "mapId", desiredMapConfig.getMapId());
			existingMapConfig = (MapConfig) this.createOrApplyObjects(existingMapConfig, desiredMapConfig);
		}
		MapConfig stdMapConfig = (MapConfig) this.dbDao.getEntityByStringField(MapConfig.class, "mapId", "stdmap");
		return stdMapConfig;
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
	 * Creates a default group to ensure there is always one
	 *
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws ShogunDatabaseAccessException
	 *
	 */
	private Group createDefaultAnonymousGroup() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ShogunDatabaseAccessException {

		LOGGER.info("Creating default group");

		Group desiredGroup = this.defaultAnonymousGroup;

		// getting the anonymous role
		Role anonRole = (Role) this.dbDao.getEntityByStringField(Role.class, "name", Group.ROLENAME_ANONYMOUS);
		Set<Role> roles = new HashSet<Role>();
		roles.add(anonRole);

		// setting anon role
		desiredGroup.setRoles(roles);

		// assign all available modules to the anonymous group
		// TODO ask TA if this makes sense for the anonymous group
		assignAllModulesToGroup(desiredGroup);

		Group existingGroup = (Group) this.dbDao.getEntityByStringField(
				Group.class, "group_nr", this.defaultAnonymousGroup.getGroup_nr());

		existingGroup = (Group) this.createOrApplyObjects(existingGroup,
				desiredGroup);

		return existingGroup;
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
	private Group createDefaultSuperAdminGroup() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ShogunDatabaseAccessException {

		LOGGER.info("Creating default group");

		Group desiredGroup = this.defaultSuperAdminGroup;

		// getting roles
		Set<Role> roles = new HashSet<Role>();
		roles.add(
			(Role) this.dbDao.getEntityByStringField(Role.class, "name", Group.ROLENAME_ANONYMOUS)
		);
		roles.add(
			(Role) this.dbDao.getEntityByStringField(Role.class, "name", Group.ROLENAME_ADMIN)
		);
		roles.add(
			(Role) this.dbDao.getEntityByStringField(Role.class, "name", Group.ROLENAME_USER)
		);
		roles.add(
			(Role) this.dbDao.getEntityByStringField(Role.class, "name", Group.ROLENAME_SUPERADMIN)
		);

		desiredGroup.setRoles(roles);

		// assign all available modules to the superadmin group
		assignAllModulesToGroup(desiredGroup);

		Group existingGroup = (Group) this.dbDao.getEntityByStringField(
				Group.class, "group_nr", this.defaultSuperAdminGroup.getGroup_nr());

		existingGroup = (Group) this.createOrApplyObjects(existingGroup,
				desiredGroup);

		return existingGroup;
	}

	/**
	 * Assigns all available Modules to the passed group
	 *
	 * @param group
	 */
	protected void assignAllModulesToGroup(Group group) {
		@SuppressWarnings("unchecked")
		List<Module> allModulesList = (List<Module>) (List<?>) this.dbDao
				.getAllEntities(Module.class);

		Set<Module> allModules = new HashSet<Module>(allModulesList);
		group.setModules(allModules);
		LOGGER.info("Assigned a total of " + allModules.size() + " available modules to group " + group.getName());
	}


	/**
	 * Creates an SuperAdmin {@link User} with declared properties
	 * @throws ShogunDatabaseAccessException
	 *
	 * @throws Exception
	 */
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
					Group.ROLENAME_SUPERADMIN, false);
		}

		currentSuperAdmin.setUser_name(this.superAdminName);

		PasswordEncoder pwencoder = new Md5PasswordEncoder();
		String hashed = pwencoder.encodePassword(this.superAdminPw, null);
		currentSuperAdmin.setUser_password(hashed);
		currentSuperAdmin.setApp_user(APP_USER_AUTO_CREATED);

		currentSuperAdmin = (User) this.dbDao.createOrUpdateEntity("User", currentSuperAdmin);

		// add the anonymous user to the default group
		this.persistantDefaultSuperAdminGroup.getUsers().add(currentSuperAdmin);
		this.dbDao.updateEntity("Group", this.persistantDefaultSuperAdminGroup);

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
				if (user.hasAnonymousRole() == true  && user.getUser_name().equals("anonymousUser")) {
					anon = user;
					break;
				}
			}
			if (anon == null) {
				LOGGER.info("  - We could not find an anonymous user. Create one.");
				anon = new User();
				anon.setUser_name("anonymousUser");
				anon.setApp_user(APP_USER_AUTO_CREATED);
				anon = this.dbDao.createUser(anon, Group.ROLENAME_ANONYMOUS,
						false);
			}

			// TODO check for old properties first, before overwriting these

			// Set the auto created mapconf
			// HashSet<MapConfig> mapConfigSet = new HashSet<MapConfig>();
			// mapConfigSet.add(mapConfig);
			// anon.setMapConfigs(mapConfigSet);
			anon.setMapConfig(mapConfig);

			// set the auto created WmsMapLayer
			Set<MapLayer> wmsMapLayerSet = new HashSet<MapLayer>();
			wmsMapLayerSet.add(wmsMapLayer);

			// add the stdLayer to the anon-group
			this.persistantDefaultAnonymousGroup.setMapLayers(wmsMapLayerSet);

			if (wmsProxyConfig != null) {
				// set the auto created WmsProxyConfig
				anon.setWmsProxyConfig(wmsProxyConfig);
			}

			// persist the anonymous user
			this.dbDao.updateUser(anon);


			// add the anonymous user to the default group
			this.persistantDefaultAnonymousGroup.getUsers().add(anon);
			this.dbDao.updateEntity("Group", this.persistantDefaultAnonymousGroup);



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
			source.setApp_user(APP_USER_AUTO_CREATED);
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
			target.setApp_user(APP_USER_AUTO_CREATED);
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

	/**
	 * @return the mapConfigs
	 */
	public List<MapConfig> getMapConfigs() {
		return mapConfigs;
	}

	/**
	 * @param mapConfigs the mapConfigs to set
	 */
	public void setMapConfigs(List<MapConfig> mapConfigs) {
		this.mapConfigs = mapConfigs;
	}

	/**
	 * @return the defaultAnonymousGroup
	 */
	public Group getDefaultAnonymousGroup() {
		return defaultAnonymousGroup;
	}

	/**
	 * @param defaultAnonymousGroup the defaultAnonymousGroup to set
	 */
	public void setDefaultAnonymousGroup(Group defaultAnonymousGroup) {
		this.defaultAnonymousGroup = defaultAnonymousGroup;
	}

	/**
	 * @return the defaultSuperAdminGroup
	 */
	public Group getDefaultSuperAdminGroup() {
		return defaultSuperAdminGroup;
	}

	/**
	 * @param defaultSuperAdminGroup the defaultSuperAdminGroup to set
	 */
	public void setDefaultSuperAdminGroup(Group defaultSuperAdminGroup) {
		this.defaultSuperAdminGroup = defaultSuperAdminGroup;
	}


}
