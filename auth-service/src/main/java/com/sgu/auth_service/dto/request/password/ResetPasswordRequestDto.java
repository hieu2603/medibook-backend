package com.sgu.auth_service.dto.request.password;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordRequestDto {
    private String token;
    private String newPassword;
}
