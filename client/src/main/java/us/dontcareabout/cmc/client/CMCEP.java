package us.dontcareabout.cmc.client;

import com.google.gwt.user.client.Window;

import us.dontcareabout.cmc.client.data.DataCenter;
import us.dontcareabout.cmc.client.ui.UiCenter;
import us.dontcareabout.gst.client.GSTEP;
import us.dontcareabout.gst.client.data.SheetIdDao;
import us.dontcareabout.gwt.client.iCanUse.Feature;

public class CMCEP extends GSTEP {
	public CMCEP() {
		super("CMC-SheetId", "1BFK7PYqXOe4eH8_p9cXVN3o-t48gZsRh_CB-VAgpd7U");
		needFeature(Feature.WebSocket);
		needFeature(Feature.Canvas);
	}

	@Override
	protected String version() { return "0.0.1"; }

	@Override
	protected String defaultLocale() { return "zh_TW"; }

	@Override
	protected void featureFail() {
		Window.alert("這個瀏覽器我不尬意，不給用..... \\囧/");
	}

	@Override
	protected void start() {
		UiCenter.start();
		DataCenter.wantArtifact(SheetIdDao.priorityValue());
	}
}
