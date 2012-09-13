package de.terrestris.shogun.hibernatecriteria.sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.terrestris.shogun.jsonrequest.Sort;
import de.terrestris.shogun.jsonrequest.SortItem;

/**
 * 
 */
public class HibernateSortObject {

	/**
	 * 
	 */
	private Class<? extends Serializable> mainClass = null;

	/**
	 * 
	 */
	private List<HibernateSortItem> sortItems = new Vector<HibernateSortItem>();

	/**
	 * 
	 * @param mainClass
	 */
	public HibernateSortObject(Class<? extends Serializable> mainClass) {
		super();
		this.mainClass = mainClass;
	}

	/**
	 * 
	 * @param sortItem
	 */
	public void addSortItem(HibernateSortItem sortItem) {
		sortItems.add(sortItem);
	}

	/**
	 * Static method creating a Hibernate sorting object by a given
	 * JSON request object.
	 * 
	 * @param clazz
	 * @param plainSortObject
	 * @return
	 */
	public static HibernateSortObject create(Class<? extends Serializable> clazz,
			de.terrestris.shogun.jsonrequest.Sort plainSortObject) {

		HibernateSortObject hibernateSortObject = null;

		if (plainSortObject != null) {
			hibernateSortObject = new HibernateSortObject(clazz);

			List<de.terrestris.shogun.jsonrequest.SortItem> items = plainSortObject.getSortItems();

			if (items != null) {
				for (de.terrestris.shogun.jsonrequest.SortItem item : items) {

					HibernateSortItem hibernateSortItem = new HibernateSortItem(
							item.getSort(), item.getDir());
					hibernateSortObject.addSortItem(hibernateSortItem);
				}
			}
		}

		return hibernateSortObject;
	}
	
	/**
	 * Method creates a Hibernate sorting object in a simplified way.
	 * Only one sortItem is used here, which is defined only by the field and direction.
	 * 
	 * For more elaborated use-cases please consider using the method 
	 * HibernateSortObject create(Class<? extends Serializable> clazz, de.terrestris.shogun.jsonrequest.Sort plainSortObject) {
	 * 
	 * @param clazz
	 * @param field
	 * @param dir
	 * @return
	 */
	public static HibernateSortObject create(Class<? extends Serializable> clazz, String field, String dir) {
		
		SortItem sortItem = new SortItem();
		sortItem.setDir(dir);
		sortItem.setSort(field);
		
		List<SortItem> sortItems = new ArrayList<SortItem>();
		sortItems.add(sortItem);
		
		Sort plainSortObject = new Sort();
		plainSortObject.setSortItems(sortItems);
		
		HibernateSortObject hibernateSortObject = HibernateSortObject.create((Class<? extends Serializable>) clazz, plainSortObject);
		
		return hibernateSortObject;
	}
	
	/**
	 * Method creates a Hibernate sorting object in a simplified way.
	 * Only one sortItem is used here, which is defined only by the field.
	 * Sort direction is set to ASC.
	 * 
	 * For more elaborated use-cases please consider using the method 
	 * HibernateSortObject create(Class<? extends Serializable> clazz, de.terrestris.shogun.jsonrequest.Sort plainSortObject) {
	 * 
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static HibernateSortObject create(Class<? extends Serializable> clazz, String field) {
		
		HibernateSortObject hibernateSortObject = HibernateSortObject.create((Class<? extends Serializable>) clazz, field, "ASC");
		
		return hibernateSortObject;
	}

	/**
	 * @return the mainClass
	 */
	public Class<? extends Serializable> getMainClass() {
		return mainClass;
	}

	/**
	 * @param mainClass the mainClass to set
	 */
	public void setMainClass(Class<? extends Serializable> mainClass) {
		this.mainClass = mainClass;
	}

	/**
	 * @return the sortItems
	 */
	public List<HibernateSortItem> getSortItems() {
		return sortItems;
	}
	
	/**
	 * @param sortItems
	 */
	public void setSortItems(List<HibernateSortItem> sortItems) {
		this.sortItems = sortItems;
	}

}
