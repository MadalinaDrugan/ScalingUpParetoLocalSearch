/*
 * @author Madalina Drugan: 2010-2018
 */

package MOInstance_QAPs;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import general.*;

public class Persistence_QAPs implements Serializable{

    static final long serialVersionUID = 101L; 

    ArrayList types = new ArrayList();
    ArrayList al = new ArrayList();
    
    Persistence_QAPs(){
        types = new ArrayList<>();
        al = new ArrayList<>();
    }
    
    //read formated data for QAPs as another QAPs
    // attention with the indexes !!! 
    public static QAPs readFile_QAPs_unstruct(String filename, int nrItemsFirstLine)
    {                        
        QAPs newQAPs = new QAPs();
                       
        int[] limitA;
        int[][] limitB;
        if(nrItemsFirstLine == -1){
                nrItemsFirstLine = 19;
        }

        limitA = new int[2];
        limitA[0] = nrItemsFirstLine;
        limitB = new int[2][2];
      try{  
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String temp = in.readLine();
        //process the header of the function
        //multi-objective QAP
        if(temp!=null &&  nrItemsFirstLine == 19){
              String[] entries = temp.split("\\s+");
                    
              //facilities: entries[2]
              newQAPs.numberOfFacilities = Integer.parseInt(entries[2]);
              newQAPs.a = new int[newQAPs.numberOfFacilities][newQAPs.numberOfFacilities];
              limitA[1] = nrItemsFirstLine + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities;
                    
              //objectives: entries[5]                    
              newQAPs.numberOfObjectives = Integer.parseInt(entries[5]);
              newQAPs.b = new int[newQAPs.numberOfObjectives][newQAPs.numberOfFacilities][newQAPs.numberOfFacilities];
                          
              limitB = new int[newQAPs.numberOfObjectives][2];
              for(int j = 0; j < newQAPs.numberOfObjectives; j++){
                   limitB[j][0] = nrItemsFirstLine + j + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities*(j+1);
                   limitB[j][1] = nrItemsFirstLine + j + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities*(j+2); 
              }
                   
         } else {
            //single objective QAP
             String[] entries = temp.split("\\s+");
              newQAPs.numberOfFacilities = Integer.parseInt(entries[1]);
              newQAPs.a = new int[newQAPs.numberOfFacilities][newQAPs.numberOfFacilities];
              limitA[1] = nrItemsFirstLine + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities;

              //objectives: entries[5]
              newQAPs.numberOfObjectives = 1; //Integer.parseInt(entries[5]);
              newQAPs.b = new int[newQAPs.numberOfObjectives][newQAPs.numberOfFacilities][newQAPs.numberOfFacilities];

              limitB = new int[newQAPs.numberOfObjectives][2];
              for(int j = 0; j < newQAPs.numberOfObjectives; j++){
                   limitB[j][0] = nrItemsFirstLine + j + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities*(j+1);
                   limitB[j][1] = nrItemsFirstLine + j + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities*(j+2);
              }

         }

         temp = in.readLine();
         int count = 0; // number of lines
         int countE = 0; // number of elements in a line
                
         while(temp != null){
                    if(temp.contentEquals("")){
                        temp = in.readLine();
                        continue;
                    }
                    String[] entries = temp.split("\\s+");
                    countE += entries.length;
                    
                    // read A - the distance matrix
                    if(count < newQAPs.numberOfFacilities){
                        //count is the line number
                        if(entries[0].contentEquals("")){
                             for(int i=1;i<entries.length;i++) {
                                newQAPs.a[count][i - 1] = Integer.parseInt(entries[i]);
                             }
                        } else {
                             for(int i=0;i<entries.length;i++) {
                                    newQAPs.a[count][i] = Integer.parseInt(entries[i]);
                             }
                        }
                     } else{
                        //read one of B - the flow matrices
                        //compute in which matrix is
                        int thisObj = count/newQAPs.numberOfFacilities-1;
                        int thisLine = count%newQAPs.numberOfFacilities;
                        
                            //System.out.println(Integer.toString(count)+ ": line "+Integer.toString(thisObj)+ ", colomn "+Integer.toString(thisLine) + " element "+ entries);
                            if(entries[0].contentEquals("")){
                                for(int i = 1; i < entries.length; i++) {
                                    newQAPs.b[thisObj][thisLine][i - 1] = Integer.parseInt(entries[i]);
                                }
                            } else {
                            for(int i = 0; i < entries.length; i++) {
                                newQAPs.b[thisObj][thisLine][i] = Integer.parseInt(entries[i]);
                            }
                        }
                    }

                    if( countE >= newQAPs.numberOfFacilities){
                        count++;
                        countE = 0;
                    }
                    temp = in.readLine();
        }
        return newQAPs;
    } catch (FileNotFoundException e) {
	System.out.println("Read not possible:");
	System.out.println(e);
	System.exit(0);
    } catch (IOException e) {
	System.out.println("Read not possible:");
	System.out.println(e);
	System.exit(0);
    }
    return null;
    }

