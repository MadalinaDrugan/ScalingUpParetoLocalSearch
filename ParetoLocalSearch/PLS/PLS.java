/*
 * The stochatic PLS: the main framework
 */

package PLS;

import general.Genetic.GeneticOperators;
import general.*;
import Archives.*;
import GeneticPLS.Exploration.Bitstring.Iter_NK;
import GeneticPLS.Iter_PLS;
import MOInstance_NK.NK;
import PLS.LocalSearch.One_PLS;
import Scalarization.Sets.RandomScal;
import general.Scalarization.*;
import general.Reduction.*;

/**
 * Operators for PLS_restart
 * @author madalina
 */
public class PLS implements Variator{
    
    private long numberOfSwaps = 0;
    private long maxNumberOfSwaps = 10000;           

    private long initialSwaps; 

    //Permutation p;
    private ProblemInstance problem;
    
    private StochasticPLS thisPLS;
    private StochasticPLS initPLS;

    private final static long serialVersionUID = 11239585071342354L;//10795; //
    private final static java.util.Random r = new java.util.Random();
           
    //private int sizeJump; //how far an individual jumps
    private long numberOfTrials = 10; // how many trials outside the attractor are made
    //private int nrIterations = 1000;

    private long initialSolutions = 0; //number of solutions in the initial random population

   // private int dezactivNrSteps = 5;

    private long[] numberOfPLSToOutput;
    private ArchiveSolutions[] totalNDA = new NonDominatedArchive[10];
    private boolean converged = false;
    private int currentNDAIndex = 0;
    private String outputFile;
    
    private ArchiveSolutions  currentNDA;
    //private ArchiveSolutions  previousNDA;
    private ArchiveSolutions  newNDA;

    private int PLSDeepness = 0;
    private int PLSsize;
    private double PLSsizePerLS =0;
    private long nrPLS = 0;

    //PerturbatorStrategies epsilon;
    
    //scalarizer
    SetScalarizer scal;
    SetReductions red;
    
    public PLS(long trialsS, long maxS, String outputF, StochasticPLS thisP, StochasticPLS initP, ProblemInstance p, GeneticOperators g, ImplementPLS d, long initSol){ //, PerturbatorStrategies epsilon){
        numberOfTrials = trialsS;
        maxNumberOfSwaps = maxS;
        initialSolutions = initSol;
        problem = p;

        currentNDA = new NonDominatedArchive();//epsilon);
        newNDA = new NonDominatedArchive(); //epsilon);

        thisPLS = thisP;
        initPLS = initP; //new Init_PLS();

        numberOfPLSToOutput = new long[(int)numberOfTrials];
        totalNDA = new NonDominatedArchive[(int)numberOfTrials];
        for(int i = 0; i < this.numberOfPLSToOutput.length; i++) {
             totalNDA[i] = new NonDominatedArchive();
        } //epsilon);
        outputFile = new String(outputF);
        setNumberOfNDAToOutput(numberOfPLSToOutput.length, maxS);
        currentNDAIndex = 0;
     }

    public PLS(long trialsS, long maxS, String outputF, StochasticPLS thisP, ProblemInstance p, GeneticOperators g, ImplementPLS d, long initSol){ 
        numberOfTrials = trialsS;
        maxNumberOfSwaps = maxS;
        initialSolutions = initSol;
        problem = p;

        currentNDA = new NonDominatedArchive();//epsilon);
        newNDA = new NonDominatedArchive(); //epsilon);

        thisPLS = thisP;
        initPLS = null; //new Init_PLS();

        numberOfPLSToOutput = new long[(int)numberOfTrials];
        totalNDA = new NonDominatedArchive[(int)numberOfTrials];
        for(int i = 0; i < this.numberOfPLSToOutput.length; i++) {
             totalNDA[i] = new NonDominatedArchive();
        } //epsilon);
        outputFile = new String(outputF);
        setNumberOfNDAToOutput(numberOfPLSToOutput.length, maxS);
        currentNDAIndex = 0;
     }

