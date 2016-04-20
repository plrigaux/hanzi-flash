package com.plr.hanzi.client;

import com.plr.hanzi.client.CardData.CharDefinition;

public interface ZhongWenCharacter {
	public int getId();

	public String getSimplifiedCharacter();

	public int definitionCount();

	public CharDefinition getDefinition(int j);

}
