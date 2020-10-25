package io.github.javaasasecondlanguage.flitter.dto;

public class RegistrationResponseDto extends SimpleResponseDto {
    class RegistrationInfo {
        public final String userName;
        public final String userToken;

        public RegistrationInfo(String userName, String userToken) {
            this.userName = userName;
            this.userToken = userToken;
        }
    }

    public final RegistrationInfo data;
    public final String error;


    RegistrationResponseDto(String user, String token) {
        data = new RegistrationInfo(user, token);
        this.error = null;
    }

    RegistrationResponseDto(String reason) {
        data = null;
        this.error = reason;
    }

    public static RegistrationResponseDto
        successfullRegistrationgDto(String userName, String token) {
        return new RegistrationResponseDto(userName, token);
    }

    public static RegistrationResponseDto errorRegistrationDto(String reason) {
        return new RegistrationResponseDto(reason);
    }
}
