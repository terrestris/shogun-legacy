package de.terrestris.shogun.hibernatecriteria.sort;

import org.hibernate.criterion.Order;

/**
 * 
 */
public class HibernateSortItem {
	
	/**
	 * 
	 */
	private String sort;
	
	/**
	 * 
	 */
	private String dir;
	
	/**
	 * 
	 * @param sort
	 * @param dir
	 */
	public HibernateSortItem(String sort, String dir) {
		
		if (sort != null && sort.equals("") == false) {
			this.sort = sort;
		}
		else {
			this.sort = "id";
		}
		
		if (dir != null && (dir.equals("ASC") || dir.equals("DESC"))) {
			this.dir = dir;
		}
		else {
			this.dir = "ASC";
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Order createHibernateOrder() {
		
		Order order = null;
		
		if (this.dir.equals("ASC")) {
			order = Order.asc(this.sort);
		}
		else if (this.dir.equals("DESC")) {
			order = Order.desc(this.sort);
		}
		else {
		}
		
		return order;
	}

}
