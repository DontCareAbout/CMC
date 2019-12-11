package us.dontcareabout.cmc.common.shared;

public enum Museum {
	Met(
		"Met", "https://www.metmuseum.org/art/collection/search/",
		"https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/The_Metropolitan_Museum_of_Art_Logo.svg/512px-The_Metropolitan_Museum_of_Art_Logo.svg.png"
	),
	RoyalArmouries(
		"Royal Armouries", "https://collections.royalarmouries.org/object/rac-object-",
		"https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Royal_Armouries_Logo.svg/330px-Royal_Armouries_Logo.svg.png"
	),
	;

	public final String title;
	public final String url;

	//因為 ArtifactGrid 顯示的緣故，要找寬度大於等於高度的圖
	public final String logo;

	private Museum(String title, String url, String logo) {
		this.title = title;
		this.url = url;
		this.logo = logo;
	}
}
