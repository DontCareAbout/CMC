package us.dontcareabout.cmc.common.shared;

public class ImageUrl {
	private String standard;
	private String fullSize;

	public ImageUrl() {}

	public ImageUrl(String standard, String fullSize) {
		this.standard = standard;
		this.fullSize = fullSize;
	}

	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getFullSize() {
		return fullSize;
	}
	public void setFullSize(String fullSize) {
		this.fullSize = fullSize;
	}
}
