package com.plr.hanzi.client.style;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface AppResources extends ClientBundle {
	public static final AppResources INSTANCE = GWT.create(AppResources.class);
	
	public static final  Logger logger = Logger.getLogger("ApplicationLogger");
	
	@Source("./flashcard.css")
	Style style();

	public interface Style extends CssResource {
		String tone1();

		String tone2();

		String tone3();

		String tone4();

		String tone5();

		String character();

		String pinyin();

		String customButton();
		
		String definitionPinyin();
		
		String actionButton();
	}
}
