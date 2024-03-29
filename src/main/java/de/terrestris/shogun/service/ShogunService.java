/* Copyright (c) 2012-2014, terrestris GmbH & Co. KG
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * (This is the BSD 3-Clause, sometimes called 'BSD New' or 'BSD Simplified',
 * see http://opensource.org/licenses/BSD-3-Clause)
 */
package de.terrestris.shogun.service;

import de.terrestris.shogun.exception.ShogunDatabaseAccessException;
import de.terrestris.shogun.exception.ShogunServiceException;
import de.terrestris.shogun.hibernatecriteria.filter.HibernateFilter;
import de.terrestris.shogun.hibernatecriteria.paging.HibernatePagingObject;
import de.terrestris.shogun.hibernatecriteria.sort.HibernateSortObject;
import de.terrestris.shogun.jsonrequest.Paging;
import de.terrestris.shogun.jsonrequest.Request;
import de.terrestris.shogun.jsonrequest.Sort;
import de.terrestris.shogun.jsonrequest.association.Association;
import de.terrestris.shogun.model.Module;
import de.terrestris.shogun.model.*;
import de.terrestris.shogun.util.JsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.collection.internal.PersistentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ManyToMany;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * A basic service class of SHOGun offering some business logic.
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@Service
public class ShogunService extends AbstractShogunService {

	/**
	 * the logger instance
	 */
	private static Logger LOGGER = LogManager.getLogger(ShogunService.class);


	/**
	 * the list of packages where mapped DB model classes are located
	 */
	private ArrayList<String> dbEntitiesPackages;

	/**
	 * Get Entities defined within a request Request defines filter, sorting and
	 * paging
	 *
	 * @return List<Object>
	 * @throws ShogunServiceException
	 */
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Map<String, Object> getEntities(Request request) throws ShogunServiceException {

		HibernateSortObject hibernateSortObject = null;
		HibernateFilter hibernateFilter = null;
		HibernateFilter hibernateAdditionalFilter = null;
		HibernatePagingObject hibernatePaging = null;
		Set<String> fields = null;
		Set<String> ignoreFields = null;

		try {

			// Detect the mapped model class for the given
			// object type
			String objectType = request.getObject_type();
			Class clazz = this.getHibernateModelByObjectType(objectType);

			if (clazz == null) {
				throw new ShogunServiceException("No mapped class for object type " + objectType + " found.");
			}

			// get the criteria objects from client request
			Sort sortObject = request.getSortObject();
			de.terrestris.shogun.jsonrequest.Filter filter = request.getFilter();
			de.terrestris.shogun.jsonrequest.Filter additionalFilter = request.getGlobalAndFilter();
			Paging paging = request.getPagingObject();

			hibernateSortObject = HibernateSortObject.create(clazz, sortObject);
			hibernateFilter = HibernateFilter.create(clazz, filter);

			fields = request.getFields();
			ignoreFields = request.getIgnoreFields();

			// needed to be able to have another global conjunction
			// temporary solution
			hibernateAdditionalFilter = HibernateFilter.create(clazz, additionalFilter);
			hibernatePaging = HibernatePagingObject.create(clazz, paging);

			// get total count
			long total = this.getDatabaseDao().getTotal(hibernateFilter, hibernateAdditionalFilter);

			// get the data from database
			List<Object> dataList = null;
			dataList = this.getDatabaseDao().getDataByFilter(
				hibernateSortObject,
				hibernateFilter,
				fields,
				ignoreFields,
				hibernatePaging,
				hibernateAdditionalFilter
			);

			//TODO introduce a Beans abstracting this
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("total", new Long(total));
			returnMap.put("data", dataList);
			returnMap.put("success", true);

			return returnMap;

		} catch (Exception e) {

			throw new ShogunServiceException("Error while requesting data " + e.getMessage(), e);
		}
	}