    //PLS with option of chosing thisPLS; useful in multirestart PLS
    public PLS(long trialsS, long maxS, String outputF, StochasticPLS thisP, StochasticPLS initP, ProblemInstance p, GeneticOperators g, SetScalarizer sc, ImplementPLS d, long initSol){ //, PerturbatorStrategies epsilon){
        numberOfTrials = trialsS;
        maxNumberOfSwaps = maxS;
        initialSolutions = initSol;
        problem = p;

        //this.epsilon = epsilon;

        currentNDA = new NonDominatedArchive();//epsilon);
        //previousNDA = new NonDominatedArchive(); //epsilon);
        newNDA = new NonDominatedArchive(); //epsilon);

        thisPLS = thisP;
        initPLS = initP; //new Init_PLS();
        //initPLS.setVariator(this, problem, g, d,new StopingCriteria_Nr_PLS(this));
        //initPLS.activateStatistics(false);

        numberOfPLSToOutput = new long[(int)numberOfTrials];
        totalNDA = new NonDominatedArchive[(int)numberOfTrials];
        //p = new Permutation(problem.numberOfFacilitiesbr);
        for(int i = 0; i < this.numberOfPLSToOutput.length; i++) {
            if(sc != null){
                totalNDA[i] = new NonDominatedArchive(sc);
            } else {
                totalNDA[i] = new NonDominatedArchive();
            }
        } //epsilon);
        outputFile = new String(outputF);
        setNumberOfNDAToOutput(numberOfPLSToOutput.length, maxS);
        currentNDAIndex = 0;

        scal = sc;
     }

    //PLS with option of chosing thisPLS; useful in multirestart PLS
    public PLS(long trialsS, long maxS, String outputF, StochasticPLS thisP, ProblemInstance p, GeneticOperators g, SetScalarizer sc, ImplementPLS d){ //, PerturbatorStrategies epsilon){
        
        numberOfTrials = trialsS;
        maxNumberOfSwaps = maxS;
        initialSolutions = 0;
        problem = p;

        //this.epsilon = epsilon;

        //currentNDA = new NonDominatedArchive(epsilon);
        //previousNDA = new NonDominatedArchive(epsilon);
        //newNDA = new NonDominatedArchive(epsilon);

        thisPLS = thisP;
        initPLS = null; //new Init_PLS();
        //initPLS.setVariator(this, problem, g, d,new StopingCriteria_Nr_PLS(this));
        //initPLS.activateStatistics(false);

        numberOfPLSToOutput = new long[(int)numberOfTrials];
        totalNDA = new NonDominatedArchive[(int)numberOfTrials];
        //p = new Permutation(problem.numberOfFacilitiesbr);
        for(int i = 0; i < this.numberOfPLSToOutput.length; i++) {
            if(sc != null){
                totalNDA[i] = new NonDominatedArchive(sc);
            } else {
                totalNDA[i] = new NonDominatedArchive();
            }
        } //epsilon);
        outputFile = new String(outputF);
        setNumberOfNDAToOutput(numberOfPLSToOutput.length, maxS);
        currentNDAIndex = 0;

        scal = sc;
     }

    //PLS with option of chosing thisPLS; useful in multirestart PLS
    public PLS(long trialsS, long maxS, String outputF, StochasticPLS thisP, StochasticPLS initP, ProblemInstance p, GeneticOperators g, SetReductions sc, ImplementPLS d, long initSol){ //, PerturbatorStrategies epsilon){
        numberOfTrials = trialsS;
        maxNumberOfSwaps = maxS;
        initialSolutions = initSol;
        problem = p;

        //this.epsilon = epsilon;

        if(sc != null){
            currentNDA = new NonDominatedArchive(sc,sc.genetic());//epsilon);
            newNDA = new NonDominatedArchive(sc,sc.genetic()); //epsilon);
        } else {
            currentNDA = new NonDominatedArchive();
            newNDA = new NonDominatedArchive();            
        }
        thisPLS = thisP;
        initPLS = initP; //new Init_PLS();
        //initPLS.setVariator(this, problem, g, d,new StopingCriteria_Nr_PLS(this));
        //initPLS.activateStatistics(false);

        numberOfPLSToOutput = new long[(int)numberOfTrials];
        totalNDA = new NonDominatedArchive[(int)numberOfTrials];
        //p = new Permutation(problem.numberOfFacilitiesbr);
        for(int i = 0; i < this.numberOfPLSToOutput.length; i++) {
            if(sc != null){
                totalNDA[i] = new NonDominatedArchive(sc,sc.genetic());
            } else {
                totalNDA[i] = new NonDominatedArchive();
            }
        } //epsilon);
        outputFile = outputF;
        setNumberOfNDAToOutput(numberOfPLSToOutput.length, maxS);
        currentNDAIndex = 0;

        red = sc;
     }

