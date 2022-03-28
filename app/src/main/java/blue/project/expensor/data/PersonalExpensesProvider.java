package blue.project.expensor.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PersonalExpensesProvider extends ContentProvider {
    private static final String LOG_TAG = PersonalExpensesProvider.class.getSimpleName();

    private PersonalExpensesDbHelper personalExpensesDbHelper;

    // URI matcher code for Content URI for expense_type table
    private static final int EXPENSE_TYPES =100;

    // URI matcher code for Content URI for a single record form expense_type table
    private static final int EXPENSE_TYPE = 101;

    // URI matcher code for Content URI for expenses table
    private static final int EXPENSES =200;

    // URI matcher code for Content URI for a single record form expenses table
    private static final int EXPENSE = 201;

    // URI matcher object which match the Content URI with the URI matcher code
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static Initializer
    static {
        sUriMatcher.addURI(ExpenseTypeContract.CONTENT_AUTHORITY, ExpenseTypeContract.ExpenseTypeEntry.TABLE_NAME, EXPENSE_TYPES);
        sUriMatcher.addURI(ExpenseTypeContract.CONTENT_AUTHORITY, ExpenseTypeContract.ExpenseTypeEntry.TABLE_NAME + "/#", EXPENSE_TYPE);
        sUriMatcher.addURI(ExpensesContract.CONTENT_AUTHORITY, ExpensesContract.ExpensesEntry.TABLE_NAME, EXPENSES);
        sUriMatcher.addURI(ExpensesContract.CONTENT_AUTHORITY, ExpensesContract.ExpensesEntry.TABLE_NAME + "/#", EXPENSE);
    }

    /**
     * Initialize PersonalExpensesProvider
     * @return
     */
    @Override
    public boolean onCreate() {
        personalExpensesDbHelper = new PersonalExpensesDbHelper(getContext());
        return true;
    }

    /**
     * This method quries the database with provided URI.
     * @param uri   Content URI
     * @param strings   Projection
     * @param s Selection
     * @param strings1  Selection Arguments
     * @param s1    Sort Order
     * @return  Cursor object containing data
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;

        // Getting database in read able mode
        SQLiteDatabase database = personalExpensesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match) {
            case EXPENSE_TYPES:
                try {
                    cursor = database.query(ExpenseTypeContract.ExpenseTypeEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                } catch (Exception e) {
                    Log.i(LOG_TAG, e.getMessage());
                }
                break;
            case EXPENSE_TYPE:
                String id1 = String.valueOf(ContentUris.parseId(uri));
                String selection = ExpenseTypeContract.ExpenseTypeEntry.COLUMN_ID + " = ?";
                String[] selectionArgs = {id1};
                cursor = database.query(ExpenseTypeContract.ExpenseTypeEntry.TABLE_NAME, strings, selection, selectionArgs, null, null, s1);
                break;
            case EXPENSES:
                try {
                    cursor = database.query(ExpensesContract.ExpensesEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                } catch (Exception e) {
                    Log.i(LOG_TAG, e.getMessage());
                }
                break;
            case EXPENSE:
                String id2 = String.valueOf(ContentUris.parseId(uri));
                String selection2 = ExpensesContract.ExpensesEntry.COLUMN_ID + " = ?";
                String[] selectionArgs2 = {id2};
                cursor = database.query(ExpensesContract.ExpensesEntry.TABLE_NAME, strings, selection2, selectionArgs2, null, null, s1);
                break;
            default:
                throw new IllegalArgumentException("Error! Unknown URI: " + uri + " with match code: " + match);
        }
        // Notifying the cursor that data has been changed in the database
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case EXPENSE_TYPES:
                return ExpenseTypeContract.ExpenseTypeEntry.CONTENT_LIST_TYPE;
            case EXPENSE_TYPE:
                return ExpenseTypeContract.ExpenseTypeEntry.CONTENT_ITEM_TYPE;
            case EXPENSES:
                return ExpensesContract.ExpensesEntry.CONTENT_LIST_TYPE;
            case EXPENSE:
                return ExpensesContract.ExpensesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri + " with match code: " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        if (match == EXPENSE_TYPES) {
            SQLiteDatabase database = personalExpensesDbHelper.getWritableDatabase();
            long rowID = database.insert(ExpenseTypeContract.ExpenseTypeEntry.TABLE_NAME, null, contentValues);

            // Notifying that data has been changed in the database
            getContext().getContentResolver().notifyChange(uri, null);

            return ContentUris.withAppendedId(uri, rowID);
        } else if (match == EXPENSES) {
            SQLiteDatabase database = personalExpensesDbHelper.getWritableDatabase();
            long rowID = database.insert(ExpensesContract.ExpensesEntry.TABLE_NAME, null, contentValues);

            // Notifying that data has been changed in the database
            getContext().getContentResolver().notifyChange(uri, null);

            return ContentUris.withAppendedId(uri, rowID);
        } else {
            throw new IllegalArgumentException("Error! Data is not inserted into the database.");
        }
    }

    /**
     * This method deletes the data from the database at provided URI.
     * @param uri   URI
     * @param s Selection
     * @param strings   Selection Arguments
     * @return  Number of rows affected
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        // Getting database in writable mode
        /*
        SQLiteDatabase database = passwordsDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case SAVED_PASSWORDS:
                rowsDeleted = database.delete(PasswordsContract.PasswordsEntry.TABLE_NAME, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);  // Notifying that the data has been changed
                return rowsDeleted;
            case SAVED_PASSWORD:
                String id = String.valueOf(ContentUris.parseId(uri));
                String selection = PasswordsContract.PasswordsEntry.COLUMN_ID + " = ?";
                String[] selectionArgs = {id};
                rowsDeleted = database.delete(PasswordsContract.PasswordsEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);  // Notifying that the data has been changed
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }

         */
        return 0;
    }

    /**
     * This method updates the record in the database.
     * @param uri - URI
     * @param contentValues - Content Values
     * @param s - Selection
     * @param strings - Selection Args
     * @return Number of rows affected
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        // Getting database in writable mode
        /*
        SQLiteDatabase database = passwordsDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        if (match == SAVED_PASSWORD) {
            String id = String.valueOf(ContentUris.parseId(uri));
            String selection = PasswordsContract.PasswordsEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = {id};
            int rowsUpdated = database.update(PasswordsContract.PasswordsEntry.TABLE_NAME, contentValues, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return rowsUpdated;
        } else {
            throw new IllegalArgumentException("Error! Unable to update the information");
        }

         */
        return 0;
    }
}
