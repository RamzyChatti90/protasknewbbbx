package com.protasknewbbbx.service.dto;

import com.protasknewbbbx.domain.enumeration.TaskStatus;
import java.io.Serializable;
import java.util.Objects;

public class TaskStatsDTO implements Serializable {

    private TaskStatus status;
    private Long count;

    public TaskStatsDTO() {
        // Empty constructor needed for Jackson/Spring
    }

    public TaskStatsDTO(TaskStatus status, Long count) {
        this.status = status;
        this.count = count;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskStatsDTO that = (TaskStatsDTO) o;
        return status == that.status && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, count);
    }

    @Override
    public String toString() {
        return "TaskStatsDTO{" +
               "status=" + status +
               ", count=" + count +
               '}';
    }
}
