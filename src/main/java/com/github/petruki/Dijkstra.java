package com.github.petruki;

import java.util.ArrayList;
import java.util.List;

import com.github.petruki.model.DensityMatrix;
import com.github.petruki.model.DijkstraResult;
import com.github.petruki.model.DijkstraTable;
import com.github.petruki.model.Vertex;

/**
 * Famous algrithm by Edsger W. Dijkstra.
 * 
 * Algorithm well explained here:
 * https://www.youtube.com/watch?v=pVfj6mxhdMw
 * 
 * This implementation offers a wide view of how the algorithm works.
 * I have added a few features that can make more complex scenarios such as unidirectional vertices and density matrix for pathfinder simulation.
 * 
 * @author Roger Floriano (petruki)
 * @since 2021-04
 */
public class Dijkstra {
	
	private List<Vertex> vertices;
	private DijkstraTable dTable;
	
	public Dijkstra(List<Vertex> vertices) {
		this.vertices = vertices;
	}
	
	/**
	 * Generates table containing all shortest path for each node
	 * 
	 * @param nodeOrigin which the algorithm should start calculating
	 * @throws Exception 
	 */
	public void generateTable(String nodeOrigin) throws Exception {
		this.generateTable(nodeOrigin, null);
	}
	
	/**
	 * Generates table containing all shortest path for each node
	 * 
	 * @param nodeOrigin which the algorithm should start calculating
	 * @param ignored nodes
	 * @throws Exception 
	 */
	public void generateTable(String nodeOrigin, List<String> ignored) throws Exception {
		dTable = new DijkstraTable(vertices, ignored, nodeOrigin);
		
		Vertex vertex;
		while((vertex = dTable.getVertexUnvisited()) != null) {
			calculateShortestPath(vertex);
		}
	}
	
	/**
	 * Generates table containing all shortest path for each node
	 * 
	 * @param densityMatrix which contains all matrix information
	 * @throws Exception 
	 */
	public void generateTable(DensityMatrix densityMatrix) throws Exception {
		this.generateTable(densityMatrix.getStartNode(), densityMatrix.getIgnored());
	}
	
	/**
	 * Based on the generated table, find the shortest path
	 * 
	 * @return DijkstraResult that can be used to query results from the execution
	 * @throws Exception 
	 */
	public DijkstraResult getShortestPath(String nodeDestination) throws Exception {
		List<String> path = buildPathNodeList(dTable.getNodeOrigin(), nodeDestination);
		return new DijkstraResult(path, dTable, nodeDestination);
	}

	private void calculateShortestPath(Vertex vertex) {
		for (Vertex edge : dTable.getPaths(vertex.getNode1())) {
			Vertex adjacent = dTable.getAdjacent(edge.getNeighbour());
			
			if (adjacent == null) continue;
			compareDistances(vertex, edge, adjacent);
		}
	}

	private float compareDistances(Vertex vertex, Vertex edge, Vertex adjacent) {
		float distance = edge.getDistance() + vertex.getDistance();
		
		if (distance > 0 && adjacent.getDistance() > distance) {
			adjacent.setDistance(distance);
			adjacent.setNode2(vertex.getNode1());
		}
		return distance;
	}
	
	private List<String> buildPathNodeList(String nodeOrigin, String nodeDest) 
			throws Exception {
		
		Vertex dsstVertex = dTable.getVertices().stream()
				.filter(v -> v.getNode1().equals(nodeDest))
				.findFirst().orElseThrow(() -> new Exception("Destination node not found"));
		
		if (nodeOrigin.equals(dsstVertex.getNode2())) {
			List<String> path = new ArrayList<>();
			path.add(dsstVertex.getNode2());
			return path;
		}
		
		List<String> path = buildPathNodeList(nodeOrigin, dsstVertex.getNode2());
		if (!path.contains(dsstVertex.getNode2()))
			path.add(dsstVertex.getNode2());
		path.add(dsstVertex.getNode1());
		return path;
	}
	
}
