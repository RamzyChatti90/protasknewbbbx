package com.protasknewbbbx.web.rest;

import com.protasknewbbbx.service.DashboardService;
import com.protasknewbbbx.service.dto.OverdueTasksDTO;
import com.protasknewbbbx.service.dto.TaskProgressDTO;
import com.protasknewbbbx.service.dto.TaskStatsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing dashboard data.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * {@code GET /dashboard/task-stats} : get task statistics by status for the current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TaskStatsDTO.
     */
    @GetMapping("/task-stats")
    public ResponseEntity<TaskStatsDTO> getTaskStatsByStatus() {
        log.debug("REST request to get TaskStatsDTO");
        TaskStatsDTO result = dashboardService.getTaskStatsByStatus();
        return ResponseEntity.ok(result);
    }

    /**
     * {@code GET /dashboard/overdue-tasks} : get the count of overdue tasks for the current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the OverdueTasksDTO.
     */
    @GetMapping("/overdue-tasks")
    public ResponseEntity<OverdueTasksDTO> getOverdueTasksCount() {
        log.debug("REST request to get OverdueTasksDTO");
        OverdueTasksDTO result = dashboardService.getOverdueTasksCount();
        return ResponseEntity.ok(result);
    }

    /**
     * {@code GET /dashboard/task-progress} : get the overall task progression for the current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TaskProgressDTO.
     */
    @GetMapping("/task-progress")
    public ResponseEntity<TaskProgressDTO> getTaskProgress() {
        log.debug("REST request to get TaskProgressDTO");
        TaskProgressDTO result = dashboardService.getTaskProgress();
        return ResponseEntity.ok(result);
    }
}
