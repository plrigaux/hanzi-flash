package com.plr.hanzi.client.view.flashcard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FlashCardCounter extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiField
	Label todo;
	@UiField
	Label again;

	private final NumPanel todoNum;
	private final NumPanel againNum;

	interface Binder extends UiBinder<Widget, FlashCardCounter> {
	}

	public FlashCardCounter() {
		initWidget(uiBinder.createAndBindUi(this));

		todoNum = new NumPanel(todo);
		againNum = new NumPanel(again);
	}

	public int getTodoNum() {
		return todoNum.getNum();
	}

	public void setTodoNum(int index) {
		todoNum.setNum(index);
	}

	public int getAgainNum() {
		return againNum.getNum();
	}

	public void setAgainNum(int index) {
		againNum.setNum(index);
	}

	public int incTodoNum() {
		return todoNum.incNum();
	}

	public int incAgainNum() {
		return againNum.incNum();
	}

	public int decTodoNum() {
		return todoNum.decNum();
	}

	public int decAgainNum() {
		return againNum.decNum();
	}

	private static class NumPanel {
		private final Label label;
		private int index = 0;

		NumPanel(Label label) {
			this.label = label;
			
			label.setText("" + index);
		}

		public void setNum(int index) {
			this.index = index;

			if (this.index < 0) {
				this.index = 0;
			}

			label.setText("" + this.index);
		}

		int decNum() {
			setNum(getNum() - 1);
			return getNum();
		}

		int incNum() {
			setNum(getNum() + 1);
			return getNum();
		}

		int getNum() {
			return this.index;
		}
	}
}
