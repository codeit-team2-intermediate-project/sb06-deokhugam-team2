package com.codeit.sb06deokhugamteam2.dashboard.service;

import com.codeit.sb06deokhugamteam2.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashBoardRepository;
}
