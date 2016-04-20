package com.plr.hanzi.client.supermemo;

import com.google.gwt.storage.client.Storage;

public class SuperMemoFactoryJava extends SuperMemoFactory {

	static void init() {
		SuperMemoFactory.superMemoFactory = new SuperMemoFactoryJava();
	}
	
	@Override
	RecordSaver getRecordSaver() {
		return new RecordSaverJava();
	}
	
	@Override
	Storage getLocalStorageIfSupported() {
		return null;
	}
}
