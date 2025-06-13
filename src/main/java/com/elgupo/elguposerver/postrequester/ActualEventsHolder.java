package com.elgupo.elguposerver.postrequester;

import java.io.IOException;
import java.time.Duration;

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