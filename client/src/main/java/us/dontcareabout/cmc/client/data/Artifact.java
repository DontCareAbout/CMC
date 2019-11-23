package us.dontcareabout.cmc.client.data;

import java.util.ArrayList;

import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.ImageUrl;
import us.dontcareabout.cmc.common.shared.Museum;
import us.dontcareabout.cmc.common.shared.MuseumUtil;

/**
 * 結合 {@link ArtifactGS} 與 {@link ArtifactM} 的完整 artifact
 */
public class Artifact {
	public final Museum museum;
	private final ArtifactGS fromGS;

	//原本預設會塞一個空的 instance
	//但是使用者希望知道該筆 artifact 是不是有抓到資料 or 博物館本來就沒有提供資料
	//為了不用相對奇怪的招數（例如用 name 判斷 XD），所以還是改用 null 作為預設值
	private  ArtifactM fromM;

	public Artifact(Museum museum, ArtifactGS gs) {
		this.museum = museum;
		this.fromGS = gs;
	}

	public Museum getMuseum() {
		return museum;
	}

	public String getUrlId() {
		return MuseumUtil.parseUrlId(museum, getUrl());
	}

	public boolean isReady() {
		return fromM != null;
	}

	//Google Sheet
	public String getUrl() {
		return fromGS.getUrl();
	}

	public final Integer getIndex() {
		return fromGS.getIndex();
	}

	public String getNote() {
		return fromGS.getNote();
	}

	public Double getScore() {
		return fromGS.getScore();
	}

	public ArrayList<String> getTag() {
		return fromGS.getTag();
	}
	////

	//Museum
	public void setFromMuseum(ArtifactM fromM) {
		this.fromM = fromM;
	}

	public String getName() {
		return isReady() ? fromM.getName() : "";
	}

	public String getEra() {
		return isReady() ? fromM.getEra() : "";
	}

	public String getOrigin() {
		return isReady() ? fromM.getOrigin() : "";
	}

	public String getMaterial() {
		return isReady() ? fromM.getMaterial() : "";
	}

	public String getDescription() {
		return isReady() ? fromM.getDescription() : "";
	}

	public String getDimensions() {
		return isReady() ? fromM.getDimensions() : "";
	}

	public ArrayList<ImageUrl> getImage() {
		return isReady() ? fromM.getImage() : new ArrayList<ImageUrl>();
	}
	////
}
