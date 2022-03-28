package blue.project.expensor.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PersonalExpensesDbHelper extends SQLiteOpenHelper {
    private final String LOG_TAG = PersonalExpensesDbHelper.class.getSimpleName();
    // Constants for database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "personal_expenses.db";
    private static final String CREATE_TABLE_EXPENSE_TYPE = "CREATE TABLE " + ExpenseTypeContract.ExpenseTypeEntry.TABLE_NAME + " (" +
            ExpenseTypeContract.ExpenseTypeEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ExpenseTypeContract.ExpenseTypeEntry.COLUMN_EXPENSE_TYPE + " TEXT NOT NULL UNIQUE, " +
            ExpenseTypeContract.ExpenseTypeEntry.COLUMN_EXPENSE_CHARGES + " TEXT)";
    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE " + ExpensesContract.ExpensesEntry.TABLE_NAME + " (" +
            ExpensesContract.ExpensesEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_TYPE + " TEXT NOT NULL, " +
            ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_AMOUNT + " TEXT NOT NULL, " +
            ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_DATE + " TEXT NOT NULL, " +
            ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_DETAILS + " TEXT NOT NULL)";
    private static final String SQL_DELETE_ENTRIES_TABLE_EXPENSE_TYPE = "DROP TABLE IF EXISTS " + ExpenseTypeContract.ExpenseTypeEntry.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_TABLE_EXPENSES = "DROP TABLE IF EXISTS " + ExpensesContract.ExpensesEntry.TABLE_NAME;

    public PersonalExpensesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_EXPENSE_TYPE);
        sqLiteDatabase.execSQL(CREATE_TABLE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO: In case of upgrading the database, implement this method.
    }

    /*
    public Boolean insertExpenseType(Context context, String expenseType, String expenseCharges) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", expenseType);
        contentValues.put("expense_charges", expenseCharges);
        try {
            sqLiteDatabase.insert(ExpenseTypeContract.ExpenseTypeEntry.TABLE_NAME, null, contentValues);
            return true;
        } catch (Exception exception) {
            String msg = exception.getMessage();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

     */

    public long insertExpenseType(String expenseType, String expenseCharges) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", expenseType);
        contentValues.put("expense_charges", expenseCharges);
        return sqLiteDatabase.insert(ExpenseTypeContract.ExpenseTypeEntry.TABLE_NAME, null, contentValues);
    }

    public ArrayList<String> getExpenseTypes(Context context) {
        String table = ExpenseTypeContract.ExpenseTypeEntry.TABLE_NAME;
        String[] columns = {ExpenseTypeContract.ExpenseTypeEntry.COLUMN_EXPENSE_TYPE};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.query(table, null, null, null, null, null, null );
            ArrayList<String> expenseTypes = new ArrayList<>();
            while (cursor.moveToNext()) {
                String type = cursor.getString(cursor.getColumnIndex(ExpenseTypeContract.ExpenseTypeEntry.COLUMN_EXPENSE_TYPE));
                Log.i(LOG_TAG, type);
                expenseTypes.add(type);
            }
            return expenseTypes;
        } catch (Exception exception) {
            String msg = exception.getMessage();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public long insertExpense(String expenseType, String expenseAmount, String expenseDate, String expenseDetails) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_TYPE, expenseType);
        contentValues.put(ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_AMOUNT, expenseAmount);
        contentValues.put(ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_DATE, expenseDate);
        contentValues.put(ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_DETAILS, expenseDetails);
        return sqLiteDatabase.insert(ExpensesContract.ExpensesEntry.TABLE_NAME, null, contentValues);
    }

    public ArrayList<ArrayList<String>> getExpenseDetails(Context context) {
        String table = ExpensesContract.ExpensesEntry.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.query(table, null, null, null, null, null, null );
            ArrayList<ArrayList<String>> expenseDetails = new ArrayList<>();
            ArrayList<String> dates = new ArrayList<>();
            ArrayList<String> details = new ArrayList<>();
            ArrayList<String> types = new ArrayList<>();
            ArrayList<String> amounts = new ArrayList<>();
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex(ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_DATE));
                String detail = cursor.getString(cursor.getColumnIndex(ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_DETAILS));
                String type = cursor.getString(cursor.getColumnIndex(ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_TYPE));
                String amount = cursor.getString(cursor.getColumnIndex(ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_AMOUNT));
                dates.add(date);
                details.add(detail);
                types.add(type);
                amounts.add(amount);
            }
            expenseDetails.add(dates);
            expenseDetails.add(details);
            expenseDetails.add(types);
            expenseDetails.add(amounts);
            return expenseDetails;
        } catch (Exception exception) {
            String msg = exception.getMessage();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public double getTotalExpenses(Context context) {
        String table = ExpensesContract.ExpensesEntry.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.query(table, null, null, null, null, null, null );
            ArrayList<String> amounts = new ArrayList<>();
            while (cursor.moveToNext()) {
                String amount = cursor.getString(cursor.getColumnIndex(ExpensesContract.ExpensesEntry.COLUMN_EXPENSE_AMOUNT));
                amounts.add(amount);
            }
            double totalExpenses = 0;
            for (int i = 0; i < amounts.size(); i++) {
                double amount = Double.parseDouble(amounts.get(i));
                totalExpenses = totalExpenses + amount;
            }
            return totalExpenses;
        } catch (Exception exception) {
            String msg = exception.getMessage();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            return 0;
        }
    }
}
