package io.github.javaasasecondlanguage.flitter.utils;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static io.github.javaasasecondlanguage.flitter.utils.AssertionUtils.saveRequestInfo;
import static io.github.javaasasecondlanguage.flitter.utils.RequestInfo.Method.GET;
import static java.lang.String.join;

public class LoggingRestTemplate {

    private final TestRestTemplate restTemplate;
    private final int port;

    public LoggingRestTemplate(TestRestTemplate restTemplate, int port) {
        this.restTemplate = restTemplate;
        this.port = port;
    }

    public void delete(String... handleParts) {
        var endpoint = getFullPath(handleParts);
        restTemplate.delete(endpoint);
    }

    public ResponseEntity<Result> get(String... handleParts) {
        var endpoint = getFullPath(handleParts);
        ResponseEntity<Result> response = restTemplate.getForEntity(endpoint, Result.class);

        saveRequestInfo(endpoint, GET, Map.of(), response);

        return response;
    }

    public ResponseEntity<Result> post(Map<String, String> params, String... handleParts) {
        var endpoint = getFullPath(handleParts);
        ResponseEntity<Result> response = restTemplate.postForEntity(endpoint,
                params, Result.class);

        saveRequestInfo(endpoint, GET, Map.of(), response);

        return response;
    }

    private String getFullPath(String... parts) {
        return TestConstants.LOCALHOST + port + join("/", parts);
    }
}
