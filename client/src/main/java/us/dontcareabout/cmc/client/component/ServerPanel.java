package us.dontcareabout.cmc.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;

import us.dontcareabout.cmc.client.Parameter;
import us.dontcareabout.cmc.client.Resources;
import us.dontcareabout.cmc.client.data.DataCenter;
import us.dontcareabout.cmc.client.data.WsReadyChangeEvent;
import us.dontcareabout.cmc.client.data.WsReadyChangeEvent.WsReadyChangeHandler;

public class ServerPanel extends Composite {
	private static ServerPanelUiBinder uiBinder = GWT.create(ServerPanelUiBinder.class);

	@UiField TextField server;
	@UiField Image state;

	public ServerPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		state.setResource(Resources.instance.ok());
		//不知道為什麼在 ui.xml 裡頭設定沒效果...... =.=
		state.setWidth("22px");
		state.setHeight("22px");
		server.setValue(Parameter.retrieve().getServer());

		DataCenter.addWsReadyChange(new WsReadyChangeHandler() {
			@Override
			public void onWsReadyChange(WsReadyChangeEvent event) {
				state.setVisible(event.ready);
			}
		});
	}

	@UiHandler("submitBtn")
	void selectSubmit(SelectEvent se) {
		Parameter setting = Parameter.retrieve();
		setting.setServer(server.getCurrentValue());
		Parameter.store(setting);
		DataCenter.wsConnect();
	}

	interface ServerPanelUiBinder extends UiBinder<Widget, ServerPanel> {}
}
