package com.sgu.email_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class EmailMessage {
    private String to;
    private String subject;
    private String template;
    private Map<String, Object> variables;
}
