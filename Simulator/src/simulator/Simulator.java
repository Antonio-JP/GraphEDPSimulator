package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import simulator.algebra.exceptions.lalg.ArithmeticException;
import simulator.graph.TimeSimGraph;
import simulator.graph.TimeVertex;
import simulator.timeFunctions.EDOFunction;
import simulator.timeFunctions.Function;
import simulator.timeFunctions.IntervalFunction;
import simulator.timeFunctions.InvalidValue;
import simulator.timeFunctions.PeriodicFunction;
import simulator.timeFunctions.algImpl.FunctionAlgebra;

public class Simulator {
	private static FunctionAlgebra algebra = FunctionAlgebra.getInstance();
	
	private static double elapsedTime = 0.01;
	
	private static String pathToFile = null;
	
	public static void main(String args[]) {
		try {
			int size = Integer.parseInt(args[0]);
			double density = Double.parseDouble(args[1]);
			double initTime = Double.parseDouble(args[2]);
			double time = Double.parseDouble(args[3]);
			
			if(args.length >= 5) {
				elapsedTime = Double.parseDouble(args[4]);
			}
			
			if(args.length >= 6) {
				pathToFile = args[5];
			}
			
			//Checking parameters
			if(size <= 0) {
				throw new Exception("The size (first parameter) should be a positive integer");
			} else if(density <= 0 || density > 1) {
				throw new Exception("The density (second parameter) should be a double in the interval (0,1]");
			} else if(initTime < 0) {
				throw new Exception("The init Time (third parameter) should be a double greater or equal than zero");
			} else if(time <= initTime + elapsedTime) {
				throw new Exception("The time to evalueate (fourth parameter) should be grater than the init time to print the results");
			} else if(elapsedTime <= 0) {
				throw new Exception("The elapsed time (fifth parameter) shoul be a strict positive number");
			}
		
		
			TimeSimGraph graph = CreateGraph(size, density);		
		
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

	private static TimeSimGraph CreateGraph(int size, double density) {
		TimeSimGraph graph = new TimeSimGraph();
		if(pathToFile == null) { //Random graph
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
			graph.printToFile("./graphs/graph.csv");
		} else {
			graph.buildGraphFromFile(pathToFile);
		}
		
		return graph;		
	}

	private static void SetUpInitialConditions(TimeSimGraph graph) throws ArithmeticException {

		HashSet<TimeVertex> boundary = new HashSet<>();
		
		//No boundary -> Do Nothing
		
		//We create the boundary of the graph
		//First vertex as boundary
//		Set<TimeVertex> setOfVertex = graph.vertexSet();
//		Iterator<TimeVertex> iterator = setOfVertex.iterator();
//		boundary.add(iterator.next());
		
		
		
		SetBoundaryFunction(boundary);
		
		SetPDEOverVertices(graph, boundary);
		
		SetInitialData(graph, boundary);
		
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

	private static void SetBoundaryFunction(Set<TimeVertex> boundary) {
		for(TimeVertex v : boundary) {
			v.setFunction(GetBoundaryFunction());
		}
	}	
	
	private static Function GetBoundaryFunction() {
		// Funcion senosoidal siempre positiva
//		return algebra.add(
//				algebra.sin(algebra.multiply(
//						Function.getIdentity(),
//						Function.getConstant(2*Math.PI))),
//				Function.getConstant(1.0));
		//Funcion de pulsos 1 - 0
		IntervalFunction in = new IntervalFunction();
		in.setIntervals(1.0);
		in.setFuntions(Function.getConstant(1.0), Function.getConstant(0.0));
		return new PeriodicFunction(2.0, in);
	}
	
	private static void SetPDEOverVertices(TimeSimGraph graph, Set<TimeVertex> boundaryVertices) throws ArithmeticException {
		//Initial set to EDP functions
		for(TimeVertex v : graph.vertexSet()) {
			if(!boundaryVertices.contains(v)) {
				v.setFunction(new EDOFunction(elapsedTime, 0.0));
			}
		}
		
		//Heat Equation
		for(TimeVertex v : graph.vertexSet()) {
			if(!boundaryVertices.contains(v)) {
				EDOFunction f = (EDOFunction)v.getFunction();
				
				Function der = algebra.getZero();
				for(TimeVertex neig : graph.vertexFrom(v)) {
					der = algebra.add(der, neig.getFunction());
				}
				
				der = algebra.remove(der, algebra.scalar(f, (double) graph.vertexFrom(v).size()));
				
				f.setDerivative(der);
			}
		}
		//Wave equation		
//		for(TimeVertex v : graph.vertexSet()) {
//			if(!boundaryVertices.contains(v)) {
//				EDOFunction vertexFunc = (EDOFunction)v.getFunction();
//				EDOFunction f = new EDOFunction(elapsedTime, 0.0);
//				
//				Function der = algebra.getZero();
//				for(TimeVertex neig : graph.vertexFrom(v)) {
//					der = algebra.add(der, neig.getFunction());
//				}
//				
//				der = algebra.remove(der, algebra.scalar(vertexFunc, (double) graph.vertexFrom(v).size()));
//				
//				f.setDerivative(der);
//				vertexFunc.setDerivative(f);
//			}
//		}
	}

	private static void SetInitialData(TimeSimGraph graph, HashSet<TimeVertex> boundary) {
		//All to zero -> Do nothing
		
		//The first to 1
		for(TimeVertex v : graph.vertexSet()) {
			((EDOFunction)v.getFunction()).setInitialValue(1.0);
			break;
		}
		
		//The half to 1
//		int i = 0;
//		for(TimeVertex v : graph.vertexSet()) {
//			if(!boundary.contains(v) && (i%2 == 0)) {
//				((EDOFunction)v.getFunction()).setInitialValue(1.0);
//			}
//			i++;
//		}
	}
}
