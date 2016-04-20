package com.plr.hanzi.client.tools;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.plr.hanzi.client.tools.Normalizer.Form;

public class Pinyin {

	static private final String[] allPinyinArray = { "a", "o", "e", "ai", "ei", "ao", "ou", "an", "en", "ang", "eng", "er", "yo",
			"yi", "ya", "ye", "yao", "you", "yan", "yin", "yang", "ying", "wu", "wa", "wo", "wai", "wei", "wan", "wen", "wang",
			"weng", "yu", "yue", "yuan", "yun", "yong", "bi", "ba", "bo", "bai", "bei", "bao", "ban", "ben", "bang", "beng",
			"bie", "biao", "bian", "bin", "bing", "bu", "pi", "pa", "po", "pai", "pei", "pao", "pou", "pan", "pen", "pang",
			"peng", "pie", "piao", "pian", "pin", "ping", "pu", "mi", "ma", "mo", "me", "mai", "mei", "mao", "mou", "man", "men",
			"mang", "meng", "mie", "miao", "miu", "mian", "min", "ming", "mu", "fa", "fo", "fei", "fou", "fan", "fen", "fang",
			"feng", "fu", "di", "da", "de", "dai", "dei", "dao", "dou", "dan", "dang", "deng", "die", "diao", "diu", "dian",
			"ding", "du", "duo", "dui", "duan", "dun", "dong", "ti", "ta", "te", "tai", "tao", "tou", "tan", "tang", "teng",
			"tie", "tiao", "tian", "ting", "tu", "tuo", "tui", "tuan", "tun", "tong", "ni", "na", "ne", "nai", "nei", "nao",
			"nou", "nan", "nen", "nang", "neng", "nie", "niao", "niu", "nian", "nin", "niang", "ning", "nu", "nuo", "nuan",
			"nong", "nv", "nue", "li", "la", "lo", "le", "lai", "lei", "lao", "lou", "lan", "lang", "leng", "lia", "lie", "liao",
			"liu", "lian", "lin", "liang", "ling", "lu", "luo", "luan", "lun", "long", "lv", "lue", "ga", "ge", "gai", "gei",
			"gao", "gou", "gan", "gen", "gang", "geng", "gu", "gua", "guo", "guai", "gui", "guan", "gun", "guang", "gong", "ka",
			"ke", "kai", "kei", "kao", "kou", "kan", "ken", "kang", "keng", "ku", "kua", "kuo", "kuai", "kui", "kuan", "kun",
			"kuang", "kong", "ha", "he", "hai", "hei", "hao", "hou", "han", "hen", "hang", "heng", "hu", "hua", "huo", "huai",
			"hui", "huan", "hun", "huang", "hong", "ji", "jia", "jie", "jiao", "jiu", "jian", "jin", "jiang", "jing", "ju",
			"juan", "jun", "jue", "jiong", "qi", "qia", "qie", "qiao", "qiu", "qian", "qin", "qiang", "qing", "qu", "quan",
			"qun", "que", "qiong", "xi", "xia", "xie", "xiao", "xiu", "xian", "xin", "xiang", "xing", "xu", "xuan", "xun", "xue",
			"xiong", "zhi", "zha", "zhe", "zhai", "zhei", "zhao", "zhou", "zhan", "zhen", "zhang", "zheng", "zhu", "zhua",
			"zhuo", "zhuai", "zhui", "zhuan", "zhun", "zhuang", "zhong", "chi", "cha", "che", "chai", "chao", "chou", "chan",
			"chen", "chang", "cheng", "chu", "chua", "chuo", "chuai", "chui", "chuan", "chun", "chuang", "chong", "shi", "sha",
			"she", "shai", "shei", "shao", "shou", "shan", "shen", "shang", "sheng", "shu", "shua", "shuo", "shuai", "shui",
			"shuan", "shun", "shuang", "ri", "re", "rao", "rou", "ran", "ren", "rang", "reng", "ru", "ruo", "rui", "ruan", "run",
			"rong", "zi", "za", "ze", "zai", "zei", "zao", "zou", "zan", "zen", "zang", "zeng", "zu", "zuo", "zui", "zuan",
			"zun", "zong", "ci", "ca", "ce", "cai", "cao", "cou", "can", "cen", "cang", "ceng", "cu", "cuo", "cui", "cuan",
			"cun", "cong", "si", "sa", "se", "sai", "sao", "sou", "san", "sen", "sang", "seng", "su", "suo", "sui", "suan",
			"sun", "song" };

