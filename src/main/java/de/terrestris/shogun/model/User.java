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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.terrestris.shogun.serializer.LeanBaseModelSetSerializer;

/**
 * User POJO
 *
 * @author terrestris GmbH & Co. KG
 *
 * TODO check for the deprecated org.hibernate.cache.CacheConcurrencyStrategy;
 *
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_USER")
@Embeddable
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseModel {

	private String user_name;
	private String user_longname;
	private String user_firstname;
	private String user_lastname;
	private String user_title;
	private String user_email;
	private String user_street;
	private String user_postcode;
	private String user_city;
	private String user_country;
	private String user_bureau;
	private String user_department;
	private String user_unit;
	private String user_description;
	private String user_password;
	private String user_lang;

	private Boolean active = true;

	private Set<Group> groups;
	private MapConfig mapConfig;
	private WfsProxyConfig wfsProxyConfig;
	private WmsProxyConfig wmsProxyConfig;


	/**
	 * @return the user_name
	 */
	@Column(name="USER_NAME", length=50, unique=true)
	public String getUser_name() {
		return user_name;
	}

	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}


	/**
	 * @return the user_longname
	 */
	@Column(name="USER_LONGNAME", length=100)
	public String getUser_longname() {
		return user_longname;
	}

	/**
	 * @param user_longname the user_longname to set
	 */
	public void setUser_longname(String user_longname) {
		this.user_longname = user_longname;
	}

	/**
	 * @return the user_firstname
	 */
	public String getUser_firstname() {
		return user_firstname;
	}

	/**
	 * @param user_firstname the user_firstname to set
	 */
	public void setUser_firstname(String user_firstname) {
		this.user_firstname = user_firstname;
	}

	/**
	 * @return the user_lastname
	 */
	public String getUser_lastname() {
		return user_lastname;
	}

	/**
	 * @param user_lastname the user_lastname to set
	 */
	public void setUser_lastname(String user_lastname) {
		this.user_lastname = user_lastname;
	}

	/**
	 * @return the user_title
	 */
	public String getUser_title() {
		return user_title;
	}

	/**
	 * @param user_title the user_title to set
	 */
	public void setUser_title(String user_title) {
		this.user_title = user_title;
	}

	/**
	 * @return the user_email
	 */
	@Column(name="USER_EMAIL", length=50)
	public String getUser_email() {
		return user_email;
	}

	/**
	 * @param user_email the user_email to set
	 */
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}


	/**
	 * @return the user_street
	 */
	@Column(name="USER_STREET", length=50)
	public String getUser_street() {
		return user_street;
	}

	/**
	 * @param user_street the user_street to set
	 */
	public void setUser_street(String user_street) {
		this.user_street = user_street;
	}


	/**
	 * @return the user_postcode
	 */
	@Column(name="USER_POSTCODE", length=25)
	public String getUser_postcode() {
		return user_postcode;
	}

	/**
	 * @param user_postcode the user_postcode to set
	 */
	public void setUser_postcode(String user_postcode) {
		this.user_postcode = user_postcode;
	}


	/**
	 * @return the user_city
	 */
	@Column(name="USER_CITY", length=50)
	public String getUser_city() {
		return user_city;
	}

	/**
	 * @param user_city the user_city to set
	 */
	public void setUser_city(String user_city) {
		this.user_city = user_city;
	}


	/**
	 * @return the user_country
	 */
	@Column(name="USER_COUNTRY", length=50)
	public String getUser_country() {
		return user_country;
	}

	/**
	 * @param user_country the user_country to set
	 */
	public void setUser_country(String user_country) {
		this.user_country = user_country;
	}


	/**
	 * @return the user_bureau
	 */
	public String getUser_bureau() {
		return user_bureau;
	}

	/**
	 * @param user_bureau the user_bureau to set
	 */
	public void setUser_bureau(String user_bureau) {
		this.user_bureau = user_bureau;
	}

	/**
	 * @return the user_department
	 */
	public String getUser_department() {
		return user_department;
	}

	/**
	 * @param user_department the user_department to set
	 */
	public void setUser_department(String user_department) {
		this.user_department = user_department;
	}

	/**
	 * @return the user_unit
	 */
	public String getUser_unit() {
		return user_unit;
	}

	/**
	 * @param user_unit the user_unit to set
	 */
	public void setUser_unit(String user_unit) {
		this.user_unit = user_unit;
	}

	/**
	 * @return the user_description
	 */
	public String getUser_description() {
		return user_description;
	}

	/**
	 * @param user_description the user_description to set
	 */
	public void setUser_description(String user_description) {
		this.user_description = user_description;
	}

	/**
	 * @return the user_password
	 */
	@JsonIgnore
	@Column(name="USER_PASSWORD", length=50)
	public String getUser_password() {
		return user_password;
	}

	/**
	 * @param user_password the user_password to set
	 */
	@JsonIgnore
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}


	/**
	 * @return the user_lang
	 */
	@Column(name="USER_LANG", length=3)
	public String getUser_lang() {
		return user_lang;
	}

	/**
	 * @param user_lang the user_lang to set
	 */
	public void setUser_lang(String user_lang) {
		this.user_lang = user_lang;
	}

	/**
	 * @return the active
	 */
	@Column(name="ACTIVE")
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the groups
	 */
	@ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
	@JsonIgnore
	@Fetch(FetchMode.SUBSELECT)
	@JsonSerialize(using=LeanBaseModelSetSerializer.class)
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Set<Group> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	/**
	 * @return the mapConfig
	 */
	@ManyToOne
	public MapConfig getMapConfig() {
		return mapConfig;
	}

	/**
	 * @param mapConfig the mapConfig to set
	 */
	@JsonIgnore
	public void setMapConfig(MapConfig mapConfig) {
		this.mapConfig = mapConfig;
	}

	/**
	 * @return the wfsProxyConfig
	 */
	@ManyToOne
	@JsonIgnore // needed that this is not serialized, hidden to client (security)
	public WfsProxyConfig getWfsProxyConfig() {
		return wfsProxyConfig;
	}

	/**
	 * @param wfsProxyConfig the wfsProxyConfig to set
	 */
	@JsonIgnore
	public void setWfsProxyConfig(WfsProxyConfig wfsProxyConfig) {
		this.wfsProxyConfig = wfsProxyConfig;
	}


	/**
	 * @return the wmsProxyConfig
	 */
	@ManyToOne
	@JsonIgnore // needed that this is not serialized, hidden to client (security)
	public WmsProxyConfig getWmsProxyConfig() {
		return wmsProxyConfig;
	}

	/**
	 * @param wmsProxyConfig the wmsProxyConfig to set
	 */
	@JsonIgnore
	public void setWmsProxyConfig(WmsProxyConfig wmsProxyConfig) {
		this.wmsProxyConfig = wmsProxyConfig;
	}

	/**
	 * Will return the unification of all modules of all groups the user
	 * belongs to.
	 *
	 * @return
	 */
	@Transient
	public Set<Module> getModules() {
		Set<Module> allModulesOfUser = new HashSet<Module>();
		Set<Group> groups = this.getGroups();
		if(groups != null){
			for (Group group : groups) {
				allModulesOfUser.addAll(group.getModules());
			}
		}
		return allModulesOfUser;
	}

	/**
	 * Returns whether the user has role User.ROLENAME_SUPERADMIN in his set of
	 * roles by comparing the name of all roles to the given string using
	 * the private hasRole-method.
	 *
	 * @return whether the user has the queried role User.ROLENAME_SUPERADMIN.
	 */
	public boolean hasSuperAdminRole() {
		return this.hasRole(Group.ROLENAME_SUPERADMIN);
	}

	/**
	 * Returns whether the user has role User.ROLENAME_ADMIN in his set of
	 * roles by comparing the name of all roles to the given string using
	 * the private hasRole-method.
	 *
	 * @return whether the user has the queried role User.ROLENAME_SUPERADMIN.
	 */
	public boolean hasAdminRole() {
		return this.hasRole(Group.ROLENAME_ADMIN);
	}

	/**
	 * Returns whether the user has role User.ROLENAME_ANONYMOUS in his set of
	 * roles by comparing the name of all roles to the given string using
	 * the private hasRole-method.
	 *
	 * @return whether the user has the queried role User.ROLENAME_ANONYMOUS.
	 */
	public boolean hasAnonymousRole() {
		return this.hasRole(Group.ROLENAME_ANONYMOUS);
	}

	/**
	 * Returns all Roles of the user by iterating over all his groups.
	 *
	 */
	@Transient
	public Set<Role> getRoles(){
		Set<Role> myRoles = new HashSet<Role>();
		Set<Group> myGroups = this.getGroups();
		if(myGroups != null){
			for(Group g : myGroups){
				myRoles.addAll(g.getRoles());
			}
		}

		return myRoles;
	}

	/**
	 * Returns whether the user has the given role in his set of roles by
	 * Comparing the name of all roles to the given string.
	 *
	 * @param searchRoleName The rolename to check this users set of roles for
	 * @return whether the user has the queried role or not.
	 */
	private boolean hasRole(String searchRoleName) {
		boolean hasRole = false;
		for (Role r : this.getRoles()) {
			hasRole = r.getName().equals(searchRoleName);
			if (hasRole) {
				break;
			}
		}
		return hasRole;
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
		return new HashCodeBuilder(37, 13). // two randomly chosen prime numbers
				appendSuper(super.hashCode()).
				append(getUser_longname()).
				append(getUser_email()).
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
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;

		return new EqualsBuilder().
				appendSuper(super.equals(other)).
				append(getUser_longname(), other.getUser_longname()).
				isEquals();
	}

	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("user_name", user_name)
			.append("user_firstname", user_firstname)
			.append("user_lastname", user_lastname)
			.append("user_longname", user_longname)
			.append("user_email", user_email)
			.append("user_country", user_country)
			.append("active", active)
			.append("mapConfig", mapConfig)
			.toString();
	}
}