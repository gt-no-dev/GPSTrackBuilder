package gps.gui;

import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.ParseException;

public class DoubleSpinnerConverter extends StringConverter<Double> {
    private final DecimalFormat decimalFormat;

    public DoubleSpinnerConverter(String pattern){
        decimalFormat = new DecimalFormat(pattern);
    }

    @Override
    public String toString(Double value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return StringUtils.EMPTY;
        }

        return decimalFormat.format(value);
    }

    @Override
    public Double fromString(String value) {
        try {
            // If the specified value is null or zero-length, return null
            if (value == null) {
                return null;
            }

            value = value.trim();

            if (value.length() < 1) {
                return null;
            }

            // Perform the requested parsing
            return decimalFormat.parse(value).doubleValue();
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
}
