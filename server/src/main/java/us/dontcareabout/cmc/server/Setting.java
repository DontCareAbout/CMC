package us.dontcareabout.cmc.server;

import us.dontcareabout.java.common.DoubleProperties;

public class Setting extends DoubleProperties {
	public Setting() {
		super("dev-setting.xml", "CMC.xml");
	}

	public String workspace() {
		return getProperty("workspace");
	}

	public String chromeFolder() {
		return getProperty("chromeFolder");
	}
}
