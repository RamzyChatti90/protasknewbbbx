package com.protasknewbbbx.service;

import com.protasknewbbbx.domain.enumeration.TaskStatus;
import com.protasknewbbbx.repository.TaskRepository;
import com.protasknewbbbx.security.SecurityUtils;
import com.protasknewbbbx.service.dto.OverdueTasksDTO;
import com.protasknewbbbx.service.dto.TaskProgressDTO;
import com.protasknewbbbx.service.dto.TaskStatsDTO;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DashboardService {

    private final Logger log = LoggerFactory.getLogger(DashboardService.class);

    private final TaskRepository taskRepository;

    public DashboardService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Get task statistics by status for the current user.
     * @return a list of TaskStatsDTO.
     */
    @Transactional(readOnly = true)
    public List<TaskStatsDTO> getTaskStatsByStatus() {
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Current user login not found"));

        List<Object[]> stats = taskRepository.countTasksByStatusForUser(currentUserLogin);
        return stats
            .stream()
            .map(
                row ->
                    new TaskStatsDTO(
                        TaskStatus.valueOf(row[0].toString()),
                        (Long) row[1]
                    )
            )
            .collect(Collectors.toList());
    }

    /**
     * Get the number of overdue tasks for the current user.
     * @return an OverdueTasksDTO.
     */
    @Transactional(readOnly = true)
    public OverdueTasksDTO getOverdueTasksCount() {
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Current user login not found"));

        long overdueCount = taskRepository.countOverdueTasksForUser(currentUserLogin, Instant.now());
        return new OverdueTasksDTO(overdueCount);
    }

    /**
     * Get the overall task progress for the current user.
     * @return a TaskProgressDTO.
     */
    @Transactional(readOnly = true)
    public TaskProgressDTO getTaskProgress() {
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Current user login not found"));

        long totalTasks = taskRepository.countByUserLogin(currentUserLogin);
        long completedTasks = taskRepository.countByUserLoginAndStatus(currentUserLogin, TaskStatus.COMPLETED);

        double progress = (totalTasks == 0) ? 0 : ((double) completedTasks / totalTasks) * 100;
        return new TaskProgressDTO(totalTasks, completedTasks, progress);
    }
}