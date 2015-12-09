package simulator.algebra.lalg;

import simulator.algebra.exceptions.lalg.IllegalPositionException;;

public interface Vector<T> {	
	//Modifying interface
	public T getElement(int pos) throws IllegalPositionException;
	public void setElement(int pos, T value) throws IllegalPositionException; 
	
	public int size();

	
	//Other methods
	public Vector<T> copy();
}
