package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned = false;
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
