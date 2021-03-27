package com.github.petruki;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.petruki.model.DijkstraResult;
import com.github.petruki.model.Vertex;

public class DijkstraScenario1Test {
	
	private List<Vertex> vertices;

	/*     6
	 * 	A ---- B -\
	 * 	|	 / |   \5 
	 * 1|  2/  |2   C
	 * 	|  /   |   /5
	 * 	D ---- E -/
	 *     1   
	 */   	 
	@BeforeEach
	void init() {  
		vertices = Arrays.asList(
			Vertex.get("A", "B", 6),
			Vertex.get("A", "D", 1),
			Vertex.get("B", "D", 2),
			Vertex.get("B", "E", 2),
			Vertex.get("B", "C", 5),
			Vertex.get("D", "E", 1),
			Vertex.get("E", "C", 5));
	}
	
	@Test
	void testCaseAtoC() {
		//expected
		String expected = "A -> C: [A, D, E, C] - cost: 7";
		
		//test
		Dijkstra dijkstra = new Dijkstra(vertices, "A");
		DijkstraResult result = dijkstra.getShortestPath("C");
		
		result.printResult();
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseCtoA() {
		//expected
		String expected = "C -> A: [C, E, D, A] - cost: 7";
		
		//test
		Dijkstra dijkstra = new Dijkstra(vertices, "C");
		DijkstraResult result = dijkstra.getShortestPath("A");
		
		result.printResult();
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseAtoB() {
		//expected
		String expected = "A -> B: [A, D, B] - cost: 3";
		
		//test
		Dijkstra dijkstra = new Dijkstra(vertices, "A");
		DijkstraResult result = dijkstra.getShortestPath("B");
		
		result.printResult();
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseBtoA() {
		//expected
		String expected = "B -> A: [B, D, A] - cost: 3";
		
		//test
		Dijkstra dijkstra = new Dijkstra(vertices, "B");
		DijkstraResult result = dijkstra.getShortestPath("A");
		
		result.printResult();
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseAtoE() {
		//expected
		String expected = "A -> E: [A, D, E] - cost: 2";
		
		//test
		Dijkstra dijkstra = new Dijkstra(vertices, "A");
		DijkstraResult result = dijkstra.getShortestPath("E");
		
		result.printResult();
		assertEquals(expected, result.getResult());
	}

}
