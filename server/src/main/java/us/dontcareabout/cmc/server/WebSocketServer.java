package us.dontcareabout.cmc.server;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;

import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.Selection;
import us.dontcareabout.cmc.server.museum.Service;

/**
 * 目前所有的 input 都是
 *
 */
@EnableWebSocket
public class WebSocketServer extends TextWebSocketHandler implements WebSocketConfigurer {
	private final Gson gson = new Gson();

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(this, "/websocket").setAllowedOrigins("*");
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String context = message.getPayload();
		System.out.println(context);
		Selection selection = gson.fromJson(context, Selection.class);
		ArrayList<ArtifactM> result = new ArrayList<>();

		for (String urlId : selection.getUrlId()) {
			//TODO 缺乏 purchase 機制
			try {
				result.add(Service.collection.obtain(selection.getMuseum(), urlId));
			} catch (Exception e) {
				e.printStackTrace();
			};
		}

		send(session, gson.toJson(result));
	}

	private void send(WebSocketSession session, String message) throws IOException {
		session.sendMessage(new TextMessage(message));
	}
}