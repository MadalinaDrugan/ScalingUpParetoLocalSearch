/*
 *  @author Madalina Drugan: 2010-2018
 */

package MOInstance_QAPs;

import general.*;
import java.util.*;
import java.io.*;
import org.apache.commons.lang3.ArrayUtils;

public class QAPs extends ProblemInstance implements java.io.Serializable{

    final static long serialVersionUID = 11239585071342354L;//10795; //
    final static java.util.Random r = new java.util.Random();
        
    public static int type_QAPs = 0;

    private double correlation;
    public int max_flow;
    public int max_distance;
        
    public int[][] a;
    public int[][][] b;
	
        
    public QAPs(int numberOfFac, int numberOfObj, double corr, int max_flow, int max_distance, int[][] a, int[][][] b){
	numberOfObjectives = numberOfObj;
        numberOfFacilities = numberOfFac;
                
	a = new int[numberOfFacilities][numberOfFacilities];
        b = new int[numberOfObjectives][numberOfFacilities][numberOfFacilities];
                                
	correlation = corr;
        this.max_distance = max_distance;
        this.max_flow = max_flow;
                        
        for(int i=0;i<numberOfFacilities;i++) {
            for (int j = 0; j < numberOfFacilities; j++) {
                this.a[i][j] = a[i][j];
            }
        }
        for(int k = 0; k < numberOfObjectives; k++) {
            for (int i = 0; i < numberOfFacilities; i++) {
                for (int j = 0; j < numberOfFacilities; j++) {
                    this.b[k][i][j] = b[k][i][j];
                }
            }
        }

    }
	
	public QAPs(double correlation, int max_flow, int max_distance,int[][] a, int[][][] b){
		numberOfObjectives = a.length;
                this.numberOfFacilities = b.length;
                
		this.a = new int[numberOfFacilities][numberOfFacilities];
                this.b = new int[numberOfObjectives][numberOfFacilities][numberOfFacilities];
                                
		this.correlation = correlation;
                this.max_distance = max_distance;
                this.max_flow = max_flow;
                        
                for(int i=0;i<numberOfFacilities;i++) {
                    for(int j = 0; j < numberOfFacilities; j++) {
                        this.a[i][j] = a[i][j];
                    }
                }
                for(int k = 0; k < numberOfObjectives; k++) {
                    for(int i=0;i<numberOfFacilities;i++)
                        for(int j=0;j<numberOfFacilities;j++){
                            this.b[k][i][j] = b[k][i][j];
                        }
        }
	}
	
        //overwrite the copy constructor
        public QAPs(String pathInitFile, int typeFile, int nrDim){
            QAPs newQAPs = new QAPs();
             
            type_QAPs = typeFile;
             
            if(typeFile == 1){
                newQAPs = Persistence_QAPs.readFile_QAPs_unstruct(pathInitFile, nrDim);
            } else {
                newQAPs = Persistence_QAPs.readFile_QAPs_struct(pathInitFile, nrDim);
            }
            correlation = newQAPs.correlation;
            max_distance = newQAPs.max_distance;
            max_flow = newQAPs.max_flow;
            numberOfFacilities = newQAPs.numberOfFacilities;
            numberOfObjectives = newQAPs.numberOfObjectives;
		
            this.a = new int[numberOfFacilities][numberOfFacilities];
            this.b = new int[numberOfObjectives][numberOfFacilities][numberOfFacilities];
                
            for(int i=0;i<numberOfFacilities;i++) {
                for (int j = 0; j < numberOfFacilities; j++) {
                    a[i][j] = newQAPs.a[i][j];
                }
            }
                
            for(int k = 0; k < numberOfObjectives; k++) {
                for (int i = 0; i < numberOfFacilities; i++) {
                    for (int j = 0; j < numberOfFacilities; j++) {
                        b[k][i][j] = newQAPs.b[k][i][j];
                    }
                }
            }
        }
             
