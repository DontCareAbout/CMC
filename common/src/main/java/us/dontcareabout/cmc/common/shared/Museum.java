package us.dontcareabout.cmc.common.shared;

public enum Museum {
	Met("Met", "https://www.metmuseum.org/art/collection/search/"),
	RoyalArmouries("Royal Armouries", "https://collections.royalarmouries.org/object/rac-object-"),
	;

	public final String title;
	public final String url;

	private Museum(String title, String url) {
		this.title = title;
		this.url = url;
	}
}
