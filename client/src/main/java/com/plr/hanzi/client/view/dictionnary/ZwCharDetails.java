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
package com.plr.hanzi.client.view.dictionnary;

import java.util.List;
import java.util.logging.Level;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;
import com.plr.hanzi.client.CardData;
import com.plr.hanzi.client.DataControler;
import com.plr.hanzi.client.ZhongWenCharacter;
import com.plr.hanzi.client.style.AppResources;
import com.plr.hanzi.client.view.definition.DefinitionPanel;

/**
 * A form used for editing contacts.
 */
public class ZwCharDetails extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, ZwCharDetails> {
	}

	@UiField
	Label idLabel;
	@UiField
	Label characterLabel;
	@UiField
	DefinitionPanel definitionPanel;

	// private ZhongWenCharacter contactInfo;

	private CellList<ZhongWenCharacter> cellList;

	public ZwCharDetails() {
		initWidget(uiBinder.createAndBindUi(this));

		// Add the categories to the category box.

		// Initialize the contact to null.
		setCharater(null);

		characterLabel.addStyleName(AppResources.INSTANCE.style().character());

		System.out.println(AppResources.INSTANCE.style().character());

		CharacterCell contactCell = new CharacterCell();

		cellList = new CellList<ZhongWenCharacter>(contactCell, CardData.KEY_PROVIDER) {

			@Override
			protected void renderRowValues(SafeHtmlBuilder sb, List<ZhongWenCharacter> values, int start,
					SelectionModel<? super ZhongWenCharacter> selectionModel) {
				setChar(values);
				// shuffle(values);
			}
		};

		cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);

		DataControler.get().addDataDisplay(cellList);
	}

	private void setChar(List<ZhongWenCharacter> values) {
		if (values.isEmpty()) {
			return;
		}
		System.out.println("setChar " + values);
		ZhongWenCharacter zwChar = values.get(0);
		setCharater(zwChar);

	}

	private static class CharacterCell extends AbstractCell<ZhongWenCharacter> {

		@Override
		public void render(Context context, ZhongWenCharacter value, SafeHtmlBuilder sb) {
		}
	}

	public void setCharater(ZhongWenCharacter zwChar) {
		// this.contactInfo = contact;
		// updateButton.setEnabled(contact != null);
		if (zwChar != null) {
			idLabel.setText("" + zwChar.getId());
			characterLabel.setText(zwChar.getSimplifiedCharacter());

			definitionPanel.setCharater(zwChar);

			AppResources.logger.log(Level.INFO, "Rank " + zwChar.getId() + " char: " + zwChar.getSimplifiedCharacter());
		}
	}

	public void setCharaterId(String charId) {

		int id = Integer.valueOf(charId);

		System.out.println("id: " + id);
		cellList.setVisibleRange(id - 1, 1);
	}
}
