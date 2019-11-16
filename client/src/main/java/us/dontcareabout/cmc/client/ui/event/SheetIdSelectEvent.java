package us.dontcareabout.cmc.client.ui.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.cmc.client.data.SheetId;
import us.dontcareabout.cmc.client.ui.event.SheetIdSelectEvent.SheetIdSelectHandler;

//XXX from RQC
public class SheetIdSelectEvent extends GwtEvent<SheetIdSelectHandler> {
	public static final Type<SheetIdSelectHandler> TYPE = new Type<SheetIdSelectHandler>();
	public final SheetId data;

	public SheetIdSelectEvent(SheetId sid) {
		this.data = sid;
	}

	@Override
	public Type<SheetIdSelectHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SheetIdSelectHandler handler) {
		handler.onSheetIdSelect(this);
	}

	public interface SheetIdSelectHandler extends EventHandler{
		public void onSheetIdSelect(SheetIdSelectEvent event);
	}
}
