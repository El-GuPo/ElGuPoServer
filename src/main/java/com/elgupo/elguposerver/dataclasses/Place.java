package com.elgupo.elguposerver.dataclasses;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@Getter
public class Place {
    private final Integer id;
    private final String name;
    private final Double latitude;
    private final Double longitude;
    private final String adress;
    ArrayList<Event> events = new ArrayList<Event>();

    public Place(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("id");
        name = jsonObject.getString("name");
        latitude = jsonObject.getDouble("latitude");
        longitude = jsonObject.getDouble("longitude");
        adress = jsonObject.getString("address");
    }

    public void addEvent(Event event) {
        events.add(event);
    }
}
