package simulator.graph;

import rioko.grapht.EdgeFactory;

public class TimeEdgeFactory implements EdgeFactory<TimeVertex, TimeEdge> {
	
	static TimeEdgeFactory INSTANCE = new TimeEdgeFactory();

	@Override
	public TimeEdge createEdge(TimeVertex source, TimeVertex target) {
		return new TimeEdge(source, target);
	}

}
