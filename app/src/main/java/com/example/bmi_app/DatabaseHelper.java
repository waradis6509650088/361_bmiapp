package com.example.bmi_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bmi_data.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME  = "bmi_records";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_BMI = "bmi";
    private static final String COLUMN_RESULT = "result";

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_WEIGHT + " REAL, " +
                COLUMN_HEIGHT + " REAL, " +
                COLUMN_BMI + " REAL, " +
                COLUMN_RESULT + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertBMIRecord(String date, double weight, double height, double bmi, String result) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DATE, date);
            values.put(COLUMN_WEIGHT, weight);
            values.put(COLUMN_HEIGHT, height);
            values.put(COLUMN_BMI, bmi);
            values.put(COLUMN_RESULT, result);
            db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Toast.makeText(context, "Error inserting BMI record", Toast.LENGTH_SHORT).show();
            Log.e("DatabaseHelper", "Error inserting BMI record", e);
        }
    }

    public List<BMIRecord> getAllBMIRecords() {
        List<BMIRecord> bmiRecords = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC";

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                    double weight = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT));
                    double height = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT));
                    double bmi = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BMI));
                    String result = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESULT));


                } while (cursor.moveToNext());
            }
        }
    }
}