package us.dontcareabout.cmc.server.museum.staff;

import org.jsoup.nodes.Document;

import us.dontcareabout.cmc.common.shared.ArtifactM;

/**
 * 傳說中博物館的典藏人員叫做 reseacher，
 * 借用這個角色名詞來代表處理各博物館數位典藏網頁的 interface。
 */
public interface Researcher {
	/**
	 * 將網頁內容轉換為 {@link ArtifactM}。
	 */
	ArtifactM translate(Document html);
}
