/*
 * @author Madalina Drugan: 2010-2018
* The standard PLS
 */

package PLS;

import Archives.NonDominatedArchive;
import MOInstance_QAPs.Persistence_Solution_QAPs;
import general.*;
import general.Genetic.*;
import general.Reduction.*;
import general.Scalarization.*;
import java.util.*;


public class MultiRest_PLS implements StochasticPLS{

    private final static long serialVersionUID = 11239585071342354L;//10795; //
    
    private Variator var;
    private ProblemInstance problem;
    private Statistics s;
    private ImplementPLS local;
    private GeneticOperators thisOperators;
    private Stopping stop;
    private SelectMatingChildren mating;
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
    private SetScalarizer setScal;
    
    private Reduction red;
    private SetReductions setRed;
    
    public void setPerformance(PerformanceInterface perf){
        this.perf = perf;
    }

    public MultiRest_PLS(){
    }

    @Override
    public void setVariator(Variator v, ProblemInstance p, GeneticOperators g, ImplementPLS d, Stopping st, SelectMatingChildren m, boolean deac){ //, PerturbatorStrategies e){
        var = v;
        problem = p;
        local = d;
        thisOperators = g;
        stop = st;
        deactiv = deac;
        
        newNDA = Persistence_Solution_QAPs.generateSolution(); //epsilon);
        previousNDAs = new NonDominatedArchive();
        mating = m;

    }

    @Override
    public void setVariator(Variator v, ProblemInstance p, GeneticOperators g, ImplementPLS d, Stopping st, SelectMatingChildren m, SetScalarizer s){ //, PerturbatorStrategies e){
        var = v;
        problem = p;
        local = d;
        thisOperators = g;
        stop = st;
        deactiv = false;
        
        setScal =s;
        
        newNDA = new NonDominatedArchive(s); //.add(Persistence_Solution_QAPs.generateSolution()); //epsilon);
        previousNDAs = new NonDominatedArchive(s);

        mating = m;
    }

    @Override
    public void setVariator(Variator v, ProblemInstance p, GeneticOperators g, ImplementPLS d, Stopping st, SelectMatingChildren m, SetReductions s){ //, PerturbatorStrategies e){
        var = v;
        problem = p;
        local = d;
        thisOperators = g;
        stop = st;
        deactiv = false;
        
        setRed =s;
        //geneticR = gen;
        
        newNDA = new NonDominatedArchive(s,s.genetic()); //.add(Persistence_Solution_QAPs.generateSolution()); //epsilon);
        previousNDAs = new NonDominatedArchive(s,s.genetic());

        mating = m;
    }

    @Override
    public boolean stopCriteria(){
        return stop.stopCriteria();
    }

    @Override
    public void setStatistics(Statistics stat){
        s = stat;
    }

    @Override
    public void activateStatistics(boolean statA){
        activateStatistics = statA;
    }

    @Override
    public void setMemory(boolean mem){
        memory = mem;
    }

    @Override
    public void initStopCriteria(){
         tempD = Arrays.copyOf(var.getNumberOfPLSToOutput(), var.getNumberOfPLSToOutput().length);
    }

    @Override
    public Solution randomSolution(){
        p = problem.init(var);
        return p;
    }
    
    public void update(Solution p){
        //if memory add this NDA in memory
        previousNDAs.reset();
        //for(int i = 0; i < s1.length; i++){
        previousNDAs.add(p);
            //}
    }
    
    @Override
    public Solution[] restartingSolutions(ArchiveSolutions nda){
        
       // chose the type of operation
       thisOperators.getPerturbator().chosePerturbator();
            
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
    
    @Override
    public Solution[] getParents(){
        return s1;
    }
            
    @Override
    public ArchiveSolutions PLS_procedure(Solution p, ArchiveSolutions nda){
            newNDA.reset();
            update(p);
            if(deactiv){
                newNDA  = nda.getNonAndDominated(p)[0];
                s.setDeactivNumber(newNDA.size());
            } 
    
            /////////////////////////
            // scalarized search
            if(setScal != null){
                
                if(setScal.solutionGen()){
                    if(((SolutionScalarizer) setScal).getNrSolutions() <= nda.size()){
                        scal = ((SolutionScalarizer) setScal).makeScalarizer(nda.ndaToArray())[0]; 
                    } else {
                        scal = setScal.getScalarizer();
                    }
                } else {
                    setScal.chooseScalarizer();
                    scal = setScal.getScalarizer();
                 }
                
                newNDA.add(p);
                newNDA = local.localSearch(newNDA, var,scal);

                
       
            } else if(setRed != null){
                ////////////////////
                //reduced search
                
                //if(geneticR != null && geneticR.getNrSolutions() <= nda.size() && geneticR.getNrParents() < setRed.size()){
                //    setRed = geneticR.makeReduction(setRed, nda.ndaToArray());                        
                //}
                if(setRed.genetic() && !setRed.solutionGen()){
                
                    // generate a new scalarizer with genetic operators from other reductions
                    ((GeneticReduction) setRed).makeReduction();  
                    newNDA.add(p);
                    newNDA.addSolInRed(p, 0);
                    newNDA = local.localSearchReduction(newNDA, var, 0);   
                    ((GeneticReduction) setRed).adaptation(previousNDAs, newNDA,nda,perf);
                    newNDA.addReduction(0);
                } else if(setRed.solutionGen()){
                    // generate reduction with genetic operators from solutions
                    
                    ((SolutionReduction) setRed).makeReduction(nda.ndaToArray()); 
                    
                    newNDA.add(p);
                    newNDA.addSolInRed(p, 0);
                    newNDA = local.localSearchReduction(newNDA, var, 0); 
                                                        
                } else {
                    int indexRed = setRed.chooseReduction();
                    //newNDA.add(p);
                    //int indexRed = setRed.getCurrentIndex();
                    newNDA.addSolInRed(p, indexRed);
                    newNDA = local.localSearchReduction(newNDA, var, indexRed); 
                    newNDA.addReduction(indexRed);
                 }            
                
            } else {            
                ////////////////////
                // search in the objective space
                
                newNDA.add(p);
                newNDA = local.localSearch(newNDA, var);
            }
                        
            nda.add(newNDA);  
                
            //var.setNumberOfPLS(var.getNumberOfPLS()+1);
            
            return nda;
    }
    
    @Override
    public void statistics(Solution p, ArchiveSolutions nda){
            int currentIndex =  var.getCurrentNumberOfNDAOutput();
            if(currentIndex < tempD.length-1){
              if(tempD[currentIndex] <= stop.writeCriteria()){
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
    
    @Override
    public void statisticsFinal(ArchiveSolutions nda){
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
