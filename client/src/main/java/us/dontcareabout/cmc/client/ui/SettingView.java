package us.dontcareabout.cmc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;

//XXX from RQC
public class SettingView extends Composite {
	private static SettingViewUiBinder uiBinder = GWT.create(SettingViewUiBinder.class);

	public SettingView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	interface SettingViewUiBinder extends UiBinder<Widget, SettingView> {}
}
