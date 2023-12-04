package ru.practicum.ewm;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class EndPointHitDto {
    @NotNull
    @NotEmpty
    private String app;
    @NotNull
    @NotEmpty
    private String uri;
    @NotNull
    @NotEmpty
    private String ip;
    @NotNull
    @NotEmpty
    private String timestamp;
}
