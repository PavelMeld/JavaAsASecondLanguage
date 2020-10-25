package io.github.javaasasecondlanguage.flitter.dto;

public class SimpleResponseDto {
    public enum CommonResponses {
        SUCCESS("OK"),
        UNAUTHORIZED("Unauthorized"),
        USER_NOT_FOUND("User not found");

        String text;

        CommonResponses(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public final String data;
    public final String errorMessage;

    SimpleResponseDto() {
        data = null;
        errorMessage = null;
    }

    SimpleResponseDto(String data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    //public static SimpleResponseDto successfullResponse(String okMessage) {
    //   return new SimpleResponseDto(okMessage, null);
    //}

    //public static SimpleResponseDto errorResponse(String errorMessage) {
    //    return new SimpleResponseDto(null, errorMessage);
    //}

    public static SimpleResponseDto successfullResponse(CommonResponses message) {
        return new SimpleResponseDto(message.toString(), null);
    }

    public static SimpleResponseDto errorResponse(CommonResponses message) {
        return new SimpleResponseDto(null, message.toString());
    }

}