package by.bsuir.mobileplatforms2.datasource.dao;

import java.util.List;

import by.bsuir.mobileplatforms2.entity.Favorite;

public interface FavoriteDao {

    void create(int productId, String comment);

    List<Favorite> read();

    Favorite read(int productId);

    void delete(int productId);
}
