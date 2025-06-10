package com.elgupo.elguposerver.tinder;

import com.elgupo.elguposerver.database.models.User;
import com.elgupo.elguposerver.database.repositories.LikeEventRepository;
import com.elgupo.elguposerver.database.repositories.UserRepository;
import com.elgupo.elguposerver.database.services.LikeEventService;
import com.elgupo.elguposerver.database.services.LikeUserService;
import com.elgupo.elguposerver.dataclasses.Event;
import com.elgupo.elguposerver.dataclasses.Place;
import com.elgupo.elguposerver.postrequester.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class TinderService {
    @Autowired
    private static UserRepository userRepository;
    private static final LikeEventService likeEventService = new LikeEventService();
    private static final LikeUserService likeUserService = new LikeUserService();
    public static List<User> getCandidates(Long mainUserId, Long eventId, Integer minAge, Integer maxAge, String sex) {
        List<Long> users = likeEventService.getUsers(eventId);
        users = users.stream()
                .filter(u -> !Objects.equals(u, mainUserId) && !likeUserService.wasDislike(u, mainUserId) && !likeUserService.wasDislike(mainUserId, u))
                .filter(u -> sex.isEmpty() || sex.equals(userRepository.findById(Math.toIntExact(u)).orElse(new User()).getSex()))
                .filter(u -> minAge == null || minAge <= userRepository.findById(Math.toIntExact(u)).orElse(new User()).getAge())
                .filter(u -> maxAge == null || maxAge >= userRepository.findById(Math.toIntExact(u)).orElse(new User()).getAge())
                .collect(Collectors.toList());
        Set<Long> cats = likeEventService.getCats(mainUserId);
        HashMap<Long, Long> mainVector = new HashMap<>();
        for (Long cat : cats) {
            mainVector.put(cat, likeEventService.getCountByUserIdAndCatId(mainUserId, cat));
        }
        users.sort(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                Long result1 = 0L, result2 = 0L;
                for (Long cat : cats) {
                    result1 += (mainVector.get(cat) - likeEventService.getCountByUserIdAndCatId(o1, cat)) *
                            (mainVector.get(cat) - likeEventService.getCountByUserIdAndCatId(o1, cat));
                    result2 += (mainVector.get(cat) - likeEventService.getCountByUserIdAndCatId(o2, cat)) *
                            (mainVector.get(cat) - likeEventService.getCountByUserIdAndCatId(o2, cat));
                }
                return Long.compare(result1, result2);
            }
        });
        return users.stream()
                .filter(u -> userRepository.findById(Math.toIntExact(u)).isPresent())
                .map(u -> userRepository.findById(Math.toIntExact(u)).orElse(new User()))
                .filter(u -> !u.equals(new User()))
                .collect(Collectors.toList());
    }

    public static List<User> getMatches(Long mainUserId, Long eventId) {
        List<Long> usersToCheck = likeUserService.getLikes(mainUserId, eventId, true);
        List<User> matches = new ArrayList<>();
        for (Long user : usersToCheck) {
            if (likeUserService.getLike(user, mainUserId, eventId).isLiked() && likeEventService.getLikedEvents(user).contains(eventId)) {
                User matchedUser = userRepository.findById(Math.toIntExact(user)).orElse(new User());
                if (matchedUser.equals(new User())) continue;
                matches.add(matchedUser);
            }
        }
        return matches;
    }

    public static List<Event> getLikedEvents(Long userId) {
        List<Long> eventsIds = likeEventService.getLikedEvents(userId);
        HashMap<Integer, List<Event>> actualEvents = PostRequester.getEventsByCategories();
        List<Event> allEvents = new ArrayList<>();
        for (Integer cat : actualEvents.keySet()) {
            allEvents.addAll(actualEvents.get(cat));
        }
        List<Event> likedEvents = new ArrayList<>();
        for (Event event : allEvents) {
            if (eventsIds.contains((long) event.getId())) {
                likedEvents.add(event);
            }
        }
        return likedEvents;
    }
}
