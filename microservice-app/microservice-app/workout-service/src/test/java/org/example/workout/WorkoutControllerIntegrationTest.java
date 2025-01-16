package org.example.workout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
class WorkoutControllerIntegrationTest {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        // Set the location of test SQL data
        registry.add("spring.sql.init.data-locations", () -> "classpath:workout-data.sql");
    }

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void checkInUser() throws Exception {
        Long gymId = 1L;
        String userEmail = "user2@example.com";

        mockMvc.perform(post("/api/workout-service/workout/check-in")
                        .header("X-User-Email", userEmail)
                        .param("gymId", gymId.toString())
                        .with(jwt().jwt(jwt -> jwt.claim("email", userEmail)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully checked into the gym"));
    }
    @Test
    void checkOutUser() throws Exception {
        Long gymId = 1L;
        String userEmail = "user1@example.com";

        mockMvc.perform(post("/api/workout-service/workout/check-out")
                        .header("X-User-Email", userEmail) // Add required header
                        .param("gymId", gymId.toString())
                        .with(jwt().jwt(jwt -> jwt.claim("email", userEmail))) // Optional if JWT is used
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void checkInUser_withInvalidGymId() throws Exception {
        Long gymId = -1L;
        String userEmail = "user1@example.com";

        mockMvc.perform(post("/api/workout-service/workout/check-in")
                        .header("X-User-Email", userEmail) // Add required header
                        .param("gymId", gymId.toString())
                        .with(jwt().jwt(jwt -> jwt.claim("email", userEmail))) // Optional if JWT is used
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkOutUser_withoutCheckIn() throws Exception {
        Long gymId = 1L;
        String userEmail = "invaliduser@example.com";

        mockMvc.perform(post("/api/workout-service/workout/check-out")
                        .header("X-User-Email", userEmail) // Add required header
                        .param("gymId", gymId.toString())
                        .with(jwt().jwt(jwt -> jwt.claim("email", userEmail))) // Optional if JWT is used
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
