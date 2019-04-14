/*
 * @author Madalina Drugan: 2010-2018
 */

package MOInstance_NK;

import Archives.*;
import java.io.*;
import java.util.*;
import general.*;

public class Persistence_Solution_NK implements  Serializable{

    static final long serialVersionUID = 101L;

    public static ArchiveSolutions generateSolution(){ //PerturbatorStrategies epsilon){
        return new NonDominatedArchive();//epsilon);
    }

    public static ArchiveSolutions readFile_Solutions(String filename, NK problem)//, PerturbatorStrategies epsilon)
    {
        //boolean setMinus = false;
        int nrSolutions = 0;
        Solution_NK bestS;
        boolean[] tempStr = new boolean[problem.numberOfFacilities];
        double[] tempObj = new double[problem.numberOfObjectives];
        
        NonDominatedArchive bestNDA = new NonDominatedArchive(); //epsilon);
        try{
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String temp = in.readLine();
        while(temp != null && !temp.isEmpty()){
            String[] entries = temp.split("\\s+");

            
            for(int i = 0; i < problem.numberOfObjectives;i++){
                tempObj[i] = Double.parseDouble(entries[i]);
            }
            for(int i = 0; i < problem.numberOfFacilities;i++){
                tempStr[i] = Boolean.parseBoolean(entries[i+problem.numberOfObjectives]);
                
            }
            temp = in.readLine();
            

            bestS = new Solution_NK(tempObj,tempStr); //,epsilon);
            bestNDA.add(bestS);
            

            nrSolutions++;
        }
         } catch (Exception e){
			System.out.println("Write not possible:" + filename);
			e.printStackTrace(System.out);
        }
        return bestNDA;
   }

        ////////////////////////////////
    // specific functions for RW
    ///////////////////////
    // read peaks from file0
    // initialize the current Set of archives
    // read a percent of the archive to make the statistics: numbers from 0 to 100
    // percent = 0 --> read only one archives
    // percent = 100 --> read all archives
    /////////////////////////////
    public static Stack<ArchiveSolutions> ReadPeaks(String fileO, NK problem, int percent){ //, PerturbatorStrategies epsilon){
        Stack<ArchiveSolutions> currentSetArchive = new Stack<>();
        NonDominatedArchive currentArchive = new NonDominatedArchive(); //epsilon);
        Solution_NK s1;
        java.util.Random r = new java.util.Random();
        Boolean[] tempB = new Boolean[problem.numberOfFacilities];
        Double[] tempA = new Double[problem.numberOfObjectives];
        String temp;

        try{
            BufferedReader in = new BufferedReader(new FileReader(fileO));
            temp = in.readLine();

            //nothing to read
            while(temp == null){
                // break between lines; what to do
                if(temp.contentEquals("")){
                   if(currentArchive.size() < 2){
                       currentArchive.reset();
                       temp = in.readLine();
                       continue;
                   } else if(percent == 0){
                       currentSetArchive.add(currentArchive.clone());
                       break;
                   } else if(percent > 0 && percent < 100)
                        if(r.nextDouble() > percent/100.0){
                            currentArchive.reset();
                            temp = in.readLine();
                            continue;
                        }
                        // write the current archive
                        currentSetArchive.add(currentArchive.clone());
                        temp = in.readLine();
                        currentArchive.reset();
                        continue;
                   }
               }

               //read the (solutions and objectives)
               String[] entries = temp.split(", ");

               for(int i = 0; i <= tempB.length-1; i++){
                        tempB[i] = Boolean.parseBoolean(entries[i]);
                    }
                for(int i=tempB.length;i<tempB.length+problem.numberOfObjectives;i++) {
                    tempA[i-tempB.length] = Double.parseDouble(entries[i]);
                }
                s1 = new Solution_NK(tempA,tempB); //,epsilon);
                currentArchive.add(s1);
                //}
                temp = in.readLine();
         } catch (Exception e){
                System.out.println("Write not possible:" + fileO);
                e.printStackTrace(System.out);
         }
        return currentSetArchive;
    }


        // compute the ideal reference point
    public static Object[] getIdealPoint(ArrayList<Solution> al){
        if(al == null || al.isEmpty())
            return null;
        
        Double[] referencePoint = (Double[])Arrays.copyOf(al.get(0).objectives,al.get(0).objectives.length);
        for(int i=1;i<al.size();i++){
            Double[] obj = (Double[])al.get(i).objectives;
            for(int j=0;j<referencePoint.length;j++){
		if (obj[j]<referencePoint[j])
                    referencePoint[j] = obj[j];
            }
	}
	return referencePoint;
    }

    //////////////////////
    public static Object[] getIdealPoint(ArchiveSolutions al, Object[] offset){
        if(al == null || al.size() == 0) {
            return null;
        }
        
        Iterator<Solution> iter = al.iterator();
        Solution s1 = iter.next();
        
        Double[] referencePoint = (Double[])Arrays.copyOf(s1.objectives,s1.objectives.length);
        while(iter.hasNext()){
            s1=iter.next();
            for(int i=0;i<al.size();i++){
                Double[] obj = (Double[])s1.objectives;
                for(int j=0;j<referencePoint.length;j++){
                    if(obj[j]<referencePoint[j]) {
                        referencePoint[j] = obj[j];
                    }
                }
            }
        }
        for(int i = 0; i < referencePoint.length; i++) {
            referencePoint[i] -= (Double)offset[i];
        }
	return referencePoint;
    }

    public static Object[] setIdealPoint(Object[] referencePoint, Object[] objectives, Object[] offset){
        for(int j=0;j<referencePoint.length;j++){
            if((Double)objectives[j] < (Double)referencePoint[j]) {
                referencePoint[j] = (Double)objectives[j] - (Double)offset[j];
            }
	}
        return referencePoint;
    }

    public static Object[] resetIdealPoint(int nrObjectives){
        Double[] referencePoint = new Double[nrObjectives];
        Arrays.fill(referencePoint, Integer.MAX_VALUE);
        return referencePoint;
    }

    public static long[] resetNadirPoint(int nrObjectives){
        long[] referencePoint = new long[nrObjectives];
        Arrays.fill(referencePoint, Integer.MIN_VALUE);
        return referencePoint;
    }

    public static Object[] getNadirPoint(ArrayList<Solution> al){
        Double[] referencePoint = (Double[])Arrays.copyOf(al.get(0).objectives,al.get(0).objectives.length);
	for(int i=1;i<al.size();i++){
		
            Double[] obj = (Double[])al.get(i).objectives;
            for(int j=0;j<obj.length;j++){
		if (obj[j]>referencePoint[j])
                    referencePoint[j] = obj[j];
		}
            }
            return referencePoint;
	}

    public static Object[] getNadirPoint(ArchiveSolutions al){
        Iterator<Solution> iter = al.iterator();
	Double[] retVal = new Double[al.getNthSolution(0).objectives.length];
	    Arrays.fill(retVal, Integer.MIN_VALUE);
            while(iter.hasNext()){
		Solution s=iter.next();
		for(int i=0;i<al.size();i++){
                    Double[] obj = (Double[])s.objectives;
                    for(int j=0;j<retVal.length;j++){
			if (obj[j]>retVal[j]) retVal[j] = obj[j];
                    }
		}
            }
	    return retVal;
	}
}
