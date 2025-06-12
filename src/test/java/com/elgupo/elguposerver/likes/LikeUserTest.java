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
public class LikeUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLikeUser() throws Exception {
        String userJson = """
                {
                    "likerId": 1,
                    "userLikeableId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.likerId").value(1))
        .andExpect(jsonPath("$.userLikeableId").value(2))
        .andExpect(jsonPath("$.eventId").value(3))
        .andExpect(jsonPath("$.liked").value(true))
                .andExpect(jsonPath("$.match").value(false));
    }

    @Test
    public void testGetLike() throws Exception {
        String userJson = """
                {
                    "likerId": 1,
                    "userLikeableId": 2,
                    "eventId": 3,
                    "liked": true
                }""";
        String userJson1 = """
                {
                    "likerId": 1,
                    "userLikeableId": 3,
                    "eventId": 3,
                    "liked": false
                }""";
        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        mockMvc.perform(get("/like_users/get_like?likerId=1&likeableId=2&eventId=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.liked").value(true));

        mockMvc.perform(get("/like_users/get_like?likerId=1&likeableId=3&eventId=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.liked").value(false));

        mockMvc.perform(get("/like_users/get_like?likerId=1&likeableId=7&eventId=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exists").value(false))
                .andExpect(jsonPath("$.liked").value(false));

    }

    @Test
    public void testWasDislike() throws Exception {
        String userJson = """
                {
                    "likerId": 1,
                    "userLikeableId": 2,
                    "eventId": 3,
                    "liked": false
                }""";

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(get("/like_users/get_dislike?user1=1&user2=2"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(get("/like_users/get_dislike?user1=2&user2=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(get("/like_users/get_dislike?user1=3&user2=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void testGetLikes() throws Exception {
        String userJson = """
                {
                    "likerId": 1,
                    "userLikeableId": 2,
                    "eventId": 3,
                    "liked": true
                }""";
        String userJson1 = """
                {
                    "likerId": 1,
                    "userLikeableId": 5,
                    "eventId": 3,
                    "liked": true
                }""";
        String userJson2 = """
                {
                    "likerId": 1,
                    "userLikeableId": 7,
                    "eventId": 3,
                    "liked": false
                }""";
        String userJson3 = """
                {
                    "likerId": 1,
                    "userLikeableId": 9,
                    "eventId": 3,
                    "liked": false
                }""";
        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson2));

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson3));

        mockMvc.perform(get("/like_users/get_likes?user=1&event=3&like=true"))
                .andExpect(status().isOk())
                .andExpect(content().string("[2,5]"));

        mockMvc.perform(get("/like_users/get_likes?user=1&event=3&like=false"))
                .andExpect(status().isOk())
                .andExpect(content().string("[7,9]"));

        mockMvc.perform(get("/like_users/get_likes?user=1&event=5&like=false"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testDelete() throws Exception {
        String userJson = """
                {
                    "likerId": 1,
                    "userLikeableId": 2,
                    "eventId": 3,
                    "liked": true
                }""";
        String userJson1 = """
                {
                    "likerId": 1,
                    "userLikeableId": 5,
                    "eventId": 3,
                    "liked": true
                }""";
        String userJson2 = """
                {
                    "likerId": 1,
                    "userLikeableId": 7,
                    "eventId": 3,
                    "liked": false
                }""";
        String userJson3 = """
                {
                    "likerId": 1,
                    "userLikeableId": 9,
                    "eventId": 3,
                    "liked": false
                }""";
        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson2));

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson3));

        mockMvc.perform(delete("/like_users/delete/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(delete("/like_users/delete/4"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        mockMvc.perform(delete("/like_users/delete/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        mockMvc.perform(get("/like_users/get_likes?user=1&event=3&like=true"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        mockMvc.perform(get("/like_users/get_likes?user=1&event=3&like=false"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testDistinctEvents() throws Exception {
        String userJson = """
                {
                    "likerId": 1,
                    "userLikeableId": 2,
                    "eventId": 3,
                    "liked": true
                }""";
        String userJson1 = """
                {
                    "likerId": 1,
                    "userLikeableId": 5,
                    "eventId": 4,
                    "liked": true
                }""";
        String userJson2 = """
                {
                    "likerId": 1,
                    "userLikeableId": 7,
                    "eventId": 3,
                    "liked": false
                }""";
        String userJson3 = """
                {
                    "likerId": 1,
                    "userLikeableId": 9,
                    "eventId": 9,
                    "liked": false
                }""";
        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1));

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson2));

        mockMvc.perform(post("/like_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson3));

        mockMvc.perform(get("/like_users/distinct_events"))
                .andExpect(status().isOk())
                .andExpect(content().string("[3,4,9]"));
    }

}