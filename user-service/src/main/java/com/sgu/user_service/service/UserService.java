package com.sgu.user_service.service;

import com.sgu.common.dto.PaginationResponse;
import com.sgu.user_service.dto.request.PaymentRequestDto;
import com.sgu.user_service.dto.response.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface UserService {
    PaginationResponse<UserResponseDto> getUsers(
            int page, int size,
            String role
    );

    UserResponseDto getUserById(UUID id, String role);

    String updateAvatar(UUID targetId, UUID requesterId, MultipartFile file) throws IOException;

    void updateBalance(PaymentRequestDto dto);
}
