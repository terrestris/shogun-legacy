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
package de.terrestris.shogun.hibernatecriteria.filter;

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
	private Class<?> mainClass = null;

	/**
	 *
	 * @param mainClass
	 */
	public HibernateFilter(Class<?> mainClass) {
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
	public static HibernateFilter create(Class<?> clazz,
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
	 * HibernateFilter create(Class clazz, de.terrestris.shogun.jsonrequest.Filter plainFilter)
	 *
	 * @param clazz
	 * @param fieldName
	 * @param operator
	 * @param operands
	 * @return
	 */
	public static HibernateFilter create(Class<?> clazz, String fieldName,
			String operator, String[] operands) {

		de.terrestris.shogun.jsonrequest.Filter filter = new de.terrestris.shogun.jsonrequest.Filter();

		de.terrestris.shogun.jsonrequest.FilterItem filterItem = new de.terrestris.shogun.jsonrequest.FilterItem();
		filterItem.setFieldName(fieldName);
		filterItem.setOperator(operator);

		filterItem.setOperands(operands);

		List<de.terrestris.shogun.jsonrequest.FilterItem> filterItems = new ArrayList<de.terrestris.shogun.jsonrequest.FilterItem>();
		filterItems.add(filterItem);

		filter.setFilterItems(filterItems );

		HibernateFilter hibernateFilter = HibernateFilter.create((Class<?>) clazz, filter);

		return hibernateFilter;

	}

	/**
	 *
	 * @return
	 */
	public Class<?> getMainClass() {
		return mainClass;
	}

	/**
	 *
	 * @param mainClass
	 */
	public void setMainClass(Class<?> mainClass) {
		this.mainClass = mainClass;
	}

}