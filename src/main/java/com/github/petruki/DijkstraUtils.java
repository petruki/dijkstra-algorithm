package com.github.petruki;

import static com.github.petruki.model.Vertex.get;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.petruki.model.DensityMatrix;
import com.github.petruki.model.DijkstraResult;

public class DijkstraUtils {
	
	/**
	 * Generates a Density Matrix baxed on sizeX (rows) and sizeY (columns).
	 * 
	 * @param sizeX rows
	 * @param sizeY columns
	 * @param linearCost vertical/horizontal cost
	 * @param diagCost diagonal cost. Negative numbers will make the connection being ignored
	 */
	public static DensityMatrix generateDensityMatrix(
			int sizeX, int sizeY, float linearCost, float diagCost) {
		
		Integer nodeId = 0;
		String[][] matrix = new String[sizeX][sizeY];
		
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				matrix[i][j] = String.valueOf(nodeId++);
			}
		}
		
		return generateDensityMatrix(matrix, linearCost, diagCost);
	}
	
	/**
	 * Generates a Density Matrix baxed on an array of plain text input.<br>
	 * 
	 * - Spaces are travelable nodes.<br>
	 * - 'x' are ignored nodes.<br>
	 * - 's' is the starting node (optional).<br>
	 * - 'e' is the ending node (optional).
	 * 
	 * @param input Plain text input
	 * @param linearCost vertical/horizontal cost
	 * @param diagCost diagonal cost. Negative numbers will make the connection being ignored
	 */
	public static DensityMatrix generateDensityMatrix(
			String[] input, float linearCost, float diagCost) {
		
		int maxSizeY = 0;
		for (String s : input)
			if (s.length() > 0) maxSizeY = s.length();
		
		Integer nodeId = 0;
		String[][] matrix = new String[input.length][maxSizeY];
		String startNode = null, endNode = null;
		List<String> ignore = new ArrayList<>();
		
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length(); j++, nodeId++) {
				matrix[i][j] = nodeId.toString();
				
				if (input[i].charAt(j) == '+')
					ignore.add(nodeId.toString());
				else if (input[i].charAt(j) == 's')
					startNode = nodeId.toString();
				else if (input[i].charAt(j) == 'e') {
					endNode = nodeId.toString();
				}
			}
		}
		
		DensityMatrix densityMatrix = generateDensityMatrix(matrix, linearCost, diagCost);
		densityMatrix.setIgnored(ignore);
		densityMatrix.setStartNode(startNode);
		densityMatrix.setEndNode(endNode);
		densityMatrix.setMaxSizeY(maxSizeY);
		return densityMatrix;
	}
	
	public static DensityMatrix generateDensityMatrix(
			String[][] matrix, float linearCost, float diagCost) {
		
		final DensityMatrix densityMatrix = new DensityMatrix();
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[row].length; col++) {
				if (row > 0) {
					densityMatrix.addVertex(
							get(matrix[row][col], matrix[row-1][col], linearCost));
					
					if (col + 1 < matrix[row].length && diagCost > 0)
						densityMatrix.addVertex(
								get(matrix[row][col], matrix[row-1][col+1], diagCost));	
				}
				
				if (col + 1 < matrix[row].length) {
					densityMatrix.addVertex(
							get(matrix[row][col], matrix[row][col+1], linearCost));
				
					if (row + 1 < matrix.length && diagCost > 0)
						densityMatrix.addVertex(
								get(matrix[row][col], matrix[row+1][col+1], diagCost));
				}
				
				
				if (row + 1 < matrix.length)
					densityMatrix.addVertex(
							get(matrix[row][col], matrix[row+1][col], linearCost));
			}
		}
		
		densityMatrix.setMatrix(matrix);
		densityMatrix.setMaxSizeY(matrix[0].length);
		return densityMatrix;
	}

	
	public static void printResultDensityMatrix(
			DijkstraResult result, DensityMatrix densityMatrix, boolean mask) {
		
		Integer nodeId = 0;
		String[][] matrix = densityMatrix.getMatrix();;
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[row].length; col++, nodeId++) {
				if (result.getDTable().getIgnored().contains(nodeId.toString())) {
					matrix[row][col] = "+";
				} else {
					matrix[row][col] = result.getPath().contains(nodeId.toString()) ? 
							String.format("[%s]", mask ? "O" : nodeId) : mask ? "   " : nodeId.toString();					
				}
			}
		}
		
		Arrays.stream(matrix).forEach(row -> {
			Arrays.stream(row).forEach(col -> System.out.printf(mask ? "%3s" : "%6s", col));
			System.out.println();
		});
	}

}
