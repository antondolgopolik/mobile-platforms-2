package by.bsuir.mobileplatforms2.service;

import java.util.List;

import by.bsuir.mobileplatforms2.entity.Favorite;

public interface FavoriteService {

    void addToFavorite(int productId, String comment);

    void removeFromFavorite(int productId);

    List<Favorite> getAllFavorites();

    boolean isAddedToFavorite(int productId);
}
