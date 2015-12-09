package simulator.graph;

import rioko.grapht.AdjacencyListGraph;
import simulator.algebra.exceptions.lalg.IllegalPositionException;
import simulator.algebra.lalg.Matrix;
import simulator.algebra.lalg.impl.MatrixImpl;

public class TimeSimGraph extends AdjacencyListGraph<TimeVertex, TimeEdge>{

	public TimeSimGraph() {
		super(TimeEdge.class, TimeVertex.class);
	}
	
	//Matrix getter Methods
	public Matrix<Double> getLaplacian() {
		MatrixImpl<Double> matrix = new MatrixImpl<>(this.vertexSet().size(), this.vertexSet().size());
		
		try{
			int i = 0;
			for(TimeVertex source : this.vertexSet()) {
				int j = 0;
				for(TimeVertex target : this.vertexSet()) {
					if(i == j) {
						matrix.setElement(i, j, (double)this.vertexFrom(source).size());
					} else if(this.containsEdge(source, target)) {
						matrix.setElement(i, j, 1.0);
					}
					
					j++;
				}
				i++;
			}
			
			return matrix;
		} catch(IllegalPositionException e) {
			//Impossible Exception
			return null;
		}
	}

}
