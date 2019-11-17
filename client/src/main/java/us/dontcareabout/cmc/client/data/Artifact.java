package us.dontcareabout.cmc.client.data;

import java.util.ArrayList;

import us.dontcareabout.cmc.common.shared.ArtifactM;
import us.dontcareabout.cmc.common.shared.Museum;

/**
 * 結合 {@link ArtifactGS} 與 {@link ArtifactM} 的完整 artifact
 */
public class Artifact {
	public final Museum museum;
	private final ArtifactGS fromGS;

	//對 client 來說 ArtifactM 不必然有
	//為了省去 delegate method 一堆判斷 null 的 code，所以預設塞一個空殼
	private  ArtifactM fromM = new ArtifactM();

	public Artifact(Museum museum, ArtifactGS gs) {
		this.museum = museum;
		this.fromGS = gs;
	}

	public Museum getMuseum() {
		return museum;
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
		return fromM.getName();
	}

	public String getEra() {
		return fromM.getEra();
	}

	public String getOrigin() {
		return fromM.getOrigin();
	}

	public String getMaterial() {
		return fromM.getMaterial();
	}

	public String getDescription() {
		return fromM.getDescription();
	}

	public String getDimensions() {
		return fromM.getDimensions();
	}

	public ArrayList<String> getImage() {
		return fromM.getImage();
	}
	////
}
