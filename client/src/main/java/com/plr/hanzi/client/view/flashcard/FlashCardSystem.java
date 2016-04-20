package com.plr.hanzi.client.view.flashcard;

import com.google.gwt.user.client.ui.Widget;
import com.plr.hanzi.client.system.controler.ControlerSystem;

public class FlashCardSystem extends ControlerSystem {

	@Override
	public Widget getWidget() {
		return new FlashCard(this);
	}

	@Override
	protected String getSaverKey() {
		return "chinese.character.trainer.flashcard.supermemo";
	}

	
	
}
