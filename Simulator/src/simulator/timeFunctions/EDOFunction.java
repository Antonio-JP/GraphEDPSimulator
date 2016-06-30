package simulator.timeFunctions;

import java.util.HashMap;

public class EDOFunction implements Function {
	
	private Double initValue = 0.0;
	private Double elapsedTime = 0.1;
	private Integer minTime = 0;
	private Integer maxTime = 0;
	private Double minValue, maxValue;
	
	private HashMap<Integer, Double> valueMap = new HashMap<>();
	
	private Function derivative;
	
	//Builders
	public EDOFunction(Double elapsedTime, Double initValue) {
		this(Function.getConstant(0.0), elapsedTime, initValue, 0, 0);
	}
	
	public EDOFunction(Double elapsedTime, Double initValue, Integer minTime, Integer maxTime) {
		this(Function.getConstant(0.0), elapsedTime, initValue, minTime, maxTime);
	}
	
	public EDOFunction(Function derivative) {
		this(derivative, 0.1);
	}
	
	public EDOFunction(Function derivative, Double elapsedTime) {
		this(derivative, elapsedTime, 0.0);
	}
	
	public EDOFunction(Function derivative, Double elapsedTime, Double initValue) {
		this(derivative, elapsedTime, initValue, 0, 0);
	}
	
	public EDOFunction(Function derivative, Double elapsedTime, Double initValue, Integer minTime, Integer maxTime) {
		this.derivative = derivative;
		this.elapsedTime = elapsedTime;
		this.initValue = initValue;
		this.minValue = initValue;
		this.maxValue = initValue; 
		
		this.valueMap.put(0, initValue);
		
		try {
			this.evaluate(minTime, maxTime);
		} catch(InvalidValue iv) {
			System.out.println("There are some invaluable methods for this Funtion");
		}
	}
	
	//Getters & Setters
	public void setInitialValue(Double value) {
		this.initValue = value;
		this.maxTime = 0;
		this.minTime = 0;
		this.maxValue = this.initValue;
		this.minValue = this.initValue;
		this.valueMap = new HashMap<>();
		
		this.valueMap.put(0, this.initValue);
	}
	
	public void setDerivative(Function f) {
		this.derivative = f;
		this.minTime = 0;
		this.maxTime = 0;
		this.maxValue = this.initValue;
		this.minValue = this.initValue;
		this.valueMap = new HashMap<>();
		
		this.valueMap.put(0, this.initValue);
	}

	@Override
	public double getValue(double time) throws InvalidValue {
		//First we decide if we need to evaluate more function
		if(time < this.minTime*this.elapsedTime) {
			this.evaluate((int) Math.floor(time/this.elapsedTime), this.maxTime);
		} else if(time > this.maxTime*this.elapsedTime) {
			this.evaluate(this.minTime, (int)Math.ceil(time/this.elapsedTime));
		}
		
		//Now we get the surrounding times evaluated for this Function
		Integer lowerNearestTime = (int)Math.floor(time/this.elapsedTime);
		double proportionToNext = (time - lowerNearestTime*this.elapsedTime)/this.elapsedTime;
		
		if(proportionToNext != 0) {
			return this.valueMap.get(lowerNearestTime)*(1-proportionToNext) + this.valueMap.get(lowerNearestTime + 1)*(proportionToNext);
		} else {
			return this.valueMap.get(lowerNearestTime);
		}
	}

	@Override
	public Function getDerivative() {
		return this.derivative;
	}

	@Override
	public boolean isConstant(double value) {
		return this.getDerivative().isConstant(0.0) && this.initValue == value;
	}
	
	//Private methods
	private void evaluate(Integer minTime, Integer maxTime) throws InvalidValue {
		//We extend the values with the lower time values
		if(minTime < this.minTime) {
			while(this.minTime >= minTime) {
				Double newValue = -this.elapsedTime*this.derivative.getValue(this.minTime*this.elapsedTime) + this.minValue;
				this.valueMap.put(this.minTime-1, newValue);
				this.minTime--;
				this.minValue = newValue;
			}
		}
		
		//We extend the values with the greater time values
		if(maxTime > this.maxTime) {
			while(this.maxTime <= maxTime) {
				Double newValue = this.elapsedTime*this.derivative.getValue(this.maxTime*this.elapsedTime) + this.maxValue;
				this.valueMap.put(this.maxTime+1, newValue);
				this.maxTime++;
				this.maxValue = newValue;
			}
		}
	}

}
