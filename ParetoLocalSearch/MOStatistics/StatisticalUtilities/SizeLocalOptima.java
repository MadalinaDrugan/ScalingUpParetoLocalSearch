/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MOStatistics.StatisticalUtilities;

import general.*;
import java.util.*;
import java.io.BufferedWriter;
/**
 *
 * @author Madalina Drugan, Utrecht University
 *
 */
public class SizeLocalOptima {

    private long[] lengthPLS;
    //accesable
    //private double varLengthPLS;
    //private double meanLengthPLS;

    //count the length of a independent PLS run
    private long[] lengthRun;
    private double varLengthRun;
    private double meanLengthRun;

    //distribution of the nonDomnated archive
    // size of the NDA for a single PLS
    private long[] sizeNDA_PLS;
    //private double varSizeNDA_PLS;
    //private double meanSizeNDA_PLS;

    //size of NDA for a single run PLS
    private long[] sizeNDA_Run;
    private double varSizeNDA_Run;
    private double meanSizeNDA_Run;

    //PLS in an NDA
    private int[] numberPLSinNDA;
    private double meanPLSinNDA;
    private double varPLSinNDA;

    private double[] nrSwapsFinal;
    private double[] nrSizeNDAFinal;

    private double[] nrSwapsTotal;
    private double[] nrSizeNDATotal;

    private double[] levelFinal;
    private double[] levelTotal;

    private int lastNDASize;
    private int lastNDAOverlap;

    private int effectivePerRun = 1;
    
    //private int levelIIndex = 0;
    //private int level2Index = 0;

     public SizeLocalOptima(){
        //protected internat data
        lengthPLS = new long[4];
        lengthRun = new long[4];
        for(int index = 0; index < 4; index++){
            lengthPLS[index] = 0; //the previous numberOfSwaps
            lengthRun[index] = 0; //the previous numberOfSwaps
        }
        //varLengthPLS = 0;
        //meanLengthPLS = 0;
        varLengthRun = 0;
        meanLengthRun = 0;
        varPLSinNDA = 0;
        meanPLSinNDA = 0;

        //protected internat data
        sizeNDA_PLS = new long[3];
        sizeNDA_Run = new long[3];
        numberPLSinNDA = new int[3];

        for(int i = 0; i < 3; i++){
            sizeNDA_PLS[i] = 0; //number of NDAs counted
            sizeNDA_Run[i] = 0; //number of NDAs counted
            numberPLSinNDA[i] = 0;
        }
            //
        //varSizeNDA_PLS = 0;
        //meanSizeNDA_PLS = 0;
        varSizeNDA_Run = 0;
        meanSizeNDA_Run = 0;
        meanPLSinNDA = 0;
        varPLSinNDA = 0;


        nrSwapsFinal = new double[3];
        levelFinal = new double[3];
        nrSizeNDAFinal = new double[3];
        for(int i = 0; i < 3; i++){
            nrSwapsFinal[i] = 0;
            nrSizeNDAFinal[i] = 0;
            levelFinal[i] = 0;
        }

        nrSwapsTotal = new double[4];
        levelTotal = new double[4];
        nrSizeNDATotal = new double[3];
        for(int i = 0; i < 4; i++){
            nrSwapsTotal[i] = 0;
            if(i < 3){
                nrSizeNDATotal[i] = 0;
            }
            levelTotal[i] = 0;

        }

        //levelIIndex = 0;
        //level2Index = 0;

    }

    public void restart(){
                    /////////////////
        // ls statistics
        //////////////////
        if(lengthPLS[0] != 0){
            lengthRun[0]++;
            lengthRun[1] += lengthPLS[1]/lengthPLS[0];
            lengthRun[2] += Math.pow(lengthPLS[1]/lengthPLS[0],2);
        
            sizeNDA_Run[0]++;
            sizeNDA_Run[1] += sizeNDA_PLS[1]/sizeNDA_PLS[0];
            sizeNDA_Run[2] += Math.pow(sizeNDA_PLS[1]/sizeNDA_PLS[0],2);

            nrSwapsFinal[1] += lengthPLS[2]/lengthPLS[1];

            nrSizeNDAFinal[1] += nrSizeNDATotal[1]/nrSizeNDATotal[0];
            
            nrSwapsFinal[2] += Math.pow(lengthPLS[2]/lengthPLS[1],2);
            nrSizeNDAFinal[2] += Math.pow(nrSizeNDATotal[1]/nrSizeNDATotal[0],2);
            nrSwapsFinal[0]++;
            nrSizeNDAFinal[0]++;

            this.levelFinal[0]++;
            levelFinal[1] += levelTotal[2]/levelTotal[1];
            levelFinal[2] += Math.pow(levelTotal[2]/levelTotal[1], 2);
        }

        for(int index = 0; index < 4; index++){
                lengthPLS[index] = 0; //the previous numberOfSwaps
        }
        for(int index = 0; index < 3; index++){
                sizeNDA_PLS[index] = 0; //number of NDAs counted
        }

        for(int i = 0; i < 4; i++){
            nrSwapsTotal[i] = 0;
            levelTotal[i] = 0;
            if(i < 3){
                nrSizeNDATotal[i] = 0;
            }
        }
        effectivePerRun = 1;
   }

