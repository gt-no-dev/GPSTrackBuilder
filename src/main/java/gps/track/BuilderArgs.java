package gps.track;

import lombok.Getter;

public class BuilderArgs {
    @Getter
    private final String sourceFileName;
    @Getter
    private final String trackName;
    @Getter
    private final String description;
    @Getter
    private final Integer pointsPerSection;
    @Getter
    private final Double height;
    @Getter
    private final Double heightDeviation;

    public BuilderArgs(String sourceFileName, String trackName, String description, Integer pointsPerSection, Double height, Double heightDeviation) {
        this.sourceFileName = sourceFileName;
        this.trackName = trackName;
        this.description = description;
        this.pointsPerSection = pointsPerSection;
        this.height = height;
        this.heightDeviation = heightDeviation;
    }
}
