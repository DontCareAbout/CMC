package us.dontcareabout.cmc.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.Viewport;

import us.dontcareabout.cmc.client.data.SheetId;
import us.dontcareabout.cmc.client.ui.event.RefreshSheetIdStoreEvent;
import us.dontcareabout.cmc.client.ui.event.RefreshSheetIdStoreEvent.RefreshSheetIdStoreHandler;
import us.dontcareabout.cmc.client.ui.event.SheetIdSelectEvent;
import us.dontcareabout.cmc.client.ui.event.SheetIdSelectEvent.SheetIdSelectHandler;

public class UiCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	public static void sheetIdSelect(SheetId sid) {
		eventBus.fireEvent(new SheetIdSelectEvent(sid));
	}

	public static HandlerRegistration addSheetIdSelect(SheetIdSelectHandler handler) {
		return eventBus.addHandler(SheetIdSelectEvent.TYPE, handler);
	}

	////////////////

	public static void refreshSheetIdStore() {
		eventBus.fireEvent(new RefreshSheetIdStoreEvent());
	}

	public static HandlerRegistration addRefreshSheetIdStore(RefreshSheetIdStoreHandler handler) {
		return eventBus.addHandler(RefreshSheetIdStoreEvent.TYPE, handler);
	}

	////////////////////////////////

	private final static MainView mainView = new MainView();

	public static void start() {
		viewport.add(mainView);
	}

	public static void toObjectView() {
		mainView.toObjectView();
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
