/*
 * statistics in NDAs
 */

package MOStatistics;

import MOStatistics.StatisticalUtilities.DistanceNDAs_Average;
//import MOStatistics.StatisticalUtilities.FitnessCorrelation;
import MOStatistics.StatisticalUtilities.SizeLocalOptima;
import MOStatistics.StatisticalUtilities.DistanceOneNDA;
import MOStatistics.StatisticalUtilities.QualityMeasures;
import general.*;
import java.io.*;

/**
 *
 * @author madalina
 * mean and variance of local runs and final runs --> implemented 
 * mean and variance of non-dominated archive --> implemented
 * mean and variance of the number of local searched in the non-dominated archive
 * mean and variance of the euclidian distance in the objective space
 * 
 */
public class DeactivStatistics{
    //private BufferedWriter fDist;
    //private BufferedWriter fDistTime;
    
    private BufferedWriter fOneDist;
    private BufferedWriter fOneDistTime;

    private BufferedWriter fCorr;
    private BufferedWriter fCorrTime;
    //count the number of local searches
       
    //Stacks --> data for computing the correlations between parents and chidlren
    private int lengthMeasures;

    /////////////////////////
    //distance/distance; for a plot
    ////////////////////////
    private int effectivePerRun = 0;
    private int effectiveInTotal = 0;

    /////////////////////////
    //statistics for totalImprovNonZeroNr and escape
    /////////////////////////////
    private BufferedWriter fImprov;
    private BufferedWriter fImprovTime;

    //distance NDA
    //private double averDistance;
    //private double stdDistance;
    //private double averDistanceRun;
    //private double stdDistnceRun;

    //number of swaps
    private double[] nrOfSwapsInTime;
    private double[] varOfSwapsInTime;

    private double[] nrTimeRuns;

    //private String nameFile;

    ArchiveSolutions bestNDA;
    boolean bestA[];

    //deactiv flag
    private BufferedWriter fDeactiv;
    private BufferedWriter fDeactivTime;

    boolean deactivFlag = false;
    // nr deactiv Solutions; set in the Iter_PLS file
    private long deactivNumber;
    // statistics number
    private double meanDeactivN;
    private double stdDeactivNr;
    private long nrNonZeroDeactiv;
    //
    private long nrDeactivIndex;
    private double meanDeactivNIndex;
    public double stdDeactivNrIndex;
    //
    private double meanEscapeDeactivN;
    private double stdEscapeDeactivNr;
    private long nrEscapeNonZeroDeactiv;
    //
    public long nrEscapeDeactivIndex;
    public double meanEscapeDeactivNIndex;
    public double stdEscapeDeactivNrIndex;
    //
    public double meanSucessDeactivN;
    public double stdSucessDeactivNr;
    public long nrSucessNonZeroDeactiv;
    //
    public long nrSucessDeactivIndex;
    public double meanSucessDeactivNIndex;
    public double stdSucessDeactivNrIndex;
    //
    public double meanImprovDeactivN;
    public double stdImprovDeactivNr;
    public long nrImprovNonZeroDeactiv;
    //
    public long nrImprovDeactivIndex;
    public double meanImprovDeactivNIndex;
    public double stdImprovDeactivNrIndex;
    //
    // statistics per run
    public double[] totalDeactivNr; // meanDeactivNr/nrDeactiv
    public double[] totalNonZeroNr; // nrNonZeroDeactiv
    public double[] totalDeactivNonZero; // meanDeactivNr/nrNonZeroDeactiv
    // statistics per time frame
    public double[][] timeDeactivNr; // meanDeactivNr/nrDeactiv
    public double[][] timeNonZeroNr; // nrNonZeroDeactiv
    public double[][] timeDeactivNonZero; // meanDeactivNr/nrNonZeroDeactiv
    //
    // statistics per run
    public double[] totalEscapeDeactivNr; // meanDeactivNr/nrDeactiv
    public double[] totalEscapeNonZeroNr; // nrNonZeroDeactiv
    public double[] totalEscapeDeactivNonZero; // meanDeactivNr/nrNonZeroDeactiv
    // statistics per time frame
    public double[][] timeEscapeDeactivNr; // meanDeactivNr/nrDeactiv
    public double[][] timeEscapeNonZeroNr; // nrNonZeroDeactiv
    public double[][] timeEscapeDeactivNonZero; // meanDeactivNr/nrNonZeroDeactiv
    //
    // statistics per time frame
    public double[][] timeImprovDeactivNr; // meanDeactivNr/nrDeactiv
    public double[][] timeImprovNonZeroNr; // nrNonZeroDeactiv
    public double[][] timeImprovDeactivNonZero; // meanDeactivNr/nrNonZeroDeactiv
    //
    // statistics per time frame
    private double[][] timeSucessDeactivNr; // meanDeactivNr/nrDeactiv
    private double[][] timeSucessNonZeroNr; // nrNonZeroDeactiv
    private double[][] timeSucessDeactivNonZero; // meanDeactivNr/nrNonZeroDeactiv

