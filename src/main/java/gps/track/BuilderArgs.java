package gps.track;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class BuilderArgs {
    @Getter
    private final String inputFileName;
    @Getter
    private final String trackName;
    @Getter
    private final String trackDescription;
    @Getter
    private final Integer newPointsCount;
    @Getter
    private final String outputFileName;
    @Getter
    private final Date dateFrom;
    @Getter
    private final Date dateTo;
    @Getter
    private final Double maxDeviation;

    public BuilderArgs(String inputFileName, String trackName, String trackDescription, Integer newPointsCount, String outputFileName, Date dateFrom, Date dateTo, Double maxDeviation) {
        this.inputFileName = inputFileName;
        this.trackName = trackName;
        this.trackDescription = trackDescription;
        this.newPointsCount = newPointsCount;
        this.outputFileName = outputFileName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.maxDeviation = maxDeviation;
    }
}