	/**
	 * TODO return a valid number
	 * TODO exception handling
	 * TODO refer to constant for package
	 * TODO cleanup
	 *
	 * @param association
	 * @return
	 * @throws IntrospectionException
	 */
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
	public Integer updateAssociation(Association association) throws IntrospectionException {

		Class<?> leftClazz;
		Class<?> rightClazz;
		Integer leftEntityId;
		List<Integer> assocications;

		Map<String, String> associationProperties;
		Method rightGetter;

		try {

			leftClazz = this.getHibernateModelByObjectType(association.getLeftEntity());
			rightClazz = this.getHibernateModelByObjectType(association.getRightEntity());
			leftEntityId = association.getLeftEntityId();
			assocications = association.getAssociations();

			// getter setter and property which is associated
			associationProperties = this.detectAssociationProperties(rightClazz, leftClazz);

			// Method object representing the getter-method of the right class, that returns the
			// list of left-class-objects, e.g. User.getMapLayers()
			LOGGER.debug("Create a method object for " + associationProperties.get("assocGetter"));
			rightGetter = rightClazz.getMethod(associationProperties.get("assocGetter"));

			BaseModelInheritance wmsMapLayerInstanceToAdd = (BaseModelInheritance)this.getDatabaseDao().getEntityById(leftEntityId, leftClazz);

//			List<Object> allUsers = this.getDatabaseDao().getAllEntities(rightClazz);

			List<? extends Object> allnewAssocedUsers = this.getDatabaseDao().getEntitiesByIds(assocications.toArray(), rightClazz);

			for (Iterator<?> iterator = allnewAssocedUsers.iterator(); iterator.hasNext();) {


				BaseModel currentAssocedUser = (BaseModel) iterator.next();

				// dynamic invoking of the getter
				// receive all left objects that are associated to the current right object
				// e.g. all MapLayers of a User
				PersistentSet currentAssocedUsersMapLayers = (PersistentSet) rightGetter.invoke(currentAssocedUser);

				/*
				 * http://www.java-forums.org/new-java/20849-how-can-i-avoid-java-util-concurrentmodificationexception-exception.html
				 */
				if(!currentAssocedUsersMapLayers.contains(wmsMapLayerInstanceToAdd)) {
					currentAssocedUsersMapLayers.add(wmsMapLayerInstanceToAdd);
				}

				if (currentAssocedUsersMapLayers.size() == 0) {

					currentAssocedUsersMapLayers.add(wmsMapLayerInstanceToAdd);
				}

			}

			List<? extends Object> allNotAssocedUsers = this.getDatabaseDao().getEntitiesByExcludingIds(assocications.toArray(), rightClazz);

			for (Iterator<?> iterator = allNotAssocedUsers.iterator(); iterator.hasNext();) {

				BaseModel currentNotAssocedUser = (BaseModel) iterator.next();

				// dynamic invoking of the getter
				// receive all left objects that are associated to the current right object
				// e.g. all MapLayers of a User
				PersistentSet currentNotAssocedUsersMapLayers = (PersistentSet)rightGetter.invoke(currentNotAssocedUser);

				/*
				 * http://www.java-forums.org/new-java/20849-how-can-i-avoid-java-util-concurrentmodificationexception-exception.html
				 */
				if(currentNotAssocedUsersMapLayers.contains(wmsMapLayerInstanceToAdd )) {
					currentNotAssocedUsersMapLayers.remove(wmsMapLayerInstanceToAdd);
				}

			}


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;

	}
	/**
	 * Determines the properties responsible to map the association between two
	 * entities, such as their getter and setter methods.
	 *
	 * TODO allow OneToMany as well, only ManyToMany supported ATM
	 *
	 * @param clazz
	 * @param targetClass
	 * @return
	 *
	 * @throws IntrospectionException
	 */
	private Map<String, String> detectAssociationProperties(Class<?> clazz, Class<?> targetClass) throws IntrospectionException {

		Map<String, String> classProperties = new HashMap<String, String>();

		PropertyDescriptor[] propertyDescriptors;

		propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();

		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

			Method readMethod = propertyDescriptor.getReadMethod();
			Annotation[] annotationsProp = readMethod.getAnnotations();

			for (Annotation annotation : annotationsProp) {

				if (annotation instanceof ManyToMany) {

					String dspNameProp = propertyDescriptor.getDisplayName();
					Method writeMethod = propertyDescriptor.getWriteMethod();

					Class<?> classCandidate = ((ManyToMany)annotation).targetEntity();

					if (classCandidate.isAssignableFrom(targetClass)) {

						classProperties.put("assocProperty", dspNameProp);
						classProperties.put("assocGetter", readMethod.getName());
						classProperties.put("assocSetter", writeMethod.getName());
					}
				}
			}
		}

		return classProperties;
	}