    //PerturbatorStrategies epsilon;
    private ProblemInstance problem;
    private Variator var;
    //ArchiveSolutions parents;
    private ArchiveSolutions children;
    protected ArchiveSolutions currentNDA;
    protected Solution p;

    private DistanceNDAs_Average distanceThis;
    private double[] results;
    private double[][] corrVector;
    //private double[] distance;

    //private FitnessCorrelation fitness;
    //private FitnessCorrelation[] fitnessTime;

    private DistanceOneNDA oneNDA;
    private DistanceOneNDA[] oneNDATime;

    private QualityMeasures improv;
    private QualityMeasures[] improvTime;

    private QualityMeasures deactivImprov;

    private SizeLocalOptima propertiesPLS;
    private SizeLocalOptima[] propertiesPLSTime;

    //int nrIndex;
    
    public DeactivStatistics(Variator v, ProblemInstance p, int currentIndex, String fileN, boolean deactiv){ //, PerturbatorStrategies epsilon){
        //this.epsilon = epsilon;
        //bestNDA = Persistence_Solution_QAPs.generateSolution(); //epsilon);
        problem = p;
        lengthMeasures = problem.numberOfObjectives;
        
        var = v;

        //nameFile = fileN;
        deactivFlag = deactiv;
        
        oneNDA = new DistanceOneNDA(problem.numberOfObjectives,2);// 2 euclidian distance
        oneNDATime = new DistanceOneNDA[currentIndex];
        for(int i = 0; i < currentIndex; i++){
            oneNDATime[i] = new DistanceOneNDA(lengthMeasures,2);
        }

        //fitness = new FitnessCorrelation(lengthMeasures,p.numberOfObjectives);
        //fitnessTime = new FitnessCorrelation[currentIndex];
        //for(int i = 0; i < currentIndex; i++){
        //    fitnessTime[i] = new FitnessCorrelation(lengthMeasures,p.numberOfObjectives);
        //}

        propertiesPLS = new SizeLocalOptima();
        propertiesPLSTime = new SizeLocalOptima[currentIndex];
        for(int i = 0; i < currentIndex; i++){
            propertiesPLSTime[i] = new SizeLocalOptima();
        }

        improv = new QualityMeasures(currentIndex);
        deactivImprov = new QualityMeasures(currentIndex);


        try{
            /*File myFile = new File(fileN+"_Deactiv.nda");
            FileWriter fileStream = new FileWriter(myFile,true);
            myFile.createNewFile();
            fileStream = new FileWriter(myFile);
            fDist = new BufferedWriter(fileStream);

            File myFileTime = new File(fileN+".time");
            FileWriter fileStreamTime = new FileWriter(myFileTime,false);
            myFileTime.createNewFile();
            fileStreamTime = new FileWriter(myFileTime);
            fDistTime = new BufferedWriter(fileStreamTime);
*/
            File myFileO = new File(fileN+"_OneNDA.nda");
            FileWriter fileStreamO = new FileWriter(myFileO,true);
            myFileO.createNewFile();
            fileStreamO = new FileWriter(myFileO);
            fOneDist = new BufferedWriter(fileStreamO);

            File myFileOTime = new File(fileN+"_OneNDA.time");
            FileWriter fileStreamOTime = new FileWriter(myFileOTime,false);
            myFileOTime.createNewFile();
            fileStreamOTime = new FileWriter(myFileOTime);
            fOneDistTime = new BufferedWriter(fileStreamOTime);

            // correlation files
            File myFileC = new File(fileN+"_Corr.nda");
            FileWriter fileStreamC = new FileWriter(myFileC,true);
            myFileC.createNewFile();
            fileStreamC = new FileWriter(myFileC);
            fCorr = new BufferedWriter(fileStreamC);

            File myFileCTime = new File(fileN+"_Corr.time");
            FileWriter fileStreamCTime = new FileWriter(myFileCTime,false);
            myFileCTime.createNewFile();
            fileStreamCTime = new FileWriter(myFileCTime);
            fCorrTime = new BufferedWriter(fileStreamCTime);

            ///////////////////////
            // deactiv
            ////////////////////////
            if(deactivFlag){
                File myFileDeactiv = new File(fileN+"_Deactiv.nda");
                FileWriter fileStreamDeactiv = new FileWriter(myFileDeactiv,false);
                myFileDeactiv.createNewFile();
                fileStreamDeactiv = new FileWriter(myFileDeactiv);
                fDeactiv = new BufferedWriter(fileStreamDeactiv);
            
                File myFileDeactivTime = new File(fileN+"_Deactiv.time");
                FileWriter fileStreamDeactivTime = new FileWriter(myFileDeactivTime,false);
                myFileDeactivTime.createNewFile();
                fileStreamDeactivTime = new FileWriter(myFileDeactivTime);
                fDeactivTime = new BufferedWriter(fileStreamDeactivTime);
            }

            /////////////////////////
            //  totalImprovNonZeroNr
            ////////////////
            File myFileImprov = new File(fileN+"_Improv.nda");
            FileWriter fileStreamImprov = new FileWriter(myFileImprov,false);
            myFileImprov.createNewFile();
            fileStreamImprov = new FileWriter(myFileImprov);
            fImprov = new BufferedWriter(fileStreamImprov);

            File myFileImprovTime = new File(fileN+"_Improv.time");
            FileWriter fileStreamImprovTime = new FileWriter(myFileImprovTime,false);
            myFileImprovTime.createNewFile();
            fileStreamImprovTime = new FileWriter(myFileImprovTime);
            fImprovTime = new BufferedWriter(fileStreamImprovTime);

        } catch (Exception e){
			System.out.println("Write not possible:"+fileN);
			e.printStackTrace(System.out);
	}

        effectiveInTotal = 1;
        effectivePerRun = 1;

        /////////////////////////
        // totalImprovNonZeroNr
        //////////////////////////
        //sucPerInterInTime = new double[currentIndex];
        //varSucPerInterInTime = new double[currentIndex];

        nrOfSwapsInTime = new double[currentIndex];
        varOfSwapsInTime = new double[currentIndex];

        //distParamBestNDA = new double[currentIndex];
        //varDistParamBestNDA = new double[currentIndex];
        //distObjBestNDA = new double[currentIndex];
        //varDistObjBestNDA = new double[currentIndex];

        nrTimeRuns = new double[currentIndex];

        ///////////////////////////
        //compute the hypervolume -- init
        ///////////////////////////
        //refPoint = new double[this.lengthMeasures];
        //for(int i = 0; i < this.lengthMeasures; i++)
        //    refPoint[i] = 0;
        /*hyperMean = 0;
        hyperVar = 0;
        hyperTotal = new double[3];
        hyperMeanTime = new double[currentIndex];
        hyperVarTime = new double[currentIndex];
        hyperMeanIndex = 0;
        // coun only the maximum hypervolume
        maxHyper =  0;
        hyperMaxTime = new double[currentIndex];
        hyperVarMaxTime = new double[currentIndex];
        */
        ///////////////////////////
        // deactiv - init
        //////////////////////////
        //deactivFlag = false;
        deactivNumber = 0;
        //
        nrDeactivIndex = 0;
        meanDeactivNIndex = 0;
        stdDeactivNrIndex = 0;
        //
        meanDeactivN = 0;
        stdDeactivNr = 0;
        nrNonZeroDeactiv = 0;
        //
        meanEscapeDeactivN = 0;
        stdEscapeDeactivNr = 0;
        nrEscapeNonZeroDeactiv = 0;
        //
        nrEscapeDeactivIndex = 0;
        meanEscapeDeactivNIndex = 0;
        stdEscapeDeactivNrIndex = 0;
        //
        meanSucessDeactivN = 0;
        stdSucessDeactivNr = 0;
        nrSucessNonZeroDeactiv = 0;
        //
        nrSucessDeactivIndex = 0;
        meanSucessDeactivNIndex = 0;
        stdSucessDeactivNrIndex = 0;
        //
        meanImprovDeactivN = 0;
        stdImprovDeactivNr = 0;
        //
        nrDeactivIndex = 0;
        meanImprovDeactivNIndex = 0;
        stdImprovDeactivNrIndex = 0;
        //
        nrImprovNonZeroDeactiv = 0;
        //
        totalDeactivNr = new double[2]; // meanDeactivNr/nrDeactiv
        totalNonZeroNr = new double[2]; // nrNonZeroDeactiv
        totalDeactivNonZero = new double[2]; // meanDeactivNr/nrNonZeroDeactiv
        timeDeactivNr = new double[currentIndex][2]; // meanDeactivNr/nrDeactiv
        timeNonZeroNr = new double[currentIndex][2]; // nrNonZeroDeactiv
        timeDeactivNonZero = new double[currentIndex][2]; // meanDeactivNr/nrNonZeroDeactiv
        //
        totalEscapeDeactivNr = new double[2]; // meanDeactivNr/nrDeactiv
        totalEscapeNonZeroNr = new double[2]; // nrNonZeroDeactiv
        totalEscapeDeactivNonZero = new double[2]; // meanDeactivNr/nrNonZeroDeactiv
        timeEscapeDeactivNr = new double[currentIndex][2]; // meanDeactivNr/nrDeactiv
        timeEscapeNonZeroNr = new double[currentIndex][2]; // nrNonZeroDeactiv
        timeEscapeDeactivNonZero = new double[currentIndex][2]; // meanDeactivNr/nrNonZeroDeactiv
        //
        //totalImprovDeactivNr = new double[2]; // meanDeactivNr/nrDeactiv
        //totalImprovNonZeroNr = new double[2]; // nrNonZeroDeactiv
        //totalImprovDeactivNonZero = new double[2]; // meanDeactivNr/nrNonZeroDeactiv
        timeImprovDeactivNr = new double[currentIndex][2]; // meanDeactivNr/nrDeactiv
        timeImprovNonZeroNr = new double[currentIndex][2]; // nrNonZeroDeactiv
        timeImprovDeactivNonZero = new double[currentIndex][2]; // meanDeactivNr/nrNonZeroDeactiv
        //
        //totalSucessDeactivNr = new double[2]; // meanDeactivNr/nrDeactiv
        //totalSucessNonZeroNr = new double[2]; // nrNonZeroDeactiv
        //totalSucessDeactivNonZero = new double[2]; // meanDeactivNr/nrNonZeroDeactiv
        timeSucessDeactivNr = new double[currentIndex][2]; // meanDeactivNr/nrDeactiv
        timeSucessNonZeroNr = new double[currentIndex][2]; // nrNonZeroDeactiv
        timeSucessDeactivNonZero = new double[currentIndex][2]; // meanDeactivNr/nrNonZeroDeactiv
       
    }


