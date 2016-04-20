package com.plr.hanzi.client.system.stattable;

import java.util.List;

import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.plr.hanzi.client.ApplicationConst;
import com.plr.hanzi.client.supermemo.RecordInfo;
import com.plr.hanzi.client.supermemo.Revision;

public class StatDataControler implements ApplicationConst {

	

	private final Revision revision;

	/**
	 * The provider that holds the list of contacts in the database.
	 */
	private AbstractDataProvider<RecordInfo> dataProvider = new AbstractDataProvider<RecordInfo>() {
		@Override
		protected void onRangeChanged(final HasData<RecordInfo> display) {

			Range visibleRange = display.getVisibleRange();

			int start = visibleRange.getStart();
			int length = visibleRange.getLength();

			System.out.println("visibleRange " + visibleRange);

			if (start + length > LIMIT) {
				System.out.println("!hasData(visibleRange)");
				start = Math.max(0, LIMIT - length);
				// return;
			}

			List<RecordInfo> recordsList = revision.getRecordsList(start, length);

			display.setRowData(start, recordsList);
		}

	};

	StatDataControler(Revision revision) {
		this.revision = revision;
	}

	public void addDataDisplay(HasData<RecordInfo> display) {
		dataProvider.addDataDisplay(display);
		display.setRowCount(LIMIT);
	}

}
