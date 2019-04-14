/*
 * statistics in NDAs
 */

package MOStatistics;

import MOStatistics.StatisticalUtilities.*;
import general.*;
import java.io.*;
//import java.util.*;
//import MOInstance_QAPs.*;

/**
 *
 * @author madalina
 * The statistics for a single NDA: 1
 *          1. escape, improvement, success --> own file
 * local runs and final runs --> implemented
 * non-dominated archive --> implemented
 * the number of local searched in the non-dominated archive
 * the euclidian distance in the objective space
 * 
 * properties of the search
 * files with the statistical properties as mean, variance, min, max
 * distances in one NDA; only applicable for multi-objectives
 */
public class NDAStatistics{

    ///////////////////////////
    // statistics with the distance 
    private BufferedWriter fOneDist;
    private BufferedWriter fOneDistTime;

    private DistanceOneNDA oneNDA;
    private DistanceOneNDA[] oneNDATime;

    //////////////////
    // statistics of a PLS
    private BufferedWriter fProp;
    private BufferedWriter fPropTime;

    private SizeLocalOptima propertiesPLS;
    private SizeLocalOptima[] propertiesPLSTime;

    /////////////////////////
    //statistics for improvement and escape
    /////////////////////////////
    private BufferedWriter fImprov;
    private BufferedWriter fImprovTime;

    private QualityMeasures improv;
    private QualityMeasures[] improvTime;

    //count the number of local searches
       
    //Stacks --> data for computing the correlations between parents and chidlren
    private int lengthMeasures;

    /////////////////////////
    // counts
    ////////////////////////
    private int effectivePerRun = 0;
    private int effectiveInTotal = 0;


    //distance NDA
    //private double averDistance;
    //private double stdDistance;
    //private double averDistanceRun;
    //private double stdDistnceRun;

    /////////////////
    //number of swaps
    // steps in neighborhood
    private double[] nrOfSwapsInTime;
    private double[] varOfSwapsInTime;

    private double[] nrTimeRuns;

    //private String nameFile;

    /////////////////////
    // know, approximative best solution ?
    ArchiveSolutions bestNDA;
    boolean bestA[];

    //////////
    // parameters of the search
    //PerturbatorStrategies epsilon;
    private ProblemInstance problem;
    private Variator var;
    //ArchiveSolutions parents;
    private ArchiveSolutions children;
    protected ArchiveSolutions currentNDA;
    protected Solution p;
    
    private String fileN;

    public NDAStatistics(Variator v, ProblemInstance p, int currentIndex, String fileN, boolean deactiv){ //, PerturbatorStrategies epsilon){
        //this.epsilon = epsilon;
        //bestNDA = Persistence_Solution_QAPs.generateSolution(); //epsilon);
        problem = p;
        lengthMeasures = problem.numberOfObjectives;
        this.fileN = fileN;
        var = v;

        //nameFile = fileN;
        if(lengthMeasures > 1){
            oneNDA = new DistanceOneNDA(problem.numberOfObjectives,2);// 2 euclidian distance
            oneNDATime = new DistanceOneNDA[currentIndex];
            for(int i = 0; i < currentIndex; i++){
                oneNDATime[i] = new DistanceOneNDA(lengthMeasures,2);
            }
        }

        propertiesPLS = new SizeLocalOptima();
        propertiesPLSTime = new SizeLocalOptima[currentIndex];
        for(int i = 0; i < currentIndex; i++){
            propertiesPLSTime[i] = new SizeLocalOptima();
        }

        improv = new QualityMeasures(currentIndex);
        improvTime = new QualityMeasures[currentIndex];
        for(int i = 0; i < currentIndex; i++){
            improvTime[i] = new QualityMeasures(currentIndex);
        }


        try{
        if(lengthMeasures > 1){
            File myFileO = new File(fileN+"_OneNDA.nda");
            FileWriter fileStreamO = new FileWriter(myFileO,true);
            myFileO.createNewFile();
            fileStreamO = new FileWriter(myFileO);
            fOneDist = new BufferedWriter(fileStreamO);
        }
            // correlation files
            File myFileC = new File(fileN+"_Prop.nda");
            FileWriter fileStreamC = new FileWriter(myFileC,true);
            myFileC.createNewFile();
            fileStreamC = new FileWriter(myFileC);
            fProp = new BufferedWriter(fileStreamC);

            /////////////////////////
            //  improvement; escape; success
            ////////////////
            File myFileImprov = new File(fileN+"_Improv.nda");
            FileWriter fileStreamImprov = new FileWriter(myFileImprov,false);
            myFileImprov.createNewFile();
            fileStreamImprov = new FileWriter(myFileImprov);
            fImprov = new BufferedWriter(fileStreamImprov);

        } catch (Exception e){
			System.out.println("Write not possible:"+fileN);
			e.printStackTrace(System.out);
	}

        effectiveInTotal = 1;
        effectivePerRun = 1;

        /////////////////////////
        // improvement
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

    }

