package ru.practicum.stats.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class StatsDtoInTest {

    @Autowired
    private JacksonTester<StatsDtoIn> jacksonTester;

    private final StatsDtoIn statsDtoIn = StatsDtoIn.builder()
            .app("main-service")
            .uri("/events/1")
            .ip("10.8.0.1")
            .timestamp(LocalDateTime.parse("1993-12-03 10:05:00",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();

    @Test
    void statsDtoInSerializationTest() throws IOException {
        assertThat(jacksonTester.write(statsDtoIn))
                .isStrictlyEqualToJson("statsDtoIn.json");
    }

    @Test
    void statsDtoInDeserializationTest() throws IOException {
        jacksonTester.read("statsDtoIn.json");
        assertThat(jacksonTester.read("statsDtoIn.json"))
                .usingRecursiveComparison()
                .isEqualTo(statsDtoIn);
    }
}