package by.bsuir.mobileplatforms2.datasource.dao.impl;

import static by.bsuir.mobileplatforms2.datasource.DbContract.HistoryEntry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import by.bsuir.mobileplatforms2.datasource.DbHelper;
import by.bsuir.mobileplatforms2.datasource.dao.HistoryDao;
import by.bsuir.mobileplatforms2.datasource.dao.UserDao;
import by.bsuir.mobileplatforms2.entity.History;

public class HistoryDaoImpl implements HistoryDao {
    private final DbHelper dbHelper;
    private final UserDao userDao;

    public HistoryDaoImpl(Context context) {
        dbHelper = new DbHelper(context);
        userDao = new UserDaoImpl(context);
    }

    @Override
    public void create(int userId, String categoryName, Double price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Object[] bindArgs = {userId, categoryName, price};
        db.execSQL(
                "insert into " + HistoryEntry.TABLE_NAME + "(" +
                        HistoryEntry.COLUMN_NAME_USER_ID + ", " +
                        HistoryEntry.COLUMN_NAME_CATEGORY + ", " +
                        HistoryEntry.COLUMN_NAME_PRICE + ") values(?, ?, ?)", bindArgs
        );
    }

    @Override
    public List<History> read() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                HistoryEntry.TABLE_NAME, null, null, null,
                null, null, null
        );
        List<History> histories = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(HistoryEntry._ID));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_USER_ID));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_CATEGORY));
            Double price = cursor.getDouble(cursor.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_PRICE));
            Timestamp createdAt = Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_CREATED_AT)));
            History history = new History();
            history.setId(id);
            history.setUser(userDao.read(userId));
            history.setCategory(category);
            history.setPrice(price);
            history.setCreatedAt(createdAt);
            histories.add(history);
        }
        return histories;
    }
}
