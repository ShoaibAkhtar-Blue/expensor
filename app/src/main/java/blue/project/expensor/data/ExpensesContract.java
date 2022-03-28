package blue.project.expensor.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ExpensesContract {
    // Making constructor private so that the object of this class can not be created
    private ExpensesContract() {
    }

    /**
     * Parts of Content URI.
     */
    public static final String CONTENT_AUTHORITY = "blue.project.expensor";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Inner class that defines the constants for the table.
     */
    public static final class ExpensesEntry implements BaseColumns {
        public static final String TABLE_NAME = "expenses";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_EXPENSE_TYPE = "expense_type";
        public static final String COLUMN_EXPENSE_AMOUNT = "expense_amount";
        public static final String COLUMN_EXPENSE_DATE = "expense_date";
        public static final String COLUMN_EXPENSE_DETAILS = "expense_details";

        // Complete Content URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);

        /**
         * MIME type of Content URI for the List of passwords.
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        /**
         * MIME type of Content URI for a single password.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }
}
