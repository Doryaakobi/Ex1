package ex1.src;

import java.io.*;
import java.util.*;

/**
 * This class implements the weighted_graph_algorithms interface for Undirected (positive) Weighted Graph Theory algorithm.
 * The WGraph_ALgo class implements the methods for the WGraph_Ds using the Dijkstra algorithm in order to
 * find the shortest path between two vertices inside a graph.
 * The Save and Load methods are used to save a graph to a sort of file type and to load a graph back reconstructing it back.
 * In order to use the Dijkstra algorithm the nodeDistComp determines which path is "shorter" to continue.
 */

public class WGraph_Algo implements weighted_graph_algorithms, Serializable {

    private weighted_graph weighted_graph;
    private nodeDistComp comparator;


    public WGraph_Algo() {
        this.weighted_graph = new WGraph_DS();
        comparator = new nodeDistComp();
    }

    /**
     * Initializing on which graph the algorithm will be performed.
     *
     * @param g - the graph to initialize
     */

    @Override
    public void init(weighted_graph g) {
        this.weighted_graph = g;
        comparator = new nodeDistComp();
    }

    @Override
    public weighted_graph getGraph() {
        return this.weighted_graph;
    }

    /**
     * Computing a deep copy of a graph by creating a new set of vertices using the copyN function below
     * due to the reason the "Node" class is an inner class and adding them to the new graph that's being constructed.
     * after all of the node_info had been copied and added to the new graph for each node_info in the graph its neighbors are
     * connected to it.
     * This method runs in O(V+E).
     * @return The new copied WGraph_DS, or an empty WGraph_DS if there was nothing to copy.
     */

    @Override
    public weighted_graph copy() {
        weighted_graph cGraph = new WGraph_DS();
        if (this.weighted_graph != null) {
            for (node_info toBeCopied : this.weighted_graph.getV()) {
                copyN(cGraph, toBeCopied);
            }

            for (node_info cNode : this.weighted_graph.getV()) {
                for (node_info neighbor : this.weighted_graph.getV(cNode.getKey())) {
                    cGraph.connect(neighbor.getKey(), cNode.getKey(), this.weighted_graph.getEdge(neighbor.getKey(), cNode.getKey()));

                }

            }
            return cGraph;
        }
        return cGraph;
    }

    private void copyN(weighted_graph g, node_info toCopy) {
        g.addNode(toCopy.getKey());
        g.getNode(toCopy.getKey()).setInfo(toCopy.getInfo());
        g.getNode(toCopy.getKey()).setTag(toCopy.getTag());
    }

    /**
     * This method returns true if and only if there's a path between all of the vertices
     * inside the graph using the BFS algorithm to explore the graph.
     * @return true if the graph is fully connected.
     */
    @Override
    public boolean isConnected() {
        resetTag();
        int visited = 1;
        if (weighted_graph.getV().size() == 1) return true;

        Queue<node_info> queue = new LinkedList<>();
        node_info arbitraryNode = getArbitraryNode();
        if (arbitraryNode == null) return true;
        queue.add(arbitraryNode);
        arbitraryNode.setTag(visited);

        if (weighted_graph.getV(arbitraryNode.getKey()).size() == 0) return false;
        while (!queue.isEmpty()) {
            node_info curr = queue.poll();

            for (node_info node : weighted_graph.getV(curr.getKey())) {
                if (node.getTag() != visited) {
                    node.setTag(visited);
                    queue.add(node);
                }
            }

        }
        for (node_info n : weighted_graph.getV()) {
            if (n.getTag() != visited) return false;
        }

        return true;
    }

    /**
     * Used to retain a vertex in the graph to begin the BFS algorithm from.
     * @return an arbitrary node_info from the graph, null if there was no node added to the graph.
     */

    private node_info getArbitraryNode() {
        for (node_info n : this.weighted_graph.getV()) {
            return n;
        }
        return null;
    }

    /**
     * Setting the tags to the maximum value of Double both to be used in the BFS algorithm and in the Dijkstra
     * algorithm to find the shortest path.
     */

    private void resetTag() {
        for (node_info n : this.weighted_graph.getV()) {
            n.setTag(Double.MAX_EXPONENT);
        }
    }

