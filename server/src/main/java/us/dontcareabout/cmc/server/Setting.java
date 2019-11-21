package us.dontcareabout.cmc.server;

import us.dontcareabout.java.common.DoubleProperties;

public class Setting extends DoubleProperties {
	private static String workspace;

	public Setting() {
		super("dev-setting.xml", "CMC.xml");
	}

	public String workspace() {
		if (workspace == null) {
			workspace = getProperty("workspace");
		}

		return workspace;
	}
}
