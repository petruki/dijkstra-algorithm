package com.github.petruki.app.matrix;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.github.petruki.app.model.MatrixSettings;
import com.github.petruki.app.model.Options;

public class MatrixRender implements TableCellRenderer {

	private Options selectedOption;
	private MatrixSettings matrixSettings;

	public MatrixRender(JTable table, MatrixSettings matrixSettings) {
		this.matrixSettings = matrixSettings;
		setupTable(table);
	}
	
	public MatrixRender(JTable table) {
		setupTable(table);
	}

	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		var cell = new JPanel();
		cell.setBackground(Color.WHITE);

		if (selectedOption != null && isSelected && hasFocus) {
			updateSelectionRender(value.toString());
		}

		setBackgroundColor(value, cell);
		return cell;
	}

	private void setBackgroundColor(Object value, JPanel cell) {
		if (matrixSettings.getIgnoredNodes().contains(value.toString())) {
			cell.setBackground(Color.BLACK);
		} else if (matrixSettings.getPath().contains(value.toString())) {
			cell.setBackground(Color.GREEN);
		} else if (value.toString().equals(matrixSettings.getNodeStart())) {
			cell.setBackground(Color.LIGHT_GRAY);
		} else if (value.toString().equals(matrixSettings.getNodeEnd())) {
			cell.setBackground(Color.GRAY);
		}
	}

	private void updateSelectionRender(String nodeId) {
		switch (selectedOption) {
		case SELECT_IGNORE:
			matrixSettings.updateIgnoreNodes(nodeId);
			break;
		case SELECT_START:
			matrixSettings.setNodeStart(nodeId);
			selectedOption = Options.UNSELECTED;
			break;
		case SELECT_END:
			matrixSettings.setNodeEnd(nodeId);
			selectedOption = Options.UNSELECTED;
			break;
		default:
			break;
		}
	}

	public void setupTable(JTable table) {
		String columnName;
		for (int i = 0; i < table.getColumnCount(); i++) {
			columnName = table.getColumnName(i);
			table.getColumn(columnName).setCellRenderer(this);
		}
	}

	public void setMatrixSettings(MatrixSettings matrixSettings) {
		this.matrixSettings = matrixSettings;
	}

	public void setSelectedOption(Options selectedOption) {
		this.selectedOption = selectedOption;
	}

}
