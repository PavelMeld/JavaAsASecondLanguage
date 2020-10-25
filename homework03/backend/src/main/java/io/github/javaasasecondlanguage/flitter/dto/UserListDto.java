package io.github.javaasasecondlanguage.flitter.dto;

import java.util.List;
import java.util.Set;

public class UserListDto extends SimpleResponseDto {
    public final List<String> data;

    public UserListDto(List<String> data) {
        this.data = data;
    }

    public UserListDto(Set<String> data) {
        this.data = List.copyOf(data);
    }
}
