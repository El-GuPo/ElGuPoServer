package com.elgupo.elguposerver.postrequester;

import com.elgupo.elguposerver.dataclasses.Category;
import com.elgupo.elguposerver.dataclasses.Event;
import com.elgupo.elguposerver.dataclasses.Place;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import static com.elgupo.elguposerver.postrequester.ActualEvents.loadNewEvents;

public class ActualEventsHolder {

    private static final ActualEventsHolder INSTANCE = new ActualEventsHolder();

    private volatile ActualEvents currentEvents;

    private static final Duration REFRESH_INTERVAL = Duration.ofHours(1);

    private ActualEventsHolder() {
        try {
            this.currentEvents = loadNewEvents();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Thread updater = new Thread(this::updateLoop, "ActualEventsUpdater");
        updater.setDaemon(true);
        updater.start();
    }

    public static ActualEventsHolder getInstance() {
        return INSTANCE;
    }

    public ActualEvents getEvents() {
        return currentEvents;
    }

    private void updateLoop() {
        while (true) {
            try {
                Thread.sleep(REFRESH_INTERVAL.toMillis());
                currentEvents = loadNewEvents();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}