package com.elgupo.elguposerver;

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
public class BigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void bigTest() throws Exception {

        String email = "test@example.com";
        String userJson = """
                {
                    "email": "%s",
                    "password": "Test123!@#",
                    "confirmPassword": "Test123!@#"
                }""".formatted(email);

        String userJson1 = """
                {
                    "email": "%s"
                }""".formatted(email);

        String userJson2 = """
                {
                    "email": "%s",
                    "password": "Test123!@"
                }""".formatted(email);

        String userJson3 = """
                {
                    "email": "%s",
                    "password": "Test123!@#"
                }""".formatted(email);

        String userJson4 = """
                {
                    "userId": 1,
                    "sex": "MAN",
                    "name" : "Gosha",
                    "surname" : "Gubanov",
                    "age" : 20,
                    "description" : "love programming",
                    "telegramTag" : "spydula"
                }""";

        String userJson5 = """
                {
                    "userId": 1,
                    "catId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        String userJson6 = """
                {
                    "likerId": 1,
                    "userLikeableId": 2,
                    "eventId": 3,
                    "liked": true
                }""";

        mockMvc.perform(post("/check_email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isUserExists").value(false));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.message").value("OK"));

        mockMvc.perform(post("/check_email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isUserExists").value(true));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("This email address is already in use"));

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid password"));

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isProfileFilledOut").value(false))
                .andExpect(jsonPath("$.message").value("OK"));

        mockMvc.perform(post("/fill_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson4))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isProfileFilledOut").value(true))
                .andExpect(jsonPath("$.message").value("OK"));

        mockMvc.perform(get("/get_profile/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Gosha"))
                .andExpect(jsonPath("$.surname").value("Gubanov"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.description").value("love programming"))
                .andExpect(jsonPath("$.telegramTag").value("spydula"))
                .andExpect(jsonPath("$.sex").value("MAN"))
                .andExpect(jsonPath("$.email").value(email));

        mockMvc.perform(post("/like_event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.catId").value(2))
                .andExpect(jsonPath("$.eventId").value(3))
                .andExpect(jsonPath("$.status").value("OK"));

        mockMvc.perform(get("/like_events/user/1/cats"))
                .andExpect(status().isOk())
                .andExpect(content().string("[2]"));

        mockMvc.perform(get("/like_events/event/3/get_users"))
                .andExpect(status().isOk())
                .andExpect(content().string("[1]"));

        mockMvc.perform(get("/like_events/user/1/get_count/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mockMvc.perform(get("/like_events/user/1/get_count/6"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));

        mockMvc.perform(get("/like_events/user/1/event/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(post("/like_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson6))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.likerId").value(1))
                .andExpect(jsonPath("$.userLikeableId").value(2))
                .andExpect(jsonPath("$.eventId").value(3))
                .andExpect(jsonPath("$.liked").value(true))
                .andExpect(jsonPath("$.match").value(false));

        mockMvc.perform(get("/like_users/get_like?likerId=1&likeableId=2&eventId=3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.liked").value(true));

        mockMvc.perform(get("/like_users/get_dislike?user1=1&user2=2"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        mockMvc.perform(get("/like_users/get_likes?user=1&event=3&like=true"))
                .andExpect(status().isOk())
                .andExpect(content().string("[2]"));

        mockMvc.perform(get("/like_users/distinct_events"))
                .andExpect(status().isOk())
                .andExpect(content().string("[3]"));

        mockMvc.perform(delete("/like_users/delete/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(get("/like_users/distinct_events"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}