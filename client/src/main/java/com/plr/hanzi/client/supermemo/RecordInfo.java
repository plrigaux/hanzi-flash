package com.plr.hanzi.client.supermemo;

public class RecordInfo {
	final Record record;

	RecordInfo(Record record) {
		this.record = record;
	}

	RecordInfo(int id, RecordSaver saver) {
		this.record = saver.getNewRecord(id);
	}

	/**
	 * Because the rank start at 1 and index start at 0
	 * 
	 * @return index of charater
	 */

	public int getCharIndex() {
		return getId() - 1;
	}

	public int getRepetition() {
		return record.getR();
	}

	public int getInterval() {
		return record.getI();
	}

	public double getEf() {
		return record.getE();
	}

	public int getId() {
		return record.getId();
	}

	void setRepetition(int repetition) {
		record.setR(repetition);
	}

	void setInterval(int interval) {
		record.setI(interval);
	}

	void setEf(double ef) {
		record.setE(ef);
	}

	public int getOrder() {
		return record.getO();
	}

	public void setOrder(int order) {
		record.setO(order);
	}

	@Override
	public String toString() {
		return "id:" + getId() + " r:" + getRepetition() + " o:" + getOrder() + " i:" + getInterval() + " ef:" + getEf();
	}

	Record getRecord() {
		return record;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordInfo other = (RecordInfo) obj;

		if (record.getId() != other.record.getId())
			return false;
		return true;
	}

}
