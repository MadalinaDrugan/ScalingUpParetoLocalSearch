package general.Scalarization;

import general.*;

public interface ArchiveScalSolutions{
        
   //public abstract void dominatedListAdd(Solution s);

   public boolean add(Solution s, Scalarizer sc);
   public boolean addSolInScal(Solution s, int indentif);
   public boolean addSolution(Scalarizer sc);

   public Solution get(Scalarizer sc);
   public Solution get(int indentif);

   public void setScalarizer(int indentif);
   public void setScalarizer(Scalarizer sc);

   public boolean add(Scalarizer sc);

   public boolean isDominated(Solution s, Scalarizer sc);
   //public boolean isDominated(ArchiveScalSolutions nda, Scalarizer sc);

   public boolean dominates (Solution s, Scalarizer sc);
   //public boolean dominates (ArchiveScalSolutions nda, Scalarizer sc);

   public boolean incomparable(Solution s, Scalarizer sc);
   //public boolean incomparable(ArchiveScalSolutions nda, Scalarizer sc);

}
