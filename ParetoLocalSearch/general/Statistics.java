/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general;

//import java.util.Stack;
//import java.util.Arrays;
//import java.util.TreeMap;
/**
 *
 * @author madalina
 */
public interface Statistics {

    //private ProblemInstance problem;
    //public Variator var;
    //public Solution p;

    //public ArchiveSolutions parents;
    //public ArchiveSolutions children;
    //public ArchiveSolutions currentNDA;
    
    public boolean twoParents = false; //two

    public abstract void writeStatistics();
    //public abstract void writeStatisticsRun();
    public abstract void writeStatisticsTime();

    public abstract void collectStatistics();
    //public abstract void computeStatisticsRun();
    public abstract void computeStatisticsTime(int currentIndex);

    public abstract void restart();
    
    public abstract void closeFiles();
    public abstract void setNDAs(ArchiveSolutions children, boolean flag);
    public abstract void setNDAs(ArchiveSolutions children, Solution p, ArchiveSolutions currentNDA, boolean flag);
    public abstract void setStatistics(boolean[] flags, boolean bestOrNot);
    public abstract void setBestType(int typeBest, String fileI);

    public abstract void setReference(double[] refP);
    public abstract void setDeactivNumber(long deactivNr);

    public abstract void bestFile(String fileI);
    public abstract void bestSolution();
}
