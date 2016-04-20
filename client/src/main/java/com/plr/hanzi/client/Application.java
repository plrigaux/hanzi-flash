package com.plr.hanzi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.plr.hanzi.client.i18n.HanziMessages;
import com.plr.hanzi.client.style.AppResources;
import com.plr.hanzi.client.view.welcome.Welcome;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Application implements EntryPoint {

	@SuppressWarnings("unused")
	private HistoryManager historyManager = null;
	
	@Override
	public void onModuleLoad() {
		//init the pinyin converter
		PinyinConverter.getPinyinConverter();
		
		AppResources.INSTANCE.style().ensureInjected();
		GWT.create(HanziMessages.class);
		
		RootLayoutPanel rootPanel = RootLayoutPanel.get();
		
		rootPanel.clear();
		Welcome welcome = new Welcome();
		rootPanel.add(welcome);
		
		historyManager = new HistoryManager();
			
		Cookies.setCookie("local", "fr");
	}

}
