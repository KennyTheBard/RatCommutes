import java.util.ArrayList;

public class Line {
    /**
     *  Clasa ce retine despre o line de transport in comun date.
     */

    /**
     *  Numarul liniei.
     */
    private int nr;

    /**
     *  Sirul de noduri(statii) care formeaza ruta acesteia.
     */
    private ArrayList<Node> route;

    /**
     *  Distantele dintre fiecare doua statii consecutive de pe ruta liniei.
     */
    private int[] distances;

    /**
     *  Obiectul care retine ora plecarii vehiculelor din captul rutei.
     */
    private Timetable table;

    public Line(int nr, ArrayList<Node> route, int[] distances, Timetable table) {
        this.nr = nr;
        this.route = route;
        this.distances = distances;
        this.table = table;
    }

    public int getNr() {
        return nr;
    }

    /**
     *  Metoda de calcul al costului unui vehicul din capat
     *  pana la o statie anume de pe ruta sa.
     * @param code_station ~ codul statiei(UID-ul nodului);
     * @return Costul capat-statie de pe linia respectiva;
     */
    public int getCostTo(int code_station) {
        int cost = 0;
        /** Iteram elementele rutei pana ce gasim nodul corespunzator statiei */
        for(int i = 0; i < route.size(); i++) {
            //cost += 1; // Asteptarea in statie sa plece vehiculul
            if(route.get(i).getCode() == code_station) {
                break;
            } else {
                cost += distances[i];
            }
        }
        return cost;
    }

    /**
     *  Metoda de determinare a urmatorului vehicul de pe
     *  linia respectiva care va trece printr-o statie
     *  anume, dupa o anumita ora.
     * @param code_station ~ codul statiei pentru care cautam urmatorul vehicul;
     * @param time ~ momentul 0 de la care cautam urmatorul vehicul;
     * @return Ora plecarii urmatorului vehicul din statia pe care o cautam.
     */
    public Time nextArrive(int code_station, Time time) {
        Time arvTime = new Time(0,0);
        for(Time dprtTime : table.getSchedule()) {
            /** Comparam costul drumului plus ora plecarii cu ora actuala */
            if(dprtTime.toInt() + getCostTo(code_station) > time.toInt()) {
                arvTime.addMinutes(dprtTime.toInt() + getCostTo(code_station));
                break;
            }
        }
        if(arvTime.toInt() == 0) {
            arvTime.addMinutes(60*23 + 59);
        }
        return arvTime;
    }

}
