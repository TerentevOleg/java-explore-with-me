package ru.practicum.statsclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.StatsDtoIn;

import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    private static final String API_PREFIX = "";

    @Autowired
    public StatsClient(@Value("stat-server.url") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStats(String startDateTime, String endDateTime,
                                           List<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", startDateTime,
                "end", endDateTime,
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", null, parameters);
    }

    public ResponseEntity<Object> create(StatsDtoIn statsDtoIn) {
        return post("/hit", statsDtoIn);
    }
}
