package com.example.bmi_app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    DecimalFormat formatter = new DecimalFormat("#,###.##");
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

        TextInputEditText weightInput = findViewById(R.id.input_weight);
        weightInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(8, 2)});
        TextInputEditText heightInput = findViewById(R.id.input_height);
        heightInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(8, 2)});

        TextInputEditText bmiOutput = findViewById(R.id.output_bmi);
        TextInputEditText resultTextView = findViewById(R.id.result);
        Button calculateButton = findViewById(R.id.button_calculate);

        calculateButton.setOnClickListener(v -> {
            String weightStr = Objects.requireNonNull(weightInput.getText()).toString().replace(",","");
            String heightStr = Objects.requireNonNull(heightInput.getText()).toString().replace(",","");

            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(MainActivity.this, getString(R.string.empty_input), Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double weight = Double.parseDouble(weightStr);
                double height = Double.parseDouble(heightStr) / 100;

                if (height <= 0) {
                    Toast.makeText(MainActivity.this, getString(R.string.height_zero), Toast.LENGTH_SHORT).show();
                    return;
                }

                double bmi = weight / (height * height);

                String formattedWeight = formatter.format(weight);
                String formattedHeight = formatter.format(height * 100);
                String formattedBmi = formatter.format(bmi);

                weightInput.setText(formattedWeight);
                heightInput.setText(formattedHeight);
                bmiOutput.setText(formattedBmi);

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

        ImageButton historyButton = findViewById(R.id.history);
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, )
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        TextView topText = findViewById(R.id.top_text);
        TextView nameWeight = findViewById(R.id.name_weight);
        TextInputEditText inputWeight = findViewById(R.id.input_weight);
        TextView nameHeight = findViewById(R.id.name_height);
        TextInputEditText inputHeight = findViewById(R.id.input_height);
        TextView nameResult = findViewById(R.id.name_result);
        TextInputEditText outputBmi = findViewById(R.id.output_bmi);
        TextView nameCategory = findViewById(R.id.name_category);
        TextInputEditText resultCategory = findViewById(R.id.result);
        Button calculateButton = findViewById(R.id.button_calculate);


        float fontScale = newConfig.fontScale;


        float fontSizeLarge = 30 * fontScale;
        float fontSizeNormal = 20 * fontScale;


        topText.setTextSize(fontSizeLarge);
        nameWeight.setTextSize(fontSizeNormal);
        inputWeight.setTextSize(fontSizeNormal);
        nameHeight.setTextSize(fontSizeNormal);
        inputHeight.setTextSize(fontSizeNormal);
        nameResult.setTextSize(fontSizeNormal);
        outputBmi.setTextSize(fontSizeNormal);
        nameCategory.setTextSize(fontSizeNormal);
        resultCategory.setTextSize(fontSizeNormal);
        calculateButton.setTextSize(fontSizeNormal);
    }


    private class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;
        DecimalDigitsInputFilter(int digits, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digits - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) +
                    "})?)||(\\.)?");
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

}