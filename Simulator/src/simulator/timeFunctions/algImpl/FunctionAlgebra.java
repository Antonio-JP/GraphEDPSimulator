package simulator.timeFunctions.algImpl;

import simulator.algebra.Algebra;
import simulator.algebra.exceptions.ZeroDivisionException;
import simulator.algebra.exceptions.lalg.ArithmeticException;
import simulator.timeFunctions.Function;
import simulator.timeFunctions.InvalidValue;

public class FunctionAlgebra extends Algebra<Function, Double> {
	
	private static FunctionAlgebra FUNC_ALGEBRA = new FunctionAlgebra();

	private FunctionAlgebra() {
		super(RealField.getInstance());
	}

	@Override
	public Function multiply(Function f1, Function f2) {
		return new Function() {
			@Override
			public double getValue(double time) throws InvalidValue {
				return f1.getValue(time) * f2.getValue(time);
			}
			
			@Override
			public Function getDerivative() {
				return add(multiply(f1.getDerivative(), f2), multiply(f1,f2.getDerivative()));
			}

			@Override
			public boolean isConstant(double value) {
				try {
					return f1.isConstant() && f2.isConstant() && (f1.getValue(0) * f2.getValue(0) == value);
				} catch (InvalidValue e) {
					//Impossible Exception
					e.printStackTrace();
					return false;
				}
			}
		};
	}
	