    /**
     * Returns the shortest "distance" between two vertices in the graph by the sum of the edges between them.
     * the distance of a vertex to himself is 0.
     * @param src - start node
     * @param dest - end (target) node
     * @return - the sum of the edges between the nodes and -1 if there's no path.
     */


    @Override
    public double shortestPathDist(int src, int dest) {
        if (src == dest) return 0;
        if (weighted_graph.getNode(src) == null || weighted_graph.getNode(dest) == null) return -1;
        List<node_info> path = shortestPath(src, dest);
        double ans = path.get(path.size() - 1).getTag();

        return ans;
    }

    /**
     * This method explore the graph using the Dijkstra algorithm to find the shortest distance. First all of the
     * vertices tags are reset to be "infinity" and the distance from the src vertex to himself to be 0.
     * Then the unvisited neighbors of the src vertex are added to the priority queue (which are then rearranged in the queue to
     * determine which one is the closest in order to continue in it's path first), for each vertex if the known distance (it's tag) is bigger than the
     * distance calculated from the src vertex to him ,the tag is set to be that distance. At that point previousN list is updated in order to build the shortest path.
     * after that the vertex is added to the visited list so that we won't go that path again.
     * @param src - start node
     * @param dest - end (target) node
     * @return List<node_info> representing the path / empty list if the src==dest / null if there's no node_info in the graph.
     */

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        resetTag();
        PriorityQueue<node_info> queue = new PriorityQueue<>(comparator);
        List<node_info> visitedNodes = new ArrayList<>();
        HashMap<Integer, node_info> previousN = new HashMap<>();
        if (src == dest) return visitedNodes;
        if (weighted_graph.getNode(src) == null || weighted_graph.getNode(dest) == null) return null;
        weighted_graph.getNode(src).setTag(0);
        queue.add(weighted_graph.getNode(src));
        while (!(queue.isEmpty())) {
            node_info srcN = queue.poll();
            for (node_info neighbor : weighted_graph.getV(srcN.getKey())) {
                if (!(visitedNodes.contains(neighbor))) {
                    double edgeW = weighted_graph.getEdge(srcN.getKey(), neighbor.getKey());
                    if (neighbor.getTag() > edgeW + srcN.getTag()) {
                        neighbor.setTag(edgeW + srcN.getTag());
                        previousN.put(neighbor.getKey(), srcN);
                        queue.add(neighbor);
                    }// to update the current "distance" from the src vertex.

                }// in order to check if the vertex had been visited already.
            }// Visiting the current vertex neighbors
            visitedNodes.add(srcN);// after adding all of his neighbors we mark it as visited.
        }
        return getPath(previousN, src, dest);

    }// shortestPath

    /**
     * This method returns the path by the related node_info that was previous to the dest vertex.
     * @param previousN - the HashMap containing the information of the previous node leading to the dest.
     * @param src - the src node
     * @param dest - the dest node
     * @return The List<node_info> containing the vertices in the path.
     */

    private List<node_info> getPath(HashMap<Integer, node_info> previousN, int src, int dest) {
        List<node_info> path = new ArrayList<>();
        node_info prev = weighted_graph.getNode(dest);
        path.add(prev);
        while (!(path.contains(previousN.get(src)))) {
            path.add(previousN.get(prev.getKey()));
            prev = previousN.get(prev.getKey());
            if (prev.getKey() == src) break;

        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Saves this weighted (undirected) graph to the given file name.
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved.
     */

    @Override
    public boolean save(String file) {
        ObjectOutputStream oos;
        try {
            FileOutputStream fout = new FileOutputStream(file);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this.getGraph());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * loading a graph to this graph algorithm. If the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */

    @Override
    public boolean load(String file) {
        try {
            FileInputStream streamIn = new FileInputStream(file);
            ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
            weighted_graph createdGraph = (WGraph_DS) objectinputstream.readObject();

            this.init(createdGraph);

            streamIn.close();
            objectinputstream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}//WGraph_Algo

class nodeDistComp implements Comparator<node_info> {

    @Override
    public int compare(node_info o1, node_info o2) {
        return (int) (o1.getTag() - o2.getTag());
    }
}





