package us.dontcareabout.cmc.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.nmorel.gwtjackson.client.ObjectReader;
import com.github.nmorel.gwtjackson.client.ObjectWriter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.cmc.client.data.ArtifactMReadyEvent.ArtifactMReadyHandler;
import us.dontcareabout.cmc.client.data.ArtifactReadyEvent.ArtifactReadyHandler;
import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.common.shared.Selection;
import us.dontcareabout.gwt.client.google.Sheet;
import us.dontcareabout.gwt.client.google.SheetHappen;
import us.dontcareabout.gwt.client.google.SheetHappen.Callback;
import us.dontcareabout.gwt.client.websocket.WebSocket;
import us.dontcareabout.gwt.client.websocket.event.MessageEvent;
import us.dontcareabout.gwt.client.websocket.event.MessageHandler;

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

	private static final String SERVER = "localhost:8080/server/";	//FIXME 改讀 local setting
	private static final WebSocket ws = new WebSocket("ws://" + SERVER + "spring/websocket");

	static interface SelectionWriter extends ObjectWriter<Selection> {}
	static interface ArtifactMReader extends ObjectReader<List<ArtifactM>> {}

	private static SelectionWriter writer = GWT.create(SelectionWriter.class);
	private static ArtifactMReader reader = GWT.create(ArtifactMReader.class);

	static {
		ws.addMessageHandler(new MessageHandler() {
			@Override
			public void onMessage(MessageEvent e) {
				eventBus.fireEvent(new ArtifactMReadyEvent(reader.read(e.getMessage())));
			}
		});
		ws.open();
	}

	public static void wantArtifactM(List<Artifact> list) {
		Selection selection = new Selection();
		selection.setMuseum(list.get(0).getMuseum());

		for (Artifact a : list) {
			selection.getUrlId().add(a.getUrlId());
		}

		ws.send(writer.write(selection));
	}

	public static HandlerRegistration addArtifactMReady(ArtifactMReadyHandler handler) {
		return eventBus.addHandler(ArtifactMReadyEvent.TYPE, handler);
	}
}
