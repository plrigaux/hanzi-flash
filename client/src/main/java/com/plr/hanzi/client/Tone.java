package com.plr.hanzi.client;

import com.plr.hanzi.client.style.AppResources;

public enum Tone {

	TONE1('\u0304', AppResources.INSTANCE.style().tone1()), TONE2('\u0301', AppResources.INSTANCE.style().tone2()), TONE3(
			'\u030C', AppResources.INSTANCE.style().tone3()), TONE4('\u0300', AppResources.INSTANCE.style().tone4()), TONE5(null,
			AppResources.INSTANCE.style().tone5());

	public Character getToneChar() {
		return toneChar;
	}

	public String getCssClass() {
		return cssClass;
	}

	private final Character toneChar;

	private final String cssClass;

	private Tone(Character toneChar, String cssClass) {
		this.toneChar = toneChar;
		this.cssClass = cssClass;
	}

	public static Tone getTone(int tone) {
		try {
			return Tone.values()[tone - 1];
		} catch (Exception e) {
			return TONE5;
		}
	}
}
