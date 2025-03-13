package com.elgupo.elguposerver.postrequester;
import java.io.*;
import java.net.http.*;
import java.net.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import com.elgupo.elguposerver.dataclasses.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.StackTrace;
import org.json.*;
import com.elgupo.elguposerver.dataclasses.Place;

import javax.xml.crypto.Data;

public class PostRequester {
    private final static String commonURL = "https://api.afisha7.ru/v3.1/";
    private final static String commonURLParameters = "APIKey=bdea8eea2845195c0870&org=1048&token=387b59015b331a2c38746513941261ee&loc_url=sankt-peterbyrg&loc_id=203";

    private static Map getStringBuilder(String urlAdditional, String parametersAdditional) throws IOException, InterruptedException {
        URI url = URI.create(commonURL + urlAdditional);
        String urlParameters = commonURLParameters + parametersAdditional;

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(urlParameters))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), Map.class);
    }

    @SuppressWarnings({"unchecked cast", "rawtypes"})
    public static List<Place> getPlacesNearby(Double latitude, Double longitude, int count, int radius) throws IOException, JSONException, InterruptedException {
        ArrayList<Place> places = new ArrayList<>();
        for (Category category : Category.CATEGORIES) {
            Map response = getStringBuilder("places/", "&cat_id=" + category.getId().toString());

            List<Map<String, String>> data = (List<Map<String, String>>) response.get("places");

            for (Map placeData : data) {
                Place place;
                try {
                    place = new Place(placeData);
                } catch (DataFormatException e) {
                    continue;
                }
                places.add(place);
            }
        }

        places = (ArrayList<Place>) places.stream()
                .filter(p -> Math.pow(p.getLatitude() - latitude, 2) + Math.pow(p.getLongitude() - longitude, 2) < Math.pow(radius, 2))
                .collect(Collectors.toList());
        places.sort(Comparator.comparingDouble(p ->
                Math.pow(p.getLatitude() - latitude, 2) + Math.pow(p.getLongitude() - longitude, 2)));
        List<Place> result = places.subList(0, Math.min(count, places.size()));
        for (Place place : result) {
            Map responseEvents = getStringBuilder("evs/", "&places_id=" + place.getId().toString());
            if (!responseEvents.containsKey("events")) {
                continue;
            }
            Event event = null;
            try {
                List<Map<String, String>> dataEvents = (List<Map<String, String>>) responseEvents.get("events");
                for (Map dataEvent : dataEvents) {
                    event = new Event(dataEvent);
                }
            } catch (DataFormatException e) {
                continue;
            }
            place.addEvent(event);
        }
        return result;
    }
}
