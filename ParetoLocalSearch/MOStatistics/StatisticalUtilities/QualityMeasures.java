/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MOStatistics.StatisticalUtilities;

import general.*;
import java.io.BufferedWriter;
/**
 *
 * @author Madalina Drugan, Utrecht University
 *
 */
public class QualityMeasures {

    private int improvement = 0; //improvement; imcomparable or diminating
    private int sucess = 0; //incomparable or dominated/escape
    private int dominates = 0; //dominating others
    //private int improvDominates = 0;
    private int escape = 0; //escape rate   
    private int improvDomin = 0;// non-dominated values

    //
    //private int improvIndex = 0;
    //private int improvDominIndex = 0;
    //private int escapeIndex = 0;
    //private int sucessIndex = 0;
    //private int nrIndex = 0;
    //private int ImprovEscapeI = 0;
    //
    //private int sucPerInterIndex = 0;
    private int sucPerInter = 0;
    private double sucPerInterVar = 0;

    private int sucPerInterDomin = 0;
    private double sucPerInterVarDomin = 0;
    //private int nrInter = 0;

    private int effectivePerRun = 0;
    private int effectiveInTotal = 0;


    public QualityMeasures(int nrRuns){
   }

    public void restart(){

        improvement = 0;
        escape = 0;
        sucess = 0;
        dominates = 0;

        sucPerInter = 0;
        sucPerInterVar = 0;

        sucPerInterDomin = 0;
        sucPerInterVarDomin = 0;
        //ImprovEscapeI = 0;
        //nrInter = 0;

        //improvIndex=0;
        //escapeIndex =0;
        //sucessIndex = 0;

        //nrIndex = 0;

        improvDomin = 0;
        //improvDominIndex = 0;

        effectivePerRun = 1;
        effectiveInTotal++;

    }

    private int tempS = 0;
    private int tempS1 = 0;
    public void collectStatistics(Solution p, ArchiveSolutions children, Variator var){
        if(p == null){
            return;
        }

        /////////////////////
        // escape, sucessful
        ////////////////////////////
        if(!children.contains(p)){
              escape++;
              //escapeIndex++;

              // improvement when non-dominated
              if(!p.dominates(children)){ // || children.dominates(p)) {
                   sucess++;
              }

              // improvement when dominates
              if(children.dominates(p)){
                   dominates++;
              }
         }

         ///////////////////////
         // and improvement
         /////////////////////////
         if(!children.contains(p) && !p.dominates(children)){
               improvement++;
               //improvIndex++;

               sucPerInterVar += Math.pow(tempS,2);
               sucPerInter += tempS;
               tempS = 0;
         } else {
                tempS++;
         }

         ////////////////////////
         // non dominated improvement
         ///////////////////////
         if(!children.contains(p) && children.dominatesAtLeastOne(p)){
             improvDomin++;

             sucPerInterVarDomin += Math.pow(tempS1,2);
             sucPerInterDomin += tempS1;
             tempS1 = 0;
         } else {
             tempS1++;
         }
         //ImprovEscapeI++;
         //var.
         //nrIndex++;

         effectivePerRun++;
    }

    public void writeStaticstics(BufferedWriter fImprov){
        //improvement and escape
        if(effectivePerRun == 0){
            return;
        }
        try{
            //write the header ??
            //fImprov.write("nrMeasures, pNonZero, nrImprov/pNonZero, nrEscape/pNonZero, sucessPerEscape, dominates, dominates/escape, nrInter, (pNonZero_Improv)/improvPerInterv, sucPerInterv/improvPerInterval, distImprov (6), hypervolume (2) \n");
            fImprov.write(effectivePerRun + "\t");
            //fImprov.write(ImprovEscapeI + " \t ");
            fImprov.write(improvement + " \t ");
            fImprov.write(escape + " \t ");
            fImprov.write(sucess + " \t ");
            fImprov.write(dominates + " \t ");           
            fImprov.write(improvDomin + " \t ");

            if(improvement != 0){
                double tempMean = ((double)sucPerInter)/improvement;
                double tempVar = Math.sqrt(((double)sucPerInterVar)/improvement - Math.pow(tempMean,2));
                fImprov.write(tempMean + " \t " + tempVar + "\t");
            } else {
                fImprov.write("0 \t 0 \t ");
            }

            if(improvDomin != 0){
                double tempMean = ((double)sucPerInterDomin)/improvDomin;
                double tempVar = Math.sqrt(((double)sucPerInterVarDomin/improvDomin) - Math.pow(tempMean,2));
                fImprov.write(tempMean + " \t " + tempVar + "\t");
            } else {
                fImprov.write("0 \t 0 \t");
            }
            
            fImprov.newLine();
            fImprov.flush();
        }catch(Exception e){
			System.out.println("Write not possible:"+fImprov);
			e.printStackTrace(System.out);
	}

     }
    
}

