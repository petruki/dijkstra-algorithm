package com.github.petruki.app;

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
import com.github.petruki.model.DensityMatrix;
import com.github.petruki.model.DijkstraResult;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Simple app that displays the Density Matrix simulation using the Dijkstra algorithm
 * 
 * @author Roger Floriano (petruki)
 */
public class DensityMatrixApp extends JFrame {

	public static final String ERROR_TITLE = "Error";

	private JButton btnGenerate;
	private JButton btnExecute;
	private JSpinner spinnerRows;
	private JSpinner spinnerCols;
	private JCheckBox chkDiagonalTrip;
	private JProgressBar progressBar;
	private final JPanel contentPane;
	private JTable matrix;

	private transient MatrixRender matrixRender;
	private transient DensityMatrix densityMatrix;
	private transient Dijkstra dijkstra;

	private MatrixSettings matrixSettings;
	private Options optionSelected = Options.UNSELECTED;
	private JTextField txtTotalCost;
	
	private boolean capturing = false;
	
	public DensityMatrixApp() {
		setTitle("Density Matrix - Dijkstra Pathfinder v1.1.0");
		setIconImage(LoadImage.load("end.png"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 680, 600);
		
		buildMenu();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		buildView();
	}
	
	private void buildMenu() {
		final var menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		final var mnNewMenu = new JMenu("Options");
		menuBar.add(mnNewMenu);
		
		final var menuNew = new JMenuItem("New");
		menuNew.addActionListener(e -> initializeMatrix(true, false));
		mnNewMenu.add(menuNew);
		
		final var menuNewInverted = new JMenuItem("New Inverted");
		menuNewInverted.addActionListener(e -> initializeMatrix(true, true));
		mnNewMenu.add(menuNewInverted);
		
		mnNewMenu.add(new JSeparator());
		
		final var menuOpen = new JMenuItem("Open");
		menuOpen.addActionListener(e -> {
			matrixSettings = FileManagement.openReadWork();
			if (matrixSettings != null) {
				initializeMatrix(false, false);
				onGenerate();
			}
		});
		mnNewMenu.add(menuOpen);
		
		final var menuSave = new JMenuItem("Save");
		menuSave.addActionListener(e -> FileManagement.openSaveWork(matrixSettings));
		mnNewMenu.add(menuSave);
		
		mnNewMenu.add(new JSeparator());
		
		final var menuImportClipboard = new JMenuItem("Import Clipboard");
		mnNewMenu.add(menuImportClipboard);
		
		menuImportClipboard.addActionListener(this::actionPerformed);
		
		final var menuImportImage = new JMenuItem("Import Image");
		menuImportImage.addActionListener(e -> onImportImage(FileManagement.openImage()));
		mnNewMenu.add(menuImportImage);
		
		mnNewMenu.add(new JSeparator());
		
		final var menuImportImageBuffer = new JMenuItem("Start Capturing");
		menuImportImageBuffer.addActionListener(e -> onImportImageBuffer());
		mnNewMenu.add(menuImportImageBuffer);
	}
	
	private void buildView() {
		// Build bottom bar
		final var panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
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
		
		spinnerRows = new JSpinner();
		spinnerRows.setValue(30);
		spinnerRows.setToolTipText("Rows");
		panel.add(spinnerRows);
		
		spinnerCols = new JSpinner();
		spinnerCols.setValue(30);
		spinnerCols.setToolTipText("Cols");
		panel.add(spinnerCols);
		
		chkDiagonalTrip = new JCheckBox("Diagonal Trip");
		chkDiagonalTrip.setSelected(true);
		chkDiagonalTrip.addActionListener(e -> btnExecute.setEnabled(false));
		panel.add(chkDiagonalTrip);
		
		// Build sidebar
		var toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		contentPane.add(toolBar, BorderLayout.WEST);
		
		final var btnSelectStartNode = new JButton();
		btnSelectStartNode.setIcon(new ImageIcon(Objects.requireNonNull(LoadImage.load("start.png"))));

		final var btnSelectEndNode = new JButton();
		btnSelectEndNode.setIcon(new ImageIcon(Objects.requireNonNull(LoadImage.load("end.png"))));

		final var btnSelectIgnoreNode = new JButton();
		btnSelectIgnoreNode.setIcon(new ImageIcon(Objects.requireNonNull(LoadImage.load("ignore.png"))));

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
		initializeMatrix(true, false);
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
						optionSelected.equals(Options.SELECT_IGNORE)) {
					btnExecute.setEnabled(false);
				}
				
				matrixRender.setSelectedOption(optionSelected);
			}

