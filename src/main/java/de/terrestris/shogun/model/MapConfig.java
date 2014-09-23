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

import java.lang.reflect.InvocationTargetException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * MapConfig POJO
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_MAPCONFIG")
public class MapConfig extends BaseModel {

	public static final String DEFAULT_MAPCONFIG = "default-mapconfig";

	private String name; //TODO remove

	private String mapId; // id needed to reference map in portalConfig above
	private String title;
	private String projection;
	private String units;
	private Double maxResolution;
	private String maxExtent; // "-20037508, -20037508, 20037508, 20037508"; //TODO data hibernate format
	private String center; // [-10764594.758211, 4523072.3184791], //TODO data hibernate format
	private String resolutions;
	private String scales;
	private Integer zoom;


	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the mapId
	 */
	@Column(name = "MAPID", nullable = false)
	public String getMapId() {
		return mapId;
	}
	/**
	 * @param mapId the mapId to set
	 */
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	/**
	 * @return the title
	 */
	@Column(name = "TITLE", nullable = false)
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the projection
	 */
	@Column(name = "PROJECTION", nullable = false)
	public String getProjection() {
		return projection;
	}
	/**
	 * @param projection the projection to set
	 */
	public void setProjection(String projection) {
		this.projection = projection;
	}

	/**
	 * @return the units
	 */
	@Column(name = "UNITS", nullable = false)
	public String getUnits() {
		return units;
	}
	/**
	 * @param units the units to set
	 */
	public void setUnits(String units) {
		this.units = units;
	}

	/**
	 * @return the maxResolution
	 */
	@Column(name = "MAXRESOLUTION", nullable = false)
	public Double getMaxResolution() {
		return maxResolution;
	}
	/**
	 * @param maxResolution the maxResolution to set
	 */
	public void setMaxResolution(Double maxResolution) {
		this.maxResolution = maxResolution;
	}

	/**
	 * @return the maxExtent
	 */
	@Column(name = "MAXEXTENT", nullable = false)
	public String getMaxExtent() {
		return maxExtent;
	}
	/**
	 * @param maxExtent the maxExtent to set
	 */
	public void setMaxExtent(String maxExtent) {
		this.maxExtent = maxExtent;
	}

	/**
	 * @return the center
	 */
	@Column(name = "CENTER", nullable = false)
	public String getCenter() {
		return center;
	}
	/**
	 * @param center the center to set
	 */
	public void setCenter(String center) {
		this.center = center;
	}

	/**
	 * @return the zoom
	 */
	@Column(name = "ZOOM", nullable = false)
	public Integer getZoom() {
		return zoom;
	}
	/**
	 * @param zoom the zoom to set
	 */
	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
	/**
	 * @return the resolutions
	 */
	@Column(name="RESOLUTIONS", length=1000)
	public String getResolutions() {
		return resolutions;
	}
	/**
	 * @param resolutions the resolutions to set
	 */
	public void setResolutions(String resolutions) {
		this.resolutions = resolutions;
	}
	/**
	 * @return the scales
	 */
	public String getScales() {
		return scales;
	}
	/**
	 * @param scales the scales to set
	 */
	public void setScales(String scales) {
		this.scales = scales;
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
		return new HashCodeBuilder(23, 19). // two randomly chosen prime numbers
				appendSuper(super.hashCode()).
				append(getMapId()).
				append(getProjection()).
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
		if (!(obj instanceof MapConfig))
			return false;
		MapConfig other = (MapConfig) obj;

		return new EqualsBuilder().
				appendSuper(super.equals(other)).
				append(getMapId(), other.getMapId()).
				append(getProjection(), other.getProjection()).
				isEquals();
	}

	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("name", name)
			.append("mapId", mapId)
			.append("title", title)
			.append("projection", projection)
			.append("units", units)
			.append("maxResolution", maxResolution)
			.append("maxExtent", maxExtent)
			.append("center", center)
			.append("resolutions", resolutions)
			.append("scales", scales)
			.append("zoom", zoom)
			.toString();
	}

	/**
	 * Applies the given {@linkplain MapConfig} properties to the properties of
	 * this instance, e. g. to restrict it. The id property used as PK in the
	 * database is preserved.
	 *
	 * @param MapConfToApply
	 *            the MapConfig to be applied
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void restrictBy(MapConfig MapConfToApply)
			throws IllegalAccessException, InvocationTargetException {

		if (MapConfToApply == null) {
			return;
		}
		// no need to apply when objects are equal
		if (this.equals(MapConfToApply)) {
			return;
		}

		// since most of the MapConfig properties are dependent on the
		// projection it has to be the same, otherwise skip operation
		if (this.projection != null
				&& this.projection.equals(MapConfToApply.getProjection())) {
			// preserve id
			int id = this.getId();
			// merge properties
			BeanUtils.copyProperties(this, MapConfToApply);
			// restore id
			this.setId(id);
		}
	}
}
