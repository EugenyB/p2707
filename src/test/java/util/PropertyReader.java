package util;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class PropertyReader {

    private Properties properties;

    private static PropertyReader instance;

    private PropertyReader() {
        properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader("config.properties"))) {
            properties.load(reader);
        } catch (Exception e) {
            throw new RuntimeException("Error reading properties");
        }
    }

    public static PropertyReader getInstance() {
        if (instance == null) {
            instance = new PropertyReader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
