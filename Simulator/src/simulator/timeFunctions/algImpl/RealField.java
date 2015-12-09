package simulator.timeFunctions.algImpl;

import simulator.algebra.Field;
import simulator.algebra.exceptions.ZeroDivisionException;
import simulator.algebra.exceptions.lalg.ArithmeticException;

public class RealField implements Field<Double> {
	
	private static RealField REAL_FIELD = new RealField();
	
	private RealField() {}

	@Override
	public Double multiply(Double e1, Double e2) throws ArithmeticException {
		return e1*e2;
	}

	@Override
	public Double getUnity() {
		return 1.0;
	}

	@Override
	public Double add(Double e1, Double e2) throws ArithmeticException {
		return e1+e2;
	}

	@Override
	public Double opposite(Double e) throws ArithmeticException {
		return -e;
	}

	@Override
	public Double getZero() {
		return 0.0;
	}

	@Override
	public Double inverse(Double e) throws ZeroDivisionException {
		if(this.isZero(e)) {
			throw new ZeroDivisionException();
		} else {
			return 1.0/e;
		}
	}

	//Static methods
	public static RealField getInstance() {
		return RealField.REAL_FIELD;
	}
}
