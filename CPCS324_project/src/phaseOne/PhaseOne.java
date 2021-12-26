package phaseOne;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeSet;
import javafx.util.Pair;

public class PhaseOne {
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
            adjacencylist = new LinkedList[vertices]; //initialize adjacency lists for all the vertices
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

        static class HeapNode {  //  class for Heap Node  we save the vertex and key


            int vertex;
            int key;
        }

        static class ResultSet { // class for  Result Set we save the parent and weight


            int parent;
            int weight;
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
            ResultSet[] resultSet = new ResultSet[vertices];// create array to save for result Set
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
                Pair<Integer, Integer> extractedPair = pq.poll() ;

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
            int total_min_weight = 0; // initialize total_min_weight to count the weight
            System.out.println("Minimum Spanning Tree:  (prime priority queue) ");
            for (int i = 1; i <vertices ; i++) { // print resultSet of Minimum Spanning Tree



                // #### if you want to see the vertices of the Minimum Spanning Tree###
                //System.out.println("Edge: " + i + " - " + resultSet[i].parent +
                //      " key: " + resultSet[i].weight);
                total_min_weight += resultSet[i].weight; // count the weight
            }
            System.out.println("Total minimum key: " + total_min_weight); // print minimum weight
        }





        //%%%%%%%%%%%%%%%%%%%%%%%%%%prime min heap%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



