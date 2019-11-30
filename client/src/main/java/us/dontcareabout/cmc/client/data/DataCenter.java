package us.dontcareabout.cmc.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.cmc.client.data.ArtifactMReadyEvent.ArtifactMReadyHandler;
import us.dontcareabout.cmc.client.data.ArtifactReadyEvent.ArtifactReadyHandler;
import us.dontcareabout.cmc.client.data.WsReadyChangeEvent.WsReadyChangeHandler;
import us.dontcareabout.cmc.client.ui.UiCenter;
import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.common.shared.Selection;
import us.dontcareabout.gwt.client.Console;
import us.dontcareabout.gwt.client.google.Sheet;
import us.dontcareabout.gwt.client.google.SheetHappen;
import us.dontcareabout.gwt.client.google.SheetHappen.Callback;
import us.dontcareabout.gwt.client.websocket.event.ErrorEvent;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	////////////////

	private static ArrayList<Artifact> artifactList;

	public static List<Artifact> getArtifact() {
		return Collections.unmodifiableList(artifactList);
	}

	public static void wantArtifact(String sheetId) {
		//TODO 處理多個 museum work sheet
		int index = 1;
		SheetHappen.get(SheetId.get(), index, new Callback<ArtifactGS>() {
			@Override
			public void onSuccess(Sheet<ArtifactGS> gs) {
				artifactList = new ArrayList<>();
				Museum museum = null;

				for (Museum m : Museum.values()) {
					if (m.name().equals(gs.getTitle())) {
						museum = m;
					}
				}

				if (museum == null) { return; }

				for (ArtifactGS ags : gs.getEntry()) {
					Artifact item = new Artifact(museum, ags);
					artifactList.add(item);
				}

				eventBus.fireEvent(new ArtifactReadyEvent());
			}

			@Override
			public void onError(Throwable exception) {
				// TODO Auto-generated method stub
			}
		});
	}

	public static HandlerRegistration addArtifactReady(ArtifactReadyHandler handler) {
		return eventBus.addHandler(ArtifactReadyEvent.TYPE, handler);
	}

	////////////////

	private static WSClient ws = new WSClient();
	private static boolean wsReady = false;
	private static ArrayList<Selection> selectionQueue = new ArrayList<>();

	public static void wantArtifactM(List<Artifact> list) {
		Selection selection = new Selection();
		selection.setMuseum(list.get(0).getMuseum());

		for (Artifact a : list) {
			selection.getUrlId().add(a.getUrlId());
		}

		if (wsReady) {
			ws.request(selection);
		} else {
			selectionQueue.add(selection);
		}
	}

	public static HandlerRegistration addArtifactMReady(ArtifactMReadyHandler handler) {
		return eventBus.addHandler(ArtifactMReadyEvent.TYPE, handler);
	}

	public static HandlerRegistration addWsReadyChange(WsReadyChangeHandler handler) {
		return eventBus.addHandler(WsReadyChangeEvent.TYPE, handler);
	}

	public static void wsConnect() {
		ws.connect();
	}

	static void changeWsReady(boolean ready) {
		if (wsReady == ready) { return; }

		wsReady = ready;
		eventBus.fireEvent(new WsReadyChangeEvent(ready));

		if (!wsReady) { return; }

		for (Selection s : selectionQueue) {
			ws.request(s);
		}
	}

	static void artifactMReady(List<ArtifactM> data) {
		eventBus.fireEvent(new ArtifactMReadyEvent(data));
	}

	static void wsError(ErrorEvent e) {
		if (e.byException) {
			UiCenter.wsOpenException();
			Console.error("Server URL Error : " + e.exception.getMessage());
		} else {
			UiCenter.wsError();
			Console.inspect(e.error);
		}

		changeWsReady(false);
	}
}
