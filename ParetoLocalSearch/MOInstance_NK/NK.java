/*
 * @author Madalina Drugan: 2010-2018
 */

package MOInstance_NK;

import general.*;
import java.util.*;
import java.io.*;

public class NK extends ProblemInstance implements java.io.Serializable{

    final static long serialVersionUID = 11239585071342354L;//10795; //
    final static java.util.Random r = new java.util.Random();
        
    public static int type_QAPs = 1;

    public double correlation;
    public int nr_links;
        
    public int[][] linksEachLocus;
    public double[][][] contribution;
	
    Stack<String> K_bits = new Stack<>();
    
    public NK(int numberOfFac, int numberOfObj, int nrL, double corr, int[][] a, int[][][] b){
	numberOfObjectives = numberOfObj;
        numberOfFacilities = numberOfFac;
        nr_links = nrL;        
        
	linksEachLocus = new int[numberOfFac][nrL];
        contribution = new double[numberOfObj][numberOfFac][(int)Math.pow(2, nrL+1)];
                                
	correlation = corr;
                        
        for(int i=0;i<numberOfFacilities;i++) {
            System.arraycopy(a[i], 0, linksEachLocus[i], 0, nrL);
        }
        
        for(int k = 0; k < numberOfObjectives; k++) {
            for (int i = 0; i < numberOfFacilities; i++) {
                for (int j = 0; j < (int)Math.pow(2, nrL+1); j++) {
                    contribution[k][i][j] = b[k][i][j];
                }
            }
        }
        
        set_K_bits(nrL);
     }
	
    private void set_K_bits(int nrL){
        //K_bits = new String[(int)Math.pow(2, nrL)];
        Integer[] tempN = new Integer[nrL];
        String tempS = new String();
        for(int i = 0; i < nrL; i++){
            tempN[i] = 0;
            tempS = tempS.concat(tempN[i].toString());
        }
        K_bits.add(tempS);
        
        for(int j = 1; j < (int)Math.pow(2, nrL+1); j++) {
            tempS = new String();
            for(int i = nrL-1; i >= 0; i--){
                if(tempN[i] == 0){ 
                    tempN[i] = 1;
                    break;
                }
                tempN[i] = 0;    
            }
            
            for(int i = 0; i < nrL; i++){
                tempS = tempS.concat(tempN[i].toString());
            }
            
            K_bits.add(tempS);
        }        
    }
    
    public NK(double corr, int[][] a, double[][][] b){
	numberOfObjectives = b.length;
        numberOfFacilities = a.length;
        nr_links = a[0].length; 
                
	linksEachLocus = new int[numberOfFacilities][nr_links];
        contribution = new double[numberOfObjectives][numberOfFacilities][(int)Math.pow(2, nr_links+1)];
                                
	correlation = corr;

        for(int i=0;i<numberOfFacilities;i++) {
            System.arraycopy(a[i], 0, linksEachLocus[i], 0, nr_links);
        }
                
        for(int k = 0; k < numberOfObjectives; k++) {
            for(int i=0;i<numberOfFacilities;i++)
                System.arraycopy(b[k][i], 0, contribution[k][i], 0, (int)Math.pow(2, nr_links+1));
        }

        set_K_bits(nr_links);
    }
	
    //overwrite the copy constructor
    public NK(String pathInitFile, int typeFile, int nrDim){
        NK newNK = new NK();
             
        type_QAPs = typeFile;
             
        newNK = Persistence_NK.readFile_NK(pathInitFile);

        correlation = newNK.correlation;
        numberOfFacilities = newNK.numberOfFacilities;
        numberOfObjectives = newNK.numberOfObjectives;
        nr_links = newNK.nr_links;
		
        linksEachLocus = new int[numberOfFacilities][nr_links];
        contribution = new double[numberOfObjectives][numberOfFacilities][(int)Math.pow(2, nr_links+1)];
                
        for(int i=0;i<numberOfFacilities;i++) {
            System.arraycopy(newNK.linksEachLocus[i], 0, linksEachLocus[i], 0, nr_links);
        }
                
        for(int k = 0; k < numberOfObjectives; k++) {
            for (int i = 0; i < numberOfFacilities; i++) {
                System.arraycopy(newNK.contribution[k][i], 0, contribution[k][i], 0, (int)Math.pow(2, nr_links+1));
            }
        }

        set_K_bits(nr_links);
    }
             
