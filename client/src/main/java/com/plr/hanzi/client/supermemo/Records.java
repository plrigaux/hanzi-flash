package com.plr.hanzi.client.supermemo;

import java.util.List;

public interface Records {
	List<Record> getRecords();

	void setRecords(List<Record> records);

	int getBatchNum();

	void setBatchNum(int batchNum);

	int getBatchSize();

	void setBatchSize(int batchSize);
}