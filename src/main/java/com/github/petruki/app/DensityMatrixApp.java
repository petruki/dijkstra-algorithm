package com.github.petruki.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import com.github.petruki.app.matrix.MatrixEventHandler;
import com.github.petruki.app.matrix.MatrixModel;
import com.github.petruki.app.matrix.MatrixRender;
import com.github.petruki.app.model.MatrixSettings;
import com.github.petruki.app.model.MatrixVertex;
import com.github.petruki.app.model.Options;
import com.github.petruki.app.utils.FileManagement;
import com.github.petruki.app.utils.LoadImage;
import com.github.petruki.model.DijkstraResult;
import com.github.petruki.model.Vertex;

import javax.swing.JTextField;
import javax.swing.JProgressBar;

/**
 * Simple app that displays the Density Matrix simulation using the Dijkstra algorithm
 * 
 * @author Roger Floriano (petruki)
 */
@SuppressWarnings("serial")
public class DensityMatrixApp extends JFrame {

	private JButton btnGenerate;
	private JButton btnExecute;
	private JSpinner matrixSize;
	private JCheckBox chkDiagonalTrip;
	private JProgressBar progressBar;
	private JPanel contentPane;
	private JTable matrix;
	private MatrixRender matrixRender;
	
	private List<Vertex> densityMatrix;
	private MatrixSettings matrixSettings;
	
	private Dijkstra dijkstra;
	private Options optionSelected = Options.UNSELECTED;
	private JTextField txtTotalCost;
	
	public DensityMatrixApp() {
		setTitle("Density Matrix Simulator - Dijkstra Pathfinder");
		setIconImage(LoadImage.load("end.png"));
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
		final JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		final JButton btnNew = new JButton("New");
		btnNew.addActionListener(e -> initializeMatrix(true));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(btnNew);
		
		final JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(e -> {
			matrixSettings = FileManagement.openReadWork();
			if (matrixSettings != null)
				initializeMatrix(false);
		});
		panel.add(btnOpen);
		
		final JButton btnSave = new JButton("Save");
		btnSave.addActionListener(e -> FileManagement.openSaveWork(matrixSettings));
		panel.add(btnSave);
		
		btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(e -> onGenerate());
		panel.add(btnGenerate);
		
		btnExecute = new JButton("Execute");
		btnExecute.setEnabled(false);
		btnExecute.addActionListener(e -> onExecute());
		panel.add(btnExecute);
		
		progressBar = new JProgressBar();
		progressBar.setVisible(false);
		progressBar.setIndeterminate(true);
		panel.add(progressBar);
		
		txtTotalCost = new JTextField();
		txtTotalCost.setEditable(false);
		panel.add(txtTotalCost);
		txtTotalCost.setColumns(10);
		
		matrixSize = new JSpinner();
		matrixSize.setValue(30);
		panel.add(matrixSize);
		
		chkDiagonalTrip = new JCheckBox("Diagonal Trip");
		chkDiagonalTrip.setSelected(true);
		chkDiagonalTrip.addActionListener(e -> btnExecute.setEnabled(false));
		panel.add(chkDiagonalTrip);
		
		// Build side bar
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		contentPane.add(toolBar, BorderLayout.WEST);
		
		final JButton btnSelectStartNode = new JButton();
		btnSelectStartNode.setIcon(new ImageIcon(LoadImage.load("start.png")));

		final JButton btnSelectEndNode = new JButton();
		btnSelectEndNode.setIcon(new ImageIcon(LoadImage.load("end.png")));

		final JButton btnSelectIgnoreNode = new JButton();
		btnSelectIgnoreNode.setIcon(new ImageIcon(LoadImage.load("ignore.png")));

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
		matrix.setTableHeader(null);
		matrix.setShowGrid(true);

		contentPane.add(new JScrollPane(matrix), BorderLayout.CENTER);
		
		centerUI();
		initializeMatrix(true);
		initMatrixEventHandler();
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
	
	private void initializeMatrix(boolean newWork) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// configure matrix
				if (newWork) {
					matrixSettings = new MatrixSettings(
							Integer.valueOf(matrixSize.getValue().toString()),
							chkDiagonalTrip.isSelected() ? 1.2f : -1);
					
					matrixSettings.resetMatrix();
				} else {
					matrixSize.setValue(matrixSettings.getSize());
					chkDiagonalTrip.setSelected(matrixSettings.getDiagonalTrip() != -1);
				}
				
				// initialize view
				matrix.setModel(new MatrixModel().getTableModel(createMatrixIds()));
				matrixRender = new MatrixRender(matrix, matrixSettings);
				
				// reset view
				btnGenerate.setEnabled(true);
				txtTotalCost.setText("");
			}
		});
	}
	
	private String[][] createMatrixIds() {
		matrixSettings.setDiagonalTrip(chkDiagonalTrip.isSelected() ? 1.2f : -1);
		matrixSettings.setSize(Integer.valueOf(matrixSize.getValue().toString()));
		
		Integer nodeId = 0;
		String[][] matrixIds = new String[matrixSettings.getSize()][matrixSettings.getSize()];
		for (int i = 0; i < matrixIds.length; i++) {
			for (int j = 0; j < matrixIds[i].length; j++) {
				matrixIds[i][j] = String.valueOf(nodeId++);
			}
		}
		
		return matrixIds;
	}
	
	private void onGenerate() {
		progressBar.setVisible(true);
		txtTotalCost.setVisible(false);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final String[][] matrixIds = createMatrixIds();
					densityMatrix = DijkstraUtils.generateDensityMatrix(
							matrixIds, 1f, matrixSettings.getDiagonalTrip());
					
					dijkstra = new Dijkstra(densityMatrix);
					dijkstra.generateTable(matrixSettings.getNodeStart(), matrixSettings.getIgnoredNodes());
					
					// reset view
					btnGenerate.setEnabled(true);
					btnExecute.setEnabled(true);
					txtTotalCost.setText("");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(
							DensityMatrixApp.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} finally {
					progressBar.setVisible(false);
					txtTotalCost.setVisible(true);
					txtTotalCost.setText("Matrix generated");
				}
			}
		});
	}
	
	private void onExecute() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				DijkstraResult result;
				try {
					result = dijkstra.getShortestPath(matrixSettings.getNodeEnd());
					
					matrixSettings.updatePath(result.getPath());
					txtTotalCost.setText(result.getResult());
					matrix.repaint();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(
							DensityMatrixApp.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
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
					new DensityMatrixApp().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
