package com.example.gymtrackapp.models;

public class Exercise {
    private String name;
    private String day;
    private int reps;
    private int sets;

    public Exercise(String name, String day, int reps, int sets) {
        this.name = name;
        this.day = day;
        this.reps = reps;
        this.sets = sets;
    }
    public Exercise() {

    }
    public String getName() {
        return name;
    }

    public String getDay() {
        return day;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }
}

