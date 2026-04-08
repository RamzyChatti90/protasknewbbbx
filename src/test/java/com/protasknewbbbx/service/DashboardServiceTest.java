package com.protasknewbbbx.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.protasknewbbbx.domain.Task;
import com.protasknewbbbx.domain.enumeration.TaskStatus;
import com.protasknewbbbx.repository.TaskRepository;
import com.protasknewbbbx.security.SecurityUtils;
import com.protasknewbbbx.service.dto.OverdueTasksDTO;
import com.protasknewbbbx.service.dto.TaskProgressDTO;
import com.protasknewbbbx.service.dto.TaskStatsDTO;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private static final String TEST_USER_LOGIN = "testuser";

    // SecurityUtils.getCurrentUserLogin() needs to be mocked for each test that uses it.
    // Using MockedStatic within each test or a helper method for clarity.

    @Test
    void getTaskStatsByStatus_shouldReturnCorrectCounts() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of(TEST_USER_LOGIN));

            // Given
            when(taskRepository.countByOwnerLoginAndStatus(TEST_USER_LOGIN, TaskStatus.TODO)).thenReturn(5L);
            when(taskRepository.countByOwnerLoginAndStatus(TEST_USER_LOGIN, TaskStatus.IN_PROGRESS)).thenReturn(3L);
            when(taskRepository.countByOwnerLoginAndStatus(TEST_USER_LOGIN, TaskStatus.DONE)).thenReturn(2L);
            when(taskRepository.countByOwnerLoginAndStatus(TEST_USER_LOGIN, TaskStatus.CANCELLED)).thenReturn(1L);

            // When
            TaskStatsDTO result = dashboardService.getTaskStatsByStatus();

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getCountsByStatus()).hasSize(TaskStatus.values().length);
            assertThat(result.getCountsByStatus().get(TaskStatus.TODO)).isEqualTo(5L);
            assertThat(result.getCountsByStatus().get(TaskStatus.IN_PROGRESS)).isEqualTo(3L);
            assertThat(result.getCountsByStatus().get(TaskStatus.DONE)).isEqualTo(2L);
            assertThat(result.getCountsByStatus().get(TaskStatus.CANCELLED)).isEqualTo(1L);

            verify(taskRepository, times(TaskStatus.values().length)).countByOwnerLoginAndStatus(anyString(), any(TaskStatus.class));
        }
    }

    @Test
    void getOverdueTasksCount_shouldReturnCorrectCount() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of(TEST_USER_LOGIN));

            // Given
            when(taskRepository.countByOwnerLoginAndDueDateBeforeAndStatusIsNot(eq(TEST_USER_LOGIN), any(Instant.class), eq(TaskStatus.DONE)))
                .thenReturn(7L);

            // When
            OverdueTasksDTO result = dashboardService.getOverdueTasksCount();

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getOverdueCount()).isEqualTo(7L);

            verify(taskRepository).countByOwnerLoginAndDueDateBeforeAndStatusIsNot(eq(TEST_USER_LOGIN), any(Instant.class), eq(TaskStatus.DONE));
        }
    }

    @Test
    void getTaskProgress_shouldReturnCorrectProgress() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of(TEST_USER_LOGIN));

            // Given
            when(taskRepository.countByOwnerLogin(TEST_USER_LOGIN)).thenReturn(10L);
            when(taskRepository.countByOwnerLoginAndStatus(TEST_USER_LOGIN, TaskStatus.DONE)).thenReturn(4L);

            // When
            TaskProgressDTO result = dashboardService.getTaskProgress();

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTotalTasks()).isEqualTo(10L);
            assertThat(result.getCompletedTasks()).isEqualTo(4L);
            assertThat(result.getCompletionPercentage()).isEqualTo(40.0);

            verify(taskRepository).countByOwnerLogin(TEST_USER_LOGIN);
            verify(taskRepository).countByOwnerLoginAndStatus(TEST_USER_LOGIN, TaskStatus.DONE);
        }
    }

    @Test
    void getTaskProgress_shouldHandleZeroTotalTasks() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of(TEST_USER_LOGIN));

            // Given
            when(taskRepository.countByOwnerLogin(TEST_USER_LOGIN)).thenReturn(0L);
            when(taskRepository.countByOwnerLoginAndStatus(TEST_USER_LOGIN, TaskStatus.DONE)).thenReturn(0L);

            // When
            TaskProgressDTO result = dashboardService.getTaskProgress();

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTotalTasks()).isEqualTo(0L);
            assertThat(result.getCompletedTasks()).isEqualTo(0L);
            assertThat(result.getCompletionPercentage()).isEqualTo(0.0);

            verify(taskRepository).countByOwnerLogin(TEST_USER_LOGIN);
            verify(taskRepository).countByOwnerLoginAndStatus(TEST_USER_LOGIN, TaskStatus.DONE);
        }
    }
}
