package com.github.petruki.ui.matrix;

import javax.swing.table.DefaultTableModel;

/**
 * @author Roger Floriano (petruki)
 */
public class MatrixModel {
	
	private DefaultTableModel tableModel;
	private String[][] matrix;
	
	@SuppressWarnings("serial")
	public DefaultTableModel getTableModel(String[][] matrix) {
		this.matrix = matrix;
		tableModel = new DefaultTableModel(this.matrix, new String[this.matrix.length]) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return tableModel;
	}
	
	public String[][] getMatrix() {
		return matrix;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

}
