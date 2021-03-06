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

import us.dontcareabout.cmc.common.shared.ArtifactId;
import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.Selection;
import us.dontcareabout.cmc.common.shared.exception.MuseumNotFoundException;
import us.dontcareabout.cmc.server.museum.Service;
import us.dontcareabout.cmc.server.museum.exception.ArtifactNotExistException;

@EnableWebSocket
public class WebSocketServer extends TextWebSocketHandler implements WebSocketConfigurer {
	private final Gson gson = new Gson();

	/**
	 * 紀錄目前有哪些 artifact 的資料還沒抓回來，
	 * 以避免在還沒抓回來之前，使用者不斷重複發出 request。
	 * XXX 這是小規模的便宜行事實作，有 OOM 的風險...... XD
	 */
	private final HashSet<ArtifactId> purchaseSet = new HashSet<>();

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
			ArtifactId aid = new ArtifactId(selection.getMuseum(), urlId);
			try {
				result.add(Service.collection.obtain(aid));

				//因為 artifact 已經有了，所以檢查是不是存在 purchaseSet 當中
				//以避免 purchaseSet 無限制膨脹
				if (purchaseSet.contains(aid)) {
					purchaseSet.remove(aid);
				}
			} catch (ArtifactNotExistException e) {
				//找不到的文物就要買(X) 抓(O) 回來
				if (purchaseSet.contains(aid)) { continue; }

				Service.collection.purchase(aid);
				purchaseSet.add(aid);
			} catch (MuseumNotFoundException e) {
				//理論上不會發生，不過還是噴個訊息
				System.out.println(selection.getMuseum() + " is not ready"); //TODO 改 log 機制
			} catch (IOException e) {
				//JSOUP 讀取檔案失敗，目前不（知道怎麼）處理
				System.out.println(aid + " throw IOException");
			} catch (Exception e) {
				//理論上是 parse 過程發生意料外的狀況
				//所以砍掉重抓一次，就不去動 purchaseSet 了
				Service.collection.remove(aid);
				Service.collection.purchase(aid);

				System.out.println("!!!! " + aid + " !!!!");
				e.printStackTrace();	//TODO 改 log 機制

				//XXX 目前遇到 Chrome Agent 在頁面還沒 ready 就儲存檔案並結束的狀況
				//主要是發生在 Royal Armouries 上..... ＝＝"
				//目前還無解，只能靠 Bookmarklet 解決？
			}
		}

		send(session, gson.toJson(result));
	}

	private void send(WebSocketSession session, String message) throws IOException {
		session.sendMessage(new TextMessage(message));
	}
}