    //PLS with option of chosing thisPLS; useful in multirestart PLS
    public PLS(long trialsS, long maxS, String outputF, StochasticPLS thisP, ProblemInstance p, GeneticOperators g, SetReductions sc, ImplementPLS d){ //, PerturbatorStrategies epsilon){
        
        numberOfTrials = trialsS;
        maxNumberOfSwaps = maxS;
        initialSolutions = 0;
        problem = p;

        //this.epsilon = epsilon;

        //currentNDA = new NonDominatedArchive(epsilon);
        //previousNDA = new NonDominatedArchive(epsilon);
        //newNDA = new NonDominatedArchive(epsilon);

        thisPLS = thisP;
        initPLS = null; //new Init_PLS();
        //initPLS.setVariator(this, problem, g, d,new StopingCriteria_Nr_PLS(this));
        //initPLS.activateStatistics(false);

        numberOfPLSToOutput = new long[(int)numberOfTrials];
        totalNDA = new NonDominatedArchive[(int)numberOfTrials];
        //p = new Permutation(problem.numberOfFacilitiesbr);
        for(int i = 0; i < this.numberOfPLSToOutput.length; i++) {
            if(sc != null){
                totalNDA[i] = new NonDominatedArchive(sc,sc.genetic());
            } else {
                totalNDA[i] = new NonDominatedArchive();
            }
        } //epsilon);
        outputFile = outputF;
        setNumberOfNDAToOutput(numberOfPLSToOutput.length, maxS);
        currentNDAIndex = 0;

        red = sc;
     }

    @Override
    public void setStatistics(Statistics stat){
        thisPLS.setStatistics(stat);
        if(initPLS != null) {
            initPLS.setStatistics(stat);
        }
    }

    public PLS(){
        problem = null;
        thisPLS = null;
    }

    @Override
    public void setNumberOfPLS(long otherPLS){
        nrPLS = otherPLS;
    }
    
    @Override
    public long getNumberOfPLS(){
        return nrPLS;
    }

    @Override
    public long getInitPLS(){
        return this.initialSolutions;
    }

    @Override
    public ProblemInstance getProblem(){
        return problem;
    }
    @Override
    public void setProblem(ProblemInstance p){
        problem = p;
    }

    @Override
    public void writeCurrentNumberOfNDAOutput(ArchiveSolutions  nda, int c){
        problem.writeToFile(outputFile+".temp"+String.valueOf(c), nda, true);
    }

    @Override
    public void writeTotalNDA(){
        for(int index = 0; index < totalNDA.length; index++) {
            problem.writeToFile(outputFile+".totalNDA", totalNDA[index], true);
        }
    }

    @Override
    public void writeFinalNDAOutput(ArchiveSolutions  nda){
        problem.writeToFile(outputFile+".temp_F", nda, true);
    }

    @Override
    public void setNumberOfNDAToOutput(int i, long end){
       if(i != totalNDA.length){
           this.numberOfPLSToOutput = new long[i];
           totalNDA = new NonDominatedArchive[i];
           for(int j = 0; j < i; j++){
               totalNDA[j] = new NonDominatedArchive(); //epsilon);
               this.numberOfPLSToOutput[j] = end/i*(j+1);
           }
       } else {
           for(int j =0; j < i; j++) {
                this.numberOfPLSToOutput[j] = end/i*(j+1);
            }
       }
   }

    @Override
   public void setNumberOfNDAToOutput(long[] outputC){
       if(this.numberOfPLSToOutput.length == outputC.length){
        for(int i = 0; i < outputC.length; i++) {
                this.numberOfPLSToOutput[i] = outputC[i];
            }
       } else {
        numberOfPLSToOutput = new long[outputC.length];
        System.arraycopy(outputC, 0, this.numberOfPLSToOutput, 0, outputC.length);
       }
    }
    
    @Override
    public int getCurrentNumberOfNDAOutput(){
        return currentNDAIndex;
    }

    @Override
    public void setCurrentNumberOfNDAOutput(int c){
        currentNDAIndex = c;
    }
    
    @Override
    public void setTotalNDA(int currentIndex, ArchiveSolutions  nda){
            totalNDA[currentIndex].add(nda);
    }
    
    @Override
    public String identfOperator(){
        return new String(" multistart best PLS ");
    }