    public NK(NK newNK){
        correlation = newNK.correlation;
        numberOfFacilities = newNK.numberOfFacilities;
        numberOfObjectives = newNK.numberOfObjectives;
        nr_links = newNK.nr_links;
		
        linksEachLocus = new int[numberOfFacilities][nr_links];
        contribution = new double[numberOfObjectives][numberOfFacilities][(int)Math.pow(2, nr_links+1)];
                
        for(int i=0;i<numberOfFacilities;i++) {
            System.arraycopy(newNK.linksEachLocus[i], 0, linksEachLocus[i], 0, nr_links);
        }
                
        for(int k = 0; k < numberOfObjectives; k++) {
            for (int i = 0; i < numberOfFacilities; i++) {
                System.arraycopy(newNK.contribution[k][i], 0, contribution[k][i], 0, (int)Math.pow(2, nr_links+1));
            }
        }

        set_K_bits(nr_links);
    }
        
    public NK(){}
       
    @Override
    public void restarts(){} 
        
    @Override
    public Solution_NK init(Variator var){//, PerturbatorStrategies e){
        Boolean[] perm = new Boolean[numberOfFacilities];
        for(int i = 0; i < perm.length; i++){
            perm[i] = r.nextBoolean();
        }
        
        Double[] obj = (Double[]) computeSolution(perm);
        Solution_NK tempS = new Solution_NK(obj,perm); //,e);
        var.updateNrSwaps(this.numberOfFacilities);
        return tempS;
    }
        
    public final static NK readFileInObject(String pathInitFile, int dim){
        //read the file into a string:
        return new NK(Persistence_NK.readFile_NK(pathInitFile));
    }
                
    @Override
    public Solution computeSolution(Solution s1){
        Double[] tempObj = new Double[numberOfObjectives];
        //
        for(int j = 0; j < numberOfFacilities; j++) {
                
            String tempS = new String();
            for (int k = 0; k < nr_links; k++) {
                if(s1.items[linksEachLocus[j][k]].equals(false)) {
                    tempS = tempS.concat("0");
                } else {
                    tempS = tempS.concat("1");
                }
            }
            
            for(int i = 0; i < numberOfObjectives; i++){
                int index = K_bits.indexOf(tempS);
                if(j == 0)
                    tempObj[i] = 0.0;
                if(index == -1){
                    System.err.print(" Errro in index" + tempS);
                }
                tempObj[i] += contribution[i][j][index];
            }
        }
        s1.objectives = Arrays.copyOf(tempObj, numberOfObjectives);
        return s1;
    }

    public PartialSolution_NK computeSolution(PartialSolution_NK s1){
        Double[] tempObj = new Double[numberOfObjectives];
        //
        for(int j = 0; j < numberOfFacilities; j++) {
                
            String tempS = new String();
            for (int k = 0; k < nr_links; k++) {
                if(s1.items[linksEachLocus[j][k]].equals(false)) {
                    tempS = tempS.concat("0");
                } else {
                    tempS = tempS.concat("1");
                }
            }
            
            for(int i = 0; i < numberOfObjectives; i++){
                int index = K_bits.indexOf(tempS);
                tempObj[i] += contribution[i][j][index];
            }
        }
        s1.objectives = Arrays.copyOf(tempObj, numberOfObjectives);
        return s1;
    }

    @Override
    public ArchiveSolutions computeSolution(ArchiveSolutions nda){
        Iterator<Solution> iterator = nda.iterator();
        while(iterator.hasNext()){
            Solution_NK s1 = (Solution_NK) iterator.next();

            computeSolution(s1);
            if(s1.set != null){

                Iterator<PartialSolution_NK> iteratorV = s1.set.iterator();
                while(iteratorV.hasNext()){
                    PartialSolution_NK temp = iteratorV.next();
                    computeSolution(temp);
                }
            }
        }
        return nda;
    }
        
    public ArchiveSolutions computeSolution(ArchiveSolutions nda, int t){
        Iterator<Solution> iterator = nda.iterator();
        while(iterator.hasNext()){
            Solution_NK s1 = (Solution_NK) iterator.next();
            computeSolution(s1);

            if(s1.set != null){
                Iterator<PartialSolution_NK> iteratorV = s1.set.iterator();
                while(iteratorV.hasNext()){
                    PartialSolution_NK temp = iteratorV.next();
                    computeSolution(temp);
                }
            }
        }

        return nda;
    }

