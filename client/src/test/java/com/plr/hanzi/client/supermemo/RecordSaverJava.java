package com.plr.hanzi.client.supermemo;

public class RecordSaverJava implements RecordSaver {

	
	@Override
	public String serializeToJson(Records recordMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Records deserializeFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Records getNewRecords() {
		return new RecordsImp();
	}

	@Override
	public Record getNewRecord(int id) {
		return new RecordImp(id);
	}

}
