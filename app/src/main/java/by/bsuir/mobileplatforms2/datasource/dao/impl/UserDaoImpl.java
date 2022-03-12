package by.bsuir.mobileplatforms2.datasource.dao.impl;

import static by.bsuir.mobileplatforms2.datasource.DbContract.UserEntry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import by.bsuir.mobileplatforms2.datasource.DbHelper;
import by.bsuir.mobileplatforms2.datasource.dao.UserDao;
import by.bsuir.mobileplatforms2.entity.User;

public class UserDaoImpl implements UserDao {
    private final DbHelper dbHelper;

    public UserDaoImpl(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void create(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Object[] bindArgs = {username};
        db.execSQL("insert or ignore into " + UserEntry.TABLE_NAME + "(username) values(?)", bindArgs);
    }

    public User read(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {UserEntry.COLUMN_NAME_USERNAME};
        String selection = UserEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(
                UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, null
        );
        if (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_USERNAME));
            User user = new User();
            user.setUserId(userId);
            user.setUsername(username);
            return user;
        } else {
            return null;
        }
    }

    public User read(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {UserEntry._ID};
        String selection = UserEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(
                UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, null
        );
        if (cursor.moveToNext()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(UserEntry._ID));
            User user = new User();
            user.setUserId(userId);
            user.setUsername(username);
            return user;
        } else {
            return null;
        }
    }
}
