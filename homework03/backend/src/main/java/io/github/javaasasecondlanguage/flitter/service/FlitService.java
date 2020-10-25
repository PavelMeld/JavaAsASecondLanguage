package io.github.javaasasecondlanguage.flitter.service;

import io.github.javaasasecondlanguage.flitter.db.FlitDatabase;
import io.github.javaasasecondlanguage.flitter.db.SubscriptionDatabase;
import io.github.javaasasecondlanguage.flitter.db.UserDatabase;
import io.github.javaasasecondlanguage.flitter.dto.FlitListDto;
import io.github.javaasasecondlanguage.flitter.dto.NewFlitDto;
import io.github.javaasasecondlanguage.flitter.dto.SimpleResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flit")
public class FlitService {
    @Autowired
    UserDatabase userDatabase;
    @Autowired
    FlitDatabase flitDatabase;
    @Autowired
    SubscriptionDatabase subscriptionDatabase;

    @PostMapping(
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleResponseDto addFlit(@RequestBody NewFlitDto newFlit) {
        String user = userDatabase.getUserByToken(newFlit.getUserToken());

        if (user == null) {
            return SimpleResponseDto.errorResponse(SimpleResponseDto.CommonResponses.USER_NOT_FOUND);
        }

        flitDatabase.addFlit(user, newFlit.getContent());
        return SimpleResponseDto.successfullResponse(SimpleResponseDto.CommonResponses.SUCCESS);
    }

    @GetMapping(value = "/list/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleResponseDto discoverLastTen(@PathVariable("username") String userName) {
        if (!userDatabase.userExists(userName)) {
            return SimpleResponseDto.errorResponse(SimpleResponseDto.CommonResponses.USER_NOT_FOUND);
        }
        return FlitListDto.FlitListFromFlitDatabase(flitDatabase.flitsFromUser(userName));

    }

    @GetMapping(value = "/list/feed/{usertoken}", produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleResponseDto getFeed(@PathVariable("usertoken") String userToken) {
        String user = userDatabase.getUserByToken(userToken);

        if (user == null) {
           return SimpleResponseDto.errorResponse(SimpleResponseDto.CommonResponses.USER_NOT_FOUND);
        }

        Set<String> publishers = subscriptionDatabase.getSources(user);

        var flits = publishers.stream()
                .map(flitDatabase::flitsFromUser)
                .flatMap((publisherFlits)->publisherFlits.stream())
                .collect(Collectors.toList());

        return FlitListDto.FlitListFromFlitDatabase(flits);
    }

    @GetMapping(value = "/discover", produces = MediaType.APPLICATION_JSON_VALUE)
    FlitListDto discoverLastTen() {
        return FlitListDto.FlitListFromFlitDatabase(flitDatabase.lastTen());
    }
}