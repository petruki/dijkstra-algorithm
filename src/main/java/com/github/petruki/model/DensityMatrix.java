package com.github.petruki.model;

import java.util.ArrayList;
import java.util.List;

public class DensityMatrix {
	
	private List<Vertex> vertices = new ArrayList<>();
	private List<String> ignored = new ArrayList<>();
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
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	public void setVertices(List<Vertex> matrix) {
		this.vertices = matrix;
	}
	
	public String[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(String[][] matrix) {
		this.matrix = matrix;
	}

	public List<String> getIgnored() {
		return ignored;
	}
	
	public void setIgnored(List<String> ignored) {
		this.ignored = ignored;
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
