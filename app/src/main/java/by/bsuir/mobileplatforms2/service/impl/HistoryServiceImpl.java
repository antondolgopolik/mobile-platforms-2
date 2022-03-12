package by.bsuir.mobileplatforms2.service.impl;

import android.content.Context;

import java.util.List;

import by.bsuir.mobileplatforms2.datasource.dao.HistoryDao;
import by.bsuir.mobileplatforms2.datasource.dao.impl.HistoryDaoImpl;
import by.bsuir.mobileplatforms2.entity.History;
import by.bsuir.mobileplatforms2.entity.User;
import by.bsuir.mobileplatforms2.service.HistoryService;
import by.bsuir.mobileplatforms2.service.UserService;

public class HistoryServiceImpl implements HistoryService {
    private final UserService userService;
    private final HistoryDao historyDao;

    public HistoryServiceImpl(Context context) {
        userService = new UserServiceImpl(context);
        historyDao = new HistoryDaoImpl(context);
    }

    @Override
    public void saveInHistory(String username, String categoryName, Double price) {
        User user = userService.getUser(username);
        historyDao.create(user.getUserId(), categoryName, price);
    }

    @Override
    public List<History> getAllHistory() {
        return historyDao.read();
    }
}
