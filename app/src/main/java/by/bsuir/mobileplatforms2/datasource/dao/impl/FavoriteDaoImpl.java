package by.bsuir.mobileplatforms2.datasource.dao.impl;

import static by.bsuir.mobileplatforms2.datasource.DbContract.FavoriteEntry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import by.bsuir.mobileplatforms2.datasource.DbHelper;
import by.bsuir.mobileplatforms2.datasource.dao.FavoriteDao;
import by.bsuir.mobileplatforms2.entity.Favorite;

public class FavoriteDaoImpl implements FavoriteDao {
    private final DbHelper dbHelper;

    public FavoriteDaoImpl(Context context) {
        dbHelper = new DbHelper(context);
    }

    @Override
    public void create(int productId, String comment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Object[] bindArgs = {productId, comment};
        db.execSQL(
                "insert into " + FavoriteEntry.TABLE_NAME + "(" +
                        FavoriteEntry.COLUMN_NAME_PRODUCT_ID + ", " +
                        FavoriteEntry.COLUMN_NAME_COMMENT + ") values(?, ?)", bindArgs
        );
    }

    @Override
    public List<Favorite> read() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FavoriteEntry.TABLE_NAME, null, null, null,
                null, null, null
        );
        List<Favorite> favorites = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteEntry._ID));
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteEntry.COLUMN_NAME_PRODUCT_ID));
            String comment = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteEntry.COLUMN_NAME_COMMENT));
            Favorite favorite = new Favorite();
            favorite.setId(id);
            favorite.setProductId(productId);
            favorite.setComment(comment);
            favorites.add(favorite);
        }
        return favorites;
    }

    @Override
    public Favorite read(int productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {FavoriteEntry._ID, FavoriteEntry.COLUMN_NAME_COMMENT};
        String selection = FavoriteEntry.COLUMN_NAME_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId)};
        Cursor cursor = db.query(
                FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, null
        );
        if (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteEntry._ID));
            String comment = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteEntry.COLUMN_NAME_COMMENT));
            Favorite favorite = new Favorite();
            favorite.setId(id);
            favorite.setProductId(productId);
            favorite.setComment(comment);
            return favorite;
        } else {
            return null;
        }
    }

    @Override
    public void delete(int productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = FavoriteEntry.COLUMN_NAME_PRODUCT_ID + " = ?";
        String[] whereArgs = {String.valueOf(productId)};
        db.delete(FavoriteEntry.TABLE_NAME, whereClause, whereArgs);
    }
}
