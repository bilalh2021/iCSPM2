/*
CS10 CS with POLM mutation 
 */
package distributedcuckoopol;

import java.util.Random;

public class CS10 {

    public int n;// Number of the host nests 
    public int d;// number of solutions in the solution vector 
    private final double LB;
    private final double UB;
    private final double LO;
    private final double UO;
    private final double alpha = 1;
    public double pa; //A fraction (pa) of worse nests
    public int MaxGeneration;// MaxGeneration
    public double[][] population;
    public double[] evaluation;
    public double[] cuckoo;
    public double[] temp;
    public double[] shift;
    public double[] sphere1;
    int f;  // funvtion number
    final static double MUTATION_RATE = 0.05; // probability of mutation
    private static Random m_rand = new Random();  // random-number generator
    double bestEvaluation = Double.MAX_VALUE;

    CS10(int ff, int maxItr, int dimensiion, int numPopulation, double L, double U) {
        f = ff;
        n = numPopulation;
        d = dimensiion;
        LB = L;
        UB = U;
        LO = -80;
        UO = 80;
        MaxGeneration = maxItr;
        population = new double[n][d];
        evaluation = new double[n];
        cuckoo = new double[d];
        temp = new double[d];

        shift = new double[d];
        sphere1 = new double[d];
        initializePopulation();
    }

    public double search() {
        int j = 0;
        int iteration = 0;

        evaluateSolutions();
        while (iteration < MaxGeneration) {
            GenerateCuckoo();
            j = selectNest();
            if (evaluation[j] > evaluateCukoo()) {

                switchcuckoo(j);
            }

            sortSolutions();
            abandonNests();
           evaluateSolutions();
          sortSolutions();
          EOBL();
          evaluateSolutions();
          sortSolutions();
           // System.out.println("iteration" + iteration);
            bestSolution();
            iteration++;

        }
        return bestSolution();

    }
 public void EOBL(){
       int noElite=(int) Math.round(pa*n); 
       int count=0;
       double delta = new Random().nextDouble();

      while(noElite>0){
          double da= min(count);
          double db= max(count);
       for(int i=0;i<population[count].length;i++){
          temp[i]= delta*(da-db)-population[count][i];
          if (population[count][i]<LB || population[count][i]>UB)
            temp[i]=GenerateHost();  
       }
       double temp1;
       if (evaluateTemp()<evaluateFunction(count)){
       for (int j = 0; j < population[count].length; j++) {
           temp1= temp[j];
            population[count][j] = temp1;
        }
       }
             noElite--;
             count++;
         
      }    
  }
  public double min(int x){
      double smallest;
      smallest=Double.MAX_VALUE;
        for(int i=0;i<population[x].length;i++){
            if (population[x][i]<smallest)
             smallest=population[x][i];
       }
      return smallest;
  
  }
    public double max(int x){
      double biggest;
      biggest=Double.MIN_VALUE;
        for(int i=0;i<population[x].length;i++){
            if (population[x][i]>biggest)
             biggest=population[x][i];
       }
      return biggest;
  
  }
    public double bestSolution() {
        int bestNest = 0;

        for (int i = 0; i < evaluation.length; i++) {
            if (bestEvaluation > evaluation[i]) {
                bestEvaluation = evaluation[i];
                bestNest = i;
            }
        }
     //   System.out.println(bestNest + "\t" + bestEvaluation);
        return bestEvaluation;
    }

    public void abandonNests() {
        int abandonNest = (int) Math.round(pa * n);
        int count = population.length - 1;
        while (abandonNest > 0) {
            for (int i = 0; i < population[count].length; i++) {
                population[count][i] = GenerateHost();
            }
            abandonNest--;
            count--;
        }

    }

