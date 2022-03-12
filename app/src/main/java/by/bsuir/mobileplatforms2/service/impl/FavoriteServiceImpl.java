package by.bsuir.mobileplatforms2.service.impl;

import android.content.Context;

import java.util.List;

import by.bsuir.mobileplatforms2.datasource.dao.FavoriteDao;
import by.bsuir.mobileplatforms2.datasource.dao.impl.FavoriteDaoImpl;
import by.bsuir.mobileplatforms2.entity.Favorite;
import by.bsuir.mobileplatforms2.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteDao favoriteDao;

    public FavoriteServiceImpl(Context context) {
        favoriteDao = new FavoriteDaoImpl(context);
    }

    @Override
    public void addToFavorite(int productId, String comment) {
        favoriteDao.create(productId, comment);
    }

    @Override
    public void removeFromFavorite(int productId) {
        favoriteDao.delete(productId);
    }

    @Override
    public List<Favorite> getAllFavorites() {
        return favoriteDao.read();
    }

    @Override
    public boolean isAddedToFavorite(int productId) {
        Favorite favorite = favoriteDao.read(productId);
        return favorite != null;
    }
}
