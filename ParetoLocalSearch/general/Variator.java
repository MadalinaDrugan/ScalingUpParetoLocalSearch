/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general;

//import general.Scalarization.ArchiveScalSolutions;
/**
 *
 * @author madalina
 */
public interface Variator{
    
    public long getNumberOfSwaps();
    public long getMaxNumberOfSwaps();
    public long resetNrOfSwaps();

    public abstract long setNumberOfSwaps(long otherSwaps);
    public abstract long setMaxNumberOfSwaps(long otherSwaps);
    public abstract long getNumberUpdatedOfSwaps();
    
    public void setNrInitialSolutions(long initialSolution);

    public long[] getNumberOfPLSToOutput(); 
    public void setNumberOfPLSToOutput(long[] PLSToOutput);
    public boolean isConverged();
    public void resetConverge();
    public ArchiveSolutions[] getTotalNDA();
    public void resetTotalNDA();
    public void setTotalNDA(int currentIndex, ArchiveSolutions nda);
    public void setNumberOfNDAToOutput(int i, long end); 
    public void setNumberOfNDAToOutput(long[] outputC); 
    public int getCurrentNumberOfNDAOutput();
    public void setCurrentNumberOfNDAOutput(int c);   
    public long getMaximPLSToOutput();

    public void writeCurrentNumberOfNDAOutput(ArchiveSolutions nda, int c);
    public void writeTotalNDA(); 
    public void writeFinalNDAOutput(ArchiveSolutions nda);

    public void setDeepLevel(int level);
    public double getDeepLevel();
    public double getSizeNDA();
    public double getSizePerLS();
    public void addDeepLevel(int level, long sizeNDA);    

    public abstract void setNumberOfPLS(long otherPLS);
    public abstract long getNumberOfPLS();
    public abstract long getInitPLS();

    public ArchiveSolutions getNext(Statistics s);

    public void restart();
    
    public String identfOperator();
    
    public int getQAPType();

    public ProblemInstance getProblem();
    public void setProblem(ProblemInstance p);

    public long updateNrSwaps(long nr);

    public void setStatistics(Statistics stat);

    //public PerturbatorStrategies getEpsilon();
    
}
