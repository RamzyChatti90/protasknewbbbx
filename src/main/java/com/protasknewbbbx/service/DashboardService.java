package com.protasknewbbbx.service;

import com.protasknewbbbx.domain.Task;
import com.protasknewbbbx.domain.User;
import com.protasknewbbbx.domain.enumeration.TaskStatus;
import com.protasknewbbbx.repository.TaskRepository;
import com.protasknewbbbx.repository.UserRepository;
import com.protasknewbbbx.security.SecurityUtils;
import com.protasknewbbbx.service.dto.OverdueTasksDTO;
import com.protasknewbbbx.service.dto.TaskProgressDTO;
import com.protasknewbbbx.service.dto.TaskStatsDTO;
import java.time.Instant;
import java.util.List;
import java.util.Map;
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
    private final UserRepository userRepository;

    public DashboardService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get task statistics by status for the current user.
     * @return TaskStatsDTO containing a map of TaskStatus to count.
     */
    public TaskStatsDTO getTaskStatsByStatusForCurrentUser() {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Current user login not found"));

        User currentUser = userRepository.findOneByLogin(userLogin)
            .orElseThrow(() -> new IllegalStateException("User not found with login: " + userLogin));

        log.debug("Request to get task stats by status for user: {}", userLogin);

        List<Task> tasks = taskRepository.findByUser(currentUser);
        Map<TaskStatus, Long> statusCounts = tasks.stream()
            .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        // Ensure all statuses are present, even with 0 count
        for (TaskStatus status : TaskStatus.values()) {
            statusCounts.putIfAbsent(status, 0L);
        }

        return new TaskStatsDTO(statusCounts);
    }

    /**
     * Get the count of overdue tasks for the current user.
     * @return OverdueTasksDTO containing the count of overdue tasks.
     */
    public OverdueTasksDTO getOverdueTasksCountForCurrentUser() {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Current user login not found"));

        User currentUser = userRepository.findOneByLogin(userLogin)
            .orElseThrow(() -> new IllegalStateException("User not found with login: " + userLogin));

        log.debug("Request to get overdue tasks count for user: {}", userLogin);

        Instant now = Instant.now();
        Long overdueCount = taskRepository.countByUserAndDueDateBeforeAndStatusIsNot(currentUser, now, TaskStatus.DONE);

        return new OverdueTasksDTO(overdueCount);
    }

    /**
     * Get the overall task progress for the current user.
     * @return TaskProgressDTO containing completed tasks count and total tasks count.
     */
    public TaskProgressDTO getTaskProgressForCurrentUser() {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Current user login not found"));

        User currentUser = userRepository.findOneByLogin(userLogin)
            .orElseThrow(() -> new IllegalStateException("User not found with login: " + userLogin));

        log.debug("Request to get task progress for user: {}", userLogin);

        Long totalTasks = taskRepository.countByUser(currentUser);
        Long completedTasks = taskRepository.countByUserAndStatus(currentUser, TaskStatus.DONE);

        return new TaskProgressDTO(completedTasks, totalTasks);
    }
}