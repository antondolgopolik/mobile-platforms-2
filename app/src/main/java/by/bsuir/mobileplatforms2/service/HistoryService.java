package by.bsuir.mobileplatforms2.service;

import java.util.List;

import by.bsuir.mobileplatforms2.entity.History;

public interface HistoryService {

    void saveInHistory(String username, String categoryName, Double price);

    List<History> getAllHistory();
}
