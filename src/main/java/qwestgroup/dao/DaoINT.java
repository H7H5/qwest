package qwestgroup.dao;

import qwestgroup.model.Purchase;

import java.util.List;

public interface DaoINT {
    List<Purchase> allPurchare();
    void add(List<Purchase> purchases);
    void deleteAll();
    Purchase getById(int id);
}
