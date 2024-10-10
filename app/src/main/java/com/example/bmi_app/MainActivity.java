package com.example.bmi_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final TextInputEditText weightInput = findViewById(R.id.input_weight);
        final TextInputEditText heightInput = findViewById(R.id.input_height);
        final TextInputEditText bmiOutput = findViewById(R.id.output_bmi);
        final TextInputEditText resultTextView = findViewById(R.id.result);
        final Button calculateButton = findViewById(R.id.button_calculate);

        calculateButton.setOnClickListener(v -> {
            String weightStr = Objects.requireNonNull(weightInput.getText()).toString();
            String heightStr = Objects.requireNonNull(heightInput.getText()).toString();

            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(MainActivity.this, getString(R.string.empty_input), Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float weight = Float.parseFloat(weightStr);
                float height = Float.parseFloat(heightStr) / 100;

                if (height <= 0) {
                    Toast.makeText(MainActivity.this, getString(R.string.height_zero), Toast.LENGTH_SHORT).show();
                    return;
                }

                float bmi = weight / (height * height);
                bmiOutput.setText(String.format(Locale.getDefault(), "%.2f", bmi));

                // Determine BMI category and set message and color
                String resultMessage;
                int backgroundColor;


                if (bmi < 18.5) {
                    resultMessage = getString(R.string.underweight);
                    backgroundColor = ContextCompat.getColor(MainActivity.this, R.color.red_underweight);
                } else if (bmi < 24.9) {
                    resultMessage = getString(R.string.normal_weight);
                    backgroundColor = ContextCompat.getColor(MainActivity.this, R.color.green);
                } else if (bmi < 29.9) {
                    resultMessage = getString(R.string.overweight);
                    backgroundColor = ContextCompat.getColor(MainActivity.this, R.color.yellow);
                } else {
                    resultMessage = getString(R.string.obesity);
                    backgroundColor = ContextCompat.getColor(MainActivity.this, R.color.red_overweight);
                }

                resultTextView.setText(resultMessage);
                resultTextView.setBackgroundColor(backgroundColor);

            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
