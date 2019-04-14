/*
 * @author Madalina Drugan: 2010-2018
 */

package general;

public abstract class ProblemInstance {
    

        public int numberOfFacilities;
        public int numberOfObjectives;

        public abstract Solution init(Variator var); //, PerturbatorStrategies e);
       
        public abstract Solution computeSolution(Solution s1);
        public abstract ArchiveSolutions computeSolution(ArchiveSolutions nda);
        public abstract Object[] computeSolution(Object[] items);
      
        public abstract void restarts();
        public abstract void writeToFile(String nameFile, ArchiveSolutions nda, boolean append);
        //public abstract void writeToFileHV(String nameFile, ArchiveSolutions nda);
        public abstract void writeNDA(String outputFile, ArchiveSolutions totalNDA);
        
        public abstract int getType();
        public abstract Solution generateSolutions(); //PerturbatorStrategies e);
        public abstract ArchiveSolutions bestFile(String fileI); //, PerturbatorStrategies e);

}       
 