        public QAPs(QAPs newQAPs){
            this.correlation = newQAPs.correlation;
            this.max_distance = newQAPs.max_distance;
            this.max_flow = newQAPs.max_flow;
            this.numberOfFacilities = newQAPs.numberOfFacilities;
            this.numberOfObjectives = newQAPs.numberOfObjectives;
		
            this.a = new int[numberOfFacilities][numberOfFacilities];
            this.b = new int[numberOfObjectives][numberOfFacilities][numberOfFacilities];
                
            for(int i=0;i<numberOfFacilities;i++)
                    for(int j=0;j<numberOfFacilities;j++)
			{
				this.a[i][j] = newQAPs.a[i][j];
			}
                
            for(int k = 0; k < numberOfObjectives; k++)
                    for(int i=0;i<numberOfFacilities;i++)
			for(int j=0;j<numberOfFacilities;j++)
			{
				this.b[k][i][j] = newQAPs.b[k][i][j];
			}
        }
        
        public QAPs(){
            
        }
       
        public void restarts(){
            
        } 
        
    @Override
    public Solution_QAPs init(Variator var){//, PerturbatorStrategies e){
            //generate another random solution
            Integer[] perm = ArrayUtils.toObject(Permutation.randomizePermutation(numberOfFacilities));
            Long[] obj = (Long[]) computeSolution(perm);
            Solution_QAPs tempS = new Solution_QAPs(obj,perm); 
            var.updateNrSwaps(this.numberOfFacilities);
            return tempS;
        }
        
        public final static QAPs readFileInObject(String pathInitFile, int dim){
            //read the file into a string:
            if(type_QAPs == 1) {
                return new QAPs(Persistence_QAPs.readFile_QAPs_unstruct(pathInitFile, dim));
            } else {
                return new QAPs(Persistence_QAPs.readFile_QAPs_struct(pathInitFile, dim));
            }
	}
                
    @Override
    public Solution computeSolution(Solution s1){
        Integer[] tempItems = new Integer[numberOfFacilities];
        for(int j = 0; j < numberOfFacilities; j++) {
            for (int k = j + 1; k < numberOfFacilities; k++) {
                 
                for(int i = 0; i < numberOfFacilities; i++){
                    tempItems[i] = (Integer)s1.items[i];
                }

                for(int i = 0; i < numberOfObjectives; i++){
                    if (a[j][k] == a[k][j] & b[i][tempItems[j]][tempItems[k]] == b[i][tempItems[k]][tempItems[j]]) {
                        s1.objectives[i] =+ (long)(2 * a[j][k] * b[i][tempItems[j]][tempItems[k]]);
                    } else {
                        System.out.println("Matrices are not symetrical");
                    }
                }
                
                //s1.objectives = tempObj;
            }
        }
        return s1;
    }

    public Solution computeSolution(Solution s1, int i){
        
        Long[] tempObj = new Long[numberOfObjectives];
        for(int index = 0; index < numberOfObjectives; index++){
            if(index != i)
                tempObj[index] = (Long)s1.objectives[index];
            else 
                tempObj[index] = (long)0;
        }
        
        Integer[] tempItems = new Integer[numberOfFacilities];
        for(int index = 0; index < numberOfFacilities; index++){
            tempItems[index] = (Integer)s1.items[index];
        }
        
        for (int j = 0; j < numberOfFacilities; j++) {
            for (int k = j + 1; k < numberOfFacilities; k++) {
                if (a[j][k] == a[k][j] & b[i][tempItems[j]][tempItems[k]] == b[i][tempItems[k]][tempItems[j]]) {
                    tempObj[i] += 2 * a[j][k] * b[i][tempItems[j]][tempItems[k]];
                } else {
                    System.out.println("Matrices are not symetrical");
                }
            }
        }
        return s1;
    }
 
    public boolean computeSolutionTest(Solution s1){
        long[] temp = new long[numberOfObjectives];
        for(int i = 0; i < numberOfObjectives; i++){
            temp[i] = 0;
            for(int j = 0; j < numberOfFacilities; j++) {
                for (int k = j + 1; k < numberOfFacilities; k++) {
                    if (a[j][k] == a[k][j] & b[i][(Integer)s1.items[j]][(Integer)s1.items[k]] == b[i][(Integer)s1.items[k]][(Integer)s1.items[j]]) {
                        temp[i] += 2 * a[j][k] * b[i][(Integer)s1.items[j]][(Integer)s1.items[k]];
                    } else {
                        System.out.println("Matrices are not symetrical");
                    }
                }
            }
            if((Long)s1.objectives[i] != temp[i]) {
                return false;
            }
        }
            
        return true;
    }


