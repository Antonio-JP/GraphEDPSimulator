package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import simulator.algebra.exceptions.lalg.ArithmeticException;
import simulator.graph.TimeSimGraph;
import simulator.graph.TimeVertex;
import simulator.timeFunctions.EDOFunction;
import simulator.timeFunctions.Function;
import simulator.timeFunctions.InvalidValue;
import simulator.timeFunctions.algImpl.FunctionAlgebra;

public class Simulator {
	private static FunctionAlgebra algebra = FunctionAlgebra.getInstance();
	
	private static double elapsedTime = 0.01;
	
	public static void main(String args[]) {
		try {
			int size = Integer.parseInt(args[0]);
			double density = Double.parseDouble(args[1]);
			double initTime = Double.parseDouble(args[2]);
			double time = Double.parseDouble(args[3]);
		
		
			TimeSimGraph graph = CreateRandomGraph(size, density);
		
		
			SetUpInitialConditions(graph);
			
			System.out.println("Getting the EDP solution");
			
			RunUntilTime(graph, time);
			
			System.out.println("Graph have been run");
			
			CreateOutput(graph, initTime, time);
			
			System.out.println("Output created");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static TimeSimGraph CreateRandomGraph(int size, double density) {
		TimeSimGraph graph = new TimeSimGraph();
		
		Random r = new Random();
		
		for(int i = 0; i < size; i++) {
			graph.addVertex(new TimeVertex());
		}
		
		List<TimeVertex> vertexList = Arrays.asList(graph.vertexSet().toArray(new TimeVertex[0]));
		
		for(int i = 0; i < vertexList.size(); i++) {
			for(int j = i+1; j < vertexList.size(); j++) {
				if(r.nextDouble() < density) {
					graph.addEdge(vertexList.get(i), vertexList.get(j));
					graph.addEdge(vertexList.get(j), vertexList.get(i));
				}
			}
		}
		
		return graph;		
	}

	private static void SetUpInitialConditions(TimeSimGraph graph) throws ArithmeticException {
		Set<TimeVertex> setOfVertex = graph.vertexSet();
		Iterator<TimeVertex> iterator = setOfVertex.iterator();
		
		TimeVertex first = iterator.next();
		first.setFunction(algebra.add(
				algebra.sin(algebra.multiply(
						Function.getIdentity(),
						Function.getConstant(2*Math.PI))),
				Function.getConstant(1.0)));
		
		while(iterator.hasNext()) {
			TimeVertex v = iterator.next();
			
			v.setFunction(new EDOFunction(elapsedTime,0.0));
		}
		
		for(TimeVertex v : graph.vertexSet()) {
			if(!v.equals(first)) {
				EDOFunction f = (EDOFunction)v.getFunction();
				
				Function der = algebra.getZero();
				for(TimeVertex neig : graph.vertexFrom(v)) {
					der = algebra.add(der, neig.getFunction());
				}
				
				der = algebra.remove(der, algebra.scalar(f, (double) graph.vertexFrom(v).size()));
				
				f.setDerivative(der);
			}
		}
		
	}

	private static void RunUntilTime(TimeSimGraph graph, double time) throws InvalidValue {
		for(TimeVertex v : graph.vertexSet()) {
			v.getFunction().getValue(time);
		}
	}

	private static void CreateOutput(TimeSimGraph graph, double init, double time) throws InvalidValue, FileNotFoundException {
		double currentTime = init;
		
		File outFile = new File("./output.csv");
		PrintStream stream = new PrintStream(outFile);
		
		NumberFormat format = NumberFormat.getInstance();
		
		while(currentTime <= time) {
			String out = "";
			Iterator<TimeVertex> iterator = graph.vertexSet().iterator();
			out += format.format(currentTime);
			
			while(iterator.hasNext()) {
				out+=";";
				out+=format.format(iterator.next().getFunction().getValue(currentTime));
			}
			
			stream.println(out);
			currentTime += elapsedTime;
		}
		
		stream.close();
	}
}
