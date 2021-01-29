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
        int dimension = 10;  //the dimension of the problem
        int Fm = 100;  // migration frequency
        int N = 1000; // population size
        int numSubpopulation = 4;  // number of islands
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
        }else if (ff == 7) {
            LB = -5.12;
            UB = 5.12;
         } else if (ff == 4) {
            LB = -2.048;
            UB =  2.048;
        }  else if (ff == 10) {
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
         
              CS10 o1= new CS10(ff,Fm,dimension,N/numSubpopulation,LB,UB);
             result[0]=  o1.search();
             if (result[0]<bestOptimizedValue)
            bestOptimizedValue=result[0];
             CS11 o2= new CS11(ff,Fm,dimension,N/numSubpopulation,LB,UB);
             result[1]=  o2.search();
              if (result[1]<bestOptimizedValue)
            bestOptimizedValue=result[1];
             CS1 o3= new CS1(ff,Fm,dimension,N/numSubpopulation,LB,UB);
               result[2]=  o3.search();
              if (result[2]<bestOptimizedValue)
            bestOptimizedValue=result[2];
             CSJ o4= new CSJ(ff,Fm,dimension,N/numSubpopulation,LB,UB);
             result[3]=  o4.search();
              if (result[3]<bestOptimizedValue)
            bestOptimizedValue=result[3];

         
             
if (i%5==0){
o1.replaceWorst(o4.migrateBest(numberofMigrants));
o2.replaceWorst(o1.migrateBest(numberofMigrants));
o3.replaceWorst(o2.migrateBest(numberofMigrants));
o4.replaceWorst(o3.migrateBest(numberofMigrants));
}
else if (i%3==1){
o1.replaceWorst(o2.migrateBest(numberofMigrants));
o4.replaceWorst(o1.migrateBest(numberofMigrants));
o3.replaceWorst(o4.migrateBest(numberofMigrants));
o2.replaceWorst(o3.migrateBest(numberofMigrants));  
}
else{
o1.replaceWorst(o4.migrateBest(numberofMigrants));
o2.replaceWorst(o1.migrateBest(numberofMigrants));
o3.replaceWorst(o2.migrateBest(numberofMigrants));
o4.replaceWorst(o3.migrateBest(numberofMigrants)); 

}
System.out.println(bestOptimizedValue);
        }
        
//        double avg1=best1 / numberofAttempts ;
//        double avg2=best2 / numberofAttempts ;
//        double avg3=best3 / numberofAttempts ;
//        double avg4=best4 / numberofAttempts ;
//        double avg5=best5 / numberofAttempts ;
//        double avg6=best6 / numberofAttempts ;
//        double avg7=best7 / numberofAttempts ;
//        double avg8=best8 / numberofAttempts ;
//        double avg9=best9 / numberofAttempts ;
//        double avg10=best10 / numberofAttempts ;
//        double avg11=best11 / numberofAttempts ;
  System.out.println("**********************************************************");       
//        System.out.println(avg1 + "\t" + avg2
//                + "\t" + avg3 + "\t" + avg4 + "\t"
//                + avg5 + "\t" + avg6 + "\t"
//                + avg7 + "\t"+avg8+"\t"+ avg9+"\t"+ avg10+"\t"+ avg11+"\t");
 System.out.println("**********************************************************");
         
//             for (int i = 0; i < attempt.length ; i++) {
//             for (int j = 0; j < attempt[i].length ; j++) {
//         System.out.print(attempt[i][j]+"\t");
//          
//          }
//System.out.println();
//             }
System.out.println("Minimum Value= "+bestOptimizedValue);
    }
    
}
