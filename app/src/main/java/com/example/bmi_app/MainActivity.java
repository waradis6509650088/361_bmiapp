package com.example.bmi_app;

import static com.example.bmi_app.Constants.BMI;
import static com.example.bmi_app.Constants.DATABASE_NAME;
import static com.example.bmi_app.Constants.DATABASE_VERSION;
import static com.example.bmi_app.Constants.DATE;
import static com.example.bmi_app.Constants.HEIGHT;
import static com.example.bmi_app.Constants.RESULT;
import static com.example.bmi_app.Constants.TABLE_NAME;
import static com.example.bmi_app.Constants.WEIGHT;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String CATEGORY_UNDERWEIGHT = "underweight";
    private static final String CATEGORY_NORMAL = "normal";
    private static final String CATEGORY_OVERWEIGHT = "overweight";
    private static final String CATEGORY_OBESE = "obese";
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

                String categoryKey;
                String resultMessage;
                int backgroundColor;


                if (bmi < 18.5) {
                    categoryKey = CATEGORY_UNDERWEIGHT;
                    resultMessage = getString(R.string.underweight);
                    backgroundColor = ContextCompat.getColor(MainActivity.this, R.color.red_underweight);
                } else if (bmi < 24.9) {
                    categoryKey = CATEGORY_NORMAL;
                    resultMessage = getString(R.string.normal_weight);
                    backgroundColor = ContextCompat.getColor(MainActivity.this, R.color.green);
                } else if (bmi < 29.9) {
                    categoryKey = CATEGORY_OVERWEIGHT;
                    resultMessage = getString(R.string.overweight);
                    backgroundColor = ContextCompat.getColor(MainActivity.this, R.color.yellow);
                } else {
                    categoryKey = CATEGORY_OBESE;
                    resultMessage = getString(R.string.obesity);
                    backgroundColor = ContextCompat.getColor(MainActivity.this, R.color.red_overweight);
                }

                resultTextView.setText(resultMessage);
                resultTextView.setBackgroundColor(backgroundColor);

                addBMI(categoryKey);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton historyButton = findViewById(R.id.history);
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ColumnActivity.class);
            startActivity(intent);
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

    private String getCurrentBuddhistEraDate() {
        Calendar calendar = Calendar.getInstance();
        int gregorianYear = calendar.get(Calendar.YEAR);
        int buddhistYear = gregorianYear + 543;
        calendar.set(Calendar.YEAR, buddhistYear);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
    private void addBMI(String categoryKey) {
        TextInputEditText weightInput = findViewById(R.id.input_weight);
        TextInputEditText heightInput = findViewById(R.id.input_height);
        TextInputEditText bmiOutput = findViewById(R.id.output_bmi);
        //TextInputEditText resultTextView = findViewById(R.id.result);

        // Get the current values from input fields
        String weightStr = Objects.requireNonNull(weightInput.getText()).toString().replace(",", "");
        String heightStr = Objects.requireNonNull(heightInput.getText()).toString().replace(",", "");
        String bmiStr = Objects.requireNonNull(bmiOutput.getText()).toString().replace(",", "");
        //String result = Objects.requireNonNull(resultTextView.getText()).toString();

        // Get current date in Buddhist Era
        String currentDate = getCurrentBuddhistEraDate();

        try {
            // Convert strings to appropriate data types
            double weight = Double.parseDouble(weightStr);
            double height = Double.parseDouble(heightStr);
            double bmi = Double.parseDouble(bmiStr);

            // Create database helper instance
            BMIData dbHelper = new BMIData(this, DATABASE_NAME, null, DATABASE_VERSION);

            // Get writable database
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Create ContentValues object to hold the data
            ContentValues values = new ContentValues();
            values.put(DATE, currentDate);
            values.put(WEIGHT, weight);
            values.put(HEIGHT, height);
            values.put(BMI, bmi);
            values.put(RESULT, categoryKey);

            // Insert the data into the database
            long newRowId = db.insert(TABLE_NAME, null, values);

            // Check if insertion was successful
            if (newRowId != -1) {
                Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.save_failed), Toast.LENGTH_SHORT).show();
            }

            // Close the database connection
            db.close();
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
        }
    }
    public static String getLocalizedResult(Context context, String categoryKey) {
        switch (categoryKey) {
            case CATEGORY_UNDERWEIGHT:
                return context.getString(R.string.underweight);
            case CATEGORY_NORMAL:
                return context.getString(R.string.normal_weight);
            case CATEGORY_OVERWEIGHT:
                return context.getString(R.string.overweight);
            case CATEGORY_OBESE:
                return context.getString(R.string.obesity);
            default:
                return "";
        }
    }
}