	/**
	 * Builds an application context object, consisting of:
	 * <ul>
	 * <li>logged in user from session</li>
	 * <li>application object derived as a combination of user's permissions
	 * and the permissions of the user's groups</li>
	 * <ul>
	 *
	 * @return HashMap representing the application context as JSON object
	 * @throws ShogunDatabaseAccessException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@Transactional
	public Map<String, Object> getAppContextBySession() throws ShogunServiceException, ShogunDatabaseAccessException, IllegalAccessException, InvocationTargetException {

		// get the authorization context, incl. user name
		Authentication authResult = SecurityContextHolder.getContext().getAuthentication();
		// get the user object from database
		List<User> users = this.getDatabaseDao().getUserByName(authResult.getName());

		// user-check
		User user = null;
		if (users.size() > 0) {
			user = users.get(0);
		} else {
			throw new ShogunServiceException("No user found, who is logged in at the backend");
		}

		// fetch the modules within the transaction in order to prevent a LazyLoadingException
		// while serializing the user object to JSON
		user.getModules();

		// get groups and layers of user
		Set<Group> usergroups = user.getGroups();
		Set<Module> userModules = user.getModules();
		// container for app related objects
		Set<MapLayer> appMapLayers = new HashSet<MapLayer>();
		Set<Module> appModules = new HashSet<Module>();

		// collect layers and modules owned by user's group
		for (Group usergroup : usergroups) {
			appMapLayers.addAll(usergroup.getMapLayers());
			appModules.addAll(usergroup.getModules());
		}

		// derive the common modules of user an its groups
		appModules.retainAll(userModules);

		// add all owned layers to the set of map layers in the app
		List<MapLayer> ownedLayers = this.getDatabaseDao().getOwnedMapLayers(user);
		// TODO detect co-owned MapLayers as well
		appMapLayers.addAll(ownedLayers);

		MapConfig stdMapConfig = (MapConfig) this.getDatabaseDao()
				.getEntityByStringField(MapConfig.class, "mapId", "stdmap");
		stdMapConfig.restrictBy(user.getMapConfig());

		// create an data object containing an JS object for app and user
		// as a sub object of the return object
		Map<String, Object> appDataMap = new HashMap<String, Object>();
		appDataMap.put("mapConfig", stdMapConfig);
		appDataMap.put("mapLayers", appMapLayers);
		appDataMap.put("modules", appModules);

		// the application context object
		Map<String, Object> appContextMap = new HashMap<String, Object>(2);
		appContextMap.put("app", appDataMap);
		appContextMap.put("user", user);

		return appContextMap;
	}

	/**
	 * Method returns the modules of a user specified by its user name
	 *
	 * The complete user object is fetched from the database and the modules are
	 * extracted and returned
	 *
	 * @param username
	 * @return the list of module objects
	 * @throws ShogunDatabaseAccessException
	 */
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
	public java.util.Set<Module> getModulesByUser(String username) throws ShogunDatabaseAccessException {

		List<User> users = this.getDatabaseDao().getUserByName(username);

		User user = null;

		if (users.size() > 0) {
			user = users.get(0);
			Set<Module> modules = user.getModules();
			return modules;
		} else {
			return new HashSet<Module>();
		}
	}

