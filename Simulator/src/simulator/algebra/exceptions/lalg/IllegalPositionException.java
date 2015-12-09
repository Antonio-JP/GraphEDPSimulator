package simulator.algebra.exceptions.lalg;

import simulator.algebra.exceptions.AlgebraException;

public class IllegalPositionException extends AlgebraException {

	private static final long serialVersionUID = -2232767741419768683L;
	
	public IllegalPositionException() {
		super("Impossible to access that position in a vector or matrix");
	}
}
