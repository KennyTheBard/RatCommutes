import java.util.ArrayList;

public class Dijkstra {

    /** instante accesate des de catre metodele algorimului */
    private DirectedGraph graph;
    private TransportService ratb;

    /** Id-ul nodurilor de plecare si de sosire */
    private int start, finish;

    /** Valorile cele mai bune obtinute de algoritm */
    private ArrayList<Node> bestRoute;
    private int bestCost;

    /** Totalitatea nodurilor pe care se aplica algorimul */
    private ArrayList<MappedNode> mp;

    /** Momentul pornirii de la statia actuala la cea cautata */
    private Time present;

    public Dijkstra(DirectedGraph graph, int start, int finish, TransportService ratb, Time present) {
        this.graph = graph;
        this.start = start;
        this.finish = finish;
        this.ratb = ratb;
        this.present = present;
        this.bestCost = -1;
        shortestRoute();
    }

    public void shortestRoute() {
        mp = new ArrayList<>();

        mappingNodes();

        selectRoute();
    }

    /**
     * @param node ~ nodul cautat intre cele mapate;
     * @return Corespondentul mapat al nodului cautat
     *         sau null in caz ca acesta nu exista.
     */
    private MappedNode inMappedNodes(Node node) {
        for(MappedNode mNode : mp) {
            if(mNode.getNode().getCode() == node.getCode()) {
                return mNode;
            }
        }
        return null;
    }

    /**
     *  Maparea nodurilor pentru a se putea aplica algoritmul
     *  Dijkstra pe ele, incepand de la nodul cu id-ul plecarii
     *  in care ajunge primul vehicul.
     */
    private void mappingNodes() {

        Queue q = new Queue();

        Node first = graph.getNodeById(start);
        int first_cost = -1;

        /**
           Cautarea nodului cu id-ul nodului de plecare in care
           ajunge un vehicul primul. Acesta devine nodul origine
           al grafului in sensul aplicarii algoritmului.
        */
        for (Node node : graph.getAllNodes(start)) {
            if(first_cost < 0) {
                first_cost = ratb.getLine(node.getLine()).nextArrive(node.getCode(),present).toInt();
                first = node;
            }
            if(ratb.getLine(node.getLine()).nextArrive(node.getCode(),present).toInt() < first_cost) {
                first_cost = ratb.getLine(node.getLine()).nextArrive(node.getCode(),present).toInt();
                first = node;
            }
        }

        MappedNode origin = new MappedNode(first);
        first_cost -= present.toInt();
        origin.setCost(first_cost);
        mp.add(origin);
        q.push(origin);

        while(!q.isEmpty()) {

            /** Parcurgem fiecare nod din partea conexa a graficului legata la first */
            MappedNode father = q.pop();

            for(Node node : father.getNode().getNeighbours()) {

                MappedNode mNode = inMappedNodes(node);

                /** In loc de o verificare de forma isVisited() */
                if(mNode == null) {
                    mNode = new MappedNode(node);
                    mp.add(mNode);
                    q.push(mNode);
                    //System.out.print(node.toString() + " pushed by " + father.getNodeById().toString() + "\n");
                }

                /** Calcularea costului de schimbare al vehiculului */
                if(father.getNode().getEdge(node).getDistance() < 0) {
                    father.getNode().getEdge(node).setDistance(
                            costToChangeVehicle(node.getCode(), node.getLine(), father.getCost()));
                }

                /** Pastrarea distantei optime pentru fiecare nod */
                int cost = father.getNode().getEdge(node).getDistance() + father.getCost() ;
                if (mNode.getCost() < 0 || mNode.getCost() > cost) {
                    mNode.setCost(cost);
                    q.push(mNode);
                }
            }
        }
    }

    /**
     *  Obtinerea costului de aschimba vehiculul actual
     *  cu altul dintre cele care trec prin statia respectiva.
     * @param code ~ codul nodului curent;
     * @param nr_line_to_change ~ numarul liniei pe care vrem sa schimbam;
     * @param current_cost ~ costul pana in prezent al drumului;
     * @return Costul asteptarii urmatorului vehicul care
     *         trece prin statia respectiva.
     */
    private int costToChangeVehicle(int code, int nr_line_to_change, int current_cost) {
        Time cost = new Time(0,0);
        cost.addMinutes(present.toInt());
        cost.addMinutes(current_cost);

        return ratb.getLine(nr_line_to_change).nextArrive(code,cost).toInt() - present.toInt() - current_cost;
    }

