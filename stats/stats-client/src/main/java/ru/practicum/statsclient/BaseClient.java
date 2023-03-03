package ru.practicum.statsclient;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate restTemplate;

    public BaseClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected ResponseEntity<Object> post(String path, Object body) {
        return makeAndSendRequest(HttpMethod.POST, path, null, body, Object.class);
    }

    protected ResponseEntity<List> get(String path, Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null, List.class);
    }

    private <T> ResponseEntity<T> makeAndSendRequest(HttpMethod method, String path,
                                                     @Nullable Map<String, Object> parameters,
                                                     @Nullable Object body,
                                                     Class<T> responseType) {

        HttpEntity<Object> requestEntity = new HttpEntity<>(body, null);

        ResponseEntity<T> statsServerResponse;

        try {
            if (parameters != null) {
                statsServerResponse = restTemplate.exchange(path, method, requestEntity, responseType, parameters);
            } else {
                statsServerResponse = restTemplate.exchange(path, method, requestEntity, responseType);
            }
        } catch (HttpStatusCodeException e) {
            return (ResponseEntity<T>) ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareStatsClientResponse(statsServerResponse);
    }

    private static <T> ResponseEntity<T> prepareStatsClientResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}