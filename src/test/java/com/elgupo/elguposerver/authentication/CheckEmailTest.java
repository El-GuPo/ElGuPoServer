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
public class CheckEmailTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testExistsEmail() throws Exception {
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

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        mockMvc.perform(post("/check_email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isUserExists").value(true));
    }

    @Test
    public void testNotExistsEmail() throws Exception {
        String email = "test@example.com";
        String userJson1 = """
                {
                    "email": "%s"
                }""".formatted(email);

        mockMvc.perform(post("/check_email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isUserExists").value(false));
    }

}