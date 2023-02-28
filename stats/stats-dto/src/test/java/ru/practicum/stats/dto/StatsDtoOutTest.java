package ru.practicum.stats.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class StatsDtoOutTest {

    @Autowired
    private JacksonTester<StatsDtoOut> jacksonTester;

    private final StatsDtoOut statsDtoOut = StatsDtoOut.builder()
            .app("main-service")
            .uri("/events/1")
            .hits(3L)
            .build();

    @Test
    void statsDtoOutSerializationTest() throws IOException {
        assertThat(jacksonTester.write(statsDtoOut))
                .isStrictlyEqualToJson("statsDtoOut.json");
    }

    @Test
    void statsDtoOutDeserializationTest() throws IOException {
        assertThat(jacksonTester.read("statsDtoOut.json"))
                .usingRecursiveComparison()
                .isEqualTo(statsDtoOut);
    }
}