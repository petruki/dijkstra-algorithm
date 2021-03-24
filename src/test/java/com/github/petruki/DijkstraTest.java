package com.github.petruki;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.petruki.model.Vertex;

public class DijkstraTest {
	
	private List<Vertex> vertices;

	@BeforeEach
	void init() {
		vertices = Arrays.asList(
			new Vertex("A", "B", 6),
			new Vertex("A", "D", 1),
			new Vertex("B", "D", 2),
			new Vertex("B", "E", 2),
			new Vertex("B", "C", 5),
			new Vertex("D", "E", 1),
			new Vertex("E", "C", 5));
	}
	
	@Test
	void testCase1() {
		//expected
		String expected = "A -> C: [A, D, E, C] - cost: 7";
		
		//test
		Dijkstra dijkstra = new Dijkstra(vertices);
		dijkstra.generateTable("A");
		assertEquals(expected, dijkstra.getShortestPath("C").getResult());
	}

}