	static public final char COMBINING_GRAVE_ACCENT = '\u0300';
	static public final char COMBINING_ACUTE_ACCENT = '\u0301';
	static public final char COMBINING_MACRON = '\u0304';
	// static public final char COMBINING_OVERLINE = '\u0305';
	static public final char COMBINING_BREVE = '\u0306';
	static public final char COMBINING_CARON = '\u030C';

	private static final char[] pinyinVoy;

	private final static RegExp p;

	private static final int NOT_FOUND = -1;

	static {

		String COMBININGS = "" + COMBINING_CARON + COMBINING_ACUTE_ACCENT + COMBINING_MACRON + COMBINING_ACUTE_ACCENT
				+ COMBINING_GRAVE_ACCENT;

		String voyellBase = "aeiou";
		StringBuilder sb = new StringBuilder((COMBININGS.length() + 1) * voyellBase.length());
		for (int i = 0; i < COMBININGS.length(); i++) {

			char comb = COMBININGS.charAt(i);

			for (int j = 0; j < voyellBase.length(); j++) {
				char voy = voyellBase.charAt(j);
				sb.append(voy).append(comb);
			}
		}

		sb.append(voyellBase);

		sb.append(sb);

		for (int i = sb.length() / 2; i < sb.length(); i++) {
			char voy = sb.charAt(i);
			voy = Character.toUpperCase(voy);
			sb.setCharAt(i, voy);
		}

		String allVoyelle = Normalizer.normalize(sb, Form.NFC);

		pinyinVoy = new char[allVoyelle.length()];

		allVoyelle.getChars(0, allVoyelle.length(), pinyinVoy, 0);

		Arrays.sort(pinyinVoy);

		String regex = "((?:b|c|d|f|g|h|j|k|l|m|n|p|q|r|s|t|w|x|y|z|zh|sh|ch)" + "(?![" + COMBININGS + "]))?(?:[" + allVoyelle
				+ COMBININGS + "]){1,4}" + "(?:(?:ngr|ng|nr|n|r)(?![" + allVoyelle + "]))?+\\d?";

		p = RegExp.compile(regex, "i");

	}

	private static int binarySearch(char x) {
		int low = 0;
		int high = pinyinVoy.length - 1;
		int mid;

		while (low <= high) {
			mid = (low + high) / 2;

			if (pinyinVoy[mid] < x)
				low = mid + 1;
			else if (pinyinVoy[mid] > x)
				high = mid - 1;
			else
				return mid;
		}

		return NOT_FOUND;
	}

	static final HashSet<String> allPinyin = new HashSet<String>();

	static {

		for (String s : allPinyinArray) {
			allPinyin.add(s);
		}

	}

	/**
	 * <ul>
	 * <li><b>a</b> and <b>e</b> trump all other vowels and always take the tone
	 * mark. There are no Mandarin syllables in Hanyu Pinyin that contain both a
	 * and e.
	 * <li>
	 * In the combination <b>ou</b>, <b>o</b> takes the mark.
	 * <li>In all other cases, the final vowel takes the mark.
	 * </ul>
	 * 
	 * @param input
	 * @return
	 */
	public String convertSyllable(String input) {

		input = Normalizer.normalize(input, Form.NFD);

		return convertSyllableNormalize(input);
	}

	private String convertSyllableNormalize(String input) {

		StringBuilder returnString = new StringBuilder(input.length() + 4);

		char toneLetter = 0;
		int position = 0;
		int tone = 0;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (isVoyel(c)) {
				if (setNewToAccent(toneLetter, c)) {
					toneLetter = c;
					position = i;
				}
				returnString.append(c);
			} else if (!Character.isDigit(c)) {
				returnString.append(c);
			} else if (Character.isDigit(c)) {
				tone = Character.digit(c, 10);
			} else if (isCombining(c)) {
				tone = getToneFromCombined(c);
			}
		}

		toneLetter = getCharTone(toneLetter, tone);
		returnString.setCharAt(position, toneLetter);