    public double POLM(int c1) {

        Random random2 = new Random();

        int selected1 = random2.nextInt(d);

        double delta1, delta2, deltaq, r, etam = 1, result = 0; // etam=1 means strong mutation
        delta1 = (population[c1][selected1] - LB) / (UB - LB);
        delta2 = (UB - population[c1][selected1]) / (UB - LB);
        r = random2.nextDouble();
        if (r <= 0.5) {
            deltaq = Math.pow(Math.abs(2 * r + (1 - 2 * r) * Math.pow((1 - delta1), etam + 1)), (1 / (etam - 1)) - 1);
        } else {
            deltaq = 1 - Math.pow(Math.abs(2 * (1 - r) + 2 * (r - 0.5) * Math.pow((1 - delta2), etam + 1)), (1 / (etam - 1)));
        }

        result = population[c1][selected1] + deltaq * (UB - LB);
        return result;
    }

    public void changeCuckoo(int index) {
        double temp = 1000000;

        for (int i = 0; i < population[index].length; i++) {

            population[index][i] = GenerateHost();

        }

    }

    public void evaluateSolutions() {
        for (int i = 0; i < evaluation.length; i++) {
            evaluation[i] = evaluateFunction(i);
        }
    }

    public void sortSolutions() {

        for (int i = 0; i < evaluation.length; i++) {
            for (int j = i + 1; j < evaluation.length; j++) {
                double temp1 = 0;
                double temp2[] = new double[d];
                if (evaluation[i] > evaluation[j]) {
                    temp1 = evaluation[i];
                    evaluation[i] = evaluation[j];
                    evaluation[j] = temp1;
                    //temp2 = population[i];
                    System.arraycopy(population[i], 0, temp2, 0, population[i].length);
                    //population[i] = population[j];
                    System.arraycopy(population[j], 0, population[i], 0, population[i].length);
                    //population[j] = temp2;
                    System.arraycopy(temp2, 0, population[i], 0, temp2.length);
                }
            }
        }

    }

 double evaluateFunction(int xi)
{ double result=0;
    switch (f) {
        case 1:
            result = sphere1(xi);
            break;
        case 2:
            result = Schwefel2_2_1(xi);
            break;
        case 3:
            result = step1(xi);
            break;
        case 4:
            result = Rosenbrock1(xi);
            break;
        case 5:
            result = rotated_hyper_ellipsoid1(xi);
            break;
        case 6:
            result = generalized_swefel1(xi);
            break;
        case 7:
            result = rastrigin1(xi);
            break;
        case 8:
            result = ackley1(xi);
            break;
        case 9:
            result = griewank1(xi);
            break;
        case 10:
            result = six_hump_camel_back1(xi);
            break;
        case 11:
            result = shiftedSphere1(xi);
            break;
        case 12:
            result = ShiftedSchwefel1(xi);
            break;
        case 13:
            result = shifted_rosenbrock1(xi);
            break;
        case 14:
            result = Shifted_Rastrigin1(xi);
            break;
        case 15:
            result = expandedf1_1(xi);
            break;
default:
System.out.println("invalid choice for function number!");

}
return result;

}
  
