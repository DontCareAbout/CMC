package us.dontcareabout.cmc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.TabPanel;

public class MainView extends Composite {
	private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	@UiField TabPanel root;

	public MainView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void toObjectView() {
		//FIXME
	}

	interface MainViewUiBinder extends UiBinder<Widget, MainView> {}
}
