package simulator.algebra.exceptions;

import simulator.algebra.Ring;

public class NotUnitException extends AlgebraException {

	private static final long serialVersionUID = -1394238383535536093L;

	public NotUnitException(Object element, Ring<?> ring) {
		super("The element " + element.toString() + " is not an unit on the ring " + ring.getClass().getSimpleName());
	}
}