    public void restart(){

        writeStatisticsTime();

        improv.restart();        
        //fitness.restart();
        oneNDA.restart();
        propertiesPLS.restart();
        ////////////////////////
        //hypervolume
        ////////////////////////
        /*this.hyperTotal[0]++;
        hyperTotal[1] += hyperMean/effectivePerRun;
        hyperTotal[2] += Math.sqrt(hyperVar/effectivePerRun - Math.pow(hyperMean/this.effectivePerRun,2));
        hyperMean = 0;
        hyperVar = 0;
        hyperMeanIndex = 0;
        this.maxHyper = 0;*/

        effectivePerRun = 1;

        //varLengthPLS = 0;
       // meanLengthPLS = 0;
        //varSizeNDA_PLS = 0;
        //meanSizeNDA_PLS = 0;
         
        effectiveInTotal++;
        
         //distParam = 0;
        //varParam = 0;
        //distObj = 0;
        //varObj = 0;

        ///////////////////////////
        // deactivation
        /////////////////////////////
        deactivNumber = 0;
        meanDeactivN = 0;
        stdDeactivNr = 0;
        nrNonZeroDeactiv = 0;
        //
        meanEscapeDeactivN = 0;
        stdEscapeDeactivNr = 0;
        nrEscapeNonZeroDeactiv = 0;
        //
        meanSucessDeactivN = 0;
        stdSucessDeactivNr = 0;
        nrSucessNonZeroDeactiv = 0;
        //
        meanImprovDeactivN = 0;
        stdImprovDeactivNr = 0;
        nrImprovNonZeroDeactiv = 0;
        //
        nrDeactivIndex = 0;
        nrSucessDeactivIndex = 0;
        nrEscapeDeactivIndex = 0;
        nrImprovDeactivIndex = 0;

    }


