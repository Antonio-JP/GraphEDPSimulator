package simulator.graph;

import rioko.grapht.Vertex;
import rioko.grapht.VertexFactory;
import simulator.timeFunctions.Function;

public class TimeVertex implements Vertex {
	
	private Function valueFunction;
	private boolean mark = false;
	
	//Builders
	public TimeVertex() {
		this.valueFunction = Function.getConstant(0);
	}
	
	public TimeVertex(Function function) {
		this.valueFunction = function;
	}

	//Getters & Setters
	public Function getFunction() {
		return this.valueFunction;
	}
	
	public void setFunction(Function function) {
		this.valueFunction = function;
	}
	
	@Override
	public TimeVertex copy() {
		return new TimeVertex(this.valueFunction);
	}

	@Override
	public VertexFactory<?> getVertexFactory() {
		return TimeVertexFactory.INSTANCE;
	}

	@Override
	public boolean getMark() {
		return this.mark;
	}

	@Override
	public void setMark(boolean marked) {
		this.mark = marked;
	}

}
