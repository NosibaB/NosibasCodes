package phaseOne;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import javafx.util.Pair;

public class KruskalVSpqPrim {
    static class HeapNode {

        int vertex;
        int key;
    }

    static class ResultSet {

        int parent;
        int weight;
    }

    static class Edge { // Edge class to create many edge

        int source; // integer number represent the source
        int destination; // integer number represent the destination
        int weight; // the weight of edge

        public Edge(int source, int destination, int weight) { // Edge contractor to add new Edge
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    static class Graph { // Graph class to create many Graph

        int vertices; // number of node in graph
        LinkedList<Edge>[] adjacencylist; // LinkedList from Edge


        Graph(int vertices) { // Graph contractor
            this.vertices = vertices;
            adjacencylist = new LinkedList[vertices];  //initialize adjacency lists for all the vertices

            for (int i = 0; i < vertices; i++) {
                adjacencylist[i] = new LinkedList<>(); // create  adjacencylist for every vertices
            }
        }

        public void addEdge(int source, int destination, int weight) { // add Edge in adjacencylist
            Edge edge = new Edge(source, destination, weight); // create the edge
            adjacencylist[source].addFirst(edge); //  add Edge in source's adjacencylist

            edge = new Edge(destination, source, weight); // create the edge
            adjacencylist[destination].addFirst(edge); //   add Edge in destination's adjacencylist
            /*
            * because it for undirected graph we must add the edge
             in source's adjacencylist and destination's adjacencylist
            */

        }



        // class to represent a node in PriorityQueue
        // Stores a vertex and its corresponding
        // key value
        class node {
            int vertex;
            int key;
        }

        // Comparator class created for PriorityQueue
        // returns 1 if node0.key > node1.key
        // returns 0 if node0.key < node1.key and
        // returns -1 otherwise
        class comparator implements Comparator<node> {

            @Override
            public int compare(node node0, node node1)
            {
                return node0.key - node1.key;
            }
        }
        //%%%%%%%%%%%%%%%%%%%%%%%%%%% prime priority queue%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        void prims_mst(){

            boolean[] mst = new boolean[vertices];
            ResultSet[] resultSet = new ResultSet[vertices]; // create array to save for result Set
            int [] key = new int[vertices];  //keys used to store the key to know whether priority queue update is required

            //Initialize all the keys to infinity and
            //initialize resultSet for all the vertices
            for (int i = 0; i <vertices ; i++) {
                key[i] = Integer.MAX_VALUE;
                resultSet[i] = new ResultSet();
            }

            //Initialize priority queue
            //override the comparator to do the sorting based keys
            PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(vertices, new Comparator<Pair<Integer, Integer>>() {
                @Override
                public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                    //sort using key values
                    int key1 = p1.getKey();
                    int key2 = p2.getKey();
                    return key1-key2;
                }
            });

            //create the pair for for the first index, 0 key 0 index
            key[0] = 0;
            Pair<Integer, Integer> p0 = new Pair<>(key[0],0);
            //add it to pq
            pq.offer(p0);

            resultSet[0] = new ResultSet();
            resultSet[0].parent = -1;

            //while priority queue is not empty
            while(!pq.isEmpty()){
                //extract the min
                Pair<Integer, Integer> extractedPair = pq.poll();

                //extracted vertex
                int extractedVertex = extractedPair.getValue();
                mst[extractedVertex] = true;

                //iterate through all the adjacent vertices and update the keys
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i <list.size() ; i++) {
                    Edge edge = list.get(i);
                    //only if edge destination is not present in mst
                    if(mst[edge.destination]==false) {
                        int destination = edge.destination;
                        int newKey = edge.weight;
                        //check if updated key < existing key, if yes, update if
                        if(key[destination]>newKey) {
                            //add it to the priority queue
                            Pair<Integer, Integer> p = new Pair<>(newKey, destination);
                            pq.offer(p);
                            //update the resultSet for destination vertex
                            resultSet[destination].parent = extractedVertex;
                            resultSet[destination].weight = newKey;
                            //update the key[]
                            key[destination] = newKey;
                        }
                    }
                }
            }
            //print mst
            print_MST(resultSet);
        }