    double evaluateCukoo()
{ double result=0;
    switch (f) {
        case 1:
            result = sphere2();
            break;
        case 2:
            result = Schwefel2_2_2();
            break;
        case 3:
            result = step2();
            break;
        case 4:
            result = Rosenbrock2();
            break;
        case 5:
            result = rotated_hyper_ellipsoid2();
            break;
        case 6:
            result = generalized_swefel2();
            break;
        case 7:
            result = rastrigin2();
            break;
        case 8:
            result = ackley2();
            break;
        case 9:
            result = griewank2();
            break;
        case 10:
            result = six_hump_camel_back2();
            break;
        case 11:
            result = shiftedSphere2();
            break;
        case 12:
            result = ShiftedSchwefel2();
            break;
        case 13:
            result = shifted_rosenbrock2();
            break;
        case 14:
            result = Shifted_Rastrigin2();
            break;
        case 15:
            result = expandedf1_2();
            break;
        default:
            System.out.println("invalid choice for function number!");

}
return result;

}
    double evaluateTemp()
{ double result=0;
    switch (f) {
        case 1:
            result = sphere3();
            break;
        case 2:
            result = Schwefel2_2_3();
            break;
        case 3:
            result = step3();
            break;
        case 4:
            result = Rosenbrock3();
            break;
        case 5:
            result = rotated_hyper_ellipsoid3();
            break;
        case 6:
            result = generalized_swefel3();
            break;
        case 7:
            result = rastrigin3();
            break;
        case 8:
            result = ackley3();
            break;
        case 9:
            result = griewank3();
            break;
        case 10:
            result = six_hump_camel_back3();
            break;
        case 11:
            result = shiftedSphere3();
            break;
        case 12:
            result = ShiftedSchwefel3();
            break;
        case 13:
            result = shifted_rosenbrock3();
            break;
        case 14:
            result = Shifted_Rastrigin3();
            break;
        case 15:
            result = expandedf1_3();
            break;
        default:
            System.out.println("invalid choice for function number!");

}
return result;

}
      public double sphere1(int xi){

        double sum=0;
      for(int i=0;i<population[xi].length;i++){
          sum+=population[xi][i]*population[xi][i];
      }
      
      return sum;
  }
      public double sphere2(){
       
        double sum=0;
      for(int i=0;i<cuckoo.length;i++)
          sum+=cuckoo[i]*cuckoo[i];
      
      return sum;
  }
      public double sphere3(){
       
        double sum=0;
      for(int i=0;i<temp.length;i++)
          sum+=temp[i]*temp[i];
      
      return sum;
  }   
      public double Schwefel2_2_1 (int xi){

          double sum=0;
        double mult=1;
      for(int i=0;i<population[xi].length;i++){
         sum+=Math.abs(population[xi][i]);
          mult*=Math.abs(population[xi][i]);
      }
    return sum+mult;
    }
      public double Schwefel2_2_2(){
       
        double sum=0;
        double mult=1;
      for(int i=0;i<cuckoo.length;i++)
      {
          sum+=Math.abs(cuckoo[i]);
          mult*=Math.abs(cuckoo[i]);
      }
      return sum+mult;
  }
       public double Schwefel2_2_3(){
       
        double sum=0;
        double mult=1;
      for(int i=0;i<temp.length;i++)
      {
          sum+=Math.abs(temp[i]);
          mult*=Math.abs(temp[i]);
      }
      return sum+mult;
  }   
      public double step1(int xi){

        double sum=0;
      for(int i=0;i<population[xi].length;i++){
          sum+=Math.floor(population[xi][i]+0.5)*Math.floor(population[xi][i]+0.5);
      }
    return sum;
    }
      public double step2(){
       
        double sum=0;
      for(int i=0;i<cuckoo.length;i++)
      
          sum+=Math.floor(cuckoo[i]+0.5)*Math.floor(cuckoo[i]+0.5);
    
      return sum;
  }
     public double step3(){
       
        double sum=0;
      for(int i=0;i<temp.length;i++)
      
          sum+=Math.floor(temp[i]+0.5)*Math.floor(temp[i]+0.5);
    
      return sum;
  }   
     public double Rosenbrock1(int xi){

 double result = 0;
 double temp1=0;
  if (population[xi].length == 2) {
    result= 100 * ( Math.pow(population[xi][1]  -  Math.pow(population[xi][0],2) , 2) + Math.pow( population[xi][0]-1 , 2));
  }
  else{
    for (int i = 1;i< population[xi].length ;i++)
	  temp1 = result+100 * (  Math.pow(population[xi][i]  -   Math.pow(population[xi][i-1],2) , 2) +  Math.pow(population[xi][i-1] -1, 2));
 
     result=temp1+ result;
  }
  return result;
  }
      public double Rosenbrock2(){
       
  double result = 0;
   double temp1=0;
  if (cuckoo.length == 2) {
    result= 100 * ( Math.pow(cuckoo[1]  -  Math.pow(cuckoo[0],2) , 2) + Math.pow( cuckoo[0]-1 , 2));
  }
  else{
    for (int i = 1;i< cuckoo.length;i++)
	
     temp1=100 * (  Math.pow(cuckoo[i]  -   Math.pow(cuckoo[i-1],2) , 2) +  Math.pow(cuckoo[i-1] -1, 2));
    result= temp1+ result;
  }
  return result;
  }
      public double Rosenbrock3(){
       
  double result = 0;
 double temp1=0;
  if (temp.length == 2) {
    result= 100 * ( Math.pow(temp[1]  -  Math.pow(temp[0],2) , 2) + Math.pow( temp[0]-1 , 2));
  }
  else{
    for (int i = 1;i< temp.length;i++)
	  temp1 = result+100 * (  Math.pow(temp[i]  -   Math.pow(temp[i-1],2) , 2) +  Math.pow(temp[i-1] -1, 2));
  }       result=temp1+result;
  return result;
  }   
          public double rotated_hyper_ellipsoid1(int xi){

        double temp=0,sum=0;
      for(int i=0;i<population[xi].length;i++){
          for (int j=0;j<=i;j++){
          temp+=population[xi][j];
          }
          sum+=temp*temp;
      }
      
      return sum;
  }
      public double rotated_hyper_ellipsoid2(){
       
        double sum=0,temp=0;
      for(int i=0;i<cuckoo.length;i++){
               for (int j=0;j<=i;j++){
          temp+=cuckoo[j];
          }
          sum+=temp*temp;
      }
      return sum;
  }
    public double rotated_hyper_ellipsoid3(){
       
        double sum=0,temp1=0;
      for(int i=0;i<temp.length;i++){
               for (int j=0;j<=i;j++){
          temp1+=temp[j];
          }
          sum+=temp1*temp1;
      }
      return sum;
  }
       
