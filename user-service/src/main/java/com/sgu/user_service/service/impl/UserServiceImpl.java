package com.sgu.user_service.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sgu.user_service.constant.PaymentType;
import com.sgu.user_service.constant.TransferType;
import com.sgu.user_service.dto.common.PaginationMeta;
import com.sgu.user_service.dto.common.PaginationResponse;
import com.sgu.user_service.dto.request.PaymentRequestDto;
import com.sgu.user_service.dto.request.TransferRequestDto;
import com.sgu.user_service.dto.response.UserResponseDto;
import com.sgu.user_service.exception.InsufficientBalanceException;
import com.sgu.user_service.exception.ResourceNotFoundException;
import com.sgu.user_service.mapper.UserMapper;
import com.sgu.user_service.model.User;
import com.sgu.user_service.repository.UserRepository;
import com.sgu.user_service.security.UserPermissionValidator;
import com.sgu.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPermissionValidator userPermissionValidator;
    private final Cloudinary cloudinary;

    @Override
    public PaginationResponse<UserResponseDto> getUsers(int page, int size, String role) {
        // Kiểm tra quuyen
        userPermissionValidator.validateGetUsersPermission(role);

        int pageIndex = (page <= 0) ? 0 : page - 1;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by("createdAt").ascending());
        Page<User> userProfilePage = userRepository.findAll(pageable);

        List<UserResponseDto> data = userProfilePage.map(UserMapper::toDto).getContent();

        long totalItems = userProfilePage.getTotalElements();

        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(totalItems == 0 ? 0 : userProfilePage.getNumber() + 1)
                .pageSize(userProfilePage.getSize())
                .totalPages(userProfilePage.getTotalPages())
                .totalItems(totalItems)
                .build();

        return PaginationResponse.<UserResponseDto>builder()
                .data(data)
                .meta(meta)
                .build();
    }

    @Override
    public UserResponseDto getUserById(UUID id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userPermissionValidator.validateGetUserByIdPermission(user, id, role);

        return UserMapper.toDto(user);
    }

    @Override
    public String updateAvatar(UUID targetId, UUID requesterId, MultipartFile file) throws IOException {
        User user = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userPermissionValidator.validateUpdateAvatarPermission(user, requesterId);

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "user_avatars",
                        "public_id", targetId.toString(),
                        "resource_type", "image",
                        "overwrite", true
                )
        );

        String imageUrl = (String) uploadResult.get("secure_url");

        user.setAvatarUrl(imageUrl);

        userRepository.save(user);

        return imageUrl;
    }

    @Override
    public void updateBalance(PaymentRequestDto dto, UUID id) {
        BigDecimal amount = dto.getAmount();
        PaymentType type = dto.getType();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BigDecimal userBalance = user.getBalance();

        switch (type) {
            case DEPOSIT -> user.setBalance(userBalance.add(amount));
            case WITHDRAW -> {
                if (userBalance.compareTo(amount) < 0) {
                    throw new InsufficientBalanceException("Not enough balance to withdraw");
                }
                user.setBalance(userBalance.subtract(amount));
            }
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void transfer(TransferRequestDto dto) {
        UUID senderId = dto.getFromUserId();
        UUID receiverId = dto.getToUserId();
        BigDecimal amount = dto.getAmount();
        TransferType type = dto.getType();

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        switch (type) {
            case APPOINTMENT -> {
                if (sender.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientBalanceException("Patient does not have enough balance for appointment");
                }
                sender.setBalance(sender.getBalance().subtract(amount));
                receiver.setPendingBalance(receiver.getPendingBalance().add(amount));
            }
            case REFUND -> {
                if (receiver.getPendingBalance().compareTo(amount) < 0) {
                    throw new InsufficientBalanceException("Clinic does not have enough pending balance to refund");
                }
                sender.setBalance(sender.getBalance().add(amount));
                receiver.setPendingBalance(receiver.getPendingBalance().subtract(amount));
            }
            case SETTLE -> {
                if (receiver.getPendingBalance().compareTo(amount) < 0) {
                    throw new InsufficientBalanceException("Clinic does not have enough pending balance to settle");
                }
                receiver.setPendingBalance(receiver.getPendingBalance().subtract(amount));
                receiver.setBalance(receiver.getBalance().add(amount));
            }
        }

        userRepository.save(sender);
        userRepository.save(receiver);
    }
}
