public class Time {
    /**
     *  Clasa ce reprezinta timpul (in ore si minute).
     */

    private int hour, min;

    public Time(int hour, int min) {
        this.hour = hour;
        this.min = min;
    }

    @Override
    public String toString() {
        return hour + ":" + min;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    /**
     *  Metoda de comparare a doi timpi.
     * @param time ~ ora cu care comparam instanta e timp respectiva;
     * @return False daca timpul introdul este mai mare decat cel al instantei
     *         si true daca este invers;
     */
    public boolean laterThan(Time time) {
        if(this.hour > time.getHour()) {
            return true;
        }
        if(this.min > time.getMin() && this.hour == time.getHour()) {
            return true;
        }
        return false;
    }

    /**
     *  Metoda de transformare a valorilor instantei intr-un numar.
     * @return Numarul de minute si orele transformate in minute al
     *         instantei.
     */
    public int toInt() {
        int time = hour * 60 + min;
        return time;
    }

    /**
     *  Metoda de adaugare minute.
     * @param minutes ~ valoarea pe care dorim sa o adaugam;
     */
    public void addMinutes(int minutes) {
        this.min += minutes;
        this.hour += this.min / 60;
        this.hour = this.hour % 24;
        this.min = this.min % 60;
    }
}
