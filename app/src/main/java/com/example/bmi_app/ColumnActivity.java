package com.example.bmi_app;

import static com.example.bmi_app.Constants.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class ColumnActivity extends AppCompatActivity {
    private BMIData dbHelper;
    private ArrayList<HashMap<String, String>> bmiList;

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

        // Create adapter and set to ListView
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                bmiList,
                R.layout.column,  // You'll need to create this layout
                new String[]{DATE, WEIGHT, HEIGHT, BMI, RESULT},
                new int[]{R.id.col_date, R.id.col_weight, R.id.col_height, R.id.col_bmi, R.id.col_result}
        );

        listView.setAdapter(adapter);
    }

    private void loadBMIData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

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
                    map.put(RESULT, cursor.getString(resultIndex));

                    // Add HashMap to ArrayList
                    bmiList.add(map);

                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
            Toast.makeText(this, "Error loading BMI records", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            db.close();
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