	/**
	 * Method returns all available {@link Module} objects
	 *
	 * <b>CAUTION: Only if the logged in user has the role ROLE_SUPERADMIN the
	 * function is accessible, otherwise access is denied.</b>
	 *
	 * @return the list of module objects
	 * @throws Exception
	 *			 Specifiy generic errors with an own error message
	 */
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
	public List<Module> getAllModules() throws ShogunServiceException {

		try {
			// get Objects from database
			List<Object> dedicatedModules = this.getDatabaseDao().getAllEntities(Module.class);

			// Cast from Object to Module
			List<Module> allModules = new ArrayList<Module>(dedicatedModules.size());
			for (Iterator<Object> iterator = dedicatedModules.iterator(); iterator.hasNext();) {
				Module module = (Module) iterator.next();
				allModules.add(module);
			}

			return allModules;

		} catch (Exception e) {
			throw new ShogunServiceException(
					"Error while fetching all Modules from database" + e.getMessage());
		}

	}

	/**
	 * CAUTION! Not used at the moment, maybe useful for several future use-cases.<br>
	 * Method returns distinct field values of a specified column within an
	 * entity
	 *
	 * <b>CAUTION: Only if the logged in user has the role ROLE_USER or the role
	 * ROLE_SUPERADMIN the function is accessible, otherwise access is
	 * denied.</b>
	 *
	 * @return the list of distinct country phone codes as String
	 * @throws ShogunServiceException
	 */
	@Transactional
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public List<Object> getDistictFieldValues(String entity, String field) throws ShogunServiceException {

		Class<?> entityClass = null;
		try {
			entityClass = Class.forName(entity);
		} catch (ClassNotFoundException cnfEx) {
			cnfEx.printStackTrace();
			throw new ShogunServiceException("Provided Class " + entity + " not found " + cnfEx.getMessage());
		}

		// TODO use param for entity MODEL_CLASS_MAP.get(objectType)
		List<String> codes = this.getDatabaseDao().getDistinctEntitiesByField(
				entityClass, field, true);
		List returnList = new ArrayList();

		for (Iterator<String> iterator = codes.iterator(); iterator.hasNext();) {
			String code = iterator.next();

			List<String> codeAsList = new ArrayList<String>();
			codeAsList.add(code);

			returnList.add(codeAsList);

		}

		return returnList;

	}

