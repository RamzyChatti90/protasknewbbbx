package com.protasknewbbbx.service.dto;

import com.protasknewbbbx.domain.enumeration.TaskStatus;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class TaskStatsDTO implements Serializable {

    private Map<TaskStatus, Long> statusCounts;

    public TaskStatsDTO() {
        // Empty constructor needed for Jackson/Spring
    }

    public TaskStatsDTO(Map<TaskStatus, Long> statusCounts) {
        this.statusCounts = statusCounts;
    }

    public Map<TaskStatus, Long> getStatusCounts() {
        return statusCounts;
    }

    public void setStatusCounts(Map<TaskStatus, Long> statusCounts) {
        this.statusCounts = statusCounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskStatsDTO that = (TaskStatsDTO) o;
        return Objects.equals(statusCounts, that.statusCounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCounts);
    }

    @Override
    public String toString() {
        return "TaskStatsDTO{" +
               "statusCounts=" + statusCounts +
               '}';
    }
}