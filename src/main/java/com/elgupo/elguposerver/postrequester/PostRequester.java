package com.elgupo.elguposerver.postrequester;
import java.io.*;
import java.net.http.*;
import java.net.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import ch.qos.logback.core.joran.sanity.Pair;
import com.elgupo.elguposerver.dataclasses.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.StackTrace;
import org.json.*;
import com.elgupo.elguposerver.dataclasses.Place;

import javax.xml.crypto.Data;

public class PostRequester {
    @SuppressWarnings({"unchecked cast", "rawtypes"})
    public static List<Place> getPlacesNearby(Double latitude, Double longitude, int count, Double radius) throws IOException, JSONException, InterruptedException {

        List<Place> result = ActualEventsHolder.getInstance().getEvents().getData();
        return result.stream()
                .filter(p -> Math.pow(p.getLatitude() - latitude, 2) + Math.pow(p.getLongitude() - longitude, 2) < Math.pow(radius, 2))
                .sorted(Comparator.comparingDouble(p ->
                        Math.pow(p.getLatitude() - latitude, 2) + Math.pow(p.getLongitude() - longitude, 2)))
                .limit(count)
                .toList();
    }

    public static HashMap<Integer, List<Event>> getEventsByCategories() {
        List<Place> data = ActualEventsHolder.getInstance().getEvents().getData();
        HashMap<Integer, List<Event>> result = new HashMap<>();
        for (Category cat : Category.CATEGORIES) {
            result.put(cat.getId(), new ArrayList<>());
        }
        result.put(-1, new ArrayList<>());
        for (Place place : data) {
            for (Event event : place.getEvents()) {
                if (!result.containsKey(event.getCatId())) result.get(-1).add(event);
                else result.get(event.getCatId()).add(event);
            }
        }
        return result;
    }
}
