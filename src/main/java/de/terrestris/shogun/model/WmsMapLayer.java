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

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * WmsMapLayer POJO.
 *
 * This is the POJO for a visual layer in the application. Instances of this
 * class can be used in the client to derive valid OpenLayers.Layer-objects.
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_WMSMAPLAYER")
@Cacheable
public class WmsMapLayer extends MapLayer {

	private String url;
	private String layers;
	private Boolean transparent = true;
	private Boolean singleTile = false;
	private Integer ratio = null;
	private String format;


	/**
	 * @return the url
	 */
	@Column(name = "URL", nullable = false)
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * @return the layers
	 */
	@Column(name = "LAYERS", nullable = false, length = 2048)
	public String getLayers() {
		return layers;
	}

	/**
	 * @param layers the layers to set
	 */
	public void setLayers(String layers) {
		this.layers = layers;
	}

	/**
	 * @return the transparent
	 */
	@Column(name = "TRANSPARENT", nullable = false)
	public Boolean getTransparent() {
		return transparent;
	}

	/**
	 * @param transparent the transparent to set
	 */
	public void setTransparent(Boolean transparent) {
		this.transparent = transparent;
	}

	/**
	 * @return the singleTile value
	 */
	@Column(name = "SINGLETILE", nullable = false)
	public Boolean getSingleTile() {
		return singleTile;
	}

	/**
	 * @param singleTile the value of singleTile to set
	 */
	public void setSingleTile(Boolean singleTile) {
		this.singleTile = singleTile;
	}


	/**
	 * @return the ratio
	 */
	@Column(name = "RATIO")
	public Integer getRatio() {
		return ratio;
	}

	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	}

	/**
	 * @return the format
	 */
	@Column(name = "FORMAT", nullable = false)
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 *
	 * According to
	 * http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java
	 * it is recommended only to use getter-methods when using ORM like Hibernate
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(37, 11). // two randomly chosen prime numbers
				appendSuper(super.hashCode()).
				append(getUrl()).
				append(getLayers()).
				toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 *
	 * According to
	 * http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java
	 * it is recommended only to use getter-methods when using ORM like Hibernate
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof WmsMapLayer))
			return false;
		WmsMapLayer other = (WmsMapLayer) obj;

		return new EqualsBuilder().
				appendSuper(super.equals(other)).
				append(getUrl(), other.getUrl()).
				append(getLayers(), other.getLayers()).
				isEquals();
	}

	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("url", url)
			.append("layers", layers)
			.append("transparent", transparent)
			.append("singleTile", singleTile)
			.append("ratio", ratio)
			.append("format", format)
			.toString();
	}
}