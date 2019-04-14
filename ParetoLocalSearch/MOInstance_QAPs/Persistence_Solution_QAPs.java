/*
 * @author Madalina Drugan: 2010-2018
 */

package MOInstance_QAPs;

import Archives.*;
import java.io.*;
import java.util.*;
import general.*;

public class Persistence_Solution_QAPs implements  Serializable{

    static final long serialVersionUID = 101L;

    public static ArchiveSolutions generateSolution(){ //PerturbatorStrategies epsilon){
        return new NonDominatedArchive();//epsilon);
    }
    //read formated data for QAPs as another QAPs
    // attention with the indexes !!!
    public static ArchiveSolutions readFile_Solutions(String filename, QAPs problem)//, PerturbatorStrategies epsilon)
    {
        //boolean setMinus = false;
        int nrSolutions = 0;
        Solution_QAPs bestS;
        int[] tempStr = new int[problem.numberOfFacilities];
        long[] tempObj = new long[problem.numberOfObjectives];
        NonDominatedArchive bestNDA = new NonDominatedArchive(); //epsilon);
        try{
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String temp = in.readLine();
            //temp = in.readLine();
            while(temp != null && !temp.isEmpty()){
                String[] entries = temp.split("\\s+");
            
                //
                for(int i = 0; i < problem.numberOfFacilities;i++){
                    
                    if(i == problem.numberOfFacilities-1){ 
                       //String[] zonder = entries[i+1].split(")");
                       tempStr[i] = Integer.parseInt(entries[i+1].substring(0, entries[i+1].length()-1));
                    }
                    else{
                        String[] zonder = entries[i+1].split(",");
                        if(i == 0){  
                            //String[] zonder_ = zonder[0].split("(");
                            tempStr[i] = Integer.parseInt(zonder[0].substring(1));
                        } else{ 
                            tempStr[i] = Integer.parseInt(zonder[0]);
                        }
                    }
                }

                //
                for(int i = 0; i < problem.numberOfObjectives;i++){
                    tempObj[i] = Long.parseLong(entries[i+problem.numberOfFacilities+1]);
                }
            
                temp = in.readLine();  

                bestS = new Solution_QAPs(tempObj,tempStr); //,epsilon);
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
    public static Stack<ArchiveSolutions> ReadPeaks(String fileO, QAPs problem, int percent){ //, PerturbatorStrategies epsilon){
        Stack<ArchiveSolutions> currentSetArchive = new Stack<>();
        NonDominatedArchive currentArchive = new NonDominatedArchive(); //epsilon);
        Solution_QAPs s1;
        java.util.Random r = new java.util.Random();
        int[] tempB = new int[problem.numberOfFacilities];
        long[] tempA = new long[problem.numberOfObjectives];
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
                        tempB[i] = Integer.parseInt(entries[i]);
                    }
                for(int i=tempB.length;i<tempB.length+problem.numberOfObjectives;i++) {
                    tempA[i-tempB.length] = Long.parseLong(entries[i]);
                }
                s1 = new Solution_QAPs(tempA,tempB); //,epsilon);
                currentArchive.add(s1);
                
                temp = in.readLine();
         } catch (Exception e){
                System.out.println("Write not possible:" + fileO);
                e.printStackTrace(System.out);
         }
        return currentSetArchive;
    }


        // compute the ideal reference point
    public static long[] getIdealPoint(ArrayList<Solution> al){
        if(al == null || al.isEmpty())
            return null;
        Solution tempSol = al.get(0);
        int nrObj = tempSol.objectives.length;
        
        long[] referencePoint = new long[nrObj];
        for(int index = 0; index < nrObj; index++){
            referencePoint[index] = (Long)tempSol.objectives[index];
        }

        for(int i=1;i<al.size();i++){
            tempSol = al.get(i);
            Long[] tempItems = new Long[nrObj];
            for(int index = 0; index < nrObj; index++){
                tempItems[index] = (Long)tempSol.objectives[index];
            }
        
            for(int j=0;j<referencePoint.length;j++){
                if (tempItems[j]<referencePoint[j])
                    referencePoint[j] = tempItems[j];
            }
        }
	return referencePoint;
    }

    //////////////////////
    public static long[] getIdealPoint(ArchiveSolutions al, Object[] offset){
        if(al == null || al.size() == 0) {
            return null;
        }
        Iterator<Solution> iter = al.iterator();
        Solution s1 = iter.next();
        int nrObj = s1.objectives.length;
        //long[] referencePoint = Arrays.copyOf(al.get(0).getObjectives(), nrObj);
        long[] referencePoint = new long[nrObj];
        for(int index = 0; index < nrObj; index++){
            referencePoint[index] = (Long)s1.objectives[index];
        }
        while(iter.hasNext()){
            s1=iter.next();
            
            Long[] tempItems = new Long[nrObj];
            for(int index = 0; index < nrObj; index++){
                tempItems[index] = (Long)s1.objectives[index];
            }

            for(int j=0;j<referencePoint.length;j++){
                if(tempItems[j] < referencePoint[j]) {
                    referencePoint[j] = tempItems[j];
                }
            }
        }
        
        for(int i = 0; i < referencePoint.length; i++) {
            referencePoint[i] -= (Long)offset[i];
        }
	return referencePoint;
    }

    public static Object[] setIdealPoint(Object[] referencePoint, Object[] objectives, Object[] offset){
        for(int j=0;j<referencePoint.length;j++){
            if((Long)objectives[j]<(Long)referencePoint[j]) {
                referencePoint[j] = (Long)objectives[j] - (Long)offset[j];
            }
	}
        return referencePoint;
    }

    public static Long[] resetIdealPoint(int nrObjectives){
        Long[] referencePoint = new Long[nrObjectives];
        Arrays.fill(referencePoint, Integer.MAX_VALUE);
        return referencePoint;
    }

    public static long[] resetNadirPoint(int nrObjectives){
        long[] referencePoint = new long[nrObjectives];
        Arrays.fill(referencePoint, Integer.MIN_VALUE);
        return referencePoint;
    }

}
