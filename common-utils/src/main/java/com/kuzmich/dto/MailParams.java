package com.kuzmich.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailParams {
    String id;
    String emailTo;

}
