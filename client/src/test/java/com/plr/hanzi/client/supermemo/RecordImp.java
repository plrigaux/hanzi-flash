package com.plr.hanzi.client.supermemo;

public class RecordImp implements Record {
	private int id;
	private int repetition;
	private int interval;
	private double ef;
	private int order;

	public RecordImp(int id) {
		this.repetition = 0;
		this.interval = 0;
		this.ef = 2.5;
		this.id = id;
	}

	@Override
	public int getR() {
		return repetition;
	}

	@Override
	public int getI() {
		return interval;
	}

	@Override
	public double getE() {
		return ef;
	}

	@Override
	public int getId() {		
		return id;
	}
	

	@Override
	public void setR(int repetition) {
		this.repetition = repetition;
	}

	@Override
	public void setI(int interval) {
		this.interval = interval;
	}

	@Override
	public void setE(double ef) {
		this.ef = ef;
	}

	@Override
	public int getO() {
		return order;
	}

	@Override
	public void setO(int order) {
		this.order = order;
	}

	@Override
	public void setId(int Id) {
		this.id = Id;
	}

}