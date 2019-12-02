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
import us.dontcareabout.cmc.server.chrome.Agent;
import us.dontcareabout.cmc.server.museum.exception.ArtifactNotExistException;
import us.dontcareabout.cmc.server.museum.exception.MuseumNotReadyException;

public class CollectionManager {
	private final HashMap<Museum, Researcher> map = new HashMap<>();
	private final File storage;
	private final Agent agent;

	public CollectionManager(String storageFolder, String chromeFolder) {
		storage = new File(storageFolder);
		agent = new Agent(chromeFolder, 10);

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
		agent.fetch(find(museum).artifactUrl(urlId), locate(museum, urlId));
	}

	/**
	 * 從 CMC 的 collection 取得文物資料。
	 */
	public ArtifactM obtain(Museum museum, String urlId) throws ArtifactNotExistException, MuseumNotReadyException, IOException {
		File artifact = locate(museum, urlId);

		if (!artifact.exists()) { throw new ArtifactNotExistException(); }

		ArtifactM result = find(museum).translate(DataUtil.load(artifact, "UTF-8", ""));
		result.setMuseum(museum);
		result.setUrlId(urlId);
		return result;
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

	private Researcher find(Museum museum) throws MuseumNotReadyException {
		Researcher collector = map.get(museum);

		if (collector == null) { throw new MuseumNotReadyException(); }

		return collector;
	}
}
