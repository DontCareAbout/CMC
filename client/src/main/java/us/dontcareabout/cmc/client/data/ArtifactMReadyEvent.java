package us.dontcareabout.cmc.client.data;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.cmc.client.data.ArtifactMReadyEvent.ArtifactMReadyHandler;
import us.dontcareabout.cmc.common.shared.ArtifactM;

public class ArtifactMReadyEvent extends GwtEvent<ArtifactMReadyHandler> {
	public static final Type<ArtifactMReadyHandler> TYPE = new Type<ArtifactMReadyHandler>();

	public final List<ArtifactM> data;

	public ArtifactMReadyEvent(List<ArtifactM> data) {
		this.data = data;
	}

	@Override
	public Type<ArtifactMReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ArtifactMReadyHandler handler) {
		handler.onArtifactMReady(this);
	}

	public interface ArtifactMReadyHandler extends EventHandler{
		public void onArtifactMReady(ArtifactMReadyEvent event);
	}
}
