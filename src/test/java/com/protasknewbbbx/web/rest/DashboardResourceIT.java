package com.protasknewbbbx.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.protasknewbbbx.ProtasknewbbbxApp;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DashboardResource} REST controller.
 */
@SpringBootTest(classes = ProtasknewbbbxApp.class)
@AutoConfigureMockMvc
@WithMockUser
class DashboardResourceIT {

    private static final String API_BASE_URL = "/api/dashboard";

    @Autowired
    private ObjectMapper om;

    @MockBean
    private DashboardService dashboardService;

    @Autowired
    private MockMvc restDashboardMockMvc;

    @BeforeEach
    public void setup() {
        // Reset mocks before each test
        reset(dashboardService);
    }

    @Test
    void getTaskStatsByStatus() throws Exception {
        Map<TaskStatus, Long> statusCounts = new HashMap<>();
        statusCounts.put(TaskStatus.TODO, 5L);
        statusCounts.put(TaskStatus.IN_PROGRESS, 3L);
        statusCounts.put(TaskStatus.DONE, 2L);
        TaskStatsDTO mockDto = new TaskStatsDTO(statusCounts);

        when(dashboardService.getTaskStatsByStatusForCurrentUser()).thenReturn(mockDto);

        restDashboardMockMvc
            .perform(get(API_BASE_URL + "/task-stats-by-status").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.statusCounts.TODO").value(5))
            .andExpect(jsonPath("$.statusCounts.IN_PROGRESS").value(3))
            .andExpect(jsonPath("$.statusCounts.DONE").value(2));

        verify(dashboardService, times(1)).getTaskStatsByStatusForCurrentUser();
    }

    @Test
    void getOverdueTasksCount() throws Exception {
        OverdueTasksDTO mockDto = new OverdueTasksDTO(7L);

        when(dashboardService.getOverdueTasksCountForCurrentUser()).thenReturn(mockDto);

        restDashboardMockMvc
            .perform(get(API_BASE_URL + "/overdue-tasks-count").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.overdueCount").value(7));

        verify(dashboardService, times(1)).getOverdueTasksCountForCurrentUser();
    }

    @Test
    void getTaskProgress() throws Exception {
        TaskProgressDTO mockDto = new TaskProgressDTO(10L, 20L);

        when(dashboardService.getTaskProgressForCurrentUser()).thenReturn(mockDto);

        restDashboardMockMvc
            .perform(get(API_BASE_URL + "/task-progress").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.completedTasks").value(10))
            .andExpect(jsonPath("$.totalTasks").value(20));

        verify(dashboardService, times(1)).getTaskProgressForCurrentUser();
    }
}