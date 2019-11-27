package us.dontcareabout.cmc.client;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;

/**
 * 儲存在 local storage 的使用者參數 VO 以及存取 API
 */
public class Parameter {
	private static Mapper mapper = GWT.create(Mapper.class);
	private static Storage storage = Storage.getLocalStorageIfSupported();
	private static final String KEY = "SETTING";

	private String server = "http://localhost:8080/server";

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public static Parameter retrieve() {
		String json = storage.getItem(KEY);
		return json == null ? new Parameter() : mapper.read(json);
	}

	public static void store(Parameter value) {
		storage.setItem(KEY, mapper.write(value));
	}

	interface Mapper extends ObjectMapper<Parameter> {}
}
