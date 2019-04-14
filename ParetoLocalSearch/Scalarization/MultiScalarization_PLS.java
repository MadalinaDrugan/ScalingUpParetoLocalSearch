/*
@author Madalina Drugan: 2010-2018 
* The standard PLS
 */

package Scalarization;

import general.Genetic.*;
import PLS.*;
import general.*;
import general.Scalarization.*;
import java.util.*;
import Archives.NonDominatedArchive;
import MOInstance_QAPs.Persistence_Solution_QAPs;
import Scalarization.Sets.Iterator_S;

public class MultiScalarization_PLS extends MultiRest_PLS{

    private final static long serialVersionUID = 11239585071342354L;//10795; //
    
    private Variator var;
    private ProblemInstance problem;
    private Statistics s;
    private ImplementPLS local;
    private Stopping stop;
     private boolean deactiv = false;

    private Solution p;
    
    private ArchiveSolutions previousNDAs;
    private ArchiveSolutions newNDA;

    private long[] tempD;
    
    private boolean activateStatistics = false;
    // memory of past peaks
    public boolean memory = false;
    
    private PerformanceInterface perf;

    private Solution[] s1;
    private Solution[] s2;
    
    private Scalarizer scal;
    private Iterator_S setScal;
    
    //private GeneticScalarizer genetic;
    private GeneticOperators thisOperators;
    private SelectMatingChildren mating;
  
    @Override
    public void setPerformance(PerformanceInterface perf){
        this.perf = perf;
    }

    public MultiScalarization_PLS(){
    }

    @Override
    public void setVariator(Variator v, ProblemInstance p, GeneticOperators g, ImplementPLS d, Stopping st, SelectMatingChildren m, boolean deac){ //, PerturbatorStrategies e){
        var = v;
        problem = p;
        local = d;
        thisOperators = g;
        stop = st;
        deactiv = deac;
        
        //epsilon = e;
        //previousNDAs = Persistence_Solution_QAPs.generateSolution(); //epsilon);
        newNDA = Persistence_Solution_QAPs.generateSolution(); //epsilon);

        mating = m;
    }

    public void setVariator(Variator v, ProblemInstance p, GeneticOperators g, ImplementPLS d, Stopping st, SelectMatingChildren m, Iterator_S s){ //, PerturbatorStrategies e){
        var = v;
        problem = p;
        local = d;
        thisOperators = g;
        stop = st;
        deactiv = false;
        
        setScal = s;
        //genetic = gen;
        
        newNDA = new NonDominatedArchive(s); //.add(Persistence_Solution_QAPs.generateSolution()); //epsilon);
        previousNDAs = new NonDominatedArchive(s);

        mating = m;
    }

    @Override public boolean stopCriteria(){
        return stop.stopCriteria();
    }

    @Override public void setStatistics(Statistics stat){
        s = stat;
    }

    @Override public void activateStatistics(boolean statA){
        activateStatistics = statA;
    }

    @Override public void setMemory(boolean mem){
        memory = mem;
    }

    @Override public void initStopCriteria(){
         tempD = Arrays.copyOf(var.getNumberOfPLSToOutput(), var.getNumberOfPLSToOutput().length);
    }

    @Override public Solution randomSolution(){
        p = problem.init(var);
        return p;
    }
    
    @Override public void update(Solution p){
        //if memory add this NDA in memory
        previousNDAs.reset();
        //for(int i = 0; i < s1.length; i++){
        previousNDAs.add(p);
            //}
    }
    
    @Override public Solution[] restartingSolutions(ArchiveSolutions nda){
        
       // chose the type of operation
       thisOperators.getPerturbator().chosePerturbator();
            // chose one or two parents;
       s1 = thisOperators.getParents(nda,mating);
       if(s1 == null){
          return null;
       }
       
       // perturbate it: mutate it
       s2 = thisOperators.perturbator(s1, var);
       if(s2 == null){
           return null;
       }
       
       return s2;
    }
    
    @Override public Solution[] getParents(){
        return s1;
    }
            
    private Iterator_S tempIS;
    private Scalarizer sc;
    //private ArchiveSolutions[] manyNDAs;
    
    @Override public ArchiveSolutions PLS_procedure(Solution p, ArchiveSolutions nda){
            //only mutation here
             //,epsilon);
            
        newNDA.reset();
        if(deactiv){
                newNDA  = nda.getNonAndDominated(p)[0];
                s.setDeactivNumber(newNDA.size());
        } 
              
        if(setScal != null){
            
            tempIS.restartIterator();  
            while(!tempIS.endIterator()){ 
                scal = tempIS.iterator();
                newNDA.reset();
                newNDA.add(p);
                newNDA = local.localSearch(newNDA, var,scal);
                // it is a good scalarizer as well
                if(newNDA.dominates(nda) || newNDA.incomparable(nda, null)){
                    nda.add(newNDA); 
              
                }
            }
            
        } else {            
                newNDA.add(p);
                newNDA = local.localSearch(newNDA, var);
        }
            
           nda.add(newNDA);  
                
            
            return nda;
    }
    
    @Override public void statistics(Solution p, ArchiveSolutions nda){
            int currentIndex =  var.getCurrentNumberOfNDAOutput();
            if(currentIndex < tempD.length-1){
              if(tempD[currentIndex] < stop.writeCriteria()){
                var.setTotalNDA(currentIndex, nda);
                var.writeCurrentNumberOfNDAOutput(nda,currentIndex);
                s.setNDAs(newNDA,p,nda, true);
                s.collectStatistics();

                s.computeStatisticsTime(currentIndex);
                var.setCurrentNumberOfNDAOutput(currentIndex+1);

              } else if(this.activateStatistics){
                s.setNDAs(newNDA,p,nda, false);
                s.collectStatistics();
              }
            } else if(this.activateStatistics){
                s.setNDAs(newNDA,p,nda, false);
                s.collectStatistics();                
            }
    }
    
    @Override public void statisticsFinal(ArchiveSolutions nda){
           int currentIndex =  var.getCurrentNumberOfNDAOutput();
           if(currentIndex == tempD.length-1){
               //currentIndex--;
                var.setTotalNDA(currentIndex, nda);
                var.writeCurrentNumberOfNDAOutput(nda,currentIndex);
                s.computeStatisticsTime(currentIndex);
                var.setCurrentNumberOfNDAOutput(currentIndex+1);
           }else {
               System.err.println("error in currentIndex "+ currentIndex);
                var.setTotalNDA(currentIndex, nda);
                var.writeCurrentNumberOfNDAOutput(nda,currentIndex);
                s.computeStatisticsTime(currentIndex);
                var.setCurrentNumberOfNDAOutput(currentIndex+1);
           }
           thisOperators.restart();
    }
    
    @Override public ArchiveSolutions getNext(ArchiveSolutions  nda){

         initStopCriteria();
         //if(deactiv)
          //   ((NDAStatistics) s).deactivFlag = true;
         while(stop.stopCriteria()){

            p = randomSolution();
            //nda = 
            PLS_procedure(p,nda);
            
            statistics(p,nda);
            
           }

         statisticsFinal(nda);
         return nda;
    }
    
}
