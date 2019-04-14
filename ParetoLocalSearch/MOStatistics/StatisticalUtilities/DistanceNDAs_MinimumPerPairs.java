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
public class DistanceNDAs_MinimumPerPairs {

    //correlations betweem objectives
   private boolean[] parentsA;
   private boolean[] childrenA;
   private double key = 0;
   private int[] value = new int[2];

   private double tempF = 0;
   //private double tempC = 0;
   //private double tempR = 0;
   //private double tempT = 0;

   class InnerDistance implements java.io.Serializable{
        public double distance;
        public int[] instances;
        public long[] objParent1;
        public long[] objParent2;
        public InnerDistance(double d, int[] i, long[] p1, long[] p2){
           this.distance = d;
           this.objParent1 = Arrays.copyOf(p1, p1.length); //new double[p1.length];
           this.objParent2 =  Arrays.copyOf(p2, p2.length);
           this.instances =  Arrays.copyOf(i, i.length);
       }
       public InnerDistance(){
       }
    }

    private Stack<InnerDistance> thisStack = new Stack<>();
    private TreeMap<Double,Stack<InnerDistance>> matrixDistances = new TreeMap<>();
    //private long[][] maxParents;

    //private ProblemInstance problem;
    private int nrObjectives;

    //public DistanceNDAs_MinimumPerPairs(){}

    public DistanceNDAs_MinimumPerPairs(ProblemInstance p){
        //problem = p;
        nrObjectives = p.numberOfObjectives;
    }

    public DistanceNDAs_MinimumPerPairs(){}
    
    private Iterator<Solution> iter;
    long[] tempObj, tempObjC;
    public void distanceNDAs(ArchiveSolutions parents, ArchiveSolutions children){

        int intP, intC = 0;
        value[0] = 0;
        value[1] = 0;
        //double key;
        matrixDistances.clear();
        thisStack.clear();

        iter = parents.iterator();
        
        tempObj = new long[nrObjectives];
        tempObjC = new long[nrObjectives];
        
        while(iter.hasNext()){
            Solution temp =  iter.next();

            intP=0;
            Iterator<Solution> iterC = children.iterator();
            while(iterC.hasNext()){
                 Solution tempC = iterC.next();

                 key = tempC.getDistance(temp);
                 value[0] = intP;
                 value[1] = intC;
                 
                 for(int i = 0; i < nrObjectives; i++){
                     tempObj[i] = (long)temp.objectives[i];
                     tempObjC[i] = (long)tempC.objectives[i];
                 }
                 InnerDistance classInner = new InnerDistance(key,value,tempObj,tempObjC);

                 if(!matrixDistances.isEmpty() & matrixDistances.containsKey(key)) {
                    thisStack = matrixDistances.get(key);
                }
                 else {
                    thisStack = new Stack<>();
                }
                thisStack.push(classInner);
                matrixDistances.put(key, thisStack);

                 intP++;
            }

            intC++;
        }
   }

   public double[][] getCorrVector(){
    return corrVector;
   }

