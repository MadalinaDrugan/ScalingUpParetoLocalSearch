/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scalarization.Sets;

import general.*;
import general.Genetic.*;
import general.Scalarization.*;
/**
 *
 * @author madalina
 */
public class MutOperScalarization  implements PerturbatorScalarization{
    
    private double rateMut = 1.0;
    //private int nrChildren = 2;
            
    final private java.util.Random r = new java.util.Random();
    
    private PerturbatorScalarization[] mut;
    //private PerturbatorScalarization[] recomb;
    
    private int valCurrentOp;
    private boolean typeOperator = false;
    
    public MutOperScalarization(PerturbatorScalarization[] m){
        mut = m;
    }
    
    @Override
    public void choosePerturbator(){
        double rand = r.nextDouble();
        double rateM = rateMut/mut.length;
        valCurrentOp = (int)Math.floor(rand/rateM);
        typeOperator = false;
    }
    
    public PerturbatorScalarization getPerturbator(){
        return mut[valCurrentOp];
    }
    
    public void setPerturbator(PerturbatorScalarization[] m){
        mut = m;
    }
    
    @Override
    public int getNrParents(){
            return mut[valCurrentOp].getNrParents();
    }
   
    @Override
    public int getNrSolutions(){
            return mut[valCurrentOp].getNrSolutions();
    }


    @Override
    public Scalarizer makeScalarizer(Solution[] selectSol, Scalarizer[] children, PerturbatorStrategies strat){
            return mut[valCurrentOp].makeScalarizer(selectSol, children, strat);
    }
    
    @Override
   public int getParent(){
        return mut[valCurrentOp].getParent();
   }
      
    @Override
    public int[] chooseScalarizers(Scalarizer[] parents, int sizeScal){
            return mut[valCurrentOp].chooseScalarizers(parents, sizeScal);
    }

    @Override
    public int[] chooseSolutions(Solution[] parents, int sizeScal){
            return mut[valCurrentOp].chooseSolutions(parents, sizeScal);
    }

    @Override
    public Scalarizer makeRandomScalarizer(Scalarizer[] parents) {
            return mut[valCurrentOp].makeRandomScalarizer(parents);
    }
}
