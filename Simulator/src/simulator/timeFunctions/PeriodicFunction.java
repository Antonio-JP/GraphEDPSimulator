package simulator.timeFunctions;

public class PeriodicFunction implements Function {

	Double period;
	Function function = Function.getConstant(0.0);
	
	//Builders
	public PeriodicFunction(Double period) {
		this.period = period;
	}
	
	public PeriodicFunction(Double period, Function f) {
		this.period = period;
		this.function = f;
	}
	
	//Setters & Getters
	public void setPeriod(Double period) {
		this.period = period;
	}
	
	public void setFunction(Function f) {
		this.function = f;
	}
	
	//Functions methods
	@Override
	public double getValue(double time) throws InvalidValue {
		Double lowestInitPeriodTime = Math.floor(time/this.period)*this.period;
		
		return this.function.getValue(time-lowestInitPeriodTime);
	}

	@Override
	public Function getDerivative() {
		return new PeriodicFunction(this.period, this.function.getDerivative());
	}

	@Override
	public boolean isConstant(double value) {
		return this.function.isConstant(value);
	}

}
