package us.dontcareabout.cmc.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import us.dontcareabout.cmc.common.shared.ArtifactId;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.common.shared.MuseumUtil;
import us.dontcareabout.cmc.server.museum.Service;

/**
 * 手動儲存數位典藏網頁的 HTTP 窗口。
 * 接受兩個 parameter：
 * <ul>
 * 	<li>url：數位典藏的文物網址。如果在 {@link Museum#url} 找不到對應，會送出 HTTP status 406</li>
 * 	<li>
 * 		html：html tag 內的網頁內容。亦即：會用 html tag 包住傳入值再進行處理。
 * 	</li>
 * </ul>
 */
@WebServlet(name="ManulaPurchase", urlPatterns={"/ManualPurchase"})
@MultipartConfig
public class ManualPurchaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");

		String url = request.getParameter("url");
		String html = request.getParameter("html");

		if (url == null || html == null) { response.sendError(406); }

		for (Museum museum : Museum.values()) {
			if (url.startsWith(museum.url)) {
				Service.collection.store(
					//Refactory
					new ArtifactId(museum, MuseumUtil.parseUrlId(museum, url)),
					//目前是設計 bookmarklet 來簡化操作
					//但是懶得在 JS 作太細緻的處理，所以就偷懶了..... XD
					"<html>" + html + "</html>"
				);
				return;
			}
		}

		response.sendError(406);
	}
}
