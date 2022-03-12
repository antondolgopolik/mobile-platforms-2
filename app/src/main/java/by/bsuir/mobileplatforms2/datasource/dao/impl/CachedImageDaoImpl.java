package by.bsuir.mobileplatforms2.datasource.dao.impl;

import static by.bsuir.mobileplatforms2.datasource.DbContract.CachedImageEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;

import by.bsuir.mobileplatforms2.datasource.DbHelper;
import by.bsuir.mobileplatforms2.datasource.dao.CachedImageDao;
import by.bsuir.mobileplatforms2.entity.CachedImage;

public class CachedImageDaoImpl implements CachedImageDao {
    private final DbHelper dbHelper;

    public CachedImageDaoImpl(Context context) {
        dbHelper = new DbHelper(context);
    }

    @Override
    public void create(String uid, Timestamp expires) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Object[] bindArgs = {uid, expires};
        db.execSQL(
                "insert into " + CachedImageEntry.TABLE_NAME + "(" +
                        CachedImageEntry.COLUMN_NAME_IMAGE_UID + ", " +
                        CachedImageEntry.COLUMN_NAME_EXPIRES + ") values(?, ?)", bindArgs
        );
    }

    @Override
    public CachedImage read(String uid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {CachedImageEntry._ID, CachedImageEntry.COLUMN_NAME_EXPIRES};
        String selection = CachedImageEntry.COLUMN_NAME_IMAGE_UID + " = ?";
        String[] selectionArgs = {uid};
        Cursor cursor = db.query(
                CachedImageEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, null
        );
        if (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(CachedImageEntry._ID));
            Timestamp expires = Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(CachedImageEntry.COLUMN_NAME_EXPIRES)));
            CachedImage cachedImage = new CachedImage();
            cachedImage.setId(id);
            cachedImage.setUid(uid);
            cachedImage.setExpires(expires);
            return cachedImage;
        } else {
            return null;
        }
    }

    @Override
    public void update(CachedImage cachedImage) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CachedImageEntry.COLUMN_NAME_EXPIRES, cachedImage.getExpires().toString());
        String whereClause = CachedImageEntry._ID + " = ?";
        String[] whereArgs = {String.valueOf(cachedImage.getId())};
        db.update(CachedImageEntry.TABLE_NAME, values, whereClause, whereArgs);
    }
}
