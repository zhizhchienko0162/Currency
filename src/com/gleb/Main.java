package com.gleb;

import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Main {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String result = br.lines().collect(Collectors.joining("\n"));
        JSONObject json = (JSONObject) (new JSONObject(result).get("Valute"));

        Map<String, Double> diff = new TreeMap<>();

        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            String key = it.next();

            double current = ((JSONObject) json.get(key)).getDouble("Value");
            double last = ((JSONObject) json.get(key)).getDouble("Previous");

            diff.put(((JSONObject) json.get(key)).getString("Name"), abs(current - last));
        }

        diff.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .forEach(System.out::println);
    }
}
