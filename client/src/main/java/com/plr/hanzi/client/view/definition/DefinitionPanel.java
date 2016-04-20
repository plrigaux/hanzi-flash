/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.plr.hanzi.client.view.definition;

import com.google.common.base.Splitter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.plr.hanzi.client.PinyinConverter;
import com.plr.hanzi.client.Tone;
import com.plr.hanzi.client.ZhongWenCharacter;
import com.plr.hanzi.client.CardData.CharDefinition;
import com.plr.hanzi.client.style.AppResources;

/**
 * A form used for editing contacts.
 */
public class DefinitionPanel extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, DefinitionPanel> {
	}

	final private static Splitter wsSplitter = Splitter.on(' ');

	@UiField
	SimplePanel simplePanel;

	// private ZhongWenCharacter contactInfo;

	public DefinitionPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		// Add the categories to the category box.

		// Initialize the contact to null.
		setCharater(null);

	}

	static private RegExp mesureWordRegExp = RegExp.compile("(.+?)\\[(\\w+)\\],*", "g");

	static private RegExp pinyinRegExp = RegExp.compile("\\[(.+?)\\]", "g");

	public void setCharater(ZhongWenCharacter zwChar) {

		if (zwChar != null) {

			FlexTable definitionTable = new FlexTable();
			int row = 0;
			for (int j = 0; j < zwChar.definitionCount(); j++) {
				CharDefinition charDefinition = zwChar.getDefinition(j);

				Label lp = new Label(charDefinition.getPinyin());
				lp.addStyleName(AppResources.INSTANCE.style().pinyin());

				int tone = charDefinition.getTone();

				String toneStyle = Tone.getTone(tone).getCssClass();

				lp.addStyleName(toneStyle);

				definitionTable.setWidget(row, 0, lp);

				for (int k = 0; k < charDefinition.getDefinition().length(); k++) {
					String definition = charDefinition.getDefinition().get(k);

					definition = printDefinition(definition);

					definitionTable.setHTML(row++, 1, definition);
				}

				simplePanel.clear();
				simplePanel.add(definitionTable);
			}
		}
	}

	private String printDefinition(String definition) {
		if (definition.startsWith("CL:")) {

			String out = "<i>Mesure word: </i>";

			boolean first = true;
			for (MatchResult result = mesureWordRegExp.exec(definition); result != null; result = mesureWordRegExp.exec(definition)) {

				String character = result.getGroup(1);
				character = character.substring(character.length() - 1);

				if (first) {
					first = false;
				} else {
					out += ", ";
				}

				String pinyinNum = result.getGroup(2);

				int tone1 = CharDefinition.getTone(pinyinNum);

				String toneStyle1 = Tone.getTone(tone1).getCssClass();

				out += character + " <span class='" + toneStyle1 + " " + AppResources.INSTANCE.style().definitionPinyin() + "'>" + PinyinConverter.getConvert(pinyinNum)
						+ "</span>";
			}

			definition = out;

		} else {
			boolean first = true;
			String out = "";

			int begin = 0;
			int end = 0;
			for (MatchResult result = pinyinRegExp.exec(definition); result != null; result = pinyinRegExp.exec(definition)) {

				if (first) {
					first = false;
				}

				String pinyinNums = result.getGroup(1);

				end = result.getIndex();

				out += definition.substring(begin, end);

				for (String pinyinNum : wsSplitter.split(pinyinNums)) {
					int tone1 = CharDefinition.getTone(pinyinNum);
					String toneStyle = Tone.getTone(tone1).getCssClass();
					out += " <span class='" + toneStyle + " " + AppResources.INSTANCE.style().definitionPinyin() + 
					"'>" + PinyinConverter.getConvert(pinyinNum) + "</span>";
				}

				begin = end + result.getGroup(0).length();
			}

			if (!first) {
				out += definition.substring(begin);
				definition = out;
			} else {

			}
		}
		return definition;
	}

	public void clear() {
		simplePanel.clear();

	}
}
