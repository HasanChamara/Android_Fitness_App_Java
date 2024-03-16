package com.example.gymtrackapp.models;

import java.util.List;

public class WorkoutPlan {
    private String dayOfWeek;
    private List<List<String>> exercises;

    public WorkoutPlan(String dayOfWeek, List<List<String>> exercises) {
        this.dayOfWeek = dayOfWeek;
        this.exercises = exercises;
    }

    // Getters and setters
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public List<List<String>> getExercises() {
        return exercises;
    }

    public void setExercises(List<List<String>> exercises) {
        this.exercises = exercises;
    }
}
