package com.elgupo.elguposerver.postrequester;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.elgupo.elguposerver.dataclasses.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostRequester {
    private final static String commonURL = "https://api.afisha7.ru/v3.1/";
    private final static String commonURLParameters = "APIKey=bdea8eea2845195c0870&org=1048&token=387b59015b331a2c38746513941261ee&loc_url=sankt-peterbyrg&loc_id=203";

    public static List<Place> getPlacesNearby(Double latitude, Double longitude, int count, int radius) throws IOException, JSONException {
        StringBuilder response = getStringBuilder("places/", "&cat_id=11");

        JSONArray data = (new JSONObject(response.toString())).getJSONArray("places");

        ArrayList<Place> places = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            try {
                places.add(new Place(data.getJSONObject(i)));
            } catch(JSONException e) {
                continue;
            }
        }
        places = (ArrayList<Place>) places.stream()
                .filter(p -> Math.pow(p.getLatitude() - latitude, 2) + Math.pow(p.getLongitude() - longitude, 2) < radius)
                .collect(Collectors.toList());
        places.sort(Comparator.comparingDouble(p ->
                Math.pow(p.getLatitude() - latitude, 2) + Math.pow(p.getLongitude() - longitude, 2)));
        List<Place> result = places.subList(0, Math.min(count, places.size()));
        for (Place place : result) {
            StringBuilder responseEvents = getStringBuilder("evs/", "&places_id=" + place.getId().toString());
            try {
                JSONArray dataEvents = new JSONObject(responseEvents.toString()).getJSONArray("events");
                for (int i = 0; i < dataEvents.length(); i++) {
                    Event event = new Event(dataEvents.getJSONObject(i));
                    place.addEvent(event);
                }
            } catch(JSONException e) {
                continue;
            }
        }
        System.out.println(result);
        return result;
    }

    private static StringBuilder getStringBuilder(String urlAdditional, String parametersAdditional) throws IOException {
        String urlParameters = commonURLParameters + parametersAdditional;

        HttpURLConnection connection = (HttpURLConnection) new URL(commonURL + urlAdditional).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        InputStream responseStream = connection.getInputStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        return response;
    }
}
