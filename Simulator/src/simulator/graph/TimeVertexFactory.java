package simulator.graph;

import rioko.grapht.VertexFactory;
import simulator.timeFunctions.Function;

public class TimeVertexFactory implements VertexFactory<TimeVertex> {
	
	static TimeVertexFactory INSTANCE = new TimeVertexFactory();

	@Override
	public TimeVertex createVertex(Object... args) {
		if(args.length == 0) {
			return new TimeVertex();
		} else if(args.length == 1 && (args[0] instanceof Function)) {
			return new TimeVertex((Function) args[0]);
		} else {
			return null;
		}
	}

}
