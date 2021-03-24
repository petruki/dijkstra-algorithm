package com.github.petruki.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DijkstraResult {
	
	public static final String RESULT_PATTERN = "%s -> %s: %s - cost: %s";
	private List<String> path;
	private DijkstraTable dTable;
	private String nodeDestination;

	public DijkstraResult(List<String> path, DijkstraTable dTable, String nodeDestination) {
		Collections.reverse(path);
		this.path = path;
		this.dTable = dTable;
		this.nodeDestination = nodeDestination;
	}

	public String getResult() {
		Vertex vertexDest = dTable.getVertex(nodeDestination);
		return String.format(RESULT_PATTERN, 
				dTable.getNodeOrigin(), nodeDestination,
				Arrays.toString(path.toArray()), vertexDest.getDistance());
	}

}
