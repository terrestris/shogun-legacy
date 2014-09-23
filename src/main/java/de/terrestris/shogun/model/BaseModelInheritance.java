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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.terrestris.shogun.deserializer.DateDeserializer;
import de.terrestris.shogun.serializer.DateSerializer;

/**
 * BaseModelInheritance POJO ensures that a primary key ID and the internal
 * metadata
 * <ul>
 * <li>created_at</li>
 * <li>updated_at</li>
 * <li>app_user</li>
 * </ul>
 * are available in every model instance (DB table)<br>
 * <br>
 *
 * Use this as a class to extend when you want to have DB inheritance (compare
 * e.g. {@link MapLayer} and its child {@link WmsMapLayer}). The only difference
 * of BaseModelInheritance to BaseModel is in the annotation for the field "id"
 * which has annotation @GeneratedValue(strategy = GenerationType.TABLE)
 *
 * @see http://stackoverflow.com/q/916169
 *
 * @author terrestris GmbH & Co. KG
 * @author Christian Mayer
 * @author Marc Jansen
 *
 * @version $Id$
 *
 */
@MappedSuperclass
public class BaseModelInheritance implements BaseModelInterface {

	private int id;

	private Date created_at;
	private Date updated_at;
	private String app_user;

	public BaseModelInheritance() {
		this.setCreated_at(new Date());
		this.setUpdated_at(new Date());
		this.setApp_user("default");
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "ID", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// ----------------------------------------------------------------
	// SOME METADATA
	// ----------------------------------------------------------------

	/**
	 *
	 */
	@Column(name = "CREATED_AT", nullable = false)
	@JsonSerialize(using = DateSerializer.class)
	public Date getCreated_at() {
		return created_at;
	}

	/**
	 *
	 * @param created_at
	 */
	@JsonDeserialize(using = DateDeserializer.class)
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	/**
	 *
	 * @return
	 */
	@Column(name = "UPDATED_AT", nullable = false)
	@JsonSerialize(using = DateSerializer.class)
	public Date getUpdated_at() {
		return updated_at;
	}

	/**
	 *
	 * @param updated_at
	 */
	@JsonDeserialize(using = DateDeserializer.class)
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	/**
	 *
	 * @return
	 */
	@Column(name = "APP_USER", nullable = false)
	public String getApp_user() {
		return app_user;
	}

	/**
	 *
	 * @param user
	 */
	public void setApp_user(String user) {
		this.app_user = user;
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
		return new HashCodeBuilder(19, 29). // two randomly chosen prime numbers
				append(getCreated_at()).
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BaseModelInheritance))
			return false;
		BaseModelInheritance other = (BaseModelInheritance) obj;

		return new EqualsBuilder().
				append(getId(), other.getId()).
				append(getCreated_at(), other.getCreated_at()).
				isEquals();
	}

	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("id", id)
			.append("created_at", created_at)
			.append("updated_at",updated_at)
			.append("app_user", app_user)
			.toString();
	}

	/**
	 * Returns a short string which contains the most basic info about the
	 * object instance: the simple class name and the id.
	 *
	 * Useful e.g. for logging purposes.
	 * @return
	 */
	public String info(){
		String identity = this.getClass().getSimpleName() +
			" (id=" + this.id + ")";
		return identity;
	}
}