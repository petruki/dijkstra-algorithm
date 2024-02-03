package com.github.petruki.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraTable {
	private Map<String, List<Vertex>> paths;
	private Set<Vertex> verticesTable;
	private Set<String> visited;
	private final Set<String> ignored;
	private Vertex[] sortedTable;
	private final String nodeOrigin;
	
	public DijkstraTable(Set<Vertex> input, Set<String> ignored, 
			String nodeOrigin) throws Exception {
		this.nodeOrigin = nodeOrigin;
		this.ignored = ignored == null ? new HashSet<>() : ignored;
		
		initTable(input, ignored);
		setNodeOrigin(nodeOrigin);
	}
	
	private void initTable(Set<Vertex> input, Set<String> ignored) {
		paths = new HashMap<>();
		verticesTable = new HashSet<>();
		visited = new HashSet<>();
		
		if (ignored == null)
			ignored = new HashSet<>();
		
		for (Vertex v : input) {
			if (ignored.contains(v.getNode1()) || ignored.contains(v.getNode2()))
				continue;
			
			addPatch(v.getNode1(), v.getNode2(), v.getDistance());
			if (v.isBidirectional())
				addPatch(v.getNode2(), v.getNode1(), v.getDistance());
		}
	}
	
	private void addPatch(String node, String neighbour, float distance) {
		verticesTable.add(new Vertex(node, null));
		if (paths.computeIfAbsent(node, k -> new ArrayList<>()).isEmpty()) {
			paths.put(node, new ArrayList<>());
		}
		
		paths.get(node).add(new Vertex(neighbour, distance));
	}
	
	private void setNodeOrigin(String nodeOrigin) throws Exception {
		verticesTable.stream()
			.filter(v -> v.getNode1().equals(nodeOrigin))
			.findFirst()
			.orElseThrow(() -> new Exception("Node not foud")).setDistance(0);
	}
	
	public Vertex getVertexUnvisited() {
		return getVertexUnvisited_v2();
	}
	
	/**
	 * Cleaner version of node/vertix search
	 */
	public Vertex getVertexUnvisited_v1() {
		return verticesTable.stream()
			.filter(v -> !visited.contains(v.getNode1()))
			.min(Comparator.comparing(Vertex::getDistance))
			.map(v -> { visited.add(v.getNode1()); return v; })
			.orElse(null);
	}
	
	/**
	 * This implementation can be +50% faster than the v1
	 */
	public Vertex getVertexUnvisited_v2() {
		if (sortedTable == null)
			sortedTable = verticesTable.toArray(new Vertex[0]);
		
		Arrays.parallelSort(sortedTable, this::compareDistance);
		
		for (Vertex v : sortedTable) {
			if (!visited.contains(v.getNode1())) {
				visited.add(v.getNode1());
				return v;
			}
		}
		
		return null;
	}
	
	public int compareDistance(Vertex v1, Vertex v2) {
		return v1.getDistance() > v2.getDistance() ? 1 :
			v1.getDistance() == v2.getDistance() ? 0 : -1;
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

	public Set<String> getIgnored() {
		return ignored;
	}

	public Set<Vertex> getVertices() {
		return verticesTable;
	}

	public String getNodeOrigin() {
		return nodeOrigin;
	}

}
