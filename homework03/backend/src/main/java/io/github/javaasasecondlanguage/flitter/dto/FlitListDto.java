package io.github.javaasasecondlanguage.flitter.dto;

import io.github.javaasasecondlanguage.flitter.db.FlitDatabase;

import java.util.List;
import java.util.stream.Collectors;

public class FlitListDto extends SimpleResponseDto {
    public final List<FlitDto> data;
    public final String errorMessage;

    FlitListDto(List<FlitDto> data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static FlitListDto FlitListFromFlitDatabase(List<FlitDatabase.FlitRecord> records) {
        return new FlitListDto(
                records.stream()
                        .map((record) -> new FlitDto(record.user, record.content))
                        .collect(Collectors.toList()),
                null
        );
    }
}
