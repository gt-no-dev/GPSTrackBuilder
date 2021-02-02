package gps.track;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class GPSTrackPoint {
    @Getter
    private final double latitude;
    @Getter
    private final double longitude;
    @Getter
    @Setter
    private double height;
    @Getter
    private final Date date;

    public GPSTrackPoint clone() {
        return new GPSTrackPoint(this.latitude, this.longitude, this.height, this.date);
    }

    public GPSTrackPoint(double latitude, double longitude, double height, Date date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.date = date;
    }

    public long getTime() {
        return this.date.getTime();
    }
}
