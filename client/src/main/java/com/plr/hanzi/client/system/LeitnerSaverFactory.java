package com.plr.hanzi.client.system;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class LeitnerSaverFactory {
	// Instantiate the factory
	LeitnerFactory factory = GWT.create(LeitnerFactory.class);

	// In non-GWT code, use AutoBeanFactoryMagic.create(MyFactory.class);

	public LeitnerSaver createSaver() {
		// Construct the AutoBean
		AutoBean<LeitnerSaver> saver = factory.leitnerSaver();

		return saver.as();
	}

	public String serializeToJson(LeitnerSaver saver) {
		// Retrieve the AutoBean controller
		AutoBean<LeitnerSaver> bean = AutoBeanUtils.getAutoBean(saver);

		return AutoBeanCodex.encode(bean).getPayload();
	}

	public LeitnerSaver deserializeFromJson(String json) {
		AutoBean<LeitnerSaver> bean = AutoBeanCodex.decode(factory,
				LeitnerSaver.class, json);
		return bean.as();
	}

	public LeitnerBoxSaver createBoxSaver() {
		// Construct the AutoBean
		AutoBean<LeitnerBoxSaver> saver = factory.leitnerBoxSaver();

		return saver.as();
	}
}
