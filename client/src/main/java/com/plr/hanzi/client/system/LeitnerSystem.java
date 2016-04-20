package com.plr.hanzi.client.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.storage.client.Storage;
import com.plr.hanzi.client.style.AppResources;

public class LeitnerSystem {

	private Map<LEVEL, LinkedHashSet<Integer>> leitnerLearningBoxes = new TreeMap<LEVEL, LinkedHashSet<Integer>>();

	private LinkedHashSet<CharInfo> buffer = new LinkedHashSet<CharInfo>();

	private int maxId = 0;

	public enum LEVEL implements BoxLevel {
		NEW(0), LEVEL_1(750), LEVEL_2(250), LEVEL_3(100), LEVEL_4(50);

		final int weight;
		int sumWeight;

		LEVEL(int weight) {
			this.weight = weight;
		}

		private static int sum = 0;
		static {
			for (LEVEL l : LEVEL.values()) {
				sum += l.weight;
				l.sumWeight = sum;
			}
		}

		static LEVEL getLevel(int value) {
			for (LEVEL l : LEVEL.values()) {
				if (value < l.sumWeight) {
					return l;
				}
			}

			return NEW;
		}

		static private Random rand = new Random();

		static LEVEL getLevelRand() {
			int value = rand.nextInt(LEVEL.sum);
			return getLevel(value);
		}

		@Override
		public int getOrdinal() {
			return ordinal();
		}

		static LEVEL getSafeLevel(int kl) {

			kl = Math.min(LEVEL.values().length - 1, kl);
			kl = Math.max(0, kl);

			LEVEL l = LEVEL.values()[kl];
			return l;
		}
	}

	private LeitnerSystem() {
		for (LEVEL l : LEVEL.values()) {
			leitnerLearningBoxes.put(l, new LinkedHashSet<Integer>());
		}
	}

	private LeitnerSystem(Map<Double, List<Integer>> map) {
		this();

		if (map != null) {
			for (Map.Entry<Double, List<Integer>> en : map.entrySet()) {

				double kl = en.getKey();
				LEVEL l = LEVEL.values()[(int) kl];

				LinkedHashSet<Integer> set = new LinkedHashSet<Integer>();
				set.addAll(en.getValue());

				leitnerLearningBoxes.put(l, set);
			}
		}

	}

	private LeitnerSystem(LeitnerSaver saver) {
		this();

		maxId = saver.getMaxId();

		for (LeitnerBoxSaver leitnerBoxes : saver.getLeitnerBoxes()) {

			int kl = leitnerBoxes.getBoxId();
			LEVEL l = LEVEL.getSafeLevel(kl);

			LinkedHashSet<Integer> set = new LinkedHashSet<Integer>(leitnerBoxes.getElements());

			leitnerLearningBoxes.put(l, set);
		}
	}

	int newToday() {
		return 0;
	}

	int reviewToday() {
		return 0;
	}

	public void setNew(int newNb) {
		LinkedHashSet<Integer> learningBox = leitnerLearningBoxes.get(LEVEL.NEW);

		int limit = newNb + maxId;
		for (int i = maxId; i < limit; i++) {
			learningBox.add(i + 1);

			maxId = Math.max(i + 1, maxId);
		}
	}

	public int totalSize() {
		int totalSize = 0;
		for (LinkedHashSet<Integer> learningBox : leitnerLearningBoxes.values()) {
			totalSize += learningBox.size();
		}
		return totalSize;
	}

	public CharInfo getNextCard() {
		CharInfo zc = null;

		zc = getAndRemove(LEVEL.NEW);

		// Not found
		if (zc == null) {
			LEVEL level = LEVEL.getLevelRand();
			for (int i = level.ordinal(); i >= 0; i--) {

				zc = getAndRemove(LEVEL.values()[i]);

				if (zc != null) {
					break;
				}
			}

			// Not found
			if (zc == null) {
				for (int i = level.ordinal(); i < LEVEL.values().length; i++) {

					zc = getAndRemove(LEVEL.values()[i]);

					if (zc != null) {
						break;
					}
				}
			}
		}

		if (zc != null) {
			buffer.add(zc);
		}

		return zc;
	}

