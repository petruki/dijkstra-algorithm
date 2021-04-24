package com.github.petruki.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MatrixSettings implements Serializable {
	
	private static final long serialVersionUID = -422134078142927041L;
	
	private List<String> ignoredNodes = new ArrayList<>();
	private List<String> path = new ArrayList<>();
	private String nodeStart;
	private String nodeEnd;
	private float diagonalTrip;
	private int sizeX;
	private int sizeY;
	
	public MatrixSettings(int sizeX, int sizeY, float diagonalTrip) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.diagonalTrip = diagonalTrip;
	}
	
	public void resetMatrix() {
		this.ignoredNodes.clear();
		this.path.clear();
		this.nodeStart = null;
		this.nodeEnd = null;
	}
	
	public void updatePath(List<String> path) {
		path.remove(0);
		path.remove(path.size() - 1);
		
		this.path.clear();
		this.path.addAll(path);
	}
	
	public void updateIgnoreNodes(String nodeId) {
		if (ignoredNodes.contains(nodeId)) {
			ignoredNodes.remove(nodeId);
		} else {
			ignoredNodes.add(nodeId);
		}
	}
	
	public List<String> getIgnoredNodes() {
		return ignoredNodes;
	}
	
	public void setIgnoredNodes(List<String> ignoredNodes) {
		this.ignoredNodes = ignoredNodes;
	}
	
	public List<String> getPath() {
		return path;
	}
	
	public void setPath(List<String> path) {
		this.path = path;
	}
	
	public String getNodeStart() {
		return nodeStart;
	}
	
	public void setNodeStart(String nodeStart) {
		this.nodeStart = nodeStart;
	}
	
	public String getNodeEnd() {
		return nodeEnd;
	}
	
	public void setNodeEnd(String nodeEnd) {
		this.nodeEnd = nodeEnd;
	}

	public float getDiagonalTrip() {
		return diagonalTrip;
	}

	public void setDiagonalTrip(float diagonalTrip) {
		this.diagonalTrip = diagonalTrip;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

}
