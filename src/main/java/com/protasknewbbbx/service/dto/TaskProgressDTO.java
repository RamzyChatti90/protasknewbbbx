package com.protasknewbbbx.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class TaskProgressDTO implements Serializable {

    private Long completedTasks;
    private Long totalTasks;

    public TaskProgressDTO() {
        // Empty constructor needed for Jackson/Spring
    }

    public TaskProgressDTO(Long completedTasks, Long totalTasks) {
        this.completedTasks = completedTasks;
        this.totalTasks = totalTasks;
    }

    public Long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Double getProgressPercentage() {
        if (totalTasks == null || totalTasks == 0) {
            return 0.0;
        }
        return (double) completedTasks / totalTasks * 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskProgressDTO that = (TaskProgressDTO) o;
        return Objects.equals(completedTasks, that.completedTasks) &&
               Objects.equals(totalTasks, that.totalTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completedTasks, totalTasks);
    }

    @Override
    public String toString() {
        return "TaskProgressDTO{" +
               "completedTasks=" + completedTasks +
               ", totalTasks=" + totalTasks +
               '}';
    }
}