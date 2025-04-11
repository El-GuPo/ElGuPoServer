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
    private static String commonURLParameters = "org=1048&loc_url=sankt-peterbyrg&loc_id=203";
    static {
        try (FileReader tokenReader = new FileReader("src/main/java/com/elgupo/elguposerver/postrequester/token.txt")) {
            BufferedReader reader = new BufferedReader(tokenReader);
            commonURLParameters += "&token=" + reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileReader APIKeyReader = new FileReader("src/main/java/com/elgupo/elguposerver/postrequester/APIKey.txt")) {
            BufferedReader reader = new BufferedReader(APIKeyReader);
            commonURLParameters += "&APIKey=" + reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
    public static List<Place> getPlacesNearby(Double latitude, Double longitude, int count, Double radius) throws IOException, JSONException, InterruptedException {
        ArrayList<Place> places = new ArrayList<>();
        TreeSet<Integer> ids = new TreeSet<>();
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
                if (ids.contains(place.getId())) continue;
                ids.add(place.getId());
                places.add(place);
            }
        }

        places = (ArrayList<Place>) places.stream()
                .filter(p -> Math.pow(p.getLatitude() - latitude, 2) + Math.pow(p.getLongitude() - longitude, 2) < Math.pow(radius, 2))
                .sorted(Comparator.comparingDouble(p ->
                        Math.pow(p.getLatitude() - latitude, 2) + Math.pow(p.getLongitude() - longitude, 2)))
                .limit(10)
                .collect(Collectors.toList());
        List<Place> result = new ArrayList<>();
        for (Place place : places) {
            Map responseEvents = getStringBuilder("evs/", "&places_id=" + place.getId().toString());
            if (!responseEvents.containsKey("events")) {
                continue;
            }
            Event event = null;
            List<Map<String, String>> dataEvents = (List<Map<String, String>>) responseEvents.get("events");
            for (Map dataEvent : dataEvents) {
                try {
                    event = new Event(dataEvent);
                } catch (DataFormatException e) {
                    continue;
                }
                place.addEvent(event);
            }
            if (!place.getEvents().isEmpty()) {
                result.add(place);
            }
        }
        return result;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        List<Place> places = getPlacesNearby(55.75, 37.61, 10, 5.0);
        for (Place p : places) {
            System.out.println(p.getEvents().size());
        }
    }
}
