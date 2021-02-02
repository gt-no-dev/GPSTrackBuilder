package gps.track;

import java.util.Date;

public class GPSTrackPoint {
    private final double latitude;
    private final double longitude;
    private double height;
    private final Date date;

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Date getDate() {
        return this.date;
    }


    public long getTime() {
        return this.date.getTime();
    }


    public GPSTrackPoint clone() {
        return new GPSTrackPoint(this.latitude, this.longitude, this.height, this.date);
    }


    public GPSTrackPoint(double latitude, double longitude, double height, Date date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.date = date;
    }
}
