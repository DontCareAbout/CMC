package us.dontcareabout.cmc.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.cmc.client.data.ArtifactReadyEvent.ArtifactReadyHandler;

public class ArtifactReadyEvent extends GwtEvent<ArtifactReadyHandler> {
	public static final Type<ArtifactReadyHandler> TYPE = new Type<ArtifactReadyHandler>();

	@Override
	public Type<ArtifactReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ArtifactReadyHandler handler) {
		handler.onArtifactReady(this);
	}

	public interface ArtifactReadyHandler extends EventHandler{
		public void onArtifactReady(ArtifactReadyEvent event);
	}
}
