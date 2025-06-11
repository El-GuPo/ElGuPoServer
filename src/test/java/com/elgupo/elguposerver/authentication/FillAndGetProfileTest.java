package com.elgupo.elguposerver.authentication;

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
public class FillAndGetProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFillProfile() throws Exception {
        String email = "test@example.com";
        String userJson = """
                {
                    "email": "%s",
                    "password": "Test123!@#",
                    "confirmPassword": "Test123!@#"
                }""".formatted(email);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        String userJson1 = """
                {
                    "userId": 1,
                    "sex": "MAN",
                    "name" : "Gosha",
                    "surname" : "Gubanov",
                    "age" : 20,
                    "description" : "love programming",
                    "telegramTag" : "spydula"
                }""";

        mockMvc.perform(post("/fill_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testFillNonExistingProfile() throws Exception {
        String userJson2 = """
                {
                    "userId": 228,
                    "sex": "MAN",
                    "name" : "Gosha",
                    "surname" : "Gubanov",
                    "age" : 20,
                    "description" : "love programming",
                    "telegramTag" : "spydula"
                }""";

        mockMvc.perform(post("/fill_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(-1));
    }

    @Test
    public void testGetProfile() throws Exception {
        String email = "test@example.com";
        String userJson = """
                {
                    "email": "%s",
                    "password": "Test123!@#",
                    "confirmPassword": "Test123!@#"
                }""".formatted(email);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        String userJson1 = """
                {
                    "userId": 1,
                    "sex": "MAN",
                    "name" : "Gosha",
                    "surname" : "Gubanov",
                    "age" : 20,
                    "description" : "love programming",
                    "telegramTag" : "spydula"
                }""";

        mockMvc.perform(post("/fill_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1));

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
    }

    @Test
    public void testUnsuccessfulUserLogin() throws Exception {
        String email = "test@example.com";
        String userJson = """
                {
                    "email": "%s",
                    "password": "Test123!@#",
                    "confirmPassword": "Test123!@#"
                }""".formatted(email);
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        String userJson1 = """
                {
                    "email": "lala@gmail.com",
                    "password": "Test123!@#"
                }""";

        String userJson2 = """
                {
                    "email": "%s",
                    "password": "Test123!@"
                }""".formatted(email);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid password"));
    }

}