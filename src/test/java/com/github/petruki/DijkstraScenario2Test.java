package com.github.petruki;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.petruki.model.DijkstraResult;
import com.github.petruki.model.Vertex;

public class DijkstraScenario2Test {
	
	private List<Vertex> vertices;
	/*
	 *				 3
     *            ----------
	 *     2    /        3   \  
	 * 	A ---- B      G ---- H
	 * 	|	 / |      | \    |
	 * 3|  4/  |2     |5 \2  |3
	 * 	|  /   |      |   \  |
	 * 	D ---- E ---- C ---- J
	 *     1   	  2       7
	 *     
	 */   	 
	@BeforeEach
	void init() {  
		vertices = Arrays.asList(
			Vertex.get("A", "B", 2),
			Vertex.get("A", "D", 3),
			Vertex.get("D", "B", 4),
			Vertex.get("D", "E", 1),
			Vertex.get("E", "B", 2),
			Vertex.get("E", "C", 2),
			Vertex.get("B", "H", 3),
			Vertex.get("C", "J", 7),
			Vertex.get("C", "G", 5),
			Vertex.get("G", "H", 3),
			Vertex.get("G", "J", 2),
			Vertex.get("H", "J", 3)
		);
	}
	
	@Test
	void testCaseAtoJ() {
		//expected
		String expected = "A -> J: [A, B, H, J] - cost: 8";
		
		//test
		Dijkstra dijkstra = new Dijkstra(vertices, "A");
		DijkstraResult result = dijkstra.getShortestPath("J");
		
		result.printResult();
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseJtoA() {
		//expected
		String expected = "J -> A: [J, H, B, A] - cost: 8";
		
		//test
		Dijkstra dijkstra = new Dijkstra(vertices, "J");
		DijkstraResult result = dijkstra.getShortestPath("A");
		
		result.printResult();
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseJtoA_excludedRoute() {
		//given
		vertices = Arrays.asList(
				Vertex.get("A", "B", 2),
				Vertex.get("A", "D", 3),
				Vertex.get("D", "B", 4),
				Vertex.get("D", "E", 1),
				Vertex.get("E", "B", 2),
				Vertex.get("E", "C", 2),
				Vertex.get("B", "H", 3),
				Vertex.get("C", "J", 7),
				Vertex.get("C", "G", 5),
				Vertex.get("G", "H", 3),
				Vertex.get("G", "J", 2),
				
				/*
				 * J to H is not allowed
				 * only H -> J
				 */
				Vertex.get("H", "J", 3, false)
			);
		
		//expected
		String expected = "J -> A: [J, G, H, B, A] - cost: 10";
		
		//test
		Dijkstra dijkstra = new Dijkstra(vertices, "J");
		DijkstraResult result = dijkstra.getShortestPath("A");
		
		result.printResult();
		assertEquals(expected, result.getResult());
	}

}