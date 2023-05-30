package com.example.rentmyspot;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
//
public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "RentMySpot.db";
    public static final String TABLENAME1 = "users";
    public static final String T1COL1 = "username";
    public static final String T1COL2 = "password";
    public static final String T1COL3 = "age";
    public static final String T1COL4 = "email";
    public static final String T1COL5 = "rentedSeatingName";

//    ----------------------------------------------------------

    public static final String TABLENAME2 = "seating";
    public static final String T2COL1 = "seatingID";
    public static final String T2COL2 = "username";
    public static final String T2COL3 = "seatingName";
    public static final String T2COL4 = "seatingCategory";
    public static final String T2COL5 = "seatingPrice";
    public static final String T2COL6 = "seatingDescription";
    public static final String T2COL7 = "seatingImage";
    public static final String T2COL8 = "rented";

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLENAME1 + "(" + T1COL1 + " TEXT PRIMARY KEY," + T1COL2 + " TEXT," + T1COL3 + " INTEGER," + T1COL4 + " TEXT," + T1COL5 + " TEXT"+ ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLENAME2 + "(" + T2COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T2COL2 + " TEXT,"
                + T2COL3 + " TEXT,"
                + T2COL4 + " TEXT,"
                + T2COL5 + " INTEGER,"
                + T2COL6 + " TEXT,"
                + T2COL7 + " BLOB,"
                + T2COL8 + " INTEGER"+
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists " + TABLENAME1);
        sqLiteDatabase.execSQL("drop Table if exists " + TABLENAME2);
        onCreate(sqLiteDatabase);
    }

    public boolean addSeating(Seating newSeating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(T2COL2, newSeating.getUsername());
        cv.put(T2COL3, newSeating.getSName());
        cv.put(T2COL4, newSeating.getSCategory());
        cv.put(T2COL5, newSeating.getSPrice());
        cv.put(T2COL6, newSeating.getSDescription());
        cv.put(T2COL7, newSeating.getImageData());
        cv.put(T2COL8, newSeating.isRented() ? 1 : 0);
        long insert = db.insert(TABLENAME2, null, cv);

        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean DeleteOne(Seating deleteSeating) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLENAME2, T2COL3 + "=?", new String[] { deleteSeating.getUsername() });
        if (deletedRows > 0) {
            return true;
        } else {
            // nothing happens. no one is added.
            return false;
        }
        //close
    }

    public Boolean insertData(String username, String password ,String email, String age) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(T1COL1, username);
        contentValues.put(T1COL2, password);
        contentValues.put(T1COL3, age);
        contentValues.put(T1COL4, email);

        long result = MyDB.insert(TABLENAME1, null, contentValues);

        if (result == -1)
            return false;
        return true;
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + TABLENAME1 + " where " + T1COL1 + " = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + TABLENAME1 + " where " + T1COL4 + " = ?", new String[]{email});
        if (cursor.getCount() > 0 )
            return true;
        return false;
    }



    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from " + TABLENAME1 + " where " + T1COL1 + " = ? and " + T1COL2 + " = ?", new String[]{username, password});
        if (cursor.getCount() > 0) return true;
        return false;
    }



    public List<Seating> SeatingList(String username) {
        List<Seating> returnList = new ArrayList<>();
        String queryString = "Select * from " + TABLENAME2 + " WHERE " +
                T2COL2 + " = '" + username + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                int seatingID = cursor.getInt(0);
                String sName = cursor.getString(2);
                String sCategory = cursor.getString(3);
                int sPrice = cursor.getInt(4);
                String sDescription = cursor.getString(5);
                byte[] imageData = cursor.getBlob(6);
                boolean rented = cursor.getInt(7) == 1;

                Seating newSeat = new Seating(username, sName, sCategory, sPrice, sDescription, imageData, rented);
                returnList.add(newSeat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public List<Seating> ListALLseatings() {
        List<Seating> returnList = new ArrayList<>();
        String queryString = "Select * from " + TABLENAME2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(1);
                String sName = cursor.getString(2);
                String sCategory = cursor.getString(3);
                int sPrice = cursor.getInt(4);
                String sDescription = cursor.getString(5);
                byte[] imageData = cursor.getBlob(6);
                boolean rented = cursor.getInt(7) == 1;

                Seating newSeat = new Seating(username, sName, sCategory, sPrice, sDescription, imageData, rented);
                returnList.add(newSeat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean updateRentedStatus(int seatingID, boolean rented) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(T2COL8, rented ? 1 : 0);

        int update = db.update(TABLENAME2, cv, T2COL1 + " = ?", new String[]{String.valueOf(seatingID)});

        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }
}