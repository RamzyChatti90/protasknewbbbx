package com.protasknewbbbx.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.protasknewbbbx.ProtasknewbbbxApp;
import com.protasknewbbbx.config.Constants;
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
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ProtasknewbbbxApp.class)
@Transactional
class DashboardServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("testuser");
        testUser.setEmail("testuser@localhost");

        // Mock SecurityContextHolder for SecurityUtils
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("testuser", "password"));
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findOneByLogin("testuser")).thenReturn(Optional.of(testUser));
    }

    @Test
    void getTaskStatsByStatusForCurrentUser_shouldReturnCorrectCounts() {
        Task task1 = new Task().status(TaskStatus.TODO).user(testUser);
        Task task2 = new Task().status(TaskStatus.TODO).user(testUser);
        Task task3 = new Task().status(TaskStatus.IN_PROGRESS).user(testUser);
        Task task4 = new Task().status(TaskStatus.DONE).user(testUser);

        List<Task> tasks = Arrays.asList(task1, task2, task3, task4);
        when(taskRepository.findByUser(testUser)).thenReturn(tasks);

        TaskStatsDTO result = dashboardService.getTaskStatsByStatusForCurrentUser();

        assertThat(result).isNotNull();
        Map<TaskStatus, Long> expectedCounts = new HashMap<>();
        expectedCounts.put(TaskStatus.TODO, 2L);
        expectedCounts.put(TaskStatus.IN_PROGRESS, 1L);
        expectedCounts.put(TaskStatus.DONE, 1L);
        assertThat(result.getStatusCounts()).isEqualTo(expectedCounts);
    }

    @Test
    void getOverdueTasksCountForCurrentUser_shouldReturnCorrectCount() {
        Instant now = Instant.now();
        Instant past = now.minus(1, ChronoUnit.DAYS);

        when(taskRepository.countByUserAndDueDateBeforeAndStatusIsNot(eq(testUser), any(Instant.class), eq(TaskStatus.DONE)))
            .thenReturn(2L);

        OverdueTasksDTO result = dashboardService.getOverdueTasksCountForCurrentUser();

        assertThat(result).isNotNull();
        assertThat(result.getOverdueCount()).isEqualTo(2L);
    }

    @Test
    void getTaskProgressForCurrentUser_shouldReturnCorrectProgress() {
        when(taskRepository.countByUser(testUser)).thenReturn(10L);
        when(taskRepository.countByUserAndStatus(testUser, TaskStatus.DONE)).thenReturn(3L);

        TaskProgressDTO result = dashboardService.getTaskProgressForCurrentUser();

        assertThat(result).isNotNull();
        assertThat(result.getCompletedTasks()).isEqualTo(3L);
        assertThat(result.getTotalTasks()).isEqualTo(10L);
        assertThat(result.getProgressPercentage()).isEqualTo(30.0);
    }

    @Test
    void getTaskProgressForCurrentUser_noTasks_shouldReturnZeroProgress() {
        when(taskRepository.countByUser(testUser)).thenReturn(0L);
        when(taskRepository.countByUserAndStatus(testUser, TaskStatus.DONE)).thenReturn(0L);

        TaskProgressDTO result = dashboardService.getTaskProgressForCurrentUser();

        assertThat(result).isNotNull();
        assertThat(result.getCompletedTasks()).isEqualTo(0L);
        assertThat(result.getTotalTasks()).isEqualTo(0L);
        assertThat(result.getProgressPercentage()).isEqualTo(0.0);
    }
}