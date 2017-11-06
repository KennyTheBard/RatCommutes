public class Edge {

    private Node n1, n2;

    public static final int SPEED = 650; /** in metri pe minut */

    private int distance; /** in metri */

    public Edge(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public Edge(Node n1, Node n2, int distance) {
        this.n1 = n1;
        this.n2 = n2;
        this.distance = distance;
    }

    /**
     *  Metoda de obtinere a nodului de la celalalt
     *  capat al muchiei respective.
     * @param n ~ nodul respectiv.
     * @return Nodul opus.
     */
    public Node getOtherNode(Node n) {
        if(n.getCode() == n1.getCode()) return n2;
        else return n1;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * @return costul muchiei pentru viteza
     *         de 650 m/min.
     */
    public int getCostDrive() {
        int cost = distance / SPEED;
        return cost;
    }

}
