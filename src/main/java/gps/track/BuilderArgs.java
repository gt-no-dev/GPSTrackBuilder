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

    public BuilderArgs(String sourceFileName, String trackName, String description, Integer pointsPerSection) {
        this.sourceFileName = sourceFileName;
        this.trackName = trackName;
        this.description = description;
        this.pointsPerSection = pointsPerSection;
    }
}
