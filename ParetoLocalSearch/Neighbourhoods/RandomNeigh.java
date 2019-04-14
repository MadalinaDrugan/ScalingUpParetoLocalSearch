package Neighbourhoods;

import Neighbourhoods.Iterator_Solution.Iterator_Solutions_QAPs;
import general.Neighbourhoods.SetNeighbourhoods;
import general.ProblemInstance;
import general.Solution;
import general.Variator;
import java.util.Stack;


public class RandomNeigh implements SetNeighbourhoods {

    Stack<Iterator_Solutions_QAPs> neigh = new Stack<Iterator_Solutions_QAPs>();
    final static java.util.Random r = new java.util.Random();
    final long identificationNumber = r.nextLong();

    int currentScal;

    public RandomNeigh(Stack<Iterator_Solutions_QAPs> scal){
        for(int i = 0; i < scal.size(); i++){
            neigh.add(scal.get(i));
        }
        currentScal = r.nextInt(scal.size());
    }

    /*public RandomRed(Stack<CollectScalarizers> s){
        reduction = scal;
        currentScal = r.nextInt(scal.size());
    }*/

    public RandomNeigh(){}

    //public abstract boolean adaptation(Solution[] s, long[] refPoint);
    @Override
    public void setNeighbours(Iterator_Solutions_QAPs scal){
        if(containsNeigh(scal)) {
            currentScal = indexOfNeigh(scal);
        }
        else {
            addNeigh(scal);
        }
    }

    @Override
    public void setNeighbours(int indentif){
        if(indentif < neigh.size()) {
            currentScal = indentif;
        }
    }

    @Override
    public Iterator_Solutions_QAPs getNeighbours(){
        return neigh.get(currentScal);
    }

    @Override
    public int getCurrentIndexNeigh(){
        return currentScal;
    }

    @Override
    public Iterator_Solutions_QAPs getNeighbours(int indentif){
        currentScal = indentif;
        return neigh.get(indentif);
    }

    @Override
    public int indexOfNeigh(Iterator_Solutions_QAPs sc){
        for(int i = 0; i < neigh.size(); i++){
            if(neigh.get(i).getNeigh() == sc.getNeigh()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsNeigh(Iterator_Solutions_QAPs sc){
        for(int i = 0; i < neigh.size(); i++){
            if(neigh.get(i).getNeigh() == sc.getNeigh()) {
                return true;
            }
        }
        return false;
    }

    // add and remove scalarizers
    @Override
    public boolean addNeigh(Iterator_Solutions_QAPs scal){
        if(!containsNeigh(scal)){
            return neigh.add(scal);
        }
        return false;
    }

    @Override
    public Iterator_Solutions_QAPs removeNeigh(int indentif){
        if(neigh.size() > indentif){
            return neigh.remove(indentif);
        }
        return null;
    }

    @Override
    public void setNeigh(int indentif, Iterator_Solutions_QAPs scal){
        neigh.set(indentif, scal);
    }

    @Override
    public int chooseNeighbours(){
        //double rand = r.nextDouble();
        currentScal = r.nextInt(neigh.size());
                //(int)Math.round(rand*neigh.size());
        return currentScal;
    }

    @Override
    public boolean adaptationNeigh(){
        return false;
    }

    @Override
    public void restart(){
        currentScal = r.nextInt(neigh.size());
    }

    //public void choseScalarization(){}
    //
    @Override public String toString(){
	StringBuilder sb = new StringBuilder();
        for(int i = 0; i < neigh.size(); i++){
            sb.append(neigh.get(i).toString());
            sb.append("\n");
        }
	return sb.toString();
    }

    ///////////////////////////
    //////////////////////////////
    // scalarizer
    public int compareTo(Iterator_Solutions_QAPs o){
        for(int i = 0; i < neigh.size(); i++) {
            if(neigh.get(i).getNeigh() == o.getNeigh()) {
                return 0;
            }
        }
        return 1;
    }

    public int compare(Iterator_Solutions_QAPs o, Iterator_Solutions_QAPs o1){
        if(o == o1 || o.getNeigh() == o1.getNeigh()) {
            return 0;
        }
        return 1;
    }

    @Override public int hashCode(){
        return (int) this.identificationNumber;
    }

    @Override public RandomNeigh clone(){
        RandomNeigh newS = new RandomNeigh(neigh);
        return newS;
    }

    @Override public boolean equals(Object o){
        if (this==o) {
            return true;
        }
        if(neigh.size() != ((RandomNeigh) o).neigh.size()) {
            return false;
        }
        for(int i=0;i<neigh.size();i++){
              boolean eq = false;
              for(int j = 0; j < neigh.size(); j++){
                if(neigh.get(i).equals(((RandomNeigh)o).neigh.get(i))){
                  eq = true;
                }
              }
              if(!eq) {
                return false;
            }
          }
          return true;
    }

    @Override
    public int sizeNeigh(){
        return neigh.size();
    }
    
    @Override
    public Iterator_Solutions_QAPs[] toArrayNeigh(){
        Iterator_Solutions_QAPs[] tempS = new Iterator_Solutions_QAPs[neigh.size()];
        for(int i = 0; i < neigh.size(); i++){
            tempS[i] = neigh.get(i);
        }
        return tempS;
    }

    @Override
    public boolean hasNext() {
        return neigh.get(currentScal).hasNext();
    }

    @Override
    public void iteratorInit(Solution ps, Variator var, ProblemInstance problem) {
        // reset the current index;
        restart();
        chooseNeighbours();
        neigh.get(currentScal).iteratorInit(ps, var, problem);
    }

    @Override
    public void iteratorInit(Solution ps, ProblemInstance problem) {
        restart();
        chooseNeighbours();
        neigh.get(currentScal).iteratorInit(ps, problem);
    }

    @Override
    public Solution next() {
        return neigh.get(currentScal).next();
    }

    @Override
    public boolean adaptation() {
        return false;
    }

}
