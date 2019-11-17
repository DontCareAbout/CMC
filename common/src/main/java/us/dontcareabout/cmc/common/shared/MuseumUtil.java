package us.dontcareabout.cmc.common.shared;

public class MuseumUtil {
	public static String parseUrlId(Museum museum, String url) {
		switch(museum) {
		case Met:
			int index = url.indexOf("?");
			return url.substring(Museum.Met.url.length(), index == -1 ? url.length() : index);
		}

		throw new IllegalArgumentException(museum + "is undefined");
	}

	public static String purgeUrl(Museum museum, String url) {
		return museum.url + parseUrlId(museum, url);
	}
}
