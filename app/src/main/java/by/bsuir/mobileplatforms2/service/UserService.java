package by.bsuir.mobileplatforms2.service;

import by.bsuir.mobileplatforms2.entity.User;

public interface UserService {

    void createUser(String username);

    User getUser(String username);
}
