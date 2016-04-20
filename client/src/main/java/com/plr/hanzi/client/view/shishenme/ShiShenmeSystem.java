package com.plr.hanzi.client.view.shishenme;

import com.google.gwt.user.client.ui.Widget;
import com.plr.hanzi.client.system.controler.ControlerSystem;

public class ShiShenmeSystem extends ControlerSystem {

	@Override
	public Widget getWidget() {
		return new ShiShenme(this);
	}

	@Override
	protected String getSaverKey() {
		return "chinese.character.trainer.shishenme.leitner";
	}
}