    public void restart(){
        problem.restarts();
        numberOfSwaps = 0;
        this.converged = false;
        this.currentNDAIndex = 0;
        //numberOfSwaps += problem.numberOfFacilities;
        nrPLS = 0;
        PLSDeepness = 0;
        PLSsize = 0;
        PLSsizePerLS = 0;
    }

    @Override
    public ArchiveSolutions  getNext(Statistics s){

        if(red != null){
            currentNDA = new NonDominatedArchive(red,red.genetic());
            newNDA = new NonDominatedArchive(red,red.genetic());            
        } else {
            currentNDA = new NonDominatedArchive();//epsilon);
            newNDA = new NonDominatedArchive(); //epsilon);
        }
        
        //previousNDA.setEpsilon(epsilon);
        //newNDA.setEpsilon(epsilon);
        //currentNDA.setEpsilon(epsilon);

        //p = problem.init(this);
        if(initPLS!= null){
            newNDA.add(problem.init(this)); //,epsilon));
            currentNDA = initPLS.getNext(newNDA);
        } else {
            currentNDA.add(problem.init(this));
        } //,epsilon));
        
            currentNDA = thisPLS.getNext(currentNDA);
        //if(initPLS.stopCriteria())
        writeFinalNDAOutput(currentNDA);
        return currentNDA;
    }
    
    @Override
    public long getNumberOfSwaps(){
        return this.numberOfSwaps;
    }
    
    @Override
    public long getNumberUpdatedOfSwaps(){
        return this.numberOfSwaps - this.initialSwaps;
    }

    @Override
    public long getMaxNumberOfSwaps(){
        return this.maxNumberOfSwaps;
    }           
 
    @Override
    public long setNumberOfSwaps(long otherSwaps){
        this.numberOfSwaps = otherSwaps;
        return this.numberOfSwaps;
    }

    @Override
    public long resetNrOfSwaps(){
        this.numberOfSwaps = 0;
        return this.numberOfSwaps;
    }

    @Override
    public long setMaxNumberOfSwaps(long otherSwaps){
        this.maxNumberOfSwaps = otherSwaps;
        setNumberOfNDAToOutput(numberOfPLSToOutput.length, maxNumberOfSwaps);
        return this.maxNumberOfSwaps;
    }
      
    @Override
    public long[] getNumberOfPLSToOutput(){
        return this.numberOfPLSToOutput;
    } 
    
    @Override
    public void setNumberOfPLSToOutput(long[] PLSToOutput){
        if(this.numberOfPLSToOutput.length != PLSToOutput.length)
            this.numberOfPLSToOutput = new long[PLSToOutput.length];
        for(int i = 0; i < this.numberOfPLSToOutput.length; i++)
            this.numberOfPLSToOutput[i] = PLSToOutput[i]; 
    }

    @Override
    public long getMaximPLSToOutput(){
        return numberOfPLSToOutput[numberOfPLSToOutput.length-1];
    }

    @Override
    public boolean isConverged(){
        return this.converged;
    }
    
    @Override
    public void resetConverge(){
        converged = false;
    }
    
    @Override
    public ArchiveSolutions [] getTotalNDA(){
        return this.totalNDA;
    }
    
    @Override
    public void resetTotalNDA(){
        for(int i = 0; i < this.totalNDA.length; i++){
            if(red != null){
                totalNDA[i] = new NonDominatedArchive(red,red.genetic());
            } else {
                totalNDA[i] = new NonDominatedArchive(); //epsilon);
            }
        }
    }

    @Override
    public int getQAPType(){
        return problem.getType();
    }

    @Override
    public void setNrInitialSolutions(long initialSolution){
        this.initialSolutions = initialSolution;
    }

    @Override
    public void setDeepLevel(int level){
        PLSDeepness = level;
    }

    @Override
    public double getDeepLevel(){
        return (double)PLSDeepness/(double)nrPLS;
    }

    @Override
    public double getSizeNDA(){
        return (double)PLSsize/(double)nrPLS;
    }
    
    @Override
    public double getSizePerLS(){
        return PLSsizePerLS/nrPLS;
    }

    @Override
    public void addDeepLevel(int level, long sizeNDA){
        PLSDeepness += level;
        nrPLS++;
        PLSsize += sizeNDA;
        PLSsizePerLS += sizeNDA/level;
    }

    @Override
    public long updateNrSwaps(long nr){
        this.numberOfSwaps += nr;
        return this.numberOfSwaps;
    }

    //public PerturbatorStrategies getEpsilon(){
    //    return epsilon;
    //}
}
