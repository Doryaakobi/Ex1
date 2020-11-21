package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
/**
 * This class represents an undirected weighted graph with a positive number for the weight of each edge.
 * The vertices representation in the graph are implemented using the node_info interface as an inner class (Node), so that in order to add an vertex to the graph it has to be initialized first
 * and so there's no access to change the  characteristics of it.
 * Each Graph_DS holds a number of fields as listed:
 * HashMap - representing the vertices associated with the graph.
 * HashMap - representing the vertices (the key) and mapped values which are the "neighbors" of the specific vertex and the weight of the edge connecting them.
 */

public class WGraph_DS implements weighted_graph , Serializable {
    private int vertices, edges, Mc;
    private HashMap<Integer, node_info> wGraph;
    private HashMap<Integer, HashMap<node_info, Double>> neighborsListW;

    public WGraph_DS() {
        this.Mc = 0;
        this.edges = 0;
        this.wGraph = new HashMap<>();
        this.neighborsListW = new HashMap<>();

    }

    /**
     * Adding a new vertex to the graph with its unique key.
     * @param key
     */
    @Override
    public void addNode(int key) { //In order to add a vertex to the graph, it cannot contain the same key provided
        if (!(this.wGraph.containsKey(key))) {
            this.wGraph.put(key, new Node(key));
            this.neighborsListW.put(key,new HashMap<>());
            vertices++;
            Mc++;
        }

    }

    /**
     * Returns the specified vertex in the graph by the node id if it exists in the graph.
     * @param key - the node_id
     * @return node_info null if none.
     */

    @Override
    public node_info getNode(int key) {
        if (this.wGraph.containsKey(key)) {
            return this.wGraph.get(key);
        }
        return null;
    }

    /**
     * This method check iff (if and only if) there is an edge between two vertices.
     * @param node1
     * @param node2
     * @return true if each vertex contains the id of the other vertex in the mapped values.
     */

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (node1 == node2) return false;
        return (this.neighborsListW.get(node1).containsKey(getNode(node2)) && this.neighborsListW.get(node2).containsKey(getNode(node1)));
    }
    /**
     * Return the weight of the edge between two vertices (if a connection between them had been established already).
     * In case there's no edge between them returns -1.
     * @param node1
     * @param node2
     * @return double (weight of the edge), -1 if there's no edge.
     */

    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2) ){
            double weight = neighborsListW.get(node1).get(getNode(node2));
            return weight;
        }
        return -1;
    }
    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */

    @Override
    public void connect(int node1, int node2, double w) {
        if (this.wGraph.containsKey(node1) && this.wGraph.containsKey(node2) && !(hasEdge(node1,node2)) && node1!=node2 && w>=0) {
            neighborsListW.get(node1).put(getNode(node2), w);
            neighborsListW.get(node2).put(getNode(node1), w);
            Mc++;
            edges++;
        }
        if (hasEdge(node1,node2)){
            neighborsListW.get(node1).replace(getNode(node2),w);
            neighborsListW.get(node2).replace(getNode(node1),w);
            Mc++;
        }
    }

    /**
     * Returns a shallow copy (pointer) of a collection representing all vertices in
     * the specific graph.
     * @return Collection<node_info>
     */

    @Override
    public Collection<node_info> getV() {
        return this.wGraph.values();
    }

    /**
     * Returns a Collection<node_info> of the vertices connected to the vertex by its id.
     * @param node_id
     * @return Collection<node_info> or an empty collection if there are no neighbors.
     */

    @Override
    public Collection<node_info> getV(int node_id) {
        return this.neighborsListW.get(node_id).keySet();
    }

    /**
     * Deletes the vertex from the graph (by the id given) and all data related to it (neighbors and edges).
     * this method runs in O(n) as all vertices connected to the deleted node_info should be covered.
     * @param key
     * @returnthe node_info removed from the graph, null if it doesn't exist.
     */

    @Override
    public node_info removeNode(int key) {
        if (this.wGraph.containsKey(key)) {
            for (node_info n : wGraph.values()) {
                    removeEdge(n.getKey(), key);
            }
            vertices--;
            return wGraph.remove(key);
        }
        return null;
    }

    /**
     * Deletes the edge connecting to vertices.
     * @param node1
     * @param node2
     */

    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            neighborsListW.get(node1).remove(getNode(node2));
            neighborsListW.get(node2).remove(getNode(node1));
            edges--;
            Mc++;
        }
    }

    /**
     * The number of vertices in the graph.
     * @return
     */

    @Override
    public int nodeSize() {
        return this.vertices;
    }

    /**
     * The number of edges in the graph.
     * @return
     */

    @Override
    public int edgeSize() {
        return this.edges;
    }

    /**
     * The number of changes performed on the graph.
     * @return
     */

    @Override
    public int getMC() {
        return this.Mc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return vertices == wGraph_ds.vertices &&
                edges == wGraph_ds.edges;
//                Mc == wGraph_ds.Mc &&
//                Objects.equals(wGraph, wGraph_ds.wGraph) &&
//                Objects.equals(neighborsListW, wGraph_ds.neighborsListW);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, edges, Mc, wGraph, neighborsListW);
    }

    private class Node implements node_info, Serializable {

        private int key; double tag;
        private String info;


        public Node(int k) {
            this.key = k;
            this.info = "";
            this.tag = 0;
        }


        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }
    }//private class Node implementing node_info


}//WGraph_DS class