               public double generalized_swefel1(int xi){
  int i ;
  double result_O, result2, Sum ;
  double result = 0;
  result2 = 0;
  result_O = 0;
  Sum=0;
  
    for (i = 0;i< population[xi].length;i++){
        result_O = Math.sqrt(Math.abs(population[xi][i]));
        result2 = Math.sin(result_O);
        result_O = population[xi][i] * result2;
        Sum = Sum + result_O;
	}
    
    result= -1* Sum;
    return result;
  }
          public double generalized_swefel2(){
   int i ;
  double result_O, result2, Sum ;
  double result = 0;
  result2 = 0;
  result_O = 0;
  Sum=0;
  
    for (i = 0;i<cuckoo.length;i++){
        result_O = Math.sqrt(Math.abs(cuckoo[i]));
        result2 = Math.sin(result_O);
        result_O = cuckoo[i] * result2;
        Sum = Sum + result_O;
	}
    
    result= -1  * Sum;
    return result;
  }
     public double generalized_swefel3(){
   int i ;
  double result_O, result2, Sum ;
  double result = 0;
  result2 = 0;
  result_O = 0;
  Sum=0;
  
    for (i = 0;i<temp.length;i++){
        result_O = Math.sqrt(Math.abs(temp[i]));
        result2 = Math.sin(result_O);
        result_O = temp[i] * result2;
        Sum = Sum + result_O;
	}
    
    result=  -1* Sum;
    return result;
  }
                
          public double rastrigin1(int xi) {

        double sum = 0;
        for (int i = 0; i < population[xi].length; i++) {
     sum +=Math.pow(population[xi][i], 2) -10*Math.cos(2*Math.PI*population[xi][i]);

        }
        return 10*d+sum;
    }


