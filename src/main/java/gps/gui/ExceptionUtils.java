package gps.gui;

import javafx.application.Platform;

public class ExceptionUtils {
    public static void showError(@SuppressWarnings("unused") Thread thread, Throwable throwable) {
        if (Platform.isFxApplicationThread()) {
            AlertsUtils.showErrorAlert(throwable, null);
        } else {
            throwable.printStackTrace();
        }
    }
}
