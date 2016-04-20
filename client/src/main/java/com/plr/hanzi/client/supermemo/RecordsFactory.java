package com.plr.hanzi.client.supermemo;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface RecordsFactory extends AutoBeanFactory {
	AutoBean<Record> record();

	AutoBean<Records> records();
}
