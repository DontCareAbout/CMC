package us.dontcareabout.cmc.server.museum.collection;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.ImageUrl;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.server.museum.staff.Researcher;

public class Met implements Researcher {
	@Override
	public String transport(String urlId) throws Exception {
		//開發測試時有被 Met 認定是機器人不給抓的經驗（包含用 headless Chrome）
		//XXX 需要再確認實作方式
		return Jsoup.connect(Museum.Met.url + urlId).validateTLSCertificates(false)
			.get().toString();
	}

	@Override
	public ArtifactM translate(Document doc) throws IOException {
		ArtifactM result = new ArtifactM();

		result.setName(text(doc.getElementsByClass("artwork__title--text")));

		HashMap<String, String> tombstoneMap = new HashMap<>();
		Elements tombstoneLabel = doc.getElementsByClass("artwork__tombstone--label");

		for (Element tsl : tombstoneLabel) {
			tombstoneMap.put(tsl.text(), tsl.nextElementSibling().text());
		}

		result.setEra(tombstoneMap.get("Date:"));
		result.setOrigin(tombstoneMap.get("Culture:"));
		result.setMaterial(tombstoneMap.get("Medium:"));
		result.setDescription(text(doc.getElementsByClass("artwork__intro__desc")));
		result.setDimensions(tombstoneMap.get("Dimensions:"));

		image(doc.getElementsByClass("met-carousel__item"), result);

		return result;
	}

	private static String text(Elements elements) {
		if (elements.size() < 1) { return ""; }

		StringBuffer result = new StringBuffer(elements.get(0).text());

		for (int i = 1; i < elements.size(); i++) {
			result.append(" / ");
			result.append(elements.get(i).text());
		}

		return result.toString();
	}

	private static void image(Elements elements, ArtifactM artifact) {
		for (Element c : elements) {
			artifact.getImage().add(new ImageUrl(
				c.child(0).attr("src"),
				c.child(0).attr("data-largeimage")
			));
		}
	}
}
