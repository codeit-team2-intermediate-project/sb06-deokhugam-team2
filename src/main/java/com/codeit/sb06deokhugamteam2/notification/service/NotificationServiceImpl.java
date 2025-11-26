package com.codeit.sb06deokhugamteam2.notification.service;

import com.codeit.sb06deokhugamteam2.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
  private final NotificationRepository repository;


}