      public double rastrigin2(){
       
        double sum=0;
     
      for(int i=0;i<cuckoo.length;i++)
      {
            sum +=Math.pow(cuckoo[i], 2) -10*Math.cos(2*Math.PI*cuckoo[i]);
        
      }
          return 10*d+sum;
  }
           public double rastrigin3(){
       
        double sum=0;
     
      for(int i=0;i<temp.length;i++)
      {
            sum +=Math.pow(temp[i], 2) -10*Math.cos(2*Math.PI*temp[i]);
        
      }
          return 10*d+sum;
  }
   public double ackley1(int xi){
  
  double result2;
  double result = 0;
  result2 = 0;
    for (int i = 0; i< population[xi].length; i++){
        result = result + (Math.pow(population[xi][i], 2));
        result2 = result2 + Math.cos(2 * Math.PI * population[xi][i]);
	}
    
    result= (-20 * Math.exp(-0.2 * Math.sqrt((1.0 / population[xi].length) * (result)))) - (Math.exp((1.0 / population[xi].length) * result2)) + 20 + Math.exp(1.0);
    return result;
  }
      public double ackley2(){
  double result2;
  double result = 0;
  result2 = 0;
    for (int i = 0; i< cuckoo.length; i++){
        result = result + (Math.pow(cuckoo[i], 2));
        result2 = result2 + Math.cos(2 * Math.PI * cuckoo[i]);
	}
    
    result= (-20 * Math.exp(-0.2 * Math.sqrt((1.0 / cuckoo.length) * (result)))) - (Math.exp((1.0 / cuckoo.length) * result2)) + 20 + Math.exp(1.0);
    return result;
  } 
          public double ackley3(){
  double result2;
  double result = 0;
  result2 = 0;
    for (int i = 0; i< temp.length; i++){
        result = result + (Math.pow(temp[i], 2));
        result2 = result2 + Math.cos(2 * Math.PI * temp[i]);
	}
    
    result= (-20 * Math.exp(-0.2 * Math.sqrt((1.0 / temp.length) * (result)))) - (Math.exp((1.0 / temp.length) * result2)) + 20 + Math.exp(1.0);
    return result;
  }
  public double griewank1(int xi){

   double result=0;
  double result1, result2,val;
  result1 = 0;
  result2 = 1;
  
  if (population[xi].length == 1)
    result= ((1.0 / 4000) * (Math.pow(population[xi][0], 2))) - (Math.cos(population[xi][0] / Math.sqrt(1.0))) + 1;
  else{
    for (int i = 0; i< population[xi].length; i++){
	  val =i+1;
      result1 = result1 + (Math.pow(population[xi][i], 2));
      result2 = result2 * (Math.cos(population[xi][i] / Math.sqrt(val)));
	}
    
    result= ((1.0 / 4000) * (result1)) - result2 + 1;
    
  }
  return result;
  }
      public double griewank2(){
      double result=0;
  double result1, result2,val;
  result1 = 0;
  result2 = 1;
  
  if (cuckoo.length == 1)
    result= ((1.0 / 4000) * (Math.pow(cuckoo[0], 2))) - (Math.cos(cuckoo[0] / Math.sqrt(1.0))) + 1;
  else{
    for (int i = 0; i< cuckoo.length; i++){
	  val =i+1;
      result1 = result1 + (Math.pow(cuckoo[i], 2));
      result2 = result2 * (Math.cos(cuckoo[i] / Math.sqrt(val)));
	}
    
    result= ((1.0 / 4000) * (result1)) - result2 + 1;
    
  }
  return result;
  }  
            public double griewank3(){
      double result=0;
  double result1, result2,val;
  result1 = 0;
  result2 = 1;
  
  if (temp.length == 1)
    result= ((1.0 / 4000) * (Math.pow(temp[0], 2))) - (Math.cos(temp[0] / Math.sqrt(1.0))) + 1;
  else{
    for (int i = 0; i< temp.length; i++){
	  val =i+1;
      result1 = result1 + (Math.pow(temp[i], 2));
      result2 = result2 * (Math.cos(temp[i] / Math.sqrt(val)));
	}
    
    result= ((1.0 / 4000) * (result1)) - result2 + 1;
    
  }
  return result;
  }  
        public double six_hump_camel_back1(int xi){
        double result=0;

  if (population[xi].length  == 2){
    result = (4 * (Math.pow(population[xi][0], 2))) - (2.1 * (Math.pow(population[xi] [0], 4))) + (1.0 / 3 * (Math.pow(population[xi] [0], 6))) + (population[xi] [0] * population[xi] [1]) - (4 * (Math.pow(population[xi] [1], 2))) + (4 * (Math.pow(population[xi] [1], 4)));
 
  }
    return result;
    } 
      public double six_hump_camel_back2(){
        double result=0;

  if (cuckoo.length  == 2){
    result = (4 * (Math.pow(cuckoo[0], 2))) - (2.1 * (Math.pow(cuckoo [0], 4))) + (1.0 / 3 * (Math.pow(cuckoo[0], 6))) + (cuckoo[0] * cuckoo [1]) - (4 * (Math.pow(cuckoo [1], 2))) + (4 * (Math.pow(cuckoo[1], 4)));
 
  }
    return result;
  }  
       public double six_hump_camel_back3(){
        double result=0;

  if (temp.length  == 2){
    result = (4 * (Math.pow(temp[0], 2))) - (2.1 * (Math.pow(temp [0], 4))) + (1.0 / 3 * (Math.pow(temp[0], 6))) + (temp[0] * temp [1]) - (4 * (Math.pow(temp [1], 2))) + (4 * (Math.pow(temp[1], 4)));
 
  }
    return result;
  }  
          public double shiftedSphere1(int xi){

        double sum=0,F_bias=-450,z=0;
  GenerateShift();
      
      for(int i=0;i<population[xi].length;i++){
          
          z=population[xi][i]-shift[i];
          sum+=z*z;
      }
       
    
      
      return sum+F_bias;
  }
      public double shiftedSphere2(){
       
       double sum=0,F_bias=-450,z=0;
  GenerateShift();
      
      for(int i=0;i<cuckoo.length;i++){
          
          z=cuckoo[i]-shift[i];
          sum+=z*z;
      }
       
      return sum+F_bias;
  }
           public double shiftedSphere3(){
       
       double sum=0,F_bias=-450,z=0;
  GenerateShift();
      
      for(int i=0;i<temp.length;i++){
          
          z=temp[i]-shift[i];
          sum+=z*z;
      }
       
      return sum+F_bias;
  }   
    public double ShiftedSchwefel1(int xi){
        double sum0=0,sum1=0,z=0,F_bias=-450;
      
      for(int i=0;i<population[xi].length;i++){
           for(int j=0;j<i;j++){
          z=population[xi][i]-shift[i];
          sum0+=z;
      }
           sum0=sum0*sum0;
           sum1+=sum0;
           sum0=0;
           z=0;
      }  
      return sum1+F_bias;
  }
      
