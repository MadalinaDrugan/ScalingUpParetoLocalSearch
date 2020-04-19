/*
 * @author Madalina Drugan: 2010-2018
 */

package MOInstance_NK;

//import knapsack_optimal.Persistence;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import general.*;

public class Persistence_NK implements Serializable{

    static final long serialVersionUID = 101L; 

    ArrayList types = new ArrayList();
    ArrayList al = new ArrayList();
    
    Persistence_NK(){
        types = new ArrayList<>();
        al = new ArrayList<>();
    }
    
    //read formated data for NK
    // attention with indexes !!! 
    public static NK readFile_NK(String filename)
    {                        
        NK newNK = new NK();
                            
        try{  
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String temp = in.readLine();
            temp = in.readLine();
            temp = in.readLine();
            temp = in.readLine();
            
            String[] entries = temp.split("\\s+");
                    
            newNK.correlation = Double.parseDouble(entries[2]);
            newNK.numberOfObjectives = Integer.parseInt(entries[3]);
            newNK.numberOfFacilities = Integer.parseInt(entries[4]);
            newNK.nr_links = Integer.parseInt(entries[5]);
            
            newNK.linksEachLocus = new int[newNK.numberOfFacilities][newNK.nr_links];
            newNK.contribution = new double[newNK.numberOfObjectives][newNK.numberOfFacilities][(int)Math.pow(2,newNK.nr_links+1)];
            
            temp = in.readLine();

            for(int i = 0; i < newNK.numberOfFacilities; i++){
                temp = in.readLine();

                for(int j = 0; j < newNK.nr_links; j++){
                    temp = in.readLine();
                    String[] obj = temp.split("\\s+");
                    
                    if(obj.length != newNK.numberOfObjectives){
                        System.err.print("String should be equal with the obj");
                    } else {
                        //for(int k = 0; k < newNK.numberOfObjectives; k++){
                        newNK.linksEachLocus[i][j] = Integer.parseInt(obj[0]);
                        //}
                    }
                }
            }
            
            temp = in.readLine();

            for(int i = 0; i < newNK.numberOfFacilities; i++){

                for(int j = 0; j < (int) Math.pow(2,newNK.nr_links+1); j++){
                    
                    temp = in.readLine();
                    String[] obj = temp.split("\\s+");
                    
                    if(obj.length != newNK.numberOfObjectives){
                         System.err.print("String should be equal with the obj");
                    } else {
                        for(int k = 0; k < newNK.numberOfObjectives; k++){
                            newNK.contribution[k][i][j] = Double.parseDouble(obj[k]);
                        }
                    }
                }
            }
            
         return newNK;
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
                myFile.createNewFile();
                fileStream = new FileWriter(myFile);
            }
            
            BufferedWriter fos = new BufferedWriter(fileStream);
                        
            File myFile_sol = new File(filename+"_sol");
            FileWriter fileStream_sol = new FileWriter(myFile_sol,append);
            if(append == true & myFile.exists() & myFile.canWrite()){
            } else{
                System.out.println("The file " +filename + " cannot be opened");
                myFile.createNewFile();
                fileStream = new FileWriter(myFile_sol);
            }
            BufferedWriter fos_sol = new BufferedWriter(fileStream_sol);
   
            Iterator<Solution> thisIterator = nda.iterator();
            while(thisIterator.hasNext()){
                Solution_NK thisNK = (Solution_NK)thisIterator.next();
                
                int nrObj = thisNK.objectives.length;
                double[] tempObj = new double[nrObj];
                for(int i = 0; i < nrObj; i++){
                    tempObj[i] = (double)thisNK.objectives[i];
                }
                
                int nrItems = thisNK.items.length;
                boolean[] tempItems = new boolean[nrItems];
                for(int i = 0; i < nrItems; i++){
                    tempItems[i] = (boolean)thisNK.items[i];
                }
                
                for(int i = 0; i < nrObj; i++){
                    fos.write(Double.toString(tempObj[i])+ " ");
                }
                fos.newLine();
                          
                fos_sol.write(" (");
                for(int i = 0; i < nrItems; i++)
                    fos_sol.write(Boolean.toString(tempItems[i])+ ", ");
                fos_sol.write(Boolean.toString(tempItems[nrItems-1])+ ") ");
                          
                for(int i = 0; i < nrObj; i++){
                    fos_sol.write(Double.toString(tempObj[i])+ " ");
                }
                fos_sol.newLine();
                            
            }
            fos.newLine();
            fos.close();

            fos_sol.newLine();
            fos_sol.close();

        }catch (Exception e){
        	System.out.println("Write not possible:"+filename);
            	e.printStackTrace(System.out);
	}
    }    

    
}
