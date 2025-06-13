package com.elgupo.elguposerver.database;

import com.elgupo.elguposerver.database.services.LikeUserService;
import com.elgupo.elguposerver.dataclasses.Event;
import com.elgupo.elguposerver.postrequester.PostRequester;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LikesCleaner {
    private final LikeUserService likeUserService;

    public LikesCleaner(LikeUserService likeUserService) {
        this.likeUserService = likeUserService;
        Thread updater = new Thread(this::updateLoop, "LikesTableCleaner");
        updater.setDaemon(true);
        updater.start();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateLoop() {
            try {
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
}
