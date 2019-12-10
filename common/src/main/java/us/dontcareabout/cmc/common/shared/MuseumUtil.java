package us.dontcareabout.cmc.common.shared;

import us.dontcareabout.cmc.common.shared.exception.MuseumNotFoundException;

public class MuseumUtil {
	public static ArtifactId toArtifactId(String url) throws MuseumNotFoundException {
		ArtifactId result = new ArtifactId();

		for (Museum m : Museum.values()) {
			if (url.startsWith(m.url)) {
				result.setMuseum(m);
				result.setUrlId(parseUrlId(m, url));
				return result;
			}
		}

		throw new MuseumNotFoundException(url + " 無對應 Museum");
	}

	public static String toUrl(ArtifactId aid) {
		switch(aid.getMuseum()) {
		//urlId 結尾區
		case Met:
			return aid.getMuseum().url + aid.getUrlId();
		case RoyalArmouries:
			return aid.getMuseum().url + aid.getUrlId() + ".html";
		default:
			return "";	//理論上不會發生
		}
	}

	private static String parseUrlId(Museum museum, String url) {
		switch(museum) {
		case Met:
			int index = url.indexOf("?");
			return url.substring(Museum.Met.url.length(), index == -1 ? url.length() : index);
		case RoyalArmouries:
			return url.substring(Museum.RoyalArmouries.url.length(), url.length() - 5);
		}

		throw new IllegalArgumentException(museum + " is undefined");
	}
}
