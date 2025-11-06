package com.sgu.payment_service.repository;

import com.sgu.payment_service.model.Payment;
import com.sgu.payment_service.enums.PaymentType;
import com.sgu.payment_service.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    
    List<Payment> findByUserIdOrderByCreatedAtDesc(String userId);
    
    List<Payment> findByUserIdAndPaymentTypeOrderByCreatedAtDesc(String userId, PaymentType paymentType);
    
    List<Payment> findByStatusOrderByCreatedAtDesc(PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.userId = :userId AND p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
    List<Payment> findByUserIdAndDateRange(
            @Param("userId") String userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    Optional<Payment> findByTransactionId(String transactionId);
}