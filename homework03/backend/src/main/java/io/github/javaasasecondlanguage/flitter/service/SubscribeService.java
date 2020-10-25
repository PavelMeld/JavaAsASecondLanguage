package io.github.javaasasecondlanguage.flitter.service;

import io.github.javaasasecondlanguage.flitter.db.SubscriptionDatabase;
import io.github.javaasasecondlanguage.flitter.db.UserDatabase;
import io.github.javaasasecondlanguage.flitter.dto.SimpleResponseDto;
import io.github.javaasasecondlanguage.flitter.dto.SubscribeRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SubscribeService {
    @Autowired
    UserDatabase userDatabase;

    @Autowired
    SubscriptionDatabase subscriptionDatabase;

    @PostMapping(value = "/subscribe", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleResponseDto subscribe(@RequestBody SubscribeRequestDto requestDto) {
        String user = userDatabase.getUserByToken(requestDto.getSubscriberToken());

        if (user == null) {
            return SimpleResponseDto.errorResponse(SimpleResponseDto.CommonResponses.UNAUTHORIZED);
        }

        String publisher = requestDto.getPublisherName();

        if (!userDatabase.userExists(publisher)) {
            return SimpleResponseDto.errorResponse(SimpleResponseDto.CommonResponses.USER_NOT_FOUND);
        }

        subscriptionDatabase.subscribe(user, publisher);

        return SimpleResponseDto.successfullResponse(SimpleResponseDto.CommonResponses.SUCCESS);
    }

    @PostMapping(value = "/unsubscribe", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleResponseDto unsubscribe(@RequestBody SubscribeRequestDto requestDto) {
        String user = userDatabase.getUserByToken(requestDto.getSubscriberToken());

        if (user == null) {
            return SimpleResponseDto.errorResponse(SimpleResponseDto.CommonResponses.UNAUTHORIZED);
        }

        String publisher = requestDto.getPublisherName();

        if (!subscriptionDatabase.userIsSubscribed(user, publisher)) {
            return SimpleResponseDto.errorResponse(SimpleResponseDto.CommonResponses.USER_NOT_FOUND);
        }

        subscriptionDatabase.unsubscribe(user, publisher);

        return SimpleResponseDto.successfullResponse(SimpleResponseDto.CommonResponses.SUCCESS);
    }

}