    @Override
    public Object[] computeSolution(Object[] rep){
        Double[] objectives = new Double[numberOfObjectives];
                    
        for(int j = 0; j < numberOfFacilities; j++) {
                
            String tempS = new String();
            for (int k = 0; k < nr_links; k++) {
                if(rep[linksEachLocus[j][k]].equals(false)) {
                    tempS = tempS.concat("0");
                } else {
                    tempS = tempS.concat("1");
                }
            }
            
            for(int i = 0; i < numberOfObjectives; i++){
                if(j == 0){
                   objectives[i] = (Double) 0.0; 
                }
                int index = K_bits.indexOf(tempS);
                objectives[i] += contribution[i][j][index];
            }
        }
        //s1.objectives = Arrays.copyOf(tempObj, numberOfObjectives);
        return objectives;
    }
        
    //@Override
    public Double[] computeSolution(int[][] linksEachLocusT, double[][][] contributionT, Object[] rep){
        Double[] objectives = new Double[numberOfObjectives];
                    
        for(int j = 0; j < numberOfFacilities; j++) {
                
            String tempS = new String();
            for (int k = 0; k < nr_links; k++) {
                if(((Boolean)rep[linksEachLocusT[j][k]]).equals(false)) {
                    tempS = tempS.concat("0");
                } else {
                    tempS = tempS.concat("1");
                }
            }
            
            for(int i = 0; i < numberOfObjectives; i++){
                if(j == 0){
                   objectives[i] = (Double) 0.0; 
                }
                int index = K_bits.indexOf(tempS);
                objectives[i] += contributionT[i][j][index];
            }
        }
        //s1.objectives = Arrays.copyOf(tempObj, numberOfObjectives);
        return objectives;
    }
        
    @Override
    public String toString(){
	StringBuilder sb = new StringBuilder();
	sb.append("Describe the space \n");
        sb.append("First matrix: \n");
        for (int[] linksEachLocu : linksEachLocus) {
            sb.append("\t");
            for (int j = 0; j < linksEachLocus[0].length; j++) {
                sb.append(linksEachLocu[j]).append("\t");
            }
            sb.append("\n");
        }
		
        sb.append("Second Matrix: \n");
        for (double[][] contribution1 : contribution) {
            sb.append("\t");
            for (int j = 0; j < contribution[0].length; j++) {
                sb.append("\t");
                for (int k = 0; k < contribution[0][0].length; k++) {
                    sb.append(contribution1[j][k]).append("\t");
                }
            }
            sb.append("\n");
        }                
	
        return sb.toString();
    }	
	
        
    @Override
    public void writeToFile(String nameFile, ArchiveSolutions nda, boolean append){
        Persistence_NK.writeToFile(nameFile, nda,append);
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
            try (BufferedWriter fos = new BufferedWriter(fileStream)) {
                Iterator<Solution> iterator = totalNDA.iterator();
                while(iterator.hasNext()){
                    Solution_NK s1 = (Solution_NK)iterator.next();
                    for (Object objective : s1.objectives) {
                        fos.write(Double.toString((Double)objective) + "   ");
                    }
                    fos.write("  (");
                    for (Object item : s1.items) {
                        fos.write(item + ", ");
                    }
                    fos.write(")\n");
                }
                
                fos.newLine();
            }
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

   double getDistance(int[] s, int[] s1){
            int dist = 0;
            diffTemp1.removeAllElements();
            diffTemp2.removeAllElements();
            for(int i = 0; i < s.length;i++){
                if(s[i] != s1[i]){
                    diffTemp1.add(s1[i]);
                    diffTemp2.add(s[i]);
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
       Solution_NK temp = new Solution_NK();
       return temp;
   }

   public Solution generateSolutions(double[] tempA, boolean[] tempB){
       Solution_NK temp = new Solution_NK(tempA,tempB);
       return temp;
   }

    @Override
   public ArchiveSolutions bestFile(String fileI){ //, PerturbatorStrategies epsilon){
       return Persistence_Solution_NK.readFile_Solutions(fileI, this); //,epsilon);
   }
}
