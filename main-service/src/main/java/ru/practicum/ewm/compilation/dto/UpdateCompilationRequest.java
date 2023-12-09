package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @Length(min = 1, max = 50)
    private String title;
}
