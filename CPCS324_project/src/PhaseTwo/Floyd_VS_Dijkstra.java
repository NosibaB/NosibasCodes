package PhaseTwo;
import java.util.ArrayList;

public class Floyd_VS_Dijkstra {
    static int inf=999;


    //function that prints a matrix
    public static void printMatrix(int [][] matrix) {

        for (int i=0; i<matrix.length; i++){

            for (int j=0; j<matrix.length; j++){

                // 221E is unicode infinity symbol
                String num= matrix[i][j]==inf? "\u221e" : matrix[i][j]+"";
                System.out.printf("%-4s", num);
            }

            System.out.println();
        }
        System.out.println("--------------------------------------------");

    }

    public static void floydWarshall(int [][] graph) {


        for (int k=0; k<graph.length; k++){
            System.out.println("Iteration "+ (k+1)+ ":\n");

            for (int i=0; i<graph.length; i++){

                for (int j=0; j<graph.length; j++){

                    //find the shortest distance from vertex i to vertex j with a maximum of k+1 intermediate vertices
                    graph[i][j]= Math.min(graph[i][j] , (graph[i][k] + graph[k][j]));

                }
            }

            printMatrix(graph); //print the distance matrix
        }

    }

    public static void dijkstra(int[][] graph, int source) {
        String[] vertices = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        String tree = "";
        String visited = "";
        ArrayList<edge> Edges= new ArrayList();
        int INF = Integer.MAX_VALUE;

        int count = graph.length;
        boolean[] visitedVertex = new boolean[count];
        int[] distance = new int[count];
        // initialize visitedVertex and distance arrays
        for (int i = 0; i < count; i++) {
            visitedVertex[i] = false;
            distance[i] = Integer.MAX_VALUE;
            Edges.add(new edge(source));
        }

        // Distance of self loop is zero
        distance[source] = 0;
        for (int i = 0; i < count; i++) {
            // Update the distance between neighbouring vertex and source vertex
            int u = findMinDistance(distance, visitedVertex);
            visitedVertex[u] = true;


            // Update all the neighbouring vertex distances
            for (int v = 0; v < count; v++) {
                if (!visitedVertex[v] && graph[u][v] != 0 && graph[u][v] != INF &&(distance[u] + graph[u][v] < distance[v])) {
                    distance[v] = distance[u] + graph[u][v];
                    Edges.get(v).setSrc(u); // save the intermediate source
                    visited+=vertices[v];  // save the visited nodes labels

                }
            }

        }
        // printing final distances
        System.out.println("\nShortest distances from "+ vertices[source] +" to all other vertices are:");
        for (int i = 0; i < distance.length; i++) {
            if(!vertices[source].equals( vertices[i]))
                System.out.println(String.format("Distance from %-2s to %-2s is %-4d", vertices[source], vertices[i], distance[i]));
        }

    }

    // Finding the minimum distance
    private static int findMinDistance(int[] distance, boolean[] visitedVertex) {
        int minDistance = Integer.MAX_VALUE;
        int minDistanceVertex = -1;
        for (int i = 0; i < distance.length; i++) {
            if (!visitedVertex[i] && distance[i] < minDistance) {
                minDistance = distance[i];
                minDistanceVertex = i;

            }
        }
        return minDistanceVertex;
    }

    static class edge{
        int vert;
        int src;
        edge(int src){
            this.src=src;
        }

        public int getVert() {
            return vert;
        }

        public void setVert(int vert) {
            this.vert = vert;
        }

        public int getSrc() {
            return src;
        }

        public void setSrc(int src) {
            this.src = src;
        }
    }



    public static void main(String[] args) {

        //weight matrix: directed distances between all pairs of vertices
        int [][] graph={{0,10,inf,inf,inf,5,inf,inf,inf,inf},
                {inf,0,3,inf,3,inf,inf,inf,inf,inf},
                {inf,inf,0,4,inf,inf,inf,5,inf,inf},
                {inf,inf,inf,0,inf,inf,inf,inf,4,inf},
                {inf,inf,4,inf,0,inf,2,inf,inf,inf},
                {inf,3,inf,inf,inf,0,inf,inf,inf,2},
                {inf,inf,inf,7,inf,inf,0,inf,inf,inf},
                {inf,inf,inf,4,inf,inf,inf,0,3,inf},
                {inf,inf,inf,inf,inf,inf,inf,inf,0,inf},
                {inf,6,inf,inf,inf,inf,8,inf,inf,0}};

        System.out.println("The weight matrix:\n");
        printMatrix(graph);
        System.out.println("\n\n");


        //apply Floyd-Warshall algorithm
        floydWarshall(graph);


        System.out.println("\n\n\nThe final matrix:\n");
        printMatrix(graph);

        // D
        int source=0;
        dijkstra(graph, source);

    }
}
