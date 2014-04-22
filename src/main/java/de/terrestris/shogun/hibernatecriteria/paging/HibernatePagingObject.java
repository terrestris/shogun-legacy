package de.terrestris.shogun.hibernatecriteria.paging;

import java.io.Serializable;

/**
 *
 */
public class HibernatePagingObject {

	private Class<? extends Serializable> mainClass = null;

	private int start;

	private int limit;

	/**
	 *
	 * @param mainClass
	 */
	public HibernatePagingObject(Class<? extends Serializable> mainClass) {
		super();
		this.mainClass = mainClass;
	}

	/**
	 *
	 * @param clazz
	 * @param plainPagingObject
	 * @return
	 */
	public static HibernatePagingObject create(Class<? extends Serializable> clazz,
			de.terrestris.shogun.jsonrequest.Paging plainPagingObject) {


		HibernatePagingObject hibernatePagingObject = new HibernatePagingObject(clazz);

		try {
			// CAST
			int start = Integer.parseInt(plainPagingObject.getStart());
			int limit = Integer.parseInt(plainPagingObject.getLimit());

			// set
			hibernatePagingObject.setStart(start);
			hibernatePagingObject.setLimit(limit);
		}
		catch (Exception e) {
			return null;
		}



		return hibernatePagingObject;
	}


	/**
	 * @return the mainClass
	 */
	public Class<? extends Serializable> getMainClass() {
		return mainClass;
	}

	/**
	 * @param mainClass
	 *            the mainClass to set
	 */
	public void setMainClass(Class<? extends Serializable> mainClass) {
		this.mainClass = mainClass;
	}


	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}


	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}


	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}


	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
