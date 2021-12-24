package com.pilnyck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Connector {
    public static String getContent(String currentPath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(currentPath).openStream()))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                //stringBuilder.append("\n");
                //System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