        public double ShiftedSchwefel2(){

           double sum0=0,sum1=0,z=0,F_bias=-450;
      
      for(int i=0;i<cuckoo.length;i++){
           for(int j=0;j<i;j++){
          z=cuckoo[i]-shift[i];
          sum0+=z;
      }
            sum0=sum0*sum0;
           sum1+=sum0;
           sum0=0;
           z=0;
      }  
      return sum1+F_bias;
  }  
     public double ShiftedSchwefel3(){

           double sum0=0,sum1=0,z=0,F_bias=-450;
      
      for(int i=0;i<temp.length;i++){
           for(int j=0;j<i;j++){
          z=temp[i]-shift[i];
          sum0+=z;
      }
            sum0=sum0*sum0;
           sum1+=sum0;
           sum0=0;
           z=0;
      }  
      return sum1+F_bias;
  }      
            public double shifted_rosenbrock1(int xi){
 int i ;
 double  z, z_before, fbias ;
 double result = 0;
 fbias = 390;

    for (i = 1;i< population[xi].length;i++){
        z = population[xi][i] - sphere1[i] + 1;
        z_before = population[xi][i-1] - sphere1[i - 1] + 1;
        result = result + (100 * (Math.pow((Math.pow(z_before , 2) - z) , 2)) + Math.pow((z_before - 1) , 2));
	}
    
    result = result + fbias;
    
    return result;
  }
      public double shifted_rosenbrock2(){
 int i ;
 double  z, z_before, fbias ;
 double result = 0;
 fbias = 390;

    for (i = 1;i< cuckoo.length;i++){
        z = cuckoo[i] - sphere1[i] + 1;
        z_before = cuckoo[i-1] - sphere1[i - 1] + 1;
        result = result + (100 * (Math.pow((Math.pow(z_before , 2) - z) , 2)) + Math.pow((z_before - 1) , 2));
	}
    
    result = result + fbias;
    
    return result;
  }
          public double shifted_rosenbrock3(){
 int i ;
 double  z, z_before, fbias ;
 double result = 0;
 fbias = 390;

    for (i = 1;i< temp.length;i++){
        z = temp[i] - sphere1[i] + 1;
        z_before = temp[i-1] - sphere1[i - 1] + 1;
        result = result + (100 * (Math.pow((Math.pow(z_before , 2) - z) , 2)) + Math.pow((z_before - 1) , 2));
	}
    
    result = result + fbias;
    
    return result;
  }
          public double Shifted_Rastrigin1(int xi){
  int  i;
  double  fbias, z ;
  
 double result = 0;
  fbias = -330;
  

    for (i = 0;i< population[xi].length;i++){
        z = population[xi][i] - sphere1[i];
        result = result + ((Math.pow(z , 2)) - (10 * Math.cos(2 * Math.PI * z)) + 10);
	}
    
    result = result + -330;
    
    return result;   
  }
      public double Shifted_Rastrigin2(){
  int  i;
  double  fbias, z ;
  
 double result = 0;
  fbias = -330;
  

    for (i = 0;i< cuckoo.length;i++){
        z = cuckoo[i] - sphere1[i];
        result = result + ((Math.pow(z , 2)) - (10 * Math.cos(2 * Math.PI * z)) + 10);
	}
    
    result = result + -330;
    
    return result;
  }
     public double Shifted_Rastrigin3(){
  int  i;
  double  fbias, z ;
  
 double result = 0;
  fbias = -330;
  

    for (i = 0;i< temp.length;i++){
        z = temp[i] - sphere1[i];
        result = result + ((Math.pow(z , 2)) - (10 * Math.cos(2 * Math.PI * z)) + 10);
	}
    
    result = result + -330;
    
    return result;
  }
        
