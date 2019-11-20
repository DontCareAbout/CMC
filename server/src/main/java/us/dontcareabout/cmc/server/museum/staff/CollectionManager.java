package us.dontcareabout.cmc.server.museum.staff;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.jsoup.helper.DataUtil;

import com.google.common.io.Files;

import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.common.shared.MuseumUtil;

public class CollectionManager {
	private final HashMap<Museum, Researcher> map = new HashMap<>();
	private final File storage;

	public CollectionManager(String storageFolder) {
		storage = new File(storageFolder);

		if (!storage.exists()) { storage.mkdirs(); }
	}

	/**
	 * 註冊哪些博物館由哪些 {@link Researcher} 負責處理。
	 */
	public void recruit(Museum museum, Researcher researcher) {
		map.put(museum, researcher);
	}

	/**
	 * 從各博物館網頁把資料抓回來儲存。
	 */
	public void purchase(Museum museum, String urlId) throws Exception {
		store(museum, urlId, find(museum).transport(urlId));
	}

	/**
	 * 從 CMC 的 collection 取得文物資料。
	 */
	public ArtifactM obtain(Museum museum, String urlId) throws Exception {
		File artifact = locate(museum, urlId);

		if (!artifact.exists()) { throw new Exception(); }	//TODO 改良 exception

		return find(museum).translate(DataUtil.load(artifact, "UTF-8", ""));
	}

	/**
	 * 將網頁資料儲存到 CMC 的 collection 當中。
	 */
	public void store(Museum museum, String urlId, String html) throws IOException {
		Files.write(
			html,
			locate(museum, urlId),
			StandardCharsets.UTF_8
		);
	}

	private File locate(Museum museum, String urlId) {
		return new File(storage, MuseumUtil.artifactId(museum, urlId));
	}

	private Researcher find(Museum museum) throws Exception {
		Researcher collector = map.get(museum);

		if (collector == null) { throw new Exception(); }	//TODO 改良 exception

		return collector;
	}
}
