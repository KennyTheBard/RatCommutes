import java.util.ArrayList;

public class DirectedGraph {
    /**
     *  Clasa ce retine graful orientat aferent sistemului
     *  de transport in comun reprezentat.
     */

    private ArrayList<Node> nodes;

    public DirectedGraph() {
        this.nodes = new ArrayList<>();
    }

    public Node add(String name) {
        Node node = new Node(nodes.size(), nodes.size(), name);
        nodes.add(node);
        return node;
    }

    public Node add(int id, String name) {
        Node node = new Node(id, nodes.size(), name);
        nodes.add(node);
        return node;
    }

    /**
     *  Metoda de formare a unei legaturi orientate intre
     *  doua noduri ale graficului.
     * @param n1 ~ nodul DINSPRE care se face legatura;
     * @param n2 ~ nodul INSPRE care se face legatura;
     * @param distance ~ costul muchiei noi;
     */
    public void bind(Node n1, Node n2, int distance) {
        n1.createDirectedPath(n2);
        n1.getEdge(n2).setDistance(distance);
    }

    /**
     * @param id ~ id-ul nodului cautat;
     * @return Primul nod cu id-ul respectiv
     *         gasit sau null.
     */
    public Node getNodeById(int id) {
        for(Node node : nodes) {
            if(node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    /**
     * @param id ~ id-ul nodurilor cautate;
     * @return Toate nodurile cu acel id.
     */
    public ArrayList<Node> getAllNodes(int id) {
        ArrayList<Node> all = new ArrayList<>();
        for(Node node : nodes) {
            if(node.getId() == id) {
                all.add(node);
            }
        }
        return all;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     *  Metoda de testare a integritatii graficului.
     */
    public void printNodes() {
        for(Node node : nodes) {
            System.out.print(node.toString() + ": ");
            node.printEdges();
            System.out.println();
        }
    }
}
