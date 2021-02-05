package gps.gui;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.function.UnaryOperator;

public class TextFormatters {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.###############################");

    public static DecimalFormat getDecimalFormat(){
        return decimalFormat;
    }

    private static final StringConverter<Double> converter = new StringConverter<Double>() {
        @Override
        public String toString(Double value) {
            if (value == null) {
                return StringUtils.EMPTY;
            }
            return decimalFormat.format(value);
        }

        @Override
        public Double fromString(String value) {
            try {
                if (StringUtils.isBlank(value)) {
                    return null;
                }

                return decimalFormat.parse(value).doubleValue();
            } catch (ParseException ex) {
                return null;
            }
        }
    };

    private static final UnaryOperator<TextFormatter.Change> filter = c -> {
        String text = c.getControlNewText();
        try {
            decimalFormat.parse(text).doubleValue();
            return c;
        } catch (ParseException e) {
            return null;
        }
    };

    public static TextFormatter<Double> createTextFormatter() {
        return new TextFormatter<>(converter, 0.0, filter);
    }
}
