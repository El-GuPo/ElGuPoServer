package com.elgupo.elguposerver.likes;

import com.elgupo.TestConfig;
import com.elgupo.elguposerver.authentication.routes.AuthenticationController;
import com.elgupo.elguposerver.authentication.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LikeEventTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLikeAndDislikeEvent() throws Exception {
        String userJson = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        mockMvc.perform(post("/like_event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.catId").value(2))
                .andExpect(jsonPath("$.eventId").value(3))
                .andExpect(jsonPath("$.status").value("OK"));

        String userJson1 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": false
                }""";

        mockMvc.perform(post("/like_event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.catId").value(2))
                .andExpect(jsonPath("$.eventId").value(3))
                .andExpect(jsonPath("$.status").value("DELETED"));

        String userJson2 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 6,
                    "liked": false
                }""";

        mockMvc.perform(post("/like_event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.catId").value(2))
                .andExpect(jsonPath("$.eventId").value(6))
                .andExpect(jsonPath("$.status").value("DOESN'T FIND"));

        String userJson3 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": false
                }""";

        mockMvc.perform(post("/like_event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.catId").value(2))
                .andExpect(jsonPath("$.eventId").value(3))
                .andExpect(jsonPath("$.status").value("DOESN'T FIND"));

    }

    @Test
    public void testGetLikedEvents() throws Exception {
        String userJson = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        String userJson1 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 5,
                    "liked": true
                }""";

        mockMvc.perform(post("/like_event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        mockMvc.perform(get("/like_events/user/1/events"))
                .andExpect(status().isOk())
                .andExpect(content().string("[3,5]"));

        String userJson2 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": false
                }""";

        mockMvc.perform(post("/like_event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2));


        mockMvc.perform(get("/like_events/user/1/events"))
                .andExpect(status().isOk())
                .andExpect(content().string("[5]"));

    }

    @Test
    public void testGetCommonEvents() throws Exception {
        String userJson = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        String userJson1 = """
                {
                    "userId": 2,
                    "catId": 2,
                    "eventId": 5,
                    "liked": true
                }""";

        String userJson2 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 8,
                    "liked": true
                }""";

        String userJson3 = """
                {
                    "userId": 2,
                    "catId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson2));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson3));

        mockMvc.perform(get("/like_events/user/1/common_events/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("[3]"));

        mockMvc.perform(get("/like_events/user/2/common_events/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("[3]"));

        String userJson5 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": false
                }""";

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson5));


        mockMvc.perform(get("/like_events/user/2/common_events/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testGetCats() throws Exception {
        String userJson = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        String userJson1 = """
                {
                    "userId": 1,
                    "catId": 3,
                    "eventId": 5,
                    "liked": true
                }""";

        String userJson2 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 8,
                    "liked": true
                }""";

        String userJson3 = """
                {
                    "userId": 1,
                    "catId": 4,
                    "eventId": 10,
                    "liked": true
                }""";

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson2));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson3));

        mockMvc.perform(get("/like_events/user/1/cats"))
                .andExpect(status().isOk())
                .andExpect(content().string("[2,3,4]"));

        String userJson5 = """
                {
                    "userId": 1,
                    "catId": 3,
                    "eventId": 5,
                    "liked": false
                }""";

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson5));


        mockMvc.perform(get("/like_events/user/1/cats"))
                .andExpect(status().isOk())
                .andExpect(content().string("[2,4]"));
    }

    @Test
    public void testGetUsers() throws Exception {
        String userJson = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        String userJson1 = """
                {
                    "userId": 7,
                    "catId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        String userJson2 = """
                {
                    "userId": 3,
                    "catId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson2));

        mockMvc.perform(get("/like_events/event/3/get_users"))
                .andExpect(status().isOk())
                .andExpect(content().string("[1,7,3]"));

        String userJson5 = """
                {
                    "userId": 7,
                    "catId": 2,
                    "eventId": 3,
                    "liked": false
                }""";

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson5));


        mockMvc.perform(get("/like_events/event/3/get_users"))
                .andExpect(status().isOk())
                .andExpect(content().string("[1,3]"));
    }

    @Test
    public void testGetCountByUserIdAndCatId() throws Exception {
        String userJson = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        String userJson1 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 5,
                    "liked": true
                }""";

        String userJson2 = """
                {
                    "userId": 1,
                    "catId": 3,
                    "eventId": 10,
                    "liked": true
                }""";

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson2));

        mockMvc.perform(get("/like_events/user/1/get_count/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        mockMvc.perform(get("/like_events/user/1/get_count/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        String userJson5 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": false
                }""";

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson5));


        mockMvc.perform(get("/like_events/user/1/get_count/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mockMvc.perform(get("/like_events/user/1/get_count/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void testIsEventLiked() throws Exception {
        String userJson2 = """
                {
                    "userId": 1,
                    "catId": 3,
                    "eventId": 10,
                    "liked": true
                }""";

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson2));

        mockMvc.perform(get("/like_events/user/1/event/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(get("/like_events/user/1/event/11"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        mockMvc.perform(get("/like_events/user/2/event/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        String userJson5 = """
                {
                    "userId": 1,
                    "catId": 3,
                    "eventId": 10,
                    "liked": false
                }""";

        mockMvc.perform(post("/like_event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson5));


        mockMvc.perform(get("/like_events/user/1/event/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

}