package us.dontcareabout.cmc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;

import us.dontcareabout.cmc.client.component.ArtifactGrid;
import us.dontcareabout.cmc.client.data.ArtifactMReadyEvent;
import us.dontcareabout.cmc.client.data.ArtifactMReadyEvent.ArtifactMReadyHandler;
import us.dontcareabout.cmc.client.data.ArtifactReadyEvent;
import us.dontcareabout.cmc.client.data.ArtifactReadyEvent.ArtifactReadyHandler;
import us.dontcareabout.cmc.client.data.DataCenter;

public class ArtifactView extends Composite {
	private static ArtifactViewUiBinder uiBinder = GWT.create(ArtifactViewUiBinder.class);

	@UiField ArtifactGrid grid;

	public ArtifactView() {
		initWidget(uiBinder.createAndBindUi(this));
		DataCenter.addArtifactReady(new ArtifactReadyHandler() {
			@Override
			public void onArtifactReady(ArtifactReadyEvent event) {
				grid.refresh(DataCenter.getArtifact());
			}
		});
		DataCenter.addArtifactMReady(new ArtifactMReadyHandler() {
			@Override
			public void onArtifactMReady(ArtifactMReadyEvent event) {
				grid.injectArtifactM(event.data);
			}
		});
	}

	interface ArtifactViewUiBinder extends UiBinder<Widget, ArtifactView> {}
}
