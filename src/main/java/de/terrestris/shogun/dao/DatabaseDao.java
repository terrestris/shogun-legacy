package de.terrestris.shogun.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.SessionImpl;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.terrestris.shogun.exception.ShogunDatabaseAccessException;
import de.terrestris.shogun.hibernatecriteria.filter.Filter.LogicalOperator;
import de.terrestris.shogun.hibernatecriteria.filter.HibernateFilter;
import de.terrestris.shogun.hibernatecriteria.filter.HibernateFilterItem;
import de.terrestris.shogun.hibernatecriteria.paging.HibernatePagingObject;
import de.terrestris.shogun.hibernatecriteria.sort.HibernateSortItem;
import de.terrestris.shogun.hibernatecriteria.sort.HibernateSortObject;
import de.terrestris.shogun.model.Group;
import de.terrestris.shogun.model.Role;
import de.terrestris.shogun.model.User;


/**
 * The database Data Access Object of SHOGun.
 *
 * <p>
 * This is a generic database DAO in order to do queries against the
 * connected database using Hibernate/Hibernate Spatial.
 * </p>
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@Repository
@Transactional
@Primary
public class DatabaseDao {

	/**
	 * the Hibernate SessionFactory reference
	 */
	private SessionFactory sessionFactory;


	/**
	 * Retrieves entities of the database by a given filter, sort-object
	 * and paging-object
	 *
	 * @param hibernateSortObject
	 * @param hibernateFilter
	 * @param hibernatePagingObject
	 * @param hibernateAdditionalFilter
	 * @return
	 * @throws ShogunDatabaseAccessException
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getDataByFilter(HibernateSortObject hibernateSortObject,
			HibernateFilter hibernateFilter,
			HibernatePagingObject hibernatePagingObject,
			HibernateFilter hibernateAdditionalFilter) throws ShogunDatabaseAccessException {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(
				hibernateSortObject.getMainClass());

		// PAGING
		if (hibernatePagingObject != null) {
			criteria.setFirstResult(hibernatePagingObject.getStart());
			criteria.setMaxResults(hibernatePagingObject.getLimit());
		}

		// SORT
		List<HibernateSortItem> hibernateSortItems = hibernateSortObject.getSortItems();

		for( HibernateSortItem hibernateSortItem : hibernateSortItems) {
			criteria.addOrder(hibernateSortItem.createHibernateOrder());
		}

		/*
		 * Check additional filters:
		 * These are being sent from a client and represent AND conditions to be
		 * applied globally.
		 * The use-case that lead us to implement the additionalFilter is
		 * the requirement that users are allowed to have both a AND and an OR
		 * filter (e.g. in the frontend for EigeneLayer).
		 * Usually one would implement this requirement with nested logical
		 * filters
		 */
		if (hibernateAdditionalFilter != null) {
			Conjunction afConjunction = Restrictions.conjunction();
			Criterion afCriterion = null;
			try {
				HibernateFilterItem hfi = (HibernateFilterItem) hibernateAdditionalFilter.getFilterItem(0);
				afCriterion = hfi.makeCriterion(hibernateAdditionalFilter.getMainClass());
				afConjunction.add(afCriterion);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ShogunDatabaseAccessException(
						"Error creating a criterion for additionalFilter.", e);
			}

			criteria.add(afConjunction);
		}


		// FILTER
		int filterItemCount = hibernateFilter.getFilterItemCount();

		if (filterItemCount > 0) {

			Disjunction dis = Restrictions.disjunction();

			// OR connected filter items
			if (hibernateFilter.getLogicalOperator().equals(LogicalOperator.OR)) {

				try {
					for (int i = 0; i < filterItemCount; i++) {
						HibernateFilterItem hfi = (HibernateFilterItem) hibernateFilter.getFilterItem(i);
						Criterion criterion = hfi.makeCriterion(hibernateFilter.getMainClass());
						if (criterion != null) {
							dis.add(criterion);
						}

					}
					criteria.add(dis);

				} catch (Exception e) {
					throw new ShogunDatabaseAccessException(
							"Error while adding an OR connected filter", e);
				}

			} else {
				// AND connected filter items
				try {
					Conjunction conjunction = Restrictions.conjunction();
					for (int i = 0; i < filterItemCount; i++) {
						HibernateFilterItem hfi = (HibernateFilterItem) hibernateFilter.getFilterItem(i);
						Criterion criterion = hfi.makeCriterion(hibernateFilter.getMainClass());
						if (criterion != null) {
							conjunction.add(criterion);
						}
					}
					criteria.add(conjunction);

				} catch (Exception e) {
					throw new ShogunDatabaseAccessException(
							"Error while adding an AND connected filter", e);
				}
			}

		}

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}


	/**
	 * Method returns distinct field values of a defined entity type. <br>
	 * For example: retrieving all streets of the user table without
	 * duplicated values
	 * <br>
	 * <br>
	 * You can decide whether the returned field values should be
	 * filtered by the current group logged in in the session or
	 * if all values are returned
	 *
	 * @param clazz The model which should be filtered as Class object
	 * @param field The field which should be returned
	 * @param groupDependent flag to decide whether it is filtered by the
	 * 						 current group
	 *
	 * @return List of String representations of the values of the desired field
	 *
	 */
	public List<String> getDistinctEntitiesByField(Class<?> clazz, String field,
			boolean groupDependent) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(
				clazz);

		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property(field), field)));

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}


	/**
	 * Returns an list of all Objects of a given Entity class,
	 * which is specified by the parameter clazz<br>
	 * For example: Get a all Modules which are stored in tbl_module <br>
	 *
	 * @param clazz The class of the entity to be used
	 * @return The object list fulfilling the filter request
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getAllEntities(Class<?> clazz) {

		Criteria criteria = null;
		criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	/**
	 * Determines all records of the passed entity owned by the
	 * currently logged in user.
	 *
	 * @param clazz
	 * @return
	 */
	public List<Object> getAllEntitiesByUser(Class<?> clazz) {

		// get user ID of logged in User and check if there is the
		// and it to the query as WHERE
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);
		int userId = this.getUserIdFromSession();
		criteria.add(Restrictions.eq("user_id", userId));
		List<Object> records = criteria.list();

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return records;
	}


	/**
	 * Returns an object of a certain entity defined by its ID.
	 *
	 * @param id the ID of the object to query
	 * @param clazz The entity to be queried
	 * @return the object matching the passed entity and the passed ID
	 */
	public Object getEntityById(int id, Class<?> clazz) throws ShogunDatabaseAccessException {

		Criteria criteria = null;
		criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq("id", id));

		// we expect a single record or null
		Object result = criteria.uniqueResult();

		return result;
	}

	/**
	 * Returns a list of object of a certain entity defined by its ID.
	 *
	 * @param values a list of IDs as array
	 * @param clazz The entity to be queried
	 * @return the objects matching the passed entity and the passed IDs
	 */
	@SuppressWarnings("unchecked")
	public List<? extends Object> getEntitiesByIds(Object[] values, Class clazz) {

		Criteria criteria = null;
		criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);
		if (values.length > 0) {
			criteria.add(Restrictions.in("id", values));
		}

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	/**
	 * Returns all entites besides the ones passed (as IDs).
	 *
	 * @param values a list of IDs as array
	 * @param clazz The entity to be queried
	 * @return the objects matching the passed entity and NOT matching the passed IDs
	 */
	@SuppressWarnings("unchecked")
	public List<? extends Object> getEntitiesByExcludingIds(Object[] values, Class clazz) {

		Criteria criteria = null;
		criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);

		if (values.length > 0) {
			criteria.add(Restrictions.not(Restrictions.in("id", values)));
		}

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	/**
	 * Returns an Object by a String comparison of a specified field
	 * <br>
	 * For example: Get a country by a given name
	 * (Return the country where the name equals GERMANY)
	 * <br>
	 * <br>
	 * NOTE: The operator used is 'ILIKE'
	 * <br>
	 * <br>
	 *
	 * @param clazz The class of the object model to be used
	 * @param fieldname the column which should be filtered
	 * @param value the value to filter
	 *
	 * @return The object fulfilling the filter request
	 * @throws ShogunDatabaseAccessException
	 */
	public Object getEntityByStringField(
			Class clazz, String fieldname, String value) throws ShogunDatabaseAccessException {

		HashMap<String, String> fieldsAndValues = new HashMap<String, String>();
		fieldsAndValues.put(fieldname, value);

		return this.getEntityByStringFields(clazz, fieldsAndValues);
	}


	/**
	 * Returns an Object by a String comparison of specified fields <br>
	 * For example: Get a record by a given name
	 * (Return the city where the name equals NEUSTADT) and its postcode.
	 * <br>
	 * <br>
	 * NOTE: The operator used is 'ILIKE'
	 * <br>
	 * <br>
	 * TODO: add an explicit connector (AND/OR, no usage of default because
	 *       of readability)
	 * TODO: exception handling
	 *
	 * @param clazz
	 * @param fieldsAndValues
	 *
	 * @return The object fulfilling the filter request
	 * @throws ShogunDatabaseAccessException
	 */
	public Object getEntityByStringFields(Class clazz,
			HashMap<String, String> fieldsAndValues) throws ShogunDatabaseAccessException {

		Criteria criteria = null;
		Object returnObject = null;
		try {

			criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);

			for(Iterator<String> iter = fieldsAndValues.keySet().iterator(); iter.hasNext();) {

				String fieldname = iter.next();
				String value = fieldsAndValues.get(fieldname);

				criteria.add(Restrictions.ilike(fieldname, value));
			}

			List<Object> objectList = criteria.list();


			if (objectList.size() > 0) {
				returnObject = objectList.get(0);
			}

		} catch (Exception e) {
			throw new ShogunDatabaseAccessException(
					"Error getting entity " + clazz.getSimpleName() +
					" by text field.", e);
		}

		return returnObject;
	}

	/**
	 * Returns a set of Objects from database by a Integer comparison
	 * of a specified field <br>
	 *
	 * @param clazz
	 * @param fieldname
	 * @param value
	 * @return
	 */
	public List<Object> getEntitiesByIntegerField(Class clazz, String fieldname, Integer value) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq(fieldname, value));

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	/**
	 * Creates a record of a given Entity in the database
	 *
	 * @param entityClass Entity class of the new object
	 * @param objToCreate the new object to be created in the DB
	 * @return the created object
	 */
	public Object createEntity(String entityClass, Object objToCreate) {

		this.sessionFactory.getCurrentSession().save(entityClass, objToCreate);

		return objToCreate;
	}

	/**
	 * Creates a record of a given Entity in the database. This method will return the
	 * newly created object, whereas the createEntity(String entityClass,
	 * Object objToCreate) will return the originally passed object.
	 *
	 * @param objToCreate the new object to be created in the DB
	 * @return the object that was created in the database
	 * @throws ShogunDatabaseAccessException
	 */
	public <T> T createEntity(T objToCreate) throws ShogunDatabaseAccessException {

		Class<? extends Object> clazz = objToCreate.getClass();
		Object createdObjectId = this.getSessionFactory().getCurrentSession().save(
				clazz.getSimpleName(), objToCreate);
		return (T)this.getEntityById((Integer)createdObjectId, clazz);
	}

	/**
	 * Creates or updates a record of a given Entity in the database. <br>
	 * Method checks automatically if the passed object has to be created or
	 * updated.
	 *
	 * @param entityClass Entity class of the object
	 * @param objToCreateOrUpdate the new object to be created/updated in the DB
	 * @return the created/updated object
	 */
	public Object createOrUpdateEntity(String entityClass, Object objToCreateOrUpdate) {

		this.sessionFactory.getCurrentSession().saveOrUpdate(entityClass, objToCreateOrUpdate);

		return objToCreateOrUpdate;
	}

	/**
	 * Updates a record of a given Entity in the database with the passed one
	 *
	 * @param entityClass Entity class of the object
	 * @param objToUpdate the object to be updated in the DB
	 * @return the updated object
	 */
	public Object updateEntity(String entityClass, Object objToUpdate) {

		Object updatedObject = this.sessionFactory.getCurrentSession().merge(entityClass, objToUpdate);
		this.sessionFactory.getCurrentSession().flush();

		return updatedObject;
	}

	/**
	 * Deletes a record of a given Entity in the database. The record to be
	 * deleted is defined by its ID.
	 *
	 * @param clazz Entity class of the object to be deleted
	 * @param id the ID of the record to be deleted
	 */
	public void deleteEntity(Class clazz, Integer id) {

		// delete the object record
		Object record = this.sessionFactory.getCurrentSession().load(clazz, id);
		this.sessionFactory.getCurrentSession().delete(record);
	}

	/**
	 * Deletes a record of a given Entity in the database. The record to be
	 * deleted is defined by a certain value of one its data columns.
	 * (For example WHERE name='Peter')
	 *
	 * @param clazz Entity class of the object to be deleted
	 * @param column Column name which is used to determine the record
	 * @param value The value which is used to determine the record
	 */
	public void deleteEntityByValue(Class clazz, String column, String value) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq(column, value));
		List<Object> records = criteria.list();

		Object record = null;
		if(records.size() == 1) {
			record = records.get(0);
		}

		this.sessionFactory.getCurrentSession().delete(record);
	}

	/**
	 * Delete an object of an entity NOT regarding the logged
	 * in user is in the same group
	 *
	 * @param clazz Class object defining the entity
	 * @param id the id to delete
	 *
	 * @throws Exception
	 */
	public void deleteEntityGroupDependent(Class clazz, Integer id) throws ShogunDatabaseAccessException {

		// get group ID of logged in User and check if there is the
		// user to be deleted is a child of the current group
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq("id", id));

		List<Integer> groupIdsOfSessionUser = this.getGroupIdsFromSession();

		if (groupIdsOfSessionUser.size() > 0) {
			criteria.createCriteria("groups")
				.add(Restrictions.in("id", groupIdsOfSessionUser));
		}

		List<Object> records = criteria.list();

		// group check --> user allowed to delete this instance?
		if (records.size() > 0) {

			// delete the instance record in DB
			this.deleteEntity(clazz, id);

		} else {
			throw new ShogunDatabaseAccessException(
				"The " + clazz.getSimpleName() +
				" to be deleted is not accessible for the logged in user!");
		}
	}


	// ---------------------------------------------------------------------------

	// USER RELATED STUFF

	// ---------------------------------------------------------------------------

	/**
	 * Returns the {@link User} object defined by the passed user name.
	 *
	 * @param name the user_name of the record in the database
	 * @return
	 * @throws ShogunDatabaseAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUserByName(String name)
										throws ShogunDatabaseAccessException {

		Criteria criteria = null;

		try {

			criteria = this.sessionFactory.getCurrentSession().createCriteria(User.class);

			criteria.add(Restrictions.ilike("user_name", name));

		} catch (Exception e) {
			throw new ShogunDatabaseAccessException(
					"Error while getting User by name: " + name, e);
		}

		// we have to ensure that the modules are distinct
		// @see http://docs.jboss.org/hibernate/orm/3.6/javadocs/org/hibernate/Criteria.html#createAlias(java.lang.String, java.lang.String, int)
		criteria.createAlias("modules", "module", CriteriaSpecification.INNER_JOIN);

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	/**
	 * Returns the {@link User} object defined by the passed user name.
	 *
	 * @param name the user_name of the record in the database
	 * @return
	 * @throws ShogunDatabaseAccessException
	 */
	public User getUserByName(String name,
			String additionalCriteriaPath, Criterion additionalCriterion) throws ShogunDatabaseAccessException {

		Criteria criteria = null;

		try {

			criteria = this.sessionFactory.getCurrentSession().createCriteria(User.class);

			// add additional restrictions like
			// where user is in group with ID xy
			if (additionalCriteriaPath != null &&
					additionalCriteriaPath.isEmpty() == false &&
					additionalCriterion != null) {

				criteria.createCriteria(additionalCriteriaPath)
					.add(additionalCriterion);
			}

			criteria.add(Restrictions.ilike("user_name", name));

		} catch (Exception e) {
			throw new ShogunDatabaseAccessException(
					"Error while getting User by name: " + name, e);
		}

		// we have to ensure that the modules are distinct
		// @see http://docs.jboss.org/hibernate/orm/3.6/javadocs/org/hibernate/Criteria.html#createAlias(java.lang.String, java.lang.String, int)
		criteria.createAlias("modules", "module", CriteriaSpecification.INNER_JOIN);

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return (User) criteria.uniqueResult();
	}



	/**
	 * Create a new User on the database or
	 * update User
	 */
	public User saveUser(User user, boolean isNew){

		// check if update or create
		if (isNew == false) {
			// it is an update
			this.sessionFactory.getCurrentSession().merge(user);
			this.sessionFactory.getCurrentSession().flush();
		} else {
			// you are saving a new one
			this.sessionFactory.getCurrentSession().save(user);
		}

		return user;
	}


	/**
	 * Creates a new {@link User} object in the database.
	 * The gets the role passed as String.
	 * If needed the current group of the logged in user is stored
	 * to the new user.
	 *
	 * @param user the User object to create
	 * @param role the role name
	 * @param setSessionGroup flag controls if the current user group should be set to new user
	 * @return
	 * @throws Exception
	 */
	public User createUser(User user, String role, boolean setSessionGroup) throws ShogunDatabaseAccessException {

		try {

			this.sessionFactory.getCurrentSession().save(user);

			if (setSessionGroup == true) {

				try {

					// add the saved user to current session group
					Group sessionGroup = this.getFirstGroupObjectFromSessionUser();

					if (sessionGroup != null) {

						sessionGroup.getUsers().add(user);

						// persist the changes of the group object
						this.updateEntity("Group", sessionGroup);
					}

				} catch (Exception e) {
					throw new ShogunDatabaseAccessException(
							"Error adding the saved user to current session group. " + e.getMessage());
				}
			}

			// create the mapping of new user and its role
			Role oRole = (Role)this.getEntityByStringField(Role.class, "name", role);
			Set<Role> roles = new HashSet<Role>();
			roles.add(oRole);

			user.setRoles(roles);

		} catch (Exception e) {
			throw new ShogunDatabaseAccessException(
					"Error creating User with roles. ", e);
		}

		return user;
	}

	/**
	 * TODO: this method should call the internal updateEntity-method!
	 */
	public User updateUser(User user) {

		this.sessionFactory.getCurrentSession().merge(user);
		this.sessionFactory.getCurrentSession().flush();

		return user;
	}

	/**
	 * Deletes a user instance given by its user id. <br>
	 *
	 * Here it is NOT checked if the logged in group matches the one stored
	 * for the user. This method is intended to be called by the super user
	 * with the role ROLE_SUPERADMIN
	 *
	 * @param id
	 * @throws Exception
	 */
	public void deleteUser(int id) throws ShogunDatabaseAccessException {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("id", id));
		User userToDelete = (User) criteria.uniqueResult();

		if (userToDelete != null) {

			// delete the user instance
			this.deleteEntity(User.class, userToDelete.getId());

		} else {
			throw new ShogunDatabaseAccessException(
					"No User found with ID " + id);
		}
	}

	/**
	 * Empties the session completely.
	 * <br><br>
	 * <b>BE CAREFULL in use with this!</b>
	 *
	 * @see http://www.torsten-horn.de/techdocs/java-hibernate.htm#First-Level-Cache
	 */
	public void clearSession() {
		 this.sessionFactory.getCurrentSession().clear();
	}

	/**
	 * Return the total count of a request.
	 * Additionally a global AND filter could be passed.
	 *
	 * @param hibernateFilter the {@link HibernateFilter} object which filters the amount of records
	 * @param hibernateAdditionalFilter an additional {@link HibernateFilter} connected with a global AND
	 *   to the rest of the query filter. Most of the cases not needed (pass null)
	 * @return the amount of matching records
	 * @throws ShogunDatabaseAccessException
	 */
	public long getTotal(HibernateFilter hibernateFilter, HibernateFilter hibernateAdditionalFilter) throws ShogunDatabaseAccessException{

		Criteria criteria = null;

		try {
			criteria = this.sessionFactory.getCurrentSession().createCriteria(hibernateFilter.getMainClass());

		} catch (Exception e) {
			throw new ShogunDatabaseAccessException(
					"The requested model " + hibernateFilter.getMainClass() +
					" is not defined ", e);
		}

		/*
		 * check additional filters:
		 *
		 * These are being sent from a client and represent AND conditions to be
		 * applied globally.
		 * The use-case that lead us to implement the additionalFilter is
		 * the requirement that users are allowed to have both a AND and an OR
		 * filter.
		 * Usually one would implement this requirement with nested logical
		 * filters
		 */
		if (hibernateAdditionalFilter != null) {
			Conjunction afConjunction = Restrictions.conjunction();
			Criterion afCriterion = null;
			try {
				HibernateFilterItem hfi = (HibernateFilterItem) hibernateAdditionalFilter.getFilterItem(0);
				afCriterion = hfi.makeCriterion(hibernateAdditionalFilter.getMainClass());
				afConjunction.add(afCriterion);
			} catch (Exception e) {
				throw new ShogunDatabaseAccessException(
						"Error creating a criterion for additionalFilter.", e);
			}

			criteria.add(afConjunction);
		}

		criteria.setProjection(Projections.count("id"));

		if (hibernateFilter != null) {

			int filterItemCount = hibernateFilter.getFilterItemCount();

			// FILTER
			if (hibernateFilter.getLogicalOperator().equals(LogicalOperator.OR)) {

				Disjunction dis = Restrictions.disjunction();

				try {
					for (int i = 0; i < filterItemCount; i++) {
						HibernateFilterItem hfi = (HibernateFilterItem) hibernateFilter.getFilterItem(i);
						Criterion criterion = hfi.makeCriterion(hibernateFilter.getMainClass());
						if (criterion != null) {
							dis.add(criterion);
						}
					}
					criteria.add(dis);

				} catch (Exception e) {
					throw new ShogunDatabaseAccessException(
							"Error while combining criteria with OR", e);
				}

			} else {

				try {
					for (int i = 0; i < filterItemCount; i++) {
						HibernateFilterItem hfi = (HibernateFilterItem) hibernateFilter.getFilterItem(i);
						Criterion criterion = hfi.makeCriterion(hibernateFilter.getMainClass());
						if (criterion != null) {
							criteria.add(criterion);
						}
					}

				} catch (Exception e) {
					throw new ShogunDatabaseAccessException(
							"Error while combining criteria with AND", e);
				}
			}
		}

		List<?> totalList = criteria.list();

		return (Long)totalList.get(0);
	}


	// ---------------------------------------------------------------------------

	// SESSION RELATED STUFF

	// ---------------------------------------------------------------------------


	/**
	 * Determines the name of the logged in user from Security Context
	 *
	 * @return the name of the logged in user or NULL if not found
	 */
	public String getUserNameFromSession() {
		// get the authorization context, incl. user name
		Authentication authResult = SecurityContextHolder.getContext().getAuthentication();

		if (authResult != null) {
			return authResult.getName();
		}
		else {
			return null;
		}
	}

	/**
	 * Determines the ID of the logged in user from Security Context
	 *
	 * @return the name of the logged in user or NULL if not found
	 */
	public Integer getUserIdFromSession() {

		String username = this.getUserNameFromSession();

		if (username != null) {

			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(User.class);

			criteria.setProjection(Projections.property("id"));
			criteria.add(Restrictions.eq("user_name", username));

			Integer id = (Integer)criteria.list().get(0);

			return id;
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the {@link User} object of the logged in user
	 *
	 * @return the {@link User} object of the logged in user or NULL if not found
	 */
	public User getUserObjectFromSession() {

		// get the authorization context, incl. user name
		Authentication authResult = SecurityContextHolder.getContext().getAuthentication();

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(User.class);

		criteria.add(Restrictions.ilike("user_name", authResult.getName()));

		if (criteria.list().size() > 0) {
			return (User)criteria.list().get(0);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the IDs of all groups ID of the logged in user
	 *
	 * @return
	 * @throws ShogunDatabaseAccessException
	 * @throws Exception
	 */
	public List<Integer> getGroupIdsFromSession() {

		Set<Group> sessionGroups = this.getGroupObjectsFromSessionUser();
		List<Integer> groupIdsOfSessionUser = new ArrayList<Integer>();

		for (Group group : sessionGroups) {
			groupIdsOfSessionUser.add(group.getId());
		}

		return groupIdsOfSessionUser;
	}

	/**
	 * Returns all {@link Group} objects of the logged in user.
	 *
	 * @return the set of {@link Group} objects of the logged in user
	 * 			or NULL if not found
	 */
	public Set<Group> getGroupObjectsFromSessionUser() {

		// get the logged-in user
		User sessionUser = this.getUserObjectFromSession();

		// get a set of the groups of the logged-in user
		if (sessionUser != null) {
			return sessionUser.getGroups();
		} else {
			return null;
		}
	}

	/**
	 * Returns the first {@link Group} object of the logged in user.
	 *
	 * @return the first {@link Group} object of the logged in user or
	 * 			NULL if not found
	 */
	public Group getFirstGroupObjectFromSessionUser() {

		// get the logged-in user
		User sessionUser = this.getUserObjectFromSession();

		if (sessionUser != null && sessionUser.getGroups() != null) {
			return sessionUser.getGroups().iterator().next();
		} else {
			return null;
		}
	}

	/**
	 * Determines if the logged in User is a SuperAdmin.
	 *
	 * @return flag SuperAdmin=true/false
	 */
	public boolean isSuperAdmin() {

		// get the logged-in user and check if he has the SuperAdmin role
		User sessionUser = this.getUserObjectFromSession();
		Set<Role> roles = sessionUser.getRoles();

		for (Role role : roles) {
			if (role.getName().equals(User.ROLENAME_SUPERADMIN)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Helper function to print out the SQL from a criteria object
	 *
	 * @param criteria
	 * @return
	 */
	private String toSql(Criteria criteria) {
		try {
			CriteriaImpl c = (CriteriaImpl) criteria;
			SessionImpl s = (SessionImpl) c.getSession();
			SessionFactoryImplementor factory = (SessionFactoryImplementor) s.getSessionFactory();
			String[] implementors = factory.getImplementors(c.getEntityOrClassName());

			CriteriaLoader loader = new CriteriaLoader(
					(OuterJoinLoadable) factory
							.getEntityPersister(implementors[0]),
					factory, c, implementors[0], s.getLoadQueryInfluencers());
			Field f = OuterJoinLoader.class.getDeclaredField("sql");
			f.setAccessible(true);
			return (String) f.get(loader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets the SessionFactory of Hibernate via Spring's DI
	 *
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @return the session factory of Hibernate
	 */
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

}
