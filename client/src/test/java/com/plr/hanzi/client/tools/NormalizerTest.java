package com.plr.hanzi.client.tools;

import org.junit.Assert;
import org.junit.Test;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.plr.hanzi.client.tools.Pinyin;

public class NormalizerTest {

	@Test
	public void testSingleDouble() {

		RegExp r = RegExp
				.compile(
						"[Aa\\xaa\\xc0-\\xc5\\xe0-\\xe5\u0100-\u0105\u01cd\u01ce\u0200-\u0203\u0226\u0227\u1d2c\u1d43\u1e00\u1e01\u1e9a\u1ea0-\u1ea3\u2090\u2100\u2101\u213b\u249c\u24b6\u24d0\u3371-\u3374\u3380-\u3384\u3388\u3389\u33a9-\u33af\u33c2\u33ca\u33df\u33ff\uff21\uff41]",
						"i");

		Assert.assertTrue(r.test("à"));

		String a = "a" + Pinyin.COMBINING_GRAVE_ACCENT;
		System.out.println(a);
		Assert.assertTrue(r.test(a));

		String t = r.exec(a).getGroup(0);

		System.out.println(t.length());
		System.out.println(t);
	}

	@Test
	public void testSingleDouble3() {

		//RegExp r = RegExp.compile("\\P{M}\\p{M}*", "g");
		//RegExp r = RegExp.compile("\\p{L}\\p{M}*", "g");
		
		RegExp r = RegExp
				.compile(
						"[Aa\\xaa\\xc0-\\xc5\\xe0-\\xe5\u0100-\u0105\u01cd\u01ce\u0200-\u0203\u0226\u0227\u1d2c\u1d43\u1e00\u1e01\u1e9a\u1ea0-\u1ea3\u2090\u2100\u2101\u213b\u249c\u24b6\u24d0\u3371-\u3374\u3380-\u3384\u3388\u3389\u33a9-\u33af\u33c2\u33ca\u33df\u33ff\uff21\uff41]",
						"ig");

		System.out.println(r.getGlobal());
		// //Assert.assertTrue(r.test("à"));
		//
		String a = "da" + Pinyin.COMBINING_GRAVE_ACCENT + "dfàg";
		// System.out.println(a);
		// //Assert.assertTrue(r.test(a));

		MatchResult mr = r.exec(a);

		System.out.println("getIndex " + mr.getIndex());
		System.out.println("getGroupCount " + mr.getGroupCount());
		System.out.println("getLastIndex " + r.getLastIndex());

		r.setLastIndex(r.getLastIndex());

		while (mr != null) {
			String t = mr.getGroup(0);
			System.out.println("l " + t.length() + " '" + t  + "'");
			//System.out.println(t);

			r.setLastIndex(r.getLastIndex());
			mr = r.exec(a);
		}

	}
}