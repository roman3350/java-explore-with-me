package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.location.model.Location;

@Data
@AllArgsConstructor
public class UpdateEventAdminRequest {
    @Length(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Length(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Length(min = 3, max = 120)
    private String title;
}
