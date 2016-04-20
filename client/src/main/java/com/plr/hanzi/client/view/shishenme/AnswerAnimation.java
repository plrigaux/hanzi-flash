package com.plr.hanzi.client.view.shishenme;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

public class AnswerAnimation extends Animation {

	private Element element;

	private double endSize = 48.0;

	private double startSize = 30.0;

	public AnswerAnimation(Element element) {
		this.element = element;
	}

	@Override
	protected void onUpdate(double progress) {

		double s;
		if (progress < 0.5) {
			progress = progress * 2;
			s = endSize - progress * (endSize - startSize);
		} else {
			progress = (progress - 0.5) * 2;
			s = startSize + progress * (endSize - startSize);
		}
		
		Style style = element.getStyle();
		style.setFontSize(s, Unit.PX);
		// style.setOpacity(progress);
	}

}
