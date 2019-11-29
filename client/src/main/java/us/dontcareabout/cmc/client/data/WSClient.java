package us.dontcareabout.cmc.client.data;

import java.util.List;

import com.github.nmorel.gwtjackson.client.ObjectReader;
import com.github.nmorel.gwtjackson.client.ObjectWriter;
import com.google.gwt.core.client.GWT;

import us.dontcareabout.cmc.client.Parameter;
import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.Selection;
import us.dontcareabout.gwt.client.websocket.WebSocket;
import us.dontcareabout.gwt.client.websocket.event.MessageEvent;
import us.dontcareabout.gwt.client.websocket.event.MessageHandler;

public class WSClient {
	private static SelectionWriter writer = GWT.create(SelectionWriter.class);
	private static ArtifactMReader reader = GWT.create(ArtifactMReader.class);

	private final WebSocket ws = new WebSocket();

	public WSClient() {
		ws.addMessageHandler(new MessageHandler() {
			@Override
			public void onMessage(MessageEvent e) {
				DataCenter.artifactMReady(reader.read(e.getMessage()));
			}
		});
		connect();
	}

	public void connect() {
		String serverUrl = Parameter.retrieve().getServer().trim();

		if (serverUrl != null && serverUrl.startsWith("http://")) {
			ws.setUrl(serverUrl.replace("http://", "ws://") + "/spring/websocket");
		} else {
			ws.setUrl(serverUrl);
		}

		ws.open();
	}

	public void request(Selection selection) {
		ws.send(writer.write(selection));
	}

	interface SelectionWriter extends ObjectWriter<Selection> {}
	interface ArtifactMReader extends ObjectReader<List<ArtifactM>> {}
}
