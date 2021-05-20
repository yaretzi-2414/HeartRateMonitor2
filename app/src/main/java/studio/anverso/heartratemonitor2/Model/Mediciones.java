package studio.anverso.heartratemonitor2.Model;

public class Mediciones {
    private String date, time, medicion;

    public Mediciones() {
    }

    public Mediciones(String date, String time, String medicion) {
        this.date = date;
        this.time = time;
        this.medicion = medicion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMedicion() {
        return medicion;
    }

    public void setMedicion(String medicion) {
        this.medicion = medicion;
    }
}
