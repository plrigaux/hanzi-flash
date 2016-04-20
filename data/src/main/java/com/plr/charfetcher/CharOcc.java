package com.plr.charfetcher;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.plr.cvstojson.Data;

public class CharOcc {

	private static final int CHARNUM = 5000;
	Map<Character, Data> charMap = new HashMap<>();

	public static void main(String[] args) {

		CharOcc m = new CharOcc();

		try {
			m.doAction();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void doAction() throws Exception {

		InputStream u = this.getClass().getResourceAsStream("../cvstojson/Modern Chinese Character Frequency List.txt");

		if (u == null) {
			System.err.println("snif");
		}

		// fstream = new FileInputStream("charset.csv");

		Splitter splitter = Splitter.on('\t');
		DataInputStream in = new DataInputStream(u);

		int i = 0;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {

			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}

				i++;
				Iterable<String> ite = splitter.split(line);
				Iterator<String> it = ite.iterator();

				Character c = null;

				it.next();
				c = it.next().charAt(0);

				Data d = new Data();
				d.setId(i);
				d.setSimpleCharacter(c);

				charMap.put(c, d);

				if (i >= CHARNUM) {
					break;
				}
			}

		} catch (Exception e) {

		}

		u = this.getClass().getResourceAsStream("../cvstojson/cedict_ts.u8");

		if (u == null) {
			System.err.println("snif");
		}

		// fstream = new FileInputStream("charset.csv");

		in = new DataInputStream(u);

		Pattern p = Pattern.compile("(\\S+)\\s(\\S+)\\s\\[(.+?)\\]\\s(.+)");
		String line = null;
		Splitter splitterSlash = Splitter.on('/').omitEmptyStrings();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {

			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}

				Matcher matcher = p.matcher(line);

				if (matcher.find()) {
					// String trad = m.group(1);
					String simple = matcher.group(2);

					if (simple.length() != 1) {
						continue;
					}

					String pinyin = matcher.group(3);
					String description = matcher.group(4);

					Character c = simple.charAt(0);

					Data data = charMap.get(c);

					if (data != null) {
						Iterable<String> ite = splitterSlash.split(description);
						data.addDefinitionNum(pinyin, ite);
					}
				}

			}

		} catch (Exception e) {
			System.err.println(e.getMessage() + " line: " + line);
			e.printStackTrace();
		}

		CharOccWriter writer = new CharOccWriter();

		Set<Data> dataSet = new TreeSet<>(new Comparator<Data>() {

			@Override
			public int compare(Data o1, Data o2) {
				return o1.getId() - o2.getId();
			}
		});

		dataSet.addAll(charMap.values());

		writer.write(dataSet);

	}
}
