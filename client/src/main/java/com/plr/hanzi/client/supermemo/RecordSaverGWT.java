package com.plr.hanzi.client.supermemo;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

public class RecordSaverGWT implements RecordSaver {
	// Instantiate the factory
	private final RecordsFactory factory = GWT.create(RecordsFactory.class);

	@Override
	public String serializeToJson(Records records) {
		// Retrieve the AutoBean controller
		AutoBean<Records> bean = AutoBeanUtils.getAutoBean(records);

		Splittable splittable = AutoBeanCodex.encode(bean);
		
		String json = splittable.getPayload();

		return json;
	}

	@Override
	public Records deserializeFromJson(String json) {
		AutoBean<Records> bean = AutoBeanCodex.decode(factory, Records.class, json);
		return bean.as();
	}

	@Override
	public Records getNewRecords() {
		AutoBean<Records> autoBeanRecords = factory.records();
		return autoBeanRecords.as();
	}

	@Override
	public Record getNewRecord(int id) {
		AutoBean<Record> autoBeanRecords = factory.record();
		Record r = autoBeanRecords.as();
		r.setId(id);
		r.setE(2.5);
		r.setI(0);
		r.setO(0);
		r.setR(0);
	
		
		return r;
	}

}
