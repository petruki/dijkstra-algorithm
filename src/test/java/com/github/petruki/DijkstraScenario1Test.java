package com.github.petruki;

import static com.github.petruki.model.Vertex.get;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.petruki.model.DijkstraResult;
import com.github.petruki.model.Vertex;

public class DijkstraScenario1Test {
	
	private Dijkstra dijkstra;

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
		Set<Vertex> vertices = Stream.of(
			get("A", "B", 6),
			get("A", "D", 1),
			get("B", "D", 2),
			get("B", "E", 2),
			get("B", "C", 5),
			get("D", "E", 1),
			get("E", "C", 5))
		.collect(Collectors.toSet());
		
		dijkstra = new Dijkstra(vertices);
	}
	
	@Test
	void testCaseAtoC() throws Exception {
		//expected
		String expected = "A -> C: [A, D, E, C] - cost: 7.0";
		
		//test
		dijkstra.generateTable("A");
		DijkstraResult result = dijkstra.getShortestPath("C");
		
		result.printResult(false);
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseCtoA() throws Exception {
		//expected
		String expected = "C -> A: [C, E, D, A] - cost: 7.0";
		
		//test
		dijkstra.generateTable("C");
		DijkstraResult result = dijkstra.getShortestPath("A");
		
		result.printResult(false);
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseAtoB() throws Exception {
		//expected
		String expected = "A -> B: [A, D, B] - cost: 3.0";
		
		//test
		dijkstra.generateTable("A");
		DijkstraResult result = dijkstra.getShortestPath("B");
		
		result.printResult(false);
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseBtoA() throws Exception {
		//expected
		String expected = "B -> A: [B, D, A] - cost: 3.0";
		
		//test
		dijkstra.generateTable("B");
		DijkstraResult result = dijkstra.getShortestPath("A");
		
		result.printResult(false);
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testCaseAtoE() throws Exception {
		//expected
		String expected = "A -> E: [A, D, E] - cost: 2.0";
		
		//test
		dijkstra.generateTable("A");
		DijkstraResult result = dijkstra.getShortestPath("E");
		
		result.printResult(false);
		assertEquals(expected, result.getResult());
	}
	
	@Test
	void testInvalidOriginNode() {
		Exception expectedExc = assertThrows(Exception.class, 
				() -> dijkstra.generateTable("INVALID"));
		
		assertEquals(expectedExc.getMessage(), "Node not foud");
	}
	
	@Test
	void testInvalidDestinationNode() {
		assertDoesNotThrow(() -> dijkstra.generateTable("A"));
		
		Exception expectedExc = assertThrows(Exception.class, 
				() -> dijkstra.getShortestPath("INVALID_NODE"));
		
		assertEquals(expectedExc.getMessage(), "Destination node not found");
	}

}
