package com.training.helpdesk.dto.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedbackDto {

    private Long ticketId;

    private String ticketName;

    @Pattern(regexp = "terrible|bad|medium|good|great", message = "Wrong ticket rate")
    @NotBlank(message = "rate must not be blank")
    private String rate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private String text;
}
