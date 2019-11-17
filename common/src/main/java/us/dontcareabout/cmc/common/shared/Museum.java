package us.dontcareabout.cmc.common.shared;

public enum Museum {
	Met("https://www.metmuseum.org/art/collection/search/"),
	;

	public final String url;

	private Museum(String url) {
		this.url = url;
	}
}
