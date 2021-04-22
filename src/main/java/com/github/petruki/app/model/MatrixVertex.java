package com.github.petruki.app.model;

public class MatrixVertex {
	
	private String id;
	private int row;
	private int column;
	
	public MatrixVertex(String id, int row, int column) {
		this.id = id;
		this.row = row;
		this.column = column;
	}

	public String getId() {
		return id;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "MatrixVertex [id=" + id + ", row=" + row + ", column=" + column + "]";
	}	

}
