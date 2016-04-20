package com.plr.hanzi.client.compress;

/**
 * 
 * @author kebernet
 */
class HuffLeaf extends HuffTree {

	private int value;

	// HuffLeaf constructor
	public HuffLeaf(int value, int frequency) {
		this.value = value;
		// Note that frequency is inherited from HuffTree
		this.frequency = frequency;
	}// end HuffLeaf constructor

	public int getValue() {
		return value;
	}// end getValue

}