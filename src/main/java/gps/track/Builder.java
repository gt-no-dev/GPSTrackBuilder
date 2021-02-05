package gps.track;

import java.util.List;

public class Builder {
    public static void Perform(BuilderArgs args) {
        // Загружаем трек
        GPSTrack track = new GPSTrack(args.getInputFileName(), args.getTrackName(), args.getTrackDescription());

        // Создаем новый набор точек
        List<GPSTrackPoint> points = track.getTrackWithDeviation(
                args.getDateFrom(),
                args.getDateTo(),
                args.getMaxDeviation());

        // Изменим количество точек
        points = GPSTrack.resizePoints(points, args.getNewPointsCount());

        // Запишем файл
        track.writeGpxFile(args.getOutputFileName(), points);
    }
}

