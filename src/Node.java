import java.util.ArrayList;

public class Node {
    /**
     *  Clasa ce retine in fiecare instanta cate o statie de pe traseele liniilor.
     */

    /**  Id-ul statiei reale reprezentate */
    private int id;

    /**  UID-ul nodului */
    private int code;

    /**  Numarul liniei care trece prin nodul respectiv */
    private int line;

    /** Numele statiei reale reprezentate */
    private String station_name;

    /** Drumurile spre alte noduri ce pornesc din acest node */
    private ArrayList<Edge> paths;

    public Node(int id, int code, String station_name) {
        this.id = id;
        this.code = code;
        this.station_name = station_name;
        this.paths = new ArrayList<>();
    }

    /**
     *  Metoda de creare a muchiilor orientate.
     * @param node ~ nodul spre care dorim sa cream aceasta muchie;
     */
    public void createDirectedPath(Node node) {
        Edge edge = new Edge(this, node);
        addPath(edge);
    }

    /**
     *  Metoda de alipire a unei noi muchii nodului.
     * @param edge ~ muchia de alipit;
     */
    public void addPath(Edge edge) {
        paths.add(edge);
    }

    /**
     *  Metoda de returnare a muchiei spre un anumit nod,
     *  din acesta, daca exista.
     * @param node ~ nodul in spre care cautam muchia;
     * @return Muchia cautata sau null in caz ca nu exista.
     */
    public Edge getEdge(Node node) {
        for(Edge edge : paths) {
            if(edge.getOtherNode(this) == node) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String s = "" + id;
        return s;
    }

    public int getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    /**
     *  Metoda de testare a functionalitatii nodului.
     */
    public void printEdges() {
        for(Edge edge : paths) {
            System.out.print(edge.getOtherNode(this) + " ");
        }
    }

    /**
     * @return Un ArrayList de noduri vecine.
     */
    public ArrayList<Node> getNeighbours() {
        ArrayList<Node> nb = new ArrayList<>();
        for(Edge edge : paths) {
            nb.add(edge.getOtherNode(this));
        }
        return nb;
    }

    public String getName() {
        return station_name;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
