package io.github.javaasasecondlanguage.flitter.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import static io.github.javaasasecondlanguage.flitter.utils.CollectionTestUtils.retainKeys;
import static io.github.javaasasecondlanguage.flitter.utils.ExpectedStatus.EXPECT_SUCCESS;
import static java.lang.String.format;

public class AssertionUtils {

    public static void assertTrue(boolean condition) {
        String fullMessage = lastRequestInfo.toString();
        org.junit.jupiter.api.Assertions.assertTrue(condition, fullMessage);
    }

    public static void assertEquals(Object expected, Object actual) {
        String fullMessage = lastRequestInfo.toString();
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual, fullMessage);
    }

    public static void assertNotNull(Object actual) {
        String fullMessage = lastRequestInfo.toString();
        org.junit.jupiter.api.Assertions.assertNotNull(actual, fullMessage);
    }

    public static void fail(String message) {
        String fullMessage = format("%s. %s", message, lastRequestInfo.toString());
        org.junit.jupiter.api.Assertions.fail(fullMessage);
    }

    public static <A, B> void assertSetEquals(Collection<A> expectedCollection,
                                              Collection<B> actualCollection) {
        assertEquals(
                new HashSet<>(expectedCollection),
                new HashSet<>(actualCollection)
        );
    }

    public static <A, B> void assertMapsEqualByKeys(
            Collection<Map<String, A>> expectedCollection,
            Collection<Map<String, B>> actualCollection,
            String... keys
    ) {
        assertEquals(
                retainKeys(expectedCollection, keys),
                retainKeys(actualCollection, keys)
        );
    }

    public static void assertThatStatusIsExpected(ExpectedStatus expectedStatus,
                                                  ResponseEntity<Result> responseEntity) {
        var statusCode = responseEntity.getStatusCode();
        var result = responseEntity.getBody();

        Object data = result.getData();
        String errorMessage = result.getErrorMessage();

        if (expectedStatus == EXPECT_SUCCESS) {
            handleUnexpectedFail(statusCode, errorMessage, data);
        } else {
            handleUnexpectedSuccess(statusCode, errorMessage, data);
        }
    }

    private static void handleUnexpectedFail(HttpStatus statusCode,
                                             String errorMessage,
                                             Object data) {
        var hasErrorMessage = (errorMessage != null);
        var hasErrorCode = (statusCode != HttpStatus.OK);

        String assertionMessage = null;
        if (hasErrorMessage && hasErrorCode) {
            assertionMessage = format(
                    "Expected success in this response, but it has error message and error code. "
                    + "Error message: %s, error code: %s, Data: %s",
                    errorMessage,
                    statusCode,
                    data
            );
        } else if (hasErrorMessage) {
            assertionMessage = format(
                    "Expected success in this response, but it has error message. "
                    + "Error message: %s, data: %s",
                    errorMessage,
                    data
            );
        } else if (hasErrorCode) {
            assertionMessage = format(
                    "Expected success in this response, but it has error code. "
                    + "Error code: %s, data: %s",
                    statusCode,
                    data
            );
        }

        if (assertionMessage != null) {
            fail(assertionMessage);
        }
    }

    private static void handleUnexpectedSuccess(HttpStatus statusCode,
                                                String errorMessage,
                                                Object data) {
        var hasErrorMessage = (errorMessage != null);
        var hasErrorCode = (statusCode != HttpStatus.OK);

        String assertionMessage = null;
        if (!hasErrorMessage && !hasErrorCode) {
            assertionMessage = format(
                    "Expected fail in this response, but it has no error message and no error code."
                    + " Data: %s",
                    data
            );
        } else if (hasErrorMessage && !hasErrorCode) {
            assertionMessage = format(
                    "Expected fail in this response, but it has no error code, only error message. "
                    + "Error message: %s, data: %s",
                    errorMessage,
                    data
            );
        } else if (!hasErrorMessage) {
            assertionMessage = format(
                    "Expected fail in this response, but it has no error message, only error code. "
                    + "Error code: %s, data: %s",
                    statusCode,
                    data
            );
        }

        if (assertionMessage != null) {
            fail(assertionMessage);
        }
    }

    // ==== Information about last HTTP request is saved in static context
    // for better assertion messages.

    private static RequestInfo lastRequestInfo = new RequestInfo("<nothing called yet>",
            null, Map.of(), null);

    public static void saveRequestInfo(String endpoint,
                                       RequestInfo.Method method,
                                       Map<String, String> params,
                                       Object response) {
        lastRequestInfo = new RequestInfo(endpoint, method, params, response);
    }

    public static RequestInfo getLastRequestInfo() {
        return lastRequestInfo;
    }
}
