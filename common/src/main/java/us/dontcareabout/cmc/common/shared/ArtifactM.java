package us.dontcareabout.cmc.common.shared;

import java.util.ArrayList;

/**
 * 博物館系統提供的 artifact 資訊。
 */
public class ArtifactM {
	private Museum museum;
	private String urlId;

	private String name;
	private String era;
	private String origin;
	private String material;
	private String description;
	private String dimensions;
	private ArrayList<ImageUrl> image = new ArrayList<>();

	public Museum getMuseum() {
		return museum;
	}
	public void setMuseum(Museum museum) {
		this.museum = museum;
	}
	public String getUrlId() {
		return urlId;
	}
	public void setUrlId(String urlId) {
		this.urlId = urlId;
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
	public ArrayList<ImageUrl> getImage() {
		return image;
	}
}
