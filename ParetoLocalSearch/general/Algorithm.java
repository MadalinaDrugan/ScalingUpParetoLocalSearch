/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general;

//import general.Scalarization.ArchiveScalSolutions
/**
 *
 * @author madalina
 */
public abstract class Algorithm implements Runnable{
    
   public long maxSizeArchive = 300000;
   public int restartsNr = 1000; 
   public int numberOfIterations = 0;
   
   public ProblemInstance p;
   public Variator var;
   public Statistics s;
   
   public ArchiveSolutions currentArchive;
 
   public long numberOfSwaps = 0;
   //public long maxNumberOfSwaps;
   //public long initialSolutions = 0;

   //public abstract void run(Variator var, ProblemInstance p, Statistics s);

   //public abstract void run();
   
   public abstract void restarts();
   //public abstract void collectStatistics(Statistics s);            
}
