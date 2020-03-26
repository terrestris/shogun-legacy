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
package de.terrestris.shogun.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * WfsProxyConfig POJO
 *
 * @author terrestris GmbH & Co. KG
 *
 * TODO remove this for 0.1 release?
 *
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_WFSPROXYCONFIG")
@Cacheable
public class WfsProxyConfig extends OwsProxyConfig {

	private String typename;
	private String srs;
	private String bbox;
	private String outputformat;
	private String exceptions;
	private String maxfeatures;


	/**
	 * @return the typename
	 */
	@Column(name = "TYPENAME", nullable = false)
	public String getTypename() {
		return typename;
	}
	/**
	 * @param typename the typename to set
	 */
	public void setTypename(String typename) {
		this.typename = typename;
	}

	/**
	 * @return the srs
	 */
	@Column(name = "SRS", nullable = false)
	public String getSrs() {
		return srs;
	}
	/**
	 * @param srs the srs to set
	 */
	public void setSrs(String srs) {
		this.srs = srs;
	}

	/**
	 * @return the bbox
	 */
	@Column(name = "BBOX", nullable = false)
	public String getBbox() {
		return bbox;
	}
	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(String bbox) {
		this.bbox = bbox;
	}

	/**
	 * @return the outputformat
	 */
	@Column(name = "OUTPUTFORMAT", nullable = false)
	public String getOutputformat() {
		return outputformat;
	}
	/**
	 * @param outputformat the outputformat to set
	 */
	public void setOutputformat(String outputformat) {
		this.outputformat = outputformat;
	}

	/**
	 * @return the exceptions
	 */
	@Column(name = "EXCEPTIONS", nullable = false)
	public String getExceptions() {
		return exceptions;
	}
	/**
	 * @param exceptions the exceptions to set
	 */
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * @return the maxfeatures
	 */
	@Column(name = "MAXFEATURES", nullable = false)
	public String getMaxfeatures() {
		return maxfeatures;
	}
	/**
	 * @param maxfeatures the maxfeatures to set
	 */
	public void setMaxfeatures(String maxfeatures) {
		this.maxfeatures = maxfeatures;
	}

	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("typename", typename)
			.append("srs", srs)
			.append("bbox", bbox)
			.append("outputformat", outputformat)
			.append("exceptions", exceptions)
			.append("maxfeatures", maxfeatures)
			.toString();
	}
}