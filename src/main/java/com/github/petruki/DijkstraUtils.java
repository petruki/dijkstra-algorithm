package com.github.petruki;

import static com.github.petruki.model.Vertex.get;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.petruki.model.DijkstraResult;
import com.github.petruki.model.Vertex;

public class DijkstraUtils {
	
	public static List<Vertex> generateDensityMatrix(
			int size, float linearCost, float diagCost) {
		
		Integer nodeId = 0;
		String[][] matrix = new String[size][size];
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = String.valueOf(nodeId++);
			}
		}
		
		return generateDensityMatrix(matrix, linearCost, diagCost);
	}
	
	public static List<Vertex> generateDensityMatrix(
			String[][] matrix, float linearCost, float diagCost) {
		
		List<Vertex> vertices = new ArrayList<>();
		int size = matrix.length;
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (i > 0) {
					vertices.add(get(matrix[i][j], matrix[i-1][j], linearCost));
					
					if (j + 1 < size)
						vertices.add(get(matrix[i][j], matrix[i-1][j+1], diagCost));	
				}
				
				if (j + 1 < size) {
					vertices.add(get(matrix[i][j], matrix[i][j+1], linearCost));
				
					if (i + 1 < size)
						vertices.add(get(matrix[i][j], matrix[i+1][j+1], diagCost));
				}
				
				
				if (i + 1 < size)
					vertices.add(get(matrix[i][j], matrix[i+1][j], linearCost));
			}
		}
		
		return vertices;
	}
	
	public static void printResultDensityMatrix(DijkstraResult result, int size, boolean mask) {
		Integer nodeId = 0;
		String[][] matrix = new String[size][size];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++, nodeId++) {
				if (result.getDTable().getIgnored().contains(nodeId.toString())) {
					matrix[i][j] = "[X]";
				} else {
					matrix[i][j] = result.getPath().contains(nodeId.toString()) ? 
							String.format("[%s]", mask ? "O" : nodeId) : mask ? "[-]" : nodeId.toString();					
				}
			}
		}
		
		Arrays.stream(matrix).forEach(row -> {
			Arrays.stream(row).forEach(col -> System.out.printf(mask ? "%3s" : "%6s", col));
			System.out.println();
		});
	}

}
