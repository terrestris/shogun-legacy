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
package de.terrestris.shogun.jsonrequest;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Class represents a complex request to the backend with filter, sort criteria
 * etc...
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
public class Request {

	/**
	 * The model class-name which is requested
	 */
	private String object_type;

	/**
	 * Filter conditions
	 */
	private Filter filter;

	/**
	 *
	 */
	private Set<String> fields;

	/**
	 *
	 */
	private Set<String> ignoreFields;
	/**
	 * Additional Filter conditions that are added as global conjunction.
	 *
	 * This is only a intermediate solution.
	 */
	private Filter globalAndFilter;

	/**
	 * Sort criteria
	 */
	private Sort sortObject;

	/**
	 *
	 */
	private Paging pagingObject;

	public Request() {
	}

	public void finalize() throws Throwable {
	}



	public String getObject_type() {
		return object_type;
	}

	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}

	/**
	 *
	 * @return filter
	 */
	 public Filter getFilter() {
		 return filter;
	 }

	/**
	 *
	 * @param filter
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}


	/**
	 * @return the fields
	 */
	public Set<String> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Set<String> fields) {
		this.fields = fields;
	}

	/**
	 * @return the globalAndFilter
	 */
	public Filter getGlobalAndFilter() {
		return globalAndFilter;
	}

	/**
	 * @param globalAndFilter the globalAndFilter to set
	 */
	public void setGlobalAndFilter(Filter globalAndFilter) {
		this.globalAndFilter = globalAndFilter;
	}

	/**
	 * @return the sortObject
	 */
	public Sort getSortObject() {
		return sortObject;
	}

	/**
	 * @param sortObject the sortObject to set
	 */
	public void setSortObject(Sort sortObject) {
		this.sortObject = sortObject;
	}

	/**
	 * @return the pagingObject
	 */
	public Paging getPagingObject() {
		return pagingObject;
	}

	/**
	 * @param pagingObject the pagingObject to set
	 */
	public void setPagingObject(Paging pagingObject) {
		this.pagingObject = pagingObject;
	}

	public Set<String> getIgnoreFields() {
		// TODO Auto-generated method stub
		return ignoreFields;
	}

	/**
	 * @param ignoreFields the ignoreFields to set
	 */
	public void setIgnoreFields(Set<String> ignoreFields) {
		this.ignoreFields = ignoreFields;
	}



}