package com.sgu.auth_service.dto.request.email;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class EmailMessage {
    private String to;
    private Map<String, Object> variables;
}
