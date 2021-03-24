package com.github.petruki;

import java.util.ArrayList;
import java.util.List;

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
 * Based on behavior modeling, the implementation is modular enough to be viewed as granular
 * version of the algorithm.
 * 
 * @author Roger Floriano (petruki)
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
	 */
	public void generateTable(String nodeOrigin) {
		dTable = new DijkstraTable(vertices, nodeOrigin);
		
		Vertex vertex = dTable.getVertexUnvisited();
		for (;vertex != null; vertex = dTable.getVertexUnvisited()) {
			calculateDistance(vertex);
		}
	}
	
	/**
	 * Based on the generated table, find the shortest path
	 * 
	 * @return DijkstraResult that can be used to query results from the execution
	 */
	public DijkstraResult getShortestPath(String nodeDestination) {
		List<String> path = new ArrayList<>();
		calcuteBestRoute(dTable.getNodeOrigin(), nodeDestination, path);
		return new DijkstraResult(path, dTable, nodeDestination);
	}

	private void calculateDistance(Vertex vertex) {
		for (Vertex edge : dTable.getPaths(vertex.getNode1())) {
			Vertex adjacent = dTable.getAdjacent(edge.getNeighbour());
			
			if (adjacent == null) continue;
			compareDistances(vertex, edge, adjacent);
		}
	}

	private int compareDistances(Vertex vertex, Vertex edge, Vertex adjacent) {
		int distance = edge.getDistance() + vertex.getDistance();
		
		if (distance < 0)
			distance = Integer.MAX_VALUE;
		else if (distance > 0 && adjacent.getDistance() > distance) {
			adjacent.setDistance(distance);
			adjacent.setNode2(vertex.getNode1());
		}
		return distance;
	}
	
	private void calcuteBestRoute(
			String nodeOrigin, String nodeDest, List<String> path) {
		
		path.add(nodeDest);
		
		for (Vertex v : dTable.getVertices()) {
			if (!nodeDest.equals(v.getNode1())) continue;
			
			if (nodeOrigin.equals(v.getNode2())) {
				path.add(v.getNode2());
			} else {
				calcuteBestRoute(nodeOrigin, v.getNode2(), path);
			}
		}
	}
	
}
