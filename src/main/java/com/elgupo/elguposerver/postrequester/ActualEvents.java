package com.elgupo.elguposerver.postrequester;

import com.elgupo.elguposerver.dataclasses.Category;
import com.elgupo.elguposerver.dataclasses.Event;
import com.elgupo.elguposerver.dataclasses.Place;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

@Getter
class ActualEvents {
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

    private final List<Place> data;

    public ActualEvents(List<Place> data) {
        this.data = List.copyOf(data);
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
    public static ActualEvents loadNewEvents() throws IOException, InterruptedException {
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
                places.put(place.getId(), place);
            }
        }
        int i = 0;
        List<Map<String, String>> dataEvents = new ArrayList<>();
        Map responseEvents = getStringBuilder("events/", "&limit=0");
        int total = Integer.parseInt((String) responseEvents.getOrDefault("total", "0"));
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
            if (!dataEvent.containsKey("places")) {
                continue;
            }

            List<Map> eventPlaces = (List) dataEvent.get("places");
            for (Map place : eventPlaces) {
                String link = (String) place.getOrDefault("link", "");
                try {
                    String id = (String) link.subSequence(link.lastIndexOf('/') + 1, link.length() - 2);
                    places.get(Integer.parseInt(id)).addEvent(event);
                    event.addAdress(places.get(Integer.parseInt(id)).getAdress());
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
        return new ActualEvents(result);
    }

}
