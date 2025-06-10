package com.elgupo.elguposerver.database;

import com.elgupo.elguposerver.database.services.LikeEventService;
import com.elgupo.elguposerver.database.services.LikeUserService;
import com.elgupo.elguposerver.dataclasses.Event;
import com.elgupo.elguposerver.postrequester.ActualEventsHolder;
import com.elgupo.elguposerver.postrequester.PostRequester;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LikesCleaner {
    private static final Duration REFRESH_INTERVAL = Duration.ofHours(24);
    private static final LikeUserService likeUserService = new LikeUserService();

    public LikesCleaner() {
        Thread updater = new Thread(this::updateLoop, "LikesTableCleaner");
        updater.setDaemon(true);
        updater.start();
    }

    private void updateLoop() {
        while (true) {
            try {
                Thread.sleep(REFRESH_INTERVAL.toMillis());
                List<Long> tableEvents = likeUserService.getDistinctEvents();
                HashMap<Integer, List<Event>> actualEvents = PostRequester.getEventsByCategories();
                List<Long> allEvents = new ArrayList<>();
                for (Integer cat : actualEvents.keySet()) {
                    for (Event event : actualEvents.get(cat)) {
                        allEvents.add(Long.valueOf(event.getId()));
                    }
                }
                for (Long event : tableEvents) {
                    if (!allEvents.contains(event)) likeUserService.deleteLikeUsers(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
