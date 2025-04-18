package com.elgupo.elguposerver.postrequester;

import com.elgupo.elguposerver.dataclasses.Place;
import com.elgupo.elguposerver.postrequester.PostRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
public class PlacesNearbyController {
    @GetMapping("/places-nearby")
    public synchronized List<Place> getPlaces (
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam int count,
            @RequestParam Double radius
    ) throws IOException, InterruptedException, DataFormatException {
        return PostRequester.getPlacesNearby(latitude, longitude, count, radius);
    }
}