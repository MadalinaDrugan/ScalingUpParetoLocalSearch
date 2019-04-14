/*
 * Interface for all the genetic operators
 */

package general.Genetic;

//import java.util.Vector;

import general.ArchiveSolutions;
import general.ProblemInstance;
import general.Solution;
import general.Variator;


/**
 *
 * @author madalina
 */
public interface GeneticOperators {
    
    public abstract Solution[] perturbator(Solution[] s, Variator var);
    public abstract Solution[] perturbator(Solution[] s, ProblemInstance p);
    public abstract void setVariator( ProblemInstance p, PerturbatorStrategies stat);
    public abstract int getNrParents();
    public Solution[] getParents(ArchiveSolutions nda, SelectMatingChildren set);
    //public Solution[] getParents(ArchiveSolutions nda, Vector past);
    public abstract void setPerturbatorType(int operatorType);
    public abstract PerturbatorStrategies getPerturbator();
    public abstract boolean adaptation();
    public abstract void restart();
}
