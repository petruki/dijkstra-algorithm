package com.github.petruki.app.matrix;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import com.github.petruki.app.model.MatrixVertex;

public abstract class MatrixEventHandler extends MouseAdapter {
	
	private final JTable table;
	
	protected MatrixEventHandler(final JTable table) {
		this.table = table;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		final var id = table.getModel().getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
		doRelease(new MatrixVertex(id, table.getSelectedRow(), table.getSelectedColumn()));
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		final var id = table.getModel().getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
		doPress(new MatrixVertex(id, table.getSelectedRow(), table.getSelectedColumn()));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		final var id = table.getModel().getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
		
		if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
			doDoubleClick(new MatrixVertex(id, table.getSelectedRow(), table.getSelectedColumn()));
		}
	}
	
	public abstract void doRelease(MatrixVertex matrixVertex);
	
	public abstract void doPress(MatrixVertex matrixVertex);
	
	public abstract void doDoubleClick(MatrixVertex matrixVertex);

}
