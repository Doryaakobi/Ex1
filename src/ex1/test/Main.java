package ex1.test;

import ex1.src.WGraph_DS;
import ex1.src.weighted_graph;

public class Main {
    public static void main(String[] args) {
        weighted_graph graph = new WGraph_DS();
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.addNode(5);

        graph.connect(1,2,1);
        System.out.println(graph.getEdge(1,2));
        System.out.println(graph.getEdge(1,5));
    }

}
