package com.plr.hanzi.client.supermemo;

import java.util.ArrayList;
import java.util.List;

public class RecordsImp implements Records {

	private List<Record> records = new ArrayList<Record>();
	private int batchNum = 0;
	private int batchSize = 0;

	@Override
	public List<Record> getRecords() {
		return records;
	}

	@Override
	public void setRecords(List<Record> records) {
		this.records = records;
	}

	@Override
	public int getBatchNum() {
		return batchNum;
	}

	@Override
	public void setBatchNum(int batchNum) {
		this.batchNum = batchNum;
	}

	@Override
	public int getBatchSize() {
		return batchSize;
	}

	@Override
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

}
