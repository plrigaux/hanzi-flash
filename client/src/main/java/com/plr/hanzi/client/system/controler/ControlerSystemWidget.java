package com.plr.hanzi.client.system.controler;

import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.plr.hanzi.client.supermemo.RecordInfo;

public abstract class ControlerSystemWidget extends Composite {

	protected final List<RecordInfo> trainingList;

	protected final ControlerSystem controlerSystem;
	
	protected RecordInfo charInfo = null;

	protected ControlerSystemWidget(ControlerSystem controlerSystem) {
		this.controlerSystem = controlerSystem;
		
		trainingList = controlerSystem.getTrainingList();
	}

	protected void nextChar() {
		if (trainingList.isEmpty()) {
			Panel panel = (Panel) this.getParent();
			this.removeFromParent();
			controlerSystem.init();
			panel.add(controlerSystem);
		}
		
		charInfo = trainingList.remove(trainingList.size() - 1);
	}
}
