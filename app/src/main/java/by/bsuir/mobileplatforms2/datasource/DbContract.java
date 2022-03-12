package by.bsuir.mobileplatforms2.datasource;

import android.provider.BaseColumns;

public final class DbContract {

    private DbContract() {
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USERNAME = "username";
    }

    public static class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_CREATED_AT = "created_at";
    }

    public static class CachedImageEntry implements BaseColumns {
        public static final String TABLE_NAME = "cached_images";
        public static final String COLUMN_NAME_IMAGE_UID = "image_uid";
        public static final String COLUMN_NAME_EXPIRES = "expires";
    }

    public static class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME_PRODUCT_ID = "product_id";
        public static final String COLUMN_NAME_COMMENT = "comment";
    }
}