			@Override
			public void doDoubleClick(MatrixVertex matrixVertex) {
				if (btnExecute.isEnabled()) {
					onExecute();
				}
			}
		});
	}
	
	private void centerUI() {
		var dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);
	}
	
	private void initializeMatrix(boolean newWork, boolean inverted) {
		EventQueue.invokeLater(() -> {
			// configure matrix
			densityMatrix = null;

			if (newWork) {
				matrixSettings = new MatrixSettings(
						Integer.parseInt(spinnerRows.getValue().toString()),
						Integer.parseInt(spinnerCols.getValue().toString()),
						chkDiagonalTrip.isSelected() ? 1.2f : -1);

				matrixSettings.resetMatrix();
			} else {
				spinnerRows.setValue(matrixSettings.getSizeX());
				spinnerCols.setValue(matrixSettings.getSizeY());
				chkDiagonalTrip.setSelected(matrixSettings.getDiagonalTrip() != -1);
			}

			// initialize view
			matrix.setModel(new MatrixModel().getTableModel(createMatrixIds(inverted)));
			matrixRender = new MatrixRender(matrix, matrixSettings);

			// reset view
			btnGenerate.setEnabled(true);
			txtTotalCost.setText("");
		});
	}
	
	private String[][] createMatrixIds(boolean inverted) {
		var nodeId = 0;
		var matrixIds = new String[matrixSettings.getSizeX()][matrixSettings.getSizeY()];
		for (int i = 0; i < matrixSettings.getSizeX(); i++) {
			for (int j = 0; j < matrixSettings.getSizeY(); j++, nodeId++) {
				if (inverted) {
					matrixSettings.getIgnoredNodes().add(String.valueOf(nodeId));
				}
				matrixIds[i][j] = String.valueOf(nodeId);
			}
		}
		
		return matrixIds;
	}
	
	private void updateMatrixSettings() {
		matrixSettings.setDiagonalTrip(chkDiagonalTrip.isSelected() ? 1.2f : -1);
		matrixSettings.setSizeX(Integer.parseInt(spinnerRows.getValue().toString()));
		matrixSettings.setSizeY(Integer.parseInt(spinnerCols.getValue().toString()));
	}
	
	private void onGenerate() {
		if (capturing) {
			btnGenerate.setText("Generate");
			capturing = false;
			return;
		}
		
		progressBar.setVisible(true);
		txtTotalCost.setVisible(false);
		
		EventQueue.invokeLater(() -> {
			try {
				updateMatrixSettings();

				if (densityMatrix == null) {
					densityMatrix = DijkstraUtils.generateDensityMatrix(
							createMatrixIds(false), 1f, matrixSettings.getDiagonalTrip());
				} else {
					densityMatrix = DijkstraUtils.generateDensityMatrix(
							densityMatrix.getMatrix(), 1f, matrixSettings.getDiagonalTrip());

					spinnerRows.setValue(densityMatrix.getMatrix().length);
					spinnerCols.setValue(densityMatrix.getMaxSizeY());
				}

				dijkstra = new Dijkstra(densityMatrix.getVertices());
				dijkstra.generateTable(matrixSettings.getNodeStart(), matrixSettings.getIgnoredNodes());

				// reset view
				btnGenerate.setEnabled(true);
				btnExecute.setEnabled(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(DensityMatrixApp.this, e.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			} finally {
				progressBar.setVisible(false);
				txtTotalCost.setVisible(true);
				txtTotalCost.setText("Matrix generated");
			}
		});
	}
	
	private void onExecute() {
		EventQueue.invokeLater(() -> {
			DijkstraResult result;
			try {
				result = dijkstra.getShortestPath(matrixSettings.getNodeEnd());

				matrixSettings.updatePath(result.getPath());
				txtTotalCost.setText(result.getResult());
				matrix.repaint();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(DensityMatrixApp.this, e.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			}
		});
	}
	
	private void onImportClipboard(String input) {
    	if (input == null || input.isEmpty()) {
			return;
		}
    	
		// compile text input
		final var inputArr = input.split("\n");
		densityMatrix = DijkstraUtils.generateDensityMatrix(inputArr, 1f, matrixSettings.getDiagonalTrip());
		
		matrixSettings.resetMatrix();
		matrixSettings.setNodeStart(densityMatrix.getStartNode());
		matrixSettings.setNodeEnd(densityMatrix.getEndNode());
		matrixSettings.setIgnoredNodes(densityMatrix.getIgnored());
		
		// initialize view
		matrix.setModel(new MatrixModel().getTableModel(densityMatrix.getMatrix()));
		matrixRender = new MatrixRender(matrix, matrixSettings);
	}
	
	private void onImportImage(File input) {
    	if (input == null) {
			return;
		}
    	
    	try {
    		var thresholdLevel = JOptionPane.showInputDialog("Detail level (0-255)", "190");
    		if (thresholdLevel == null || thresholdLevel.isEmpty()) {
				thresholdLevel = "190";
			}
    		
    		// generate density matrix from image
			densityMatrix = DijkstraUtils.generateDensityMatrix(
					input, 
					Integer.parseInt(spinnerCols.getValue().toString()),
					Integer.parseInt(spinnerRows.getValue().toString()),
					Integer.parseInt(thresholdLevel), 1f, matrixSettings.getDiagonalTrip());
			
			matrixSettings.resetMatrix();
			matrixSettings.setIgnoredNodes(densityMatrix.getIgnored());
			
			// initialize view
			matrix.setModel(new MatrixModel().getTableModel(densityMatrix.getMatrix()));
			matrixRender = new MatrixRender(matrix, matrixSettings);
			matrix.repaint();
    	} catch (Exception e) {
    		JOptionPane.showMessageDialog(DensityMatrixApp.this, e.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void onImportImageBuffer() {
		JOptionPane.showConfirmDialog(this, 
				"Place your mouse cursor at the start and press ok.", "Find Start", 
				JOptionPane.OK_CANCEL_OPTION);
		final var xStartPos = MouseInfo.getPointerInfo().getLocation().x;
		final var yStartPos = MouseInfo.getPointerInfo().getLocation().y;
	    
	    JOptionPane.showConfirmDialog(this, 
				"Place your mouse cursor at the end and press ok.", "Find End", 
				JOptionPane.OK_CANCEL_OPTION);
	    final var xEndPos = MouseInfo.getPointerInfo().getLocation().x;
	    final var yEndPos = MouseInfo.getPointerInfo().getLocation().y;
	    
	    var thresholdLevel = Integer.parseInt(JOptionPane.showInputDialog("Detail level (0-255)", 190));
		
    	try {
    		var robot = new Robot();
    	    var width = (xEndPos - xStartPos);
			var height = (yEndPos - yStartPos);
    	    var area = new Rectangle(xStartPos, yStartPos, width, height);
    	    
    	    final int sampleCols = Integer.parseInt(spinnerCols.getValue().toString());
    	    final int sampleRows = Integer.parseInt(spinnerRows.getValue().toString());
 
    	    new Thread(() -> {
				capturing = true;
				btnGenerate.setText("Stop Capture");

				matrixRender = new MatrixRender(matrix, matrixSettings);

				while (capturing) {
					try {
						densityMatrix = DijkstraUtils.generateDensityMatrix(
								robot.createScreenCapture(area),
								sampleCols,
								sampleRows, thresholdLevel, 1f, -1f);

						matrixSettings.setIgnoredNodes(densityMatrix.getIgnored());
						matrixRender.setMatrixSettings(matrixSettings);
						matrix.repaint();
						Thread.sleep(20);
					} catch (Exception ignored) {
						Thread.currentThread().interrupt();
					}
				}
			}).start();

    	} catch (Exception e) {
    		JOptionPane.showMessageDialog(DensityMatrixApp.this, e.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}

	private void actionPerformed(ActionEvent e) {
		try {
			onImportClipboard((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
		} catch (HeadlessException | UnsupportedFlavorException | IOException ex) {
			JOptionPane.showMessageDialog(DensityMatrixApp.this, ex.getMessage(), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
				new DensityMatrixApp().setVisible(true);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		});
	}

}
