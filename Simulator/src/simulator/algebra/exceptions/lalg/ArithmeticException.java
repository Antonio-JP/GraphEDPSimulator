package simulator.algebra.exceptions.lalg;

import simulator.algebra.exceptions.AlgebraException;

public class ArithmeticException extends AlgebraException {

	private static final long serialVersionUID = -650728099465422569L;

	public ArithmeticException() {
		super("The vector or matrices are not compatible for that operation");
	}
	
	public ArithmeticException(Throwable cause) {
		super("Error performing this operation.", cause);
	}
}
