package simulator.algebra;

import simulator.algebra.exceptions.ZeroDivisionException;

public interface Field<T> extends Ring<T> {
	@Override
	public T inverse(T e) throws ZeroDivisionException;
}
