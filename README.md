### About

Famous algrithm by Edsger W. Dijkstra.

Algorithm well explained here:
[Graph Data Structure 4. Dijkstra’s Shortest Path Algorithm](https://www.youtube.com/watch?v=pVfj6mxhdMw)

This implementation offers a wide view of how the algorithm works.<br>
I have added a few features that can make more complex scenarios such as unidirectional vertices and density matrix for pathfinder simulation.

### Features

- Pre-calculate Dijkstra table based on the origin node.
- Vertices can be set as single or bidirectional for more complex scenarios.
- Nodes can bet set to ignored.
- Density matrix for pathfinder simulations.
- Display result table.
- Display summary with visited nodes and total cost.

#### Density Matrix Simulator App

- Create and run Density Matrix simulations using the Desktop UI.

### Running

> Scenario

![Scenario 1](https://raw.githubusercontent.com/petruki/dijkstra-algorithm/master/docs/scenario1.jpg)

> Code

```java
import static com.github.petruki.model.Vertex.get;

List<Vertex> vertices = Arrays.asList(
	/*
	 * B to A is not allowed
	 * only A -> B
	 */
	get("A", "B", 2, false),
	get("A", "D", 3),
	get("D", "B", 1),
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
);

Dijkstra dijkstra = new Dijkstra(vertices);
dijkstra.generateTable("A");
DijkstraResult result = dijkstra.getShortestPath("A");

result.pritnResult(true);
```

> Displays

```
J -> A: [J, G, H, B, D, A] - cost: 12.0
vertex=A, prev=D, distance=12
vertex=B, prev=H, distance=8
vertex=C, prev=J, distance=7
vertex=D, prev=B, distance=9
vertex=E, prev=C, distance=9
vertex=G, prev=J, distance=2
vertex=H, prev=G, distance=5
vertex=J, prev= , distance=0
```

### Running: Density Matrix

A density matrix consists of a given square matrix that contains 'n' nodes connected to all surrounding neighbors.
The cost for both horizontal and vertical vertices can be different from diagonal vertices.

To make the pathfinder more interesting, it is possible to exclude nodes from the matrix.

```java
// Generates a 10x10 density matrix with 1 as h&v costs and 1.2 as diagonal cost
Dijkstra dijkstra = new Dijkstra(DijkstraUtils.generateDensityMatrix(10, 1f, 1.2f));

// Calculate the Dijkstra table starting at node 99 and
// ignore the given list of nodes as the second argument
dijkstra.generateTable("99", Arrays.asList(
		"89", "88", "87", "86", "85", "84", "74", "65", "76", "56", "55", "67", "78"));

// Find the shortest path in the matrix to node 79
DijkstraResult result = dijkstra.getShortestPath("79");

// Print result summary
result.printResult(false);

// Print result desity matrix
DijkstraUtils.printResultDensityMatrix(result, 10, false);
```

#### **Output**

- [NodeId] = shortest path found
- X = ignored NodeIds 

```
99 -> 79: [99, 98, 97, 96, 95, 94, 83, 73, 64, 75, 66, 57, 68, 79] - cost: 14.399999
     0     1     2     3     4     5     6     7     8     9
    10    11    12    13    14    15    16    17    18    19
    20    21    22    23    24    25    26    27    28    29
    30    31    32    33    34    35    36    37    38    39
    40    41    42    43    44    45    46    47    48    49
    50    51    52    53    54   [X]   [X]  [57]    58    59
    60    61    62    63  [64]   [X]  [66]   [X]  [68]    69
    70    71    72  [73]   [X]  [75]   [X]    77   [X]  [79]
    80    81    82  [83]   [X]   [X]   [X]   [X]   [X]   [X]
    90    91    92    93  [94]  [95]  [96]  [97]  [98]  [99]
```

The last parameter of 'DijkstraUtils.printResultDensityMatrix' can print the matrix in a more visual way, which is interesting for maze simulations.

```
[O][O][O][-][X][-][-][-][-][-]
[-][X][O][O][X][-][-][-][-][-]
[-][-][X][O][X][X][-][-][-][-]
[X][X][X][O][-][X][-][-][-][-]
[X][O][O][O][X][X][O][O][O][-]
[O][O][X][X][X][O][O][X][O][-]
[O][X][X][O][O][O][-][X][O][X]
[O][X][O][O][-][-][-][X][O][O]
[O][O][O][-][-][-][X][X][X][O]
[-][-][-][-][-][-][X][O][O][O]
```