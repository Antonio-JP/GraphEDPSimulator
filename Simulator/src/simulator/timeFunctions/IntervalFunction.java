package simulator.timeFunctions;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class IntervalFunction implements Function {
	
	private ArrayList<Function> functions = new ArrayList<>();
	private SortedSet<Double> intervals = new TreeSet<>();
	
	public void setIntervals(Double ... intervals) {
		this.intervals = new TreeSet<>();
		
		for(double d : intervals) {
			this.intervals.add(d);
		}
	}
	
	public void setFuntions(Function ... functions) throws InvalidParameterException {
		if(functions.length != this.intervals.size()+1) {
			throw new InvalidParameterException("It is needed one more function than points defining intervals");
		}
		
		this.functions = new ArrayList<>();
		for(Function f : functions) {
			this.functions.add(f);
		}
	}

	@Override
	public double getValue(double time) throws InvalidValue {
		int position = 0;
		
		Iterator<Double> iterator = this.intervals.iterator();
		while(iterator.hasNext() && iterator.next() > time) {
			position++;
		}
		
		return this.functions.get(position).getValue(time);
	}

	@Override
	public Function getDerivative() {
		IntervalFunction intFun = new IntervalFunction();
		
		intFun.setIntervals(this.intervals.toArray(new Double[0]));
		Function[] derivatives = new Function[this.functions.size()];
		
		int i = 0;
		for(Function f : this.functions) {
			derivatives[i] = f.getDerivative();
			i++;
		}
		
		intFun.setFuntions(derivatives);
		
		return intFun;
	}

	@Override
	public boolean isConstant(double value) {
		boolean res = true;

		for(Function f : this.functions) {
			res &= f.isConstant(value);
			if(!res) {
				break;
			}
		}
		
		return res;
	}

}
