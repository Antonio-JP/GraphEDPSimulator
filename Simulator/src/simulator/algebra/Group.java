package simulator.algebra;

import simulator.algebra.exceptions.lalg.ArithmeticException;

public interface Group<T> {
	//Operations methods 
	public T add(T e1, T e2) throws ArithmeticException;
	public default T remove(T e1, T e2) throws ArithmeticException {
		return this.add(e1, this.opposite(e2));
	}
	
	//Inverse methods
	public T opposite(T e) throws ArithmeticException;
	
	//Neutral elements methods
	public T getZero();
	public default Boolean isZero(T e) {
		return this.getZero().equals(e);
	}
}