    //mean and variance of independent runs
    public void collectStatistics(){
        if(children == null || children.size()  == 0){
            return;
        }

        propertiesPLS.collectStatistics(children, var);

        ///////////////////////////
        // distances; BEST NDA SHOULD BE INITIALIZED !!!!!!
        /////////////////////////////
        results = distanceThis.internDistance(bestNDA, currentNDA);
        corrVector = distanceThis.getCorrVector();

        //fitness.collectStatistics(results, corrVector,bestNDA, currentNDA);

        ///////////////////
        //calculate the distance in NDA: solution space
        /////////////////////////
        oneNDA.distanceNDA(currentNDA);

        /////////////////////
        // hyper  computation
        ////////////////
        //double tempHyper = this.computeHyper(children);
        /*hyperMean += tempHyper;
        hyperVar += Math.pow(tempHyper, 2);

        //maximum hypervolume
        if(this.maxHyper < tempHyper)
            maxHyper = tempHyper;
        */
        
        ////////////////////////
        // deactivation statistics
        ///////////////////////////
        if(this.deactivFlag){
            if(this.deactivNumber > 0){
                this.nrNonZeroDeactiv++;
                this.meanDeactivN += deactivNumber;
                this.stdDeactivNr += Math.pow(deactivNumber,2);

                nrDeactivIndex++;
                this.meanDeactivNIndex += deactivNumber;
                this.stdDeactivNrIndex += Math.pow(deactivNumber,2);
            }
        }
        
        /////////////////////
        // escape, sucessful and totalImprovNonZeroNr
        ////////////////////////////
        improv.collectStatistics(p, children, var);
        
        if(deactivFlag && deactivNumber > 0) {
            if(p != null){
                if(!children.contains(p)){
                        // totalImprovNonZeroNr when non-dominated
                        if(!p.dominates(children)){ //
                            //if(children.dominates(p) || !p.dominates(children)){
                            // deactivation sucess probability
                                nrSucessNonZeroDeactiv++;
                                meanSucessDeactivN += deactivNumber;
                                stdSucessDeactivNr += Math.pow(deactivNumber,2);
                                nrSucessDeactivIndex++;
                        }

                            nrEscapeNonZeroDeactiv++;
                            meanEscapeDeactivN += deactivNumber;
                            stdEscapeDeactivNr += Math.pow(deactivNumber,2);
                            nrEscapeDeactivIndex++;
                 }
                
                if(!children.contains(p) && !p.dominates(children)){
                    nrImprovNonZeroDeactiv++;
                    meanImprovDeactivN += deactivNumber;
                    stdImprovDeactivNr += Math.pow(deactivNumber,2);
                    nrImprovDeactivIndex++;
                }
            }
        }

        this.effectivePerRun++;
        //this.similarityMeasures += similMeasure;

        //indexex
     }

