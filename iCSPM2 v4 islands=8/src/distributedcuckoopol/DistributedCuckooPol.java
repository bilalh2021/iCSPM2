/**
 * ************************************************
 * This Program is coded by Bilal H. Abed-alguni
 *
 * Island-based Cuckoo Search with Highly Disruptive Polynomial Mutation
 *
 * Yarmouk University- Department of Computer Sciences  *
 *************************************************
 */
package distributedcuckoopol;

import java.util.Scanner;

public class DistributedCuckooPol {

    public static void main(String[] args) {

        int Maxiter = 10000;  //Maximum number of iterations
        int dimension = 100;  //the dimension of the problem
        int Fm = 50;  // migration frequency
        int N = 1000; // population size
        int numSubpopulation = 8;  // number of islands
        double Rm = 0.10; // migration rate
        int ff = 1;       // function number
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the dimension of the problem: ");
        dimension = scan.nextInt();
        System.out.println("Enter the value of Fm: ");
        Fm = scan.nextInt();
        System.out.println("Enter the value of Rm (between 0 and 1) ");
        Rm = scan.nextDouble();
        System.out.println("Enter the function number (between 1 to 15): ");
        ff = scan.nextInt();
        double LB = 0, UB = 0; //lower bound and upper bound of the function
        if (ff == 1 || ff == 3 || ff == 5 || ff == 11 || ff == 12 || ff == 13) {
            LB = -100;
            UB = 100;
        } else if (ff == 2) {
            LB = -10;
            UB = 10;
        } else if (ff == 7) {
            LB = -5.12;
            UB = 5.12;
         } else if (ff == 4) {
            LB = -2.048;
            UB =  2.048;
        } else if (ff == 10) {
            dimension = 2;
            LB = -5;
            UB = 5;
        } else if (ff == 14 || ff == 15) {
            LB = -5;
            UB = 5;
        } else if (ff == 6) {
            LB = -500;
            UB = 500;
        } else if (ff == 8) {
            LB = -32;
            UB = 32;
        } else if (ff == 9) {
            LB = -600;
            UB = 600;
        }

        int numberofMigrants = (int) (N / numSubpopulation * Rm); //number of migrants
        double result[] = new double[numSubpopulation];
        double bestOptimizedValue = 1000000000;

        for (int i = 0; i < Maxiter / Fm; i++) {
            System.out.println("**************pass " + i);
            CS10 o1 = new CS10(ff, Fm, dimension, N / numSubpopulation, LB, UB);
            result[0] = o1.search();
            if (result[0] < bestOptimizedValue) {
                bestOptimizedValue = result[0];
            }
            CS11 o2 = new CS11(ff, Fm, dimension, N / numSubpopulation, LB, UB);
            result[1] = o2.search();
            if (result[1] < bestOptimizedValue) {
                bestOptimizedValue = result[1];
            }
            CS1 o3 = new CS1(ff, Fm, dimension, N / numSubpopulation, LB, UB);
            result[2] = o3.search();
            if (result[2] < bestOptimizedValue) {
                bestOptimizedValue = result[2];
            }
            CSJ o4 = new CSJ(ff, Fm, dimension, N / numSubpopulation, LB, UB);
            result[3] = o4.search();
            if (result[3] < bestOptimizedValue) {
                bestOptimizedValue = result[3];
            }
            CS11 o5 = new CS11(ff, Fm, dimension, N / numSubpopulation, LB, UB);
            result[4] = o5.search();
            if (result[4] < bestOptimizedValue) {
                bestOptimizedValue = result[4];
            }
            CS1 o6 = new CS1(ff, Fm, dimension, N / numSubpopulation, LB, UB);
            result[5] = o6.search();
            if (result[5] < bestOptimizedValue) {
                bestOptimizedValue = result[5];
            }
            CS10 o7 = new CS10(ff, Fm, dimension, N / numSubpopulation, LB, UB);
            result[6] = o7.search();
            if (result[6] < bestOptimizedValue) {
                bestOptimizedValue = result[6];
            }
            CSJ o8 = new CSJ(ff, Fm, dimension, N / numSubpopulation, LB, UB);
            result[7] = o8.search();
            if (result[7] < bestOptimizedValue) {
                bestOptimizedValue = result[7];
            }
        
           

            if (i % 10 == 0 || i % 10 == 1) {
                o2.replaceWorst(o1.migrateBest(numberofMigrants));
                o3.replaceWorst(o2.migrateBest(numberofMigrants));
                o4.replaceWorst(o3.migrateBest(numberofMigrants));
                o5.replaceWorst(o4.migrateBest(numberofMigrants));
                o6.replaceWorst(o5.migrateBest(numberofMigrants));
                o8.replaceWorst(o6.migrateBest(numberofMigrants));
                o7.replaceWorst(o8.migrateBest(numberofMigrants));
                o1.replaceWorst(o7.migrateBest(numberofMigrants));
            } else if (i % 10 == 02 || i % 10 == 3) {
                o3.replaceWorst(o1.migrateBest(numberofMigrants));
                o5.replaceWorst(o3.migrateBest(numberofMigrants));
                o4.replaceWorst(o5.migrateBest(numberofMigrants));
                o6.replaceWorst(o4.migrateBest(numberofMigrants));
                o8.replaceWorst(o6.migrateBest(numberofMigrants));
                o7.replaceWorst(o8.migrateBest(numberofMigrants));
                o2.replaceWorst(o7.migrateBest(numberofMigrants));
                o1.replaceWorst(o2.migrateBest(numberofMigrants));
            } else if (i % 10 == 4 || i % 10 == 5) {
                o1.replaceWorst(o7.migrateBest(numberofMigrants));
                o2.replaceWorst(o1.migrateBest(numberofMigrants));
                o3.replaceWorst(o2.migrateBest(numberofMigrants));
                o4.replaceWorst(o3.migrateBest(numberofMigrants));
                o6.replaceWorst(o4.migrateBest(numberofMigrants));
                o5.replaceWorst(o6.migrateBest(numberofMigrants));
                o8.replaceWorst(o5.migrateBest(numberofMigrants));
                o7.replaceWorst(o8.migrateBest(numberofMigrants));
            } else {
                o5.replaceWorst(o6.migrateBest(numberofMigrants));
                o1.replaceWorst(o5.migrateBest(numberofMigrants));
                o3.replaceWorst(o1.migrateBest(numberofMigrants));
                o7.replaceWorst(o3.migrateBest(numberofMigrants));
                o2.replaceWorst(o7.migrateBest(numberofMigrants));
                o8.replaceWorst(o2.migrateBest(numberofMigrants));
                o4.replaceWorst(o8.migrateBest(numberofMigrants));
                o6.replaceWorst(o4.migrateBest(numberofMigrants));
            }
            System.out.println("Minimum Value= "+ bestOptimizedValue);
        }
    }

}
