package org.example.app.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

public class LogConfig {
    public static void configureLogging() {
        try {
            FileInputStream configFile = new FileInputStream("mylogs.properties");
            LogManager.getLogManager().readConfiguration(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}