    public synchronized void writeStatistics(){

        propertiesPLS.writeStatistics(fDeactiv);

        //fitness.writeStatistics(fCorr);

        improv.writeStaticstics(fImprov);

        oneNDA.writeStatistics(fOneDist);

        ////////////////////////////
        // deactivation
        ////////////////////////
        if(this.deactivFlag){
            try{
                //write the header ??
                fDeactiv.write("nrMeasures, nrNonZeroDeactiv , nrDeactivPerMeasures, std, nrDeactivPerNonZeroDeactiv, std,  escapes (5), improv (5), sucess (5) \n");
                fDeactiv.write(effectivePerRun + "\t");
                double tempMean = this.nrNonZeroDeactiv;
                this.fDeactiv.write(Double.toString(tempMean)+ " \t ");

                tempMean = this.meanDeactivN/this.effectivePerRun;
                double varMean = Math.sqrt(this.stdDeactivNr/this.effectivePerRun  - Math.pow(this.meanDeactivN/this.effectivePerRun,2));
                this.fDeactiv.write(Double.toString(tempMean)+ " \t " + Double.toString(varMean)+ " \t ");

                tempMean = this.meanDeactivN/this.nrNonZeroDeactiv;
                varMean = Math.sqrt(this.stdDeactivNr/this.nrNonZeroDeactiv  - Math.pow(this.meanDeactivN/this.nrNonZeroDeactiv,2));
                this.fDeactiv.write(Double.toString(tempMean)+ " \t " + Double.toString(varMean)+ " \t ");

                if(nrEscapeNonZeroDeactiv > 0){
                    tempMean = this.meanEscapeDeactivN/this.nrEscapeNonZeroDeactiv;
                    varMean =Math.sqrt(this.stdEscapeDeactivNr/this.nrEscapeNonZeroDeactiv  - Math.pow(this.meanEscapeDeactivN/this.nrEscapeNonZeroDeactiv,2));
                    this.fDeactiv.write(Double.toString(tempMean)+ " \t " + Double.toString(varMean)+ " \t ");
                
                    tempMean = this.nrEscapeNonZeroDeactiv;
                    this.fDeactiv.write(Double.toString(tempMean)+ " \t " );
                } else {
                    fDeactiv.write("0 \t 0 \t 0 \t");
                }

                if(nrImprovNonZeroDeactiv > 0){
                    tempMean = this.meanImprovDeactivN/this.nrImprovNonZeroDeactiv;
                    varMean =Math.sqrt(this.stdImprovDeactivNr/this.nrImprovNonZeroDeactiv  - Math.pow(this.meanImprovDeactivN/this.nrImprovNonZeroDeactiv,2));
                    this.fDeactiv.write(Double.toString(tempMean)+ " \t " + Double.toString(varMean)+ " \t ");

                    tempMean = this.nrImprovNonZeroDeactiv;
                    this.fDeactiv.write(Double.toString(tempMean)+ " \t " );
                } else {
                    fDeactiv.write("0 \t 0 \t 0 \t");
                }

                if(nrSucessNonZeroDeactiv > 0){
                    tempMean = this.meanSucessDeactivN/this.nrSucessNonZeroDeactiv;
                    varMean =Math.sqrt(this.stdSucessDeactivNr/this.nrSucessNonZeroDeactiv  - Math.pow(this.meanSucessDeactivN/this.nrSucessNonZeroDeactiv,2));
                    this.fDeactiv.write(Double.toString(tempMean)+ " \t " + Double.toString(varMean)+ " \t ");

                    tempMean = this.nrSucessNonZeroDeactiv;
                    this.fDeactiv.write(Double.toString(tempMean)+ " \t " );
                } else {
                    fDeactiv.write("0 \t 0 \t 0 \t");
                }

                fDeactiv.newLine();
                fDeactiv.flush();
            }catch(Exception e)
		{
			System.out.println("Write not possible:"+fDeactiv);
			e.printStackTrace(System.out);
		}
        }
    }
    
