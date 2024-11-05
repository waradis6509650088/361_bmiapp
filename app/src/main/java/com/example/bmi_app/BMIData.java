package com.example.bmi_app;

import static android.provider.BaseColumns._ID;
import static com.example.bmi_app.Constants.BMI;
import static com.example.bmi_app.Constants.DATABASE_NAME;
import static com.example.bmi_app.Constants.DATABASE_VERSION;
import static com.example.bmi_app.Constants.DATE;
import static com.example.bmi_app.Constants.HEIGHT;
import static com.example.bmi_app.Constants.RESULT;
import static com.example.bmi_app.Constants.TABLE_NAME;
import static com.example.bmi_app.Constants.WEIGHT;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BMIData extends SQLiteOpenHelper {
    public BMIData(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                _ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE + " TEXT, " +
                WEIGHT + " REAL, " +
                HEIGHT + " REAL, " +
                BMI + " REAL, " +
                RESULT + " TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }
}
