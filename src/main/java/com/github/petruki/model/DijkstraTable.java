package com.github.petruki.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraTable {
	
	private Map<String, List<Vertex>> paths;
	private Set<Vertex> verticesTable;
	private List<String> visited;
	private final String nodeOrigin;
	
	public DijkstraTable(List<Vertex> input, String nodeOrigin) {
		this.nodeOrigin = nodeOrigin;
		
		initTable(input);
		setNodeOrigin(nodeOrigin);
	}
	
	private void initTable(List<Vertex> input) {
		paths = new HashMap<>();
		verticesTable = new HashSet<>();
		visited = new ArrayList<>();
		
		for (Vertex v : input) {
			addPatch(v.getNode1(), v.getNode2(), v.getDistance());
			if (v.isBidirectional())
				addPatch(v.getNode2(), v.getNode1(), v.getDistance());
		}
	}
	
	private void addPatch(String node, String neighbour, int distance) {
		verticesTable.add(new Vertex(node, null));
		if (!paths.containsKey(node))
			paths.put(node, new ArrayList<>());
		
		paths.get(node).add(new Vertex(neighbour, distance));
	}
	
	private void setNodeOrigin(String nodeOrigin) {
		verticesTable.stream()
			.filter(v -> v.getNode1().equals(nodeOrigin))
			.findFirst()
			.get().setDistance(0);
	}
	
	public Vertex getVertexUnvisited() {
		return verticesTable.stream()
				.filter(v -> !visited.contains(v.getNode1()))
				.min(Comparator.comparing(Vertex::getDistance))
				.map(v -> { visited.add(v.getNode1()); return v; })
				.orElse(null);
	}
	
	public Vertex getVertex(String node) {
		return verticesTable.stream()
				.filter(v -> v.getNode1().equals(node))
				.findFirst()
				.orElse(null);
	}
	
	public Vertex getAdjacent(String neighbour) {
		return verticesTable.stream()
				.filter(e -> e.getNode1().equals(neighbour) 
						&& !visited.contains(neighbour))
				.findFirst().orElse(null);
	}
	
	public List<Vertex> getPaths(String node) {
		return paths.get(node);
	}

	public Set<Vertex> getVertices() {
		return verticesTable;
	}

	public String getNodeOrigin() {
		return nodeOrigin;
	}

}
