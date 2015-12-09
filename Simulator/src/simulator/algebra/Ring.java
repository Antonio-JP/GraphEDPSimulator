package simulator.algebra;

import simulator.algebra.exceptions.AlgebraException;
import simulator.algebra.exceptions.NotUnitException;
import simulator.algebra.exceptions.ZeroDivisionException;
import simulator.algebra.exceptions.lalg.ArithmeticException;

public interface Ring<T> extends Group<T>{
	//Operations methods 
	public T multiply(T e1, T e2) throws ArithmeticException;
	public default T divide (T e1, T e2) throws ArithmeticException, NotUnitException, ZeroDivisionException {
		return this.multiply(e1, this.inverse(e2));
	}
	
	//Inverse methods
	public T inverse(T e) throws ArithmeticException, NotUnitException, ZeroDivisionException;
	
	//Neutral elements
	public T getUnity();
	public default Boolean isUnity(T e) {
		return this.getUnity().equals(e);
	}
	
	public default boolean isUnit(T e) {
		try {
			this.inverse(e);
			return true;
		} catch(AlgebraException ae) {
			return false;
		}
	}
}
