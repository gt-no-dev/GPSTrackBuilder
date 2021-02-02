package gps.track;

import java.util.List;
import java.util.Objects;

public class Builder {
    public static void Perform(BuilderArgs args, BuilderOutArgs outArgs1, BuilderOutArgs outArgs2) {
        GPSTrack track = new GPSTrack(
                args.getSourceFileName(), args.getTrackName(), args.getDescription());

        List<GPSTrackPoint> rev1Points = track.getTrackWithDeviation(
                outArgs1.getDateFrom(),
                outArgs1.getDateTo(),
                outArgs1.getMaxDeviation(),
                args.getHeight(),
                args.getHeightDeviation());

        List<GPSTrackPoint> rev2Points = GPSTrack.createTrackWithDeviation(rev1Points,
                outArgs2.getDateFrom(),
                outArgs2.getDateTo(),
                outArgs2.getMaxDeviation(),
                args.getHeight(),
                args.getHeightDeviation());

        List<GPSTrackPoint> extRev1Points = GPSTrack.getExtendedTrack(rev1Points, args.getPointsPerSection());
        List<GPSTrackPoint> extRev2Points = GPSTrack.getExtendedTrack(Objects.requireNonNull(rev2Points), args.getPointsPerSection());

        GPSTrack.interpolateHeigts(extRev1Points, extRev2Points);

        track.writeGpxFile(outArgs1.getFileName(), extRev1Points);
        track.writeGpxFile(outArgs2.getFileName(), extRev2Points);
    }
}

