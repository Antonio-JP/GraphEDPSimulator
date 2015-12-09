package simulator.timeFunctions;

public interface Function {
	public double getValue(double time) throws InvalidValue;
	public Function getDerivative();
	
	public boolean isConstant(double value);
	
	//Default methods
	public default boolean isConstant() {
		return this.getDerivative().isConstant(0);
	}
	
	//Basic getter methods
	public static Function getConstant(double c) {
		return new Function() {

			@Override
			public double getValue(double time) throws InvalidValue {
				return c;
			}

			@Override
			public Function getDerivative() {
				return Function.getConstant(0);
			}

			@Override
			public boolean isConstant(double value) {
				return value == c;
			}
			
		};
	}
	
	public static Function getIdentity() {
		return new Function() {

			@Override
			public double getValue(double time) throws InvalidValue {
				return time;
			}

			@Override
			public Function getDerivative() {
				return Function.getConstant(0);
			}

			@Override
			public boolean isConstant(double value) {
				return false;
			}
			
		};
	}
	
	public static Function getPolynomial(double ... coefs) {	
		if(coefs.length == 0) {
			return Function.getConstant(0);
		} else if(coefs.length == 1) {
			return Function.getConstant(coefs[0]);
		}
		
		return new Function() {

			@Override
			public double getValue(double time) throws InvalidValue {
				double res = 0;
				
				for(int i = 0; i < coefs.length; i++) {
					res += coefs[i]*Math.pow(time, i);
				}
				
				return res;
			}

			@Override
			public Function getDerivative() {
				if(coefs.length == 2) {
					return Function.getConstant(coefs[1]);
				}
				
				double[] newCoefs = new double[coefs.length -1];
				
				for(int i = 1; i < coefs.length; i++) {
					newCoefs[i-1] = coefs[i]*i;
				}
				
				return Function.getPolynomial(newCoefs);
			}

			@Override
			public boolean isConstant(double value) {
				return false;
			}
			
		};
	}	
}
