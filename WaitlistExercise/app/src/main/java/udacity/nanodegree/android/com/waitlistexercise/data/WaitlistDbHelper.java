package udacity.nanodegree.android.com.waitlistexercise.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ramon on 15/07/17.
 */

public class WaitlistDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "waitlist.db";

    public static final int DATABASE_VERSION = 1;

    public WaitlistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
      final  String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE ".concat(WaitlistContract.WaitlistEntry.TABLE_NAME).concat(" (")
                .concat(WaitlistContract.WaitlistEntry._ID).concat(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ")
                .concat(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME).concat(" TEXT NOT NULL, ")
                .concat(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE).concat(" INTEGER NOT NULL, ")
                .concat(WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP).concat(" TIMESTAMP DEFAULT CURRENT_TIMESTAMP );");

        // Criando a tabela
        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
     }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WaitlistContract.WaitlistEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
