import java.util.ArrayList;

public class TransportService {

    private ArrayList<Line> lines;

    public TransportService() {
        lines = new ArrayList<>();
    }

    public void addRoute(Line line) {
        lines.add(line);
    }

    /**
     * @param nr_line ~ numarul liniei cautate.
     * @return Instanta acesteia.
     */
    public Line getLine(int nr_line) {
        for(int i = 0; i < lines.size(); i++) {
            if(lines.get(i).getNr() == nr_line) {
                return lines.get(i);
            }
        }
        return null;
    }
}
