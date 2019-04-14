/**
 *
 * @author Madalina Drugan: 2010-2018
 */
package GeneticPLS.Exploration.Permutation;

import general.*;
import general.Genetic.*;

public class Iter_QAP implements GeneticOperators{
   
   private ProblemInstance problem;
   private PerturbatorStrategies strat;

   private int typeOperator = 1;
   public int mut_rate;

   private Solution s1;
   
   public Iter_QAP(){
   }
   
   private final static long serialVersionUID = 11239585071342354L;//10795; //
   private final static java.util.Random r = new java.util.Random();

   public int[] index;

   private Solution[] results = new Solution[1];

    @Override
   public void setVariator(ProblemInstance p, PerturbatorStrategies stat){
       problem = p;
       strat = stat;
   }

    @Override
   public boolean adaptation(){
        return strat.adaptation();
    }

    @Override
    public void restart(){
        
    }

    @Override
    public int getNrParents(){
        return 1;
    }

    @Override
    public Solution[] getParents(ArchiveSolutions nda, SelectMatingChildren set){
        if(nda == null || nda.size() == 0) {
            return null;
        }
        Solution[] population = nda.ndaToArray();
        int[] indexP = set.select(1, population); 
        //r.setSeed(r.nextLong());
        Solution[] temp = new Solution[1];

        temp[0] = nda.getNthSolution(indexP[0]);
        return temp;
    }

    @Override
    public PerturbatorStrategies getPerturbator(){
       return strat;
    }

    private boolean side;
    
    //cycle recombination
    @Override
    public Solution[] perturbator(Solution[] ps1, Variator var){
        results = this.perturbator(ps1, var.getProblem());
        if(results != null){
            //if(side == true){
            int dist = results[0].getDistance(ps1[0]);
            
            var.updateNrSwaps(dist);
        }
        return results;
    }


   @Override public Solution[] perturbator(Solution[] s, ProblemInstance p){
        s1 = s[0].clone();
        boolean b;
        mut_rate = (int)strat.getPerturbator();
        index = new int[mut_rate];
        for(int i = 0; i < mut_rate; i++){
            index[i] = r.nextInt(s1.items.length);
            while(true){
                b = true;
                for(int k = 0; k < i; k++) {
                    if (index[k] == index[i]) {
                        b = false;
                        break;
                    }
                }
                if(b == false) {
                    index[i] = r.nextInt(s1.items.length);
                }
                else {
                    break;
                }
            }
        }

        //randomize the direction of swaping
        side = r.nextBoolean();
        if(side){
            int temp = (Integer)s1.items[index[0]];
            for(int i = 0; i < mut_rate-1; i++) {
                s1.items[index[i]] = s1.items[index[i + 1]];
            }
            s1.items[index[mut_rate-1]]=temp;
        } else {
            int temp = (Integer)s1.items[index[mut_rate-1]];
            for(int i = mut_rate-1; i > 0; i--) {
                s1.items[index[i]] = s1.items[index[i - 1]];
            }
            s1.items[index[0]]=temp;
        }
        s1.flagVisited = false;

        //var.getProblem()
        s1 = p.computeSolution(s1);

        results[0] = s1;
        return results;
     }

    @Override
   public void setPerturbatorType(int operatorType){
        this.typeOperator = operatorType;
    }
}
