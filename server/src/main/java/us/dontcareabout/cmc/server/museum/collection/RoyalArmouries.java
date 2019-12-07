package us.dontcareabout.cmc.server.museum.collection;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.ImageUrl;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.server.museum.staff.Researcher;

public class RoyalArmouries implements Researcher {
	@Override
	public String artifactUrl(String urlId) {
		return Museum.RoyalArmouries.url + urlId + ".html";
	}

	@Override
	public ArtifactM translate(Document doc) {
		ArtifactM result = new ArtifactM();

		result.setOrigin(doc.getElementsByClass("creation ng-binding ng-scope").get(0).text().substring(7));

		Element main = doc.getElementById("main");

		for (Element child : main.children()) {
			if ("div" != child.tagName()) { continue; }

			Element element = child.child(1);

			switch(child.child(0).text()) {
			case "Object Title":
				result.setName(element.text()); break;
			case "Date":
				result.setEra(element.text());	break;
			case "Materials":
				result.setMaterial(element.text()); break;
			case "Physical Description":
				result.setDescription(element.child(0).html()); break;
			case "Dimensions":
				if (element.children().size() == 0) {
					result.setDimensions(element.text());
				} else {
					//原本表格有給 cellpadding 但是完全沒效果
					//所以只好自己讓每個 td 都掛上 padding 的 style
					for(Element td : element.getElementsByTag("td")) {
						td.attr("style", "padding:2px 6px 2px 2px;");
					}

					element.child(0).clearAttributes();
					result.setDimensions(element.html());
				}
				break;
			}
		}

		for (Element e : doc.getElementsByTag("figure")) {
			Element child = e.child(0);

			if (!"a".equals(child.tagName())) { continue; }

			result.getImage().add(new ImageUrl(e.child(0).child(0).attr("src"), e.child(0).attr("href")));
		}

		return result;
	}

}
