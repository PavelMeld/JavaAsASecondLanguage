package io.github.javaasasecondlanguage.flitter;

import io.github.javaasasecondlanguage.flitter.utils.FlitterRestMethods;
import io.github.javaasasecondlanguage.flitter.utils.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.github.javaasasecondlanguage.flitter.utils.AssertionUtils.assertMapsEqualByKeys;
import static io.github.javaasasecondlanguage.flitter.utils.AssertionUtils.assertSetEquals;
import static io.github.javaasasecondlanguage.flitter.utils.AssertionUtils.assertTrue;
import static io.github.javaasasecondlanguage.flitter.utils.ExpectedStatus.EXPECT_FAIL;
import static io.github.javaasasecondlanguage.flitter.utils.ExpectedStatus.EXPECT_SUCCESS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubscriptionRestTest {

    private FlitterRestMethods rest;

    public SubscriptionRestTest(
            @Autowired TestRestTemplate restTemplate,
            @LocalServerPort int port
    ) {
        this.rest = new FlitterRestMethods(restTemplate, port);
    }

    @BeforeEach
    void setUp() {
        rest.clear();
    }

    @Test
    void test_listSubscribers_empty() {
        var token = rest.addUser("Sasha", EXPECT_SUCCESS);
        var subscribers = rest.listSubscribers(token, EXPECT_SUCCESS);
        assertTrue(subscribers.isEmpty());
    }


    @Test
    void test_listSubscribers_unknownPublisher() {
        rest.addUser("Sasha", EXPECT_SUCCESS);
        rest.listSubscribers("UnknownToken", EXPECT_FAIL);
    }

    @Test
    void test_listPublishers_empty() {
        var token = rest.addUser("Sasha", EXPECT_SUCCESS);
        var publishers = rest.listPublishers(token, EXPECT_SUCCESS);
        assertTrue(publishers.isEmpty());
    }

    @Test
    void test_listPublishers_unknownSubscriber() {
        rest.addUser("Sasha", EXPECT_SUCCESS);
        rest.listPublishers("UnknownToken", EXPECT_FAIL);
    }

    @Test
    void test_subscribe_singlePublisher_singleSubscriber() {
        var sashaToken = rest.addUser("Sasha", EXPECT_SUCCESS);
        var nikitaToken = rest.addUser("Nikita", EXPECT_SUCCESS);

        rest.subscribe(sashaToken, "Nikita", EXPECT_SUCCESS);

        assertSetEquals(
                List.of("Nikita"),
                rest.listPublishers(sashaToken, EXPECT_SUCCESS)
        );
        assertSetEquals(
                List.of("Sasha"),
                rest.listSubscribers(nikitaToken, EXPECT_SUCCESS)
        );

        rest.unsubscribe(sashaToken, "Nikita", EXPECT_SUCCESS);

        assertSetEquals(
                Collections.emptyList(),
                rest.listPublishers(sashaToken, EXPECT_SUCCESS)
        );
        assertSetEquals(
                Collections.emptyList(),
                rest.listSubscribers(nikitaToken, EXPECT_SUCCESS)
        );
    }

    @Test
    void test_subscribe_singlePublisher_multipleSubscribers() {
        var subscriberNames = List.of(
                "SubOne",
                "SubTwo",
                "SubThree"
        );
        var subscriberTokens = rest.addAllUsers(subscriberNames, EXPECT_SUCCESS);
        var publisherToken = rest.addUser("Pub", EXPECT_SUCCESS);
        rest.subscribeAll(subscriberTokens, List.of("Pub"), EXPECT_SUCCESS);

        for (String subscriberToken : subscriberTokens) {
            assertSetEquals(
                    List.of("Pub"),
                    rest.listPublishers(subscriberToken, EXPECT_SUCCESS)
            );
        }
        assertSetEquals(
                subscriberNames,
                rest.listSubscribers(publisherToken, EXPECT_SUCCESS)
        );

        rest.unsubscribeAll(subscriberTokens, List.of("Pub"), EXPECT_SUCCESS);

        for (String subscriberToken : subscriberTokens) {
            assertSetEquals(
                    Collections.emptyList(),
                    rest.listPublishers(subscriberToken, EXPECT_SUCCESS)
            );
        }
        assertSetEquals(
                Collections.emptyList(),
                rest.listSubscribers(publisherToken, EXPECT_SUCCESS)
        );
    }

    @Test
    void test_subscribe_multiplePublishers_multipleSubscribers() {
        var subscriberNames = List.of(
                "SubOne",
                "SubTwo",
                "SubThree"
        );
        var publisherNames = List.of(
                "PubOne",
                "PubTwo",
                "PubThree"
        );
        var subscriberTokens = rest.addAllUsers(subscriberNames, EXPECT_SUCCESS);
        var publisherTokens = rest.addAllUsers(publisherNames, EXPECT_SUCCESS);
        rest.subscribeAll(subscriberTokens, publisherNames, EXPECT_SUCCESS);

        for (String subscriberToken : subscriberTokens) {
            assertSetEquals(
                    publisherNames,
                    rest.listPublishers(subscriberToken, EXPECT_SUCCESS)
            );
        }
        for (String publisherToken : publisherTokens) {
            assertSetEquals(
                    subscriberNames,
                    rest.listSubscribers(publisherToken, EXPECT_SUCCESS)
            );
        }

        rest.unsubscribeAll(subscriberTokens, publisherNames, EXPECT_SUCCESS);

        for (String publisherToken : publisherTokens) {
            assertSetEquals(
                    Collections.emptyList(),
                    rest.listSubscribers(publisherToken, EXPECT_SUCCESS)
            );
        }
        for (String subscriberToken : subscriberTokens) {
            assertSetEquals(
                    Collections.emptyList(),
                    rest.listPublishers(subscriberToken, EXPECT_SUCCESS)
            );
        }
    }

    @Test
    void test_consumeFlits() {
        var publisherNames = List.of(
                "PubOne",
                "PubTwo",
                "PubThree"
        );

        var subscriberToken = rest.addUser("SubOne", EXPECT_SUCCESS);
        var neutralToken = rest.addUser("NeutralOne", EXPECT_SUCCESS);
        var publisherTokens = rest.addAllUsers(publisherNames, EXPECT_SUCCESS);

        rest.subscribeAll(
                List.of(subscriberToken),
                publisherNames,
                EXPECT_SUCCESS
        );

        var expectedFlits = new ArrayList<Map<String, Object>>();
        for (int publisherIndex = 0; publisherIndex < publisherTokens.size(); publisherIndex++) {
            var publisherName = publisherNames.get(publisherIndex);
            var publisherToken = publisherTokens.get(publisherIndex);

            for (int flitIndex = 0; flitIndex < 3; flitIndex++) {
                var content = String.format("Flit number %s by %s", flitIndex, publisherName);
                rest.addFlit(publisherToken, content, EXPECT_SUCCESS);
                expectedFlits.add(Map.of(
                        TestConstants.USER_NAME, publisherName,
                        TestConstants.CONTENT, content
                ));
            }
        }

        // Also adding flits from user "NeutralOne", who is not read by anyone actually
        for (int flitIndex = 0; flitIndex < 3; flitIndex++) {
            var content = String.format("Flit number %s by %s", flitIndex, neutralToken);
            rest.addFlit(neutralToken, content, EXPECT_SUCCESS);
        }

        var flitsFromFeed = rest.listFlitsConsumedByUser(subscriberToken, EXPECT_SUCCESS);
        assertMapsEqualByKeys(expectedFlits,
                flitsFromFeed, TestConstants.USER_NAME, TestConstants.USER_TOKEN);
    }


    @Test
    void test_unknownPublisher() {
        var sashaToken = rest.addUser("Sasha", EXPECT_SUCCESS);
        rest.listSubscribers("UnknownToken", EXPECT_FAIL);
    }

    @Test
    void test_consumeFlits_unknownSubscriber() {
        var token = rest.addUser("Sasha", EXPECT_SUCCESS);
        rest.listFlitsConsumedByUser("UnknownToken", EXPECT_FAIL);
    }
}
