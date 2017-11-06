public class MappedNode {
    /**
     *  Clasa de noduri speciale pentru a fi aplicat
     *  algoritmul Dijkstra pe ele.
     */

    /** Nodul respectiv */
    private Node node;

    /** Costul minim pana la nodul respectiv */
    private int cost;

    public MappedNode(Node node) {
        this.node = node;
        this.cost = -1;
    }

    public Node getNode() {
        return node;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void printMappedNode() {
        System.out.println("(" + node.toString() + ", " + node.getCode() + ") : " + cost);
    }
}
