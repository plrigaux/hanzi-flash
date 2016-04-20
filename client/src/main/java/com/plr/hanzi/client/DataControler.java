package com.plr.hanzi.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class DataControler implements ApplicationConst {
	private static final int CHAR_BY_FILE = 200;
	
	private static final int MAXINDEX = LIMIT / CHAR_BY_FILE;

	private static DataControler instance = null;

	static private Logger LOGGER = Logger.getLogger(DataControler.class.getName());

	private int lastLoadedIndex = 0;

	private Set<String> resources = new HashSet<String>();
	/**
	 * The provider that holds the list of contacts in the database.
	 */

	private ArrayList<ZhongWenCharacter> mainList = new ArrayList<ZhongWenCharacter>();

	private AbstractDataProvider<ZhongWenCharacter> dataProvider = new AbstractDataProvider<ZhongWenCharacter>() {
		@Override
		protected void onRangeChanged(final HasData<ZhongWenCharacter> display) {

			Range visibleRange = display.getVisibleRange();
			int start = visibleRange.getStart();
			int length = visibleRange.getLength();

			System.out.println("visibleRange " + visibleRange);

			if (start + length > LIMIT) {
				System.out.println("!hasData(visibleRange)");
				start = Math.max(0, LIMIT - length);
				// return;
			}

			List<ZhongWenCharacter> zhongWenCharacters = mainList;

			// System.out.println("zhongWenCharacters.size() " +
			// zhongWenCharacters.size());
			// System.out.println("visibleRange.getStart() + visibleRange.getLength() "
			// + visibleRange.getStart() + visibleRange.getLength());

			// no need to load data
			if (zhongWenCharacters.size() > start + length) {
				// System.out.println("onRangeChangedSuper(display); ");

				setRowData(display, start, length);
			} else {

				int lastRangeIndex = Math.min(((start + length) / CHAR_BY_FILE) + 1, MAXINDEX);

				// System.out.println("lastRangeIndex " + lastRangeIndex);

				// System.out.println("call loadData form change" );
				loadData(display, lastRangeIndex, start, length);
			}
		}

		private void loadData(final HasData<ZhongWenCharacter> display, final int lastRangeIndex, final int start,
				final int length) {
			
			final int indexToLoad = lastLoadedIndex + 1;
			
			if (indexToLoad > MAXINDEX) {
				return;
			}
			
			final String resource = "data/chineseChar-" + indexToLoad + ".json";
			// System.out.println(resource);

			System.out.println(resource);
			RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, resource);

			requestBuilder.setCallback(new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {

					int code = response.getStatusCode();

					System.out.println("code " + code);
					if (code < 200 && code >= 400) {
						LOGGER.log(Level.SEVERE, resource + " code http : " + code);
						return;
					}

					// System.out.println("jsonString " + jsonString);

					// Load only one time the resource
					if (resources.add(resource)) {
						// System.out.println("Already loaded " + resource + " "
						// + resources);
						String jsonString = response.getText();

						JsArray<CardData> cardDatas = buildCardData(jsonString);

						List<ZhongWenCharacter> zhongWenCharacters = mainList;

						for (int i = 0; i < cardDatas.length(); i++) {

							zhongWenCharacters.add(cardDatas.get(i));
						}

						lastLoadedIndex = indexToLoad;
					}

					// System.out.println(" " + resource + " "
					// +zhongWenCharacters.size());

					System.out.println("lastLoadedIndex " + lastLoadedIndex);
					System.out.println("indexToLoad " + indexToLoad);
					System.out.println("lastRangeIndex " + lastRangeIndex);

					// We going to get the next range
					if (indexToLoad < lastRangeIndex) {
						// System.out.println("call loadData form load" );
						loadData(display, lastRangeIndex, start, length);
					} else {
						setRowData(display, start, length);
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					LOGGER.log(Level.SEVERE, exception.getMessage());
				}
			});

			try {

				System.out.println("URL " + requestBuilder.getUrl());
				requestBuilder.send();
			} catch (RequestException e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		}

		private void setRowData(HasData<ZhongWenCharacter> display, int start, int length) {

			if (mainList.size() >= start + length) {
				List<ZhongWenCharacter> list = mainList.subList(start, start + length);
				display.setRowData(start, list);
			}

		}

	};

	private boolean dataReady = false;

	private DataControler() {

	}

	public static DataControler get() {
		if (instance == null) {
			instance = new DataControler();
		}
		return instance;

	}

	public static final native JsArray<CardData> buildCardData(String json) /*-{
																			return eval('(' + json + ')');
																			}-*/;

	public void addDataDisplay(HasData<ZhongWenCharacter> display) {
		dataProvider.addDataDisplay(display);
		display.setRowCount(LIMIT);
	}

}
