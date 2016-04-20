package com.plr.hanzi.client.system;

import java.util.List;

public interface LeitnerBoxSaver {
	int getBoxId();
	void setBoxId(int boxId);
	
	List<Integer> getElements();
	void setElements(List<Integer> elements);
}
