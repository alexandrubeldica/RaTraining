package com.endava.petclinic.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvReader {
    private static Properties properties = new Properties();

    static {
        InputStream is = EnvReader.class.getClassLoader().getResourceAsStream("env.properties");

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//metode pt fiecare element din fisierul env
    public static String getBaseUri() {
        return properties.getProperty("baseUri");
    }

    public static String getBasePath() {
        return properties.getProperty("basePath");
    }

    public static Integer getPort() {
        return Integer.parseInt(properties.getProperty("port")); //aduce string, dar noi vrem integer
    }

}