        public void primMST(Graph graph) {

            boolean[] inHeap = new boolean[graph.vertices]; //
            ResultSet[] resultSet = new ResultSet[graph.vertices]; // create array to save for result Set
            //keys[] used to store the key to know whether min hea update is required
            int[] key = new int[graph.vertices];
            //create heapNode for all the vertices
            HeapNode[] heapNodes = new HeapNode[graph.vertices];
            for (int i = 0; i < graph.vertices; i++) {
                heapNodes[i] = new HeapNode();
                heapNodes[i].vertex = i;
                heapNodes[i].key = Integer.MAX_VALUE; // initial value
                resultSet[i] = new ResultSet();// initial new result set for this vertices
                resultSet[i].parent = -1; // initial value
                inHeap[i] = true; // initial value
                key[i] = Integer.MAX_VALUE; // initial value
            }

            //decrease the key for the first index
            heapNodes[0].key = 0;

            //add all the vertices to the MinHeap
            MinHeap minHeap = new MinHeap(graph.vertices);
            //add all the vertices to priority queue
            for (int i = 0; i < graph.vertices; i++) {
                minHeap.insert(heapNodes[i]);
            }

            //while minHeap is not empty
            while (!minHeap.isEmpty()) {
                //extract the min
                HeapNode extractedNode = minHeap.extractMin();

                //extracted vertex
                int extractedVertex = extractedNode.vertex;
                inHeap[extractedVertex] = false;// deleted vertex

                //iterate through all the adjacent vertices
                LinkedList<Edge> list = graph.adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    //only if edge destination is present in heap
                    if (inHeap[edge.destination]) {
                        int destination = edge.destination;
                        int newKey = edge.weight;
                        //check if updated key < existing key, if yes, update if
                        if (key[destination] > newKey) {
                            decreaseKey(minHeap, newKey, destination);
                            //update the parent node for destination
                            resultSet[destination].parent = extractedVertex;
                            resultSet[destination].weight = newKey;
                            key[destination] = newKey;
                        }
                    }
                }
            }
            //print mst
            printMST( graph,resultSet);
        }

        public void decreaseKey(MinHeap minHeap, int newKey, int vertex) { // function for updated key

            //get the index which key's needs a decrease;
            int index = minHeap.indexes[vertex];

            //get the node and update its value
            HeapNode node = minHeap.mH[index];
            node.key = newKey;
            minHeap.bubbleUp(index);
        }

        public void printMST(Graph graph ,ResultSet[] resultSet) {
            int total_min_weight = 0; // initialize total_min_weight to count the weight
            System.out.println("Minimum Spanning Tree: (prime min heap)");
            for (int i = 1; i < graph.vertices; i++) {// print resultSet of Minimum Spanning Tree

                // #### if you want to see the vertices of the Minimum Spanning Tree###
                // System.out.println("Edge: " + i + " - " + resultSet[i].parent
                //      + " weight: " + resultSet[i].weight);
                total_min_weight += resultSet[i].weight; //  count the weight
            }
            System.out.println("Total minimum key: " + total_min_weight); // print Total minimum key
        }


        static class MinHeap { //

            int capacity;
            int currentSize;
            HeapNode[] mH;
            int[] indexes; //will be used to decrease the key

            public MinHeap(int capacity) { // create MinHeap for kwon capacity
                this.capacity = capacity;
                mH = new HeapNode[capacity + 1];
                indexes = new int[capacity];
                mH[0] = new HeapNode();
                mH[0].key = Integer.MIN_VALUE;
                mH[0].vertex = -1;
                currentSize = 0;
            }


            public void insert(HeapNode x) {
                currentSize++;
                int idx = currentSize;
                mH[idx] = x;
                indexes[x.vertex] = idx;
                bubbleUp(idx);
            }


            public void bubbleUp(int pos) {
                int parentIdx = pos / 2;
                int currentIdx = pos;
                while (currentIdx > 0 && mH[parentIdx].key > mH[currentIdx].key) {
                    HeapNode currentNode = mH[currentIdx];
                    HeapNode parentNode = mH[parentIdx];

                    //swap the positions
                    indexes[currentNode.vertex] = parentIdx;
                    indexes[parentNode.vertex] = currentIdx;
                    swap(currentIdx, parentIdx);
                    currentIdx = parentIdx;
                    parentIdx = parentIdx / 2;
                }
            }

            public HeapNode extractMin() {
                HeapNode min = mH[1];
                HeapNode lastNode = mH[currentSize];
//            update the indexes[] and move the last node to the top
                indexes[lastNode.vertex] = 1;
                mH[1] = lastNode;
                mH[currentSize] = null;
                sinkDown(1);
                currentSize--;
                return min;
            }

            public void sinkDown(int k) {
                int smallest = k;
                int leftChildIdx = 2 * k;
                int rightChildIdx = 2 * k + 1;
                if (leftChildIdx < heapSize() && mH[smallest].key > mH[leftChildIdx].key) {
                    smallest = leftChildIdx;
                }
                if (rightChildIdx < heapSize() && mH[smallest].key > mH[rightChildIdx].key) {
                    smallest = rightChildIdx;
                }
                if (smallest != k) {

                    HeapNode smallestNode = mH[smallest];
                    HeapNode kNode = mH[k];

                    //swap the positions
                    indexes[smallestNode.vertex] = k;
                    indexes[kNode.vertex] = smallest;
                    swap(k, smallest);
                    sinkDown(smallest);
                }
            }

            public void swap(int a, int b) {
                HeapNode temp = mH[a];
                mH[a] = mH[b];
                mH[b] = temp;
            }

            public boolean isEmpty() {
                return currentSize == 0;
            }

            public int heapSize() {
                return currentSize;
            }
        }
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



    public static void main(String[] args) {



        for (int i =1 ; i<11 ; i++){ // to do all the 10 cases
            System.out.print(" Case "+i+"----------------------             ");


            Graph g =  make_graph(i );
            // System.out.println(graph1.adjacencylist.length);

            long startTime_priority = System.nanoTime();
            g.prims_mst(); // call priority-queue Prim
            long endTime_priority   = System.nanoTime();
            long totalTime_priority = endTime_priority - startTime_priority;


            System.out.println("**************");
            long startTime_minheap = System.nanoTime();
            g.primMST(g); // call min-heap Prim
            long endTime_minheap    = System.nanoTime();
            long totalTime_minheap  = endTime_minheap  - startTime_minheap ;

            System.out.println("\n Time in priority-queue Prim : "+ totalTime_priority / 1000000+ "  milliseconds   , Time in min heap Prim : "+ totalTime_minheap /1000000+" milliseconds  \n\n");

        }

    }


    public static Graph make_graph(int choice ) {

        switch(choice) {

            case 1: // •	n=1000 and m=10000

                //**************************************************************
                System.out.println(" n=1000 and m=10000                           ....,");
                int V1 = 1000; // number of node
                Graph graph1 = new Graph(V1);

                for (int i=0 ; i<10000 ; i++){

                    int src = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int dest = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<1000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph1.addEdge( i, dest, weight);
                        //     System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with self
                            dest++ ;

                        graph1.addEdge( src, dest, weight);
                        //  System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }
                System.out.println("\n\n");
                return graph1;

            //**************************************************************

            case 2: // •	n=1000 and m= 15000

                //**************************************************************
                System.out.println(" n=1000 and m= 15000                   ....,\n");
                int V2 = 1000; // number of node
                Graph graph2 = new Graph(V2);

                for (int i=0 ; i<15000 ; i++){

                    int src = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int dest = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int weight =1+ (int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<1000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph2.addEdge( i, dest, weight);
                        //  System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph2.addEdge( src, dest, weight);
                        // System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }

                return graph2;
            //**************************************************************

            case 3:// •	n=1000 and m= 25000

                //**************************************************************
                System.out.println(" n=1000 and m=25000                   ....,\n");
                int V3 = 1000; // number of node
                Graph graph3 = new Graph(V3);

                for (int i=0 ; i<25000 ; i++){

                    int src = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int dest = (int)  (Math.random() * 1000); // Generate random number between 0 to 1000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<1000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph3.addEdge( i, dest, weight);
                        // System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph3.addEdge( src, dest, weight);
                        // System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }

                return graph3;
            //**************************************************************

            case 4: //•	n=5000 and m=15000

                //**************************************************************
                System.out.println(" n=5000 and m=15000                   ....,\n");
                int V4 = 5000; // number of node
                Graph graph4 = new Graph(V4);

                for (int i=0 ; i<15000 ; i++){

                    int src = (int)  (Math.random() * 5000); // Generate random number between 0 to 5000
                    int dest = (int)  (Math.random() * 5000); // Generate random number between 0 to 5000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<5000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph4.addEdge( i, dest, weight);
                        //    System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph4.addEdge( src, dest, weight);
                        // System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }

                return graph4;
            //**************************************************************

            case 5:// •	n=5000 and m= 25000

                //**************************************************************
                System.out.println(" n=5000 and m= 25000                   ....,\n");
                int V5 = 5000; // number of node
                Graph graph5 = new Graph(V5);

                for (int i=0 ; i<25000 ; i++){

                    int src = (int)  (Math.random() * 5000); // Generate random number between 0 to 5000
                    int dest = (int)  (Math.random() * 5000); // Generate random number between 0 to 5000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<5000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph5.addEdge( i, dest, weight);
                        // System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph5.addEdge( src, dest, weight);
                        // System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }

                return graph5;
            //**************************************************************

            case 6:// •	n=10000 and m=15000

                //**************************************************************
                System.out.println(" n=10000 and m=15000                   ....,\n");
                int V6 = 10000; // number of node
                Graph graph6 = new Graph(V6);

                for (int i=0 ; i<15000 ; i++){

                    int src = (int)  (Math.random() * 10000); // Generate random number between 0 to 10000
                    int dest = (int)  (Math.random() * 10000); // Generate random number between 0 to 10000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<10000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph6.addEdge( i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;
                        graph6.addEdge( src, dest, weight);
                        //System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }

                return graph6;
            //**************************************************************

            case 7:// •	n=10000 and m= 25000

                //**************************************************************
                System.out.println(" n=10000 and m= 25000                   ....,\n");
                int V7 = 10000; // number of node
                Graph graph7 = new Graph(V7);

                for (int i=0 ; i<25000 ; i++){

                    int src = (int)  (Math.random() * 10000); // Generate random number between 0 to 10000
                    int dest = (int)  (Math.random() * 10000); // Generate random number between 0 to 10000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<10000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;
                        graph7.addEdge( i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;
                        graph7.addEdge( src, dest, weight);
                        //System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }

                return graph7;
            //**************************************************************

            case 8: //•	n=20000 and m=200000

                //**************************************************************
                System.out.println(" n=20000 and m=200000                   ....,\n");
                int V8 = 20000; // number of node
                Graph graph8 = new Graph(V8);

                for (int i=0 ; i<200000 ; i++){

                    int src = (int)  (Math.random() * 20000); // Generate random number between 0 to 20000
                    int dest = (int)  (Math.random() * 20000); // Generate random number between 0 to 20000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<20000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph8.addEdge( i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;
                        graph8.addEdge( src, dest, weight);
                        //System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }

                return graph8;
            //**************************************************************

            case 9:// •	n=20000 and m= 300000

                //**************************************************************
                System.out.println(" n=20000 and m= 300000                   ....,\n");
                int V9 = 20000; // number of node
                Graph graph9 = new Graph(V9);

                for (int i=0 ; i<300000 ; i++){

                    int src = (int)  (Math.random() * 20000); // Generate random number between 0 to 20000
                    int dest = (int)  (Math.random() * 20000); // Generate random number between 0 to 20000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<20000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;
                        graph9.addEdge( i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;
                        graph9.addEdge( src, dest, weight);
                        //System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }

                return graph9;
            //**************************************************************

            case 10:// •	n=50000 and m=1,000,000

                //**************************************************************
                System.out.println(" n=50000 and m=1,000,000                   ....,\n");
                int V10 = 50000; // number of node
                Graph graph10 = new Graph(V10);

                for (int i=0 ; i<1000000 ; i++){

                    int src = (int)  (Math.random() * 50000); // Generate random number between 0 to 50000
                    int dest = (int)  (Math.random() * 50000); // Generate random number between 0 to 50000
                    int weight = 1+(int)  (Math.random() * 9); // Generate random number between 0 to 10

                    if (i<50000){ // to be sure all node is concted
                        if (i==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph10.addEdge( i, dest, weight);
                        //System.out.println("("+i+","+ dest+")"+" weight :"+weight);
                    }else{
                        if (src==dest)// to be sure no node have edge with himself
                            dest++ ;

                        graph10.addEdge( src, dest, weight);
                        //System.out.println("("+src+","+ dest+")"+" weight :"+weight);
                    }


                }

                return graph10;
            //**************************************************************



        }


        return null;

    }
}
