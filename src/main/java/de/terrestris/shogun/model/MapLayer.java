package de.terrestris.shogun.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import de.terrestris.shogun.serializer.LeanBaseModelSerializer;


/**
 * MapLayer POJO
 *
 * Please note that we extend the class BaseModelInheritance here to have
 * DB-inheritance (that class has the correct id-strategy) needed here.<br><br>
 *
 * Instead of @MappedSuperclass we use an abstract class acting as entity (@Entity).
 * This way we can use MapLayer as type of Sets when using one-to-many-relations
 * (see e.g {@link User}).
 *
 * @see http://stackoverflow.com/questions/2912988/persist-collection-of-interface-using-hibernate
 *
 * @author terrestris GmbH & Co. KG
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MapLayer extends BaseModelInheritance {

	private String name;
	private String type;
	private Boolean isBaseLayer = false;
	private String alwaysInRange = null;
	private Boolean visibility = true;
	private Boolean displayInLayerSwitcher = true;
	private String attribution = null;
	private Integer gutter = null;
	private String projection = null;
	private String units = null;
	private String scales = null;
	private String resolutions = null;
	private String maxExtent = null;
	private String minExtent = null;
	private Double maxResolution = null;
	private Double minResolution = null;
	private Double maxScale = null;
	private Double minScale = null;
	private Integer numZoomLevels = null;
	private Boolean displayOutsideMaxExtent = false;
	private String transitionEffect = null;

	private Set<LayerMetadata> metadata;
	private Set<Group> groups;
	
	private User owner;

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
	 * @return the type
	 */
	@Column(name = "TYPE", nullable = false)
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the isBaseLayer
	 */
	@Column(name = "ISBASELAYER", nullable = false)
	public Boolean getIsBaseLayer() {
		return isBaseLayer;
	}

	/**
	 * @param isBaseLayer the isBaseLayer to set
	 */
	public void setIsBaseLayer(Boolean isBaseLayer) {
		this.isBaseLayer = isBaseLayer;
	}


	/**
	 * @return the visibility
	 */
	@Column(name = "VISIBILITY", nullable = false)
	public Boolean getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(Boolean visibility) {
		this.visibility = visibility;
	}

	/**
	 * @return the alwaysInRange
	 */
	@Column(name = "ALWAYSINRANGE")
	public String getAlwaysInRange() {
		return alwaysInRange;
	}

	/**
	 * @param alwaysInRange the alwaysInRange to set
	 */
	public void setAlwaysInRange(String alwaysInRange) {
		this.alwaysInRange = alwaysInRange;
	}

	/**
	 * @return the displayInLayerSwitcher
	 */
	@Column(name = "DISPLAYINLAYERSWITCHER", nullable = false)
	public Boolean getDisplayInLayerSwitcher() {
		return displayInLayerSwitcher;
	}

	/**
	 * @param displayInLayerSwitcher the displayInLayerSwitcher to set
	 */
	public void setDisplayInLayerSwitcher(Boolean displayInLayerSwitcher) {
		this.displayInLayerSwitcher = displayInLayerSwitcher;
	}

	/**
	 * @return the attribution
	 */
	@Column(name = "ATTRIBUTION")
	public String getAttribution() {
		return attribution;
	}

	/**
	 * @param attribution the attribution to set
	 */
	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	/**
	 * @return the gutter
	 */
	@Column(name = "GUTTER", nullable = true)
	public Integer getGutter() {
		return gutter;
	}

	/**
	 * @param gutter the gutter to set
	 */
	public void setGutter(Integer gutter) {
		this.gutter = gutter;
	}

	/**
	 * @return the projection
	 */
	@Column(name = "PROJECTION")
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
	@Column(name = "UNITS")
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
	 * @return the scales
	 */
	@Column(name = "SCALES")
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
	 * @return the resolutions
	 */
	@Column(name = "RESOLUTIONS")
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
	 * @return the maxExtent
	 */
	@Column(name = "MAXEXTENT")
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
	 * @return the minExtent
	 */
	@Column(name = "MINEXTENT")
	public String getMinExtent() {
		return minExtent;
	}

	/**
	 * @param minExtent the minExtent to set
	 */
	public void setMinExtent(String minExtent) {
		this.minExtent = minExtent;
	}

	/**
	 * @return the maxResolution
	 */
	@Column(name = "MAXRESOLUTION")
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
	 * @return the minResolution
	 */
	@Column(name = "MINRESOLUTION")
	public Double getMinResolution() {
		return minResolution;
	}

	/**
	 * @param minResolution the minResolution to set
	 */
	public void setMinResolution(Double minResolution) {
		this.minResolution = minResolution;
	}

	/**
	 * @return the maxScale
	 */
	@Column(name = "MAXSCALE")
	public Double getMaxScale() {
		return maxScale;
	}

	/**
	 * @param maxScale the maxScale to set
	 */
	public void setMaxScale(Double maxScale) {
		this.maxScale = maxScale;
	}

	/**
	 * @return the minScale
	 */
	@Column(name = "MINSCALE")
	public Double getMinScale() {
		return minScale;
	}

	/**
	 * @param minScale the minScale to set
	 */
	public void setMinScale(Double minScale) {
		this.minScale = minScale;
	}

	/**
	 * @return the numZoomLevels
	 */
	@Column(name = "NUMZOOMLEVELS")
	public Integer getNumZoomLevels() {
		return numZoomLevels;
	}

	/**
	 * @param numZoomLevels the numZoomLevels to set
	 */
	public void setNumZoomLevels(Integer numZoomLevels) throws Exception {
		this.numZoomLevels = numZoomLevels;
	}

	/**
	 * @return the displayOutsideMaxExtent
	 */
	@Column(name = "DISPLAYOUTSIDEMAXEXTENT", nullable = false)
	public Boolean getDisplayOutsideMaxExtent() {
		return displayOutsideMaxExtent;
	}

	/**
	 * @param displayOutsideMaxExtent the displayOutsideMaxExtent to set
	 */
	public void setDisplayOutsideMaxExtent(Boolean displayOutsideMaxExtent) {
		this.displayOutsideMaxExtent = displayOutsideMaxExtent;
	}

	/**
	 * @return the transitionEffect
	 */
	@Column(name = "TRANSITIONEFFECT")
	public String getTransitionEffect() {
		return transitionEffect;
	}

	/**
	 * @param transitionEffect the transitionEffect to set
	 */
	public void setTransitionEffect(String transitionEffect) {
		this.transitionEffect = transitionEffect;
	}

	/**
	 * @return the metadata
	 */
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name="TBL_MAPLAYER_TBL_METADATA")
//	@JsonSerialize(using=LeanBaseModelSerializer.class)
	public Set<LayerMetadata> getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Set<LayerMetadata> metadata) {
		this.metadata = metadata;
	}

//	/**
//	 * @return the users
//	 */
//	@ManyToMany(mappedBy="mapLayers", fetch=FetchType.LAZY)
//	@JsonIgnore
//	@Fetch(FetchMode.SUBSELECT)
//	@JsonSerialize(using=LeanBaseModelSerializer.class)
//	public Set<User> getUsers() {
//		return users;
//	}
//
//	/**
//	 * @param users the users to set
//	 */
//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}

	/**
	 * @return the groups
	 */
	@ManyToMany(mappedBy="mapLayers", fetch=FetchType.LAZY)
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
	 * @return the owner
	 */
	@ManyToOne
	@JsonIgnore
	@Fetch(FetchMode.SELECT)
	// foreign key needed here, otherwise hibernate will generate name which is too long ( > 30 chars)
	@ForeignKey(name="FKOWNERID")
	public User getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
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
		return new HashCodeBuilder(13, 23). // two randomly chosen prime numbers
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
		if (!(obj instanceof MapLayer))
			return false;
		MapLayer other = (MapLayer) obj;

		return new EqualsBuilder().
				appendSuper(super.equals(other)).
				append(getName(), other.getName()).
				isEquals();
	}
}

