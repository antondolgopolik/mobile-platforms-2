package by.bsuir.mobileplatforms2.datasource.dao;

import by.bsuir.mobileplatforms2.entity.User;

public interface UserDao {

    void create(String username);

    User read(int userId);

    User read(String username);
}