	@Override
	public Function divide(Function f1, Function f2) throws ZeroDivisionException {
		if(this.isZero(f2)) {
			throw new ZeroDivisionException();
		}
		
		return new Function() {

			@Override
			public double getValue(double time) throws InvalidValue {
				double denominator = f2.getValue(time);
				
				if(denominator == 0) {
					throw new InvalidValue();
				}
				return f1.getValue(time)/denominator;
			}

			@Override
			public Function getDerivative() {
				try {
					return divide(
							add(
									multiply(f1.getDerivative(), f2), 
									opposite(
											multiply(f1, f2.getDerivative()))),
							multiply(f2, f2));
				} catch (ZeroDivisionException e) {
					// Impossible exception
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public boolean isConstant(double value) {
				try {
					return f1.isConstant() && f2.isConstant() && (f1.getValue(0) / f2.getValue(0) == value);
				} catch (InvalidValue e) {
					//Impossible Exception
					e.printStackTrace();
					return false;
				}
			}
			
		};
	}

	@Override
	public Function inverse(Function e) throws ZeroDivisionException {
		return this.divide(getUnity(), e);
	}

	@Override
	public Function getUnity() {
		return Function.getConstant(1.0);
	}
	
	@Override
	public Boolean isUnity(Function e) {
		return e.isConstant(1);
	}

	@Override
	public Function add(Function f1, Function f2) {
		return new Function() {
			@Override
			public double getValue(double time) throws InvalidValue {
				return f1.getValue(time) + f2.getValue(time);
			}
			
			@Override
			public Function getDerivative() {
				return add(f1.getDerivative(), f2.getDerivative());
			}

			@Override
			public boolean isConstant(double value) {
				try {
					return f1.isConstant() && f2.isConstant() && (f1.getValue(0) + f2.getValue(0) == value);
				} catch (InvalidValue e) {
					//Impossible Exception
					e.printStackTrace();
					return false;
				}
			}
		};
	}

	@Override
	public Function opposite(Function f) {
		return new Function() {
			@Override
			public double getValue(double time) throws InvalidValue {
				return -f.getValue(time);
			}
			
			@Override
			public Function getDerivative() {
				return opposite(f.getDerivative());
			}

			@Override
			public boolean isConstant(double value) {
				return f.isConstant(-value);
			}
		};
	}

	@Override
	public Function getZero() {
		return Function.getConstant(0.0);
	}
	
	@Override
	public Boolean isZero(Function e) {
		return e.isConstant(0);
	}

	@Override
	public Function scalar(Function element, Double scalar) throws ArithmeticException {
		return multiply(element, Function.getConstant(scalar));
	}
	
	//Other operations
		// -- Trigonometric methods
		public Function sin(Function f) {
			return new Function() {

				@Override
				public double getValue(double time) throws InvalidValue {
					return Math.sin(f.getValue(time));
				}

				@Override
				public Function getDerivative() {
					return multiply(
							cos(f),
							f.getDerivative());
				}

				@Override
				public boolean isConstant(double value) {
					try {
						return f.isConstant() && (Math.sin(f.getValue(0)) == value);
					} catch (InvalidValue e) {
						//Impossible Exception
						e.printStackTrace();
						return false;
					}
				}
				
			};
		}
		
		public Function cos(Function f) {
			return new Function() {

				@Override
				public double getValue(double time) throws InvalidValue {
					return Math.cos(f.getValue(time));
				}

				@Override
				public Function getDerivative() {
					return multiply(
							opposite(sin(f)),
							f.getDerivative());
				}

				@Override
				public boolean isConstant(double value) {
					try {
						return f.isConstant() && (Math.cos(f.getValue(0)) == value);
					} catch (InvalidValue e) {
						//Impossible Exception
						e.printStackTrace();
						return false;
					}
				}
				
			};
		}
		
		public Function tan(Function f) {
			return new Function() {

				@Override
				public double getValue(double time) throws InvalidValue {
					return Math.tan(f.getValue(time));
				}

				@Override
				public Function getDerivative() {
					try {
						return divide(f.getDerivative(), 
								multiply(cos(f), cos(f)));
					} catch (ZeroDivisionException e) {
						// Impossible Exception
						e.printStackTrace();
						return null;
					}
				}

				@Override
				public boolean isConstant(double value) {
					try {
						return f.isConstant() && (Math.tan(f.getValue(0)) == value);
					} catch (InvalidValue e) {
						//Impossible Exception
						e.printStackTrace();
						return false;
					}
				}
				
			};
		}

		
		//Exponential/Logarithmic methods
		public Function exp(Function f) {
			return new Function() {
				@Override
				public double getValue(double time) throws InvalidValue {
					return Math.pow(Math.E, f.getValue(time));
				}
				
				@Override
				public Function getDerivative() {
					return multiply(this, f.getDerivative());
				}

				@Override
				public boolean isConstant(double value) {
					try {
						return f.isConstant() && (Math.pow(Math.E, f.getValue(0)) == value);
					} catch (InvalidValue e) {
						//Impossible Exception
						e.printStackTrace();
						return false;
					}
				}
			};
		}
		public Function ln(Function f) {
			return log(f, Math.E);
		}
		
		public Function log(Function f) {
			return log(f, 10);
		}

		public  Function log(Function f, double base) {
			if(base <= 0 || base == 1) {
				return null;
			}
			
			return new Function() {
				@Override
				public double getValue(double time) throws InvalidValue {
					double value = f.getValue(time);
					if(value <= 0) {
						throw new InvalidValue();
					}
					
					return Math.log(value)/Math.log(base);
				}
				
				@Override
				public Function getDerivative() {
					try {
						return divide(
								f.getDerivative(), 
								multiply(f, Function.getConstant(Math.log(base))));
					} catch (ZeroDivisionException e) {
						// Impossible Exception
						e.printStackTrace();
						return null;
					}
				}

				@Override
				public boolean isConstant(double value) {
					try {
						return f.isConstant() && (Math.log(f.getValue(0)) == value*Math.log(base));
					} catch (InvalidValue e) {
						//Impossible Exception
						e.printStackTrace();
						return false;
					}
				}
			};
		}
		
		public Function pow(Function f1, Function f2) {
			if(FunctionAlgebra.getInstance().isZero(f1)) {
				return this.getZero();
			}
			return new Function() {

				@Override
				public double getValue(double time) throws InvalidValue {
					return Math.pow(f1.getValue(time), f2.getValue(time));
				}

				@Override
				public Function getDerivative() {
					try {
						return multiply(
								exp(
										multiply(ln(f1), f2)),
								add(
										divide(multiply(f1.getDerivative(), f2), f1), 
										multiply(f2.getDerivative(), ln(f1))));
					} catch (ZeroDivisionException e) {
						// Impossible exception
						e.printStackTrace();
						return null;
					}
				}

				@Override
				public boolean isConstant(double value) {
					try {
						return f1.isConstant() && f2.isConstant() && (Math.pow(f1.getValue(0), f2.getValue(0)) == value);
					} catch (InvalidValue e) {
						//Impossible Exception
						e.printStackTrace();
						return false;
					}
				}
				
			};
		}
	//Static methods
	public static FunctionAlgebra getInstance() {
		return FunctionAlgebra.FUNC_ALGEBRA;
	}
}