	/**
	 * Inserts a new module into the database.
	 * Also creates the needed stubs and config blocks within
	 * the JavaScript parts of SHOGun.
	 *
	 * @param moduleKey
	 * @param path
	 * @throws IOException
	 */
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
	public void insertModule(Module module, String contextPath)
			throws IOException {

		// WRITE RIGHT KEYS IN FILE

		java.io.File rightKeys = new java.io.File(contextPath + "src/main/webapp/client/configs/right-keys.js");

		if (rightKeys.isFile()) {

			// Note that FileReader is used, not File, since File is not closeable
			Scanner scanner = new Scanner(new FileReader(rightKeys));
			StringBuffer sb = new StringBuffer();
			try {
				//first use a Scanner to get each line
				while ( scanner.hasNextLine() ){
					sb.append(JsHelper.processLineRightKeys(scanner.nextLine(), module.getModule_name()));
				}
			}
			finally {
				// ensure the underlying stream is always closed
				// this only has any effect if the item passed to the Scanner
				// constructor implements closeable (which it does in this case).
				scanner.close();
			}

			// use buffering
			Writer output = new BufferedWriter(new FileWriter(rightKeys));
			try {
				// FileWriter always assumes default encoding is OK!
				output.write( sb.toString() );
			}
			finally {
				output.close();
			}

		} else {
			throw new IOException("file " + contextPath + "src/main/webapp/client/configs/right-keys.js not found ...");
		}


		// CREATE TEMPLATE DUE TO NAME CONVENTION

		java.io.File moduleTemplate = new java.io.File(contextPath + "src/main/webapp/client/javascript/module/TemplateModule.js");
		java.io.File newClassFile = new java.io.File(contextPath + "src/main/webapp/client/javascript/module/" + module.getModule_name() + ".js");

		if (moduleTemplate.isFile()) {

			//Note that FileReader is used, not File, since File is not Closeable
			Scanner scanner = new Scanner(new FileReader(moduleTemplate));
			StringBuffer sb = new StringBuffer();
			try {
				//first use a Scanner to get each line
				while ( scanner.hasNextLine() ){
					sb.append(JsHelper.processLine(scanner.nextLine(), module.getModule_name()));
				}
			}
			finally {
				//ensure the underlying stream is always closed
				//this only has any effect if the item passed to the Scanner
				//constructor implements Closeable (which it does in this case).
				scanner.close();
			}

			//use buffering
			Writer output = new BufferedWriter(new FileWriter(newClassFile));
			try {
				//FileWriter always assumes default encoding is OK!
				output.write( sb.toString() );
			}
			finally {
				output.close();
			}


		} else {
			throw new IOException("file " + contextPath + "src/main/webapp/client/javascript/module/TemplateModule.js not found ...");
		}

		// WRITE CONFIG FOR LOADER

		java.io.File loaderConfigOld = new java.io.File(contextPath + "src/main/webapp/client/javascript/terrestris-suite.config.loader.js");

		if (loaderConfigOld.isFile()) {

			//Note that FileReader is used, not File, since File is not Closeable
			Scanner scanner = new Scanner(new FileReader(loaderConfigOld));
			StringBuffer sb = new StringBuffer();
			try {
				//first use a Scanner to get each line
				while ( scanner.hasNextLine() ){
					sb.append(JsHelper.processLoaderConfByLine(scanner.nextLine(), module.getModule_name()));
				}
			}
			finally {
				//ensure the underlying stream is always closed
				//this only has any effect if the item passed to the Scanner
				//constructor implements Closeable (which it does in this case).
				scanner.close();
			}

			//use buffering
			Writer output = new BufferedWriter(new FileWriter(loaderConfigOld));
			try {
				//FileWriter always assumes default encoding is OK!
				output.write( sb.toString() );
			}
			finally {
				output.close();
			}

		} else {
			throw new IOException("file " + contextPath + "src/main/webapp/client/javascript/terrestris-suite.config.loader.js not found ...");
		}

		// INSERT IN DB
		this.getDatabaseDao().createEntity("Module", module);
	}

	/**
	 * Deletes a module in the database.
	 *
	 * @param module_name
	 */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
	public void deleteModule(String module_name) {

		this.getDatabaseDao().deleteEntityByValue(Module.class, "module_name", module_name);
	}


	/**
	 *
	 * @param objectType
	 * @return
	 */
	private Class<?> getHibernateModelByObjectType(String objectType) {
		Class<?> mappedClazz = null;
		for (String dbEntityPackage : this.dbEntitiesPackages) {
			try {
				mappedClazz = Class.forName(dbEntityPackage + "." + objectType);
			} catch (Exception e) {
				// DO NOTHING
			}
		}

		return mappedClazz;
	}

	/**
	 * @return the dbEntitiesPackages
	 */
	public ArrayList<String> getDbEntitiesPackages() {
		return dbEntitiesPackages;
	}

	/**
	 * @param dbEntitiesPackages the dbEntitiesPackages to set
	 */
	@Autowired
	public void setDbEntitiesPackages(ArrayList<String> dbEntitiesPackages) {
		this.dbEntitiesPackages = dbEntitiesPackages;
	}

}
