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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSuccessfulUserLogin() throws Exception {
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
                    "email": "%s",
                    "password": "Test123!@#"
                }""".formatted(email);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.isProfileFilledOut").value(false))
                .andExpect(jsonPath("$.message").value("OK"));
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