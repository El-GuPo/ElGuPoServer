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

}