package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {

    private static Random _rnd = null;

    @Test
    void nodeSize() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(1);

        g.removeNode(2);
        g.removeNode(1);
        g.removeNode(1);
        int s = g.nodeSize();
        assertEquals(1, s);

    }

    @Test
    void edgeSize() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        g.connect(0, 1, 1);
        int e_size = g.edgeSize();
        assertEquals(3, e_size);
        double w03 = g.getEdge(0, 3);
        double w30 = g.getEdge(3, 0);
        assertEquals(w03, w30);
        assertEquals(w03, 3);
    }

    @Test
    void getV() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        g.connect(0, 1, 1);
        Collection<node_info> v = g.getV();
        Iterator<node_info> iter = v.iterator();
        while (iter.hasNext()) {
            node_info n = iter.next();
            assertNotNull(n);
        }
    }

    @Test
    void hasEdge() {
        int v = 10, e = v * (v - 1) / 2;
        weighted_graph g = graph_creator(v, e, 1);
        for (int i = 0; i < v; i++) {
            for (int j = i + 1; j < v; j++) {
                boolean b = g.hasEdge(i, j);
                assertTrue(b);
                assertTrue(g.hasEdge(j, i));
            }
        }
    }

    @Test
    void connect() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        g.removeEdge(0, 1);
        assertFalse(g.hasEdge(1, 0));
        g.removeEdge(2, 1);
        g.connect(0, 1, 1);
        double w = g.getEdge(1, 0);
        assertEquals(w, 1);
    }


    @Test
    void removeNode() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        g.removeNode(4);
        g.removeNode(0);
        assertFalse(g.hasEdge(1, 0));
        int e = g.edgeSize();
        assertEquals(0, e);
        assertEquals(3, g.nodeSize());
    }

    @Test
    void removeEdge() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(0, 3, 3);
        g.removeEdge(0, 3);
        double w = g.getEdge(0, 3);
        assertEquals(w, -1);
    }


    ///////////////////////////////////

    /**
     * Generate a random graph with v_size nodes and e_size edges
     *
     * @param v_size
     * @param e_size
     * @param seed
     * @return
     */
    public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();
        _rnd = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while (g.edgeSize() < e_size) {
            int a = nextRnd(0, v_size);
            int b = nextRnd(0, v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i, j, w);
        }
        return g;
    }

    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0 + min, (double) max);
        int ans = (int) v;
        return ans;
    }

    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max - min;
        double ans = d * dx + min;
        return ans;
    }

    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     *
     * @param g
     * @return
     */
    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for (int i = 0; i < size; i++) {
            ans[i] = nodes[i].getKey();
        }
        Arrays.sort(ans);
        return ans;
    }

    /**
     * edges test create a small graph and connect edges while assigning the weight of them and then updating certain edges weight
     * to confirming that if the edge had already existed then just the updated weight should be assigned.
     */

    @Test
    void edges() {
        weighted_graph g0 = graph_creator(11, 0, 1);
        g0.connect(0, 1, 1);
        g0.connect(0, 2, 2);
        g0.connect(0, 3, 3);

        g0.connect(1, 4, 17);
        g0.connect(1, 5, 1);
        g0.connect(2, 4, 1);
        g0.connect(3, 5, 10);
        g0.connect(3, 6, 100);
        assertEquals(100, g0.getEdge(6, 3));
        g0.connect(6, 3, 2.5);
        assertEquals(2.5, g0.getEdge(3, 6));
        g0.connect(5, 7, 1.1);
        g0.connect(6, 7, 10);
        g0.connect(7, 10, 2);
        g0.connect(6, 8, 30);
        g0.connect(8, 10, 10);
        g0.connect(4, 10, 30);
        g0.connect(3, 9, 10);
        assertEquals(10, g0.getEdge(8, 10));
        //updating the weight of the edge between 8<-->10
        g0.connect(8, 10, 1);
        assertEquals(1, g0.getEdge(8, 10));

    }

    @Test
    void largeGraph(){
        weighted_graph g0 = graph_creator(21, 0, 1);

        g0.connect(0, 1, 1);
        g0.connect(0, 2, 2);
        g0.connect(0, 3, 3);
        g0.connect(1, 4, 17);
        g0.connect(1, 5, 1);
        g0.connect(2, 4, 1);
        g0.connect(3, 5, 10);
        g0.connect(3, 6, 100);
        g0.connect(5, 7, 1.1);
        g0.connect(6, 7, 10);
        g0.connect(7, 10, 2);
        g0.connect(6, 8, 30);
        g0.connect(8, 10, 10);
        g0.connect(4, 10, 30);
        g0.connect(3, 9, 10);
        g0.connect(11, 0, 11);
        g0.connect(1, 16, 4);
        g0.connect(4, 20, 4);
        g0.connect(7, 20, 3.5);
        g0.connect(13, 14, 7.5);
        g0.connect(13, 15, 1.1);
        assertFalse(g0.hasEdge(14,19));
        assertTrue(g0.hasEdge(13,14));
        g0.connect(14,19,5);
        assertTrue(g0.hasEdge(14,19));
        g0.connect(18,19,1.7);
        g0.connect(15,18,18);
        g0.connect(13,17,17);
        g0.connect(15,17,-50); // weight of an edge has to be positive
        assertFalse(g0.hasEdge(15,17));
        g0.removeNode(0); // removing one vertex from the graph should remove it from its neighbors as well.
        assertFalse(g0.hasEdge(0,1));
        assertFalse(g0.hasEdge(0,2));
        assertFalse(g0.hasEdge(0,11));

    }

}

//
//    @Test
//    void hasEdge() {
//
//    }
//
//    @Test
//    void getEdge() {
//    }

//    public static weighted_graph myGraph(){
//        weighted_graph g = new WGraph_DS();
//        for (int i = 0; i <10 ; i++) {
//            g.addNode(i);
//        }
//        g.connect(1,2,1);
//        g.connect(2,4,1);
//        g.connect(3,5,2);
//        g.connect(4,5,1);
//        g.connect(4,6,1);
//        g.connect(6,7,2);
//        g.connect(6,7,1);
//        g.connect(6,8,1);
//        g.connect(8,9,1);
//        g.connect(0,9,1);
//
////        g.removeNode(6);
////        g.addNode(6);

//        return g;


//Ds_test