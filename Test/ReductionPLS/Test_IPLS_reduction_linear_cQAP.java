/*
 * 
 */

package Test.ReductionPLS;

import GeneticPLS.Exploration.Permutation.Iter_QAP;
import GeneticPLS.Exploration.Permutation.Iter_path_QAP;
import GeneticPLS.Exploration.MutRecomb;
import GeneticPLS.Iter_PLS;
import GeneticPLS.MSPerturbatorRates.Instances.FixMutation;
import GeneticPLS.MSPerturbatorRates.MatingPool.RandomMating;
import GeneticPLS.MSPerturbatorRates.MultipleRandomMutRecomb;
import MOInstance_QAPs.QAPs;
import Neighbourhoods.Iterator_Solution.Iterator_Solutions_QAPs;
import PLS.LocalSearch.Neighbourhood.Neigh_best;
import PLS.LocalSearch.One_PLS;
import PLS.MultiRest_PLS;
import PLS.PLS;
import PLS.StopingCriteria.StopingCriteria_Nr_PLS;
import PLS.StopingCriteria.StopingCriteria_Nr_swaps;
import PLS.myPLS_sameStart;
//import Path_files_QAP;
import Performance.ImprovNDA;
import Reduction.ReduceScalarizer;
import Reduction.Reductions.CollectScalarizers;
import Reduction.Sets.RandomRed;
import Scalarization.Scalarizers.WeightedSum;
import general.Reduction.Reduction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Test_IPLS_reduction_linear_cQAP{

   
   public static void main(String[] args){
      
      int startingPop =10;
      int sizePerturbation = 15;
     
      long initialSol = 0;
      FixMutation epsilon = new FixMutation(0);
            
       //so it reads only the structured files
      QAPs.type_QAPs = 1;
      QAPs newQ;
      newQ = QAPs.readFileInObject(Path_files_QAP.multipleFiles,-1);

      //////////////
      // initialize reduction 
      /////////////
      Stack<Reduction> stackScal = new Stack<>();

      //////////////////////
      // read reductions from a file
      /////////////////////////
      int cardinalScal = 4;
      int dimProd = 1;
      int nrObj = 5;
      String filename = Path_files_QAP.filename+cardinalScal+dimProd+nrObj;
      String line;
      File myFile = new File(filename);
      try{
          //myFile.createNewFile();
          FileReader fileStream = new FileReader(myFile);
          BufferedReader fos = new BufferedReader(fileStream);
          
          while(true){
            line = fos.readLine();
            if(line == null){
                break;
            }
            
            String[] scalarizations = line.split("\t");
            if(scalarizations.length != dimProd){
                System.err.println("dimension product is not respected " + dimProd + " found " + scalarizations.length);
            }
            
            CollectScalarizers scal1 = new CollectScalarizers();
            
            for (String scalarization : scalarizations) {
                double[] weights = new double[nrObj]; 
                String[] nonZeroWeights = scalarization.split(",");
                if(nonZeroWeights.length != cardinalScal){
                      System.err.println("cardinality scalarization is not respected " + cardinalScal + " found " + nonZeroWeights.length);
                } 
                for (String nonZeroWeight : nonZeroWeights) {
                    weights[Integer.valueOf(nonZeroWeight)] = 1.0/(double)cardinalScal;
                }
            
                scal1.addScalarizer(new ReduceScalarizer(new WeightedSum(weights)));
            }
            
            stackScal.add(scal1);
          }
            
      } catch (IOException e){
          System.out.println("Read not possible:" + myFile.toString());
          e.printStackTrace(System.out);
      }
      
      RandomRed setScal = new RandomRed(stackScal);

      Iterator_Solutions_QAPs iter = new Iterator_Solutions_QAPs(2);
      Neigh_best neigh = new Neigh_best();
      neigh.setVariator(newQ, iter,epsilon,setScal);
      One_PLS onePLS = new One_PLS();
      onePLS.setVariator(neigh, iter);

      //improvement strategy
      ImprovNDA incompImprov = new ImprovNDA();
      
      ///////////////////////////////
      //Iter + Recomb PLS -- randomly pick up
      // no adaptation
      //////////////////////////////////
      String outputMultiple = Path_files_QAP.outputMultiple +cardinalScal+dimProd+nrObj+"_50.p75";

      boolean[] flags = new boolean[1];
      flags[0] = true;
      
      FixMutation[] perturbMut_R = new FixMutation[sizePerturbation];
      FixMutation[] perturbRecomb_R = new FixMutation[sizePerturbation];

      for(int i = 0; i < sizePerturbation; i++){
        perturbMut_R[i] = new FixMutation(i);
        perturbRecomb_R[i] = new FixMutation(i);
      }
      
      MultipleRandomMutRecomb genAdaptI = new MultipleRandomMutRecomb(perturbMut_R,perturbRecomb_R);

      Iter_QAP iterQAP_Random = new Iter_QAP();
      iterQAP_Random.setVariator(newQ, genAdaptI); //perturbRandom);
      Iter_path_QAP recombQAP_Random = new Iter_path_QAP(iterQAP_Random);
      recombQAP_Random.setVariator(newQ, genAdaptI); //perturbRandom);
      MutRecomb rmRQAP = new MutRecomb(recombQAP_Random,iterQAP_Random);
      rmRQAP.setVariator(newQ, genAdaptI);

      //init variator
      Iter_PLS thisPLSII = new Iter_PLS();
      thisPLSII.setPerformance(incompImprov);
      
      
      MultiRest_PLS initPLSII = new MultiRest_PLS();

      PLS newMP_Random = new PLS(startingPop,(long)(160*Math.pow(10, 6)),outputMultiple,thisPLSII,null,newQ,rmRQAP,setScal,onePLS,initialSol); //,epsilon);

      myPLS_sameStart pls_Random = new myPLS_sameStart(newMP_Random, outputMultiple,startingPop,newQ,setScal,false); //,epsilon);

      newMP_Random.setStatistics(pls_Random.s);
      pls_Random.s.setStatistics(flags,true);
      //pls_Random.s.setBestType(1, inputBest);

      StopingCriteria_Nr_PLS st_II_B_ = new StopingCriteria_Nr_PLS(newMP_Random);
      RandomMating rand = new RandomMating();     
      initPLSII.setVariator(newMP_Random, newQ, rmRQAP, onePLS,st_II_B_,rand,false); //,epsilon);

      StopingCriteria_Nr_swaps st_II_B = new StopingCriteria_Nr_swaps(newMP_Random);
      thisPLSII.setVariator(newMP_Random, newQ, rmRQAP, onePLS,st_II_B,rand,setScal); //,epsilon);
      thisPLSII.activateStatistics(true);

      //runs for comparision
      pls_Random.run();

      /////////////////////////////////
      // first improvement
      // contextual PLS
      /////////////////////////////////
      for(int index = 0; index < 50; index++){

          if(index > 0){

            pls_Random.run();
          }
 
          pls_Random.restarts();
      }
      
      pls_Random.collectStatistics();

      newMP_Random.writeTotalNDA();
   }
} 