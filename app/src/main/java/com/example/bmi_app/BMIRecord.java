package com.example.bmi_app;

import java.util.Locale;

public class BMIRecord {
    private int id;
    private String date;
    private double weight;
    private double height;
    private double bmi;
    private String result;

    public BMIRecord(int id, String date, double weight, double height, double bmi, String result) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public double getBmi() {
        return bmi;
    }

    public String getResult() {
        return result;
    }
    public String getFormattedWeight() {
        return String.format(Locale.US, "%.2f", weight);
    }

    public String getFormattedHeight() {
        return String.format(Locale.US, "%.2f", height);
    }

    public String getFormattedBmi() {
        return String.format(Locale.US, "%.2f", bmi);
    }
}