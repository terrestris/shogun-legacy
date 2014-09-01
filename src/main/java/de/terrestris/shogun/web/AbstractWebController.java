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
package de.terrestris.shogun.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A base class defining the web controllers.
 */
public abstract class AbstractWebController {

	/**
	 * Generates modelMap representing the JSON returning a set of
	 * data records.
	 *
	 * @param data A list of {@link Object} to be returned
	 * @return the map instance representing the JSON
	 */
	protected Map<String, Object> getModelMapSuccess(List<? extends Object> data) {
		Map<String, Object> returnMap = new HashMap<String, Object>(3);
		returnMap.put("total", data.size());
		returnMap.put("data", data);
		returnMap.put("success", true);

		return returnMap;
	}

	/**
	 * Generates modelMap representing the JSON returning a set of
	 * data records.
	 *
	 * @param data A set of {@link Object} to be returned
	 * @return the map instance representing the JSON
	 */
	protected Map<String, Object> getModelMapSuccess(Set<? extends Object> data) {
		Map<String, Object> returnMap = new HashMap<String, Object>(3);
		returnMap.put("total", data.size());
		returnMap.put("data", data);
		returnMap.put("success", true);

		return returnMap;
	}

	/**
	 * Generates modelMap representing the JSON returning one
	 * data record.
	 *
	 * @param dataset the {@link Object} instance to be returned
	 * @return the map instance representing the JSON
	 */
	protected Map<String, Object> getModelMapSuccess(Object dataset) {
		Map<String, Object> returnMap = new HashMap<String, Object>(3);
		returnMap.put("total", 1);
		returnMap.put("data", dataset);
		returnMap.put("success", true);

		return returnMap;
	}

	/**
	 * Generates modelMap to representing the JSON returning an error.
	 *
	 * @param msg message the error message to be returned
	 * @return the map instance representing the JSON
	 */
	protected Map<String, Object> getModelMapError(String msg) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);

		return modelMap;
	}

}
