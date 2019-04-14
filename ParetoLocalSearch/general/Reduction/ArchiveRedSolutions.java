package general.Reduction;

//import QAPs_optimal.AvlTree;
//only for two objectives???!!!???
import general.*;

public interface ArchiveRedSolutions{
        
   //public abstract void dominatedListAdd(Solution s);

   //public boolean add(Solution s, Reduction sc);
   public boolean addSolInRed(Solution s, int indentif);
   //public boolean addSolution(Reduction sc);
   public boolean addReduction(int i); //Reduction sc);

   public ArchiveSolutions getReduction(Reduction sc);
   public ArchiveSolutions getReduction(int indentif);

   public void setReduction(int indentif);
   public void setReduction(Reduction sc);

   //public boolean add(Reduction sc);
   public boolean containsSolInRed(Solution s, int index);

   public boolean isDominatedReduction(Solution s, int index);
   //public boolean isDominated(ArchiveScalSolutions nda, Scalarizer sc);

   public boolean dominatesReduction (Solution s, int index);
   //public boolean dominates (ArchiveScalSolutions nda, Scalarizer sc);

   public boolean incomparableReduction(Solution s, int index);
   //public boolean incomparable(ArchiveScalSolutions nda, Scalarizer sc);
   
   public Solution getRandomSolutionInRed(int indexR);
}
