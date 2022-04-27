package com.training.helpdesk.dto.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.training.helpdesk.model.Category;
import com.training.helpdesk.model.enums.Urgency;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketCreationOrEditDto {

    @NotBlank(message = "name must not be blank")
    @Size(min = 4, max = 100, message = "name must be between 4 and 100")
    @Pattern(regexp = "^[^A-ZА-Яа-я]*$", message = "wrong name")
    private String name;

    @Size(max = 500, message = "description must not be more then 500")
    @Pattern(regexp = "^[^А-Яа-я]*$", message = "wrong description")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "you can't select a date that is less than the current date")
    private LocalDate desiredResolutionDate;

    private Category category;

    private Urgency urgency;
}
