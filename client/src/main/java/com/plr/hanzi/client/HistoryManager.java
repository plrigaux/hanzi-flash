package com.plr.hanzi.client;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.plr.hanzi.client.view.dictionnary.ZwCharBrowser;
import com.plr.hanzi.client.view.dictionnary.ZwCharDetails;
import com.plr.hanzi.client.view.flashcard.FlashCardSystem;
import com.plr.hanzi.client.view.shishenme.ShiShenmeSystem;
import com.plr.hanzi.client.view.welcome.Welcome;

public class HistoryManager implements ValueChangeHandler<String>, ApplicationConst {

	private final RootLayoutPanel rootPanel;

	public HistoryManager() {

		rootPanel = RootLayoutPanel.get();

		History.addValueChangeHandler(this);

		// When you reload, stay on the same page
		String hash = Window.Location.getHash();

		History.newItem("");

		if (hash.startsWith("#")) {
			hash = hash.substring(1);
		}

		History.newItem(hash);
	}

	static private final Splitter splitter = Splitter.on('/');

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {

		String value = event.getValue();

		List<String> l = new ArrayList<String>();
		for (String s : splitter.split(value)) {
			l.add(s);
		}

		if (!l.isEmpty() && FLASH.equals(l.get(0))) {

			if (l.size() > 1 && STATS.equals(l.get(1))) {
				rootPanel.clear();
				FlashCardSystem mc = new FlashCardSystem();
				rootPanel.add(mc.getStatTable());

				return;
			}

			rootPanel.clear();
			FlashCardSystem mc = new FlashCardSystem();
			rootPanel.add(mc);

		} else if (SHI_SHENME.equals(value)) {
			rootPanel.clear();
			ShiShenmeSystem charDictionnary = new ShiShenmeSystem();
			rootPanel.add(charDictionnary);
		} else if (value.startsWith(CHARARCTER)) {

			if (value.charAt(CHARARCTER.length()) == '/') {
				String charId = value.substring(CHARARCTER.length() + 1);

				rootPanel.clear();
				ZwCharDetails charDetails = new ZwCharDetails();
				rootPanel.add(charDetails);

				charDetails.setCharaterId(charId);
			}
		} else if (value.equals(DICTIONNARY)) {
			rootPanel.clear();
			ZwCharBrowser charDictionnary = new ZwCharBrowser();
			rootPanel.add(charDictionnary);
		} else {
			History.newItem("", false);
			rootPanel.clear();
			Welcome welcome = new Welcome();
			rootPanel.add(welcome);
		}
	}
}
