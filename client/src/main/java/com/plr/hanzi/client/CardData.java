package com.plr.hanzi.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.view.client.ProvidesKey;

public final class CardData extends JavaScriptObject implements ZhongWenCharacter {

	protected CardData() {

	}

	/**
	 * The key provider that provides the unique ID of a contact.
	 */
	public static final ProvidesKey<ZhongWenCharacter> KEY_PROVIDER = new ProvidesKey<ZhongWenCharacter>() {
		@Override
		public Object getKey(ZhongWenCharacter item) {
			return item == null ? null : item.getId();
		}
	};

	@Override
	public final native int getId() /*-{
									return this.i;
									}-*/;

	@Override
	public final native String getSimplifiedCharacter() /*-{
														return this.s;
														}-*/;

	public final native JsArray<CharDefinition> getDefinitions() /*-{
																	return this.d;
																	}-*/;

	static public class CharDefinition extends JavaScriptObject {
		protected CharDefinition() {

		}

		public final String getPinyin() {
			return PinyinConverter.getConvert(getPinyinNum());
		}

		public final native JsArrayString getDefinition() /*-{
															return this.d;
															}-*/;

		public final native String getPinyinNum() /*-{
													return this.n;
													}-*/;

		public final int getTone() {
			return getTone(getPinyinNum());

		}

		public static final int getTone(String pinyinNum) {

			char c = pinyinNum.charAt(pinyinNum.length() - 1);

			switch (c) {
			case '1':
				return 1;
			case '2':
				return 2;
			case '3':
				return 3;
			case '4':
				return 4;
			}

			return 5;
		}
	}

	@Override
	public final int definitionCount() {
		return getDefinitions().length();
	}

	@Override
	public final CharDefinition getDefinition(int j) {
		return getDefinitions().get(j);
	}
}