   public void collectStatistics(ArchiveSolutions children, Variator var){

       if(children == null || children.size() == 0){
           return;
       }
       
        long thisLength = var.getNumberOfSwaps() - lengthPLS[0];
        lengthPLS[1]++;
        lengthPLS[2] += thisLength;
        lengthPLS[3] += thisLength*thisLength;
        lengthPLS[0] = var.getNumberOfSwaps();

         sizeNDA_PLS[0]++;
         sizeNDA_PLS[1] += children.size();
         sizeNDA_PLS[2] += Math.pow(children.size(),2);

         //number PLS per NDA
         numberPLSinNDA[0]++;
         //count the PLSs in current NDA
         int countPLS = 0;
         Stack<Long> listPLS = new Stack<Long>();
         Iterator<Solution> iterator = children.iterator();
         while(iterator.hasNext()){
             Solution s1 = iterator.next();
             Long temp = Long.valueOf(s1.getIdentifNumber());
             if(!listPLS.contains(temp)){
                 listPLS.add(temp);
                 countPLS++;
             }
             //calculate also the maximum spread of an NDA
         }
         numberPLSinNDA[1] += countPLS;
         numberPLSinNDA[2] += Math.pow(countPLS,2);

         //nrSizeNDAFinal[0] = children.size();
         //this.nrSwapsFinal[0] = var.getNumberOfSwaps();

        nrSizeNDATotal[0]++;
        nrSizeNDATotal[1]+=children.size();
        nrSizeNDATotal[2]+=Math.pow(children.size(),2);

        nrSwapsTotal[1]++;
        nrSwapsTotal[2] += var.getSizeNDA();
        nrSwapsTotal[3] += Math.pow(var.getSizeNDA(),2);
        //nrSwapsTotal[0] = var.getNumberOfSwaps();

        levelTotal[1]++;
        levelTotal[2] +=var.getDeepLevel();
        levelTotal[3] +=Math.pow(var.getDeepLevel(), 2);
        levelTotal[0] = var.getDeepLevel();

        lastNDASize = children.size();
        lastNDAOverlap = countPLS;

        //levelIIndex += var.getDeepLevel();
        //level2Index+= Math.pow(var.getDeepLevel(), 2);

        effectivePerRun++;
    }

    public synchronized void writeStatistics(BufferedWriter fDist){

        try{
            StringBuilder sb = new StringBuilder();
            // Send all output to the Appendable object sb
            Formatter formatter = new Formatter(sb, Locale.US);

            //fDist.write(effectivePerRun + "\t");
            if(lengthPLS[1] == 0){
                fDist.flush();
                return;
            }
            // number of steps in local search
            double tempMean = lengthPLS[2]/lengthPLS[1];
            double varMean = Math.sqrt(lengthPLS[3]/lengthPLS[1] - Math.pow(lengthPLS[2]/lengthPLS[1], 2));
            formatter.format("%d \t %d \t %.2f \t %.2f \t", effectivePerRun, lengthPLS[0], tempMean,varMean);

            //
            tempMean = nrSizeNDATotal[1]/nrSizeNDATotal[0];
            varMean = Math.sqrt(nrSizeNDATotal[2]/nrSizeNDATotal[0] - Math.pow(nrSizeNDATotal[1]/nrSizeNDATotal[0], 2));
            formatter.format("%.2f \t %.2f \t", tempMean, varMean);

            tempMean = numberPLSinNDA[1]/numberPLSinNDA[0];
            varMean = Math.sqrt(numberPLSinNDA[2]/numberPLSinNDA[0] - Math.pow(this.numberPLSinNDA[1]/this.numberPLSinNDA[0], 2));
            formatter.format("%.2f \t %.2f \t", tempMean, varMean);

            tempMean = levelTotal[2]/levelTotal[1];
            varMean = Math.sqrt(levelTotal[3]/levelTotal[1] - Math.pow(levelTotal[2]/levelTotal[1], 2));
            formatter.format("%.2f \t %.2f \t", tempMean, varMean);
            
            tempMean = nrSwapsTotal[2]/nrSwapsTotal[1];
            varMean = Math.sqrt(nrSwapsTotal[3]/nrSwapsTotal[1] - Math.pow(nrSwapsTotal[2]/nrSwapsTotal[1], 2));
            formatter.format("%.2f \t %.2f \t", tempMean, varMean);
            
            formatter.format("%d \t %d \t ", lastNDASize, lastNDAOverlap);
            fDist.append(sb); //.write(sb);

            fDist.newLine();
            fDist.flush();

        }catch(Exception e){
			System.out.println("Write not possible:"+fDist);
			e.printStackTrace(System.out);
		}
    }
}