    /**
     *  Cautarea rutei minime pana la gasirea unui nod
     *  cu id-ul cautat.
     */
    private void selectRoute() {

        /** Cautarea nodului origine */
        Node first = graph.getNodeById(start);
        int first_cost = -1;

        for (Node node : graph.getAllNodes(start)) {
            if(first_cost < 0) {
                first_cost = ratb.getLine(node.getLine()).nextArrive(node.getCode(),present).toInt();
                first = node;
            }
            if(ratb.getLine(node.getLine()).nextArrive(node.getCode(),present).toInt() < first_cost) {
                first_cost = ratb.getLine(node.getLine()).nextArrive(node.getCode(),present).toInt();
                first = node;
            }
        }

        Stack st = new Stack();
        MappedNode origin = new MappedNode(first);
        st.push(origin);

        Stack aux = new Stack();

         /** Cautarea nodului destinatie */
        int min = -1;
        int target = graph.getNodeById(finish).getCode();
        for(MappedNode mapped : mp) {
            if(mapped.getNode().getId() == finish) {
                if(min == -1) {
                    min = mapped.getCost();
                    target = mapped.getNode().getCode();
                }
                if(min > mapped.getCost()) {
                    min = mapped.getCost();
                    target = mapped.getNode().getCode();
                }
            }
        }
        finish = target;

        /** Cautarea recursiva a unui nod cu id-ul destinatiei */
        checkCurrentRoute(st, aux);

        ArrayList<Node> route = new ArrayList<>();
        MappedNode mNode;
        while (!aux.isEmpty()) {
            mNode = aux.pop();
            route.add(mNode.getNode());
        }

        bestRoute = route;
    }

    /**
     *  Metoda de parcurgere recursiva a grafului mapat
     *  cu valorile costurilorm cautand nodul destinatie.
     * @param st ~ stiva in care se adauga si din care se
     *             scot noduri pana ce se ajunge la unul
     *             cu id-ul final;
     * @param aux ~ stiva in care se adauga ruta curenta
     *              atunci cand este gasit un nod cu id-ul
     *              final;
     */
    private void checkCurrentRoute(Stack st, Stack aux) {
        if(st.peek() == null) {
            return;
        }
        Node node  = st.peek().getNode();

        /**
           Daca se ajunge la un nod cu id-ul final se goleste
           stiva principala si se umple cea auxiliara.
        */
        if(node.getCode() == finish) {
            if(bestCost == -1 || bestCost > st.peek().getCost()) {
                bestCost = st.peek().getCost();
            }
            if(aux.isEmpty()) {
                while (!st.isEmpty()) {
                    aux.push(st.pop());
                }
            }
        }

        /**
           Parcurgerea nodurilor din grafic in cautarea unui drum
           minim spre nodul final definit de expresia:
           cost_nod_actual + cost_muchie = cost_nod_opus_pe_muchie.
        */
        for(Node neigh : node.getNeighbours()) {

            MappedNode mNode = inMappedNodes(neigh);

            if(mNode.getCost() == inMappedNodes(node).getCost()
                                    + node.getEdge(neigh).getDistance()) {
                st.push(mNode);
                checkCurrentRoute(st,aux);
            }

        }
        st.pop();
    }

    /**
     *  Metoda de afisare a rutei optime impreuna cu actiunile aferente
     *  fiecarui moment in timp de pe parcursul acesteia (ajungerea la
     *  o statie, schimbarea unui vehicul, etc).
     */
    public void printBestRoute() {
        if(bestRoute.size()==0) {
            System.out.print("Ruta inexistenta!\n");
        } else if(bestRoute.size()==1) {
            System.out.print("Sunteti deja la destinatia dorita!\n");
        } else {
            Node prev = bestRoute.get(0);
            Node curr;
            System.out.println("La statia " + prev.getName() + " urcati in " + prev.getLine() +
                                " la " + toTime(inMappedNodes(prev).getCost() + present.toInt()));
            for(int i = 1; i < bestRoute.size(); i++) {
                curr = bestRoute.get(i);
                /*
                System.out.println("La " + toTime(inMappedNodes(curr).getCost()
                                    + present.toInt()) + " ajungeti la " + curr.getName());
                */
                if(curr.getId() == prev.getId()) {
                    System.out.println("La statia " + prev.getName()
                            + " luati urmatorul " + curr.getLine() +
                            " la " + toTime(inMappedNodes(curr).getCost() + present.toInt()));
                } else {
                    System.out.println("La " + toTime(inMappedNodes(curr).getCost()
                            + present.toInt()) + " ajungeti la " + curr.getName());
                }
                prev = curr;
            }
            System.out.println("Ajungeti la destinatie la " + toTime(present.toInt() + bestCost));
        }
    }

    /**
     *  Metoda de translatare a costului in puncte in timp
     * @param cost ~ costul de translatat.
     * @return Ora corespondenta costului adunat cu momentul
     *         prezent in format String pentru scriere.
     */
    private String toTime(int cost) {
        Time time = new Time(0,0);
        time.addMinutes(cost);
        return time.toString();
    }
}
