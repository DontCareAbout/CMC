package us.dontcareabout.cmc.client.ui;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.info.Info;

import us.dontcareabout.cmc.client.data.DataCenter;
import us.dontcareabout.gst.client.GstEventCenter;
import us.dontcareabout.gst.client.event.ReloadSheetEvent;
import us.dontcareabout.gst.client.event.ReloadSheetEvent.ReloadSheetHandler;

public class UiCenter {
	public static void wsError() {
		Info.display("連線發生錯誤", "請檢查設定值以及 Server 狀態並重新連線");
	}

	public static void wsOpenException() {
		//目前也只知道這個發生可能，所以畫面上就寫死提示訊息
		Info.display("連線失敗", "Server URL 格式有誤");
	}

	////////////////////////////////

	static {
		GstEventCenter.addReloadSheet(new ReloadSheetHandler() {
			@Override
			public void onReloadSheet(ReloadSheetEvent event) {
				toObjectView();
				DataCenter.wantArtifact(event.sheetId);
			}
		});
	}

	private final static MainView mainView = new MainView();

	public static void start() {
		viewport.add(mainView);
	}

	public static void toObjectView() {
		mainView.toArtifactView();
	}

	////////////////////////////////

	private final static Viewport viewport = new Viewport();
	static {
		RootPanel.get().add(viewport);
	}

	public static void mask(String message) {
		viewport.mask(message);
	}

	public static void unmask() {
		viewport.unmask();
	}

	private final static Window dialog = new Window();
	static {
		dialog.setModal(true);
		dialog.setResizable(false);
	}

	public static void closeDialog() {
		dialog.hide();
	}

	private static void dialog(Widget widget, int width, int height) {
		dialog.clear();
		dialog.add(widget);
		dialog.show();
		dialog.setPixelSize(width, height);
		dialog.center();
	}
}
