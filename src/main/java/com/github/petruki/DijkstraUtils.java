package com.github.petruki;

import com.github.petruki.model.DensityMatrix;
import com.github.petruki.model.DijkstraResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import static com.github.petruki.model.Vertex.get;

public class DijkstraUtils {
	
	private static final String A = " ";
	private static final String X = "+";
	private static final String S = "s";
	private static final String E = "e";

	private DijkstraUtils() {
		// Utility class
	}
	
	/**
	 * Generates a Density Matrix based on sizeX (rows) and sizeY (columns).
	 * 
	 * @param sizeX rows
	 * @param sizeY columns
	 * @param linearCost vertical/horizontal cost
	 * @param diagCost diagonal cost. Negative numbers will make the connection being ignored
	 */
	public static DensityMatrix generateDensityMatrix(
			int sizeX, int sizeY, float linearCost, float diagCost) {
		
		var nodeId = 0;
		var matrix = new String[sizeX][sizeY];
		
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
		for (String s : input) {
			if (!s.isEmpty()) {
				maxSizeY = s.length();
			}
		}
		
		var nodeId = 0;
		var matrix = new String[input.length][maxSizeY];
		var ignore = new HashSet<String>();

		String startNode = null;
		String endNode = null;
		
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length(); j++, nodeId++) {
				matrix[i][j] = String.valueOf(nodeId);
				
				if (input[i].charAt(j) == X.charAt(0))
					ignore.add(String.valueOf(nodeId));
				else if (input[i].charAt(j) == S.charAt(0))
					startNode = String.valueOf(nodeId);
				else if (input[i].charAt(j) == E.charAt(0)) {
					endNode = String.valueOf(nodeId);
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
	
	/**
	 * Generates Density Matrix from image file source
	 */
	public static DensityMatrix generateDensityMatrix(
			File inputImage, int width, int height, int threshold,
			float linearCost, float diagCost) 
			throws Exception {
		
		BufferedImage originalImage = ImageIO.read(inputImage);
		return generateDensityMatrix(originalImage, width, height, threshold, linearCost, diagCost);
	}
	
	/**
	 * Generates Density Matrix from image buffer source
	 */
	public static DensityMatrix generateDensityMatrix(
			BufferedImage imageBuffer, int width, int height, int threshold,
			float linearCost, float diagCost) {
		
		var resizedImage = resizeImage(imageBuffer, width, height);

		Color color;
		final var row = new StringBuilder();
		final var input = new String[height];
		
		for (int h = 0; h < resizedImage.getHeight(); h++) {
			
			row.setLength(0);
			for (int w = 0; w < resizedImage.getWidth(); w++) {
				color = new Color(resizedImage.getRGB(w, h));
				if (color.getRed() > threshold) {
					row.append(A);			
				} else {
					row.append(X);
				}
			}
			
			input[h] = row.toString();
		}
		
		return generateDensityMatrix(input, linearCost, diagCost);
	}

	
	public static DensityMatrix generateDensityMatrix(
			String[][] matrix, float linearCost, float diagCost) {
		
		final var densityMatrix = new DensityMatrix();
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
		
		var nodeId = 0;
		var matrix = densityMatrix.getMatrix();
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[row].length; col++, nodeId++) {
				final var nodeIdStr = String.valueOf(nodeId);
				if (result.getDTable().getIgnored().contains(nodeIdStr)) {
					matrix[row][col] = X;
				} else {
					matrix[row][col] = result.getPath().contains(nodeIdStr) ?
							String.format("[%s]", mask ? "O" : nodeIdStr) : mask ? "   " : nodeIdStr;
				}
			}
		}
		
		Arrays.stream(matrix).forEach(row -> {
			Arrays.stream(row).forEach(col -> System.out.printf(mask ? "%3s" : "%6s", col));
			System.out.println();
		});
	}
	
	private static BufferedImage resizeImage(
			BufferedImage originalImage, int targetWidth, int targetHeight) {
	    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    return outputImage;
	}

}
