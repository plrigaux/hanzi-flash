package com.plr.hanzi.client.supermemo;

/**
 * @author Pier
 * 
 */
public interface Record {

	/**
	 * Get Repetition
	 * 
	 * @param repetition
	 */
	public int getR();

	/**
	 * Get Interval
	 * 
	 * @param interval
	 */
	public int getI();

	public double getE();

	public int getId();

	public void setId(int Id);

	/**
	 * Set EF
	 * 
	 * @param repetition
	 */
	public void setE(double ef);

	/**
	 * Set Interval
	 * 
	 * @param interval
	 */
	public void setI(int interval);

	/**
	 * Set Repetition
	 * 
	 * @param repetition
	 */
	public void setR(int repetition);

	/**
	 * Set Order
	 * 
	 * @param order
	 */
	public void setO(int order);

	/**
	 * Get Order
	 * 
	 * @return
	 */
	public int getO();

}