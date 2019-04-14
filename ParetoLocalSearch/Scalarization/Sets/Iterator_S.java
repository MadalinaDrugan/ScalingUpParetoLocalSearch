/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scalarization.Sets;

import general.Scalarization.*;
import java.util.*;
/**
 *
 * @author madalina
 */
public class Iterator_S extends RandomScal implements ScalarizerIterator{
    
    //SetScalarizeres scalarizer;
    
    public Iterator_S(Stack<Scalarizer> scal){
        scalarizer = super.scalarizer;
    }
            
    private int interS = 0;
    private Scalarizer tempS;
    @Override
    public Scalarizer iterator(){
        if(interS < size()){
            tempS = getScalarizer(interS);
            interS++;
            return tempS;
        }
        if(interS == size()){
            interS = 0;
            return null;
        }
        interS= 0;
        return null;
    }
    
    @Override
    public void restartIterator(){
        interS = 0;
    }
    
    @Override
    public boolean endIterator(){
        if(interS < size()){
            return false;
        }
        return true;
    }
}
