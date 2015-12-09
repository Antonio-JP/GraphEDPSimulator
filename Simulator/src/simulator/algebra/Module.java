package simulator.algebra;

import simulator.algebra.exceptions.lalg.ArithmeticException;

public abstract class Module<T, R> implements Group<T> {
	protected Ring<R> subyacentRing;
	
	public Module(Ring<R> ring) {
		this.subyacentRing = ring;
	}

	//Scalar methods
	public abstract T scalar(T element, R scalar) throws ArithmeticException;
}
