/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MOStatistics.StatisticalUtilities;

import general.*;
import java.util.*;

/**
 *
 *
 * @author madalina
 */
public class DistanceNDAs_Average extends DistanceNDAs_MinimumPerPairs{

    //correlations betweem objectives
    //private boolean[] parentsA;
    //private boolean[] childrenA;

    //private ProblemInstance problem;
    private int nrObjectives;
    private int power;

    public DistanceNDAs_Average(ProblemInstance p, int pow){
       // problem = p;
        nrObjectives = p.numberOfObjectives;
        power = pow;
    }

    private double[] result; // distance fitness distance correlation
    private double[][][] distFitCorr;
    //private double[][][] autoCorrFunct;
    private double[][] corrVector; // statistics on each objective
    double[] tempObj, childObj;
    @Override public double[] internDistance(ArchiveSolutions parents, ArchiveSolutions children){
        double temp1 = 0;
      
        result = new double[13];
        corrVector = new double[nrObjectives][9];
        distFitCorr = new double[nrObjectives][nrObjectives][3];
       
        tempObj = new double[nrObjectives];
        childObj = new double[nrObjectives];
       
        //////////////////////////////////////
        // statistics between two local Pareto fronts !!!!
        int countP = 0;
        double tempP = 0; double tempP1 = 0;
        double tempPD = 0; double tempPD1 = 0;
        Iterator<Solution> iterP = parents.iterator();
        while(iterP.hasNext()){
            Solution aParent = iterP.next();

            for(int i = 0; i < nrObjectives; i++)
                tempObj[i] = (Long)aParent.objectives[i];
            
            for(int indexI = 0; indexI < nrObjectives; indexI++){
                //correlation
                corrVector[indexI][0] += tempObj[indexI];
                corrVector[indexI][1] += Math.pow(tempObj[indexI], 2);
            }

            int countC = 0;
            double tempC = 0; double tempC1 = 0;
            double tempCD = 0; double tempCD1 = 0;
            Iterator<Solution> iterC = children.iterator();
            while(iterC.hasNext()){
                Solution aChild = iterC.next();

                for(int i = 0; i < nrObjectives; i++)
                    childObj[i] = (Long)aChild.objectives[i];
            
                ////////////////////////
                // distance in solution space
                temp1 = aParent.getDistance(aChild);
                result[0] += temp1;
                result[1] += Math.pow(temp1, 2);

                double tempF = 0;
                double tempM = 0; double tempM1 = 0;
                //double tempMD = 0; double tempMD1 = 0;
                for(int indexI = 0; indexI < nrObjectives; indexI++){
                    corrVector[indexI][4] += tempObj[indexI]*childObj[indexI];

                    corrVector[indexI][5] += Math.max(tempObj[indexI] - childObj[indexI], 0);
                    corrVector[indexI][6] += Math.pow(tempObj[indexI] - childObj[indexI],2);

                    corrVector[indexI][7] += temp1 * Math.max(tempObj[indexI] - childObj[indexI], 0);
                    corrVector[indexI][8] += Math.pow(temp1 * (tempObj[indexI] - childObj[indexI]), 2);
                    
                    ///////////////////////////
                    // the Euclidean distance between objectives
                    if(tempObj[indexI] != childObj[indexI]){
                        tempF += Math.pow(tempObj[indexI] - childObj[indexI],power);
                    } else {
                        tempF += 0;
                    }
                    
                    /////////////////////////
                    // the min-max distance between objectives
                    double tempT = Math.max(tempObj[indexI] - childObj[indexI], 0);
                    if(indexI == 0){
                        tempM = tempT;
                    } else {
                        tempM = Math.min(tempM, tempT);
                        tempM1 = Math.max(tempM1, tempT);
                    }
                }
                
                ///////////////////////////
                // Euclidean distance in objective space
                result[2] += Math.pow(tempF,1.0/power);
                result[3] += Math.pow(tempF, 2.0/power);
                
                //////////////////////
                // min-max distance in all objectives
                if(countC == 0){
                    tempC = tempM;
                    tempC1 = tempM1;
                    
                    tempCD = temp1;
                    tempCD1 = temp1;
                } else {
                    tempC = Math.max(tempC, tempM);
                    tempC1 = Math.max(tempC1, tempM1);
                    
                    tempCD = Math.max(tempCD, temp1);
                    tempCD1 = Math.min(tempCD1, temp1);
                }
                countC++;
            }
            //for(int indexI = 0; indexI < nrObjectives; indexI++){
            //    temp2 += Math.pow(aParent.objectives[indexI],2);
            //}
            if(countP == 0){
                tempP = tempC;
                tempP1 = tempC1;
                
                tempPD = tempCD;
                tempPD1 = tempCD1;
            } else {
                tempP = Math.max(tempP,tempC);
                tempP1 = Math.min(tempP1, tempC1);
                
                tempPD = Math.max(tempPD,tempCD);
                tempPD1 = Math.min(tempPD1, tempCD1);                
            }
            countP++;
        }
        
        // difference fitness distance correlation
        result[4] = parents.size() * children.size(); // tempC

        result[0] /= result[4]; // tempR
        result[1] = Math.sqrt(result[1]/result[4] - Math.pow(result[0], 2));//tempT
        
        result[2] /= result[4]; // tempP
        result[3] = Math.sqrt(result[3]/result[4] - Math.pow(result[2], 2)); // tempD

        result[5] = tempP;
        result[6] = Math.pow(tempP, 2);
        
        result[7] = tempP1;
        result[8] = Math.pow(tempP1, 2);
        
        result[9] = tempPD;
        result[10] = Math.pow(tempPD, 2);
        
        result[11] = tempPD1;
        result[12] = Math.pow(tempPD1, 2);
        
        /////////////////////////////////
        //
        Iterator<Solution> iterC = children.iterator();
        while(iterC.hasNext()){
            Solution aChild = iterC.next();

            for(int i = 0; i < nrObjectives; i++)
                childObj[i] = (Long)aChild.objectives[i];
            
            for(int indexI = 0; indexI < nrObjectives; indexI++){
                corrVector[indexI][2] += childObj[indexI];
                corrVector[indexI][3]+= Math.pow(childObj[indexI], 2);
            }
        }

        for(int indexI = 0; indexI < nrObjectives; indexI++){
            
            corrVector[indexI][0] /= parents.size();
            corrVector[indexI][1] = Math.sqrt( corrVector[indexI][1]/parents.size() - Math.pow(corrVector[indexI][0],2) );
            corrVector[indexI][2] /= children.size();
            corrVector[indexI][3] = Math.sqrt( corrVector[indexI][3]/children.size() - Math.pow(corrVector[indexI][2],2) );
            
            corrVector[indexI][4] /= (parents.size() * children.size());
            
            corrVector[indexI][5] /= (parents.size() * children.size());
            corrVector[indexI][6] = Math.sqrt( corrVector[indexI][6]/(parents.size() * children.size()) - Math.pow(corrVector[indexI][5],2) );
            
            corrVector[indexI][7] /= (parents.size() * children.size());
            corrVector[indexI][8] = Math.sqrt( corrVector[indexI][8]/(parents.size() * children.size()) - Math.pow(corrVector[indexI][7],2) );
            
        }
        
        return result;
   }

   @Override public double[][] getCorrVector(){
    return corrVector;
   }

}
