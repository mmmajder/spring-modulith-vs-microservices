package org.example.scoring;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class LeaderboardControllerIntegrationTest {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:leaderboard-data.sql");
    }

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getTopUsers() throws Exception {
        String month = "2024-12";
        String userEmail = "user1@example.com";
        mockMvc.perform(get("/api/scoring-service/leaderboard/{month}", month)
                        .header("X-User-Email", userEmail)
                        .with(jwt().jwt(jwt -> jwt.claim("email", userEmail)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[0].points").exists());
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void getUserPoints() throws Exception {
        // Define test data
        String userEmail = "user1@example.com";
        int points = 50;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        jdbcTemplate.update(
                "INSERT INTO SCORING.scoring_record (id, user_email, points, description, timestamp) VALUES (?, ?, ?, ?, ?)",
                6, userEmail, points, "Monthly workout points", formattedDate
        );

        mockMvc.perform(get("/api/scoring-service/leaderboard/user-points")
                        .header("X-User-Email", userEmail)
                        .with(jwt().jwt(jwt -> jwt.claim("email", userEmail)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(points));
    }

}
