package de.terrestris.shogun.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import de.terrestris.shogun.dao.DatabaseDao;
import de.terrestris.shogun.serializer.LeanBaseModelSerializer;

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
public class User extends BaseModel {

	private String user_name;
	private String user_longname;
	private String user_email;
	private String user_street;
	private String user_postcode;
	private String user_city;
	private String user_country;
	private String user_password;
	private String user_lang;
	private Boolean active = true;

	private Set<Group> groups;
	private Set<Module> modules;
	private MapConfig mapConfig;
	private WfsProxyConfig wfsProxyConfig;
	private WmsProxyConfig wmsProxyConfig;
	private String user_module_list;


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
	@JsonSerialize(using=LeanBaseModelSerializer.class)
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
	 * @return the modules
	 */
	@ManyToMany(fetch = FetchType.LAZY, targetEntity=Module.class)
	@JoinTable(name = "TBL_USER_TBL_MODULE",  joinColumns = {
			@JoinColumn(name = "USER_ID", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "MODULE_ID",
					nullable = false, updatable = false) })
	@JsonSerialize(using=LeanBaseModelSerializer.class)
	@Fetch(FetchMode.SUBSELECT)
	public Set<Module> getModules() {
		return modules;
	}

	/**
	 *
	 * @param modules the modules to set
	 */
	@JsonIgnore
	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

//	/**
//	 * We have to use a Set instead of List, due to a know limitation
//	 * http://jeremygoodell.com/2009/03/26/cannot-simultaneously-fetch-multiple-bags.aspx
//	 *
//	 * @return the mapConfigs
//	 */
//	@OneToMany(fetch = FetchType.LAZY, targetEntity=MapConfig.class)
//	@Cache(usage = CacheConcurrencyStrategy.NONE)
//	public Set<MapConfig> getMapConfigs() {
//		return mapConfigs;
//	}
//
//	/**
//	 * We have to use a Set instead of List, due to a know limitation
//	 * http://jeremygoodell.com/2009/03/26/cannot-simultaneously-fetch-multiple-bags.aspx
//	 *
//	 * @param mapConfigs the mapConfigs to set
//	 */
//	@JsonIgnore
//	public void setMapConfigs(Set<MapConfig> mapConfigs) {
//		this.mapConfigs = mapConfigs;
//	}


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
	 * @return the simpleModuleList
	 */
	@Column(name="USER_MODULE_LIST")
	public String getUser_module_list() {
		return user_module_list;
	}

	/**
	 * @param simpleModuleList the simpleModuleList to set
	 */
	public void setUser_module_list(String simpleModuleList) {
		this.user_module_list = simpleModuleList;
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
	 * Will return the unification of all maplayers of all groups the user
	 * belongs to.
	 * 
	 * @return
	 */
	@Transient
	public Set<MapLayer> getMapLayers() {
		Set<MapLayer> allMapLayers = new HashSet<MapLayer>();
		Set<Group> groups = this.getGroups();
		if (groups != null) {
			for (Group group : groups) {
				allMapLayers.addAll(group.getMapLayers());
			}
		}
		return allMapLayers;
	}

	// ----------------------------------------------------------------


	/**
	 *
	 * @param databaseDAO
	 */
	public void transformSimpleModuleListToModuleObjects(DatabaseDao databaseDAO) {
		// create module object list from comma-separated list
		Set<Module> newModules = null;

		if (this.getUser_module_list() != null
				&& this.getUser_module_list().equals("") == false) {

			String[] moduleIdArray = this.getUser_module_list().split(",");
			List<Integer> intArray = new ArrayList<Integer>();

			for (int i = 0; i < moduleIdArray.length; i++) {
				Integer inte = new Integer(moduleIdArray[i]);
				intArray.add(inte);
			}

			List<? extends Object> modules = databaseDAO.getEntitiesByIds(
					intArray.toArray(), Module.class);

			newModules = new HashSet<Module>(modules.size());
			for (Iterator<?> iterator2 = modules.iterator(); iterator2.hasNext();) {
				Module module = (Module) iterator2.next();
				newModules.add(module);
			}

			modules = null;

		} else {
			newModules = new HashSet<Module>();
		}

		Set<Module> moduleSet = new HashSet<Module>(newModules);
		this.setModules(moduleSet);
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
	 * Returns whether the user has the given role in his set of roles by
	 * Comparing the name of all roles to the given string.
	 *
	 * @param searchRoleName The rolename to check this users set of roles for
	 * @return whether the user has the queried role or not.
	 */
	private boolean hasRole(String searchRoleName) {
		boolean hasRole = false;
		for (Group g : this.getGroups()) {
			for (Role r : g.getRoles()) {
				hasRole = r.getName().equals(searchRoleName);
				if (hasRole) {
					break;
				}
			}
			if (hasRole) {
				break;
			}
		}
		return hasRole;
	}
	
}