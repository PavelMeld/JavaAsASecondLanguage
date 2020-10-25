package io.github.javaasasecondlanguage.flitter.service;

import io.github.javaasasecondlanguage.flitter.db.UserDatabase;
import io.github.javaasasecondlanguage.flitter.UserExistException;
import io.github.javaasasecondlanguage.flitter.dto.RegistrationRequestDto;
import io.github.javaasasecondlanguage.flitter.dto.RegistrationResponseDto;
import io.github.javaasasecondlanguage.flitter.dto.UserListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserService {
    @Autowired
    UserDatabase userDatabase;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    RegistrationResponseDto registerUser(@RequestBody RegistrationRequestDto requestDto, String name) {
        RegistrationResponseDto response;

        try {
            String userHash;

            userHash = userDatabase.addUser(requestDto.getUserName());
            response = RegistrationResponseDto.successfullRegistrationgDto(
                    requestDto.getUserName(), userHash);
        } catch (UserExistException e) {
            response = RegistrationResponseDto.errorRegistrationDto(e.getMessage());
        }

        return response;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    UserListDto userList() {
        return new UserListDto(userDatabase.getUserList());
    }

}
