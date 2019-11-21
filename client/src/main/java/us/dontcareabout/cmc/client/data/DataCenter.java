package us.dontcareabout.cmc.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.cmc.client.data.ArtifactReadyEvent.ArtifactReadyHandler;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.gwt.client.google.Sheet;
import us.dontcareabout.gwt.client.google.SheetHappen;
import us.dontcareabout.gwt.client.google.SheetHappen.Callback;

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
}