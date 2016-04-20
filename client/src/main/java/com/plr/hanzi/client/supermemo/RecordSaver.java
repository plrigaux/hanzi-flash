package com.plr.hanzi.client.supermemo;


public interface RecordSaver {
	String serializeToJson(Records recordMap) ;

	Records deserializeFromJson(String json) ;

	Records getNewRecords();

	Record getNewRecord(int id);
}
