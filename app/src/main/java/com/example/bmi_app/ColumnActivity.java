package com.example.bmi_app;

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
import java.util.List;
import java.util.Map;

public class ColumnActivity extends AppCompatActivity {
    private ListView listView;

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

        listView = findViewById(R.id.listView);
        loadBMIRecords();
    }

    private List<Map<String, String>> getBMIRecordsAsList(List<BMIRecord> bmiRecords) {
        List<Map<String, String>> data = new ArrayList<>();

        for (BMIRecord record : bmiRecords) {
            Map<String, String> map = new HashMap<>();
            map.put("date", record.getDate());
            map.put("weight", String.valueOf(record.getWeight()));
            map.put("height", String.valueOf(record.getHeight()));
            map.put("bmi", String.valueOf(record.getBmi()));
            map.put("result", record.getResult());
            data.add(map);
        }
        return data;
    }

    private void loadBMIRecords() {
        try (DatabaseHelper db = new DatabaseHelper(this)) {
            List<BMIRecord> bmiRecords = db.getAllBMIRecords();

            if (bmiRecords.isEmpty()) {
                Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
            } else {
                // Convert BMIRecords to List<Map<String, String>>
                List<Map<String, String>> bmiData = getBMIRecordsAsList(bmiRecords);

                // Set up the SimpleAdapter
                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        this,
                        bmiData,
                        R.layout.activity_column,  // Your layout for each list item
                        new String[]{"date", "weight", "height", "bmi", "result"},
                        new int[]{R.id.col_date, R.id.col_weight, R.id.col_height, R.id.col_bmi, R.id.col_result}
                );

                listView.setAdapter(simpleAdapter);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading records", Toast.LENGTH_SHORT).show();
        }
    }
}