   private double[] result;
   private double[][] corrVector;
   public double[] internDistance(ArchiveSolutions par, ArchiveSolutions child){
       if(par == null || par.size() < 1 || child == null || child.size() < 1) {
            return null;
        }
       if(corrVector == null){
           corrVector = new double[nrObjectives][7];
       } else {
           for(int i = 0; i < nrObjectives; i++) {
                for (int j = 0; j < 7; j++) {
                    corrVector[i][j] = 0;
                }
            }
       }
       result = new double[5];
        int trueC = 0;
        int trueP = 0;
        InnerDistance allD;
        parentsA = new boolean[par.size()];
        for(int i = 0; i < parentsA.length; i++) {
            parentsA[i] = false;
        }

        childrenA = new boolean[child.size()];
        for(int i = 0; i < childrenA.length; i++) {
            childrenA[i] = false;
        }

        thisStack.clear();
        //distance metrix and correlation
        matrixDistances.clear();
        //compute distance
        distanceNDAs(par,child);
        while(trueC <= childrenA.length & trueP <= parentsA.length & matrixDistances.size() > 0){
            key = matrixDistances.lastKey();
            thisStack = matrixDistances.get(key);
            while(!thisStack.empty()){
                allD = thisStack.pop();
                value[0] = allD.instances[0];
                value[1] = allD.instances[1];
                if(value[0] >= childrenA.length | value[1] >= parentsA.length) {
                    System.err.println("Error in maximDistances " + value[0] + ", " + value[1] + "Real Values " + childrenA.length + "," + parentsA.length);
                }

                try{
                if(childrenA[value[0]] == false & parentsA[value[1]] == false){
                    childrenA[value[0]] = true;
                    parentsA[value[1]] = true;
                    trueP++;
                    trueC++;
                } else if(childrenA[value[0]] == false & trueP == parentsA.length){
                    childrenA[value[0]] = true;
                    trueC++;
                } else if(parentsA[value[1]] == false & trueC == childrenA.length){
                    parentsA[value[1]] = true;
                    trueP++;
                } else if(trueC == childrenA.length & trueP == parentsA.length) {
                        break;
                    }
                else {
                        continue;
                    }
                }catch(java.lang.ArrayIndexOutOfBoundsException e){
                    System.out.println("Children (");
                    for(int i = 0; i < childrenA.length; i++) {
                        System.out.print(childrenA[i] + ",");
                    }
                    System.out.println(")");
                    System.out.println("Parents (");
                    for(int i = 0; i < parentsA.length; i++) {
                        System.out.print(parentsA[i] + ",");
                    }
                    System.out.println(")");
                    System.out.println("Values" + value[0] + "," + value[1]);
                    System.out.println(e);
                }

                //result
                result[0] += key; //tempR+=key;
                result[1] += Math.pow(key, 2);//tempT
                //distance in objective space
                tempF = 0;
                for(int indexI = 0; indexI < nrObjectives; indexI++){
                    corrVector[indexI][0]+= allD.objParent1[indexI]; //tempMeanParents
                    corrVector[indexI][1] += Math.pow(allD.objParent1[indexI],2); //temp2Parents

                    corrVector[indexI][2]+= allD.objParent2[indexI]; //tempMeanChildren
                    corrVector[indexI][3]+=Math.pow(allD.objParent2[indexI], 2); //temp2Children

                    corrVector[indexI][4] += allD.objParent1[indexI]*allD.objParent2[indexI]; //tempParentsChildren

                    corrVector[indexI][5] += allD.objParent1[indexI] - allD.objParent2[indexI]; //tempDifference
                    corrVector[indexI][6] += Math.pow(allD.objParent1[indexI] - allD.objParent2[indexI],2); //varTempDifference
                    
                    tempF += Math.pow(allD.objParent1[indexI] - allD.objParent2[indexI],2);
                }
                result[2] += Math.sqrt(tempF); //tempP
                result[3] += tempF;//tempD
            }
            if(trueC == childrenA.length-1 & trueP == parentsA.length-1) {
                break;
            }
            else {
                matrixDistances.remove(key);
            }
        }

        result[4] = Math.max(par.size(), child.size());//tempC
        result[0] /= result[4]; // tempR
        result[2] /= result[4]; // tempP
        result[3] = Math.sqrt(result[3]/result[4] - Math.pow(result[2], 2)); // tempD
        result[1] = Math.sqrt(result[1]/result[4] - Math.pow(result[0], 2));//tempT

        return result;
   }

    //public double[] refPoint;
    public double computeHyper(ArchiveSolutions p, double[] refPoint){
        if(p == null || p.size() < 1)
            return 0;
        double temp1 = 0;

        Iterator<Solution> iter = p.iterator();
        Solution prev = iter.next();
        temp1 += ((Double)prev.objectives[0] - refPoint[0]) * ((Double)prev.objectives[1] - refPoint[1]);
        while(iter.hasNext()){
            Solution curr = iter.next();
            temp1 += ((Double)curr.objectives[0] - (Double)prev.objectives[0])*((Double)curr.objectives[1] - refPoint[1]);
            prev = curr;

        }
        return temp1;
    }


}
