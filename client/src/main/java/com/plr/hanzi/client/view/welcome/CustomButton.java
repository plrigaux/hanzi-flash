package com.plr.hanzi.client.view.welcome;

import com.google.gwt.user.client.ui.Button;
import com.plr.hanzi.client.style.AppResources;

public class CustomButton extends Button {
	

	public CustomButton() {
		addStyleName(AppResources.INSTANCE.style().customButton());
	}

}
