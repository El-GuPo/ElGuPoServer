package com.elgupo.elguposerver.postrequester;

import com.elgupo.elguposerver.dataclasses.Category;
import com.elgupo.elguposerver.dataclasses.Event;
import com.elgupo.elguposerver.dataclasses.Place;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
public class EventsByCategoryController {
    @GetMapping("/events-by-category")
    public HashMap<Integer, List<Event>> getEvents (){
        return PostRequester.getEventsByCategories();
    }
}
