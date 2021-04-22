package com.github.petruki.ui.matrix;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.github.petruki.ui.model.Options;

public class MatrixRender implements TableCellRenderer {

	private Options selectedOption;
	private final List<String> ignoredNodes;
	private final List<String> path;
	private String nodeStart;
	private String nodeEnd;

	public MatrixRender(JTable table, List<String> ignoredNodes, List<String> path) {
		this.path = path;
		this.ignoredNodes = ignoredNodes;
		setupTable(table);
	}

	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JPanel cell = new JPanel();
		cell.setBackground(Color.WHITE);
		
		if (selectedOption != null && isSelected && hasFocus) {
			updateSelectionRender(value.toString());
		}
		
		if (ignoredNodes.contains(value.toString()))
			cell.setBackground(Color.BLACK);
		
		else if (path.contains(value.toString()))
			cell.setBackground(Color.GREEN);
		
		else if (value.toString().equals(nodeStart)) {
			cell.setBackground(Color.LIGHT_GRAY);
			
		} else if (value.toString().equals(nodeEnd))
			cell.setBackground(Color.GRAY);

		return cell;
	}

	private void updateSelectionRender(String nodeId) {
		switch (selectedOption) {
		case SELECT_IGNORE:
			if (ignoredNodes.contains(nodeId)) {
				ignoredNodes.remove(nodeId);
			} else {
				ignoredNodes.add(nodeId);
			}
			break;
		case SELECT_START:
			nodeStart = nodeId;
			selectedOption = Options.UNSELECTED;
			break;
		case SELECT_END:
			nodeEnd = nodeId;
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

	public void setSelectedOption(Options selectedOption) {
		this.selectedOption = selectedOption;
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

}
