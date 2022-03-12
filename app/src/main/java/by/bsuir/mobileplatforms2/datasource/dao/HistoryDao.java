package by.bsuir.mobileplatforms2.datasource.dao;

import java.util.List;

import by.bsuir.mobileplatforms2.entity.History;

public interface HistoryDao {

    void create(int userId, String categoryName, Double price);

    List<History> read();
}
