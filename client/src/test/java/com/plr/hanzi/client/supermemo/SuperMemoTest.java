package com.plr.hanzi.client.supermemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SuperMemoTest {

	@BeforeClass
	public static void init() {
		SuperMemoFactoryJava.init();
	}

	@Test
	public void testLoad() {
		Revision.load("test");
	}

	@Test
	public void testGetBacth() {
		Revision revision = Revision.load("test");
		List<RecordInfo> list = revision.getRecordsBatch();
		System.out.println(list.size());
		System.out.println(list);
	}

	@Test
	public void testRepetition() {
		SuperMemoEngine revisione = new SuperMemoEngine();
		Revision revision = Revision.load("test");
		revision.setRecordsBatchSize(5);
		List<RecordInfo> list = revision.getRecordsBatch();

		for (RecordInfo r : list) {
			revisione.repetition(revision, r, 1);
		}

		System.out.println(list);

		for (RecordInfo r : list) {
			revisione.repetition(revision, r, 1);
		}

		System.out.println(list);
	}

	@Test
	public void testOrder() {

		Revision revision = Revision.load("test");
		revision.setRecordsBatchSize(3);
		List<RecordInfo> list = revision.getRecordsBatch();

		list.get(0).setOrder(3);
		list.get(1).setOrder(2);
		list.get(2).setOrder(1);

		System.out.println(list);

		List<RecordInfo> expected = new ArrayList<RecordInfo>();

		expected.add(list.get(2));
		expected.add(list.get(1));
		expected.add(list.get(0));

		revision.setRecordsBatchNumber(100);

		List<RecordInfo> list2 = revision.getRecordsBatch();

		System.out.println(list2);

		Assert.assertArrayEquals(expected.toArray(), list2.toArray());

	}

	@Test
	public void testNew() {

		Revision revision = Revision.load("test");
		revision.setRecordsBatchSize(3);
		List<RecordInfo> list = revision.getRecordsBatch();

		list.get(0).setOrder(3);

		List<RecordInfo> list2 = revision.getRecordsBatch();

		int max = -1;
		for (RecordInfo recordInfo : list2) {
			max = Math.max(max, recordInfo.getId());
		}
		Assert.assertEquals(4, max);

	}

	@Test
	public void testNew2() {

		Revision revision = Revision.load("test");
		revision.setRecordsBatchSize(3);
		List<RecordInfo> list = revision.getRecordsBatch();

		for (RecordInfo recordInfo : list) {
			recordInfo.setOrder(3);
		}

		List<RecordInfo> list2 = revision.getRecordsBatch();

		int max = -1;
		for (RecordInfo recordInfo : list2) {
			max = Math.max(max, recordInfo.getId());
		}
		Assert.assertEquals(6, max);

	}

	@Test
	public void testRepetitionRecord() {
		Revision revision = Revision.load("test");
		revision.setRecordsBatchSize(3);

		List<RecordInfo> list = revision.getRecordsBatch();
		Set<Integer> expecteds = new TreeSet<Integer>();
		for (RecordInfo recordInfo : list) {
			revision.setInterval(recordInfo, 1);
			expecteds.add(recordInfo.getId());
		}

		List<RecordInfo> list2 = revision.getRecordsBatch();

		Set<Integer> actuals = new TreeSet<Integer>();

		for (RecordInfo recordInfo : list2) {
			actuals.add(recordInfo.getId());
		}
		Assert.assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void testRepetitionRecord1() {
		Revision revision = Revision.load("test");
		revision.setRecordsBatchSize(3);

		List<RecordInfo> list = revision.getRecordsBatch();
		Set<Integer> expecteds = new TreeSet<Integer>();
		for (RecordInfo recordInfo : list) {
			revision.setInterval(recordInfo, 2);
			expecteds.add(recordInfo.getId());
		}

		List<RecordInfo> list2 = revision.getRecordsBatch();
		// System.out.println(list2);
		for (RecordInfo recordInfo : list2) {
			revision.setInterval(recordInfo, 2);
		}

		list2 = revision.getRecordsBatch();

		Set<Integer> actuals = new TreeSet<Integer>();

		for (RecordInfo recordInfo : list2) {
			actuals.add(recordInfo.getId());
		}
		// System.out.println(list2);
		Assert.assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void testRepetitionRecord2() {
		Revision revision = Revision.load("test");
		revision.setRecordsBatchSize(3);

		List<RecordInfo> list = revision.getRecordsBatch();
		TreeSet<Integer> expecteds = new TreeSet<Integer>();
		for (RecordInfo recordInfo : list) {
			revision.setInterval(recordInfo, 2);
			expecteds.add(recordInfo.getId());
		}
		// System.out.println(list);

		List<RecordInfo> list2 = revision.getRecordsBatch();

		TreeSet<Integer> actuals = new TreeSet<Integer>();

		for (RecordInfo recordInfo : list2) {
			actuals.add(recordInfo.getId());
		}
		// System.out.println(list2);

		Assert.assertFalse(expecteds.first() == actuals.first());
	}
}
