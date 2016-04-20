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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.plr.hanzi.client.ApplicationConst;
import com.plr.hanzi.client.CardData;
import com.plr.hanzi.client.DataControler;
import com.plr.hanzi.client.ZhongWenCharacter;

/**
 * Display all the characters
 */
public class ZwCharBrowser extends Composite {

	interface Binder extends UiBinder<Widget, ZwCharBrowser> {
	}

	private static Binder uiBinder = GWT.create(Binder.class);

	/**
	 * The pager used to display the current range.
	 */
	@UiField(provided = true)
	SimplePager pager;

	/**
	 * The CellList.
	 */
	@UiField(provided = true)
	CellTable<ZhongWenCharacter> cellTable;

	private static Range lastAccessedRange = null;
	
	public ZwCharBrowser() {

		cellTable = new CellTable<ZhongWenCharacter>(CardData.KEY_PROVIDER);

		// Create rank column.
		TextColumn<ZhongWenCharacter> rankColumn = new TextColumn<ZhongWenCharacter>() {
			@Override
			public String getValue(ZhongWenCharacter zwChar) {
				return "" + zwChar.getId();
			}
		};

		TextColumn<ZhongWenCharacter> zhCharColumn = new TextColumn<ZhongWenCharacter>() {
			@Override
			public String getValue(ZhongWenCharacter zwChar) {
				return zwChar.getSimplifiedCharacter();
			}
		};

		cellTable.addColumn(rankColumn, "Rank");
		cellTable.addColumn(zhCharColumn, "Simplified");

		cellTable.setPageSize(getPageSize());
		cellTable.setKeyboardPagingPolicy(KeyboardPagingPolicy.CHANGE_PAGE);
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

		// // Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTable);
		
		// Add a selection model so we can select cells.
		final SingleSelectionModel<ZhongWenCharacter> selectionModel = new SingleSelectionModel<ZhongWenCharacter>(
				CardData.KEY_PROVIDER);
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ZhongWenCharacter zwChar = selectionModel.getSelectedObject();	
				History.newItem(ApplicationConst.CHARARCTER + "/" + zwChar.getId());
			}
		});

		// Add the CellList to the data provider in the database.
		DataControler.get().addDataDisplay(cellTable);

		cellTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				lastAccessedRange = event.getNewRange();
			}
		});
		
		if (lastAccessedRange != null) {
			cellTable.setVisibleRange(lastAccessedRange);
		}
		
		
		initWidget(uiBinder.createAndBindUi(this));
	}

	private int getPageSize() {
		return 15;
	}

	

}
