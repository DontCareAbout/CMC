package us.dontcareabout.cmc.client;

import com.google.gwt.user.client.Window;

import us.dontcareabout.cmc.client.data.DataCenter;
import us.dontcareabout.cmc.client.data.SheetId;
import us.dontcareabout.cmc.client.ui.UiCenter;
import us.dontcareabout.gwt.client.GFEP;
import us.dontcareabout.gwt.client.iCanUse.Feature;

public class CMCEP extends GFEP {
	public CMCEP() {
		needFeature(Feature.Storage);
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
		DataCenter.wantArtifact(SheetId.get());
	}
}
