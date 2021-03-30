### About

Famous algrithm by Edsger W. Dijkstra.

Algorithm well explained here:
[Graph Data Structure 4. Dijkstra’s Shortest Path Algorithm](https://www.youtube.com/watch?v=pVfj6mxhdMw)

This implementation offers a wide view of how the algorithm works.<br>
Based on behavior modeling, the implementation is modular enough to be viewed as granular version of the algorithm.

### Features

- Pre-calculate Dijkstra table based on the origin node.
- Vertices can be set as single or bidirectional for more complex scenarios.
- Display result table.
- Display summary with visited nodes and total cost.

### Running

> Scenario

![Scenario 1](https://raw.githubusercontent.com/petruki/dijkstra-algorithm/master/docs/scenario1.jpg)

> Code

```java
List<Vertex> vertices = Arrays.asList(
	/*
	 * B to A is not allowed
	 * only A -> B
	 */
	Vertex.get("A", "B", 2, false),
	Vertex.get("A", "D", 3),
	Vertex.get("D", "B", 1),
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

Dijkstra dijkstra = new Dijkstra(vertices);
dijkstra.generateTable("A");
DijkstraResult result = dijkstra.getShortestPath("A");

result.pritnResult();
```

> Displays

```
J -> A: [J, G, H, B, D, A] - cost: 12
vertex=A, prev=D, distance=12
vertex=B, prev=H, distance=8
vertex=C, prev=J, distance=7
vertex=D, prev=B, distance=9
vertex=E, prev=C, distance=9
vertex=G, prev=J, distance=2
vertex=H, prev=G, distance=5
vertex=J, prev= , distance=0
```