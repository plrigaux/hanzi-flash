package com.plr.cvstojson;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

import com.google.common.base.Splitter;

public class Data {
	private int rankId;
	private Character tc;
	private Character simpleCharacter;
	private Character ac;

	private PriorityQueue<Definition> pron = new PriorityQueue<>(5, new Comparator<Definition>() {

		@Override
		public int compare(Definition o1, Definition o2) {
			return o1.getPyNum().compareTo(o2.getPyNum());
		}
	});

	static public class Definition {
		private String pinyin;
		private String pinyinNum;
		private ArrayList<String> def = new ArrayList<String>();

		Definition(String pinyin, Iterable<String> it) {

			pinyin = Normalizer.normalize(pinyin, Form.NFC);

			this.pinyin = pinyin;
			for (String s : it) {
				def.add(s);
			}

			pinyinNum = Pinyin.convertNum(pinyin);
		}

		public void setNum(String pinyinNum, Iterable<String> it) {

			pinyin = Pinyin.convertNum(pinyinNum);

			this.pinyin = Pinyin.convertToAccent(pinyinNum);
			for (String s : it) {
				def.add(s);
			}

			this.pinyinNum = pinyinNum;
		}

		public Definition() {
			// TODO Auto-generated constructor stub
		}

		public String getPy() {
			return pinyin;
		}

		public ArrayList<String> getDefs() {

			return def;
		}

		public String getPyNum() {
			return pinyinNum;
		}
	}

	public void setId(int i) {
		this.rankId = i;
	}

	public void setCharaters(String chars) {
		simpleCharacter = chars.charAt(0);

		if (chars.length() > 4 && chars.charAt(2) == 'F') {
			tc = chars.charAt(3);
		}

		if (chars.length() > 4 && chars.charAt(2) == 'A') {
			ac = chars.charAt(3);
		}
	}

	static Splitter s = Splitter.on(';').trimResults().omitEmptyStrings();

	public void setExplanation(String string) {

		int b = string.indexOf('[');
		int c = string.indexOf(']');

		if (c != -1 && b != -1) {
			String pinyin = string.substring(b + 1, c);

			Iterable<String> it = s.split(string.substring(c + 1));
			addDefinition(pinyin, it);
		}
	}

	public void addDefinition(String pinying, Iterable<String> it) {
		Definition d = new Definition(pinying, it);

		pron.add(d);
	}

	public void addDefinitionNum(String pinyinNum, Iterable<String> it) {
		Definition d = new Definition();

		d.setNum(pinyinNum, it);

		pron.add(d);
	}

	public int getId() {
		return rankId;
	}

	public Character getS() {
		return simpleCharacter;
	}

	public Character setSimpleCharacter(Character simpleCharacter) {
		return this.simpleCharacter = simpleCharacter;
	}

	public Character getT() {
		return tc;
	}

	public Character getA() {
		return ac;
	}

	public Collection<Definition> getDefs() {
		return pron;
	}

	@Override
	public String toString() {

		return "rankId " + rankId + " sc " + simpleCharacter;
	}
}
