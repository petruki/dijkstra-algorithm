package com.github.petruki.ui.matrix;

import javax.swing.table.DefaultTableModel;

public class MatrixModel {
	
	private String[][] matrix;
	
	@SuppressWarnings("serial")
	public DefaultTableModel getTableModel(String[][] matrix) {
		this.matrix = matrix;
		return new DefaultTableModel(this.matrix, new String[this.matrix.length]) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}
	
	public String[][] getMatrix() {
		return matrix;
	}

}
