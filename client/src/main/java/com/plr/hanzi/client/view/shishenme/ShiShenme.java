package com.plr.hanzi.client.view.shishenme;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.plr.hanzi.client.CardData;
import com.plr.hanzi.client.DataControler;
import com.plr.hanzi.client.Tone;
import com.plr.hanzi.client.ZhongWenCharacter;
import com.plr.hanzi.client.CardData.CharDefinition;
import com.plr.hanzi.client.style.AppResources;
import com.plr.hanzi.client.system.controler.ControlerSystemWidget;
import com.plr.hanzi.client.view.definition.DefinitionPanel;

public class ShiShenme extends ControlerSystemWidget {

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiField
	Button answerValidate;

	@UiField
	Label character;

	@UiField
	DefinitionPanel definitionPanel;

	@UiField
	TextBox answer;

	@UiField
	DivElement result;

	@UiField
	DivElement explanation;

	@UiField
	StyleAnswer style;

	private ZhongWenCharacter zwChar = null;
	
	interface Binder extends UiBinder<Widget, ShiShenme> {
	}

	private CellList<ZhongWenCharacter> cellList;

	CompiledResults compiledResults = new CompiledResults();

	public ShiShenme(ShiShenmeSystem shiShenmeSystem) {
		super(shiShenmeSystem);
		initWidget(uiBinder.createAndBindUi(this));

		character.addStyleName(AppResources.INSTANCE.style().character());

		CharacterCell cCell = new CharacterCell();

		cellList = new CellList<ZhongWenCharacter>(cCell, CardData.KEY_PROVIDER) {

			@Override
			protected void renderRowValues(SafeHtmlBuilder sb, List<ZhongWenCharacter> values, int start,
					SelectionModel<? super ZhongWenCharacter> selectionModel) {

				zwChar = values.get(0);
				character.setText(zwChar.getSimplifiedCharacter());
			}

		};
		cellList.setPageSize(1);
		cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);

		DataControler.get().addDataDisplay(cellList);

		cellList.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
			}
		});

		cellList.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {
			@Override
			public void onRowCountChange(RowCountChangeEvent event) {
			}
		});
	}

	@UiHandler("answerValidate")
	void onAnswerValidateClick(ClickEvent event) {
		doValidate();
		answer.setFocus(true);
	}

	@UiHandler("answer")
	void onAnswerKeyPress(KeyPressEvent event) {
		int keyCode = event.getUnicodeCharCode();
		if (KeyCodes.KEY_ENTER == keyCode) {
			doValidate();
		}
	}

	private void doValidate() {
		String input = answer.getText();

		input.toLowerCase();

		boolean ok = false;
		for (int i = 0; i < zwChar.definitionCount(); i++) {
			CharDefinition charDef = zwChar.getDefinition(i);
			String pn = charDef.getPinyinNum().toLowerCase();
			if (pn.toLowerCase().equals(input.toLowerCase())) {
				ok = true;
				break;
			}
		}

		StringBuilder displayAnswer = new StringBuilder();

		String htmlAnswer;

		if (ok) {
			htmlAnswer = "对";
			result.removeClassName(style.wrong());
			result.addClassName(style.ok());
			compiledResults.setOk(zwChar.getSimplifiedCharacter());
			controlerSystem.answerOk(charInfo);
		} else {
			htmlAnswer = "错";
			result.removeClassName(style.ok());
			result.addClassName(style.wrong());
			compiledResults.setWrong(zwChar.getSimplifiedCharacter());
			controlerSystem.answerWrong(charInfo);
		}

		result.setInnerHTML(htmlAnswer);

		displayAnswer.append("<span class='" + style.askedChar() + "'>");
		displayAnswer.append(zwChar.getSimplifiedCharacter());
		displayAnswer.append("</span> 是 ");

		int count = zwChar.definitionCount();
		for (int i = 0; i < count; i++) {

			if (i != 0) {
				if (i + 1 == count) {
					displayAnswer.append(" or ");
				} else {
					displayAnswer.append(",");
				}
			}

			CharDefinition charDefinition = zwChar.getDefinition(i);

			String pinyin = charDefinition.getPinyin();
			int tone = charDefinition.getTone();
			String toneStyle = Tone.getTone(tone).getCssClass();

			displayAnswer.append("<span class='").append(toneStyle).append("'>").append(pinyin).append("</span>");
		}

		displayAnswer.append(".");
		explanation.setInnerHTML(displayAnswer.toString());

		new AnswerAnimation(result).run(750);
		
		nextZwChar();
	}

	public void nextZwChar() {
		answer.setText("");

		super.nextChar();

		if (charInfo != null) {
			// cause the rank start at 1 and index start at 0
			cellList.setVisibleRange(charInfo.getCharIndex(), 1);
		}

	}

	static class CharacterCell extends AbstractCell<ZhongWenCharacter> {
		@Override
		public void render(Context context, ZhongWenCharacter value, SafeHtmlBuilder sb) {

		}
	}
}
