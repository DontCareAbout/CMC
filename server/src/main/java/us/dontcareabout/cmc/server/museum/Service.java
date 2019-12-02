package us.dontcareabout.cmc.server.museum;

import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.server.Setting;
import us.dontcareabout.cmc.server.museum.collection.Met;
import us.dontcareabout.cmc.server.museum.staff.CollectionManager;

public class Service {
	private static final Setting setting = new Setting();

	public static final CollectionManager collection = new CollectionManager(setting.workspace(), setting.chromeFolder());
	static {
		collection.recruit(Museum.Met, new Met());
	}
}