        public void print_MST(ResultSet[] resultSet){
            int total_min_weight = 0;
            System.out.println("Minimum Spanning Tree:  (prime priority queue) ");
            for (int i = 1; i <vertices ; i++) {

                // #### if you want to see the vertices of the Minimum Spanning Tree###
                // System.out.println("Edge: " + i + " - " + resultSet[i].parent +
                //       " key: " + resultSet[i].weight);
                total_min_weight += resultSet[i].weight;
            }
            System.out.println("Total minimum key: " + total_min_weight);
        }

    }
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%kruskal%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    static class GraphK {
        int vertices; // initial vertices
        ArrayList<Edge> allEdges = new ArrayList<>(); // initial ArrayList from Edge class

        GraphK(int vertices) { // contractor to initial new GraphK
            this.vertices = vertices;
        }

        public void addEgde(int source, int destination, int weight) { // method to add addEgde for GraphK
            Edge edge = new Edge(source, destination, weight);
            allEdges.add(edge); //add to total edges
        }
        public void kruskalMST(){
            PriorityQueue<Edge> pq = new PriorityQueue<>(allEdges.size(), Comparator.comparingInt(o -> o.weight));


            //add all the edges to priority queue, //sort the edges on weights
            for (int i = 0; i <allEdges.size()  ; i++) {
                pq.add(allEdges.get(i));

            }

            //create a parent []
            int [] parent = new int[vertices];

            //makeset
            makeSet(parent);

            ArrayList<Edge> mst = new ArrayList<>();

            //process vertices - 1 edges
            int index = 0;
            while(index<vertices-1){
                Edge edge = pq.remove();
                //check if adding this edge creates a cycle
                int x_set = find(parent, edge.source);
                int y_set = find(parent, edge.destination);

                if(x_set==y_set){
                    //ignore, will create cycle
                }else {
                    //add it to our final result
                    mst.add(edge);
                    index++;
                    union(parent,x_set,y_set);
                }
            }
            //print MST
            System.out.println("Minimum Spanning Tree:  (Kruskal) ");
            printGraph(mst);
        }

        public void makeSet(int [] parent){
            //Make set- creating a new element with a parent pointer to itself.
            for (int i = 0; i <vertices ; i++) {
                parent[i] = i;
            }
        }

        public int find(int [] parent, int vertex){
            //chain of parent pointers from x upwards through the tree
            // until an element is reached whose parent is itself
            if(parent[vertex]!=vertex)
                return find(parent, parent[vertex]);;
            return vertex;
        }

        public void union(int [] parent, int x, int y){
            int x_set_parent = find(parent, x);
            int y_set_parent = find(parent, y);
            //make x as parent of y
            parent[y_set_parent] = x_set_parent;
        }

        public void printGraph(ArrayList<Edge> edgeList){
            int total_min_weight = 0;
            for (int i = 0; i <edgeList.size() ; i++) {
                Edge edge = edgeList.get(i);

                // #### if you want to see the vertices of the Minimum Spanning Tree###
                //System.out.println( " source: " + edge.source +
                //    " destination: " + edge.destination +
                //  " weight: " + edge.weight);
                total_min_weight += edge.weight;
            }
            System.out.println("Total minimum key: " + total_min_weight);
        }

    }



    public static void main(String[] args) {
        // TODO code application logic here





        for (int i =1 ; i<11 ; i++){ // to do all the 10 cases
            System.out.print(" Case "+i+"----------------------             ");
            make_graph(i);
            System.out.println("\n\n");
        }

    }

    public static void make_graph(int choice  ) {

        switch(choice) {

            case 1: // •	n=1000 and m=10000

                //**************************************************************
                int V1 = 1000; // number of node
                Graph graph1_P = new Graph(V1);
                GraphK graph1_K = new GraphK(V1);
                for (int i=0 ; i<10000 ; i++){

                    int src = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int dest = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<1000){ // to be sure all node are connected
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph1_P.addEdge( i, dest, weight);
                        graph1_K.addEgde( i, dest, weight);
                        //  System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph1_P.addEdge( src, dest, weight);
                        graph1_K.addEgde( src, dest, weight);
                        //System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }
                System.out.println(" n=1000 and m=10000                           ....,");



                long startTime_priority = System.nanoTime();
                graph1_P.prims_mst();
                long endTime_priority   = System.nanoTime();
                long totalTime_priority = endTime_priority - startTime_priority;





                System.out.println("**************");
                long startTime_kruska1 = System.nanoTime();
                graph1_K.kruskalMST(); // call kruskal
                long endTime_kruskal    = System.nanoTime();
                long totalTime_kruska1  = endTime_kruskal  - startTime_kruska1;

                System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority /1000000+" milliseconds  , Time in Kruskal : "+ totalTime_kruska1/1000000+" milliseconds ");

                break ;
            //**************************************************************


            case 2: // •	n=1000 and m= 15000

                //**************************************************************
                int V2 = 1000; // number of node
                Graph graph2_P = new Graph(V2);
                GraphK graph2_K = new GraphK(V2);

                for (int i=0 ; i<15000 ; i++){

                    int src = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int dest = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int weight =1+ (int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<1000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph2_P.addEdge( i, dest, weight);
                        graph2_K.addEgde(i, dest, weight);
                        //    System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph2_P.addEdge( src, dest, weight);
                        graph2_K.addEgde( src, dest, weight);
                        // System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }
                System.out.println(" n=1000 and m= 15000                   ....,\n");

                long startTime_priority2 = System.nanoTime();
                graph2_P.prims_mst();
                long endTime_priority2   = System.nanoTime();
                long totalTime_priority2 = endTime_priority2 - startTime_priority2;





                System.out.println("**************");
                long startTime_kruska2 = System.nanoTime();
                graph2_K.kruskalMST(); // call kruskal
                long endTime_kruska2    = System.nanoTime();
                long totalTime_kruska2  = endTime_kruska2  - startTime_kruska2;

                System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority2 /1000000+" milliseconds  ,Time in Kruskal : "+ totalTime_kruska2 /1000000+" milliseconds");
                break ;
            //**************************************************************


            case 3:// •	n=1000 and m= 25000

                //**************************************************************
                int V3 = 1000; // number of node
                Graph graph3_P = new Graph(V3);
                GraphK graph3_K = new GraphK(V3);

                for (int i=0 ; i<25000 ; i++){

                    int src = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int dest = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<1000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph3_P.addEdge( i, dest, weight);
                        graph3_K.addEgde(i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph3_P.addEdge( src, dest, weight);
                        graph3_K.addEgde(src, dest, weight);
                        //  System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }

                }

                System.out.println(" n=1000 and m= 25000                   ....,\n");
                long startTime_priority3 = System.nanoTime();
                graph3_P.prims_mst();
                long endTime_priority3   = System.nanoTime();
                long totalTime_priority3 = endTime_priority3 - startTime_priority3;





                System.out.println("**************");
                long startTime_kruska3 = System.nanoTime();
                graph3_K.kruskalMST(); // call kruskal
                long endTime_kruska3    = System.nanoTime();
                long totalTime_kruska3  = endTime_kruska3  - startTime_kruska3;

                System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority3 /1000000+" milliseconds  ,Time in Kruskal : "+ totalTime_kruska3 /1000000+" milliseconds");
                break ;
            //**************************************************************


            case 4: //•	n=5000 and m=15000

                //**************************************************************
                int V4 = 5000; // number of node
                Graph graph4_P = new Graph(V4);
                GraphK graph4_K = new GraphK(V4);

                for (int i=0 ; i<15000 ; i++){

                    int src = (int)  (Math.random() * 5000); // Generate random number between 0 to 5000
                    int dest = (int)  (Math.random() * 5000); // Generate random number between 0 to 5000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<5000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph4_P.addEdge( i, dest, weight);
                        graph4_K.addEgde(i, dest, weight);
                        // System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;


                        graph4_P.addEdge( src, dest, weight);
                        graph4_K.addEgde(src, dest, weight);
                        // System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }
                System.out.println(" n=5000 and m=15000                   ....,\n");
                long startTime_priority4 = System.nanoTime();
                graph4_P.prims_mst();
                long endTime_priority4   = System.nanoTime();
                long totalTime_priority4 = endTime_priority4 - startTime_priority4;





                System.out.println("**************");
                long startTime_kruska4 = System.nanoTime();
                graph4_K.kruskalMST(); // call kruskal
                long endTime_kruska4    = System.nanoTime();
                long totalTime_kruska4  = endTime_kruska4  - startTime_kruska4;

                System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority4 /1000000+" milliseconds  ,Time in Kruskal : "+ totalTime_kruska4 /1000000+" milliseconds");
                break ;
            //**************************************************************


            case 5:// •	n=5000 and m= 25000

                //**************************************************************
                int V5 = 5000; // number of node
                Graph graph5_P = new Graph(V5);
                GraphK graph5_K = new GraphK(V5);

                for (int i=0 ; i<25000 ; i++){

                    int src = (int)  (Math.random() * 5000); // Generate random number between 0 to 5000
                    int dest = (int)  (Math.random() * 5000); // Generate random number between 0 to 5000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<5000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph5_P.addEdge( i, dest, weight);
                        graph5_K.addEgde(i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph5_P.addEdge( src, dest, weight);
                        graph5_K.addEgde(src, dest, weight);
                        //System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }
                System.out.println(" n=5000 and m= 25000                   ....,\n");
                long startTime_priority5 = System.nanoTime();
                graph5_P.prims_mst();
                long endTime_priority5   = System.nanoTime();
                long totalTime_priority5 = endTime_priority5 - startTime_priority5;





                System.out.println("**************");
                long startTime_kruska5 = System.nanoTime();
                graph5_K.kruskalMST(); // call kruskal
                long endTime_kruska5    = System.nanoTime();
                long totalTime_kruska5  = endTime_kruska5  - startTime_kruska5;

                System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority5 /1000000+" milliseconds  ,Time in Kruskal : "+ totalTime_kruska5 /1000000+" milliseconds");
                break ;
            //**************************************************************


            case 6:// •	n=10000 and m=15000

                //**************************************************************
                int V6 = 10000; // number of node
                Graph graph6_P = new Graph(V6);
                GraphK graph6_K = new GraphK(V6);

                for (int i=0 ; i<15000 ; i++){

                    int src = (int)  (Math.random() * 10000); // Generate random number between 0 to 10000
                    int dest = (int)  (Math.random() * 10000); // Generate random number between 0 to 10000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<10000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph6_P.addEdge( i, dest, weight);
                        graph6_K.addEgde(i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph6_P.addEdge( src, dest, weight);
                        graph6_K.addEgde(src, dest, weight);
                        //System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }
                System.out.println(" n=10000 and m=15000                   ....,\n");
                long startTime_priority6 = System.nanoTime();
                graph6_P.prims_mst();
                long endTime_priority6   = System.nanoTime();
                long totalTime_priority6 = endTime_priority6 - startTime_priority6;





                System.out.println("**************");
                long startTime_kruska6 = System.nanoTime();
                graph6_K.kruskalMST(); // call kruskal
                long endTime_kruska6    = System.nanoTime();
                long totalTime_kruska6  = endTime_kruska6  - startTime_kruska6;

                System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority6 /1000000+" milliseconds  ,Time in Kruskal : "+ totalTime_kruska6 /1000000+" milliseconds");
                break ;
            //**************************************************************


            case 7:// •	n=10000 and m= 25000

                //**************************************************************
                int V7 = 10000; // number of node
                Graph graph7_P = new Graph(V7);
                GraphK graph7_K = new GraphK(V7);

                for (int i=0 ; i<25000 ; i++){

                    int src = (int)  (Math.random() * 10000); // Generate random number between 0 to 10000
                    int dest = (int)  (Math.random() * 10000); // Generate random number between 0 to 10000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<10000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph7_P.addEdge( i, dest, weight);
                        graph7_K.addEgde(i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph7_P.addEdge( src, dest, weight);
                        graph7_K.addEgde(src, dest, weight);
                        //System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }
                System.out.println(" n=10000 and m= 25000                   ....,\n");
                long startTime_priority7 = System.nanoTime();
                graph7_P.prims_mst();
                long endTime_priority7   = System.nanoTime();
                long totalTime_priority7 = endTime_priority7 - startTime_priority7;





                System.out.println("**************");
                long startTime_kruska7 = System.nanoTime();
                graph7_K.kruskalMST(); // call kruskal
                long endTime_kruska7    = System.nanoTime();
                long totalTime_kruska7  = endTime_kruska7  - startTime_kruska7;

                System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority7 /1000000+" milliseconds  ,Time in Kruskal : "+ totalTime_kruska7 /1000000+" milliseconds");
                break ;
            //**************************************************************


            case 8: //•	n=20000 and m=200000

                //**************************************************************
                int V8 = 20000; // number of node
                Graph graph8_P = new Graph(V8);
                GraphK graph8_K = new GraphK(V8);

                for (int i=0 ; i<200000 ; i++){

                    int src = (int)  (Math.random() * 20000); // Generate random number between 0 to 20000
                    int dest = (int)  (Math.random() * 20000); // Generate random number between 0 to 20000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<20000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph8_P.addEdge( i, dest, weight);
                        graph8_K.addEgde(i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph8_P.addEdge( src, dest, weight);
                        graph8_K.addEgde(src, dest, weight);
                        //System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }
                System.out.println(" n=20000 and m=200000                   ....,\n");
                long startTime_priority8 = System.nanoTime();
                graph8_P.prims_mst();
                long endTime_priority8   = System.nanoTime();
                long totalTime_priority8 = endTime_priority8 - startTime_priority8;





                System.out.println("**************");
                long startTime_kruska8 = System.nanoTime();
                graph8_K.kruskalMST(); // call kruskal
                long endTime_kruska8    = System.nanoTime();
                long totalTime_kruska8  = endTime_kruska8  - startTime_kruska8;

                System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority8 /1000000+" milliseconds  ,Time in Kruskal : "+ totalTime_kruska8 /1000000+" milliseconds");
                break ;
            //**************************************************************


            case 9:// •	n=20000 and m= 300000

                //**************************************************************
                int V9 = 20000; // number of node
                Graph graph9_P = new Graph(V9);
                GraphK graph9_K = new GraphK(V9);

                for (int i=0 ; i<300000 ; i++){

                    int src = (int)  (Math.random() * 20000); // Generate random number between 0 to 20000
                    int dest = (int)  (Math.random() * 20000); // Generate random number between 0 to 20000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<20000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph9_P.addEdge( i, dest, weight);
                        graph9_K.addEgde(i, dest, weight);
                        //      System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph9_P.addEdge( src, dest, weight);
                        graph9_K.addEgde(src, dest, weight);
                        //         System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }
                System.out.println(" n=20000 and m= 300000                   ....,\n");
                long startTime_priority9 = System.nanoTime();
                graph9_P.prims_mst();
                long endTime_priority9   = System.nanoTime();
                long totalTime_priority9 = endTime_priority9 - startTime_priority9;





                System.out.println("**************");
                long startTime_kruska9 = System.nanoTime();
                graph9_K.kruskalMST(); // call kruskal
                long endTime_kruska9    = System.nanoTime();
                long totalTime_kruska9  = endTime_kruska9  - startTime_kruska9;

                System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority9 /1000000+" milliseconds  ,Time in Kruskal : "+ totalTime_kruska9 /1000000+" milliseconds");
                break ;
            //**************************************************************


            case 10:// •	n=50000 and m=1,000,000

                //**************************************************************
                int V10 = 50000; // number of node
                Graph graph10_P = new Graph(V10);
                GraphK graph10_K = new GraphK(V10);

                for (int i=0 ; i<1000000 ; i++){

                    int src = (int)  (Math.random() * 50000); // Generate random number between 0 to 50000
                    int dest = (int)  (Math.random() * 50000); // Generate random number between 0 to 50000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<50000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph10_P.addEdge( i, dest, weight);
                        graph10_K.addEgde(i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph10_P.addEdge( src, dest, weight);
                        graph10_K.addEgde(src, dest, weight);
                        //   System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }
                System.out.println(" n=50000 and m=1,000,000                   ....,\n");
                long startTime_priority10 = System.nanoTime();
                graph10_P.prims_mst();
                long endTime_priority10   = System.nanoTime();
                long totalTime_priority10 = endTime_priority10 - startTime_priority10;





                System.out.println("**************");
                long startTime_kruska10 = System.nanoTime();
                graph10_K.kruskalMST(); // call kruskal
                long endTime_kruska10    = System.nanoTime();
                long totalTime_kruska10  = endTime_kruska10  - startTime_kruska10;
                System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority10 /1000000+" milliseconds  ,Time in Kruskal : "+ totalTime_kruska10 /1000000+" milliseconds");

                break ;
            //**************************************************************


        }



    }
}
