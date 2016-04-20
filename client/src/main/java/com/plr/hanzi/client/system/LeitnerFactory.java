package com.plr.hanzi.client.system;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface LeitnerFactory extends AutoBeanFactory {

	AutoBean<LeitnerSaver> leitnerSaver();

	AutoBean<LeitnerBoxSaver> leitnerBoxSaver();
}
