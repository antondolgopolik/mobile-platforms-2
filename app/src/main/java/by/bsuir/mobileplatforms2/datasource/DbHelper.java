package by.bsuir.mobileplatforms2.datasource;

import static by.bsuir.mobileplatforms2.datasource.DbContract.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "app.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // users
        db.execSQL("create table " + UserEntry.TABLE_NAME + "\n" +
                "(\n" +
                "    " + UserEntry._ID + "  INTEGER\n" +
                "        constraint users_pk\n" +
                "            primary key autoincrement,\n" +
                "    " + UserEntry.COLUMN_NAME_USERNAME + " text not null\n" +
                ");");
        db.execSQL("create unique index users_username_uindex\n" +
                "    on " + UserEntry.TABLE_NAME + " (" + UserEntry.COLUMN_NAME_USERNAME + ");");
        // history
        db.execSQL("create table " + HistoryEntry.TABLE_NAME + "\n" +
                "(\n" +
                "    " + HistoryEntry._ID + " integer\n" +
                "        constraint history_pk\n" +
                "            primary key autoincrement,\n" +
                "    " + HistoryEntry.COLUMN_NAME_USER_ID + "    integer\n" +
                "        references " + UserEntry.TABLE_NAME + "\n" +
                "            on update cascade on delete cascade,\n" +
                "    " + HistoryEntry.COLUMN_NAME_CATEGORY + "   text not null,\n" +
                "    " + HistoryEntry.COLUMN_NAME_PRICE + "      real,\n" +
                "    " + HistoryEntry.COLUMN_NAME_CREATED_AT + " timestamp default CURRENT_TIMESTAMP not null\n" +
                ");");
        // cached images
        db.execSQL("create table " + CachedImageEntry.TABLE_NAME + "\n" +
                "(\n" +
                "    " + CachedImageEntry._ID + " integer\n" +
                "        constraint cached_images_pk\n" +
                "            primary key autoincrement,\n" +
                "    " + CachedImageEntry.COLUMN_NAME_IMAGE_UID + "       text not null,\n" +
                "    " + CachedImageEntry.COLUMN_NAME_EXPIRES + "     timestamp not null\n" +
                ");");
        db.execSQL("create unique index cached_images_image_uid_uindex\n" +
                "    on " + CachedImageEntry.TABLE_NAME + " (" + CachedImageEntry.COLUMN_NAME_IMAGE_UID + ");");
        // favorites
        db.execSQL("create table " + FavoriteEntry.TABLE_NAME + "\n" +
                "(\n" +
                "    " + FavoriteEntry._ID + " integer\n" +
                "        constraint favorites_pk\n" +
                "            primary key autoincrement,\n" +
                "    " + FavoriteEntry.COLUMN_NAME_PRODUCT_ID + "  integer not null,\n" +
                "    " + FavoriteEntry.COLUMN_NAME_COMMENT + "     text\n" +
                ");");
        db.execSQL("create unique index favorites_product_id_uindex\n" +
                "    on " + FavoriteEntry.TABLE_NAME + " (" + FavoriteEntry.COLUMN_NAME_PRODUCT_ID + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + UserEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + HistoryEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + CachedImageEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
