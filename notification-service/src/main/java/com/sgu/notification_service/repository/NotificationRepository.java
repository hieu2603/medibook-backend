// package com.sgu.notification_service.repository;

// import com.sgu.notification_service.model.Notification;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface NotificationRepository extends JpaRepository<Notification, Long> {

//     Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

//     @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.read = false")
//     Long countUnreadByUserId(Long userId);

//     @Modifying
//     @Query("UPDATE Notification n SET n.read = true WHERE n.id = :notificationId")
//     void markAsRead(Long notificationId);

//     @Modifying
//     @Query("UPDATE Notification n SET n.read = true WHERE n.userId = :userId")
//     void markAllAsRead(Long userId);
// }

package com.sgu.notification_service.repository;

import com.sgu.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    // Lấy tất cả thông báo của 1 user theo thời gian giảm dần
    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<Notification> findByUserId(UUID userId);

    // Đếm số thông báo chưa đọc
    long countByUserIdAndIsReadFalse(UUID userId);

    void deleteByUserId(UUID userId);

}
