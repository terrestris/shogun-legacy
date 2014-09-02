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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import de.terrestris.shogun.serializer.LeanBaseModelSetSerializer;

/**
 * Wms POJO
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_WMS")
public class Wms extends BaseModel {

	private String supportedVersion;
	private String baseUrl;
	private Set<WmsLayer> wmsLayers;

	/**
	 * The default Constructor
	 */
	public Wms() {

	}

	/**
	 * An alternative constructor for backwards compatibility: Field `wmsLayers`
	 * is of type Set<WmsLayer>, no longer List<WmsLayer>.
	 *
	 * @param supportedVersion
	 * @param baseUrl
	 * @param wmsLayers
	 */
	public Wms(String supportedVersion, String baseUrl, List<WmsLayer> wmsLayers) {
		this.supportedVersion = supportedVersion;
		this.baseUrl = baseUrl;
		this.wmsLayers = new HashSet<WmsLayer>(wmsLayers);

		this.setCreated_at(new Date());
		this.setUpdated_at(new Date());
	}

	/**
	 * An alternative constructor which will all fields to the given values.
	 *
	 * @param supportedVersion
	 * @param baseUrl
	 * @param wmsLayers
	 */
	public Wms(String supportedVersion, String baseUrl, Set<WmsLayer> wmsLayers) {
		this.supportedVersion = supportedVersion;
		this.baseUrl = baseUrl;
		this.wmsLayers = wmsLayers;

		this.setCreated_at(new Date());
		this.setUpdated_at(new Date());
	}

	/**
	 * @return the supportedVersion
	 */
	@Column(name = "SUPPORTEDVERSION", nullable = false)
	public String getSupportedVersion() {
		return supportedVersion;
	}

	/**
	 * @param supportedVersion the supportedVersion to set
	 */
	public void setSupportedVersion(String supportedVersion) {
		this.supportedVersion = supportedVersion;
	}


	/**
	 * @return the baseUrl
	 */
	@Column(name = "BASEURL", nullable = false)
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}


	/**
	 * @return the wmsLayers
	 */
	@OneToMany(fetch = FetchType.LAZY, targetEntity=WmsLayer.class)
	@Fetch(FetchMode.SUBSELECT)
	@JsonSerialize(using=LeanBaseModelSetSerializer.class)
	public Set<WmsLayer> getWmsLayers() {
		return wmsLayers;
	}

	/**
	 * @param wmsLayers the wmsLayers to set
	 */
	public void setWmsLayers(Set<WmsLayer> wmsLayers) {
		this.wmsLayers = wmsLayers;
	}

	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("supportedVersion", supportedVersion)
			.append("baseUrl", baseUrl)
			.toString();
	}
}