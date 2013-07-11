package de.terrestris.shogun.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

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
	 * Returns a string-representation of an instance of the class.<br><br>
	 *
	 * The string representation of this class (or child-classes) consists of
	 * the simple name of the class, followed by the #-sign, followed by the
	 * instance id-field, followed by '@', followed by the unsigned hexadecimal
	 * representation of the hash code of the object.<br><br>
	 *
	 * Example output for an instance with id=1 of the
	 * class 'WmsMapLayer':<br><br>
	 *
	 * <code>WmsMapLayer#1@3ac6acd3</code><br>
	 *
	 * @return String The string-representation of an instance of the class.
	 */
	public String toString(){
		return this.getClass().getSimpleName() +
			"#" + id +
			"@" + Integer.toHexString(hashCode());
	}
}