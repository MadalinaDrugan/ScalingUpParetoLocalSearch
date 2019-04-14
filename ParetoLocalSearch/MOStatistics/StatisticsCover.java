/**
 *
 * @author Madalina Drugan 2010-2018
 * - THe main file for statistics
 */

package MOStatistics;

import general.*;
import java.util.*;
import MOInstance_QAPs.*;
import Archives.*;

public class StatisticsCover implements Statistics{

    /* Type of statistics */
    //statistics for a single NDA: improvements, distance, pls
    private NDAStatistics stat1; 
    private boolean NDAStat = true;

    // distances between NDAS: current vs previous,
    //private DistanceNDAs stat2;
    //private boolean distStat = true;

    /*statistics when a best solution is known*/
    //private boolean bestFile = false;
    //private boolean bestKnownSolution = false;

    //private boolean flagT = false;
    private boolean deactivFlag = false;
    private int currentIndex;
    private String fileN;

    private ProblemInstance problem;
    private Variator var;
    private ArchiveSolutions parents;
    //private ArchiveSolutions children;

    private final static java.util.Random r = new java.util.Random();
    //what type of metrics we use for distance
    //0- distance between the clothest points in an NDA are measured
    //1 - distances between all the points in an NDA are measured
    
    private Stack<Object[]> bestSolution = new Stack<>();
    private Stack<Object[]> bestObjective = new Stack<>();
    private ArchiveSolutions bestNDA;
    //private boolean bestA[];


    public StatisticsCover(Variator v, ProblemInstance p, int curr, String file, boolean deactiv){ //, PerturbatorStrategies e){
        problem = p;
        var = v;

        currentIndex = curr;
        fileN = file;

        deactivFlag = deactiv;

        //stat1 = new NDAStatistics(v,p,curr,file,deactiv);
        //stat2 = new DistanceNDAs(v,p,curr,file);
    }

    @Override
    public void setStatistics(boolean flags[], boolean bestOrNot){
        if(flags.length >= 0){
            NDAStat = flags[0];
            if(NDAStat){
                stat1 = new NDAStatistics(var, problem, currentIndex, fileN,deactivFlag); //,e);
            }
        }


    }

    @Override
    public void bestFile(String fileI){
        bestNDA = problem.bestFile(fileI); //,epsilon);
        Iterator<Solution> iterator = bestNDA.iterator();
        while(iterator.hasNext()){
            Solution s = iterator.next();
            bestObjective.add(s.objectives);
            bestSolution.add(s.items);
        }

    }

    @Override
    public void bestSolution(){
        if(bestNDA == null || bestNDA.size() == 0){
            // assume that the best solution is 12..n
            Integer[] items = new Integer[this.problem.numberOfFacilities];
            for(int i = 0; i < items.length; i++){
                items[i] = i;
            }
            bestSolution.add(items);
            //Long[] objectives = new Long[this.problem.numberOfObjectives];
            Long[] objectives = (Long[]) problem.computeSolution(items);
            bestObjective.add(objectives);
            if(bestNDA == null){
                bestNDA = new NonDominatedArchive();
            }
            bestNDA.add(new Solution_QAPs(objectives,items));
        }
    }

    @Override
    public void setBestType(int typeBest, String fileI){
        if(typeBest == 1){
            bestFile(fileI);
        } else if(typeBest == 2){
            bestSolution();
        }
        
        if(NDAStat && (bestNDA != null || bestNDA.size() != 0)){
            stat1.bestNDA = bestNDA;
            //stat1.bestObjective = bestObjective;
            //stat1.bestSolution = bestSolution;
        }


    }

    @Override
    public void writeStatistics(){
        if(NDAStat) {
            stat1.writeStatistics();
        }


    }
    
    /*public void writeStatisticsRun(){
        if(NDAStat) {
            stat1.writeStatisticsRun();
        }

        if(distStat) {
            stat2.writeStatisticsRun();
        }

        if(varStat) {
            stat3.writeStatisticsRun();
        }
    }*/
    
    @Override
    public void writeStatisticsTime(){
        if(NDAStat) {
            stat1.writeStatisticsTime();
        }

 
    }

    @Override
    public void collectStatistics(){
        if(NDAStat) {
            stat1.collectStatistics();
        }


    }
    
    private ArchiveSolutions currentArchive;
    private ArchiveSolutions previousArchive;
    @Override
    public void computeStatisticsTime(int currentIndex){
        if(NDAStat) {
            stat1.computeStatisticsTime(currentIndex);
        }

        previousArchive = currentArchive;
       

    }

    public void restart(){
        if(NDAStat) {
            stat1.restart();
        }

    }
    
    @Override
    public void closeFiles(){
        if(NDAStat) {
            stat1.closeFiles();
        }

    }
    
    @Override
    public void setNDAs(ArchiveSolutions current, boolean flag){
        if(NDAStat) {
            stat1.setNDAs(current, flag);
        }

            parents = current.clone();
       
    }

    @Override
    public void setNDAs(ArchiveSolutions current, Solution p, ArchiveSolutions currentNDA, boolean flag){
        if(NDAStat){
            stat1.setNDAs(current, flag);
            stat1.p = p;
            stat1.currentNDA = currentNDA;
            //stat1.parents = parents;
        }

        parents = current.clone();
   }

    @Override
    public void setReference(double[] refP){
    }

    @Override
    public void setDeactivNumber(long deactivNr){
    }
}