    //read formated data for QAPs as another QAPs
    // attention with the indexes !!! 
    public final static QAPs readFile_QAPs_struct(String filename, int nrItemsFirstLine)
    {
        try{
            QAPs newQAPs = new QAPs();
            int[] limitA;
            int[][] limitB;
            if(nrItemsFirstLine == -1){
                nrItemsFirstLine = 19;
            }

            limitA = new int[2];
            limitA[0] = nrItemsFirstLine;
            limitB = new int[2][2];
		
            BufferedReader in = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String temp = in.readLine();
                //process the header of the finction
            if(temp!=null && nrItemsFirstLine == 19){
                    String[] entries = temp.split("\\s+");
                    
                    //facilities: entries[2]
                    newQAPs.numberOfFacilities = Integer.parseInt(entries[2]);
                    newQAPs.a = new int[newQAPs.numberOfFacilities][newQAPs.numberOfFacilities];
                    limitA[1] = nrItemsFirstLine + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities;
                    
                    //objectives: entries[5]                    
                    newQAPs.numberOfObjectives = Integer.parseInt(entries[5]);
                    newQAPs.b = new int[newQAPs.numberOfObjectives][newQAPs.numberOfFacilities][newQAPs.numberOfFacilities];
                          
                    limitB = new int[newQAPs.numberOfObjectives][2];
                          for(int j = 0; j < newQAPs.numberOfObjectives; j++){
                              limitB[j][0] = nrItemsFirstLine + j + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities*(j+1);
                              limitB[j][1] = nrItemsFirstLine + j + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities*(j+2); 
                          }
                   //max_distance: entry[8] 
                   newQAPs.max_distance = Integer.parseInt(entries[8]);
                   //max_flow: entry[12]
                   newQAPs.max_flow = Integer.parseInt(entries[12]);
                   //correlation
                   //newQAPs.correlation = Double.parseDouble(entries[21]);
            } else if(temp != null && nrItemsFirstLine == 1){
                newQAPs.numberOfFacilities = Integer.parseInt(temp);

                newQAPs.a = new int[newQAPs.numberOfFacilities][newQAPs.numberOfFacilities];
                limitA[1] = nrItemsFirstLine + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities;

                    //objectives: entries[5]
                newQAPs.numberOfObjectives = 1;
                newQAPs.b = new int[newQAPs.numberOfObjectives][newQAPs.numberOfFacilities][newQAPs.numberOfFacilities];

                limitB = new int[newQAPs.numberOfObjectives][2];
                    for(int j = 0; j < newQAPs.numberOfObjectives; j++){
                              limitB[j][0] = nrItemsFirstLine + j + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities*(j+1);
                              limitB[j][1] = nrItemsFirstLine + j + newQAPs.numberOfFacilities * newQAPs.numberOfFacilities*(j+2);
                    }
            }
                
		temp = in.readLine();
                int count = 0;
                
                while(temp != null){
		    if(temp.contentEquals("")){
                        temp = in.readLine();
                        continue;
                    }
                    String[] entries = temp.split("\\s+");
                    
                    //read A
                    if(count < newQAPs.numberOfFacilities){
                        //count is the line number
                        if(entries[0].contentEquals("")){
                            for(int i=1;i<entries.length;i++) {
                                newQAPs.a[count][i - 1] = Integer.parseInt(entries[i]);
                            }
                         } else {
                            for(int i=0;i<entries.length;i++) {
                                newQAPs.a[count][i] = Integer.parseInt(entries[i]);
                            }
                        }
                      }else{
                        //read one of B
                        //compute in which matrix is
                        int thisObj = (int) count/newQAPs.numberOfFacilities-1;
                        int thisLine = count%newQAPs.numberOfFacilities;
                        
                        //System.out.println(Integer.toString(count)+ ": line "+Integer.toString(thisObj)+ ", colomn "+Integer.toString(thisLine) + " element "+ entries);
                        if(entries[0].contentEquals("")){                        
                            for(int i = 1; i < entries.length; i++)
                              newQAPs.b[thisObj][thisLine][i-1] = Integer.parseInt(entries[i]);
                        } else {
                            for(int i = 0; i < entries.length; i++)
                              newQAPs.b[thisObj][thisLine][i] = Integer.parseInt(entries[i]);                            
                        }
                      }
                    count++;
				temp = in.readLine();      
                }
                //check the readen a and b matrixex
                return newQAPs;
                
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Read not possible:");
			System.out.println(e);
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("Read not possible:");
			System.out.println(e);
			System.exit(0);
		}
        return null;
    }

