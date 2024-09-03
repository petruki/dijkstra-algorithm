package com.github.petruki;

import com.github.petruki.model.DijkstraResult;
import com.github.petruki.model.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.petruki.model.Vertex.get;
import static org.junit.jupiter.api.Assertions.*;

class DijkstraScenario1Test {
	
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

	static Stream<Arguments> shortestPathArguments() {
		return Stream.of(
			Arguments.of("A", "C", "A -> C: [A, D, E, C] - cost: 7.0"),
			Arguments.of("C", "A", "C -> A: [C, E, D, A] - cost: 7.0"),
			Arguments.of("A", "B", "A -> B: [A, D, B] - cost: 3.0"),
			Arguments.of("B", "A", "B -> A: [B, D, A] - cost: 3.0"),
			Arguments.of("A", "E", "A -> E: [A, D, E] - cost: 2.0")
		);
	}

	@ParameterizedTest
	@MethodSource("shortestPathArguments")
	void testShortestPath(String origin, String destination, String expected) throws Exception {
		dijkstra.generateTable(origin);
		var result = dijkstra.getShortestPath(destination);

		result.printResult(false);
		assertEquals(expected, result.getResult());
	}

	@Test
	void testInvalidOriginNode() {
		var expectedExc = assertThrows(Exception.class, () -> dijkstra.generateTable("INVALID"));

		assertEquals("Node not found", expectedExc.getMessage());
	}

	@Test
	void testInvalidDestinationNode() {
		assertDoesNotThrow(() -> dijkstra.generateTable("A"));
		var expectedExc = assertThrows(Exception.class, () -> dijkstra.getShortestPath("INVALID_NODE"));

		assertEquals("Destination node not found", expectedExc.getMessage());
	}

}
