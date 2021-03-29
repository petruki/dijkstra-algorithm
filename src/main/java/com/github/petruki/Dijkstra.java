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
	
	public Dijkstra(List<Vertex> vertices, String nodeOString) {
		this.vertices = vertices;
		generateTable(nodeOString);
	}

	/**
	 * Generates table containing all shortest path for each node
	 * 
	 * @param nodeOrigin which the algorithm should start calculating
	 */
	public void generateTable(String nodeOrigin) {
		dTable = new DijkstraTable(vertices, nodeOrigin);
		
		Vertex vertex;
		while((vertex = dTable.getVertexUnvisited()) != null) {
			calculateShortestPath(vertex);
		}
	}
	
	/**
	 * Based on the generated table, find the shortest path
	 * 
	 * @return DijkstraResult that can be used to query results from the execution
	 */
	public DijkstraResult getShortestPath(String nodeDestination) {
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
	
	private List<String> buildPathNodeList(String nodeOrigin, String nodeDest) {
		Vertex dsstVertex = dTable.getVertices().stream()
				.filter(v -> v.getNode1().equals(nodeDest))
				.findFirst().get();
		
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
