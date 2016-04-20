package com.plr.hanzi.client.system.stattable;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.plr.hanzi.client.ApplicationConst;
import com.plr.hanzi.client.CardData;
import com.plr.hanzi.client.DataControler;
import com.plr.hanzi.client.ZhongWenCharacter;
import com.plr.hanzi.client.supermemo.RecordInfo;
import com.plr.hanzi.client.supermemo.Revision;

public class StatTable extends Composite {

	interface Binder extends UiBinder<Widget, StatTable> {
	}

	private static Binder uiBinder = GWT.create(Binder.class);

	/**
	 * The pager used to display the current range.
	 */
	@UiField(provided = true)
	AbstractPager pager;

	/**
	 * The CellList.
	 */
	@UiField(provided = true)
	CellTable<RecordInfo> cellTable;

	private static Range lastAccessedRange = null;

	public StatTable(Revision revision) {

		ProvidesKey<RecordInfo> providesKey = new ProvidesKey<RecordInfo>() {

			@Override
			public Object getKey(RecordInfo item) {
				return item == null ? null : item.getId();
			}
		};

		cellTable = new CellTable<RecordInfo>(providesKey);
		cellTable.setPageSize(getPageSize());
		cellTable.setKeyboardPagingPolicy(KeyboardPagingPolicy.CHANGE_PAGE);
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

		initTableColumns();

		// // Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTable);

		// pager = new PageSizePager(15);
		// pager.setDisplay(cellTable);

		// Add a selection model so we can select cells.
		final SingleSelectionModel<RecordInfo> selectionModel = new SingleSelectionModel<RecordInfo>(providesKey);
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				RecordInfo zwChar = selectionModel.getSelectedObject();
				History.newItem(ApplicationConst.CHARARCTER + "/" + zwChar.getId());
			}
		});

		StatDataControler statDataControler = new StatDataControler(revision);
		// Add the CellList to the data provider in the database.
		statDataControler.addDataDisplay(cellTable);

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

	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns() {

		// Column<RecordInfo, String> addressColumn = new Column<RecordInfo,
		// String>(
		// new ColorCell()) {
		//
		// };

		CharColumn charColumn = new CharColumn();

		TextColumn<RecordInfo> rankColumn = new TextColumn<RecordInfo>() {
			@Override
			public String getValue(RecordInfo zwChar) {
				return "" + zwChar.getId();
			}
		};

		TextColumn<RecordInfo> zhCharColumn = new TextColumn<RecordInfo>() {
			@Override
			public String getValue(RecordInfo zwChar) {
				return "" + zwChar.getOrder();
			}
		};

		TextColumn<RecordInfo> efColumn = new TextColumn<RecordInfo>() {
			@Override
			public String getValue(RecordInfo zwChar) {
				return "" + zwChar.getEf();
			}
		};

		cellTable.addColumn(charColumn, "Char");
		cellTable.addColumn(rankColumn, "Rank");
		cellTable.addColumn(zhCharColumn, "Order");
		cellTable.addColumn(efColumn, "EF");
	}

	private int getPageSize() {
		return 15;
	}

	class CharColumn extends TextColumn<RecordInfo> {
		public CharColumn() {
			ColorCell contactCell = new ColorCell();
			cellList = new CellList<ZhongWenCharacter>(contactCell, CardData.KEY_PROVIDER) {

				@Override
				protected void renderRowValues(SafeHtmlBuilder sb, List<ZhongWenCharacter> values, int start,
						SelectionModel<? super ZhongWenCharacter> selectionModel) {

					if (values.isEmpty()) {
						return;
					}

					ZhongWenCharacter zc = values.get(0);
					List<RecordInfo> list = cellTable.getVisibleItems();
					int id = zc.getId();

					int i = 0;
					boolean found = false;
					for (RecordInfo recordInfo : list) {
						if (id == recordInfo.getId()) {
							found = true;
							break;
						}
						i++;
					}
	

					if (found) {
						
						TableRowElement tre = cellTable.getRowElement(i);
						TableCellElement tce = tre.getCells().getItem(0);
						tce.setInnerHTML(zc.getSimplifiedCharacter());
						// System.err.println("id " + id + " sdf " + i + " " +
						// values + " start " + start);
					}

//					if (queue.isEmpty()) {
//						semaphore = true;
//					} else {
//						Range r = queue.removeLast();
//						cellList.setVisibleRange(r);
//					}

				}

				@Override
				public void setRowData(int start, List<? extends ZhongWenCharacter> values) {
					@SuppressWarnings("unchecked")
					List<ZhongWenCharacter> v = (List<ZhongWenCharacter>) values;
					renderRowValues(null, v, start, null);
					// super.setRowData(start, values);
				}

				
			};

			DataControler.get().addDataDisplay(cellList);
		}

		private CellList<ZhongWenCharacter> cellList;

		@Override
		public String getValue(RecordInfo zwChar) {
			return "";
		}

//		private boolean semaphore = true;
//		private LinkedList<Range> queue = new LinkedList<Range>();

		@Override
		public void render(Context context, RecordInfo zwChar, SafeHtmlBuilder sb) {
			Range r = new Range(zwChar.getId() - 1, 1);
//			if (semaphore) {
				cellList.setVisibleRange(r);
//				semaphore = false;
//			} else {
//				queue.addLast(r);
//			}

			System.out.println(zwChar.getId());
		}
	};

	static class ColorCell extends AbstractCell<ZhongWenCharacter> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, ZhongWenCharacter value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}

			SafeHtml safeValue = SafeHtmlUtils.fromString(value.getSimplifiedCharacter());
			sb.append(safeValue);
		}

	}
}
