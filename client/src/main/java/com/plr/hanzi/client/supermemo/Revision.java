package com.plr.hanzi.client.supermemo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.storage.client.Storage;
import com.plr.hanzi.client.style.AppResources;

public class Revision {
	public static final Logger logger = Logger.getLogger("SuperMemo");

	private final RecordSaver saver;
	private SuperMemoEngine engine = new SuperMemoEngine();
	private int recordsBatchNumber = 0;
	private int recordsBatchSize = 20;

	private List<RecordInfo> tempBatch = null;

	private TreeSet<RecordInfo> pQueue = new TreeSet<RecordInfo>(new Comparator<RecordInfo>() {
		@Override
		public int compare(RecordInfo o1, RecordInfo o2) {
			int v = o1.getOrder() - o2.getOrder();
			if (v == 0) {
				v = o1.getId() - o2.getId();
			}
			return v;
		}
	});

	private TreeMap<Integer, RecordInfo> map = new TreeMap<Integer, RecordInfo>();

	private final String key;

	Revision(String key) {
		this.key = key;
		saver = SuperMemoFactory.getSuperMemoFactory().getRecordSaver();
	}

	public int getRecordsBatchSize() {
		return recordsBatchSize;
	}

	public void setRecordsBatchSize(int batchSize) {
		this.recordsBatchSize = batchSize;
	}

	public List<RecordInfo> getRecordsBatch() {

		if (tempBatch != null) {
			setBatch();
		}

		ArrayList<RecordInfo> list = new ArrayList<RecordInfo>(recordsBatchSize);

		int limit = Math.min(recordsBatchSize, pQueue.size());
		for (int i = 0; i < limit; i++) {
			RecordInfo record = pQueue.first();

			if (record.getOrder() > recordsBatchNumber) {
				break;
			}

			pQueue.remove(record);
			list.add(record);
		}

		if (list.size() < recordsBatchSize) {

			int id = 1;
			if (!map.isEmpty()) {
				id = map.lastKey() + 1;
			}

			for (int i = list.size(); i < recordsBatchSize; i++) {

				RecordInfo record = new RecordInfo(id++, saver);

				list.add(record);
			}
		}

		recordsBatchNumber++;
		tempBatch = new ArrayList<RecordInfo>(list);
		return list;
	}

	public List<RecordInfo> getRecordsList() {
		return getRecordsList(0, recordsBatchSize);
	}

	public List<RecordInfo> getRecordsList(int start, int length) {

		if (tempBatch != null) {
			setBatch();
		}

		int end = start + length;
		ArrayList<RecordInfo> list = new ArrayList<RecordInfo>(length);

		int limit = Math.min(end, pQueue.size());

		if (start < limit) {

			int i = 0;
			for (RecordInfo recordInfo : pQueue) {
				if (i++ >= limit) {
					break;
				}

				if (i >= start) {
					list.add(recordInfo);
				}
			}
		}

		if (list.size() < length) {

			int id = start;
			if (!map.isEmpty()) {
				id = map.lastKey() + 1;
			}

			id = Math.max(id, start + 1);

			for (int i = list.size(); i < length; i++) {

				RecordInfo record = new RecordInfo(id++, saver);

				list.add(record);
			}
		}

		return list;
	}

	public void setBatch() {
		if (tempBatch == null) {
			return;
		}

		for (RecordInfo recordInfo : tempBatch) {
			pQueue.add(recordInfo);
			map.put(recordInfo.getId(), recordInfo);
		}

		tempBatch = null;
	}

	void load() {
		Records records = loadFromStorage();

		if (records == null) {
			records = saver.getNewRecords();
		} else {
			recordsBatchSize = records.getBatchSize();
			recordsBatchNumber = records.getBatchNum();
		}

		pQueue.clear();
		map.clear();

		// idiosyncrasi of java to js
		if (records.getRecords() == null) {
			records.setRecords(new ArrayList<Record>());
		}

		for (Record record : records.getRecords()) {
			RecordInfo recordInfo = new RecordInfo(record);
			pQueue.add(recordInfo);
			map.put(recordInfo.getId(), recordInfo);
		}
	}

	private Records loadFromStorage() {
		Records records = null;

		Storage stockStore = SuperMemoFactory.getSuperMemoFactory().getLocalStorageIfSupported();
		if (stockStore != null) {

			String saveString = stockStore.getItem(key);
			logger.log(Level.INFO, "saveString: " + saveString);

			if (saveString != null) {
				try {
					records = saver.deserializeFromJson(saveString);
				} catch (Exception e) {
					logger.log(Level.ALL, e.getMessage());

					// some Auto bin error so clean
					stockStore.removeItem(key);
				}
			}
		} else {
			logger.log(Level.ALL, "Local storage not Suported");
		}
		return records;
	}

	public void save() {

		setBatch();

		Records records = saver.getNewRecords();

		List<RecordInfo> recordInfoList = new ArrayList<RecordInfo>(map.values());

		List<Record> recordList = new ArrayList<Record>(recordInfoList.size());

		for (RecordInfo recordInfo : recordInfoList) {
			recordList.add(recordInfo.getRecord());
		}

		records.setRecords(recordList);

		records.setBatchSize(recordsBatchSize);

		records.setBatchNum(recordsBatchNumber);

		String saveString = saver.serializeToJson(records);

		AppResources.logger.log(Level.INFO, "saveString: " + saveString);

		Storage stockStore = SuperMemoFactory.getSuperMemoFactory().getLocalStorageIfSupported();
		if (stockStore != null) {
			stockStore.setItem(key, saveString);
		} else {
			AppResources.logger.log(Level.ALL, "Local storage not Suported");
		}

	}

	public static Revision load(String saverKey) {
		Revision r = new Revision(saverKey);
		r.load();
		return r;
	}

	public int getRecordsBatchNumber() {
		return recordsBatchNumber;
	}

	void setInterval(RecordInfo recordInfo, int interval) {

		int order = getRecordsBatchNumber() - 1 + interval;
		recordInfo.setOrder(order);
		recordInfo.setInterval(interval);
	}

	void setRecordsBatchNumber(int recordsBatchNumber) {
		this.recordsBatchNumber = recordsBatchNumber;
	}

	public void answer(int level, RecordInfo recordInfo) {
		engine.repetition(this, recordInfo, level);
	}
}
