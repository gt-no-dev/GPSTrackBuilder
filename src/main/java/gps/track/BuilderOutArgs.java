package gps.track;

import lombok.Getter;

import java.util.Date;

public class BuilderOutArgs {
    @Getter
    private final String fileName;
    @Getter
    private final Date dateFrom;
    @Getter
    private final Date dateTo;
    @Getter
    private final Double maxDeviation;

    public BuilderOutArgs(String fileName, Date dateFrom, Date dateTo, Double maxDeviation) {
        this.fileName = fileName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.maxDeviation = maxDeviation;
    }
}
