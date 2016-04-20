package com.plr.hanzi.client.compress;

/**
*
* @author kebernet
*/
class HuffNode extends HuffTree {

   private HuffTree left;
   private HuffTree right;

   //HuffNode constructor
   public HuffNode(int frequency, HuffTree left, HuffTree right) {
       this .frequency = frequency;
       this .left = left;
       this .right = right;
   }//end HuffNode constructor

   public HuffTree getLeft() {
       return left;
   }//end getLeft

   public HuffTree getRight() {
       return right;
   }//end getRight

}
