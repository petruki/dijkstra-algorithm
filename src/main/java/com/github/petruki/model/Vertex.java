package com.github.petruki.model;

public class Vertex {
	
	private String node1;
	private String node2;
	private int distance;
	
	public Vertex(String node1, String node2, int distance) {
		this.node1 = node1;
		this.node2 = node2;
		this.distance = distance;
	}
	
	public Vertex(String node1, String node2) {
		this.node1 = node1;
		this.node2 = node2;
		this.distance = Integer.MAX_VALUE;
	}
	
	public Vertex(String neighbour, int distance) {
		this.node2 = neighbour;
		this.distance = distance;
	}

	public String getNode1() {
		return node1;
	}

	public void setNode1(String node1) {
		this.node1 = node1;
	}

	public String getNode2() {
		return node2;
	}
	
	public String getNeighbour() {
		return getNode2();
	}

	public void setNode2(String node2) {
		this.node2 = node2;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node1 == null) ? 0 : node1.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (node1 == null) {
			if (other.node1 != null)
				return false;
		} else if (!node1.equals(other.node1))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Vertex [node1=" + node1 + ", node2=" + node2 + ", distance=" + distance + "]";
	}

}
