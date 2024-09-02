package com.github.petruki.model;

import java.util.Arrays;
import java.util.Set;

public class DijkstraResult {
	
	public static final String RESULT_PATTERN = "%s -> %s: %s - cost: %s";
	private final Set<String> path;
	private final DijkstraTable dTable;
	private final String nodeDestination;

	public DijkstraResult(Set<String> path, DijkstraTable dTable, String nodeDestination) {
		this.path = path;
		this.dTable = dTable;
		this.nodeDestination = nodeDestination;
	}

	public String getResult() {
		var vertexDest = dTable.getVertex(nodeDestination);
		
		if (vertexDest == null) {
			throw new RuntimeException("Destination cannot be reached");
		}
		
		return String.format(RESULT_PATTERN, 
				dTable.getNodeOrigin(), nodeDestination,
				Arrays.toString(path.toArray()), vertexDest.getDistance());
	}
	
	public void printResult(boolean printTable) {
		System.out.println("\n" + getResult());
		
		if (printTable) {
			dTable.getVertices().stream().map(v -> {
				return v.toString()
						.replace("node1", "vertex")
						.replace("node2", "prev")
						.replace("null", " ")
						.replace("]", "")
						.replace("Vertex [", "");
			}).forEach(System.out::println);	
		}
	}

	public Set<String> getPath() {
		return path;
	}

	public DijkstraTable getDTable() {
		return dTable;
	}

}
