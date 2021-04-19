package com.github.petruki.model;

import java.util.Arrays;
import java.util.List;

public class DijkstraResult {
	
	public static final String RESULT_PATTERN = "%s -> %s: %s - cost: %s";
	private List<String> path;
	private DijkstraTable dTable;
	private String nodeDestination;

	public DijkstraResult(List<String> path, DijkstraTable dTable, String nodeDestination) {
		this.path = path;
		this.dTable = dTable;
		this.nodeDestination = nodeDestination;
	}

	public String getResult() {
		Vertex vertexDest = dTable.getVertex(nodeDestination);
		
		if (vertexDest == null) {
			throw new RuntimeException("Destination cannot be reached");
		}
		
		return String.format(RESULT_PATTERN, 
				dTable.getNodeOrigin(), nodeDestination,
				Arrays.toString(path.toArray()), vertexDest.getDistance());
	}
	
	public void printResult(boolean printTable) {
		System.out.println(String.format("\n%s", getResult()));
		
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

	public List<String> getPath() {
		return path;
	}

	public DijkstraTable getDTable() {
		return dTable;
	}

}
