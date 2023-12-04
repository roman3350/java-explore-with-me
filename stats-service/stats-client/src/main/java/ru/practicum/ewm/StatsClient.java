package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.util.Map;

public class StatsClient extends BaseClient {
    @Autowired
    public StatsClient(@Value("${emw-stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postEndPointHits(String app, String uri, String ip, LocalDateTime timestamp) {
        return post("/hit", EndPointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp));
    }

    public ResponseEntity<Object> getViewStats(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}