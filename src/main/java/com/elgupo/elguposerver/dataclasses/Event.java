package com.elgupo.elguposerver.dataclasses;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class Event {
    private final int id;
    private final String name;
    private final String logo;
    private final int dateStart;
    private final int dateEnd;

    public Event(JSONObject jsonObject) {
        id = jsonObject.getInt("id");
        name = jsonObject.getString("name");
        logo = jsonObject.getString("logo");
        dateStart = jsonObject.getInt("date_start");
        dateEnd = jsonObject.getInt("date_end");
    }
}
