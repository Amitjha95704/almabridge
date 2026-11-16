package com.almabridge.controller.admin;

import com.almabridge.dto.admin.AdminDashboardResponse;
import com.almabridge.dto.admin.analytics.AdminAnalyticsResponse;
import com.almabridge.service.admin.AdminDashboardAnalyticsService;
import com.almabridge.service.admin.AdminDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;
    private final AdminDashboardAnalyticsService analyticsService;
    
    @GetMapping("/overview")
    public ResponseEntity<AdminDashboardResponse> getDashboardOverview() {

        return ResponseEntity.ok(
                dashboardService.getDashboardOverview()
        );
    }
    
    @GetMapping("/analytics")
    public ResponseEntity<AdminAnalyticsResponse> getAnalytics() {

        return ResponseEntity.ok(
                analyticsService.getAnalytics()
        );
    }
}