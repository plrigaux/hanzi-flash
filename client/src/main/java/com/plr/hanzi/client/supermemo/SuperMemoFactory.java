package com.plr.hanzi.client.supermemo;

import com.google.gwt.storage.client.Storage;

public class SuperMemoFactory {
	
	static SuperMemoFactory superMemoFactory = new SuperMemoFactory();
	
	static SuperMemoFactory getSuperMemoFactory() {
		return superMemoFactory; 
	}
	
	RecordSaver getRecordSaver() {
		return new RecordSaverGWT();
	}
	
	Storage getLocalStorageIfSupported() {
		return Storage.getLocalStorageIfSupported();
	}
}
