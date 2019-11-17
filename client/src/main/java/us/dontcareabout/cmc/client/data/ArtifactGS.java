package us.dontcareabout.cmc.client.data;

import java.util.ArrayList;

import com.google.common.base.Strings;

import us.dontcareabout.gwt.client.google.SheetEntry;

/**
 * Google Sheet 提供的 artifact 資訊
 */
public final class ArtifactGS extends SheetEntry {
	protected ArtifactGS() {}

	public String getUrl() {
		return stringField("網址");
	}

	public String getNote() {
		return stringField("備註");
	}

	public Double getScore() {
		return doubleField("重要性");
	}

	public ArrayList<String> getTag() {
		ArrayList<String> result = new ArrayList<>();
		String[] value = stringField("tag").split(",");

		for (String text : value) {
			text = text.trim();
			if (Strings.isNullOrEmpty(text)) { continue; }
			result.add(text);
		}

		return result;
	}
}
