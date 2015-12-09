package simulator.graph;

import rioko.grapht.Edge;
import rioko.grapht.EdgeFactory;

public class TimeEdge implements Edge<TimeVertex> {
	
	private double weight = 1.0;
	
	private TimeVertex source, target;
	
	//Builders
	public TimeEdge() {source = null; target = null;}
	
	public TimeEdge(TimeVertex source, TimeVertex target) {
		this.source = source;
		this.target = target;
		
		this.weight = 1.0;
	}
	
	public TimeEdge(TimeVertex source, TimeVertex target, double weight) {
		this.source = source;
		this.target = target;
		
		this.weight = weight;
	}
	
	//Methods
	@Override
	public void setType(Object obj) { }

	@Override
	public Object getType() {return null;}
	
	public double getWeight() {
		return this.weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public TimeVertex getSource() {
		return this.source;
	}

	@Override
	public TimeVertex getTarget() {
		return this.target;
	}

	@Override
	public EdgeFactory<TimeVertex, ? extends Edge<TimeVertex>> getEdgeFactory() {
		return TimeEdgeFactory.INSTANCE;
	}

}