    //format the output for the performance software 
    public final static void writeToFile(String filename, ArchiveSolutions nda, boolean append)
    {
        if(nda == null || nda.size() == 0){
            return;
        }

        try{
                    File myFile = new File(filename);
                    FileWriter fileStream = new FileWriter(myFile,append);
                    if(append == true & myFile.exists() & myFile.canWrite()){
                        } else{
                        System.out.println("The file " +filename + " cannot be opened");
                        //fileStream = new FileWriter(myFile); 
                        myFile.createNewFile();
                        fileStream = new FileWriter(myFile);
                    }
                    BufferedWriter fos = new BufferedWriter(fileStream);
                        
                    File myFile_sol = new File(filename+"_sol");
                    FileWriter fileStream_sol = new FileWriter(myFile_sol,append);
                    if(append == true & myFile.exists() & myFile.canWrite()){
                        } else{
                        System.out.println("The file " +filename + " cannot be opened");
                        //fileStream = new FileWriter(myFile); 
                        myFile.createNewFile();
                        fileStream = new FileWriter(myFile_sol);
                    }
                    BufferedWriter fos_sol = new BufferedWriter(fileStream_sol);
   
                    Iterator<Solution> thisIterator = nda.iterator();
                    while(thisIterator.hasNext()){
                        //Iterator<PartialSolution_QAPs> thisS = thisIterator.next().set.iterator();
                        Solution_QAPs thisQAPs = (Solution_QAPs)thisIterator.next();
                        //while(thisS.hasNext()){

                          //PartialSolution_QAPs thisQAPs = thisS.next();
                          for(int i = 0; i < thisQAPs.objectives.length; i++){
                                fos.write(Long.toString((Long)thisQAPs.objectives[i])+ " ");
                            }
                          fos.newLine();
                          
                          fos_sol.write(" (");
                          for(int i = 0; i < thisQAPs.items.length-1; i++)
                            fos_sol.write(Long.toString((Integer)thisQAPs.items[i])+ ", ");
                          fos_sol.write(Long.toString((Integer)thisQAPs.items[thisQAPs.items.length-1])+ ") ");
                          
                          for(int i = 0; i < thisQAPs.objectives.length; i++){
                                fos_sol.write(Long.toString((Long)thisQAPs.objectives[i])+ " ");
                          }
                          fos_sol.newLine();
                            
                        //}
                   }
                   fos.newLine();
                   fos.close();

                   fos_sol.newLine();
                   fos_sol.close();

                }
	catch (Exception e){
			System.out.println("Write not possible:"+filename);
			e.printStackTrace(System.out);
	}
    }    
}
