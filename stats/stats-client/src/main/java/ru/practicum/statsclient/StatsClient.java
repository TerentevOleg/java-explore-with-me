package ru.practicum.statsclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.StatsDtoIn;

import java.util.List;
import java.util.Map;

@Slf4j
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${statserver.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> hit(StatsDtoIn statsDtoIn) {
        log.debug("StatsClient: post statistics hit ip: {}", statsDtoIn.getIp());
        return post("/hit", statsDtoIn);
    }

    public ResponseEntity<List> get(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        log.debug("StatsClient: get statistics hit by uris: {}", parameters.get(uris));
        return get("/stats", parameters);
    }
}
