//Object for Cell phone towers created...

public class Tower {
    //Name, latitude, longitude, and frequency is stored for each tower...
    String name;
    double lat;
    double lon;
    int frequency;

    //Constructor
    public Tower(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        frequency = 0;
    }

    //Getters and setters for each attribute...
    //Start
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    //End
}
