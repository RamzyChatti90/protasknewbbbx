package com.protasknewbbbx.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class TaskProgressDTO implements Serializable {

    private Long totalTasks;
    private Long completedTasks;
    private Double completionPercentage;

    public TaskProgressDTO() {
        // Empty constructor needed for Jackson.
    }

    public TaskProgressDTO(Long totalTasks, Long completedTasks, Double completionPercentage) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.completionPercentage = completionPercentage;
    }

    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Double getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(Double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskProgressDTO that = (TaskProgressDTO) o;
        return Objects.equals(totalTasks, that.totalTasks) &&
               Objects.equals(completedTasks, that.completedTasks) &&
               Objects.equals(completionPercentage, that.completionPercentage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalTasks, completedTasks, completionPercentage);
    }

    @Override
    public String toString() {
        return "TaskProgressDTO{" +
               "totalTasks=" + totalTasks +
               ", completedTasks=" + completedTasks +
               ", completionPercentage=" + completionPercentage +
               '}';
    }
}
