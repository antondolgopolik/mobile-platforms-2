package by.bsuir.mobileplatforms2.service.impl;

import android.content.Context;

import by.bsuir.mobileplatforms2.datasource.dao.UserDao;
import by.bsuir.mobileplatforms2.datasource.dao.impl.UserDaoImpl;
import by.bsuir.mobileplatforms2.entity.User;
import by.bsuir.mobileplatforms2.service.UserService;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(Context context) {
        userDao = new UserDaoImpl(context);
    }

    @Override
    public void createUser(String username) {
        userDao.create(username);
    }

    @Override
    public User getUser(String username) {
        return userDao.read(username);
    }
}
