package simulator.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	public void printToFile(String path) {
		try {
			NumberFormat nf = NumberFormat.getInstance();
			File file = new File(path);
			PrintStream ps = new PrintStream(file);
		
			String line = "";
			for(int i = 0; i < this.vertexSet().size(); i++) {
				line += ";" + i;
			}
			
			ps.println(line);
			
			List<TimeVertex> listOfVertex = Arrays.asList(this.vertexSet().toArray(new TimeVertex[this.vertexSet().size()]));
			for(int i = 0; i < listOfVertex.size(); i++) {
				line = ""+(i);
				for(int j = 0; j < listOfVertex.size(); j++) {
					if(this.containsEdge(listOfVertex.get(i), listOfVertex.get(j))) {
						line += ";" + nf.format(this.getEdgeWeight(this.getEdge(listOfVertex.get(i), listOfVertex.get(j))));
					} else {
						line += ";0";
					}
				}
				
				ps.println(line);
			}
			
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void buildGraphFromFile(String path) {
		try {
			File file = new File(path);
			BufferedReader is = new BufferedReader(new FileReader(file));
			
			String line = is.readLine();
			
			String[] tokens = line.split(";");
			ArrayList<TimeVertex> vertices = new ArrayList<>();
			for(int i = 1; i < tokens.length; i++) {
				TimeVertex vertex = new TimeVertex();
				vertices.add(vertex);
				this.addVertex(vertex);
			}
			
			for(int i = 0; i < vertices.size(); i++) {
				line = is.readLine();
				tokens = line.split(";");
				for(int j = i+1; j < vertices.size(); j++) {
					Double weight = Double.parseDouble(tokens[j+1]);
					if(weight != 0) {
						this.addEdge(vertices.get(i), vertices.get(j));
						this.addEdge(vertices.get(j), vertices.get(i));
					}
				}
			}
			
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