      public double expandedf1_1(int xi) 
{
double [] sphere1=new double[d];
double F_bias=-130,lower=-5,upper=5;
double[] m_z= new double[d];;
double[] m_o= new double[d];;
     for (int i = 0; i < m_o.length; i++) {
      m_o[i] -= lower+((upper-lower)*Math.random());
    }
     for (int i = 0; i < m_o.length; i++) {
      m_z[i] = population[xi][i]-m_o[i]+1 ;
    }
 double result = 0;
 

  for (int i = 1; i < population[xi].length; i++) { 
      result += F8(F2(m_z[i - 1], m_z[i])); 
    } 
    result += F8(F2(population[xi][d-1], population[xi][0]));
    
    result = result + F_bias;
    
    return result; 
} 
            public double expandedf1_2() 
{
double [] sphere1=new double[cuckoo.length];
double F_bias=-130,lower=-5,upper=5;
double[] m_z= new double[cuckoo.length];;
double[] m_o= new double[cuckoo.length];;
     for (int i = 0; i < m_o.length; i++) {
      m_o[i] -= lower+((upper-lower)*Math.random());
    }
     for (int i = 0; i < m_o.length; i++) {
      m_z[i] = cuckoo[i]-m_o[i]+1 ;
    }
 double result = 0;
   int n=cuckoo.length;

  for (int i = 1; i < cuckoo.length; i++) { 
      result += F8(F2(m_z[i - 1], m_z[i])); 
    } 
    result += F8(F2(cuckoo[cuckoo.length - 1], cuckoo[0]));
    
    result = result + F_bias;
    
    return result; 
} 
            
