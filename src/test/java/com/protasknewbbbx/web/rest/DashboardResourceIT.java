package com.protasknewbbbx.web.rest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.protasknewbbbx.IntegrationTest;
import com.protasknewbbbx.domain.enumeration.TaskStatus;
import com.protasknewbbbx.service.DashboardService;
import com.protasknewbbbx.service.dto.OverdueTasksDTO;
import com.protasknewbbbx.service.dto.TaskProgressDTO;
import com.protasknewbbbx.service.dto.TaskStatsDTO;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link DashboardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DashboardResourceIT {

    @Autowired
    private ObjectMapper om; // For JSON serialization/deserialization

    @MockBean
    private DashboardService dashboardService;

    @Autowired
    private MockMvc restDashboardMockMvc;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test if necessary
        reset(dashboardService);
    }

    @Test
    void getTaskStatsByStatus() throws Exception {
        Map<TaskStatus, Long> counts = new HashMap<>();
        counts.put(TaskStatus.TODO, 5L);
        counts.put(TaskStatus.IN_PROGRESS, 3L);
        counts.put(TaskStatus.DONE, 2L);
        counts.put(TaskStatus.CANCELLED, 1L);
        TaskStatsDTO taskStatsDTO = new TaskStatsDTO(counts);

        when(dashboardService.getTaskStatsByStatus()).thenReturn(taskStatsDTO);

        restDashboardMockMvc
            .perform(get("/api/dashboard/task-stats").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.countsByStatus.TODO").value(5))
            .andExpect(jsonPath("$.countsByStatus.IN_PROGRESS").value(3))
            .andExpect(jsonPath("$.countsByStatus.DONE").value(2))
            .andExpect(jsonPath("$.countsByStatus.CANCELLED").value(1));

        verify(dashboardService).getTaskStatsByStatus();
    }

    @Test
    void getOverdueTasksCount() throws Exception {
        OverdueTasksDTO overdueTasksDTO = new OverdueTasksDTO(10L);

        when(dashboardService.getOverdueTasksCount()).thenReturn(overdueTasksDTO);

        restDashboardMockMvc
            .perform(get("/api/dashboard/overdue-tasks").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.overdueCount").value(10));

        verify(dashboardService).getOverdueTasksCount();
    }

    @Test
    void getTaskProgress() throws Exception {
        TaskProgressDTO taskProgressDTO = new TaskProgressDTO(20L, 10L, 50.0);

        when(dashboardService.getTaskProgress()).thenReturn(taskProgressDTO);

        restDashboardMockMvc
            .perform(get("/api/dashboard/task-progress").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.totalTasks").value(20))
            .andExpect(jsonPath("$.completedTasks").value(10))
            .andExpect(jsonPath("$.completionPercentage").value(50.0));

        verify(dashboardService).getTaskProgress();
    }
}