		return returnString.toString();

	}

	private boolean setNewToAccent(char toaccent, char c) {

		switch (toaccent) {
		case 'a':
		case 'A':
			return false;
		case 'e':
		case 'E':
			if (c == 'i' || c == 'I') {
				return false;
			}
			break;
		case 'i':
		case 'I':
			break;
		case 'o':
		case 'O':
			if (c == 'u') {
				return false;
			}
			break;
		case 'u':
		case 'v':
		case 'U':
		case 'V':
			return true;
		}

		return true;
	}

	private char getCharTone(char toaccent, int tone) {

		char combining;

		switch (tone) {

		case 1:
			combining = COMBINING_MACRON;
			break;
		case 2:
			combining = COMBINING_ACUTE_ACCENT;
			break;
		case 3:
			combining = COMBINING_CARON;
			break;
		case 4:
			combining = COMBINING_GRAVE_ACCENT;
			break;

		default:
			return toaccent;

		}

		StringBuilder sb = new StringBuilder(2);

		sb.append(toaccent);
		sb.append(combining);

		String norm = Normalizer.normalize(sb, Form.NFC);

		return norm.charAt(0);
	}

	private boolean isVoyel(char c) {
		return binarySearch(c) != NOT_FOUND;
	}

	public List<String> separate(String input) {
		ArrayList<String> list = new ArrayList<String>();

		MatchResult m = p.exec(input);

		for (int i = 0; i < m.getGroupCount(); i ++) {
			String s = m.getGroup(i);

			list.add(s);
		}

		return list;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @return the tone
	 */
	public int getTone() {
		return tone;
	}

	private String word;
	private int tone;

	public void extractTone(String originalWord) {

		StringBuilder builder = new StringBuilder();

		String norm = Normalizer.normalize(originalWord, Form.NFD);

		for (int i = 0; i < norm.length(); i++) {

			char ch = norm.charAt(i);

			// System.out.println("ch : " + String.format("'\\u%04x'", (int)
			// ch));

			if (Character.isDigit(ch)) {
				//tone = Character.getNumericValue(ch);
				continue;
			}

			if (isCombining(ch)) {

				tone = getToneFromCombined(ch);
			} else {
				builder.append(ch);
			}
		}

		word = builder.toString();
	}

	/**
	 * @param ch
	 */
	private int getToneFromCombined(char ch) {
		int tone;
		switch (ch) {
		case COMBINING_MACRON:
			// case COMBINING_OVERLINE:
			tone = 1;
			break;

		case COMBINING_ACUTE_ACCENT:
			tone = 2;
			break;

		case COMBINING_BREVE:
		case COMBINING_CARON:
			tone = 3;
			break;

		case COMBINING_GRAVE_ACCENT:
			tone = 4;
			break;

		default:
			tone = 5;
		}
		return tone;
	}

	
	
	static RegExp COMBINING_DIACRITICAL_MARKS = RegExp.compile("\\p{InCombining_Diacritical_Marks}");
	/**
	 * @param ch
	 * @return
	 */
	boolean isCombining(char ch) {	
		// see ftp://ftp.unicode.org/Public/UNIDATA/UnicodeData.txt
		// http://stackoverflow.com/questions/227950/programatic-accent-reduction-in-javascript-aka-text-normalization-or-unaccenting
		//return Character.UnicodeBlock.COMBINING_DIACRITICAL_MARKS.equals(Character.UnicodeBlock.of(ch));
		
		return COMBINING_DIACRITICAL_MARKS.test("" + ch);
	}

	/**
	 * @param actual
	 * @return
	 */
	public String convertToAccent(String input) {

		StringBuilder sb = new StringBuilder(input.length());

		MatchResult m = p.exec(input);

		int start = 0;
		for (int i = 0; i < m.getGroupCount(); i++) {
			String sep = m.getGroup(i);

			String sep2 = convertSyllableNormalize(sep);

			String inter = input.substring(start, m.getIndex());

			sb.append(inter);
			sb.append(sep2);

			start = m.getIndex() + sep.length();
		}

		String inter = input.substring(start, input.length());

		sb.append(inter);

		input = Normalizer.normalize(sb, Form.NFC);
		
		return input;
	}
}
