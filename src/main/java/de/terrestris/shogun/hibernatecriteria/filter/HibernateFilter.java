package de.terrestris.shogun.hibernatecriteria.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents a filter usable in Hibernate queries
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
public class HibernateFilter extends Filter {
	
	/**
	 * 
	 */
	private Class<? extends Serializable> mainClass = null;

	/**
	 * 
	 * @param mainClass
	 */
	public HibernateFilter(Class<? extends Serializable> mainClass) {
		super();
		this.mainClass = mainClass;
	}

	/**
	 * Static method creating a Hibernate filter object by a given
	 * JSON request object.
	 * 
	 * @param clazz
	 * @param plainFilter
	 * @return
	 */
	public static HibernateFilter create(Class<? extends Serializable> clazz,
			de.terrestris.shogun.jsonrequest.Filter plainFilter) {

		HibernateFilter hibernateFilter = null;
		if (plainFilter != null) {
			hibernateFilter = new HibernateFilter(clazz);

			// set the logical operator AND/OR
			if (plainFilter.getLogicalOperator().equalsIgnoreCase("AND")
					|| plainFilter.getLogicalOperator().equalsIgnoreCase("OR")) {

				// decide from request
				hibernateFilter.setLogicalOperator(LogicalOperator.valueOf(plainFilter.getLogicalOperator()));
			} else {
				
				// should be done in father class
				
//				// set default (AND)
//				hibernateFilter.setLogicalOperator(Operator.valueOf("AND"));
			}

			// plain filter items from request
			List<de.terrestris.shogun.jsonrequest.FilterItem> items = plainFilter.getFilterItems();

			// iterate over plain filter items and create hibernate filteritems
			if (items != null) {
				for (de.terrestris.shogun.jsonrequest.FilterItem item : items) {

					// hibernate filter item
					HibernateFilterItem hibernateItem = new HibernateFilterItem();

					// set field
					hibernateItem.setFieldName(item.getFieldName());
					String operator = item.getOperator();

					// set operator, e.g. Smaller
					hibernateItem.setOperator(FilterItem.Operator.valueOf(operator));

					// set operands
					Object[] operands = item.getOperands();
					int opCount = (operands == null ? 0 : operands.length);
					List<FilterOperand> filterOperands = new ArrayList<FilterOperand>();

					for (int o = 0; o < opCount; o++) {
						filterOperands.add(new FilterOperand(operands[o]));
					}
					hibernateItem.setOperands(filterOperands);

					// add filled item to hibernate filter object
					hibernateFilter.addFilterItem(hibernateItem);
				}
			}
		}
		
		return hibernateFilter;
	}
	
	
	/**
	 * Method creates a Hibernate filter object in a simplified way.
	 * Only one filterItem is used here, which is defined by its fieldname, operator and operands.
	 * 
	 * For more elaborated use-cases please consider using the method 
	 * HibernateFilter create(Class<? extends Serializable> clazz, de.terrestris.shogun.jsonrequest.Filter plainFilter)
	 * 
	 * @param clazz
	 * @param fieldName
	 * @param operator
	 * @param operands
	 * @return
	 */
	public static HibernateFilter create(Class<? extends Serializable> clazz, String fieldName, 
			String operator, String[] operands) {
		
		de.terrestris.shogun.jsonrequest.Filter filter = new de.terrestris.shogun.jsonrequest.Filter();
		
		de.terrestris.shogun.jsonrequest.FilterItem filterItem = new de.terrestris.shogun.jsonrequest.FilterItem();
		filterItem.setFieldName(fieldName);
		filterItem.setOperator(operator);
		
		filterItem.setOperands(operands);
		
		List<de.terrestris.shogun.jsonrequest.FilterItem> filterItems = new ArrayList<de.terrestris.shogun.jsonrequest.FilterItem>();
		filterItems.add(filterItem);
		
		filter.setFilterItems(filterItems );
		
		HibernateFilter hibernateFilter = HibernateFilter.create((Class<? extends Serializable>) clazz, filter);
		
		return hibernateFilter;

	}

	/**
	 * 
	 * @return
	 */
	public Class<? extends Serializable> getMainClass() {
		return mainClass;
	}

	/**
	 * 
	 * @param mainClass
	 */
	public void setMainClass(Class<? extends Serializable> mainClass) {
		this.mainClass = mainClass;
	}

}