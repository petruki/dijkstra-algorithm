package com.github.petruki;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.github.petruki.model.DensityMatrix;
import com.github.petruki.model.DijkstraResult;
import com.github.petruki.model.DijkstraTable;
import com.github.petruki.model.Vertex;

/**
 * Famous algrithm by Edsger W. Dijkstra.
 * <p>
 * Algorithm well explained
 * <a href="https://www.youtube.com/watch?v=pVfj6mxhdMw">here</a>
 * <p>
 * This implementation offers a wide view of how the algorithm works.
 * I have added a few features that can make more complex scenarios such as unidirectional vertices and density matrix for pathfinder simulation.
 *
 * @author Roger Floriano (petruki)
 * @since 2021-04
 */
public class Dijkstra {
	
	private final Set<Vertex> vertices;
	private DijkstraTable dTable;
	
	public Dijkstra(Set<Vertex> vertices) {
		this.vertices = vertices;
	}
	
	public Dijkstra(List<Vertex> vertices) {
		this.vertices = new HashSet<>();
		this.vertices.addAll(vertices);
	}
	
	/**
	 * Generates table containing all shortest path for each node
	 * 
	 * @param nodeOrigin which the algorithm should start calculating
     */
	public void generateTable(String nodeOrigin) throws Exception {
		this.generateTable(nodeOrigin, null);
	}
	
	/**
	 * Generates table containing all shortest path for each node
	 * 
	 * @param nodeOrigin which the algorithm should start calculating
	 * @param ignored nodes
     */
	public void generateTable(String nodeOrigin, Set<String> ignored) throws Exception {
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
     */
	public void generateTable(DensityMatrix densityMatrix) throws Exception {
		this.generateTable(densityMatrix.getStartNode(), densityMatrix.getIgnored());
	}
	
	/**
	 * Based on the generated table, find the shortest path
	 * 
	 * @return DijkstraResult that can be used to query results from the execution
     */
	public DijkstraResult getShortestPath(String nodeDestination) throws Exception {
		var path = buildPathNodeList(dTable.getNodeOrigin(), nodeDestination);
		return new DijkstraResult(path, dTable, nodeDestination);
	}

	private void calculateShortestPath(Vertex vertex) {
		for (var edge : dTable.getPaths(vertex.getNode1())) {
			var adjacent = dTable.getAdjacent(edge.getNeighbour());
			
			if (adjacent == null) continue;
			compareDistances(vertex, edge, adjacent);
		}
	}

	private void compareDistances(Vertex vertex, Vertex edge, Vertex adjacent) {
		var distance = edge.getDistance() + vertex.getDistance();
		
		if (distance > 0 && adjacent.getDistance() > distance) {
			adjacent.setDistance(distance);
			adjacent.setNode2(vertex.getNode1());
		}
	}
	
	private Set<String> buildPathNodeList(String nodeOrigin, String nodeDest) 
			throws Exception {
		
		final var dstVertex = dTable.getVertices().stream()
				.filter(v -> v.getNode1().equals(nodeDest))
				.findFirst().orElseThrow(() -> new Exception("Destination node not found"));
		
		if (nodeOrigin.equals(dstVertex.getNode2())) {
			final var path = new LinkedHashSet<String>();
			path.add(dstVertex.getNode2());
			return path;
		}
		
		final var path = buildPathNodeList(nodeOrigin, dstVertex.getNode2());
        path.add(dstVertex.getNode2());
		path.add(dstVertex.getNode1());

		return path;
	}
	
}
