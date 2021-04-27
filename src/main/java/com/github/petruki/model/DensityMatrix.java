package com.github.petruki.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DensityMatrix {
	
	private Set<Vertex> vertices = new HashSet<>();
	private Set<String> ignored = new HashSet<>();
	private String[][] matrix;
	private String startNode;
	private String endNode;
	private int maxSizeY;
	
	public void addVertex(Vertex vertex) {
		vertices.add(vertex);
	}
	
	public void addIgnore(String nodeId) {
		ignored.add(nodeId);
	}
	
	public Set<Vertex> getVertices() {
		return vertices;
	}
	
	public void setVertices(Set<Vertex> matrix) {
		this.vertices = matrix;
	}
	
	public String[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(String[][] matrix) {
		this.matrix = matrix;
	}

	public Set<String> getIgnored() {
		return ignored;
	}
	
	public void setIgnored(Set<String> ignored) {
		this.ignored = ignored;
	}
	
	public void setIgnored(List<String> ignored) {
		this.ignored = new HashSet<>();
		this.ignored.addAll(ignored);
	}
	
	public String getStartNode() {
		return startNode;
	}
	
	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}
	
	public String getEndNode() {
		return endNode;
	}
	
	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}

	public int getMaxSizeY() {
		return maxSizeY;
	}

	public void setMaxSizeY(int maxSizeY) {
		this.maxSizeY = maxSizeY;
	}

}
