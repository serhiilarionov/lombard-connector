package com.export;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Config {
    Properties configFile;

    public Config() {
        configFile = new Properties();

        try {
            File jarPath=new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath=jarPath.getParentFile().getAbsolutePath();
            configFile.load(new FileInputStream(propertiesPath+"/config.properties"));
        } catch (Exception eta) {
            eta.printStackTrace();
        }
    }

    public String getProperty(String key) {
        String value = this.configFile.getProperty(key);
        return value;
    }
}
