/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kristoffer
 */
public class SimStart {

    public static void main(String[] args) {
        Simulator sim = new Simulator();
        sim.simulate(1000);
//        int middle = 60;
//        int landscapeSeed = 812;
//        int number = middle;
//        for (int i = 0; i < 100; i++) {
//            number = Math.abs(((landscapeSeed ^ middle + middle ^ landscapeSeed) - ((middle + landscapeSeed) * (i^2 - i))) % 10) - 5;
//            middle += number;
//            if(middle < 0){
//                middle = 0;
//            } else if (middle > 120){
//                middle = 120;
//            }
//            
//            System.out.println("" + number + "   " + middle);
//        }
    }
}
