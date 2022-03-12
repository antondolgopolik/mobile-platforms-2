package by.bsuir.mobileplatforms2.datasource.dao;

import java.sql.Timestamp;

import by.bsuir.mobileplatforms2.entity.CachedImage;

public interface CachedImageDao {

    void create(String uid, Timestamp expires);

    CachedImage read(String uid);

    void update(CachedImage cachedImage);
}
