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

    public static void main(String[] args) throws IOException, InterruptedException, DataFormatException {
        List<Place> places = getPlacesNearby(0.0, 0.0, 100000000, 10000.0);
        for (Place p : places) {
            System.out.println(p.getId());
        }
    }
}