    public void computeStatisticsTime(int currentIndex){

        improvTime[currentIndex].collectStatistics(p, children, var);

        nrOfSwapsInTime[currentIndex] += var.getNumberOfSwaps();
        varOfSwapsInTime[currentIndex] += Math.pow(var.getNumberOfSwaps(),2);

        nrTimeRuns[currentIndex]++;


        propertiesPLSTime[currentIndex].collectStatistics(children, var);

        ///////////////////////////
        // distances; BEST NDA SHOULD BE INITIALIZED !!!!!!
        /////////////////////////////
        results = distanceThis.internDistance(bestNDA, currentNDA);
        corrVector = distanceThis.getCorrVector();
        //fitnessTime[currentIndex].collectStatistics(results, corrVector,bestNDA, currentNDA);

        ///////////////////
        //calculate the distance in NDA: solution space
        /////////////////////////
        oneNDATime[currentIndex].distanceNDA(currentNDA);


        ///////////////////////
        // distances between pareto front a maximum
        // last best distance
        //distParamBestNDA[currentIndex] += this.distParam;
        //distObjBestNDA[currentIndex] += this.distObj;
        //varDistParamBestNDA[currentIndex] += this.varParam;
        //varDistObjBestNDA[currentIndex] += this.varObj;

        //hypervolume calculation
        //this.hyperMaxTime[currentIndex] += maxHyper;
        //this.hyperVarMaxTime[currentIndex] +=  Math.pow(maxHyper,2);
        //hypervolume calculation
        //this.hyperMeanTime[currentIndex] += hyperMeanIndex/nrIndex;
        //this.hyperVarTime[currentIndex] +=  Math.pow(hyperMeanIndex/nrIndex,2);

        ///////////////////////
        // deactiv
        //////////////////////////
        if(deactivFlag){
            timeDeactivNr[currentIndex][0] += nrDeactivIndex/nrTimeRuns[currentIndex];
            timeDeactivNr[currentIndex][1] += Math.pow(nrDeactivIndex/nrTimeRuns[currentIndex],2);
            timeDeactivNonZero[currentIndex][0] += meanDeactivNIndex/nrTimeRuns[currentIndex];
            timeDeactivNonZero[currentIndex][1] +=  Math.pow(meanDeactivNIndex/nrTimeRuns[currentIndex],2);
            timeNonZeroNr[currentIndex][0] += meanDeactivNIndex/nrDeactivIndex;
            timeNonZeroNr[currentIndex][1] += Math.pow(meanDeactivNIndex/nrDeactivIndex,2);
 
            if( nrEscapeNonZeroDeactiv > 0){
                this.timeEscapeDeactivNr[currentIndex][0] += nrEscapeDeactivIndex/nrEscapeNonZeroDeactiv;
                this.timeEscapeDeactivNr[currentIndex][1] += Math.pow(this.nrEscapeDeactivIndex/nrEscapeNonZeroDeactiv,2);
                this.timeEscapeDeactivNonZero[currentIndex][0] += this.meanEscapeDeactivNIndex/nrEscapeNonZeroDeactiv;
                this.timeEscapeDeactivNonZero[currentIndex][1] +=  Math.pow(this.meanEscapeDeactivNIndex/nrEscapeNonZeroDeactiv,2);
                this.timeEscapeNonZeroNr[currentIndex][0] += this.meanEscapeDeactivNIndex/this.nrEscapeDeactivIndex;
                this.timeEscapeNonZeroNr[currentIndex][1] += Math.pow(this.meanEscapeDeactivNIndex/this.nrEscapeDeactivIndex,2);
            }

            if(nrImprovNonZeroDeactiv > 0){
                timeImprovDeactivNr[currentIndex][0] += nrImprovDeactivIndex/nrImprovDeactivIndex;
                timeImprovDeactivNr[currentIndex][1] += Math.pow(nrImprovDeactivIndex/nrImprovDeactivIndex,2);
                timeImprovDeactivNonZero[currentIndex][0] += meanImprovDeactivNIndex/nrImprovNonZeroDeactiv;
                timeImprovDeactivNonZero[currentIndex][1] +=  Math.pow(meanImprovDeactivNIndex/nrImprovNonZeroDeactiv,2);
                timeImprovNonZeroNr[currentIndex][0] += meanImprovDeactivNIndex/nrImprovDeactivIndex;
                timeImprovNonZeroNr[currentIndex][1] += Math.pow(meanImprovDeactivNIndex/this.nrImprovDeactivIndex,2);
            }

            if(nrSucessNonZeroDeactiv > 0){
                timeSucessDeactivNr[currentIndex][0] += (nrSucessDeactivIndex/nrSucessNonZeroDeactiv);
                timeSucessDeactivNr[currentIndex][1] += Math.pow(nrSucessDeactivIndex/nrSucessNonZeroDeactiv,2);
                timeSucessDeactivNonZero[currentIndex][0] += meanSucessDeactivNIndex/nrSucessNonZeroDeactiv;
                timeSucessDeactivNonZero[currentIndex][1] +=  Math.pow(meanSucessDeactivNIndex/nrSucessNonZeroDeactiv,2);
                timeSucessNonZeroNr[currentIndex][0] += meanSucessDeactivNIndex/nrSucessDeactivIndex;
                timeSucessNonZeroNr[currentIndex][1] += Math.pow(meanSucessDeactivNIndex/this.nrSucessDeactivIndex,2);
            }
        }

        //hyperMeanIndex = 0;
        //
        nrDeactivIndex = 0;
        nrImprovDeactivIndex = 0;
        nrEscapeDeactivIndex = 0;
        nrSucessDeactivIndex = 0;

        //compute the correlation
        //for (int j = 0; j < lengthMeasures; j++) {
        //   fitnessCorrelationCoefCurr[j][currentIndex] +=  this.fitnessCorrelationCoef[j]/effectivePerRun;
        //   varFitCorrCoefCurr[j][currentIndex] += Math.sqrt(this.varFitCorrCoef[j]/effectivePerRun- Math.pow(fitnessCorrelationCoef[j]/effectivePerRun,2));
        // }

        //similarityMeasures[currentIndex] += distanceInParamTotal/effectivePerRun;
        //varSimilarityMeasures[currentIndex] += Math.sqrt(varDistanceInParamTotal/effectivePerRun - Math.pow(distanceInParamTotal/effectivePerRun,2));
    }
    
    
    public void closeFiles(){
        try{
            fCorr.close();
            fCorrTime.close();

            if(this.deactivFlag){
                fDeactivTime.close();
                //fDeactivTime.close();
                fDeactiv.close();
            }
            
            fImprov.close();
            fImprovTime.close();
            //fDistTime.close();
        }catch(Exception e)
		{
			System.out.println("Write not possible:"+fDeactiv);
			e.printStackTrace(System.out);
		}
    }

