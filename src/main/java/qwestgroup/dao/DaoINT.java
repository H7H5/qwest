package qwestgroup.dao;

import qwestgroup.model.Purchare;

import java.util.List;

public interface DaoINT {
    List<Purchare> allPurchare();
    void add(List<Purchare> purchares);
    void deleteAll();
    Purchare getById(int id);
}
