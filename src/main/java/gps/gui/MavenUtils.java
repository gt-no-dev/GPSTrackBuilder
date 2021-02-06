package gps.gui;

import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MavenUtils {
    public static final String VersionNotFound = "{Версия не определена}";
    public static final String PomFilePath = "pom.xml";
    public static final String PropertiesFilePath = "package.properties";
    public static final String projectVersionPropertyName = "project.version";

    public static String getVersion() {
        try {
            if ((new File(PomFilePath)).exists()) {
                return new MavenXpp3Reader().read(new FileReader(PomFilePath)).getVersion();
            } else {
                Properties properties = new Properties();
                try (InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PropertiesFilePath)) {
                    properties.load(resourceStream);
                }
                return properties.getProperty(projectVersionPropertyName);
            }
        } catch (IOException | XmlPullParserException e) {
            AlertsUtils.showErrorAlert(e, null);
            return VersionNotFound;
        }
    }
}
