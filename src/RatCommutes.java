import java.io.File;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.System.exit;

public class RatCommutes{

    private static DirectedGraph graph = new DirectedGraph();

    private static DirectedGraph aux = new DirectedGraph();

    private static TransportService ratb = new TransportService();

    public static void main(String[] args) {
        RatCommutes Rat = new RatCommutes();
    }

    public RatCommutes() {

        String mapPath = Paths.get(".").toAbsolutePath().normalize().toString();

        File folder = Paths.get(mapPath + File.separator + "maps").toFile();

        File file = getZone(folder);
        for (File f : file.listFiles()) {

            extractData(f);

        }

        /**
           Obtinerea datelor situationale (precum pozitia curenta,
           destinatia dorita si ora actuala) din consola/terminal.
        */
        Scanner scan = new Scanner(System.in);
        int id_start = -1;
        String loc;
        System.out.println("Numele statiei la care sunteti: ");

        while(id_start == -1) {
            loc = scan.nextLine();
            id_start = findStation(loc);

        }

        int id_finish = -1;
        System.out.println("Numele statiei la care doriti sa ajungeti: ");

        while(id_finish == -1) {
            loc = scan.nextLine();
            id_finish = findStation(loc);
        }

        System.out.println("Ora curenta (0-23): ");
        int hour;
        do {
            hour = scan.nextInt();
        } while(hour < 0 || hour > 23);

        System.out.println("Minutul curent (0-59): ");
        int min;
        do {
            min = scan.nextInt();
        } while(min < 0 || min > 59);

        Dijkstra dij = new Dijkstra(graph, id_start, id_finish, ratb, new Time(hour, min));

        dij.printBestRoute();
    }

    /**
     *  Extragerea datelor in formatul necesar in functie de
     *  fisierul din care se face extragerea.
     * @param f ~ fisierul dinc are se extrag datele;
     */
    private static void extractData(File f) {
        try {
            Scanner inf = new Scanner(f);
            int len = Integer.parseInt(inf.next());
            /**
               Repetarea unei rutine diferite (care depinde de fisier)
               de un numar de ori definit la inceputul fiecarui fisier.
            */
            for (int i = 0; i < len; i++) {
                if (f.getName().contains("nodes")) {

                    /** Adaugarea nodurilor in graful auxiliar */
                    String[] name = inf.next().split("_");
                    aux.add(String.join(" ",name));

                }else if (f.getName().contains("routes")) {

                    int nr_curr_line = Integer.parseInt(inf.next());
                    int num_nodes = Integer.parseInt(inf.next());
                    ArrayList<Node> route = new ArrayList<>();
                    int id = Integer.parseInt(inf.next());
                    int[] distances = new int[num_nodes - 1];

                    Node prev = addNewStation(id,aux,nr_curr_line);
                    route.add(prev);

                    Node newNode;
                    int dist, k = 0;
                    num_nodes--;
                    /** Extragerea rutei si distantelor intre statii */
                    do {
                        dist = Integer.parseInt(inf.next());
                        id = Integer.parseInt(inf.next());
                        distances[k] = dist;
                        k++;

                        newNode = addNewStation(id,aux,nr_curr_line);
                        route.add(newNode);

                        graph.bind(prev,newNode,dist);

                        num_nodes --;
                        prev = newNode;
                    } while(num_nodes > 0);

                    int num_departures = Integer.parseInt(inf.next());
                    int min, hour;
                    Timetable table = new Timetable();
                    /** Extragerea orelor plecarii vehiculelor pe lina respectiva */
                    do {
                        hour = Integer.parseInt(inf.next());
                        min = Integer.parseInt(inf.next());
                        table.addVehicle(new Time(hour, min));
                        num_departures--;
                    } while(num_departures > 0);

                    ratb.addRoute(new Line(nr_curr_line, route, distances, table));
                }
            }
            inf.close();
        } catch (Exception ex) {
            System.out.println(f.getName() + " failed.");
            System.out.println(ex.toString());
            exit(1);
        }
    }

    /**
     *  Metoda ce creeaza o copie a unui nod existent deja pe o alta
     *  ruta astfel incat rutele sa fie separate intre ele, intalnindu-se
     *  doar la statiile comune (cu id si nume identice), intre care vor
     *  exista muchii cu un cost variabil in functie de timp (costul
     *  de schimbare a unui vehicul de pe linia curenta, cu altul
     *  de pe o linie cu care este impartita statie, adaugand costul de
     *  asteptare al vehiculului de pe noua linie).
     * @param id ~ id-ul statiei respective
     * @param aux ~ graful orientat auxiliar ce contine cate
     *              un exemplar al fiecarei statii.
     * @param nr_curr_line ~ linia ce o va avea noul nod;
     * @return Nodul copiat si adaugat in graf.
     */
    private static Node addNewStation(int id, DirectedGraph aux, int nr_curr_line) {

        boolean check;
        ArrayList<Node> same_station = new ArrayList<>();

        /** Verificam daca nodul exista deja in graful principal */
        if(graph.getNodeById(id) != null) {
            check = true;
        } else {
            check = false;
        }

        if(check) {
            same_station = graph.getAllNodes(id);
        }

            Node newNode = graph.add(id, aux.getNodeById(id).getName());
            newNode.setLine(nr_curr_line);

        if(check) {
            for (Node node : same_station) {
                /**
                   Daca este o statie comuna se creaza muchii
                   in ambele directii intre fiecare 2 noduri.
                */
                if(newNode.getLine() != node.getLine()) {
                    graph.bind(newNode, node, -1);
                    graph.bind(node, newNode, -1);
                }
            }
        }

        return newNode;
    }

    /**
     * @param folder ~ directorul curent;
     * @return Calea subdirectorul selectat;
     */
    private static File getZone(File folder) {

        int count = countZones(folder);

        return chooseZone(folder,count);

    }

    /**
     * @param folder ~ directorul curent;
     * @return Numarul de subdirectoare ale acestuia.
     */
    private static int countZones(File folder) {
        int count = 0;
        for(File file : folder.listFiles()) {
            count++;
            System.out.println(count + "." + file.getName());
        }
        return count;
    }

    /**
     *  Metoda utilitara de obtinere a sistemului de transport
     *  in comun dorit in cazul existentei mai multora.
     * @param folder ~ directorul curent;
     * @param count ~ numarul de sisteme disponibile
     *                (daca este 1, atunci selectarea se face automat);
     * @return Calea subdirectorului selectat.
     */
    private static File chooseZone(File folder, int count) {
        Scanner scan = new Scanner(System.in);
        int zone = 1;
        if(count != zone) {
            System.out.println("Numarul zonei dorite:");
            do {
                if (zone != 1) {
                    System.out.println("Zona inexistenta! Incercati din nou.");
                }
                zone = scan.nextInt();
            } while (zone <= 0 || zone > count);
        }

        int curr = 0;
        for(File file : folder.listFiles()) {
            curr++;
            if (curr == zone) {
                return file;
            }
        }
        return null;
    }

    /**
     * @param string ~ numele statie cautate;
     * @return Id-ul statiei cu acest nume.
     */
    private static int findStation(String string) {
        for(Node node : aux.getNodes()) {
            if(node.getName().equalsIgnoreCase(string)) {
                return node.getId();
            }
        }
        return -1;
    }

}
