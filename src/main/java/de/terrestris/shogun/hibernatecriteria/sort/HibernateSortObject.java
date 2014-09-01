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
package de.terrestris.shogun.hibernatecriteria.sort;

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
	private Class<?> mainClass = null;

	/**
	 *
	 */
	private List<HibernateSortItem> sortItems = new Vector<HibernateSortItem>();

	/**
	 *
	 * @param mainClass
	 */
	public HibernateSortObject(Class<?> mainClass) {
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
	public static HibernateSortObject create(Class<?> clazz,
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
	 * HibernateSortObject create(Class clazz, de.terrestris.shogun.jsonrequest.Sort plainSortObject) {
	 *
	 * @param clazz
	 * @param field
	 * @param dir
	 * @return
	 */
	public static HibernateSortObject create(Class<?> clazz, String field, String dir) {

		SortItem sortItem = new SortItem();
		sortItem.setDir(dir);
		sortItem.setSort(field);

		List<SortItem> sortItems = new ArrayList<SortItem>();
		sortItems.add(sortItem);

		Sort plainSortObject = new Sort();
		plainSortObject.setSortItems(sortItems);

		HibernateSortObject hibernateSortObject = HibernateSortObject.create((Class<?>) clazz, plainSortObject);

		return hibernateSortObject;
	}

	/**
	 * Method creates a Hibernate sorting object in a simplified way.
	 * Only one sortItem is used here, which is defined only by the field.
	 * Sort direction is set to ASC.
	 *
	 * For more elaborated use-cases please consider using the method
	 * HibernateSortObject create(Class clazz, de.terrestris.shogun.jsonrequest.Sort plainSortObject) {
	 *
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static HibernateSortObject create(Class<?> clazz, String field) {

		HibernateSortObject hibernateSortObject = HibernateSortObject.create((Class<?>) clazz, field, "ASC");

		return hibernateSortObject;
	}

	/**
	 * @return the mainClass
	 */
	public Class<?> getMainClass() {
		return mainClass;
	}

	/**
	 * @param mainClass the mainClass to set
	 */
	public void setMainClass(Class<?> mainClass) {
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
