package us.dontcareabout.cmc.common.shared;

import java.util.ArrayList;

/**
 * 博物館系統提供的 artifact 資訊。
 */
public class ArtifactM {
	private ArtifactId id;
	private String name;
	private String era;
	private String origin;
	private String material;
	private String description;
	private String dimensions;
	private ArrayList<ImageUrl> image = new ArrayList<>();

	public ArtifactId getId() {
		return id;
	}
	public void setId(ArtifactId id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEra() {
		return era;
	}
	public void setEra(String era) {
		this.era = era;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDimensions() {
		return dimensions;
	}
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}
	@Deprecated	//為了配合 gwt-jackson 而加的，不符合 coding 習慣所以掛 deprecated
	public void setImage(ArrayList<ImageUrl> image) {
		this.image = image;
	}
	public ArrayList<ImageUrl> getImage() {
		return image;
	}
}
