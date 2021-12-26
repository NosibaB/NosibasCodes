package PhaseTwo;
import java.util.LinkedList;

public class MaxFlow {
    static final int V = 6;
    /**
     * BFS supporting method to find the max-flow
     * @param rGraph
     * @param s
     * @param t
     * @param parent
     * @return true if we reached sink in BFS starting from source, or false otherwise
     */
    public boolean bfs(int rGraph[][], int s, int t, int parent[]) {
        /*
         * Create a visited array and mark all vertices as not
         * visited
         */
        boolean visited[] = new boolean[V];
        for(int i=0; i<V; ++i)
            visited[i]=false;
        /*
         * Create a queue, enqueue source vertex and mark
         * source vertex as visited
         */
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(s);
        visited[s] = true;
        parent[s]=-1;
        // Standard BFS Loop
        while (!queue.isEmpty())
        {
            int u = queue.poll();
            for (int v=0; v<V; v++)
            {
                if (visited[v]==false && rGraph[u][v] > 0)
                {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }
        /*
         * If we reached sink in BFS starting from source, then
         * return true, else false
         */
        return (visited[t] == true);
    }
    /**
     * maximum-flow algorithm using BFS
     * to find the maximum flow of the network, and the corresponding min-cut
     * @param  graph
     * @param s
     * @param t
     */
    public void fordFulkerson(int graph[][], int s, int t)
    {

        System.out.println("\t\t   Maximum flow");
        System.out.println("-----------------------------------------------\n");
        System.out.println("Augiminting paths:");
        int u, v;
        /*
         * Create a residual graph and fill the residual graph
         * with given capacities in the original graph as
         * residual capacities in residual graph
         */
        /*
         * Residual graph where rGraph[i][j] indicates
         * residual capacity of edge from i to j (if there
         * is an edge. If rGraph[i][j] is 0, then there is
         * not)
         */
        int rGraph[][] = new int[V][V];
        for (u = 0; u < V; u++)
            for (v = 0; v < V; v++)
                rGraph[u][v] = graph[u][v];
        // This array is filled by BFS and to store path
        int parent[] = new int[V];
        int max_flow = 0;  // There is no flow initially
        /*
         * Augment the flow while tere is path from source
         * to sink
         */
        //------------------------------------------


        while (bfs(rGraph, s, t, parent))
        {
            String path = "";

            /*
             *Find minimum residual capacity of the edhes
             * along the path filled by BFS. Or we can say
             * find the maximum flow through the path found.
             */
            int pathFlow = Integer.MAX_VALUE;
            for (v=t; v!=s; v=parent[v])
            {
                String direction="<<";
                u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph[u][v]);
                if(graph[u][v]!=0) direction=">>";
                path=direction+(v+1)+path;
            }
            path=(v+1)+path;
            System.out.printf("%-17s %s %d \n",path,"flow: ",pathFlow);

            /*
             * update residual capacities of the edges and
             * reverse edges along the path
             */
            for (v=t; v != s; v=parent[v])
            {
                u = parent[v];
                rGraph[u][v] -= pathFlow;
                rGraph[v][u] += pathFlow;
            }
            // Add path flow to overall flow
            max_flow += pathFlow;
            System.out.print("Updated flow: "+max_flow+"\n");

        }

        // print max-flow
        System.out.println(" The maximum flow is "+max_flow);
        System.out.println("\n\t\t   Minimum cut");
        System.out.println("-----------------------------------------------");
        // print min-cut edges
        System.out.println("\n Edges included in the min-cut");
        boolean[] isVisited = new boolean[graph.length];
        dfs(rGraph, s, isVisited);

        // Print all edges that are from a reachable vertex to
        // non-reachable vertex in the original graph
        int total_cut=0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] > 0 && isVisited[i] && !isVisited[j]) {
                    System.out.print("\nEdge: "+(i+1) + "-" + (j+1));
                    System.out.print(" , capacity = "+graph[i][j]);
                    total_cut+=graph[i][j];
                    System.out.println(" Updated min-cut capicity: "+total_cut);
                }
            }
        }
        System.out.println("\n The total min-cut capacity is "+total_cut+"\n");
    }
    /**
     * DFS supporting method to find the min-cut
     * @param graph
     * @param s
     * @param visited
     */
    public void dfs(int [][] graph, int s, boolean[]visited){
        visited[s]=true;
        for (int i = 0; i < graph.length; i++) {
            if(graph[s][i]>0&&!visited[i])
                dfs(graph,i,visited);
        }
    }
    /**
     * main function
     * @param args
     * @throws java.lang.Exception
     */
    public static void main (String[] args) throws java.lang.Exception {
        int graph[][] =new int[][] { {0, 2, 7, 0, 0, 0},
                {0, 0, 0, 3, 4, 0},
                {0, 0, 0, 4, 2, 0},
                {0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 5},
                {0, 0, 0, 0, 0, 0}
        };
        MaxFlow m = new MaxFlow();
        m.fordFulkerson(graph, 0, graph.length-1);
    }
}
