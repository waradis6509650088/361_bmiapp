package com.example.bmi_app;

import static com.example.bmi_app.Constants.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ColumnActivity extends AppCompatActivity {
    private BMIData dbHelper;
    private ArrayList<HashMap<String, String>> bmiList;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_column);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database helper
        dbHelper = new BMIData(this, DATABASE_NAME, null, DATABASE_VERSION);

        // Initialize ArrayList for storing BMI records
        bmiList = new ArrayList<>();

        // Get the ListView reference
        ListView listView = findViewById(R.id.listView);

        // Load data from database
        loadBMIData();

        // Create adapter with ViewBinder to handle custom formatting
        adapter = new SimpleAdapter(
                this,
                bmiList,
                R.layout.column,
                new String[]{DATE, WEIGHT, HEIGHT, BMI, RESULT},
                new int[]{R.id.col_date, R.id.col_weight, R.id.col_height, R.id.col_bmi, R.id.col_result}
        );

        // Set custom ViewBinder to handle result localization
        adapter.setViewBinder((view, data, textRepresentation) -> {
            if (view.getId() == R.id.col_result) {
                // Convert category key to localized string
                String localizedResult = MainActivity.getLocalizedResult(ColumnActivity.this, textRepresentation);
                TextView textView = (TextView) view;
                textView.setText(localizedResult);
                return true;
            }
            return false;
        });

        listView.setAdapter(adapter);
    }

    private void loadBMIData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        bmiList.clear();
        try {
            // Query to select all records, ordered by date descending
            String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY date DESC";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();

                    // Get column indices
                    int dateIndex = cursor.getColumnIndex(DATE);
                    int weightIndex = cursor.getColumnIndex(WEIGHT);
                    int heightIndex = cursor.getColumnIndex(HEIGHT);
                    int bmiIndex = cursor.getColumnIndex(BMI);
                    int resultIndex = cursor.getColumnIndex(RESULT);

                    // Add data to HashMap
                    map.put(DATE, cursor.getString(dateIndex));
                    map.put(WEIGHT, String.format("%.2f", cursor.getDouble(weightIndex)));
                    map.put(HEIGHT, String.format("%.2f", cursor.getDouble(heightIndex)));
                    map.put(BMI, String.format("%.2f", cursor.getDouble(bmiIndex)));
                    map.put(RESULT, cursor.getString(resultIndex)); // This will be the category key

                    // Add HashMap to ArrayList
                    bmiList.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            Collections.reverse(bmiList);
        } catch (Exception e) {
            Log.e("ColumnActivity", "Error loading BMI data", e);
        } finally {
            db.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list when returning to the activity
        // This ensures correct language display after language changes
        if (adapter != null) {
            bmiList.clear();
            loadBMIData();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}