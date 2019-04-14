package general;

import general.Genetic.PerturbatorStrategies;
import general.Scalarization.ArchiveScalSolutions;
import general.Reduction.ArchiveRedSolutions;
//import QAPs_optimal.AvlTree;
//only for two objectives???!!!???
import java.util.*;

public interface ArchiveSolutions extends ArchiveScalSolutions, ArchiveRedSolutions{
        
   //public abstract void dominatedListAdd(Solution s);

   public boolean add(Solution s);
   public Solution getNthSolution(int N);
        
   public Solution getRandomNotVisitedSolution();
   public Vector<Solution[]> getDistanceSol(int mut_rate);

   public boolean contains(Solution s);
   public boolean contains(ArchiveSolutions nda);

   //////////////////////////////////
   // order relationships
   ////////////////////////
   public boolean incomparable (Solution s, PerturbatorStrategies epsilon2);
   public boolean incomparable (ArchiveSolutions nda, PerturbatorStrategies epsilon2);
   //public ArchiveSolutions incomparable(ArchiveSolutions nda);

   //////////////////////////////
   // revise the definition of domination
   //true -- avl dominated s
   //false -- avl do not dominates s
   public boolean dominates (Solution s);
   public boolean dominates (ArchiveSolutions nda);

   public boolean dominatesAtLeastOne (Solution s);
   public boolean dominatesAtLeastOne(ArchiveSolutions nda);
   //public abstract boolean dominates (Solution s, PerturbatorStrategies epsilon2);
   //public abstract boolean dominates (ArchiveSolutions nda, PerturbatorStrategies epsilon2);

   //true -- avl is dominated by s
   // false -- avl is not dominated by s
   public boolean isDominated(Solution s);
   public boolean isDominated (ArchiveSolutions nda);

   /////////////////////////////
   // remove a solution s that is found as beening sominated
   ////////////////////////////
   public void remove(Solution s);

    //check the consistency of the tree
    public void checkConsistency(Solution s);
    //constructs a new archive that is not dominated
        //optimize
    public ArchiveSolutions clone();
        
    //constructs two new archive: that is not dominated [0]
    //and [1] that is dominating
    public ArchiveSolutions[] getNonAndDominated(Solution s);
    public ArchiveSolutions[] getNonAndDominated(ArchiveSolutions s);

    public Stack<Solution> getDominated(Solution s);

    //public abstract double getCoveredArea(double originX, double originY);
    public abstract ArchiveSolutions overlap(ArchiveSolutions nda);
    public ArchiveSolutions overlapInValue(ArchiveSolutions  nda);
    
    public abstract void setNDA(ArchiveSolutions nda);
    public abstract TreeMap<Long,Solution> getNDA();
	
    public void add(ArchiveSolutions newNDA);

    public void reset();
        
    public Iterator<Solution> iterator();
        
    public int size();

    public void setTypeProblem(boolean minimization);

    public void update(ArchiveSolutions newNDA);
    public int sizeDominated();

    public int typeArchive();

    public String ndaToString();
    public Solution[] ndaToArray();

}