	private CharInfo getAndRemove(LEVEL level) {
		CharInfo charInfo = null;
		LinkedHashSet<Integer> learningBox = leitnerLearningBoxes.get(level);

		if (learningBox != null) {
			Iterator<Integer> it = learningBox.iterator();
			while (it.hasNext()) {
				int charRank = it.next();

				charInfo = new CharInfo(level, charRank);
				it.remove();
				break;
			}
		}
		return charInfo;
	}

	public void answerCard(LEVEL level, CharInfo zwchar) {
		addToBox(level, zwchar);
	}

	public void shuffle(List<CharInfo> values) {

		Random r = new Random();
		for (int i = 0; i < values.size(); i++) {
			int index = r.nextInt(values.size());

			CharInfo c1 = values.get(i);
			CharInfo c2 = values.get(index);
			values.set(index, c1);
			values.set(i, c2);
		}
	}

	public int size(LEVEL l) {
		return leitnerLearningBoxes.get(l).size();
	}

	public List<CharInfo> getTrainingList(int listSize) {
		List<CharInfo> l = new ArrayList<CharInfo>();

		for (int i = 0; i < listSize; i++) {
			CharInfo charIdx = getNextCard();
			if (charIdx != null) {
				l.add(charIdx);
			} else {
				break;
			}
		}
		shuffle(l);
		return l;
	}

	public void save(String key) {
		LeitnerSaverFactory saverFactory = new LeitnerSaverFactory();
		LeitnerSaver saver = saverFactory.createSaver();

		ArrayList<LeitnerBoxSaver> leitnerBoxes = new ArrayList<LeitnerBoxSaver>();

		for (Map.Entry<LEVEL, LinkedHashSet<Integer>> en : leitnerLearningBoxes.entrySet()) {

			LeitnerBoxSaver boxSaver = saverFactory.createBoxSaver();

			int level = en.getKey().ordinal();
			ArrayList<Integer> elements = new ArrayList<Integer>(en.getValue());

			boxSaver.setBoxId(level);
			boxSaver.setElements(elements);

			leitnerBoxes.add(boxSaver);
		}

		saver.setLeitnerBoxes(leitnerBoxes);

		saver.setMaxId(maxId);

		String saveString = saverFactory.serializeToJson(saver);

		AppResources.logger.log(Level.INFO, "saveString: " + saveString);

		Storage stockStore = Storage.getLocalStorageIfSupported();
		if (stockStore != null) {
			stockStore.setItem(key, saveString);
		} else {
			AppResources.logger.log(Level.ALL, "Local storage not Suported");
		}

	}

	static public LeitnerSystem load(String key) {
		Storage stockStore = Storage.getLocalStorageIfSupported();
		if (stockStore != null) {
			String saveString = stockStore.getItem(key);

			AppResources.logger.log(Level.INFO, "saveString: " + saveString);
			if (saveString != null) {
				LeitnerSaverFactory saverFactory = new LeitnerSaverFactory();

				LeitnerSaver saver = saverFactory.deserializeFromJson(saveString);

				try {
					LeitnerSystem ls = new LeitnerSystem(saver);

					return ls;
				} catch (UmbrellaException e) {
					// some Auto bin error so clean
					stockStore.removeItem(key);
				}
			}
		} else {
			AppResources.logger.log(Level.ALL, "Local storage not Suported");
		}

		return new LeitnerSystem();
	}

	public void answerWrong(CharInfo charInfo) {
		if (charInfo == null) {
			return;
		}

		int ordinal = charInfo.getBoxLevel().getOrdinal();

		LEVEL l = LEVEL.getSafeLevel(ordinal);

		addToBox(l, charInfo);
	}

	public void answerOk(CharInfo charInfo) {
		if (charInfo == null) {
			return;
		}

		addToBox(LEVEL.NEW, charInfo);
	}

	private void addToBox(LEVEL level, CharInfo charInfo) {

		if (charInfo == null) {
			return;
		}

		Set<Integer> set = leitnerLearningBoxes.get(level);
		if (set != null) {
			int charRank = charInfo.getCharId();
			set.add(charRank);
			maxId = Math.max(charRank, maxId);
			buffer.remove(charInfo);
		}
	}
}
