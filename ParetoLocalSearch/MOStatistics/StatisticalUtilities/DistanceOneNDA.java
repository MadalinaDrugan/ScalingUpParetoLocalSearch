/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MOStatistics.StatisticalUtilities;

import java.util.*;
import general.*;
import java.io.BufferedWriter;
/**
 *
 * @author Madalina Drugan, Utrecht University
 *
 */
public class DistanceOneNDA {

    private double[] distance; // distances between solutions
    // solution space: mean + std; maximum; minimum; (non-equal)
    //euclidian objective: mean + std; maximum; minimum; (non-equal)
    //private double[][] distObj; // distance in objective space for each objective
    private Iterator<Solution> iter;
    private Stack<Solution> listPLS;

    private int nrObjectives;
    private int powerDist = 2;

    private int effectivePerRun = 1;

    public DistanceOneNDA(int nrO, int powerD){
        nrObjectives = nrO;
        powerDist = powerD;
        distance = new double[8+nrO*2];
    }

    public void restart(){
        for(int i = 0; i < distance.length; i++){
            distance[i] = 0;
        }
        effectivePerRun = 1;
    }

    public double[] distanceNDA(ArchiveSolutions currentNDA){
        double  tempV = 0, tempV2 = 0, tempD = 0;
        int countPLS = 0;
        double tempE = 0, tempE2 = 0, tempT = 0;
        double minV = 0, maxV = 0;
        double minE = 0, maxE = 0;
        double mean1 = 0, mean2 = 0, mean1Mean2 = 0, meanP1 = 0, meanP2 = 0;

        if(currentNDA == null || currentNDA.size() <= 1){
            return distance;
        }
        
        //if(currentNDA.size() > 1){
        tempT = 0;
        listPLS = new Stack<>();
        iter = currentNDA.iterator();
        while(iter.hasNext()){
            Solution s1 = iter.next();

            //Long temp = Long.valueOf(s1.getIdentifNumber());
            if(!listPLS.contains(s1)){
                  listPLS.add(s1);
            }

            // calculate the correlation
            for(int i = 0; i < nrObjectives; i++){
                  mean1 += (Long)s1.objectives[i];
                  meanP1 += Math.pow((Long)s1.objectives[i], 2);
             }

             //calculate also the maximum spread of an NDA
             Iterator<Solution> iterator1 = currentNDA.iterator();
             while(iterator1.hasNext()){
                 Solution s2 = iterator1.next();

                 //Long temp2 = Long.valueOf(s2.getIdentifNumber());
                 if(!listPLS.contains(s2)){
                       // distance in solution space
                       tempD =s2.getDistance(s1); 
                       
                       tempV += tempD;
                       tempV2 += Math.pow(tempD, 2);
                       
                       if(maxV < tempD) {//calculate maximum distance
                            maxV = tempD;
                       }
                       if(minV == 0){ //calculate minimu; distance
                           minV = tempD;
                       } else if(minV > tempD){
                           minV = tempD;
                       }

                       
                       // calculate Euclidian distance
                       tempT = 0; 
                       for(int i = 0; i < nrObjectives; i++){
                            tempT += Math.pow((Long)s1.objectives[i] - (Long)s2.objectives[i], powerDist);
                       }
                       tempE += Math.pow(tempT,1.0/powerDist);                      
                       tempE2 += tempT;
                       
                       if(maxE < Math.pow(tempT,1.0/powerDist)){
                           maxE = Math.pow(tempT,1.0/powerDist);
                       }
                       if(minE == 0){
                           minE = Math.pow(tempT,1.0/powerDist);
                       } else if(minE > Math.pow(tempT,1.0/powerDist)){
                           minE = Math.pow(tempT,1.0/powerDist);
                       }

                       // calculate the correlation
                       //tempT = 0;
                       for(int i = 0; i < nrObjectives; i++){
                            mean1Mean2 += (Long)s1.objectives[i] * (Long)s2.objectives[i];
                       }

                       countPLS++;
                 }
             }
        }

        distance[0] += tempV/countPLS; //(currentNDA.size() *(currentNDA.size()-1))/2;
        distance[1] += Math.sqrt(tempV2/countPLS - Math.pow(tempV/countPLS,2)); //currentNDA.size() *(currentNDA.size()-1))/2;
        
        // distance 2 and 3 already assigned
        distance[2] += tempE2/countPLS;
        distance[3] += Math.sqrt(tempE/countPLS - Math.pow(tempE2/countPLS,2));
        
        distance[4] += tempE/countPLS;
        distance[5] += Math.sqrt(tempE2/countPLS - Math.pow(tempE/countPLS, 2));
        // distance 6 and 7 already assigned
        distance[6] += maxV;
        distance[7] += minV;

        //distance 8 + correlation
        for(int i = 0; i < nrObjectives; i++){
            tempT = mean1Mean2/countPLS - mean1*mean2/Math.pow(currentNDA.size(),2);
            tempE = Math.sqrt(meanP1/currentNDA.size() - Math.pow(mean1/currentNDA.size(), 2));
            tempD = Math.sqrt(meanP2/currentNDA.size() - Math.pow(mean2/currentNDA.size(), 2));
            distance[8+2*i] += tempT/(tempE * tempD);
            distance[8+2*i+1] += Math.pow(tempT/(tempE * tempD), 2);
        }

        effectivePerRun++;
        
        return distance;
    }

    public synchronized void writeStatistics(BufferedWriter fDist){

        try{
            StringBuilder sb = new StringBuilder();
            // Send all output to the Appendable object sb
            Formatter formatter = new Formatter(sb, Locale.US);

            //fDist.write(effectivePerRun + "\t");
            double tempMean = distance[0]/effectivePerRun;
            double varMean = distance[1]/effectivePerRun;
            formatter.format("%d \t %.2f \t %.2f \t", effectivePerRun, tempMean,varMean);

            formatter.format("%.2f \t %.2f \t", distance[2]/effectivePerRun,distance[3]/effectivePerRun);

            tempMean = distance[4]/effectivePerRun;
            varMean = distance[5]/effectivePerRun;
            formatter.format("%.2f \t %.2f \t", tempMean, varMean);

            formatter.format("%.2f \t %.2f \t", distance[6]/effectivePerRun,distance[7]/effectivePerRun);

            for(int i = 0; i < nrObjectives; i++){
                tempMean = distance[2*i+8]/effectivePerRun;
                varMean = Math.sqrt(distance[2*i+9]/effectivePerRun - Math.pow(tempMean, 2));

                formatter.format("%.2f \t %.2f \t", tempMean, varMean);
            }

            fDist.append(sb); //.write(sb);

            fDist.newLine();
            fDist.flush();
        }catch(Exception e){
			System.out.println("Write not possible:"+fDist);
			e.printStackTrace(System.out);
		}
    }

}
