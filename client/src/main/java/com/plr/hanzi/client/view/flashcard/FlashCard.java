package com.plr.hanzi.client.view.flashcard;

import java.util.List;
import java.util.logging.Level;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.plr.hanzi.client.CardData;
import com.plr.hanzi.client.DataControler;
import com.plr.hanzi.client.ZhongWenCharacter;
import com.plr.hanzi.client.style.AppResources;
import com.plr.hanzi.client.system.controler.ControlerSystemWidget;
import com.plr.hanzi.client.view.definition.DefinitionPanel;
import com.plr.hanzi.client.view.welcome.CustomButton;

public class FlashCard extends ControlerSystemWidget {

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiField
	CustomButton show;
//	@UiField
//	CustomButton again;
//	@UiField
//	CustomButton hard;
//	@UiField
//	CustomButton good;
//	@UiField
//	CustomButton easy;
	@UiField
	Label character;
	@UiField
	DefinitionPanel definitionPanel;
	
	@UiField
	FlashCardCounter counter;

	@UiField
	DivElement buttonsDiv;

	@UiField
	FlashCardStyle style;

	ZhongWenCharacter zwChar = null;
	

	interface Binder extends UiBinder<Widget, FlashCard> {
	}

	private CellList<ZhongWenCharacter> cellList;

	public FlashCard(FlashCardSystem flashCardSystem) {
		super(flashCardSystem);
		initWidget(uiBinder.createAndBindUi(this));

		// Create a CellList.
		CharacterCell contactCell = new CharacterCell();

		cellList = new CellList<ZhongWenCharacter>(contactCell, CardData.KEY_PROVIDER) {

			@Override
			protected void renderRowValues(SafeHtmlBuilder sb, List<ZhongWenCharacter> values, int start,
					SelectionModel<? super ZhongWenCharacter> selectionModel) {
				setChar(values);
				// shuffle(values);
			}
		};

		// cellList.setPageSize(PAGE_SIZE);
		cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);

		DataControler.get().addDataDisplay(cellList);

		// Add a selection model so we can select cells.
		final NoSelectionModel<ZhongWenCharacter> selectionModel = new NoSelectionModel<ZhongWenCharacter>(CardData.KEY_PROVIDER);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
			}

		});

		cellList.setSelectionModel(selectionModel);

		cellList.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				FlashCard.this.onRangeChange(event.getNewRange());
			}
		});

		cellList.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {
			@Override
			public void onRowCountChange(RowCountChangeEvent event) {
				onRangeOrRowCountChanged();
			}
		});

//		again.addStyleName(style.button());
//		hard.addStyleName(style.button());
//		good.addStyleName(style.button());
//		easy.addStyleName(style.button());

		//show.addStyleName(style.showButton());

		character.addStyleName(AppResources.INSTANCE.style().character());

		nextZwChar();
	}

	private static class CharacterCell extends AbstractCell<ZhongWenCharacter> {

		@Override
		public void render(Context context, ZhongWenCharacter value, SafeHtmlBuilder sb) {
		}
	}

	@UiHandler("show")
	void onShowClick(ClickEvent event) {
		definitionPanel.setVisible(true);
		definitionPanel.setCharater(zwChar);
		buttonsDiv.setClassName(style.enabled());
		show.addStyleName(style.disabled());
	}

	private void onRangeOrRowCountChanged() {

	}

	private void onRangeChange(Range range) {

	}

	@UiHandler("again")
	void onAgainClick(ClickEvent event) {

		controlerSystem.answerCard(4, charInfo);
		counter.incAgainNum();
		nextZwChar();
	}

	@UiHandler("hard")
	void onHardClick(ClickEvent event) {
		controlerSystem.answerCard(3, charInfo);
		nextZwChar();
	}

	@UiHandler("good")
	void onGoodClick(ClickEvent event) {
		controlerSystem.answerCard(2, charInfo);
		nextZwChar();
	}

	@UiHandler("easy")
	void onEasyClick(ClickEvent event) {
		controlerSystem.answerCard(1, charInfo);
		nextZwChar();
	}
	
//	@UiHandler("trivial")
//	void onTrivialClick(ClickEvent event) {
//		getLeitnerSystem().answerCard(LEVEL.LEVEL_4, charInfo);
//		nextZwChar();
//	}

	private void nextZwChar() {
		show.removeStyleName(style.disabled());
		buttonsDiv.setClassName(style.disabled());
		definitionPanel.setVisible(false);
		
		counter.setTodoNum(trainingList.size());
		
		super.nextChar();
		
		if (charInfo != null) {
			
			cellList.setVisibleRange(charInfo.getCharIndex(), 1);
		}
	}

	private void setChar(List<ZhongWenCharacter> values) {
		if (values.isEmpty()) {
			return;
		}

		zwChar = values.get(0);
		character.setText(zwChar.getSimplifiedCharacter());
		AppResources.logger.log(Level.INFO, "Rank " + zwChar.getId() + " char: " + zwChar.getSimplifiedCharacter());
	}
}
