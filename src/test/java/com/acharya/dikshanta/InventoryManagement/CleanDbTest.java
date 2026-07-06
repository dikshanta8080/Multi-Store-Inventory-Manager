package com.acharya.dikshanta.InventoryManagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.flyway.enabled=false")
@ActiveProfiles("dev")
public class CleanDbTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void cleanDb() {
        jdbcTemplate.execute("DROP SCHEMA public CASCADE; CREATE SCHEMA public; GRANT ALL ON SCHEMA public TO postgres; GRANT ALL ON SCHEMA public TO public;");
        System.out.println("Database schema public dropped and recreated successfully!");
    }
}
