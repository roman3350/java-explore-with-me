package ru.practicum.ewm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {
    @NotEmpty
    @NotNull
    public String app;
    @NotEmpty
    @NotNull
    public String uri;
    @PositiveOrZero
    @NotNull
    public Long hits;
}
