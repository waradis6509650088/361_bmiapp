package com.example.bmi_app;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
    public static final String DATABASE_NAME = "bmi_data.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "bmi_records";
    public static final String DATE = "date";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static final String BMI = "bmi";
    public static final String RESULT = "result";
}