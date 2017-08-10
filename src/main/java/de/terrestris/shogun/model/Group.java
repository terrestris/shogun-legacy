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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.terrestris.shogun.dao.DatabaseDao;
import de.terrestris.shogun.serializer.LeanBaseModelSetSerializer;

/**
 * Group POJO
 *
 * @author terrestris GmbH & Co. KG
 *
 * TODO check for the deprecated org.hibernate.cache.CacheConcurrencyStrategy;
 *
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_GROUP")
@Embeddable
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Group extends BaseModel{

	// TODO refactor as an enum
	public static final String ROLENAME_SUPERADMIN = "ROLE_SUPERADMIN";
	public static final String ROLENAME_USER = "ROLE_USER";
	public static final String ROLENAME_ADMIN = "ROLE_ADMIN";
	public static final String ROLENAME_ANONYMOUS = "ROLE_ANONYMOUS";

	/**
	 * These roles will be assigned for new groups when they do not have
	 * roles already. This Set isn't applied in a constructor, but when
	 * creating instances of the Group-class e.g. in the service.
	 */
	public static final Set<String> DEFAULT_ROLENAMES = new HashSet<String>(
			Arrays.asList(ROLENAME_USER, ROLENAME_ANONYMOUS));

	public static final String DEFAULT_ADMIN_GROUP = "default-admin-group";
	public static final String DEFAULT_USER_GROUP = "default-user-group";

	private String name;
	private String group_nr;
	private String company;
	private String street;
	private String housenumber;
	private String country;
	private String contact_salutation;
	private String contact_firstname;
	private String contact_name;
	private String contact_position;
	private String phone1;
	private String phone2;
	private String fax;
	private String mail;
	private String url;
	private String notes;
	private String language;

	private Set<User> users;
	private Set<Module> modules;
	private Set<MapLayer> mapLayers;
	private Set<Role> roles;

	private String group_module_list;
	private Set<Integer> grantedUsers;
	private Set<Integer> grantedMapLayers;

	private boolean deletable;

	/**
	 *
	 */
	public Group() {
		super();
		this.setUsers(new HashSet<User>());
	}

	/**
	 * @return the name
	 */
	@Column(name="NAME")
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
	 * @return the group_nr
	 */
	@Column(name="GROUP_NR", nullable=false, unique=true)
	public String getGroup_nr() {
		return group_nr;
	}

	/**
	 * @param group_nr the group_nr to set
	 */
	public void setGroup_nr(String group_nr) {
		this.group_nr = group_nr;
	}

	/**
	 * @return the company
	 */
	@Column(name="COMPANY", nullable=false)
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the street
	 */
	@Column(name="STREET", length=80)
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the housenumber
	 */
	@Column(name="HOUSENUMBER", length=10)
	public String getHousenumber() {
		return housenumber;
	}

	/**
	 * @param housenumber the housenumber to set
	 */
	public void setHousenumber(String housenumber) {
		this.housenumber = housenumber;
	}

	/**
	 * @return the country
	 */
	@Column(name="COUNTRY", length=100)
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the contact_salutation
	 */
	@Column(name="CONTACT_SALUTATION", length=80)
	public String getContact_salutation() {
		return contact_salutation;
	}

	/**
	 * @param contact_salutation the contact_salutation to set
	 */
	public void setContact_salutation(String contact_salutation) {
		this.contact_salutation = contact_salutation;
	}

	/**
	 * @return the contact_firstname
	 */
	@Column(name="CONTACT_FIRSTNAME")
	public String getContact_firstname() {
		return contact_firstname;
	}

	/**
	 * @param contact_firstname the contact_firstname to set
	 */
	public void setContact_firstname(String contact_firstname) {
		this.contact_firstname = contact_firstname;
	}

	/**
	 * @return the contact_name
	 */
	@Column(name="CONTACT_NAME")
	public String getContact_name() {
		return contact_name;
	}

	/**
	 * @param contact_name the contact_name to set
	 */
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	/**
	 * @return the contact_position
	 */
	@Column(name="CONTACT_POSITION")
	public String getContact_position() {
		return contact_position;
	}

	/**
	 * @param contact_position the contact_position to set
	 */
	public void setContact_position(String contact_position) {
		this.contact_position = contact_position;
	}

	/**
	 * @return the phone1
	 */
	@Column(name="PHONE1", length=100)
	public String getPhone1() {
		return phone1;
	}

	/**
	 * @param phone1 the phone1 to set
	 */
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	/**
	 * @return the phone2
	 */
	@Column(name="PHONE2", length=100)
	public String getPhone2() {
		return phone2;
	}

	/**
	 * @param phone2 the phone2 to set
	 */
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	/**
	 * @return the fax
	 */
	@Column(name="FAX", length=100)
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the mail
	 */
	@Column(name="MAIL")
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the url
	 */
	@Column(name="URL", length=1000)
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
	 * @return the notes
	 */
	@Column(name="NOTES", length=1024)
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the users
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TBL_GROUP_TBL_USER", joinColumns = {
			@JoinColumn(name = "GROUP_FK", nullable = true, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "USER_FK",
					nullable = true, updatable = false) })
	@Fetch(FetchMode.SUBSELECT)
	@JsonSerialize(using=LeanBaseModelSetSerializer.class)
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	/**
	 * @return the modules
	 */
	@ManyToMany(fetch = FetchType.LAZY, targetEntity=Module.class)
	@JoinTable(name = "TBL_GROUP_TBL_MODULE",  joinColumns = {
			@JoinColumn(name = "GROUP_ID", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "MODULE_ID",
					nullable = false, updatable = false) })
	@Fetch(FetchMode.SUBSELECT)
	@JsonSerialize(using=LeanBaseModelSetSerializer.class)
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Set<Module> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

	/**
	 * @return the mapLayers
	 */
	@ManyToMany(fetch = FetchType.EAGER, targetEntity=MapLayer.class)
	@JoinTable(
		name = "TBL_GROUP_TBL_MAPLAYER",
		joinColumns = {
			@JoinColumn(name = "GROUP_ID", nullable = false, updatable = false)
		},
		inverseJoinColumns = {
			@JoinColumn(name = "MAPLAYER_ID", nullable = false, updatable = false)
		}
	)
	@Fetch(FetchMode.JOIN)
	@JsonSerialize(using=LeanBaseModelSetSerializer.class)
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Set<MapLayer> getMapLayers() {
		return mapLayers;
	}

	/**
	 * @param mapLayers the mapLayers to set
	 */
	public void setMapLayers(Set<MapLayer> mapLayers) {
		this.mapLayers = mapLayers;
	}

	/**
	 * @return the roles
	 */
	@ManyToMany(fetch = FetchType.EAGER, targetEntity=Role.class)
	@JoinTable(
		name = "TBL_GROUP_TBL_ROLE",
		joinColumns = {
			@JoinColumn(
				name = "GROUP_ID", nullable = false, updatable = false
			)
		},
		inverseJoinColumns = {
			@JoinColumn(
				name = "ROLE_ID", nullable = false, updatable = false
			)
		}
	)
	@Fetch(FetchMode.SUBSELECT)
	@JsonSerialize(using=LeanBaseModelSetSerializer.class)
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the group_module_list
	 */
	public String getGroup_module_list() {
		return group_module_list;
	}

	/**
	 * @param group_module_list the group_module_list to set
	 */
	public void setGroup_module_list(String group_module_list) {
		this.group_module_list = group_module_list;
	}

	/**
	 * @return the deletable
	 */
	public boolean isDeletable() {
		return deletable;
	}

	/**
	 * @param deletable the deletable to set
	 */
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	/**
	 * @return the grantedUsers
	 */
	@Transient
	public Set<Integer> getGrantedUsers() {
		return grantedUsers;
	}

	/**
	 * @param grantedUsers the grantedUsers to set
	 */
	@Transient
	public void setGrantedUsers(Set<Integer> grantedUsers) {
		this.grantedUsers = grantedUsers;
	}

	/**
	 * @return the grantedMapLayers
	 */
	@Transient
	public Set<Integer> getGrantedMapLayers() {
		return grantedMapLayers;
	}

	/**
	 * @param grantedMapLayers the grantedMapLayers to set
	 */
	@Transient
	public void setGrantedMapLayers(Set<Integer> grantedMapLayers) {
		this.grantedMapLayers = grantedMapLayers;
	}

	/**
	 * The method transforms the comma-separated list of module IDs stored in
	 * the member variable <i>user_module_list</i> into a list with
	 * {@link Module} objects. This list is set to the <i>modules</i> variable
	 * of the calling {@link Group} instance
	 *
	 * @param databaseDAO a {@link DatabaseDao} instance in order to access the database
	 */
	public void transformSimpleModuleListToModuleObjects(DatabaseDao databaseDAO) {

		// create Module object list from comma separated string (user_module_list)
		List<Module> newModules = null;
		if (this.getGroup_module_list() != null
				&& this.getGroup_module_list().equals("") == false) {

			// split the comma-separated list and make an Array of Integer
			String[] moduleIdArray = this.getGroup_module_list().split(",");
			List<Integer> intArray = new ArrayList<Integer>();
			for (int i = 0; i < moduleIdArray.length; i++) {
				Integer inte = new Integer(moduleIdArray[i]);
				intArray.add(inte);
			}

			// get the module objects from the database
			List<? extends Object> modules = databaseDAO.getEntitiesByIds(intArray.toArray(), Module.class);

			// iterate to cast the returned Objects to Module
			newModules = new ArrayList<Module>(modules.size());
			for (Iterator<?> iterator2 = modules.iterator(); iterator2.hasNext();) {
				Module module = (Module)iterator2.next();
				newModules.add(module);
			}

			modules = null;

		} else {

			// return an empty list
			newModules = new ArrayList<Module>();
		}

		newModules = null;
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
		return new HashCodeBuilder(7, 17). // two randomly chosen prime numbers
				appendSuper(super.hashCode()).
				append(getName()).
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
		if (!(obj instanceof Group))
			return false;
		Group other = (Group) obj;

		return new EqualsBuilder().
				appendSuper(super.equals(other)).
				append(getName(), other.getName()).
				isEquals();
	}

	/**
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("name", name)
			.append("group_nr", group_nr)
			.toString();
	}

}
