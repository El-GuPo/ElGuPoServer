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
        HashMap<Integer, Place> places = new HashMap<>();
        for (Category category : Category.CATEGORIES) {
            Map response = getStringBuilder("places/", "&cat_id=" + category.getId().toString());

            List<Map<String, String>> data = (List<Map<String, String>>) response.get("places");
            if (data == null) {
                continue;
            }
            for (Map placeData : data) {
                Place place;
                try {
                    place = new Place(placeData);
                } catch (DataFormatException e) {
                    continue;
                }
                if (places.containsKey(place.getId())) continue;
                if (Math.pow(place.getLatitude() - latitude, 2) + Math.pow(place.getLongitude() - longitude, 2) >= Math.pow(radius, 2)) continue;
                places.put(place.getId(), place);
            }
        }
        int i = 0;
        List<Map<String, String>> dataEvents = new ArrayList<>();
        Map responseEvents = getStringBuilder("events/", "&limit=0");
        Integer total = Integer.parseInt((String) responseEvents.getOrDefault("total", "0"));
        while (i * 100 < total) {
            responseEvents = getStringBuilder("events/", "&limit=100&offset=" + i * 100);
            i++;
            if (!responseEvents.containsKey("events") || responseEvents.get("events") == null) {
                break;
            }
            dataEvents.addAll((List) responseEvents.get("events"));
        }
        Event event = null;
        for (Map dataEvent : dataEvents) {
            try {
                event = new Event(dataEvent);
            } catch (DataFormatException e) {
                continue;
            }
            if (!dataEvent.containsKey("places")) continue;
            List<Map> eventPlaces = (List) dataEvent.get("places");
            for (Map place : eventPlaces) {
                String link = (String) place.getOrDefault("link", "");
                try {
                    String id = (String) link.subSequence(link.lastIndexOf('/') + 1, link.length() - 2);
                    places.get(Integer.parseInt(id)).addEvent(event);
                }catch(Exception E) {
                    continue;
                }
            }
        }
        List<Place> result = new ArrayList<>();
        for (Place place : places.values()) {
            if (!place.getEvents().isEmpty()) {
                result.add(place);
            }
        }
        return result.stream().
                sorted(Comparator.comparingDouble(p ->
                        Math.pow(p.getLatitude() - latitude, 2) + Math.pow(p.getLongitude() - longitude, 2)))
                .limit(count)
                .toList();
    }

    public static void main(String[] args) throws IOException, InterruptedException, DataFormatException {
        List<Place> places = getPlacesNearby(0.0, 0.0, 100000000, 10000.0);
        for (Place p : places) {
            System.out.println(p.getId());
        }
    }
}
