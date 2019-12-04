package us.dontcareabout.cmc.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;

import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.common.shared.Selection;
import us.dontcareabout.cmc.server.museum.Service;
import us.dontcareabout.cmc.server.museum.exception.ArtifactNotExistException;
import us.dontcareabout.cmc.server.museum.exception.MuseumNotReadyException;

/**
 * 目前所有的 input 都是
 *
 */
@EnableWebSocket
public class WebSocketServer extends TextWebSocketHandler implements WebSocketConfigurer {
	private final Gson gson = new Gson();

	/**
	 * 紀錄目前有哪些 artifact 的資料還沒抓回來，
	 * 以避免在還沒抓回來之前，使用者不斷重複發出 request。
	 * XXX 這是小規模的便宜行事實作，有 OOM 的風險...... XD
	 */
	private final HashSet<Argument> purchaseSet = new HashSet<>();

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(this, "/websocket").setAllowedOrigins("*");
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String context = message.getPayload();
		Selection selection = gson.fromJson(context, Selection.class);
		ArrayList<ArtifactM> result = new ArrayList<>();

		for (String urlId : selection.getUrlId()) {
			Argument argument = new Argument(selection.getMuseum(), urlId);
			try {
				result.add(Service.collection.obtain(selection.getMuseum(), urlId));

				//因為 artifact 已經有了，所以檢查是不是存在 purchaseSet 當中
				//以避免 purchaseSet 無限制膨脹
				if (purchaseSet.contains(argument)) {
					purchaseSet.remove(argument);
				}
			} catch (ArtifactNotExistException e) {
				//找不到的文物就要買(X) 抓(O) 回來
				if (purchaseSet.contains(argument)) { continue; }

				Service.collection.purchase(selection.getMuseum(), urlId);
				purchaseSet.add(argument);
			} catch (MuseumNotReadyException e) {
				//理論上不會發生，不過還是噴個訊息
				System.out.println(selection.getMuseum() + " is not ready"); //TODO 改 log 機制
			} catch (IOException e) {
				//JSOUP 讀取檔案失敗，目前不（知道怎麼）處理
			}
		}

		send(session, gson.toJson(result));
	}

	private void send(WebSocketSession session, String message) throws IOException {
		session.sendMessage(new TextMessage(message));
	}

	//Refactory 考慮變成 common VO？
	private class Argument {
		final Museum museum;
		final String urlId;

		Argument(Museum museum, String urlId) {
			this.museum = museum;
			this.urlId = urlId;
		}

		// ======== gen by Eclipse ======== //
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((museum == null) ? 0 : museum.hashCode());
			result = prime * result + ((urlId == null) ? 0 : urlId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Argument other = (Argument) obj;
			if (museum != other.museum)
				return false;
			if (urlId == null) {
				if (other.urlId != null)
					return false;
			} else if (!urlId.equals(other.urlId))
				return false;
			return true;
		}
		// ================ //
	}
}