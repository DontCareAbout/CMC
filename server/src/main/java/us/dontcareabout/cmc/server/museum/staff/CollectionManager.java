package us.dontcareabout.cmc.server.museum.staff;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.jsoup.helper.DataUtil;

import com.google.common.io.Files;

import us.dontcareabout.cmc.common.shared.ArtifactId;
import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.common.shared.exception.MuseumNotFoundException;
import us.dontcareabout.cmc.server.chrome.Agent;
import us.dontcareabout.cmc.server.museum.exception.ArtifactNotExistException;

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
	public void purchase(ArtifactId aid) throws Exception {
		agent.fetch(
			find(aid.getMuseum()).artifactUrl(aid.getUrlId()),
			locate(aid)
		);
	}

	/**
	 * 從 CMC 的 collection 取得文物資料。
	 */
	public ArtifactM obtain(ArtifactId aid) throws ArtifactNotExistException, MuseumNotFoundException, IOException {
		File artifact = locate(aid);

		if (!artifact.exists()) { throw new ArtifactNotExistException(); }

		ArtifactM result = find(aid.getMuseum()).translate(DataUtil.load(artifact, "UTF-8", ""));
		result.setId(aid);
		return result;
	}

	/**
	 * 將網頁資料儲存到 CMC 的 collection 當中。
	 */
	public void store(ArtifactId aid, String html) throws IOException {
		Files.write(
			html,
			locate(aid),
			StandardCharsets.UTF_8
		);
	}

	private File locate(ArtifactId aid) {
		return new File(storage, aid.toString());
	}

	private Researcher find(Museum museum) throws MuseumNotFoundException {
		Researcher collector = map.get(museum);

		if (collector == null) { throw new MuseumNotFoundException("缺少 " + museum.title + " Researcher"); }

		return collector;
	}
}