             public double expandedf1_3() 
{
double [] sphere1=new double[temp.length];
double F_bias=-130,lower=-5,upper=5;
double[] m_z= new double[temp.length];;
double[] m_o= new double[temp.length];;
     for (int i = 0; i < m_o.length; i++) {
      m_o[i] -= lower+((upper-lower)*Math.random());
    }
     for (int i = 0; i < m_o.length; i++) {
      m_z[i] = temp[i]-m_o[i]+1 ;
    }
 double result = 0;
   int n=temp.length;

  for (int i = 1; i < temp.length; i++) { 
      result += F8(F2(m_z[i - 1], m_z[i])); 
    } 
    result += F8(F2(temp[temp.length - 1], temp[0]));
    
    result = result + F_bias;
    
    return result; 
} 
static public double F8(double x) { 
    return (((x * x) / 4000.0) - Math.cos(x) + 1.0); 
  } 
static public double F2(double x, double y) { 
    double temp1 = (x * x) - y; 
    double temp2 = x - 1.0; 
    return ((100.0 * temp1 * temp1) + (temp2 * temp2)); 
  } 
        public double GenerateShift(){

double random = new Random().nextDouble();
double result = LO + (random * (UO - LO));
      
     return result; 
    }

    public void initializePopulation() {
        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < population[i].length; j++) {
                population[i][j] = GenerateHost();
            }
        }
        for (int j = 0; j < shift.length; j++) {
            shift[j] = GenerateHost();
        }
        double rnd = new Random().nextDouble();
        for (int i = 0; i < sphere1.length; i++) {
            sphere1[i] = LB + (rnd * (UB - LB));
        }
    }

    public void switchcuckoo(int i) {
        double temp;
        for (int j = 0; j < population[i].length; j++) {
            temp = cuckoo[j];
            population[i][j] = temp;
        }
    }


    public int selectNest() {

        Random ran = new Random();
        int selected = ran.nextInt(n);

        return selected;
    }

    public void GenerateCuckoo() {
        Random random2 = new Random();
        int selected = random2.nextInt(n);
        double temp = 1000000;
        int numberofAttemps = 1000;
        Random random = new Random();
        for (int i = 0; i < population[selected].length; i++) {
            while (numberofAttemps >= 0 && !(temp >= LB && temp <= UB)) {
                if (random.nextDouble() < MUTATION_RATE) {
                    temp = POLM(selected);
                    cuckoo[i] = temp;
                }
                numberofAttemps--;
            }
        }
    }

    public double GenerateHost() {
        double start = -5.12;
        double end = 5.12;
        double random = new Random().nextDouble();
        double result = start + (random * (end - start));
        return result;
    }
     double[][] migrateBest(int n){
  sortSolutions();
         double migrants[][]=new double[n][d];
         for(int i=0;i<n;i++){
		for(int j=0;j<d;j++)
		{migrants[i][j]=population[i][j];}
    
         }
	    return migrants;
    }

    void replaceWorst(double[][] migrants) {
        int n = population.length - 1;
        int m = migrants.length - 1;
        int c = 0;
        while (c <= m) {
            for (int i = n; i >= n - m; i--) {
                for (int j = 0; j < d; j++) {
                    population[i][j] = migrants[c][j];
                }

            }
            c++;
        }
    }
    public void print() {
        for (int c = 0; c < population.length; c++) {
            System.out.println("p[" + c + "]= " + population[c]);
            System.out.println("E[" + c + "]= " + evaluation[c]);
        }
    }
};
