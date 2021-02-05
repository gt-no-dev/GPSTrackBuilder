package gps.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Thread.setDefaultUncaughtExceptionHandler(ExceptionUtils::showError);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("GPSTrackBuilder.fxml")));
        primaryStage.setTitle(Defaults.Title);
        primaryStage.setScene(new Scene(root, Defaults.Width, Defaults.Height));
        primaryStage.setMinHeight(Defaults.Height);
        primaryStage.setHeight(Defaults.Height);
        primaryStage.setMinWidth(Defaults.Width);
        primaryStage.setWidth(Defaults.Width);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
