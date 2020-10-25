package io.github.javaasasecondlanguage.flitter.service;

import io.github.javaasasecondlanguage.flitter.db.FeedDatabase;
import io.github.javaasasecondlanguage.flitter.db.FlitDatabase;
import io.github.javaasasecondlanguage.flitter.db.SubscriptionDatabase;
import io.github.javaasasecondlanguage.flitter.db.UserDatabase;
import io.github.javaasasecondlanguage.flitter.dto.SimpleResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clear")
public class ClearService {
    @Autowired
    UserDatabase userDatabase;
    @Autowired
    FlitDatabase flitDatabase;
    @Autowired
    FeedDatabase feedDatabase;
    @Autowired
    SubscriptionDatabase subscriptionDatabase;

    @DeleteMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleResponseDto clear() {
        userDatabase.clear();
        flitDatabase.clear();
        feedDatabase.clear();
        subscriptionDatabase.clear();

        return SimpleResponseDto.successfullResponse(SimpleResponseDto.CommonResponses.SUCCESS);
    }
}
