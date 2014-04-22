package de.terrestris.shogun.dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.Modifier;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
import de.terrestris.shogun.model.BaseModel;
import de.terrestris.shogun.model.BaseModelInheritance;
import de.terrestris.shogun.model.BaseModelInterface;
import de.terrestris.shogun.model.Group;
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
	 * the logger instance
	 */
	private static Logger LOGGER = Logger.getLogger(DatabaseDao.class);


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
			Set<String> fields,
			Set<String> ignoreFields,
			HibernatePagingObject hibernatePagingObject,
			HibernateFilter hibernateAdditionalFilter) throws ShogunDatabaseAccessException {

		boolean isPlainModelRequest = (fields == null && ignoreFields == null);
		Class<?> clazz = hibernateSortObject.getMainClass();

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);

		// Fields
		if (fields != null) {
			ProjectionList pl = Projections.projectionList();

			for (Iterator<String> iterator = fields.iterator(); iterator.hasNext();) {
				String field = iterator.next();
				pl.add(Projections.property( field));
			}
			criteria.setProjection(Projections.distinct(pl));

		}

		// Ignore Fields
		// -> get all fields of the class and remove the ignorefields, works like a blacklist
		Set<String> cleanedFieldNames = new HashSet<String>();
		if (ignoreFields != null) {
			ProjectionList pl = Projections.projectionList();
			List<Field> allFields = getAllFields(new ArrayList<Field>(), clazz);

			for (Iterator<Field> iterator = allFields.iterator(); iterator.hasNext();) {
				Field field = (Field) iterator.next();

				if (!ignoreFields.contains(field.getName())) {
					cleanedFieldNames.add(field.getName());
				}

			}

			for (Iterator<String> iterator = cleanedFieldNames.iterator(); iterator.hasNext();) {
				String cleanField = iterator.next();

				pl.add(Projections.property(cleanField), cleanField);
			}
			criteria.setProjection(Projections.distinct(pl));
		}

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
				afCriterion = hfi.makeCriterion(clazz);
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
			// OR connected filter items
			if (hibernateFilter.getLogicalOperator().equals(LogicalOperator.OR)) {
				try {
					Disjunction dis = Restrictions.disjunction();
					for (int i = 0; i < filterItemCount; i++) {
						HibernateFilterItem hfi = (HibernateFilterItem) hibernateFilter.getFilterItem(i);
						if (hfi.getFieldName() != null && hfi.getFieldName().contains(".")) {
							String ownFieldName = hfi.getFieldName().split("\\.")[0];

							criteria.createCriteria(ownFieldName, ownFieldName);

							// todo move outside
							Criterion criterion = hfi.makeCriterion(clazz);
							if (criterion != null) {
								dis.add(criterion);
							}

						}
						else {
							Criterion criterion = hfi.makeCriterion(clazz);
							if (criterion != null) {
								dis.add(criterion);
							}
						}
					}
					criteria.add(dis);

				} catch (Exception e) {
					throw new ShogunDatabaseAccessException("(getDataByFilter)" +
							" Error while adding an OR connected filter: "
							+ e.getMessage(), e);
				}

			} else {
				// AND connected filter items
				try {
					Conjunction conjunction = Restrictions.conjunction();
					for (int i = 0; i < filterItemCount; i++) {
						HibernateFilterItem hfi = (HibernateFilterItem) hibernateFilter.getFilterItem(i);
						if (hfi.getFieldName() != null && hfi.getFieldName().contains(".")) {

							String ownFieldName = hfi.getFieldName().split("\\.")[0];

							criteria.createCriteria(ownFieldName, ownFieldName);

							// todo move outside
							Criterion criterion = hfi.makeCriterion(clazz);
							if (criterion != null) {
								conjunction.add(criterion);
							}

						}
						else {
							Criterion criterion = hfi.makeCriterion(clazz);
							if (criterion != null) {
								conjunction.add(criterion);
							}
						}
					}
					criteria.add(conjunction);

				} catch (Exception e) {
					throw new ShogunDatabaseAccessException("(getDataByFilter)" +
							" Error while adding an AND connected filter", e);
				}
			}
		}

		// Ok we're done creating the criteria.

