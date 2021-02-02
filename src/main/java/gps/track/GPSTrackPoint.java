package gps.track;

import lombok.Getter;

import java.util.Date;

public class GPSTrackPoint {
    @Getter
    private final double latitude;
    @Getter
    private final double longitude;
    @Getter
    private final double height;
    @Getter
    private final Date date;

    public GPSTrackPoint(double latitude, double longitude, double height, Date date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.date = date;
    }
}
