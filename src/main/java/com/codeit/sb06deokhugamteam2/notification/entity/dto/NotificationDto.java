package com.codeit.sb06deokhugamteam2.notification.entity.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationDto {
  UUID id;
  UUID userId;
  UUID reviewId;
  String reviewTitle;
  String content;
  Boolean confirmed;
  Instant createdAt;
  Instant updatedAt;
}
