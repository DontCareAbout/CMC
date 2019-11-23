package us.dontcareabout.cmc.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

//XXX from RQC
public interface Resources extends ClientBundle {
	Resources instance = GWT.create(Resources.class);

	@Source("GitHub.png")
	ImageResource github();

	@Source("ok.png")
	ImageResource ok();

	//FIXME 改用 XTemplates？
	@Source("manualPurchase.js")
	TextResource manualPurchaseJS();
}