//		System.out.println("Querying for " + clazz.getSimpleName() + " with this SQL:");
//		String niceSql = (new BasicFormatterImpl()).format(this.toSql(criteria));
//		System.out.println(niceSql);

		// next we need to know whether we are being filtered with fields
		// because we then do NOT get a List of instances of BaseModelInterface.

		List<Object> list = null;

		if (isPlainModelRequest == false) {
			// We are filtered and will have to create a sane hashmap instead of
			// relying on the serilisation of BaseModelInterface classes.

			// Please beware that we can NOT setResultTransformer here,
			// otherwise we'll loose all but the first filtered field.

			if (fields == null && cleanedFieldNames.size() > 0) {
				fields = cleanedFieldNames;
			}
			// we dont really know what criteria.list() will return, can be List<Object> or List<Object[]>
			// will be determined later
			List<Object> rawListOfResults = criteria.list();
			List<Object> saneResultList = new ArrayList<Object>();
			for (Object rawRow : rawListOfResults) {

				Map<String, Object> newRowMap = new HashMap<String, Object>();
				int fieldIdx = 0;
				for (Iterator<String> fieldIter = fields.iterator(); fieldIter.hasNext();) {
					String fieldName = fieldIter.next();
					Object fieldVal = null;

					if (rawRow != null) {
						if (rawRow.getClass().isArray()) {
							Object[] objArr = (Object[]) rawRow;
							fieldVal = objArr[fieldIdx];
						} else {
							fieldVal = rawRow;
						}
					}

					// store the pair in the newRowMap.
					newRowMap.put(fieldName, fieldVal);

					fieldIdx++;
				}
				// OK, one result row has been trasformed, store it back
				saneResultList.add(newRowMap);
			}

			// now overwrite the list we'll rerturn to the caller.
			list = saneResultList;
		} else {
			// We are NOT filtered, we can rely on the serialization process
			// that takes instances of ou models and transforms them
			// to (possibly huge) JSON structures.

			// Please beware that we can only setResultTransformer here,
			// otherwise we'd loose all but the first filtered field.

			// this ensures that no cartesian product is returned when
			// having sub objects, e.g. User <-> Modules
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			// we need to set the fetch mode for all sets in our class, as most
			// of them are defined to be fetched lazily:

			criteria = this.setEagerFetchModeForCollections(criteria, clazz);

			list = criteria.list();
		}

		// Since we have modelled entities with sub entities with the lazy
		// fetching strategy, we have to check whether we should
		// initialize the needed fields.
		//
		// This will only happen
		//   * if we got a raw result,
		//   * and we weren't being filtered for only a subset of fields
		//   * and if we have been explicitly been told to go deep.

		// as we do not use LAZY at the moment, this will not be fired!
