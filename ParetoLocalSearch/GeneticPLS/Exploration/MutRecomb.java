/*
 * @author Madalina Drugan: 2010-2018
 */

package GeneticPLS.Exploration;

import general.*;
import general.Genetic.*;
import java.util.Stack;

/**
 *
 * @author madalina
 */
public class MutRecomb implements GeneticOperators{
   
   private ProblemInstance problem;
   
   //vectors for the components of the recombination
   int[] tempItem1; // = new Vector<Integer>();
   int[] tempItem2; // = new Vector<Integer>();

   int[] cycles;
   Stack<int[]> cyclePosition = new Stack<>();
   Stack<Integer> tempArray = new Stack<>();

   Solution[] results = new Solution[2];
   private PerturbatorStrategies strat;

   private GeneticOperators recomb;
   private GeneticOperators iter;

   //private Solution s1;
   private int typeOperator = 1; //recomb
   public MutRecomb(GeneticOperators rec, GeneticOperators it){
       recomb = rec;
       iter = it;
   }
      
   private final static long serialVersionUID = 11239585071342354L;//10795; //
   private final static java.util.Random r = new java.util.Random();

   @Override public void setVariator(ProblemInstance p, PerturbatorStrategies stat){
       problem = p;
       cycles = new int[problem.numberOfFacilities];
       tempItem1 = new int[problem.numberOfFacilities];
       tempItem2 = new int[problem.numberOfFacilities];

       //s1.items = new int[problem.numberOfFacilities];
       //s1.objectives = new long[problem.numberOfObjectives];

       strat = stat;
   }

   @Override
   public PerturbatorStrategies getPerturbator(){
       return strat;
   }

   @Override
   public boolean adaptation(){
        return strat.adaptation();
   }

   @Override
   public void restart(){
   }

    //cycle recombination
   @Override public Solution[] perturbator(Solution[] ps1, Variator var){
        if(strat.getPerturbatorType() == 0)
            return iter.perturbator(ps1, var);
        return recomb.perturbator(ps1, var);
   }

    //cycle recombination
   @Override public Solution[] perturbator(Solution[] ps1, ProblemInstance p){
        if(strat.getPerturbatorType() == 0)
            return iter.perturbator(ps1, p);
        return recomb.perturbator(ps1, p);
   }

   @Override
   public int getNrParents(){
        if(strat.getPerturbatorType() == 0)
            return 1;
        return 2;
    }

   @Override
    public Solution[] getParents(ArchiveSolutions nda, SelectMatingChildren set){
        if(strat.getPerturbatorType() == 0){
            return iter.getParents(nda,set);
        }
        return recomb.getParents(nda,set);
    }

    /*public Solution[] getParents(ArchiveSolutions nda, Vector<Solution> past){
        if(strat.getPerturbatorType() == 0){
            return iter.getParents(nda);
        }
        return recomb.getParents(nda,past);
    }*/

   @Override
    public void setPerturbatorType(int operatorType){
        this.typeOperator = operatorType;
        this.strat.setPerturbatorType(typeOperator);
    }
}
