package us.dontcareabout.cmc.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.cmc.client.data.WsReadyChangeEvent.WsReadyChangeHandler;

public class WsReadyChangeEvent extends GwtEvent<WsReadyChangeHandler> {
	public static final Type<WsReadyChangeHandler> TYPE = new Type<WsReadyChangeHandler>();

	public final boolean ready;

	public WsReadyChangeEvent(boolean ready) {
		this.ready = ready;
	}

	@Override
	public Type<WsReadyChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(WsReadyChangeHandler handler) {
		handler.onWsReadyChange(this);
	}

	public interface WsReadyChangeHandler extends EventHandler{
		public void onWsReadyChange(WsReadyChangeEvent event);
	}
}
