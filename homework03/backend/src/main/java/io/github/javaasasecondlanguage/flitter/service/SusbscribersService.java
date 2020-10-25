package io.github.javaasasecondlanguage.flitter.service;

import io.github.javaasasecondlanguage.flitter.db.SubscriptionDatabase;
import io.github.javaasasecondlanguage.flitter.db.UserDatabase;
import io.github.javaasasecondlanguage.flitter.dto.SimpleResponseDto;
import io.github.javaasasecondlanguage.flitter.dto.UserListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscribers")
public class SusbscribersService {
    @Autowired
    UserDatabase userDatabase;
    @Autowired
    SubscriptionDatabase subscriptionDatabase;

    // Who subscribed on me
    @GetMapping(value = "/list/{userToken}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SimpleResponseDto> listSubscribers(@PathVariable("userToken") String userToken) {
        String user = userDatabase.getUserByToken(userToken);
        if (user == null) {
            return new ResponseEntity<>(
                    SimpleResponseDto.errorResponse(SimpleResponseDto.CommonResponses.USER_NOT_FOUND),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                new UserListDto(subscriptionDatabase.getSubscribers(user)),
                HttpStatus.OK);
    }
}
