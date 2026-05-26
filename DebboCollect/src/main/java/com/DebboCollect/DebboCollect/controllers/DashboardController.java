package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.DashboardResponse;
import com.DebboCollect.DebboCollect.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('SUPERVISEUR','BAILLEUR')")
    public DashboardResponse getDashboard() {

        return dashboardService.getDashboard();
    }
}
