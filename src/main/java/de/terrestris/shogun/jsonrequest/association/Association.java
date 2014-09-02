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
package de.terrestris.shogun.jsonrequest.association;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Class represents an association of entities, for example
 * add some layers to an User
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
public class Association {


	private String leftEntity;
	private Integer leftEntityId;
	private String rightEntity;
	private List<Integer> associations;

	/**
	 * @return the leftEntity
	 */
	public String getLeftEntity() {
		return leftEntity;
	}
	/**
	 * @param leftEntity the leftEntity to set
	 */
	public void setLeftEntity(String leftEntity) {
		this.leftEntity = leftEntity;
	}
	/**
	 * @return the leftEntityId
	 */
	public Integer getLeftEntityId() {
		return leftEntityId;
	}
	/**
	 * @param leftEntityId the leftEntityId to set
	 */
	public void setLeftEntityId(Integer leftEntityId) {
		this.leftEntityId = leftEntityId;
	}
	/**
	 * @return the rightEntity
	 */
	public String getRightEntity() {
		return rightEntity;
	}
	/**
	 * @param rightEntity the rightEntity to set
	 */
	public void setRightEntity(String rightEntity) {
		this.rightEntity = rightEntity;
	}
	/**
	 * @return the associations
	 */
	public List<Integer> getAssociations() {
		return associations;
	}
	/**
	 * @param associations the associations to set
	 */
	public void setAssociations(List<Integer> associations) {
		this.associations = associations;
	}


}