package us.dontcareabout.cmc.server.museum.collection;

import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.ImageUrl;
import us.dontcareabout.cmc.server.museum.staff.Researcher;

public class Met implements Researcher {
	@Override
	public ArtifactM translate(Document doc) {
		ArtifactM result = new ArtifactM();

		result.setName(text(doc.getElementsByClass("artwork__title--text")));

		HashMap<String, String> tombstoneMap = new HashMap<>();
		Elements tombstoneLabel = doc.getElementsByClass("artwork__tombstone--label");

		for (Element tsl : tombstoneLabel) {
			tombstoneMap.put(tsl.text(), tsl.nextElementSibling().html());
		}

		result.setEra(tombstoneMap.get("Date:"));
		result.setOrigin(tombstoneMap.get("Culture:"));
		result.setMaterial(tombstoneMap.get("Medium:"));
		result.setDescription(
			//因為（應該）只有一個所以直接 get(0)
			//裡頭還會再包一個 <p> 所以傳 children()（也正好符合 text() 的參數格式 XD）
			text(doc.getElementsByClass("artwork__intro__desc").get(0).children())
		);
		result.setDimensions(tombstoneMap.get("Dimensions:"));

		image(doc.getElementsByClass("met-carousel__item"), result);

		//只有單張圖，就沒有 met-carousel__item...... Orz
		if (result.getImage().size() == 0) {
			Elements elements = doc.getElementsByClass("gtm__download__image");
			if (elements.size() != 0) {
				String url = elements.get(0).attr("href");
				result.getImage().add(new ImageUrl(url.replace("original", "web-additional"), url));
			}
		}

		return result;
	}

	private static String text(Elements elements) {
		if (elements.size() < 1) { return ""; }

		StringBuffer result = new StringBuffer(elements.get(0).html());

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
				c.child(0).attr("data-superjumboimage")
			));
		}
	}
}