    public void setNDAs(ArchiveSolutions children, boolean flag){
        //this.parents = parents;
        this.children = children;
    }
    
    public void setNDAs(ArchiveSolutions children, Solution s, ArchiveSolutions currentNDA, boolean flag){
        this.currentNDA = currentNDA;
        this.children = children;
        this.p = s;
    }

    //index
    public synchronized void writeStatisticsTime(){
        try{

            for(int index = 0; index < nrTimeRuns.length; index++){
                fImprovTime.write(String.valueOf(index+1) + " \t "+nrTimeRuns[index] + " \t "+ (long)(this.nrOfSwapsInTime[index]/nrTimeRuns[index]) + " \t ");
                double temp = Math.sqrt(this.varOfSwapsInTime[index]/nrTimeRuns[index] - Math.pow(this.nrOfSwapsInTime[index]/nrTimeRuns[index], 2));
                fImprovTime.write(temp + " \t ");


                //fDistTime.write(this.distParamBestNDA[index]/nrTimeRuns[index] + " \t ");
                //fDistTime.write(this.varDistParamBestNDA[index]/nrTimeRuns[index] + " \t ");

                //fDistTime.write(this.distObjBestNDA[index]/nrTimeRuns[index] + " \t ");
                //fDistTime.write(this.varDistObjBestNDA[index]/nrTimeRuns[index] + " \t ");

                ///////////////////////
                // hypervolume
                //////////////////////////////
                //fDistTime.write(this.hyperMaxTime[index]/nrTimeRuns[index] + " \t ");
                //temp = Math.sqrt(this.hyperVarMaxTime[index]/nrTimeRuns[index] - Math.pow(this.hyperMaxTime[index]/nrTimeRuns[index], 2));
                //fDistTime.write(temp + " \t ");

                //fDistTime.write(this.hyperMeanTime[index]/nrTimeRuns[index] + " \t ");
                //temp = Math.sqrt(this.hyperVarTime[index]/nrTimeRuns[index] - Math.pow(this.hyperMeanTime[index]/nrTimeRuns[index], 2));
                //fDistTime.write(temp + " \t ");

                //this.fImprovTime.write(levelIndex[index]/nrTimeRuns[index] + " \t ");
                //temp = Math.sqrt(this.varLevelIndex[index]/nrTimeRuns[index] - Math.pow(levelIndex[index]/nrTimeRuns[index], 2));
                //fImprovTime.write(temp + " \t ");

                //fitness.writeStatisticsTime(fOneDistTime);
                //fDistTime.newLine();
            }

            fImprovTime.flush();
            fImprovTime.close();
        }catch (Exception e){
			System.out.println("Write not possible:"+fImprovTime);
			e.printStackTrace(System.out);
	}

	try{

            for(int index = 0; index < nrTimeRuns.length; index++){
                fImprovTime.write(timeImprovDeactivNr[index][0]/nrTimeRuns[index] + " \t ");
                double temp = Math.sqrt(timeImprovDeactivNr[index][1]/nrTimeRuns[index] - Math.pow(timeImprovDeactivNr[index][0]/nrTimeRuns[index], 2));
                fImprovTime.write(temp + " \t ");

                fImprovTime.write(timeEscapeDeactivNr[index][0]/nrTimeRuns[index] + " \t ");
                temp = Math.sqrt(timeEscapeDeactivNr[index][1]/nrTimeRuns[index] - Math.pow(timeEscapeDeactivNr[index][0]/nrTimeRuns[index], 2));
                fImprovTime.write(temp + " \t ");

                fImprovTime.write(timeSucessDeactivNr[index][0]/nrTimeRuns[index] + " \t ");
                temp = Math.sqrt(timeSucessDeactivNr[index][1]/nrTimeRuns[index] - Math.pow(timeSucessDeactivNr[index][0]/nrTimeRuns[index], 2));
                fImprovTime.write(temp + " \t ");
                
                fImprovTime.newLine();
            }
          fImprovTime.flush();
          fImprovTime.close();
	}catch (Exception e){
			System.out.println("Write not possible:"+fImprovTime);
			e.printStackTrace(System.out);
	}

        //////////////////////////////
        // deactivation
        ///////////////////////////////
        if(this.deactivFlag){
          try{
            for(int index = 0; index < nrTimeRuns.length; index++){
                fDeactivTime.write(this.timeDeactivNr[index][0]/nrTimeRuns[index] + "\t");
                double temp = Math.sqrt(this.timeDeactivNr[index][1]/nrTimeRuns[index] - Math.pow(timeDeactivNr[index][0]/nrTimeRuns[index], 2));
                fDeactivTime.write(temp + " \t ");
                
                fDeactivTime.write(this.timeDeactivNonZero[index][0] + "\t");
                temp = Math.sqrt(this.timeDeactivNonZero[index][1]/nrTimeRuns[index] - Math.pow(timeDeactivNonZero[index][0]/nrTimeRuns[index], 2));
                fDeactivTime.write(temp + " \t ");

                fDeactivTime.write(this.timeNonZeroNr[index][0] + "\t");
                temp = Math.sqrt(this.timeNonZeroNr[index][1]/nrTimeRuns[index] - Math.pow(timeNonZeroNr[index][0]/nrTimeRuns[index], 2));
                fDeactivTime.write(temp + " \t ");

                if(timeEscapeDeactivNr[index][0] > 0){
                    fDeactivTime.write(this.timeEscapeDeactivNr[index][0]/nrTimeRuns[index] + "\t");
                    temp = Math.sqrt(this.timeEscapeDeactivNr[index][1]/nrTimeRuns[index] - Math.pow(timeEscapeDeactivNr[index][0]/nrTimeRuns[index], 2));
                    fDeactivTime.write(temp + " \t ");

                    fDeactivTime.write(this.timeEscapeDeactivNonZero[index][0] + "\t");
                    temp = Math.sqrt(this.timeEscapeDeactivNonZero[index][1]/nrTimeRuns[index] - Math.pow(timeEscapeDeactivNonZero[index][0]/nrTimeRuns[index], 2));
                    fDeactivTime.write(temp + " \t ");

                    fDeactivTime.write(this.timeEscapeNonZeroNr[index][0] + "\t");
                    temp = Math.sqrt(this.timeEscapeNonZeroNr[index][1]/nrTimeRuns[index] - Math.pow(timeEscapeNonZeroNr[index][0]/nrTimeRuns[index], 2));
                    fDeactivTime.write(temp + " \t ");
                } else {
                    fDeactivTime.write("0 \t 0 \t 0 \t 0 \t 0 \t 0 \t");
                }

                if(timeImprovDeactivNr[index][0] > 0){
                    fDeactivTime.write(this.timeImprovDeactivNr[index][0]/nrTimeRuns[index] + "\t");
                    temp = Math.sqrt(this.timeImprovDeactivNr[index][1]/nrTimeRuns[index] - Math.pow(timeImprovDeactivNr[index][0]/nrTimeRuns[index], 2));
                    fDeactivTime.write(temp + " \t ");

                    fDeactivTime.write(this.timeImprovDeactivNonZero[index][0] + "\t");
                    temp = Math.sqrt(this.timeImprovDeactivNonZero[index][1]/nrTimeRuns[index] - Math.pow(timeImprovDeactivNonZero[index][0]/nrTimeRuns[index], 2));
                    fDeactivTime.write(temp + " \t ");

                    fDeactivTime.write(this.timeImprovNonZeroNr[index][0] + "\t");
                    temp = Math.sqrt(this.timeImprovNonZeroNr[index][1]/nrTimeRuns[index] - Math.pow(timeImprovNonZeroNr[index][0]/nrTimeRuns[index], 2));
                    fDeactivTime.write(temp + " \t ");
                 } else {
                    fDeactivTime.write("0 \t 0 \t 0 \t 0 \t 0 \t 0 \t");
                }

                if(timeSucessDeactivNr[index][0] > 0){
                    fDeactivTime.write(this.timeSucessDeactivNr[index][0]/nrTimeRuns[index] + "\t");
                    temp = Math.sqrt(this.timeSucessDeactivNr[index][1]/nrTimeRuns[index] - Math.pow(timeSucessDeactivNr[index][0]/nrTimeRuns[index], 2));
                    fDeactivTime.write(temp + " \t ");

                    fDeactivTime.write(this.timeSucessDeactivNonZero[index][0] + "\t");
                    temp = Math.sqrt(this.timeSucessDeactivNonZero[index][1]/nrTimeRuns[index] - Math.pow(timeSucessDeactivNonZero[index][0]/nrTimeRuns[index], 2));
                    fDeactivTime.write(temp + " \t ");

                    fDeactivTime.write(this.timeSucessNonZeroNr[index][0] + "\t");
                    temp = Math.sqrt(this.timeSucessNonZeroNr[index][1]/nrTimeRuns[index] - Math.pow(timeSucessNonZeroNr[index][0]/nrTimeRuns[index], 2));
                    fDeactivTime.write(temp + " \t ");
                 } else {
                    fDeactivTime.write("0 \t 0 \t 0 \t 0 \t 0 \t 0 \t");
                }
            }

            fDeactivTime.flush();
            fDeactivTime.close();
          }catch (Exception e){
		System.out.println("Write not possible:"+fDeactivTime);
		e.printStackTrace(System.out);
          }
        }
    }

    public void setDeactivNumber(long deactivNr){
        this.deactivNumber = deactivNr;
    }
}