    public PartialSolution_QAPs computeSolution(PartialSolution_QAPs s1){
        Arrays.fill(s1.objectives, 0);
        for(int j = 0; j < numberOfFacilities; j++) {
                for (int k = j + 1; k < numberOfFacilities; k++) {
                    
                    for(int i = 0; i < numberOfObjectives; i++){

                    if (a[j][k] == a[k][j] & b[i][s1.items[j]][s1.items[k]] == b[i][s1.items[k]][s1.items[j]]) {
                        s1.objectives[i] += 2 * a[j][k] * b[i][s1.items[j]][s1.items[k]];
                    } else {
                        System.out.println("Matrices are not symetrical");
                    }
                }
            }
        }
        return s1;
    }

    @Override
    public ArchiveSolutions computeSolution(ArchiveSolutions nda){
            Iterator<Solution> iterator = nda.iterator();
            while(iterator.hasNext()){
                Solution_QAPs s1 = (Solution_QAPs) iterator.next();

                this.computeSolution(s1);
                if(s1.set != null){

                    Iterator<PartialSolution_QAPs> iteratorV = s1.set.iterator();
                    while(iteratorV.hasNext()){
                        PartialSolution_QAPs temp = iteratorV.next();
                        this.computeSolution(temp);
                    }
                }
            }
            return nda;
        }
        

        public ArchiveSolutions computeSolution(ArchiveSolutions nda, int t){
            Iterator<Solution> iterator = nda.iterator();
            while(iterator.hasNext()){
                Solution_QAPs s1 = (Solution_QAPs) iterator.next();
                this.computeSolution(s1);

                if(s1.set != null){
                    Iterator<PartialSolution_QAPs> iteratorV = s1.set.iterator();
                    while(iteratorV.hasNext()){
                        PartialSolution_QAPs temp = iteratorV.next();
                        this.computeSolution(temp);
                    }
                }
            }

            return nda;
        }

        public boolean computeSolutionTest(ArchiveSolutions nda){
            Iterator<Solution> iterator = nda.iterator();
            while(iterator.hasNext()){
                Solution_QAPs s1 = (Solution_QAPs) iterator.next();

                long[] temp = new long[numberOfObjectives];
                for(int i = 0; i < numberOfObjectives; i++){
                    temp[i] = 0;
                    for(int j = 0; j < numberOfFacilities; j++) {
                        for(int k = j+1; k < numberOfFacilities; k++){
                            if(a[j][k] == a[k][j] & b[i][(Integer)s1.items[j]][(Integer)s1.items[k]] == b[i][(Integer)s1.items[k]][(Integer)s1.items[j]]) {
                                temp[i] = temp[i]+ 2*a[j][k]*b[i][(Integer)s1.items[j]][(Integer)s1.items[k]];
                            }
                        else {
                                System.out.println("Matrices are not symetrical");
                            } 
                    }
                    }
                    if((Long)s1.objectives[i] != temp[i]) {
                        return false;
                    }
                }
            }
            return true;
        }
        
    @Override
    public Object[] computeSolution(Object[] rep){
        Long[] objectives = new Long[numberOfObjectives];
        for(int i = 0; i < this.numberOfObjectives; i++){
            objectives[i] = new Long(0);
        }
                    
        for(int i = 0; i < numberOfObjectives; i++){
            //objectives[i] = 0;
            for(int j = 0; j < numberOfFacilities; j++){
                for(int k = j+1; k < numberOfFacilities; k++){
                    if(a[j][k] == a[k][j] & b[i][(Integer)rep[j]][(Integer)rep[k]] == b[i][(Integer)rep[k]][(Integer)rep[j]]) {
                        objectives[i] = objectives[i] + 2 * a[j][k] * b[i][(Integer)rep[j]][(Integer)rep[k]];
                    }else {
                        System.out.println("Matrices are not symetrical");
                    }
                }
            }
        }
            
        return objectives;
    }
        
