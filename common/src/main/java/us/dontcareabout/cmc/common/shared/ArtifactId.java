package us.dontcareabout.cmc.common.shared;

public final class ArtifactId {
	private Museum museum;
	private String urlId;

	ArtifactId() {}	//for gwt-jackson

	public ArtifactId(Museum museum, String urlId) {
		this.museum = museum;
		this.urlId = urlId;
	}

	public Museum getMuseum() {
		return museum;
	}

	public void setMuseum(Museum museum) {
		this.museum = museum;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}


	@Override
	public String toString() {
		return museum.name() + "." + urlId;
	}

	// ======== gen by Eclipse ======== //
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((museum == null) ? 0 : museum.hashCode());
		result = prime * result + ((urlId == null) ? 0 : urlId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ArtifactId))
			return false;
		ArtifactId other = (ArtifactId) obj;
		if (museum != other.museum)
			return false;
		if (urlId == null) {
			if (other.urlId != null)
				return false;
		} else if (!urlId.equals(other.urlId))
			return false;
		return true;
	}
	// ================ //
}
