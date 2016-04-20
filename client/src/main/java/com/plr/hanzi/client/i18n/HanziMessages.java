package com.plr.hanzi.client.i18n;

/**
 * Interface to represent the messages contained in resource bundle:
 * 	C:/GWT/workspace/hanzi/client/src/main/java/com/plr/hanzi/client/i18n/HanziMessages.properties'.
 */
public interface HanziMessages extends com.google.gwt.i18n.client.Messages {
  
  /**
   * Translated "Charater".
   * 
   * @return translated "Charater"
   */
  @DefaultMessage("Character")
  @Key("detail_character")
  String detail_character();

  /**
   * Translated "Definition".
   * 
   * @return translated "Definition"
   */
  @DefaultMessage("Definition")
  @Key("detail_definition")
  String detail_definition();

  /**
   * Translated "Rank".
   * 
   * @return translated "Rank"
   */
  @DefaultMessage("Rank")
  @Key("detail_rank")
  String detail_rank();

  /**
   * Translated "About".
   * 
   * @return translated "About"
   */
  @DefaultMessage("About")
  @Key("welcome_button_about")
  String welcome_button_about();

  /**
   * Translated "Browser".
   * 
   * @return translated "Browser"
   */
  @DefaultMessage("Browser")
  @Key("welcome_button_charBrowser")
  String welcome_button_charBrowser();

  /**
   * Translated "Flashcards".
   * 
   * @return translated "Flashcards"
   */
  @DefaultMessage("Flashcards")
  @Key("welcome_button_flashcards")
  String welcome_button_flashcards();

  /**
   * Translated "Guess".
   * 
   * @return translated "Guess"
   */
  @DefaultMessage("Guess")
  @Key("welcome_button_shishenme")
  String welcome_button_shishenme();
}
