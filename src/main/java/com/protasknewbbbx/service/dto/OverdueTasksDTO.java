package com.protasknewbbbx.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class OverdueTasksDTO implements Serializable {

    private Long overdueCount;

    public OverdueTasksDTO() {
        // Empty constructor needed for Jackson/Spring
    }

    public OverdueTasksDTO(Long overdueCount) {
        this.overdueCount = overdueCount;
    }

    public Long getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(Long overdueCount) {
        this.overdueCount = overdueCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OverdueTasksDTO that = (OverdueTasksDTO) o;
        return Objects.equals(overdueCount, that.overdueCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(overdueCount);
    }

    @Override
    public String toString() {
        return "OverdueTasksDTO{" +
               "overdueCount=" + overdueCount +
               '}';
    }
}