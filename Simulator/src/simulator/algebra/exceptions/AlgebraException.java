package simulator.algebra.exceptions;

public class AlgebraException extends Exception {

	private static final long serialVersionUID = -4699142426622598325L;

	public AlgebraException() {}
	
	public AlgebraException(String message) {
		super("Algebra exception: " + message);
	}
	
	public AlgebraException(Throwable thr) {
		super("Algebra exception caused by another exception.", thr);
	}
	
	public AlgebraException(String message, Throwable thr) {
		super("Algebra exception : " + message, thr);
	}
}
