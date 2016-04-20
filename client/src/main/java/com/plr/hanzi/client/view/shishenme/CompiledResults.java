package com.plr.hanzi.client.view.shishenme;

import java.util.HashMap;
import java.util.Map;



public class CompiledResults {

	Map<String, Results> map = new HashMap<String, Results>();

	void setOk(String zwChar) {
		getResult(zwChar).incOk();
	}

	void setWrong(String zwChar) {
		getResult(zwChar).incWrong();
	}

	private Results getResult(String zwChar) {
		Results r = map.get(zwChar);

		if (r == null) {
			r = new Results();
			map.put(zwChar, r);
		}

		return r;
	}

	public static class Results {

		private int ok = 0;
		private int wrong = 0;

		Results() {
			ok = 0;
			wrong = 0;
		}

		public int getOk() {
			return ok;
		}

		public int getWrong() {
			return wrong;
		}

		public void incWrong() {
			ok++;
		}

		public void incOk() {
			wrong++;
		}

	}
}
