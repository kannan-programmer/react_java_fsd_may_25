package com.springboot.assetsphere.dto;

import java.util.List;

import org.springframework.stereotype.Component;
@Component
public class HRDashboardStatsDto {
    private List<String> categories;
    private List<Integer> counts;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Integer> getCounts() {
        return counts;
    }

    public void setCounts(List<Integer> counts) {
        this.counts = counts;
    }
}