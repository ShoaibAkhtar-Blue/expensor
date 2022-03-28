package blue.project.expensor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import blue.project.expensor.data.PersonalExpensesDbHelper;

public class RecordExpense extends AppCompatActivity {
    private final String LOG_TAG = RecordExpense.class.getSimpleName();

    private String expenseDate;
    private Spinner expenseTypeSpinner;
    private Button saveExpenseButton;
    private EditText expenseAmountEditText;
    private EditText expenseDetailsEditText;
    private Button backButton;
    private DatePickerDialog datePickerDialog;
    private Button datePickerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_expense);

        this.setTitle("Record Expense");

        expenseTypeSpinner = findViewById(R.id.spinner_expenseType);
        saveExpenseButton = findViewById(R.id.button_saveExpense);
        expenseAmountEditText = findViewById(R.id.editText_expenseAmount);
        expenseDetailsEditText = findViewById(R.id.editText_expenseDatails);
        backButton = findViewById(R.id.button_back_recordExpense);
        datePickerButton = findViewById(R.id.datePickerButton);

        datePickerButton.setText(getTodaysDate());
        expenseDate = getTodaysDate();
        initDatePicker();
        PersonalExpensesDbHelper personalExpensesDbHelper = new PersonalExpensesDbHelper(this);
        List<String> expenseTypes = new ArrayList<>();
        expenseTypes = personalExpensesDbHelper.getExpenseTypes(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseTypeSpinner.setAdapter(arrayAdapter);

        saveExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expenseType = (String) expenseTypeSpinner.getSelectedItem();
                String expenseAmount = expenseAmountEditText.getText().toString().trim();
                //String expenseDate = expenseDateEditText.getText().toString().trim();
                String expenseDatails = expenseDetailsEditText.getText().toString().trim();
                if (TextUtils.isEmpty(expenseType)) {
                    Toast.makeText(RecordExpense.this, "Please select expense type", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(expenseAmount)) {
                    Toast.makeText(RecordExpense.this, "Please enter expense amount", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(expenseDate)) {
                    Toast.makeText(RecordExpense.this, "Please select date", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(expenseDatails)) {
                    Toast.makeText(RecordExpense.this, "Please enter expense details", Toast.LENGTH_SHORT).show();
                } else {
                    long result = personalExpensesDbHelper.insertExpense(expenseType, expenseAmount, expenseDate, expenseDatails);
                    if (result > 0) {
                        Toast.makeText(RecordExpense.this, "Expense Saved", Toast.LENGTH_SHORT).show();
                        Log.i(LOG_TAG, "Type: " + expenseType + "; Amount: " + expenseAmount + "; Date: " + expenseDate + "; Datails: " + expenseDatails);
                    } else {
                        Toast.makeText(RecordExpense.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                datePickerButton.setText(date);
                expenseDate = date;
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}