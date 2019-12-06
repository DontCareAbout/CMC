package us.dontcareabout.cmc.common.shared;

public enum Museum {
	Met("Met", "https://www.metmuseum.org/art/collection/search/"),
	;

	public final String title;
	public final String url;

	private Museum(String title, String url) {
		this.title = title;
		this.url = url;
	}
}
