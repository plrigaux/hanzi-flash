package com.plr.hanzi.client.system;

public class CharInfo {

	private final BoxLevel boxLevel;
	private final int charId;

	public CharInfo(BoxLevel boxLevel, int charId) {
		super();
		this.boxLevel = boxLevel;
		this.charId = charId;
	}

	public BoxLevel getBoxLevel() {
		return boxLevel;
	}

	public int getCharId() {
		return charId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + charId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharInfo other = (CharInfo) obj;
		if (charId != other.charId)
			return false;
		return true;
	}
	
	
}
