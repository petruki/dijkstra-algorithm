package com.github.petruki;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.petruki.model.Vertex.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DijkstraScenario2Test {
	
	private Dijkstra dijkstra;
	
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
		var vertices = Stream.of(
			get("A", "B", 2),
			get("A", "D", 3),
			get("D", "B", 4),
			get("D", "E", 1),
			get("E", "B", 2),
			get("E", "C", 2),
			get("B", "H", 3),
			get("C", "J", 7),
			get("C", "G", 5),
			get("G", "H", 3),
			get("G", "J", 2),
			get("H", "J", 3)
		).collect(Collectors.toSet());
		
		dijkstra = new Dijkstra(vertices);
	}
	
	@Test
	void testCaseAtoJ() throws Exception {
		//expected
		var expected = "A -> J: [A, B, H, J] - cost: 8.0";
		
		//test
		dijkstra.generateTable("A");
		var result = dijkstra.getShortestPath("J");
		
		result.printResult(false);
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseJtoA() throws Exception {
		//expected
		var expected = "J -> A: [J, H, B, A] - cost: 8.0";
		
		//test
		dijkstra.generateTable("J");
		var result = dijkstra.getShortestPath("A");
		
		result.printResult(false);
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseJtoA_excludedRoute() throws Exception {
		//given
		var vertices = Stream.of(
				get("A", "B", 2),
				get("A", "D", 3),
				get("D", "B", 4),
				get("D", "E", 1),
				get("E", "B", 2),
				get("E", "C", 2),
				get("B", "H", 3),
				get("C", "J", 7),
				get("C", "G", 5),
				get("G", "H", 3),
				get("G", "J", 2),
				
				/*
				 * J to H is not allowed
				 * only H -> J
				 */
				get("H", "J", 3, false)
		).collect(Collectors.toSet());
		
		var dijkstraLocal = new Dijkstra(vertices);
		
		//expected
		var expected = "J -> A: [J, G, H, B, A] - cost: 10.0";
		
		//test
		dijkstraLocal.generateTable("J");
		var result = dijkstraLocal.getShortestPath("A");
		
		result.printResult(false);
		assertEquals(expected, result.getResult());
	}

}
