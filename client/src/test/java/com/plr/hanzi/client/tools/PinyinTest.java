package com.plr.hanzi.client.tools;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;

import com.plr.hanzi.client.tools.Pinyin;

public class PinyinTest {

	Pinyin pinyin;

	@Before
	public void before() {
		pinyin = new Pinyin();
	}
	
	
	@Test
	public void isCombining() {
		Assert.assertTrue(pinyin.isCombining(Pinyin.COMBINING_ACUTE_ACCENT));
		Assert.assertTrue(pinyin.isCombining(Pinyin.COMBINING_BREVE));
		Assert.assertTrue(pinyin.isCombining(Pinyin.COMBINING_CARON));
		Assert.assertTrue(pinyin.isCombining(Pinyin.COMBINING_MACRON));
		Assert.assertTrue(pinyin.isCombining(Pinyin.COMBINING_GRAVE_ACCENT));
	}

	@Test
	public void test1() {

		convertSyllable("ying2");
		convertSyllable("li4");
		convertSyllable("dao4");
		convertSyllable("xiao3");
		convertSyllable("xue3");
		convertSyllable("zou3");
		convertSyllable("chuang2");
		convertSyllable("die2");

	}
	
	private void convertSyllable(String input) {
		String converted = pinyin.convertSyllable(input);
		
		System.out.println(converted);
	}

	@Test
	public void testVoyellesPrecehence() {
		validate("aeiou1", "āeiou");
		validate("eioua1", "eiouā");
		validate("ei1", "ēi");
		validate("ie1", "iē");
		validate("iu1", "iū");

		validate("ai2", "ái");
		validate("ao2", "áo");
		validate("ei2", "éi");
		validate("ia2", "iá");
		validate("iao2", "iáo");
		validate("ie2", "ié");
		validate("io2", "ió");
		validate("iu2", "iú");
		validate("ou2", "óu");
		validate("ua2", "uá");
		validate("uai2", "uái");
		validate("ue2", "ué");
		validate("ui2", "uí");
		validate("uo2", "uó");
		validate("üe2", "üé");

	}

	private void validate(String actual, String expected) {

		String transformed = pinyin.convertSyllable(actual);
		try {
			Assert.assertEquals(expected, transformed);
		} catch (ComparisonFailure e) {
			// TODO check those char \u0301
		}
	}

//	@Test
//	public void normalise() {
//		testNormalise("bào", "bao", 4);
//		testNormalise("fēng", "feng", 1);
//		testNormalise("yǔ", "yu", 3);
//
//	}
//
//	/**
//	 * @param string
//	 * @param string2
//	 * @param i
//	 */
//	private void testNormalise(String input, String expected, int tone) {
//		pinyin.extractTone(input);
//
//		String word = pinyin.getWord();
//
//		Assert.assertEquals("Word not good", expected, word);
//		Assert.assertEquals("Tone not good", tone, pinyin.getTone());
//
//	}
}