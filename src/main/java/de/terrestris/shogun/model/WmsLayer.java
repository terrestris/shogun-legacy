package de.terrestris.shogun.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * Wms POJO
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@JsonAutoDetect
@Entity
@Table(name="TBL_WMSLAYER")
public class WmsLayer extends BaseModel {

	/**
	 *
	 */
	private String name;

	/**
	 * The default Constructor
	 *
	 * @param name
	 */
	public WmsLayer() {

	}

	/**
	 *
	 */
	public WmsLayer(String name) {
		this.name = name;

		this.setCreated_at(new Date());
		this.setUpdated_at(new Date());
	}

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
	 *
	 */
	public String toString(){
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.appendSuper(super.toString())
			.append("name", name)
			.toString();
	}
}