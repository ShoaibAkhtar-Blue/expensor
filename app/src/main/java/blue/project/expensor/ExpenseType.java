package blue.project.expensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import blue.project.expensor.data.ExpenseTypeContract;
import blue.project.expensor.data.PersonalExpensesDbHelper;

public class ExpenseType extends AppCompatActivity {
    private final String LOG_TAG = ExpenseType.class.getSimpleName();
    private EditText expenseTypeEditText;
    private EditText expenseChargesEditText;
    private Button saveButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_type);

        this.setTitle("Expense Type");

        expenseTypeEditText = findViewById(R.id.editText_expenseType);
        expenseChargesEditText = findViewById(R.id.editText_expenseCharges);
        saveButton = findViewById(R.id.button_saveExpenseType);
        backButton = findViewById(R.id.button_backExpenseType);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expenseType = expenseTypeEditText.getText().toString().trim();
                String expenseCharges = expenseChargesEditText.getText().toString().trim();
                if (TextUtils.isEmpty(expenseType)) {
                    Toast.makeText(ExpenseType.this, "Enter expense type", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(expenseCharges)) {
                    Toast.makeText(ExpenseType.this, "Enter expense charges", Toast.LENGTH_SHORT).show();
                } else {
                    PersonalExpensesDbHelper personalExpensesDbHelper = new PersonalExpensesDbHelper(ExpenseType.this);
                    long result = personalExpensesDbHelper.insertExpenseType(expenseType, expenseCharges);
                    if (result > 0) {
                        Toast.makeText(ExpenseType.this, "Information Saved", Toast.LENGTH_SHORT).show();
                        ArrayList<String> expenseTypes = new ArrayList<>();
                        expenseTypes = personalExpensesDbHelper.getExpenseTypes(ExpenseType.this);
                        Log.i(LOG_TAG, expenseTypes.toString());
                    } else {
                        Toast.makeText(ExpenseType.this, "Error", Toast.LENGTH_SHORT).show();
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
}