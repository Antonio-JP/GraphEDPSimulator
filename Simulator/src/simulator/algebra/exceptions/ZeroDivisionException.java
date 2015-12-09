package simulator.algebra.exceptions;

public class ZeroDivisionException extends AlgebraException {

	private static final long serialVersionUID = -7177185140784925979L;
	
	public ZeroDivisionException() {
		super("Impossible to divide to zero in any Ring");
	}
}
