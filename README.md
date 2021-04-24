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
	- Import plaintext or images
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

### Running: Generated Density Matrix

A density matrix consists of a given matrix that contains 'n' nodes connected to all surrounding neighbors.
The cost for both horizontal and vertical vertices can be different from diagonal vertices.

To make the pathfinder more interesting, it is possible to exclude nodes from the matrix.

```java
// Generates a 10x10 Density Matrix with 1un. as horizontal/vertical cost and ignored diagonal trip
DensityMatrix densityMatrix = DijkstraUtils.generateDensityMatrix(10, 10, 1f, -1f);
densityMatrix.setStartNode("0");
densityMatrix.setEndNode("97");
densityMatrix.setIgnored(Arrays.asList(
		"96", "87", "88", "86", "77", "67", "69",
		"57", "45", "35", "25", "14", "4", "44",
		"54", "24", "53", "62", "52", "61", "71",
		"40", "31", "30", "32", "22", "11"
		));

// Build Dijkstra table
Dijkstra dijkstra = new Dijkstra(densityMatrix.getVertices());
dijkstra.generateTable(densityMatrix);

// Find shortest path
DijkstraResult result = dijkstra.getShortestPath(densityMatrix.getEndNode());

// Print results
result.printResult(false);
DijkstraUtils.printResultDensityMatrix(result, densityMatrix, true);
```

##### **Output**

- [NodeId] = shortest path found
- X = ignored NodeIds 

```
0 -> 97: [0, 1, 2, 12, 13, 23, 33, 43, 42, 41, 51, 50, 60, 70, 80, 81, 82, 72, 73, 63, 64, 65, 55, 56, 46, 47, 48, 58, 68, 78, 79, 89, 99, 98, 97] - cost: 34.0
   [0]   [1]   [2]     3     +     5     6     7     8     9
    10     +  [12]  [13]     +    15    16    17    18    19
    20    21     +  [23]     +     +    26    27    28    29
     +     +     +  [33]    34     +    36    37    38    39
     +  [41]  [42]  [43]     +     +  [46]  [47]  [48]    49
  [50]  [51]     +     +     +  [55]  [56]     +  [58]    59
  [60]     +     +  [63]  [64]  [65]    66     +  [68]     +
  [70]     +  [72]  [73]    74    75    76     +  [78]  [79]
  [80]  [81]  [82]    83    84    85     +     +     +  [89]
    90    91    92    93    94    95     +  [97]  [98]  [99]
```

The last parameter of 'DijkstraUtils.printResultDensityMatrix' can print the matrix in a prettier way, which is interesting for maze simulations.

```
[O][O][O]     +               
     +[O][O]  +               
        +[O]  +  +            
  +  +  +[O]     +            
  +[O][O][O]  +  +[O][O][O]   
[O][O]  +  +  +[O][O]  +[O]   
[O]  +  +[O][O][O]     +[O]  +
[O]  +[O][O]           +[O][O]
[O][O][O]           +  +  +[O]
                    +[O][O][O]
```

### Running: Input Density Matrix

It is also possible to give a plain text input that will be converted internally to the Density Matrix.

The following example shows a maze type matrix, where the characters define its strucuture:

- 'x': ignored/wall
- ' ': travelable nodes
- 's': starting node
- 'e': ending node

```java
String[] maze = {
	"+++++++++++++++",
	"+s+       + +e+",
	"+ +++++ +++ + +",
	"+ + +       + +",
	"+ +   +++ + + +",
	"+ + + +   + + +",
	"+   + +   + + +",
	"+++++ +   + + +",
	"+     +   +   +",
	"+++++++++++++++"
};
```

To code below shows how to use this strucuture as input for the Density Matrix.

```java
// Generates a Density Matrix based on a plain text input
DensityMatrix densityMatrix = DijkstraUtils.generateDensityMatrix(maze, 1f, -1f);

// Build Dijkstra table
Dijkstra dijkstra = new Dijkstra(densityMatrix.getVertices());
dijkstra.generateTable(densityMatrix);

// Find shortest path
DijkstraResult result = dijkstra.getShortestPath(densityMatrix.getEndNode());

// Print results
DijkstraUtils.printResultDensityMatrix(result, densityMatrix, true);

```

##### **Output**

```
  +  +  +  +  +  +  +  +  +  +  +  +  +  +  +
  +[O]  +                       +     +[O]  +
  +[O]  +  +  +  +  +     +  +  +     +[O]  +
  +[O]  +     +[O][O][O][O][O][O][O]  +[O]  +
  +[O]  +[O][O][O]  +  +  +     +[O]  +[O]  +
  +[O]  +[O]  +     +           +[O]  +[O]  +
  +[O][O][O]  +     +           +[O]  +[O]  +
  +  +  +  +  +     +           +[O]  +[O]  +
  +                 +           +[O][O][O]  +
  +  +  +  +  +  +  +  +  +  +  +  +  +  +  +

```


#### **Density Matrix App**

Create simulations based on Density Matrix scenarios or open the sampe included in this project located at: savedwork/demo1.dmf

![Density Matrix Sample](https://raw.githubusercontent.com/petruki/dijkstra-algorithm/master/docs/density_matrix_app.jpg)

#### **Density Matrix App: Importing images**

Try using this simple built-in image processor that can import data from external image sources.

> Image Sample

![Image Sample](https://raw.githubusercontent.com/petruki/dijkstra-algorithm/master/sample/maze.png)

> Output

![Density Matrix Sample](https://raw.githubusercontent.com/petruki/dijkstra-algorithm/master/docs/density_matrix_app_image_import.jpg)
