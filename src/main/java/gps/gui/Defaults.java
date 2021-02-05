package gps.gui;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Defaults {
    private static final SimpleDateFormat trackNameDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String Title = "Преобразователь GPS-трека";
    public static final Integer Width = 700;
    public static final Integer Height = 400;

    public static final String inputFileName = StringUtils.EMPTY;
    public static final String trackName = trackNameDateFormat.format(new Date());
    public static final String trackDescription = StringUtils.EMPTY;
    public static final String outputFileName = StringUtils.EMPTY;
    public static final LocalDateTime dateTimeStart = LocalDateTime.now();
    public static final LocalDateTime dateTimeFinish = LocalDateTime.now().plusHours(3);

    public static final Integer pointsCountMin = 1;
    public static final Integer pointsCount = 500;
    public static final Integer pointsCountMax = Integer.MAX_VALUE;
    public static final Double deviation = 0.00005;
}
