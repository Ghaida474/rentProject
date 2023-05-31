package com.example.rentmyspot;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import android.util.Log;

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
    public static final String T1COL5 = "ReantedSeatingName";

    public static final String TABLENAME2 = "seating";
    public static final String T2COL1 = "SeatingID";
    public static final String T2COL2 = "username";
    public static final String T2COL3 = "SeatingName";
    public static final String T2COL4 = "SeatingCategory";
    public static final String T2COL5 = "SeatingPrice";
    public static final String T2COL6 = "SeatingDescription";
    public static final String T2COL7 = "seatingImage";

    // New RentedSeating table
    public static final String TABLENAME3 = "RentedSeating";
    public static final String T3COL1 = "RentedSeatingID";
    public static final String T3COL2 = "SeatingID";
    public static final String T3COL3 = "OwnerUsername";
    public static final String T3COL4 = "RenterUsername";

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
                + T2COL7 + " BLOB"+
                ")");

        // New RentedSeating table creation
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLENAME3 + "("
                + T3COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T3COL2 + " INTEGER,"
                + T3COL3 + " TEXT,"
                + T3COL4 + " TEXT,"
                + "FOREIGN KEY(" + T3COL2 + ") REFERENCES " + TABLENAME2 + "(" + T2COL1 + ")"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists " + TABLENAME1);
        sqLiteDatabase.execSQL("drop Table if exists " + TABLENAME2);
        sqLiteDatabase.execSQL("drop Table if exists " + TABLENAME3); // New RentedSeating table upgrade
        onCreate(sqLiteDatabase);
    }


    public boolean addSeating(Seating newSeating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(T2COL2, newSeating.getUserneme());
        cv.put(T2COL3, newSeating.getSname());
        cv.put(T2COL4, newSeating.getScategory());
        cv.put(T2COL5, newSeating.getSprice());
        cv.put(T2COL6, newSeating.getSdescription());
        cv.put(T2COL7, newSeating.getImageData());
        long insert = db.insert(TABLENAME2, null, cv);

        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean DeleteOne(Seating deleteSeating) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLENAME2, T2COL3 + "=?", new String[] { deleteSeating.getSname() });
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


    public List<Seating> SeatingList(String userneme) {
        List<Seating> returnList = new ArrayList<>();
        String queryString = "Select * from " + TABLENAME2 + " WHERE " +
                T2COL2 + " = '" + userneme + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                String SName = cursor.getString(2);
                String Scat = cursor.getString(3);
                int Sprice = cursor.getInt(4);
                String Sdes = cursor.getString(5);
                byte[] image = cursor.getBlob(6);

                Seating newSeat = new Seating(userneme, SName, Scat, Sprice, Sdes,image);
                returnList.add(newSeat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public List<Seating> ListALLseatings() {
        List<Seating> returnList = new ArrayList<>();
        String queryString = "Select * from " + TABLENAME2 ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(1);
                String SName = cursor.getString(2);
                String Scat = cursor.getString(3);
                int Sprice = cursor.getInt(4);
                String Sdes = cursor.getString(5);
                byte[] image = cursor.getBlob(6);

                Seating newSeat = new Seating(username, SName, Scat, Sprice, Sdes,image);
                returnList.add(newSeat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }
    public List<Seating> ListALLseatings(String currentUser) {
        List<Seating> seatingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLENAME2
                + " WHERE " + T2COL2 + " != ?"
                + " AND " + T2COL1 + " NOT IN (SELECT " + T3COL2
                + " FROM " + TABLENAME3 + ")", new String[]{currentUser});

        if (cursor.moveToFirst()) {
            do {

                    String username = cursor.getString(1);
                    String SName = cursor.getString(2);
                    String Scat = cursor.getString(3);
                    int Sprice = cursor.getInt(4);
                    String Sdes = cursor.getString(5);
                    byte[] image = cursor.getBlob(6);

                    Seating newSeat = new Seating(username, SName, Scat, Sprice, Sdes,image);
                    seatingList.add(newSeat);
                // Create a new Seating object and populate it with data from the cursor
                // Add the Seating object to the seatingList
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return seatingList;
    }
    public boolean rentSeating(Seating seating, String currentUser) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert the rented seating into the rented seating table
        ContentValues contentValues = new ContentValues();
        contentValues.put(T3COL2, seating.getSname());
        contentValues.put(T3COL3, currentUser);
        contentValues.put(T3COL4, seating.getUserneme()); // Assuming T3COL4 is the column for the owner's username in the rented seating table

        long insertResult = db.insert(TABLENAME3, null, contentValues);

        // If the insertion was successful, delete the seating from the available seatings table
        if (insertResult != -1) {
            int deleteResult = db.delete(TABLENAME2, T2COL2 + "=? AND " + T2COL3 + "=?", new String[]{seating.getSname(), seating.getUserneme()});

            if (deleteResult > 0) {
                // Seating was successfully rented (inserted into rented seating table and deleted from available seatings table)
                db.close();
                return true;
            } else {
                // An error occurred while deleting the seating from the available seatings table
                db.close();
                return false;
            }
        } else {
            // An error occurred while inserting the seating into the rented seating table
            db.close();
            return false;
        }

    }
    public List<Seating> getAllRentedSeatings(String currentUser) {
        List<Seating> rentedSeatingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Use a WHERE clause to filter the results based on the current user's username
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLENAME3 + " WHERE " + T3COL1 + " = ?", new String[]{currentUser});

        if (cursor.moveToFirst()) {
            do {
                String ownerUsername = cursor.getString(1);
                String seatingName = cursor.getString(2);
                String seatingCategory = cursor.getString(3);
                int seatingPrice = cursor.getInt(4);
                String seatingDescription = cursor.getString(5);
                byte[] imageData = cursor.getBlob(6);

                Seating rentedSeating = new Seating(ownerUsername, seatingName, seatingCategory, seatingPrice, seatingDescription, imageData);
                rentedSeatingList.add(rentedSeating);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return rentedSeatingList;
    }
    public boolean removeRentedSeating(Seating seating) {
        SQLiteDatabase db = this.getWritableDatabase();

        int deleteResult = db.delete(TABLENAME3, T3COL2 + "=? AND " + T3COL4 + "=?", new String[]{seating.getSname(), seating.getUserneme()});

        if (deleteResult > 0) {
            // Seating was successfully removed from the rented seatings table
            db.close();
            return true;
        } else {
            // An error occurred while deleting the seating from the rented seatings table
            db.close();
            return false;
        }
    }
}