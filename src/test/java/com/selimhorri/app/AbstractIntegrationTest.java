package com.selimhorri.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.DynamicPropertyRegistry; // Ya no se necesita
// import org.springframework.test.context.DynamicPropertySource; // Ya no se necesita
import org.springframework.test.web.servlet.MockMvc;
// import org.testcontainers.containers.MySQLContainer; // Ya no se necesita
// import org.testcontainers.containers.wait.strategy.Wait; // Ya no se necesita
// import org.testcontainers.junit.jupiter.Container; // Ya no se necesita
// import org.testcontainers.junit.jupiter.Testcontainers; // Ya no se necesita
// import java.time.Duration; // Ya no se necesita

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UserServiceApplication.class)
@AutoConfigureMockMvc
// @Testcontainers // DESACTIVAR TEMPORALMENTE
public abstract class AbstractIntegrationTest {

    // @Container // DESACTIVAR TEMPORALMENTE
    // private static final MySQLContainer<?> mySQLContainer = new
    // MySQLContainer<>("mysql:8.0.26")
    // .withDatabaseName("test")
    // .withUsername("test")
    // .withPassword("test")
    // .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)));

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    // @DynamicPropertySource // DESACTIVAR TEMPORALMENTE
    // static void dynamicProperties(DynamicPropertyRegistry registry) {
    // registry.add("spring.datasource.url", () ->
    // String.format("jdbc:mysql://%s:%d/%s",
    // mySQLContainer.getHost(),
    // mySQLContainer.getMappedPort(3306),
    // mySQLContainer.getDatabaseName()));
    // registry.add("spring.datasource.username", mySQLContainer::getUsername);
    // registry.add("spring.datasource.password", mySQLContainer::getPassword);
    // }
}