    public void restart(){

        writeStatisticsTime();

        improv.restart();
        if(lengthMeasures > 1){
            oneNDA.restart();
        }
        propertiesPLS.restart();

        effectivePerRun = 1;
         
        effectiveInTotal++;
        
    }


    //mean and variance of independent runs
    public void collectStatistics(){
        if(children == null || children.size()  == 0){
            return;
        }

        propertiesPLS.collectStatistics(children, var);

        ///////////////////
        //calculate the distance in NDA: solution space
        /////////////////////////
        if(lengthMeasures > 1){
            oneNDA.distanceNDA(currentNDA);
        }

        /////////////////////
        // escape, sucessful and improvement
        ////////////////////////////
        improv.collectStatistics(p, children, var);
        
        effectivePerRun++;
        //this.similarityMeasures += similMeasure;

        //indexex
     }

    public synchronized void writeStatistics(){

        propertiesPLS.writeStatistics(fProp);

        improv.writeStaticstics(fImprov);

        if(lengthMeasures > 1){
            oneNDA.writeStatistics(fOneDist);
        }
    }
    
    public void computeStatisticsTime(int currentIndex){

        improvTime[currentIndex].collectStatistics(p, children, var);

        nrOfSwapsInTime[currentIndex] += var.getNumberOfSwaps();
        varOfSwapsInTime[currentIndex] += Math.pow(var.getNumberOfSwaps(),2);

        nrTimeRuns[currentIndex]++;


        propertiesPLSTime[currentIndex].collectStatistics(currentNDA, var);


        ///////////////////
        //calculate the distance in NDA: solution space
        /////////////////////////
        if(lengthMeasures > 1){
            oneNDATime[currentIndex].distanceNDA(currentNDA);
        }

     }
    
    
    public void closeFiles(){
        try{
            fProp.close();
            
            fImprov.close();

            if(lengthMeasures > 1){
                fOneDist.close();
                fOneDistTime.close();
            }
        }catch(Exception e)
		{
			System.out.println("Write not possible:"+fProp);
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
        if(lengthMeasures > 1){
            File myFileOTime = new File(fileN+"_OneNDA.time");
            FileWriter fileStreamOTime = new FileWriter(myFileOTime,false);
            myFileOTime.createNewFile();
            fileStreamOTime = new FileWriter(myFileOTime);
            fOneDistTime = new BufferedWriter(fileStreamOTime);
        }
            // correlation files
            File myFileCTime = new File(fileN+"_Prop.time");
            FileWriter fileStreamCTime = new FileWriter(myFileCTime,false);
            myFileCTime.createNewFile();
            fileStreamCTime = new FileWriter(myFileCTime);
            fPropTime = new BufferedWriter(fileStreamCTime);

            /////////////////////////
            //  improvement; escape; success
            ////////////////
            File myFileImprovTime = new File(fileN+"_Improv.time");
            FileWriter fileStreamImprovTime = new FileWriter(myFileImprovTime,false);
            myFileImprovTime.createNewFile();
            fileStreamImprovTime = new FileWriter(myFileImprovTime);
            fImprovTime = new BufferedWriter(fileStreamImprovTime);
        
            for(int i = 0; i < improvTime.length; i++){
                propertiesPLSTime[i].writeStatistics(fPropTime);
            
                improvTime[i].writeStaticstics(fImprovTime);

                if(lengthMeasures > 1){
                    oneNDATime[i].writeStatistics(fOneDistTime);
                }
        
            }
        
            fPropTime.close();
            fImprovTime.close();
            if(lengthMeasures > 1){
                fOneDistTime.close();
            }
     } catch (Exception e){
			System.out.println("Write not possible:"+fileN);
			e.printStackTrace(System.out);
     }

    }

}