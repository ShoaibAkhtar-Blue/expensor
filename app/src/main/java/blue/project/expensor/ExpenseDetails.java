package blue.project.expensor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import blue.project.expensor.data.PersonalExpensesDbHelper;

public class ExpenseDetails extends AppCompatActivity {
    private TableLayout tableLayout;
    private TextView date, detail, type, amount;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        this.setTitle("Expense Details");

        tableLayout = findViewById(R.id.tableLayout);
        backButton = findViewById(R.id.button_back_ExpenseDetails);
        /*
        date = findViewById(R.id.date);
        detail = findViewById(R.id.detail);
        type = findViewById(R.id.type);
        amount = findViewById(R.id.amount);

         */

        PersonalExpensesDbHelper personalExpensesDbHelper = new PersonalExpensesDbHelper(this);
        ArrayList<ArrayList<String>> expenseDetails = new ArrayList<>();
        expenseDetails = personalExpensesDbHelper.getExpenseDetails(this);
        if (expenseDetails != null) {
            ArrayList<String> dates = new ArrayList<>();
            ArrayList<String> details = new ArrayList<>();
            ArrayList<String> types = new ArrayList<>();
            ArrayList<String> amounts = new ArrayList<>();
            dates = expenseDetails.get(0);
            details = expenseDetails.get(1);
            types = expenseDetails.get(2);
            amounts = expenseDetails.get(3);
            for (int i = 0; i < dates.size(); i++) {
                View tableRow = LayoutInflater.from(this).inflate(R.layout.table_layout, null, false);
                date = tableRow.findViewById(R.id.date);
                detail = tableRow.findViewById(R.id.detail);
                type = tableRow.findViewById(R.id.type);
                amount = tableRow.findViewById(R.id.amount);

                date.setText(dates.get(i));
                detail.setText(details.get(i));
                type.setText(types.get(i));
                amount.setText(amounts.get(i));
                tableLayout.addView(tableRow);
            }
        } else {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}