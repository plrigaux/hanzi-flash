package com.plr.hanzi.client.system;

import java.util.List;


public interface LeitnerSaver {
	int getMaxId();
	void setMaxId(int maxId);
	
	List<LeitnerBoxSaver> getLeitnerBoxes();
	void setLeitnerBoxes(List<LeitnerBoxSaver> leitnerBoxes);
}
