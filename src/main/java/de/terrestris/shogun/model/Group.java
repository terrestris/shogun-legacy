package de.terrestris.shogun.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import de.terrestris.shogun.dao.DatabaseDAO;

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
public class Group extends BaseModel{
	
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

	private List<Module> modules;
	private String group_module_list;
    

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
	@Column(name="GROUP_NR", nullable=false)
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
	 * @return the modules
	 */
//	@Cache(usage = CacheConcurrencyStrategy.NONE)
	@ManyToMany(fetch = FetchType.EAGER, targetEntity=Module.class)
	@JoinTable(name = "TBL_GROUP_TBL_MODULE",  joinColumns = { 
			@JoinColumn(name = "GROUP_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "MODULE_ID", 
					nullable = false, updatable = false) })
	public List<Module> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<Module> modules) {
		this.modules = modules;
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
	 * The method transforms the comma-separated list of module IDs stored in
	 * the member variable <i>user_module_list</i> into a list with 
	 * {@link Module} objects. This list is set to the <i>modules</i> variable 
	 * of the calling {@link Group} instance
	 * 
	 * @param databaseDAO a {@link DatabaseDAO} instance in order to access the database
	 */
	public void transformSimpleModuleListToModuleObjects(DatabaseDAO databaseDAO) {
		
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
			for (Iterator iterator2 = modules.iterator(); iterator2.hasNext();) {
				Module module = (Module)iterator2.next();
				newModules.add(module);
			}

			modules = null;

		} else {
			
			// return an empty list
			newModules = new ArrayList<Module>();
		}

//		this.setModules(newModules);
		newModules = null;
	}

}