//		if (list != null && deepInitialize == true && fields == null) {
//			this.initializeDeep(list, hibernateSortObject.getMainClass());
//		}

		return list;
	}

	private Criteria setEagerFetchModeForCollections(Criteria criteria,
			Class<?> clazz) {

		List<Field> fields = getAllFields(new ArrayList<Field>(), clazz);

		for (Field field : fields) {

			boolean isJsonIgnore = false;

			Method getterMethod;
			try {
				getterMethod = new PropertyDescriptor(field.getName(), clazz).getReadMethod();
				Annotation[] anoArr = getterMethod.getAnnotations();
				for (Annotation annotation : anoArr) {
					if (annotation instanceof JsonIgnore) {
						isJsonIgnore = true;
					}
				}
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}




			if (!isJsonIgnore && field.getType().isAssignableFrom(Set.class)) {
				// yes, we have to set the fetch mode
				criteria.setFetchMode(field.getName(), FetchMode.JOIN);
			}
		}

		return criteria;
	}

	/**
	 * TODO move to a better place or use existing functionality elsewhere.
	 * TODO we have a very similar method in {@link HibernateFilterItem}.
	 *
	 * @param fields
	 * @param type
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IntrospectionException
	 */
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
		for (Field field: type.getDeclaredFields()) {

			// check if the filed is not a constant
			if(Modifier.isStatic(field.getModifiers()) == false &&
					Modifier.isFinal(field.getModifiers()) == false) {

				// now we check if the readmethod of the field
				// has NOT a transient annotation
				try {
					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), type);
					Method readmethod = pd.getReadMethod();
					Annotation[] annotationsArr = readmethod.getAnnotations();
					if (annotationsArr.length == 0) {
						fields.add(field);
					} else {
						for (Annotation annotation : annotationsArr) {
							if(annotation.annotationType().equals(javax.persistence.Transient.class) == false) {
								fields.add(field);
							}
						}
					}
				} catch (IntrospectionException e) {
					LOGGER.error("Trying to determine the getter for field '" +
							field.getName() + "' in " + type.getSimpleName() +
							" threw IntrospectionException." +
							" Is there a getter following the Java-Beans" +
							" Specification?");
				}
			}

		}

		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}

		return fields;
	}

	/**
	 * TODO turn the logic around... initializeDeep(List, Class) should make
	 * many calls to this method, not the other way around.
	 *
	 *
	 * @param obj
	 * @param mainClass
	 */
	protected void initializeDeep(Object obj, Class<?> mainClass) {
		List<Object> list = new ArrayList<Object>();
		list.add(obj);
		this.initializeDeepList(list, mainClass);
	}



	/**
	 *
	 * @param list
	 * @param mainClass
	 */
	protected void initializeDeepList(List<? extends Object> list, Class<?> mainClass) {
		List<Field> fields = getAllFields(new ArrayList<Field>(), mainClass);
		List<Method> methods = new ArrayList<Method>();

		for (Field field : fields) {
			if (field.getType().isAssignableFrom(Set.class)) {
				// yes, we have to initialize this field via its getter
				Method method = null;
				try {
					method = new PropertyDescriptor(field.getName(), mainClass).getReadMethod();
				} catch (IntrospectionException e) {
					LOGGER.error("Failed to determine getter for field '" +
							field.getName() + "' of class '" +
							mainClass.getSimpleName() + "'.");
				}
				methods.add(method);
			}
		}

		for (Iterator<Object> iterator = (Iterator<Object>) list.iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
			if (obj == null) {
				continue;
			}
			for (Method method : methods) {
				String errMsg = "Failed to invoke getter '" +
						method.getName() + "' of class '" +
						mainClass.getSimpleName() + "': ";
				try {
					Hibernate.initialize(method.invoke(obj));
				} catch (HibernateException e) {
					LOGGER.error(errMsg + " HibernateException '" +
							e.getMessage() + "'.");
				} catch (IllegalArgumentException e) {
					LOGGER.error(errMsg + " IllegalArgumentException '" +
							e.getMessage() + "'.");
				} catch (IllegalAccessException e) {
					LOGGER.error(errMsg + " IllegalAccessException '" +
							e.getMessage() + "'.");
				} catch (InvocationTargetException e) {
					LOGGER.error(errMsg + " InvocationTargetException '" +
							e.getMessage() + "'.");
				}
			}
		}
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
	public List<Object> getAllEntities(Class<?> clazz, boolean... initializeDeep) {

		boolean initializeDeeply = initializeDeep.length > 0 ? initializeDeep[0] : false;

		Criteria criteria = null;
		criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List <Object> resultSetlist = (List<Object>)criteria.list();

		if (initializeDeeply == true) {
			this.initializeDeepList(resultSetlist, clazz);
		}

		return resultSetlist;
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
	 *
	 * @param id
	 * @param clazz
	 * @return
	 */
	public Object getEntityById(int id, Class<?> clazz) {
		return this.getEntityById(id, clazz, true);
	}


	/**
	 * Returns an object of a certain entity defined by its ID.
	 *
	 * @param id the ID of the object to query
	 * @param clazz The entity to be queried
	 * @return the object matching the passed entity and the passed ID
	 */
	public Object getEntityById(int id, Class<?> clazz, boolean initializeDeep) {

		Criteria criteria = null;
		criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq("id", id));
		// we expect a single record or null
		Object result = criteria.uniqueResult();

		if (initializeDeep) {
			this.initializeDeep(result, clazz);
		}

		return result;
	}

	// method used to keep the old behaviour, which means
	// getting entities without initializing lazy fields
	public List<? extends Object> getEntitiesByIds(Object[] values, Class<?> clazz) {
		return this.getEntitiesByIds(values, clazz, null);
	}

	public List<? extends Object> getEntitiesByIds(Set<?> values, Class<?> clazz) {
		Object[] objectValues = new Object[values.size()];
		int i = 0;
		for (Iterator<?> iterator = values.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			objectValues[i] = object;
			i++;
		}
		return this.getEntitiesByIds(objectValues, clazz, null);
	}

	/**
	 * Returns a list of object of a certain entity defined by its ID.
	 *
	 * @param values a list of IDs as array
	 * @param clazz The entity to be queried
	 * @return the objects matching the passed entity and the passed IDs
	 */
	@SuppressWarnings("unchecked")
	public List<? extends Object> getEntitiesByIds(Object[] values, Class<?> clazz, String[] eagerfields) {
		final int maxInElems = 999;
		Criteria criteria = null;
		criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);

		if (eagerfields != null && eagerfields.length > 0) {
			for (String field : eagerfields) {
				criteria.setFetchMode(field, FetchMode.JOIN);
			}
		}

		if (values.length > 0 && values.length <= maxInElems) {
			criteria.add(Restrictions.in("id", values));
		} else if (values.length > maxInElems) {
			List<Criterion> listOfInRestrictions = new ArrayList<Criterion>();
			int numSubArrays = (int) Math.ceil(((double) values.length) / ((double) maxInElems));
			int start = 0;
			for (int i = 0; i < numSubArrays; i++) {
				// int start = i * maxInElems;
				int end = (i + 1) * maxInElems;
				if (end > values.length) {
					end =  values.length;
				}
				Object[] subArr = Arrays.copyOfRange(values, start, end);
				listOfInRestrictions.add(Restrictions.in("id", subArr));
				start = end;
			}
			Disjunction disj = Restrictions.disjunction();
			for (Criterion restrictions : listOfInRestrictions) {
				disj.add(restrictions);
			}
			criteria.add(disj);
		} else {
			// we add a restriction that can never be fullfilled
			// this is the case when e.g. an empty object has been passed
			criteria.add(Restrictions.sqlRestriction("1 = 2"));
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
	public List<? extends Object> getEntitiesByExcludingIds(Object[] values, Class<?> clazz) {

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
			Class<?> clazz, String fieldname, String value) throws ShogunDatabaseAccessException {

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
		Object returnObject = null;
		List<Object> listOfEntities = this.getEntitiesByStringFields(clazz, fieldsAndValues);
		if (listOfEntities != null && listOfEntities.size() > 0) {
			returnObject = listOfEntities.get(0);
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
	public List<Object> getEntitiesByIntegerField(Class<?> clazz, String fieldname, Integer value) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq(fieldname, value));

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}


	/**
	 * Returns a set of objects from database by a boolean comparison
	 * with a specified field. <br>
	 *
	 * @param clazz The class of the object model to be used
	 * @param fieldname the column which should be filtered
	 * @param value the value to filter (type Boolean)
	 * @return The objects fulfilling the filter request
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getEntitiesByBooleanField(Class<T> clazz, String fieldname, Boolean value) {

		Criteria criteria =
			this.sessionFactory.getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq(fieldname, value));

		// this ensures that no cartesian product is returned when
		// having sub objects, e.g. User <-> Modules
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return (List<T>)criteria.list();
	}


	/**
	 * Returns a list of entities where the given fields match their respective
	 * values.
	 *
	 * <p>NOTE: The operator used is <code>'ILIKE'</code><p>
	 *
	 * @param clazz
	 * @param fieldsAndValues
	 * @return
	 * @throws ShogunDatabaseAccessException
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseModelInterface> List<T> getEntitiesByStringFields(Class<T> clazz, HashMap<String, String> fieldsAndValues) throws ShogunDatabaseAccessException {
		Criteria criteria = null;
		List<T> returnList = null;
		try {
			criteria = this.sessionFactory.getCurrentSession().createCriteria(clazz);
			for(Iterator<String> iter = fieldsAndValues.keySet().iterator(); iter.hasNext();) {
				String fieldname = iter.next();
				String value = fieldsAndValues.get(fieldname);
				criteria.add(Restrictions.ilike(fieldname, value));
			}

			returnList = (List<T>) criteria.list();

		} catch (Exception e) {
			throw new ShogunDatabaseAccessException(
					"Error getting entities of class " + clazz.getSimpleName() +
					" by text fields.", e);
		}

		return returnList;
	}


	/**
	 * Returns a list of entities where the given field matches the given value
	 *
	 * <p>NOTE: The operator used is <code>'ILIKE'</code><p>
	 *
	 * <p>This is a utility method to easily get a list of all entities
	 * matching a string comparison. Will construct a HashMap of the given field
	 * and value and then call
	 * {@link DatabaseDao#getEntitiesByStringFields(Class, HashMap)} to fetch
	 * the list of matching entities.</p>
	 *
	 * @param clazz
	 * @param fieldsAndValues
	 * @return
	 * @throws ShogunDatabaseAccessException
	 */
	public <T extends BaseModelInterface> List<T> getEntitiesByStringField(Class<T> clazz, String field, String value) throws ShogunDatabaseAccessException {
		HashMap<String, String> fieldsAndValues = new HashMap<String, String>();
		fieldsAndValues.put(field, value);
		return this.getEntitiesByStringFields(clazz, fieldsAndValues);
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
	public <T> T createEntity(T objToCreate) {

		Class<? extends Object> clazz = objToCreate.getClass();
		Object createdObjectId = this.getSessionFactory().getCurrentSession().save(
				clazz.getSimpleName(), objToCreate);
		return (T)this.getEntityById((Integer)createdObjectId, clazz);
	}

	/**
	 * Creates records for the given entities in the database. This method will
	 * return the newly created objects.
	 *
	 * @param objsToCreate the new objects to be created in the DB
	 * @return the objects that were created in the database
	 * @throws ShogunDatabaseAccessException
	 */
	@Transactional
	public <T extends BaseModel> List<T> createEntities(List<T> objsToCreate) {
		List<T> createdObjs = new ArrayList<T>();
		for (T t : objsToCreate) {
			createdObjs.add(this.createEntity(t));
		}
		return createdObjs;
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
	public void deleteEntity(Class<?> clazz, Integer id) {

		// delete the object record
		Object record = this.sessionFactory.getCurrentSession().load(clazz, id);
		this.sessionFactory.getCurrentSession().delete(record);
	}


	/**
	 * Deletes a record of a given entity-class in the database.
	 * The record to be deleted is given by its object representation.
	 *
	 * @param <T> Template class, here the class of the object to be deleted
	 * @param clazz Entity class of the object to be deleted
	 * @param objectToDelete the instance to be deleted from database
	 */
	public <T> void deleteEntity(Class<T> clazz, BaseModelInterface objectToDelete) {
		this.sessionFactory.getCurrentSession().delete(objectToDelete);
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
	public void deleteEntityByValue(Class<?> clazz, String column, String value) {

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
	public void deleteEntityGroupDependent(Class<?> clazz, Integer id) throws ShogunDatabaseAccessException {

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


	/**
	 * Deletes all entities in the given list.
	 *
	 * <p>This method is supposed to be called with lists of instances which
	 * implement the {@link BaseModelInterface}. This qualifies all
	 * subclasses of either {@link BaseModel} or {@link BaseModelInheritance}
	 * as valid list items.</p>
	 *
	 * <p>If the needed criteria is met, this method will call
	 * {@link DatabaseDao#deleteEntity(Class, Integer)} for every member of the
	 * list.</p>
	 *
	 * @param entities
	 */
	public void deleteEntities(List<? extends BaseModelInterface> entities) {
		// ignore empty lists and null
		if (entities != null && entities.size() > 0) {
			for (BaseModelInterface entity : entities) {
				if (entity != null) {
					// get the class of the current entity, we need to do it in
					// the loop as the originally passed list can contain
					// instances of more than one concrete class.
					Class<? extends BaseModelInterface> clazz = entity.getClass();
					this.deleteEntity(clazz, entity.getId());
				}
			}
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

			criteria.add(Restrictions.eq("user_name", name));

		} catch (Exception e) {
			throw new ShogunDatabaseAccessException(
					"Error while getting User by name: " + name, e);
		}

		// we have to ensure that the modules are distinct
		// @see http://docs.jboss.org/hibernate/orm/3.6/javadocs/org/hibernate/Criteria.html#createAlias(java.lang.String, java.lang.String, int)
//		criteria.createAlias("modules", "module", CriteriaSpecification.INNER_JOIN);

		criteria.setFetchMode("mapLayers", FetchMode.JOIN);


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
	 * Returns a User object defined by its ID in the database.
	 * <br>
	 * <br>
	 * Can be filtered by a further criterion (additionalCriterion) put on a
	 * specific path (additionalCriteriaPath). This could model a filter like
	 * <br>
	 * <code>
	 * criteria.createCriteria("groups")
				.add(Restrictions.in("id", groupIdsOfSessionUser));
	 * <code>
	 *
	 * @param id the ID of the {@link User} to query
	 * @param additionalCriteriaPath a criteria path to put add a further criterion
	 * @param additionalCriterion additional criterion to filter query
	 * @return the {@link User} object matching the query
	 */
	public User getUserById(int id, String additionalCriteriaPath, Criterion additionalCriterion) {

		Criteria criteria =
			this.sessionFactory.getCurrentSession().createCriteria(User.class);

		// add additional restrictions like
		// where user is in group with ID xy
		if (additionalCriteriaPath != null &&
				additionalCriteriaPath.isEmpty() == false &&
				additionalCriterion != null) {

			criteria.createCriteria(additionalCriteriaPath)
				.add(additionalCriterion);
		}

		// filter on ID
		criteria.add(Restrictions.eq("id", id));

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
	 * The roles, modules and granted layers of the user depend on the groups
	 * he is assigned to. We dont have to care about this here.
	 *
	 * @param user the User object to create
	 * @param setSessionGroup flag controls if the current user group should be set to new user
	 * @return
	 * @throws Exception
	 */
	public User createUser(User user, boolean setSessionGroup) throws ShogunDatabaseAccessException {

		try {

			this.sessionFactory.getCurrentSession().save(user);

			// TODO NB: What is the sense of setSessionGroup?
			// Due to the use of getFirstGroupObjectFromSessionUser()
			// it seems that the user is already associated with the
			// group that we will update in the following code.
			// So why do we do this?
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

		criteria.setProjection(Projections.rowCount());

		if (hibernateFilter != null) {

			int filterItemCount = hibernateFilter.getFilterItemCount();

			// FILTER
			// OR connected filter items
			if (hibernateFilter.getLogicalOperator().equals(LogicalOperator.OR)) {
				try {
					Disjunction dis = Restrictions.disjunction();
					for (int i = 0; i < filterItemCount; i++) {
						HibernateFilterItem hfi = (HibernateFilterItem) hibernateFilter.getFilterItem(i);
						if (hfi.getFieldName() != null && hfi.getFieldName().contains(".")) {
							String ownFieldName = hfi.getFieldName().split("\\.")[0];
							criteria.createCriteria(ownFieldName, ownFieldName);
							// todo move outside
							Criterion criterion = hfi.makeCriterion(hibernateFilter.getMainClass());
							if (criterion != null) {
								dis.add(criterion);
							}

						}
						else {
							Criterion criterion = hfi.makeCriterion(hibernateFilter.getMainClass());
							if (criterion != null) {
								dis.add(criterion);
							}
						}
					}
					criteria.add(dis);
				} catch (Exception e) {
					throw new ShogunDatabaseAccessException("(getTotal) Error" +
							" while adding an OR connected filter: " +
							e.getMessage(), e);
				}

			} else {

				try {
					for (int i = 0; i < filterItemCount; i++) {
						HibernateFilterItem hfi = (HibernateFilterItem) hibernateFilter.getFilterItem(i);
						if (hfi.getFieldName() != null && hfi.getFieldName().contains(".")) {
							String ownFieldName = hfi.getFieldName().split("\\.")[0];

							criteria.createCriteria(ownFieldName, ownFieldName);

							// todo move outside
							Criterion criterion = hfi.makeCriterion(hibernateFilter.getMainClass());
							if (criterion != null) {
								criteria.add(criterion);
							}

						} else {
							Criterion criterion = hfi.makeCriterion(hibernateFilter.getMainClass());

							if (criterion != null) {
								criteria.add(criterion);
							}
						}
					}

				} catch (Exception e) {
					throw new ShogunDatabaseAccessException("(getTotal) Error" +
							" while combining criteria with AND", e);
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
	 * Determines the ID of the logged in user from Security Context.
	 *
	 * TODO this is the only method in the dbDao that is secured through the
	 *      @PreAuthorize annotation. It is possibly secured since
	 *      {@link UserAdministrationController#getLoggedInUserId()} directly
	 *      calls into the database dao instead of using the appropriate
	 *      {@link UserAdministrationService}. We might consider adding a
	 *      dedicated method to that service.
	 *
	 * @return the name of the logged in user or NULL if not found
	 */
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
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

		LOGGER.debug("Starting to get user object from session.");

		// get the authorization context, incl. user name
		Authentication authResult = SecurityContextHolder.getContext().getAuthentication();

		LOGGER.debug("Got authResult: " + authResult.getName());
		LOGGER.debug("Creating criteria now to get the user.");

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(User.class);

		criteria.add(Restrictions.eq("user_name", authResult.getName()));

		LOGGER.debug("Requesting user now");

		User u = (User) criteria.uniqueResult();

		LOGGER.debug("Got user from session: " + u.getId());

		return u;

	}

	/**
	 * Checks whether the user identified by given userName belongs to at least
	 * one group which has the given roleName.
	 *
	 * @param userName
	 * @param roleName
	 * @return
	 */
	public boolean hasUserRoleByUsernameAndRolename(String userName, String roleName) {
		boolean hasRole = false;

		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("user_name", userName));
		criteria.createCriteria("groups", "g");
		criteria.createCriteria("g.roles", "r");
		criteria.add(Restrictions.eq("r.name", roleName));
		criteria.setProjection(Projections.rowCount());

		long rowCnt = 0;

		try {
			rowCnt = ((Number)criteria.uniqueResult()).longValue();
			hasRole = (rowCnt > 0l);
		} catch (HibernateException he) {
			LOGGER.error("Failed to determine whether"
					+ " user with username '" + userName + "'"
					+ " has the role with name '" + roleName + "':"
					+ " " + he.getMessage());
		}

		return hasRole;
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
		return sessionUser.hasSuperAdminRole();
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
