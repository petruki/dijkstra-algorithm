package com.github.petruki.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import com.github.petruki.Dijkstra;
import com.github.petruki.DijkstraUtils;
import com.github.petruki.model.DijkstraResult;
import com.github.petruki.model.Vertex;
import com.github.petruki.ui.matrix.MatrixEventHandler;
import com.github.petruki.ui.matrix.MatrixModel;
import com.github.petruki.ui.matrix.MatrixRender;
import com.github.petruki.ui.model.MatrixVertex;
import com.github.petruki.ui.model.Options;

/**
 * Simple app that displays the Density Matrix mode using Dijkstra
 * 
 * @author Roger Floriano (petruki)
 */
@SuppressWarnings("serial")
public class DensityMatrixSimulatorApp extends JFrame {

	private JButton btnGenerate;
	private JButton btnExecute;
	private JSpinner matrixSize;
	private JCheckBox chkDiagonalTrip;
	private JPanel contentPane;
	private JTable matrix;
	private MatrixModel matrixModel;
	private MatrixRender matrixRender;
	
	private List<Vertex> densityMatrix;
	private List<String> ignoredNodes = new ArrayList<>();
	private List<String> path = new ArrayList<>();
	
	private Dijkstra dijkstra;
	private Options optionSelected = Options.UNSELECTED;
	
	public DensityMatrixSimulatorApp() {
		setTitle("Density Matrix - Dijkstra Pathfinder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 678, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		buildView();
	}
	
	private void buildView() {
		// Build bottom bar
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(e -> initializeMatrix());
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(btnNew);
		
		JButton btnOpen = new JButton("Open");
		btnOpen.setEnabled(false);
		panel.add(btnOpen);
		
		JButton btnSave = new JButton("Save");
		btnSave.setEnabled(false);
		panel.add(btnSave);
		
		btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(e -> onGenerate());
		panel.add(btnGenerate);
		
		btnExecute = new JButton("Execute");
		btnExecute.setEnabled(false);
		btnExecute.addActionListener(e -> onExecute());
		panel.add(btnExecute);
		
		matrixSize = new JSpinner();
		matrixSize.setValue(30);
		panel.add(matrixSize);
		
		chkDiagonalTrip = new JCheckBox("Diagonal Trip");
		chkDiagonalTrip.setSelected(true);
		chkDiagonalTrip.addActionListener(e -> {
			btnGenerate.setEnabled(false);
			btnExecute.setEnabled(false);
		});
		panel.add(chkDiagonalTrip);
		
		// Build side bar
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		contentPane.add(toolBar, BorderLayout.WEST);
		
		JButton btnSelectStartNode = new JButton();
		btnSelectStartNode.setIcon(new ImageIcon(
				DensityMatrixSimulatorApp.class.getClassLoader().getResource("start.png")));

		JButton btnSelectEndNode = new JButton();
		btnSelectEndNode.setIcon(new ImageIcon(
				DensityMatrixSimulatorApp.class.getClassLoader().getResource("end.png")));

		JButton btnSelectIgnoreNode = new JButton();
		btnSelectIgnoreNode.setIcon(new ImageIcon(
				DensityMatrixSimulatorApp.class.getClassLoader().getResource("ignore.png")));

		btnSelectStartNode.setToolTipText("Select the starting node");
		btnSelectStartNode.setFocusable(false);
		btnSelectStartNode.addActionListener(e -> {
			optionSelected = Options.SELECT_START;
			btnSelectStartNode.setEnabled(false);
			btnSelectIgnoreNode.setEnabled(true);
			btnSelectEndNode.setEnabled(true);
		});
		toolBar.add(btnSelectStartNode);
		
		btnSelectEndNode.setToolTipText("Select the ending node");
		btnSelectEndNode.setFocusable(false);
		btnSelectEndNode.addActionListener(e -> {
			optionSelected = Options.SELECT_END;
			btnSelectStartNode.setEnabled(true);
			btnSelectIgnoreNode.setEnabled(true);
			btnSelectEndNode.setEnabled(false);
		});
		toolBar.add(btnSelectEndNode);
		
		btnSelectIgnoreNode.setToolTipText("Select ignore nodes");
		btnSelectIgnoreNode.setFocusable(false);
		btnSelectIgnoreNode.addActionListener(e -> {
			optionSelected = Options.SELECT_IGNORE;
			btnSelectStartNode.setEnabled(true);
			btnSelectIgnoreNode.setEnabled(false);
			btnSelectEndNode.setEnabled(true);
		});
		toolBar.add(btnSelectIgnoreNode);
		
		// Build center view
		matrix = new JTable();
		matrix.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		matrix.setBackground(Color.GRAY);
		matrix.setGridColor(Color.GRAY);
		matrix.setShowGrid(true);
		
		contentPane.add(matrix, BorderLayout.CENTER);
		
		centerUI();
		initializeMatrix();
		initMatrixEventHandler();
	}
	
	private void initializeMatrix() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// configure matrix
				int selectedMatrixSize = Integer.valueOf(matrixSize.getValue().toString());
				float diagonalTrip = chkDiagonalTrip.isSelected() ? 1.2f : -1;
				
				// initialize node ids
				Integer nodeId = 0;
				String[][] matrixIds = new String[selectedMatrixSize][selectedMatrixSize];
				for (int i = 0; i < matrixIds.length; i++) {
					for (int j = 0; j < matrixIds[i].length; j++) {
						matrixIds[i][j] = String.valueOf(nodeId++);
					}
				}
				
				densityMatrix = DijkstraUtils.generateDensityMatrix(
						matrixIds, 1f, diagonalTrip);
				
				// initialize view
				matrixModel = new MatrixModel();
				matrix.setModel(matrixModel.getTableModel(matrixIds));
				matrixRender = new MatrixRender(matrix, ignoredNodes, path);
				
				// reset view
				btnGenerate.setEnabled(true);
				ignoredNodes.clear();
				path.clear();
			}
		});
	}
	
	private void onGenerate() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dijkstra = new Dijkstra(densityMatrix);
					dijkstra.generateTable(matrixRender.getNodeStart(), ignoredNodes);
					btnExecute.setEnabled(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(
							DensityMatrixSimulatorApp.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	private void onExecute() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				DijkstraResult result;
				try {
					path.clear();
					
					result = dijkstra.getShortestPath(matrixRender.getNodeEnd());
					path.addAll(result.getPath());
					matrix.repaint();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(
							DensityMatrixSimulatorApp.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	private void initMatrixEventHandler() {
		matrix.addMouseListener(new MatrixEventHandler(matrix) {

			@Override
			public void doRelease(MatrixVertex matrixVertex) {
				matrixRender.setSelectedOption(Options.UNSELECTED);
				matrix.repaint();
			}

			@Override
			public void doPress(MatrixVertex matrixVertex) {
				if (optionSelected.equals(Options.SELECT_START) ||
						optionSelected.equals(Options.SELECT_IGNORE))
					btnExecute.setEnabled(false);
				
				matrixRender.setSelectedOption(optionSelected);
			}

			@Override
			public void doDoubleClick(MatrixVertex matrixVertex) {
				if (btnExecute.isEnabled())
					onExecute();
			}
		});
	}
	
	private void centerUI() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
					new DensityMatrixSimulatorApp().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
