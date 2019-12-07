package us.dontcareabout.cmc.common.shared;

public class MuseumUtil {
	public static String parseUrlId(Museum museum, String url) {
		switch(museum) {
		case Met:
			int index = url.indexOf("?");
			return url.substring(Museum.Met.url.length(), index == -1 ? url.length() : index);
		case RoyalArmouries:
			return url.substring(Museum.RoyalArmouries.url.length(), url.length() - 5);
		}

		throw new IllegalArgumentException(museum + " is undefined");
	}

	//Refactory 跟 Researcher.artifactUrl() 功能重複？
	public static String purgeUrl(Museum museum, String url) {
		switch(museum) {
		//urlId 結尾區
		case Met:
			return museum.url + parseUrlId(museum, url);
		//本來就很乾淨區
		case RoyalArmouries:
			return url;
		default:
			return null;
		}
	}

	/**
	 * 這是因應系統運作所產生的定義，與現實世界的 artifact 識別無關。
	 */
	public static String artifactId(Museum museum, String urlId) {
		return museum.name() + "." + urlId;
	}
}
