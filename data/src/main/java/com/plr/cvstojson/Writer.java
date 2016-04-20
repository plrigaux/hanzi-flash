package com.plr.cvstojson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.io.JSONWriter;

import com.plr.cvstojson.Data.Definition;

public class Writer {

	private final int BATCH = 200;

	private int dataWrote = 0;

	private int listIndex = 1;

	private Map<String, String> num2pinyin = new TreeMap<String, String>();

	void write(List<String[]> csvRows) throws Exception {

		File file = new File("output");

		if (file.isDirectory()) {
			file.delete();
		}

		file.mkdir();

		int nbBatch = csvRows.size() / BATCH;

		for (int i = 1; i < nbBatch; i++) {

			doABatch(i, csvRows);
		}

		doPinyinToNum();

	}

	private void doPinyinToNum() throws IOException, FileNotFoundException,
			UnsupportedEncodingException, JSONException {
		File f = new File("output/num2pinyin.json");

		f.createNewFile();

		FileOutputStream fo = new FileOutputStream(f);

		OutputStreamWriter ow = new OutputStreamWriter(fo, "UTF-8");
		try {
			JSONWriter jw = new JSONWriter(ow);
			jw.object();
			for (Map.Entry<String, String> en : num2pinyin.entrySet()) {
				String k = en.getKey();
				String v = en.getValue();
				
				jw.key(k);
				jw.value(v);
			}
			jw.endObject();
		} finally {
			ow.close();
		}
	}

	private void doABatch(int batch, List<String[]> csvRows) throws Exception {

		File f = new File("output/chineseChar-" + batch + ".json");

		f.createNewFile();

		FileOutputStream fo = new FileOutputStream(f);

		OutputStreamWriter ow = new OutputStreamWriter(fo, "UTF-8");

		JSONWriter jw = new JSONWriter(ow);
		try {
			jw.array();
			Data data = null;

			int end = batch * BATCH;
			dataWrote = 0;
			while (listIndex < csvRows.size()) {

				String[] row = csvRows.get(listIndex++);

				String id = row[0];

				if (!id.isEmpty()) {
					int idi = Integer.parseInt(id);
					doOutput(data, jw);
					if (idi > end) {
						System.out.println("dw " + dataWrote);
						data = null;
						listIndex--;
						break;
					}

					data = new Data();
					data.setId(idi);
					data.setCharaters(row[1]);

				}

				// if (data == null) {
				// System.out.println(Arrays.asList(row));
				// System.out.println("li " + listIndex);
				// System.out.println(batch);
				// System.out.println("dw " + dataWrote);
				// }

				data.setExplanation(row[2]);
			}

			doOutput(data, jw);

			jw.endArray();
		} finally {
			// ow.flush();
			ow.close();
		}

		if (dataWrote == 0) {
			f.delete();
		}
		// jw.endObject();

	}

	private void doOutput(Data d, JSONWriter jw) throws Exception {
		if (d == null) {
			return;
		}

		jw.object();
		jw.key("i");
		jw.value(d.getId());
		jw.key("s");
		jw.value(d.getS());

		// if (d.getT() != null) {
		// jw.key("t");
		// jw.value(d.getT());
		// }

		// if (d.getT() != null) {
		// jw.key("a");
		// jw.value(d.getA());
		// }

		jw.key("d");

		jw.array();

		for (Definition def : d.getDefs()) {
			jw.object();
			jw.key("p");
			jw.value(def.getPy());

			jw.key("n");
			jw.value(def.getPyNum());

			jw.key("d");
			jw.array();
			for (String defin : def.getDefs()) {
				jw.value(defin);
			}
			jw.endArray();
			jw.endObject();

			num2pinyin.put(def.getPyNum(), def.getPy());
		}

		jw.endArray();

		jw.endObject();

		dataWrote++;

	}
}
