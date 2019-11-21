package us.dontcareabout.cmc.common.shared;

import java.util.ArrayList;

public class Selection {
	private Museum museum;
	private ArrayList<String> urlId = new ArrayList<>();

	public Museum getMuseum() {
		return museum;
	}
	public void setMuseum(Museum museum) {
		this.museum = museum;
	}
	@Deprecated	//為了配合 gwt-jackson 而加的，不符合 coding 習慣所以掛 deprecated
	public void setUrlId(ArrayList<String> urlId) {
		this.urlId = urlId;
	}
	public ArrayList<String> getUrlId() {
		return urlId;
	}
}
