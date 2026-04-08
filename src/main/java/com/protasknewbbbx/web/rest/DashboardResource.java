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
     * {@code GET /dashboard/task-stats-by-status} : get task statistics by status for the current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the {@link TaskStatsDTO}.
     */
    @GetMapping("/task-stats-by-status")
    public ResponseEntity<TaskStatsDTO> getTaskStatsByStatus() {
        log.debug("REST request to get task stats by status for current user");
        TaskStatsDTO result = dashboardService.getTaskStatsByStatusForCurrentUser();
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET /dashboard/overdue-tasks-count} : get the count of overdue tasks for the current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the {@link OverdueTasksDTO}.
     */
    @GetMapping("/overdue-tasks-count")
    public ResponseEntity<OverdueTasksDTO> getOverdueTasksCount() {
        log.debug("REST request to get overdue tasks count for current user");
        OverdueTasksDTO result = dashboardService.getOverdueTasksCountForCurrentUser();
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET /dashboard/task-progress} : get the overall task progress for the current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the {@link TaskProgressDTO}.
     */
    @GetMapping("/task-progress")
    public ResponseEntity<TaskProgressDTO> getTaskProgress() {
        log.debug("REST request to get task progress for current user");
        TaskProgressDTO result = dashboardService.getTaskProgressForCurrentUser();
        return ResponseEntity.ok().body(result);
    }
}