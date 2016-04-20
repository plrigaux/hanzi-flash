package com.plr.hanzi.client.view.flashcard;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;

public class FlashCardPanel extends DockLayoutPanel {

	public FlashCardPanel(Unit unit) {
		super(unit);
		
		addNorth(new HTML("NOR"), 3);
		addSouth(new HTML("SOUTH"), 3);
		add(new HTML("CENTER"));
	}

}
