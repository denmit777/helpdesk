package com.training.helpdesk.dto.attachment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentDto {

    private Long id;

    private String name;

    private byte[] file;
}
