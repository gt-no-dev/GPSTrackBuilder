package gps.gui;

import gps.track.Builder;
import gps.track.BuilderArgs;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.lang3.StringUtils;
import tornadofx.control.DateTimePicker;

import java.io.File;
import java.text.ParseException;

public class GPSTrackBuilderController {
    @FXML
    public TextField inputFileNameTextField;
    @FXML
    public TextField trackNameTextField;
    @FXML
    public TextField trackDescriptionTextField;
    @FXML
    public TextField outputFileNameTextField;
    @FXML
    public DateTimePicker dateTimeStartDateTimePicker;
    @FXML
    public DateTimePicker dateTimeFinishDateTimePicker;
    @FXML
    public Spinner<Integer> pointsCountSpinner;
    @FXML
    public TextField deviationTextField;

    public void initialize() {
        inputFileNameTextField.setText(Defaults.inputFileName);
        trackNameTextField.setText(Defaults.trackName);
        trackDescriptionTextField.setText(Defaults.trackDescription);
        outputFileNameTextField.setText(Defaults.outputFileName);
        dateTimeStartDateTimePicker.setDateTimeValue(Defaults.dateTimeStart);
        dateTimeFinishDateTimePicker.setDateTimeValue(Defaults.dateTimeFinish);

        SpinnerValueFactory.IntegerSpinnerValueFactory pointsCountSpinnerValueFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) pointsCountSpinner.getValueFactory();
        pointsCountSpinnerValueFactory.setMin(Defaults.pointsCountMin);
        pointsCountSpinnerValueFactory.setMax(Defaults.pointsCountMax);
        pointsCountSpinnerValueFactory.setValue(Defaults.pointsCount);

        pointsCountSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                pointsCountSpinner.increment(0);
            }
        });

        deviationTextField.setTextFormatter(TextFormatters.createTextFormatter());
        deviationTextField.setText(TextFormatters.getDecimalFormat().format(Defaults.deviation));
    }

    private Window getWindow() {
        return inputFileNameTextField.getScene().getWindow();
    }

    public void chooseInputFile() {
        Window window = getWindow();
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            inputFileNameTextField.setText(file.getAbsolutePath());
        }
    }

    public void chooseOutputFile() {
        Window window = getWindow();
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            outputFileNameTextField.setText(file.getAbsolutePath());
        }
    }

    public void createOutputFile() {
        try {
            internalCreateOutputFile();
        } catch (Exception e) {
            AlertsUtils.showErrorAlert(e, getWindow());
        }
    }

    protected void internalCreateOutputFile() throws ParseException {
        BuilderArgs args = new BuilderArgs(
                inputFileNameTextField.getText(),
                trackNameTextField.getText(),
                trackDescriptionTextField.getText(),
                pointsCountSpinner.getValue(),
                outputFileNameTextField.getText(),
                DateTimeUtils.convert(dateTimeStartDateTimePicker.getDateTimeValue()),
                DateTimeUtils.convert(dateTimeFinishDateTimePicker.getDateTimeValue()),
                TextFormatters.getDecimalFormat().parse(deviationTextField.getText()).doubleValue());

        if (StringUtils.isEmpty(args.getInputFileName())){
            throw new IllegalArgumentException("Не указан исходный файл!");
        }

        if (StringUtils.isEmpty(args.getTrackName())){
            throw new IllegalArgumentException("Не указано имя трека!");
        }

        if (args.getNewPointsCount() <= 0){
            throw new IllegalArgumentException("Некорректное количество точек!");
        }

        if (StringUtils.isEmpty(args.getOutputFileName())){
            throw new IllegalArgumentException("Не указан выходной файл!");
        }

        if (args.getDateFrom() == null)
        {
            throw new IllegalArgumentException("Не указана дата и время начала!");
        }

        if (args.getDateTo() == null)
        {
            throw new IllegalArgumentException("Не указана дата и время окончания!");
        }

        if (args.getMaxDeviation() == null || args.getMaxDeviation() <= 0)
        {
            throw new IllegalArgumentException("Некорректное отклонение координат!");
        }

        Builder.Perform(args);

        AlertsUtils.showInfoAlert("Создание файла", "Готово", getWindow());
    }

    protected FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("GPX Files", "*.gpx"));
        return fileChooser;
    }
}