    public long[] computeSolution(int[][] distance, int[][][] flow, int[] rep){
        long[] objectives = new long[flow.length];
            int i = 0;
            int j = 0; 
            int k = 0;
          
          try{
            for(i = 0; i < flow.length; i++){
                objectives[i] = 0;
                for(j = 0; j < distance.length-1; j++){
                    for(k = j+1; k < distance.length; k++){
                       if(distance[j][k] == distance[k][j] && flow[i][rep[j]][rep[k]] == flow[i][rep[k]][rep[j]]) {
                            objectives[i] += 2 * distance[j][k] * flow[i][rep[j]][rep[k]];
                       }else {
                            System.out.println("Matrices are not symetrical");
                       }
                    }
                }
            }
          } catch(Exception e){
            System.out.println("Write not possible:"+i +"\t" +j +"\t" + k + "\t");
            System.out.println("Write not possible:"+distance[j][k]  +"\t" + flow[i][rep[j]][rep[k]]+"\t");
            e.printStackTrace(System.out);
          }
          
          return objectives;
        }
        
        @Override
	public String toString(){
	
            StringBuilder sb = new StringBuilder();
            sb.append("Describe the space \n");
            sb.append("First matrix: \n");
        
            for (int[] a1 : a) {
                sb.append("\t");
                for (int j = 0; j <a[1].length; j++) {
                    sb.append(a1[j] + "\t");
                }
                sb.append("\n");
            }
	
            sb.append("Second Matrix: \n");
		for(int i=0;i<b.length;i++){
                    sb.append("\t");
                    for(int j = 0; j <b[1].length; j++){
                        sb.append("\t");
                        for(int k = 0; k < b[1][1].length; k++)
                            sb.append(b[i][j][k]+"\t");
                    }
                    sb.append("\n");
		}                
		return sb.toString();
	}	
	
        
    @Override
        public void writeToFile(String nameFile, ArchiveSolutions nda, boolean append){
            Persistence_QAPs.writeToFile(nameFile, nda,append);
        }
        
    @Override
        public void writeNDA(String outputFile, ArchiveSolutions totalNDA){
                  try{
           File myFile = new File(outputFile+"_FinalNDA");
           FileWriter fileStream = new FileWriter(myFile,true);
           if(myFile.exists() & myFile.canWrite()){} 
           else{
                  System.out.println("The file " +outputFile + " cannot be opened");
                        //fileStream = new FileWriter(myFile); 
                  myFile.createNewFile();
                  fileStream = new FileWriter(myFile);
           }
           BufferedWriter fos = new BufferedWriter(fileStream);
           Iterator<Solution> iterator = totalNDA.iterator();
           while(iterator.hasNext()){
                 Solution_QAPs s1 = (Solution_QAPs)iterator.next();
                 for(int i = 0; i < s1.objectives.length; i++)
                    fos.write(Long.toString((Long)s1.objectives[i]) + "   ");
                 fos.write("  (");
                 for(int i = 0; i < s1.items.length; i++)
                     fos.write(s1.items[i]+", ");
                 fos.write(")\n");
           }
                    
           fos.newLine();
           fos.close();
        }catch (Exception e){
            System.out.println("Write not possible:"+outputFile);
            e.printStackTrace(System.out);
        }

        }
        
    @Override
    public int getType(){
        return type_QAPs;
    }

   Stack<Integer> diffTemp1 = new Stack<>();
   Stack<Integer> diffTemp2 = new Stack<>();

   double getDistance(Object[] s, Object[] s1){
        int dist = 0;
            diffTemp1.removeAllElements();
            diffTemp2.removeAllElements();
            for(int i = 0; i < s.length;i++){
                if(s[i] != s1[i]){
                    diffTemp1.add((Integer)s1[i]);
                    diffTemp2.add((Integer)s[i]);
                }
            }
            
            while(diffTemp1.size() > 1){
                int tempIndex = diffTemp1.indexOf(diffTemp2.elementAt(0));
                diffTemp1.setElementAt(diffTemp1.elementAt(0), tempIndex);

                diffTemp2.remove(0);
                diffTemp1.remove(0);

                dist++;
            }
            return dist;
        }

    @Override
   public Solution generateSolutions(){//PerturbatorStrategies e){
       Solution_QAPs temp = new Solution_QAPs();
       return temp;
   }

   public Solution generateSolutions(long[] tempA, int[] tempB){
       Solution_QAPs temp = new Solution_QAPs(tempA,tempB);
       return temp;
   }

    @Override
   public ArchiveSolutions bestFile(String fileI){ //, PerturbatorStrategies epsilon){
       return Persistence_Solution_QAPs.readFile_Solutions(fileI, this); //,epsilon);
   }
   
}
