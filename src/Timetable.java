import java.util.ArrayList;

public class Timetable {
    /**
     *  Orele plecarii vehiculelor pe ruta unei linii.
     */

    private ArrayList<Time> schedule;

    public Timetable() {
        schedule = new ArrayList<>();
    }

    public void addVehicle(Time time) {
        schedule.add(time);
    }

    public ArrayList<Time> getSchedule() {
        return schedule;
    }
}
