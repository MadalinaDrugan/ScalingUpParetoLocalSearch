/*
 * To change this template, choose Tools | Templates
 * and open the template itn the editor.
 */

package PLS;
import Archives.NonDominatedArchive;
import MOStatistics.*;
import general.*;
import general.Reduction.*;
import general.Scalarization.*;
/**
 *
 * @author madalina
 */
public class myPLS_sameStart extends Algorithm implements java.io.Serializable{
   
   public NonDominatedArchive restartsArchive;
   public ArchiveSolutions newArchive;
   public String outputFile;

   //public PerturbatorStrategies epsilon;
   SetScalarizer scal;
   SetReductions red;
  
   public myPLS_sameStart(Variator var, String outputFile,  int currentIndex,ProblemInstance p, boolean deactiv){
       //this.epsilon = epsilon;
       //currentArchive.setEpsilon(epsilon);
       this.restartsNr = 1;
       //if(sc != null){
       //   currentArchive = new NonDominatedArchive(sc); //epsilon); 
       //   restartsArchive = new NonDominatedArchive(sc); 
       //} else { 
          currentArchive = new NonDominatedArchive(); //epsilon);
          restartsArchive = new NonDominatedArchive(); //epsilon);
       //}
       //restartsArchive.setEpsilon(epsilon);
       numberOfIterations = 0;
       this.outputFile = outputFile;
       this.var = var;
       this.p = p;
       s = new StatisticsCover(var,p,currentIndex,outputFile,deactiv); //,epsilon);

       //scal = sc;
   }

   public myPLS_sameStart(Variator var, String outputFile,  int currentIndex,ProblemInstance p, SetScalarizer sc, boolean deactiv){
       //this.epsilon = epsilon;
       //currentArchive.setEpsilon(epsilon);
       this.restartsNr = 1;
       if(sc != null){
          currentArchive = new NonDominatedArchive(sc); //epsilon); 
          restartsArchive = new NonDominatedArchive(sc); 
       } else { 
          currentArchive = new NonDominatedArchive(); //epsilon);
          restartsArchive = new NonDominatedArchive(); //epsilon);
       }
       //restartsArchive.setEpsilon(epsilon);
       numberOfIterations = 0;
       this.outputFile = outputFile;
       this.var = var;
       this.p = p;
       s = new StatisticsCover(var,p,currentIndex,outputFile,deactiv); //,epsilon);

       scal = sc;
   }

   public myPLS_sameStart(Variator var, String outputFile,  int currentIndex,ProblemInstance p, String inputBest, SetScalarizer sc, boolean deactiv){
       //this.epsilon = epsilon;
       //currentArchive.setEpsilon(epsilon);
       this.restartsNr = 1;
       if(sc != null){
          currentArchive = new NonDominatedArchive(sc); //epsilon); 
          restartsArchive = new NonDominatedArchive(sc); 
       } else { 
           currentArchive = new NonDominatedArchive(); //epsilon);
           restartsArchive = new NonDominatedArchive(); //epsilon);
       }
       //restartsArchive.setEpsilon(epsilon);
       numberOfIterations = 0;
       this.outputFile = outputFile;
        this.var = var;
       this.p = p;
       s = new StatisticsCover(var,p,currentIndex,outputFile,deactiv); //,epsilon);

       scal = sc;
   }

   public myPLS_sameStart(Variator var, String outputFile,  int currentIndex,ProblemInstance p, SetReductions sc, boolean deactiv){
       //this.epsilon = epsilon;
       //currentArchive.setEpsilon(epsilon);
       this.restartsNr = 1;
       if(sc != null){
          currentArchive = new NonDominatedArchive(sc,sc.genetic()); //epsilon); 
          restartsArchive = new NonDominatedArchive(sc,sc.genetic()); 
       } else { 
          currentArchive = new NonDominatedArchive(); //epsilon);
          restartsArchive = new NonDominatedArchive(); //epsilon);
       }
       //restartsArchive.setEpsilon(epsilon);
       numberOfIterations = 0;
       this.outputFile = outputFile;
       this.var = var;
       this.p = p;
       s = new StatisticsCover(var,p,currentIndex,outputFile,deactiv); //,epsilon);

       red = sc;
   }

   public myPLS_sameStart(Variator var, String outputFile,  int currentIndex,ProblemInstance p, String inputBest, SetReductions sc, boolean deactiv){
       //this.epsilon = epsilon;
       //currentArchive.setEpsilon(epsilon);
       this.restartsNr = 1;
       if(sc != null){
          currentArchive = new NonDominatedArchive(sc,sc.genetic()); //epsilon); 
          restartsArchive = new NonDominatedArchive(sc,sc.genetic()); 
       } else { 
           currentArchive = new NonDominatedArchive(); //epsilon);
           restartsArchive = new NonDominatedArchive(); //epsilon);
       }
       //restartsArchive.setEpsilon(epsilon);
       numberOfIterations = 0;
       this.outputFile = outputFile;
        this.var = var;
       this.p = p;
       s = new StatisticsCover(var,p,currentIndex,outputFile,deactiv); //,epsilon);

       red = sc;
   }

   public void setTypeStatistics(){

   }

   public void setReference(double[] refP){
    s.setReference(refP);
   }

   public myPLS_sameStart(){
       currentArchive = new NonDominatedArchive(); //epsilon);
       restartsArchive = new NonDominatedArchive(); //epsilon);
       numberOfIterations = 0;
   }
   
    @Override
   public void restarts(){
       //this.totalNDA[0].addItem(currentArchive);
       if(scal != null){
           currentArchive = new NonDominatedArchive(scal);
       } else if(red != null) {
            currentArchive = new NonDominatedArchive(red,red.genetic()); //epsilon);
       } else {
            currentArchive = new NonDominatedArchive(); //epsilon);
       }
       //restartsArchive = new NonDominatedArchive();
       numberOfIterations = 0;
       numberOfSwaps = 0;
       var.restart();
       s.restart();
   }

    @Override
   public void run(){
       if(var == null | p == null) {
            return;
        }
     // for(int i = 0; i < restarts; i++){        
       var.setNumberOfSwaps(this.numberOfSwaps);

       newArchive = var.getNext(s);
       currentArchive.add(newArchive);
       restartsArchive.add(currentArchive);
       numberOfSwaps = var.getNumberOfSwaps();

       //s.computeStatisticsRun();
       s.writeStatistics();

       p.writeToFile(outputFile, restartsArchive, true);
       p.writeNDA(outputFile,var.getTotalNDA()[var.getNumberOfPLSToOutput().length-1]);
      
       //p.writeToFileHV(outputFile, restartsArchive);
       //p.computeSolution(currentArchive);
       //QAPs.writeToFile(outputFile, currentArchive, true);
       System.out.println("Number of solutions" + var.identfOperator() + currentArchive.size() + " number of swaps " + numberOfSwaps);            
      //}
   }           
   //public void start(){
   //    run();
   //}
   
   public void collectStatistics(){
      s.writeStatisticsTime();
      p.writeNDA(outputFile,var.getTotalNDA()[var.getNumberOfPLSToOutput().length-1]);
      s.closeFiles();
   }
   
   @Override
   public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Describe the parento optimal archive \n");
                sb.append(currentArchive.toString());
                
                sb.append("Size archive:" + currentArchive.size() + " \n");
                sb.append("Number of iterations" + numberOfIterations+ "\n");
		return sb.toString();
	}	
	
}